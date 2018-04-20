package com.fakecompany;

import java.io.Externalizable;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BinaryData {

    List<Map.Entry<String,DataTypes>> entryList;
    Map<String,Object> packetData;
    Map<String,Serializer> serializers;
    int entryLength;
    String name;

    public interface Serializer {
        public String toString(Object a);
    }

    public BinaryData(int entryLength, List<Map.Entry<String,DataTypes>> entryList) {
        this.serializers = new HashMap<>();
        this.init(entryLength, entryList);
    }

    public BinaryData(int entryLength, List<Map.Entry<String,DataTypes>> entryList, Map<String,Serializer> serializers) {
        this.serializers = serializers;
        this.init(entryLength, entryList);
    }

    private void init(int entryLength, List<Map.Entry<String,DataTypes>> entryList) {
        this.name = this.getClass().getSimpleName();
        this.entryList = entryList;
        this.entryLength = entryLength;
        int calculatedSize = entryList.stream().mapToInt(entry -> {
            switch(entry.getValue()) {
                case UINT8:  return 1;
                case UINT16: return 2;
                case UINT32: return 4;
            }
            throw new Error("invalid type");
        }).sum();
        if (calculatedSize != entryLength) {
            throw new Error("expected binary entry length " + entryLength + " does not match calculated size " + calculatedSize);
        }
    }

    public void read(java.io.InputStream stream)
            throws java.io.IOException
    {
        byte[] data = new byte[entryLength];
        int bytesRead = stream.readNBytes(data,0, data.length);
        if (bytesRead != data.length) {
            throw new java.io.IOException("read " + bytesRead + "/" + data.length + " bytes");
        }
        this.parse(data);
    }

    public void parse(byte[] data)
    {
        UnsignedByteBuffer reader = new UnsignedByteBuffer(ByteBuffer.wrap(data));
        packetData = entryList.stream().collect(Collectors.toMap(
            entry -> entry.getKey(),
            entry -> {
                Object entryData = null;
                switch(entry.getValue()) {
                    case UINT8:  entryData = reader.getUnsignedByte();
                        break;
                    case UINT16: entryData = reader.getUnsignedShort();
                        break;
                    case UINT32: entryData = reader.getUnsignedInt();
                        break;
                }
                return entryData;
            },
            (e1, e2) -> e1,
            LinkedHashMap::new));
    }

    public Object get(String key) {
        return packetData.get(key);
    }

    public String toString()
    {
        return name + ":" + packetData.entrySet().stream().collect(Collector.of(
            () -> new ArrayList<String>(),
            (result, entry) -> {
                String key = entry.getKey();
                Object value = entry.getValue();
                Serializer serial = serializers.get(key);
                if (serial != null) {
                    result.add(serial.toString(value));
                }
                else {
                    result.add(String.valueOf(value));
                }
            },
            (result1, result2) -> {
                result1.addAll(result2);
                return result1;
            },
            result -> String.join(",", result)
        ));
    }
}
