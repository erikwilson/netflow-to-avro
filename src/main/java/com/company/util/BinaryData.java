package com.company.util;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BinaryData
{
    protected Map< String, Object > packetData;
    private List< Map.Entry< String, DataTypes > > entryList;
    private Map< String, Serializer > serializers = new HashMap<>();
    private byte[] data;
    private String name;

    public interface Serializer
    {
        String toString( Object a );
    }

    public BinaryData( int entryLength, List< Map.Entry< String, DataTypes > > entryList )
    {
        this.init( entryLength, entryList );
    }

    public BinaryData( int entryLength, List< Map.Entry< String, DataTypes > > entryList,
                       Map< String, Serializer > serializers)
    {
        this.serializers = serializers;
        this.init( entryLength, entryList );
    }

    private void init( int entryLength, List< Map.Entry< String, DataTypes > > entryList)
    {
        this.name = this.getClass().getSimpleName();
        this.entryList = entryList;

        int calculatedSize = entryList.stream().mapToInt(
            entry ->
            {
                switch( entry.getValue() )
                {
                    case UINT8:  return 1;
                    case UINT16: return 2;
                    case UINT32: return 4;
                }
                throw new Error("invalid type");
            }
        ).sum();

        if ( calculatedSize != entryLength )
        {
            throw new Error( "expected binary entry length " + entryLength + " does not match calculated size " + calculatedSize );
        }
        data = new byte[entryLength];
    }

    public void read( java.io.InputStream stream )
            throws java.io.IOException
    {
        int bytesRead = stream.readNBytes( data, 0, data.length );
        if ( bytesRead != data.length )
        {
            throw new java.io.IOException( "read " + bytesRead + "/" + data.length + " bytes" );
        }
        this.parse( data );
    }

    public void parse( byte[] data )
    {
        UnsignedDataReader reader = new UnsignedDataReader( ByteBuffer.wrap( data ) );

        packetData = entryList.stream().collect( Collectors.toMap(
            Map.Entry::getKey,
            entry ->
            {
                switch( entry.getValue() )
                {
                    case UINT8:  return reader.getUnsignedByte();
                    case UINT16: return reader.getUnsignedShort();
                    case UINT32: return reader.getUnsignedInt();
                }
                throw new Error("invalid type");
            },
            ( e1, e2 ) -> e1,
            LinkedHashMap::new ) );
    }

    // code smell
    public <T> T get( String key )
    {
        return (T)packetData.get( key );
    }

    public String toString()
    {
        return name + ":" + packetData.entrySet().stream().collect( Collector.of(
            ArrayList< String >::new,
            ( result, entry ) ->
            {
                String key = entry.getKey();
                Object value = entry.getValue();
                Serializer serial = serializers.get( key );

                if ( serial != null )
                {
                    result.add( serial.toString( value ) );
                }
                else
                {
                    result.add( String.valueOf( value ) );
                }
            },
            ( result1, result2 ) ->
            {
                result1.addAll( result2 );
                return result1;
            },
            result -> String.join( ",", result )
        ));
    }
}
