package com.fakecompany;

import java.nio.ByteBuffer;

public class NetflowV5Header implements java.io.Serializable {
    int version;          // 0-1
    int count;            // 2-3
    long sysUptime;       // 4-7
    long unixSecs;        // 8-11
    long unixNanoSecs;    // 12-15
    long flowSequence;    // 16-19
    int engineType;       // 20
    int engineId;         // 21
    int samplingInterval; // 22-23

    public NetflowV5Header() {}

    public NetflowV5Header(java.io.InputStream stream)
            throws java.io.IOException
    {
        this.read(stream);
    }

    public void read(java.io.InputStream stream)
            throws java.io.IOException
    {
        byte[] data = new byte[24];
        int bytesRead = stream.readNBytes(data,0, data.length);
        if (bytesRead != data.length) {
            throw new java.io.IOException("read " + bytesRead + "/" + data.length + " bytes");
        }
        this.parse(data);
    }

    public void parse(byte[] data)
    {
        UnsignedByteBuffer reader = new UnsignedByteBuffer(ByteBuffer.wrap(data));
        version = reader.getUnsignedShort();
        count = reader.getUnsignedShort();
        sysUptime = reader.getUnsignedInt();
        unixSecs = reader.getUnsignedInt();
        unixNanoSecs = reader.getUnsignedInt();
        flowSequence = reader.getUnsignedInt();
        engineType = reader.getUnsignedByte();
        engineId = reader.getUnsignedByte();
        samplingInterval = reader.getUnsignedShort();
    }

    public String toString()
    {
        return "NetflowV5Header:" +
                version + ":" +
                count + ":" +
                sysUptime + ":" +
                unixSecs + ":" +
                unixNanoSecs + ":" +
                flowSequence + ":" +
                engineType + ":" +
                engineId + ":" +
                samplingInterval;
    }
}
