package com.company;

import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

/**
 * Hello world!
 *
 */
public class App
{

    private static final Logger LOG = Logger.getLogger("App");

    public static void main(String[] args) throws Exception {
        LOG.info("Running Netflow-to-Avro! args:" + String.join(" ",args));
        System.setProperty("hadoop.home.dir", "/");
        int exitCode;

//        exitCode = ToolRunner.run(new Configuration(), new FlowRunner(), args);
//        if (exitCode != 0) System.exit(exitCode);

        exitCode = ToolRunner.run(new Configuration(), new MetricsRunner(), args);
        if (exitCode != 0) System.exit(exitCode);

        System.exit(0);
    }
}
