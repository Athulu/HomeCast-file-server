import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonBuilder {
    public static final String HLS_DIRECTORY = "";
    public static final String DASH_DIRECTORY = "";
    public static final String MOVIES_DIRECTORY = "http://192.168.1.107:8080/mp4/";
    public static final String IMAGES_DIRECTORY = "http://192.168.1.107:8080/images/";
    public static void main(String[] args) {

        JSONObject jsonString = new JSONObject();
        jsonString.put("name", "Movies");
        jsonString.put("hls", HLS_DIRECTORY);
        jsonString.put("dash", DASH_DIRECTORY);
        jsonString.put("mp4", MOVIES_DIRECTORY);
        jsonString.put("images", IMAGES_DIRECTORY);

        JSONObject jsonVideosString = new JSONObject();
        jsonVideosString.put("subtitle", "cos tam");

        JSONObject jsonSourcesString = new JSONObject();
        jsonSourcesString.put("type", "mp4");
        jsonSourcesString.put("mime", "videos/mp4");
        jsonSourcesString.put("url", "01chainsaw.mp4");

        JSONArray jsonSourcesArray = new JSONArray();
        jsonSourcesArray.put(jsonSourcesString);

        jsonVideosString.put("sources", jsonSourcesArray);
        jsonVideosString.put("thumb", "ccc.jpg");
        jsonVideosString.put("image-480x270", "ccc.jpg");
        jsonVideosString.put("image-780x1200", "bbb.jpg");
        jsonVideosString.put("title", "Chainsawman");
        jsonVideosString.put("studio", "odcinek 1");
        jsonVideosString.put("duration", 321);

        JSONArray jsonVideosArray = new JSONArray();
        jsonVideosArray.put(jsonVideosString);
        jsonString.put("videos", jsonVideosArray);
        System.out.println(jsonVideosArray);


        JSONArray jsonCategoriesArray = new JSONArray();
        jsonCategoriesArray.put(jsonString);

        JSONObject mainObj = new JSONObject();
        mainObj.put("categories", jsonCategoriesArray);

        String formattedJsonString = mainObj.toString(4);
        System.out.println(formattedJsonString);

        //new File("C:\\Users\\MyName\\filename.txt");
        File myObj = new File("D:\\chainsawman\\db.json");
        if (myObj.delete()) {
            System.out.println(">>> Deleted the file: " + myObj.getName());
        } else {
            System.out.println(">>> Failed to delete the file.");
        }

        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("D:\\chainsawman\\db.json");
            myWriter.write(formattedJsonString);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

