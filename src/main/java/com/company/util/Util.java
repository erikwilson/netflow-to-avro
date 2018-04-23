package com.company.util;

public class Util
{
    public static String addressToString( long address )
    {
        return "" +
                ((address & 0xff000000) >> 24) + "." +
                ((address & 0x00ff0000) >> 16) + "." +
                ((address & 0x0000ff00) >> 8 ) + "." +
                ((address & 0x000000ff));
    }
}
