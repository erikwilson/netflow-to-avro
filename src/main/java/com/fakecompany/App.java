package com.fakecompany;

import java.io.FileInputStream;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        System.out.println(String.join(" ",args));
        FileInputStream fis = new FileInputStream(args[0]);
        System.out.println("doing a read...");
        NetflowV5Header2 header = new NetflowV5Header2();
        NetflowV5Packet2 packet = new NetflowV5Packet2();
        while (fis.available() > 0) {
            header.read(fis);
            System.out.println(header);
            int count = (int)header.get("count");
            for (int i=0; i<count; i++) {
                packet.read(fis);
                System.out.println(packet);
            }
        }
        fis.close();
    }
}
