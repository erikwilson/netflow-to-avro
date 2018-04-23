package com.fakecompany;

import com.company.util.BinaryData;
import com.company.util.DataTypes;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Unit test for simple BinaryData.
 */
public class BinaryDataTest
    extends TestCase
{
    private class UnsignedBinaryTypesReader extends BinaryData
    {
        private UnsignedBinaryTypesReader()
        {
            super(7, Arrays.asList(
                Map.entry("byte", DataTypes.UINT8),   // 0
                Map.entry("short", DataTypes.UINT16), // 1-2
                Map.entry("int", DataTypes.UINT32)    // 3-6
            ) );
        }
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BinaryDataTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BinaryDataTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        UnsignedBinaryTypesReader reader = new UnsignedBinaryTypesReader();
        reader.parse(new byte[]{ 0x01, 0x01, 0x02, 0x01, 0x02, 0x03, 0x04 });

        int byteRead = reader.get("byte");
        int shortRead = reader.get("short");
        long intRead = reader.get("int");

        assertEquals( byteRead, 0x01 );
        assertEquals( shortRead, 0x0102 );
        assertEquals( intRead, 0x01020304 );
    }
}
