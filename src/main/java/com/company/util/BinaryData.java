package com.company.util;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BinaryData {

    private String name;

    protected Map<String,Object> packetData;
    protected List<Map.Entry<String,DataTypes>> entryList;
    protected Map<String,Serializer> serializers;
    protected byte[] data;

    public int entryLength;

    public interface Serializer {
        String toString(Object a);
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
        data = new byte[entryLength];
    }

    public void read(java.io.InputStream stream)
            throws java.io.IOException
    {
        int bytesRead = stream.readNBytes(data,0, data.length);
        if (bytesRead != data.length) {
            throw new java.io.IOException("read " + bytesRead + "/" + data.length + " bytes");
        }
        this.parse(data);
    }

    public void parse(byte[] data)
    {
        UnsignedDataReader reader = new UnsignedDataReader(ByteBuffer.wrap(data));
        packetData = entryList.stream().collect(Collectors.toMap(
            Map.Entry::getKey,
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

    // evil
    public <T> T get(String key) { return (T)packetData.get(key); }

    public String toString()
    {
        return name + ":" + packetData.entrySet().stream().collect(Collector.of(
            ArrayList<String>::new,
            (result, entry) -> {
                String key = entry.getKey();
                Object value = entry.getValue();
                Serializer serial = serializers != null ? serializers.get(key) : null;
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
