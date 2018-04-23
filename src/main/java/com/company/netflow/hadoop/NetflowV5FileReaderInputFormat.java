package com.company.netflow.hadoop;

import com.fakecompany.flow.Flow;
import org.apache.avro.mapred.AvroWrapper;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

public class NetflowV5FileReaderInputFormat extends FileInputFormat< AvroWrapper< Flow >, NullWritable >
{
    @Override
    public NetflowV5FileRecordReader getRecordReader(
            InputSplit inputSplit, JobConf jobConf,
            Reporter reporter ) throws IOException
    {
        reporter.setStatus( inputSplit.toString() );
        return new NetflowV5FileRecordReader( jobConf, (FileSplit)inputSplit );
    }
}
