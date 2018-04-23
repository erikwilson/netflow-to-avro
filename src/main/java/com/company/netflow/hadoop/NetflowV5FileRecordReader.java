package com.company.netflow.hadoop;

import com.fakecompany.flow.Flow;
import org.apache.avro.mapred.AvroRecordReader;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class NetflowV5FileRecordReader extends AvroRecordReader< Flow >
{

    public NetflowV5FileRecordReader( JobConf job, FileSplit split )
            throws IOException
    {
        super( new NetflowV5FileReader( split.getPath().toUri(), job ), split );
    }

}
