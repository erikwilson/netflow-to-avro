package com.company;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * Hello world!
 *
 */
public class App
{

    final static Logger logger = LoggerFactory.getLogger(CombinedRunner.class);

    public static void main(String[] args) throws Exception {
        logger.debug("Running Netflow-to-Avro! args:" + String.join(" ",args));
//        System.setProperty("hadoop.home.dir", "/");
        int exitCode;

//        exitCode = ToolRunner.run(new Configuration(), new FlowRunner(), args);
//        if (exitCode != 0) System.exit(exitCode);

        exitCode = ToolRunner.run(new Configuration(), new CombinedRunner(), args);
        if (exitCode != 0) System.exit(exitCode);

        System.exit(0);
    }
}
