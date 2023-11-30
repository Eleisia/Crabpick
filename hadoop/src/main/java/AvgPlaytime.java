import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.json.JSONObject;

import java.io.IOException;

public class AvgPlaytime {

    // Map Class
    public static class PlaytimeMap
            extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            //Parse Review data
            String csvDataLine = value.toString();
            if (csvDataLine.startsWith(",")) {
                return;
            }
            String[] csvDataElements = csvDataLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            // Now you can access each element by index:
            // String reviewIndex = csvDataElements[0];
            String gameId = csvDataElements[1];
            // String reviewId = csvDataElements[2];
            //Parse Review Author Data
            JSONObject author_data = new JSONObject(csvDataElements[3].substring(1, csvDataElements[3].length()-1));
            // String steamid = author_data.getString("steamid");
            int playtime_forever = 0;
            if (author_data.get("playtime_forever").equals("None")){
                return;}
            else {
                playtime_forever = author_data.getInt("playtime_forever");
            }

            // boolean voted_up = Boolean.parseBoolean(csvDataElements[4]);
            // String language = csvDataElements[5];
            // boolean steam_purchase = Boolean.parseBoolean(csvDataElements[6]);

            // System.out.println(reviewIndex);
            // System.out.println(gameId);
            // System.out.println(reviewId);
            // System.out.println(author);
            // System.out.println(voted_up);
            // System.out.println(language);
            // System.out.println(steam_purchase);

            // System.out.println(steamid);
            // System.out.println(playtime_forever);
            if (playtime_forever > 120){
                context.write(new Text(gameId), new IntWritable(playtime_forever));
            }
        }
    }

    // Reduce Class
    public static class PlaytimeReduce
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum_of_playtime = 0;
            int count = 0;
            for (IntWritable value : values) {
                sum_of_playtime = sum_of_playtime + value.get();
                count = count + 1;
            }

            int avg_playtime = sum_of_playtime/count;

            context.write(key, new IntWritable(avg_playtime));
        }
    }

    // Job Setting
    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: AvgPlaytime <input path> <output path>");
            System.exit(2);
        }

        Job job = new Job(conf, "AvgPlaytime");
        job.setJarByClass(AvgPlaytime.class);

        // let hadoop know my map and reduce classes
        job.setMapperClass(PlaytimeMap.class);
        job.setReducerClass(PlaytimeReduce.class);

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}