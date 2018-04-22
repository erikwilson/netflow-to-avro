package com.company;

import com.fakecompany.flow.Flow;
import com.fakecompany.metrics.Metrics;
import com.fakecompany.adjacencylist.AdjacencyList;
import com.fakecompany.combineddata.CombinedData;
import org.apache.avro.Schema;
import org.apache.avro.mapred.*;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class CombinedRunner extends Configured implements Tool {

    final private static Logger logger = LoggerFactory.getLogger(CombinedRunner.class);

    public static class CombinedMapper extends AvroMapper<Flow, Pair<Long,CombinedData>> {

        private void collect(AvroCollector<Pair<Long,CombinedData>> collector,
                             Metrics metrics, Long srcAddr, Long dstAddr ) throws IOException
        {
            metrics.setAddr(srcAddr);
            AdjacencyList adjacencyList = new AdjacencyList(srcAddr,new LinkedList<>(){{add(dstAddr);}});
            CombinedData result = new CombinedData(metrics,adjacencyList);
            collector.collect(new Pair<>(srcAddr, result));
        }

        @Override
        public void map(Flow flow, AvroCollector<Pair<Long,CombinedData>> collector,
                        Reporter reporter) throws IOException
        {
            Metrics metrics = new Metrics(0L,flow.getOctets(),flow.getPackets(), new HashMap<>(){{
                put(flow.getTcpFlags().toString(),1L);
            }});

            long srcAddr = flow.getSrcAddr();
            long dstAddr = flow.getDstAddr();

            collect(collector, metrics, srcAddr, dstAddr);
            collect(collector, metrics, dstAddr, srcAddr);
        }

    }

    public static class CombinedReducer extends AvroReducer<Long, CombinedData, Pair<Long,CombinedData>> {

        private AvroMultipleOutputs amos;

        public void configure(JobConf conf) {
            amos = new AvroMultipleOutputs(conf);
        }
        public void close() throws IOException { amos.close(); }

        @Override
        public void reduce(Long addr,
                           Iterable<CombinedData> allData,
                           AvroCollector<Pair<Long,CombinedData>> collector,
                           Reporter reporter) throws IOException {

            Map<CharSequence,Long> tcpFlags = new HashMap<>();
            Metrics allMetrics = new Metrics(addr, 0L, 0L, tcpFlags);
            List<Long> neighbors = new LinkedList<>();
            AdjacencyList adjacencyList = new AdjacencyList(addr,neighbors);

            for (CombinedData data : allData) {
                Metrics metric = data.getMetrics();
                allMetrics.setOctets( allMetrics.getOctets() + metric.getOctets() );
                allMetrics.setPackets( allMetrics.getPackets() + metric.getPackets() );
                metric.getTcpFlags().forEach((k,v)->{
                    Long currentValue = tcpFlags.getOrDefault(k,0L);
                    tcpFlags.put(k, currentValue + v);
                });

               data.getAdjacency().getNeighbors().forEach( n -> {
                   if (!neighbors.contains(n)) neighbors.add(n);
               });
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Reduced metrics:" + allMetrics);
                logger.debug("Reduces adjacency:" + adjacencyList);
            }
            amos.collect("metrics",reporter,Metrics.getClassSchema(),allMetrics);
            amos.collect("adjacency",reporter,AdjacencyList.getClassSchema(),adjacencyList);
        }

    }

    public int run(String[] args) throws Exception {

        logger.debug("Starting CombinedRunner...");

        JobConf conf = new JobConf(getConf(), CombinedRunner.class);

        conf.setJobName("CombinedRunner");

        FileInputFormat.setInputPaths(conf, new Path("input"));
        FileOutputFormat.setOutputPath(conf, new Path("output"));

        AvroMultipleOutputs.addNamedOutput(conf, "metrics", AvroOutputFormat.class, Metrics.getClassSchema());
        AvroMultipleOutputs.addNamedOutput(conf, "adjacency", AvroOutputFormat.class, AdjacencyList.getClassSchema());

        AvroJob.setMapperClass(conf, CombinedMapper.class);
        AvroJob.setReducerClass(conf, CombinedReducer.class);

        AvroJob.setInputSchema(conf, Flow.getClassSchema());
        AvroJob.setOutputSchema(conf,
            Pair.getPairSchema(
                Schema.create(Schema.Type.LONG),
                CombinedData.getClassSchema()));

        JobClient.runJob(conf);
        logger.debug("Done CombinedRunner!");

        return 0;
    }

}
