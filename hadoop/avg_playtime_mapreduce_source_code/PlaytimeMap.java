import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONObject;

import java.io.IOException;

public class PlaytimeMap
        extends Mapper <LongWritable, Text, IntWritable, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        //리뷰 데이터 파싱
        String csvDataLine = value.toString();
        String[] csvDataElements = csvDataLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // Now you can access each element by index:
        int reviewIndex = Integer.parseInt(csvDataElements[0]);
        int gameId = Integer.parseInt(csvDataElements[1]);
        int reviewId = Integer.parseInt(csvDataElements[2]);

        //작성자 데이터 파싱
        JSONObject author_data = new JSONObject(csvDataElements[3].substring(1, csvDataElements[3].length()-1));
        String steamid = author_data.getString("steamid");
        int playtime_forever = author_data.getInt("playtime_forever");


        boolean voted_up = Boolean.parseBoolean(csvDataElements[4]);
        String language = csvDataElements[5];
        boolean steam_purchase = Boolean.parseBoolean(csvDataElements[6]);

//        System.out.println(reviewIndex);
//        System.out.println(gameId);
//        System.out.println(reviewId);
//        System.out.println(author);
//        System.out.println(voted_up);
//        System.out.println(language);
//        System.out.println(steam_purchase);

//        System.out.println(steamid);
//        System.out.println(playtime_forever);
        if (playtime_forever > 300){
            context.write(new IntWritable(gameId), new IntWritable(playtime_forever));
        }
    }
}
