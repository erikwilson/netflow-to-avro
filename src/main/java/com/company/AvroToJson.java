package com.company;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;

import java.io.File;

public class AvroToJson
{
    public static void main( String[] args ) throws Exception
    {
        File inputFile = new File( args[ 0 ] );
        DataFileReader< GenericRecord > reader = new DataFileReader<>( inputFile, new GenericDatumReader<>() );

        System.out.println( "[" );

        while ( reader.hasNext() )
        {
            System.out.print( "  " + reader.next() );

            if ( reader.hasNext() )
            {
                System.out.println( "," );
            }
            else
            {
                System.out.println();
            }
        }

        System.out.println( "]" );
    }
}
