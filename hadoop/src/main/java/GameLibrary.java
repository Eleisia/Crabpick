import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;


public class GameLibrary {

    // Map Class
    public static class LibraryMap
            extends Mapper<LongWritable, Text, Text, Text> {

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
            String steamId = author_data.getString("steamid");
            // int playtime_forever = 0;
            // if (author_data.get("playtime_forever").equals("None")){
            //     return;}
            // else {
            //     playtime_forever = author_data.getInt("playtime_forever");
            // }

             boolean voted_up = Boolean.parseBoolean(csvDataElements[4]);
            // String language = csvDataElements[5];
            // boolean steam_purchase = Boolean.parseBoolean(csvDataElements[6]);
            if (voted_up) {
                context.write(new Text(steamId), new Text(gameId));
            }

        }
    }

    // Reduce Class
    public static class LibraryReduce
            extends Reducer<Text, Text, Text, CustomArrayWritable> {

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            List<String> stringList = new ArrayList<String>();
            for (Text value : values) {
                stringList.add(value.toString());
            }
            String[] stringArray = stringList.toArray(new String[stringList.size()]);
            Writable[] writables = Arrays.stream(stringArray)
                    .map(Text::new)
                    .toArray(Writable[]::new);
            CustomArrayWritable result = new CustomArrayWritable(writables);
            context.write(key, result);
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

        Job job = new Job(conf, "GameLibrary");
        job.setJarByClass(GameLibrary.class);

        // let hadoop know my map and reduce classes
        job.setMapperClass(GameLibrary.LibraryMap.class);
        job.setReducerClass(GameLibrary.LibraryReduce.class);
        // set multi reduce task
        job.setNumReduceTasks(10);

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CustomArrayWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
