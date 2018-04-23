package com.company.util;

import java.nio.ByteBuffer;

public class UnsignedDataReader {

    private ByteBuffer byteBuffer;

    public UnsignedDataReader(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int getUnsignedByte() { return (byteBuffer.get() & 0xff); }

    public int getUnsignedShort() {
        return (byteBuffer.getShort() & 0xffff);
    }

    public long getUnsignedInt() {
        return ((long) byteBuffer.getInt() & 0xffffffffL);
    }

}
