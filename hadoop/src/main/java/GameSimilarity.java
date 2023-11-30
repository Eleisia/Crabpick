import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.*;
/*
Algorithm -> count Number of User who reviewed A game and B game
then make data like under example

A : {B: 10, C: 5}

then we can understand A:B is more similar that A:C
*/
public class GameSimilarity {

    // Map Class
    public static class SimilarityMap
            extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String dataLine = value.toString();
            // Hadoop output parsing
            String[] dataLineElements = dataLine.split("\t", 2);

            String[] gameIds = dataLineElements[1].split("\t");
            // GameSimilarity mapping
            for (String A: gameIds) {
                for (String B: gameIds) {
                    if (!A.equals(B)) {
                        context.write(new Text(A), new Text(B));
                    }
                }
            }
        }
    }

    // Reduce Class
    public static class SimilarityReduce
            extends Reducer<Text, Text, Text, Text> {

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            // steamGameID : (how many user reviewed both game)
            HashMap<String, Integer> gameCount = new HashMap<>();
            for (Text value: values) {
                String str = value.toString();
                if (gameCount.containsKey(str)) {
                    gameCount.replace(str, gameCount.get(str) + 1);
                }
                else {
                    gameCount.put(str, 1);
                }
            }
            final double n = 84390;


            if (gameCount.size() > 0) {
                // sorting map by value
                List<Map.Entry<String, Integer>> nlist = new ArrayList<>(gameCount.entrySet());
                if (nlist.size() > 100) {
                    nlist = nlist.subList(0, 100);
                }
                int df = nlist.size();
                double eq = (n/(1+df));
                nlist.sort(Map.Entry.comparingByValue());
                // forming reduce output
                StringBuilder result = new StringBuilder();
                for(Map.Entry<String, Integer> entry : nlist){
                    result.insert(0, entry.getKey() + ":" + String.format("%.4f", entry.getValue() * (Math.log(eq) / Math.log(500))) + " ");
                }
                context.write(key, new Text(result.toString()));
            }



        }
    }

    // Job Setting
    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: GameLibrary <input path> <output path>");
            System.exit(2);
        }

        Job job = new Job(conf, "GameSimilarity");
        job.setJarByClass(GameSimilarity.class);
        // separator change
        job.setOutputFormatClass(TextOutputFormat.class);
        conf.set("mapreduce.textoutputformat.separator", ",");
        // let hadoop know my map and reduce classes
        job.setMapperClass(GameSimilarity.SimilarityMap.class);
        job.setReducerClass(GameSimilarity.SimilarityReduce.class);
        // set multi reduce task
        job.setNumReduceTasks(10);
        // file input output setting
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        // mapper class input output setting
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        // job output data setting
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
