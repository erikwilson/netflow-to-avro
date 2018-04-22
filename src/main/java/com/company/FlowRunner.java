package com.company;

import com.fakecompany.flow.Flow;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;


public class FlowRunner extends Configured implements Tool
{
    final private static org.slf4j.Logger logger = LoggerFactory.getLogger(FlowRunner.class);

    @Override
    public int run(String[] args) throws Exception {
        logger.debug("Starting FlowRunner...");
        String inFile = args[0];
        File outFile = new File("flow.avro");

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(Flow.SCHEMA$);
        DataFileWriter<GenericRecord> writer = new DataFileWriter<GenericRecord>(datumWriter)
                .setSyncInterval(100);
        writer.create(Flow.SCHEMA$,outFile);

        FileInputStream fis = new FileInputStream(inFile);

        NetflowV5Header header = new NetflowV5Header();
        NetflowV5Packet packet = new NetflowV5Packet();
        Flow flow = new Flow();

        long counter = 0;

        while (fis.available() > 0) {
            header.read(fis);
            flow.setAgent(3232235777L);
            flow.setTimestamp((long)header.get("unixSecs"));

            int count = (int)header.get("count");
            for (int i=0; i<count; i++, counter++) {
                packet.read(fis);
                packet.toAvroFlow(flow);
                writer.append(flow);
            }
        }

        writer.close();
        fis.close();

        logger.debug("FlowRunner complete! Processed " + counter + " records");
        return 0;
    }

}
