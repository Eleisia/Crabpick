import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PlaytimeReduce
        extends Reducer <IntWritable, IntWritable, IntWritable, DoubleWritable>{

    @Override
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        double sum_of_playtime = 0;
        int count = 0;
        for (IntWritable value : values) {
            sum_of_playtime = sum_of_playtime + value.get();
            count = count + 1;
        }

        double avg_playtime = sum_of_playtime/count;

        context.write(key, new DoubleWritable(avg_playtime));
    }
}
