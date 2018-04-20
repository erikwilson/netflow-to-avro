package com.fakecompany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class NetflowV5Header2 extends BinaryData {

    public NetflowV5Header2() {
        super(24, new ArrayList<>(Arrays.asList(
            Map.entry("version", DataTypes.UINT16),          // 0-1
            Map.entry("count", DataTypes.UINT16),            // 2-3
            Map.entry("sysUptime", DataTypes.UINT32),        // 4-7
            Map.entry("unixSecs", DataTypes.UINT32),         // 8-11
            Map.entry("unixNanoSecs", DataTypes.UINT32),     // 12-15
            Map.entry("flowSequence", DataTypes.UINT32),     // 16-19
            Map.entry("engineType", DataTypes.UINT8),        // 20
            Map.entry("engineId", DataTypes.UINT8),          // 21
            Map.entry("samplingInterval", DataTypes.UINT16)  // 22-23
        )));
    }
}
