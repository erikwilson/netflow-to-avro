package com.company.netflow;

import com.company.util.BinaryData;
import com.company.util.DataTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class NetflowV5Header extends BinaryData
{

    public NetflowV5Header()
    {
        super(24, Arrays.asList(
            Map.entry( "version", DataTypes.UINT16 ),          // 0-1
            Map.entry( "count", DataTypes.UINT16 ),            // 2-3
            Map.entry( "sysUptime", DataTypes.UINT32 ),        // 4-7
            Map.entry( "unixSecs", DataTypes.UINT32 ),         // 8-11
            Map.entry( "unixNanoSecs", DataTypes.UINT32 ),     // 12-15
            Map.entry( "flowSequence", DataTypes.UINT32 ),     // 16-19
            Map.entry( "engineType", DataTypes.UINT8 ),        // 20
            Map.entry( "engineId", DataTypes.UINT8 ),          // 21
            Map.entry( "samplingInterval", DataTypes.UINT16 )  // 22-23
        ) );
    }

    @Override
    public void parse( byte[] data )
    {
        super.parse( data );
        int version = get( "version") ;
        if ( version != 5 )
        {
            throw new Error( "invalid Netflow header, expected version 5, got version: " + version );
        }
    }
}
