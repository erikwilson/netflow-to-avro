package com.company;

import com.fakecompany.flow.Flow;
import org.apache.avro.Schema;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class NetflowV5Packet extends BinaryData {

    public NetflowV5Packet() {
        super(48, new ArrayList<>(Arrays.asList(
            Map.entry("srcAddr", DataTypes.UINT32),     // 0-3
            Map.entry("dstAddr", DataTypes.UINT32),     // 4-7
            Map.entry("nexthop", DataTypes.UINT32),     // 8-11
            Map.entry("inputIface", DataTypes.UINT16),  // 12-13
            Map.entry("outputIface", DataTypes.UINT16), // 14-15
            Map.entry("packets", DataTypes.UINT32),     // 16-19
            Map.entry("octets", DataTypes.UINT32),      // 20-23
            Map.entry("first", DataTypes.UINT32),       // 24-27
            Map.entry("last", DataTypes.UINT32),        // 28-31
            Map.entry("srcPort", DataTypes.UINT16),     // 32-33
            Map.entry("dstPort", DataTypes.UINT16),     // 34-35
            Map.entry("pad1", DataTypes.UINT8),         // 36
            Map.entry("tcpFlags", DataTypes.UINT8),     // 37
            Map.entry("protocol", DataTypes.UINT8),     // 38
            Map.entry("tos", DataTypes.UINT8),          // 39
            Map.entry("srcAS", DataTypes.UINT16),       // 40-41
            Map.entry("dstAS", DataTypes.UINT16),       // 42-43
            Map.entry("srcMask", DataTypes.UINT8),      // 44
            Map.entry("dstMask", DataTypes.UINT8),      // 45
            Map.entry("pad2", DataTypes.UINT16))        // 46-47
        ),
        new HashMap<String,Serializer>(){{
            put("srcAddr", addr->Util.addressToString((long)addr));
            put("dstAddr", addr->Util.addressToString((long)addr));
            put("nexthop", addr->Util.addressToString((long)addr));
        }});
    }

    public void toAvroFlow(Flow flow) {
        List<Schema.Field> fields = Flow.getClassSchema().getFields();
        for (int i=2; i<fields.size(); i++) {
            Schema.Field field = fields.get(i);
            Object value = packetData.get(field.name());
            if (value == null) throw new Error("Netflow packet will not set null for Flow key " + field.name());
            flow.put(i,value);
        }
    }
}
