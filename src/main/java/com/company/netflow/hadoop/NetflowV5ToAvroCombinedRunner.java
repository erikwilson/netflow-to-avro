package com.company.netflow.hadoop;

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

public class NetflowV5ToAvroCombinedRunner extends Configured implements Tool {

    private final static Logger LOGGER = LoggerFactory.getLogger(NetflowV5ToAvroCombinedRunner.class);

    public static class Mapper extends AvroMapper<Flow, Pair<Long,CombinedData>> {

        private AvroMultipleOutputs amos;

        public void configure(JobConf conf) {
            amos = new AvroMultipleOutputs(conf);
        }
        public void close() throws IOException { amos.close(); }

        @Override
        public void map(Flow flow, AvroCollector<Pair<Long,CombinedData>> collector,
                        Reporter reporter) throws IOException
        {
            Metrics metrics = new Metrics(0L,flow.getOctets(),flow.getPackets(), new HashMap<>(){{
                put(flow.getTcpFlags().toString(),1L);
            }});

            long srcAddr = flow.getSrcAddr();
            long dstAddr = flow.getDstAddr();

            amos.collect("flow", reporter, Flow.getClassSchema(), flow);
            collect(collector, metrics, srcAddr, dstAddr);
            collect(collector, metrics, dstAddr, srcAddr);
        }

        private void collect(AvroCollector<Pair<Long,CombinedData>> collector,
                             Metrics metrics, Long srcAddr, Long dstAddr ) throws IOException
        {
            metrics.setAddr(srcAddr);
            AdjacencyList adjacencyList = new AdjacencyList(srcAddr,new LinkedList<>(){{add(dstAddr);}});
            CombinedData result = new CombinedData(metrics,adjacencyList);
            collector.collect(new Pair<>(srcAddr, result));
        }

    }

    public static class Reducer extends AvroReducer<Long, CombinedData, Pair<Long,CombinedData>> {

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

            amos.collect("metrics", reporter, Metrics.getClassSchema(), allMetrics);
            amos.collect("adjacency", reporter, AdjacencyList.getClassSchema(), adjacencyList);
        }

    }

    public int run(String[] args) throws Exception {

        LOGGER.debug("Starting NetflowV5ToAvroCombinedRunner...");

        JobConf conf = new JobConf(getConf(), NetflowV5ToAvroCombinedRunner.class);

        conf.setJobName("NetflowV5ToAvroCombinedRunner");
        conf.setInputFormat(NetflowV5FileReaderInputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        AvroMultipleOutputs.addNamedOutput(conf, "flow", AvroOutputFormat.class, Flow.getClassSchema());
        AvroMultipleOutputs.addNamedOutput(conf, "metrics", AvroOutputFormat.class, Metrics.getClassSchema());
        AvroMultipleOutputs.addNamedOutput(conf, "adjacency", AvroOutputFormat.class, AdjacencyList.getClassSchema());

        AvroJob.setMapperClass(conf, Mapper.class);
        AvroJob.setReducerClass(conf, Reducer.class);

        AvroJob.setInputSchema(conf, Flow.getClassSchema());
        AvroJob.setOutputSchema(conf,
            Pair.getPairSchema(
                Schema.create(Schema.Type.LONG),
                CombinedData.getClassSchema()));

        JobClient.runJob(conf);
        LOGGER.debug("Done NetflowV5ToAvroCombinedRunner!");

        return 0;
    }

}
