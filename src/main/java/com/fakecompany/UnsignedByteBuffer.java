package com.fakecompany;

import java.nio.ByteBuffer;

public class UnsignedByteBuffer {

    enum Types {
        UINT8, UINT16, UINT32;
    }

    ByteBuffer byteBuffer;

    public UnsignedByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    // ---------------------------------------------------------------

    public short getUnsignedByte() {
        return ((short) (byteBuffer.get() & 0xff));
    }

    public void putUnsignedByte(int value) {
        byteBuffer.put((byte) (value & 0xff));
    }

    public short getUnsignedByte(int position) {
        return ((short) (byteBuffer.get(position) & (short) 0xff));
    }

    public void putUnsignedByte(int position, int value) {
        byteBuffer.put(position, (byte) (value & 0xff));
    }

    // ---------------------------------------------------------------

    public int getUnsignedShort() {
        return (byteBuffer.getShort() & 0xffff);
    }

    public void putUnsignedShort(int value) {
        byteBuffer.putShort((short) (value & 0xffff));
    }

    public int getUnsignedShort(int position) {
        return (byteBuffer.getShort(position) & 0xffff);
    }

    public void putUnsignedShort(int position, int value) {
        byteBuffer.putShort(position, (short) (value & 0xffff));
    }

    // ---------------------------------------------------------------

    public long getUnsignedInt() {
        return ((long) byteBuffer.getInt() & 0xffffffffL);
    }

    public void putUnsignedInt(long value) {
        byteBuffer.putInt((int) (value & 0xffffffffL));
    }

    public long getUnsignedInt(int position) {
        return ((long) byteBuffer.getInt(position) & 0xffffffffL);
    }

    public void putUnsignedInt(int position, long value) {
        byteBuffer.putInt(position, (int) (value & 0xffffffffL));
    }


}
