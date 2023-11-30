import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JunitTest {

    @Test
    public void string_is_concatenated() {
        String csvDataLine = "1,1433210,107278675,\"{'steamid': '76561197987632873', 'num_games_owned': 804, 'num_reviews': 72, 'playtime_forever': 17, 'playtime_last_two_weeks': 0, 'playtime_at_review': 17, 'last_played': 1641272965}\",True,english,True";
        String csvDataLine2 = "1,1433210,107278675,\"{'steamid': '76561197987632873', 'num_games_owned': 804, 'num_reviews': 72, 'playtime_forever': None, 'playtime_last_two_weeks': None, 'playtime_at_review': None, 'last_played': 1641272965}\",True,english,True";
        //Parse Review data
        String[] csvDataElements = csvDataLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // Now you can access each element by index:
        int reviewIndex = Integer.parseInt(csvDataElements[0]);
        String gameId = csvDataElements[1];
        String reviewId = csvDataElements[2];

        //Parse Review Author Data
        JSONObject author_data = new JSONObject(csvDataElements[3].substring(1, csvDataElements[3].length()-1));
        String steamid = author_data.getString("steamid");
        int playtime_forever = 0;
        try {
            playtime_forever = author_data.getInt("playtime_forever");
        }catch (JSONException e) {
            System.out.println(e.getMessage());
        }



        boolean voted_up = Boolean.parseBoolean(csvDataElements[4]);
        String language = csvDataElements[5];
        boolean steam_purchase = Boolean.parseBoolean(csvDataElements[6]);

        System.out.println(reviewIndex);
        System.out.println(gameId);
        System.out.println(reviewId);
        System.out.println(author_data);
//        System.out.println(voted_up);
//        System.out.println(language);
//        System.out.println(steam_purchase);
//
//        System.out.println(steamid);
        System.out.println(playtime_forever);

}
    @Test
    public void string_has_None() {
        String csvDataLine = "1,1433210,107278675,\"{'steamid': '76561197987632873', 'num_games_owned': 804, 'num_reviews': 72, 'playtime_forever': None, 'playtime_last_two_weeks': None, 'playtime_at_review': None, 'last_played': 1641272965}\",True,english,True";
        //Parse Review data
        String[] csvDataElements = csvDataLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // Now you can access each element by index:
        int reviewIndex = Integer.parseInt(csvDataElements[0]);
        String gameId = csvDataElements[1];
        String reviewId = csvDataElements[2];

        //Parse Review Author Data
        JSONObject author_data = new JSONObject(csvDataElements[3].substring(1, csvDataElements[3].length()-1));
        String steamid = author_data.getString("steamid");
//        int playtime_forever = 0;
//        if (author_data.getString("playtime_forever").equals("None")){
//            System.out.println("Test Success");}
//        else {
//            playtime_forever = author_data.getInt("playtime_forever");
//        }




        boolean voted_up = Boolean.parseBoolean(csvDataElements[4]);
        String language = csvDataElements[5];
        boolean steam_purchase = Boolean.parseBoolean(csvDataElements[6]);

        System.out.println(reviewIndex);
        System.out.println(gameId);
        System.out.println(reviewId);
        System.out.println(author_data);
//        System.out.println(voted_up);
//        System.out.println(language);
//        System.out.println(steam_purchase);
//
        System.out.println(steamid);
//        System.out.println(playtime_forever);

    }

    @Test
    public void divided_for_what() {
        try {
            File f = new File("C:\\Users\\SSAFY\\Desktop\\project02\\Git\\hadoop\\data\\part-r-00000");
            BufferedReader br = new BufferedReader(new FileReader(f));
            System.out.println("file content: ");

            String r=br.readLine();
            while(r != null){
                String[] elem = r.split("\t");
                for (String val: elem) {
                    System.out.println(val);
                }
                r = br.readLine();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void calc_test() {
        double n = 80000;
        long tf = 11000;
        long df = 60000;
        double in_eq = (n/(1+df));
        System.out.println(in_eq);
        double value = tf * (Math.log10(in_eq) / Math.log10(500));
        System.out.println(value);
    }
}