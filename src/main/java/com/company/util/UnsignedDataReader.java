package com.company.util;

import java.nio.ByteBuffer;

class UnsignedDataReader
{
    private ByteBuffer buffer;

    UnsignedDataReader( ByteBuffer buffer )
    {
        this.buffer = buffer;
    }

    // should return short, but to satisfy avro data use int for now
    int getUnsignedByte()
    {
        return (buffer.get() & 0xff);
    }

    int getUnsignedShort()
    {
        return (buffer.getShort() & 0xffff);
    }

    long getUnsignedInt()
    {
        return ((long) buffer.getInt() & 0xffffffffL);
    }
}
