package com.company.netflow.hadoop;

import com.company.netflow.NetflowV5Reader;
import com.fakecompany.flow.Flow;
import org.apache.avro.Schema;
import org.apache.avro.file.FileReader;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PushbackInputStream;
import java.net.URI;

public class NetflowV5FileReader extends NetflowV5Reader implements FileReader< Flow >
{
    private final static Logger LOGGER = LoggerFactory.getLogger( NetflowV5FileReader.class );

    public NetflowV5FileReader( URI uri, JobConf conf )
    {
        LOGGER.info( "NetflowV5FileReader uri: " + uri );
        Path path = new Path( uri );

        try
        {
            FileSystem fs = FileSystem.get( conf );
            input = new PushbackInputStream( fs.open( path ) );
        }
        catch ( Exception e )
        {
            throw new Error( "unable to process path:" + path );
        }
    }

    @Override
    public Schema getSchema()
    {
        return Flow.getClassSchema();
    }
}
