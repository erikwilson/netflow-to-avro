package com.company;

import com.fakecompany.flow.Flow;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.hadoop.io.AvroSequenceFile;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.util.Tool;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;


public class FlowRunner extends Configured implements Tool
{

    private static final Logger LOG = Logger.getLogger("FlowRunner");

    @Override
    public int run(String[] args) throws Exception {
        LOG.info("Starting FlowRunner...");
        String inFile = args[0];
        File outFile = new File("flow.avro");
//        FileSystem fs = FileSystem.get(getConf());

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(Flow.SCHEMA$);
        DataFileWriter<GenericRecord> writer = new DataFileWriter<GenericRecord>(datumWriter)
                .setSyncInterval(100);
        writer.create(Flow.SCHEMA$,outFile);
//        writer.setCodec(CodecFactory.snappyCodec());

//        AvroSequenceFile.Writer.Options options = new AvroSequenceFile.Writer.Options()
//            .withFileSystem(fs)
//            .withConfiguration(getConf())
//            .withOutputPath(outFile)
//            .withKeyClass(AvroKey.class)
//            .withKeySchema(Schema.create(Schema.Type.LONG))
//            .withValueClass(Flow.class)
//            .withValueSchema(Flow.SCHEMA$);
//
//        Writer writer = AvroSequenceFile.createWriter(options);

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

        LOG.info("FlowRunner complete! Processed " + counter + " records");
        return 0;
    }

}
