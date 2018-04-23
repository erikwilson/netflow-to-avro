package com.company;

import com.company.netflow.hadoop.NetflowV5ToAvroCombinedRunner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class NetflowV5ToAvro
{
    private final static Logger LOGGER = LoggerFactory.getLogger( NetflowV5ToAvroCombinedRunner.class );

    public static void main( String[] args ) throws Exception
    {
        LOGGER.debug( "Running Netflow-to-Avro! args:" + String.join( " ", args ) );
        int exitCode = ToolRunner.run( new Configuration(), new NetflowV5ToAvroCombinedRunner(), args );
        System.exit( exitCode );
    }
}
