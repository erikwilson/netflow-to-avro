package com.fakecompany;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class NetflowV5Packet2 extends BinaryData {

    public NetflowV5Packet2() {
        super(48, new ArrayList<>(Arrays.asList(
            Map.entry("srcaddr", DataTypes.UINT32),    // 0-3
            Map.entry("dstaddr", DataTypes.UINT32),    // 4-7
            Map.entry("nexthop", DataTypes.UINT32),    // 8-11
            Map.entry("input", DataTypes.UINT16),      // 12-13
            Map.entry("output", DataTypes.UINT16),     // 14-15
            Map.entry("dPkts", DataTypes.UINT32),      // 16-19
            Map.entry("dOctets", DataTypes.UINT32),    // 20-23
            Map.entry("First", DataTypes.UINT32),      // 24-27
            Map.entry("Last", DataTypes.UINT32),       // 28-31
            Map.entry("srcport", DataTypes.UINT16),    // 32-33
            Map.entry("dstport", DataTypes.UINT16),    // 34-35
            Map.entry("pad1", DataTypes.UINT8),        // 36
            Map.entry("tcp_flags", DataTypes.UINT8),   // 37
            Map.entry("prot", DataTypes.UINT8),        // 38
            Map.entry("tos", DataTypes.UINT8),         // 39
            Map.entry("src_as", DataTypes.UINT16),     // 40-41
            Map.entry("dst_as", DataTypes.UINT16),     // 42-43
            Map.entry("src_mask", DataTypes.UINT8),    // 44
            Map.entry("dst_mask", DataTypes.UINT8),    // 45
            Map.entry("pad2", DataTypes.UINT16))       // 46-47
        ),
        new HashMap<String,Serializer>(){{
            put("srcaddr", addr->Util.addressToString((long)addr));
            put("dstaddr", addr->Util.addressToString((long)addr));
            put("nexthop", addr->Util.addressToString((long)addr));
        }});
    }
}
