package com.company.netflow.hadoop;

import com.company.netflow.NetflowV5Header;
import com.company.netflow.NetflowV5Packet;
import com.fakecompany.flow.Flow;
import org.apache.avro.Schema;
import org.apache.avro.file.FileReader;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PushbackInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NetflowV5FileReader implements FileReader<Flow> {

    private final static Logger LOGGER = LoggerFactory.getLogger(NetflowV5FileReader.class);

    private PushbackInputStream input;
    private int packetCount = 0;
    private long totalCount = 0L;
    private long lastTimestamp = 0L;
    private NetflowV5Header header = new NetflowV5Header();
    private NetflowV5Packet packet = new NetflowV5Packet();
    private Flow flow = new Flow();

    public NetflowV5FileReader(URI uri, JobConf conf) {

        LOGGER.info("NetflowV5FileReader uri: " + uri);
        Path path = new Path(uri);

        try {
            FileSystem fs = FileSystem.get(conf);
            input = new PushbackInputStream(fs.open(path));
        } catch (Exception e) {
            throw new Error("unable to process path:" + path);
        }

        flow.setAgent(3232235777L);

    }

    @Override
    public Schema getSchema() {
        return Flow.getClassSchema();
    }


    @Override
    public Flow next(Flow flow) throws IOException {

        if (flow == null) flow = this.flow;

        while (input != null) {

            try {

                if (packetCount == 0) {
                    header.read(input);
                    packetCount = header.get("count");
                    lastTimestamp = header.get("unixSecs");
                }
                if (packetCount > 0) {
                    packet.read(input);
                    packet.toAvroFlow(flow);
                    flow.setTimestamp(lastTimestamp);
                    packetCount -= 1;
                    totalCount += 1;
                    return flow;
                }

            } catch (Exception e) {
                e.printStackTrace();

                try {
                    input.close();
                } catch(Exception closeError) {
                    LOGGER.warn(closeError.toString());
                }

                packetCount = 0;
                input = null;
            }

        }

        LOGGER.info("failed after packet# " + totalCount);
        throw new NoSuchElementException();
    }

    @Override
    public void sync(long l) throws IOException {
        while (totalCount<l && hasNext()) next();
    }

    @Override
    public boolean pastSync(long l) throws IOException {
        return false;
    }

    @Override
    public long tell() throws IOException {
        return totalCount;
    }

    @Override
    public void close() throws IOException {
        if (input == null) return;
        try {
            input.close();
        } catch( Exception e) {
            LOGGER.warn(e.toString());
        }
        input = null;
    }

    @Override
    public Iterator<Flow> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (input == null) return false;
        try {
            int next = input.read();
            if (next > -1) {
                input.unread(next);
                return true;
            }
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
        input = null;
        return false;
    }

    @Override
    public Flow next() {
        try {
            return next(flow);
        } catch( Exception e ) {
            LOGGER.warn(e.toString());
        }
        return null;
    }

}
