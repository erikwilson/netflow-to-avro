package com.company;

import com.fakecompany.flow.Flow;
import com.fakecompany.metrics.Metrics;
import com.fakecompany.adjacencylist.AdjacencyList;
import org.apache.avro.Schema;
import org.apache.avro.mapred.*;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class MetricsRunner  extends Configured implements Tool {

    private static final Logger LOG = Logger.getLogger("App");

    public static class MetricsMapper extends AvroMapper<Flow, Pair<Long,Pair<Metrics,AdjacencyList>>> {

        private void collect(AvroCollector<Pair<Long,Pair<Metrics,AdjacencyList>>> collector,
                             Metrics metrics, Long srcAddr, Long dstAddr ) throws IOException
        {
            metrics.setAddr(srcAddr);
            AdjacencyList adjacencyList = new AdjacencyList(srcAddr,new LinkedList<Long>(){{add(dstAddr);}});
            Pair<Metrics,AdjacencyList> result = new Pair<>(metrics,adjacencyList);
            collector.collect(new Pair<Long,Pair<Metrics,AdjacencyList>>(srcAddr, result));
        }

        @Override
        public void map(Flow flow, AvroCollector<Pair<Long,Pair<Metrics,AdjacencyList>>> collector,
                        Reporter reporter) throws IOException
        {
            LOG.info("In Mapper!");

            Metrics metrics = new Metrics(0L,flow.getOctets(),flow.getPackets(), new HashMap<>(){{
                put(flow.getTcpFlags().toString(),1L);
            }});

            long srcAddr = flow.getSrcAddr();
            long dstAddr = flow.getDstAddr();

            collect(collector, metrics, srcAddr, dstAddr);
            collect(collector, metrics, dstAddr, srcAddr);

            LOG.info("Done Mapper!");
        }

    }

    public static class MetricsReducer extends AvroReducer<Long, Pair<Metrics,AdjacencyList>, Pair<Long,Pair<Metrics,AdjacencyList>>> {

        private AvroMultipleOutputs amos;

        public void configure(JobConf conf) {
            amos = new AvroMultipleOutputs(conf);
        }

        @Override
        public void reduce(Long addr,
                           Iterable<Pair<Metrics,AdjacencyList>> allData,
                           AvroCollector<Pair<Long,Pair<Metrics,AdjacencyList>>> collector,
                           Reporter reporter) throws IOException {
            LOG.info("In Reducer!");

            Map<CharSequence,Long> tcpFlags = new HashMap<>();
            Metrics allMetrics = new Metrics(addr, 0L, 0L, tcpFlags);
            List<Long> neighbors = new LinkedList<>();
            AdjacencyList adjacencyList = new AdjacencyList(addr,neighbors);

            for (Pair<Metrics,AdjacencyList> data : allData) {
                Metrics metric = (Metrics)data.key();
                allMetrics.setOctets(allMetrics.getOctets()+metric.getOctets());
                allMetrics.setPackets(allMetrics.getPackets()+metric.getPackets());
                metric.getTcpFlags().forEach((k,v)->{
                    Long currentValue = tcpFlags.getOrDefault(k,0L);
                    tcpFlags.put(k, currentValue + v);
                });

                ((AdjacencyList)data.value()).getNeighbors().forEach( n -> {
                   if (!neighbors.contains(n)) neighbors.add(n);
               });
            }
            LOG.info("Reduce allMetrics:" + allMetrics);
//            collector.collect(new Pair<>(addr, allMetrics));
            amos.collect("metrics",reporter,Metrics.getClassSchema(),allMetrics);
            amos.collect("adjacency",reporter,AdjacencyList.getClassSchema(),adjacencyList);
        }

    }

    public int run(String[] args) throws Exception {
        LOG.info("Starting MetricsRunner...");

        JobConf conf = new JobConf(getConf(), MetricsRunner.class);
        conf.setJobName("MetricsRunner");

        FileInputFormat.setInputPaths(conf, new Path("input"));
        FileOutputFormat.setOutputPath(conf, new Path("output"));

        AvroMultipleOutputs.addNamedOutput(conf, "metrics", AvroOutputFormat.class, Metrics.getClassSchema());
        AvroMultipleOutputs.addNamedOutput(conf, "adjacency", AvroOutputFormat.class, AdjacencyList.getClassSchema());

        AvroJob.setMapperClass(conf, MetricsMapper.class);
        AvroJob.setReducerClass(conf, MetricsReducer.class);

        AvroJob.setInputSchema(conf, Flow.getClassSchema());
        AvroJob.setOutputSchema(conf,
                Pair.getPairSchema(Schema.create(Schema.Type.LONG),
                        Pair.getPairSchema(Metrics.getClassSchema(),AdjacencyList.getClassSchema())));

        JobClient.runJob(conf);

        return 0;
    }

}
