
# netflow-to-avro

This project will process a directory containing Netflow files in hadoop and output the
associated flow, metrics, and adjacency data. This is accomplished by creating an
avro.hadoop.mapred NetflowV5 to flow reader, outputting flow data in the map stage, while
reducing metrics and adjacency information at the same time. In this way we can read
and convert relevant Netflow data once in a parallel manner, then use a single map/reduce
event to record our three Avro files.

## Building and Running

This project depends on Java 9, maven, and running hadoop services.

See `build.sh` and `run.sh`. To perform both: `./build.sh && ./run.sh`.

Netflow files are read from the `./input` directory. Flow, metrics, and adjacency Avro files
are written to the `./output` directory.

Correct operation may require `<classifier>hadoop1</classifier>` being removed from `pom.xml`.
This project was tested on OSX with brew installed hadoop.

Files can be converted to a human readable format by using the `avroToJson.sh` command:
```
./avroToJson.sh ./output/flow-m-00000.avro >flow.json
./avroToJson.sh ./output/metrics-r-00000.avro >metrics.json
./avroToJson.sh ./output/adjacency-r-00000.avro >adjacency.json
```

## Other

The method of binary parsing used sacrifices some speed and type safety for code compactness.
A schema to class generator similar to Avro's or an annotated markup might be preferred.

Avro flow timestamps are populated from the timestamp seen in the most recent Netflow header.

Needs more tests, documentation in code.
