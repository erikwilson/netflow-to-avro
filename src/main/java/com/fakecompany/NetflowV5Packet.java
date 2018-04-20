package com.fakecompany;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NetflowV5Packet {

    long srcaddr;    // 0-3
    long dstaddr;    // 4-7
    long nexthop;    // 8-11
    int input;       // 12-13
    int output;      // 14-15
    long dPkts;      // 16-19
    long dOctets;    // 20-23
    long First;      // 24-27
    long Last;       // 28-31
    int srcport;     // 32-33
    int dstport;     // 34-35
    short pad1;      // 36
    short tcp_flags; // 37
    short prot;      // 38
    short tos;       // 39
    int src_as;      // 40-41
    int dst_as;      // 42-43
    short src_mask;  // 44
    short dst_mask;  // 45
    int pad2;        // 46-47

    public NetflowV5Packet() {}

    public NetflowV5Packet(java.io.InputStream stream)
            throws java.io.IOException
    {
        this.read(stream);
    }

    public void read(java.io.InputStream stream)
            throws java.io.IOException
    {
        byte[] data = new byte[48];
        int bytesRead = stream.readNBytes(data,0, data.length);
        if (bytesRead != data.length) {
            throw new java.io.IOException("read " + bytesRead + "/" + data.length + " bytes");
        }
        this.parse(data);
    }

    public void parse(byte[] data)
    {
        UnsignedByteBuffer reader = new UnsignedByteBuffer(ByteBuffer.wrap(data));

        srcaddr = reader.getUnsignedInt();
        dstaddr = reader.getUnsignedInt();
        nexthop = reader.getUnsignedInt();
        input = reader.getUnsignedShort();
        output = reader.getUnsignedShort();
        dPkts = reader.getUnsignedInt();
        dOctets = reader.getUnsignedInt();
        First = reader.getUnsignedInt();
        Last = reader.getUnsignedInt();
        srcport = reader.getUnsignedShort();
        dstport = reader.getUnsignedShort();
        pad1 = reader.getUnsignedByte();
        tcp_flags = reader.getUnsignedByte();
        prot = reader.getUnsignedByte();
        tos = reader.getUnsignedByte();
        src_as = reader.getUnsignedShort();
        dst_as = reader.getUnsignedShort();
        src_mask = reader.getUnsignedByte();
        dst_mask = reader.getUnsignedByte();
        pad2 = reader.getUnsignedShort();
    }

    public String toString()
    {
        return "NetflowV5Packet:" +
            Util.addressToString(srcaddr) + ":" +
            Util.addressToString(dstaddr) + ":" +
            Util.addressToString(nexthop) + ":" +
            input + ":" +
            output + ":" +
            dPkts + ":" +
            dOctets + ":" +
            First + ":" +
            Last + ":" +
            srcport + ":" +
            dstport + ":" +
            pad1 + ":" +
            tcp_flags + ":" +
            prot + ":" +
            tos + ":" +
            src_as + ":" +
            dst_as + ":" +
            src_mask + ":" +
            dst_mask + ":" +
            pad2;
    }
}
