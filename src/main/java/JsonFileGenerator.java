import converters.FileNamesConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.io.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class JsonFileGenerator {
    public static final String JSON_DB_FILE = "C:\\HomeCast\\db.json";
    public static final String HLS_DIRECTORY = "";
    public static final String DASH_DIRECTORY = "";
    public static final String MOVIES_DIRECTORY = "http://192.168.1.107:8080/mp4/";
    public static final String IMAGES_DIRECTORY = "http://192.168.1.107:8080/images/";
    public static ArrayList<String> listOfFileNames;
    private static ChatGPTDescribeGenerator chatGPTDescribeGenerator;

    public JsonFileGenerator(ChatGPTDescribeGenerator chatGPTDescribeGenerator) {
        JsonFileGenerator.chatGPTDescribeGenerator = chatGPTDescribeGenerator;
    }

    public void createJsonFile(){
        setFileList();
        createJsonFileFromString(generateJsonString());
    }

    public static void setFileList() {
        //TODO: przerobić jednak na lokalna
        // to potem wykorzystam:
        // https://stackoverflow.com/questions/25341219/get-files-name-in-directory

        LinkedList<String> fileNamesList = new LinkedList<>();
        try {
            URL url = new URL(MOVIES_DIRECTORY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(".mp4")) {
                    int startIndex = line.indexOf("<a href");
                    String result = "";
                    if (startIndex != -1) {
                        result = line.substring(startIndex);
                    }

                    String safe = Jsoup.clean(result, Safelist.simpleText());
                    fileNamesList.add(safe);
                }
            }
            reader.close();
            conn.disconnect();
        } catch (IOException e) {
            System.out.println(e);
        }

        listOfFileNames = new ArrayList<>(fileNamesList);
    }


    public String generateJsonString() {

        JSONObject jsonString = new JSONObject();
        jsonString.put("name", "Movies");
        jsonString.put("hls", HLS_DIRECTORY);
        jsonString.put("dash", DASH_DIRECTORY);
        jsonString.put("mp4", MOVIES_DIRECTORY);
        jsonString.put("images", IMAGES_DIRECTORY);

        JSONObject jsonSourcesString;
        JSONArray jsonVideosArray = new JSONArray();

        int season;
        int episode;
        String describe;

        for (String name: listOfFileNames) {
            FileNamesConverter converter = FileNamesConverterFactory.getFileNameConverter(name);
            season = Integer.parseInt(converter.getEpisode().substring(1,3));
            episode = Integer.parseInt(converter.getEpisode().substring(4,6));
            describe = chatGPTDescribeGenerator.getDescription(season,episode,converter.getName());

            JSONObject jsonVideosString = new JSONObject();
            jsonVideosString.put("subtitle", describe);
            JSONArray jsonSourcesArray = new JSONArray();
            jsonSourcesString = new JSONObject();
            jsonSourcesString.put("type", "mp4");
            jsonSourcesString.put("mime", "videos/mp4");
            jsonSourcesString.put("url", name);
            jsonSourcesArray.put(jsonSourcesString);

            jsonVideosString.put("sources", jsonSourcesArray);
            jsonVideosString.put("thumb", name.replace(".mp4", "") +"480x270.png");
            jsonVideosString.put("image-480x270", name.replace(".mp4", "") +"480x270.png");
            jsonVideosString.put("image-780x1200", "bbb.png");
            jsonVideosString.put("title", converter.getName());
            jsonVideosString.put("studio", converter.getEpisode());
            jsonVideosString.put("duration", 100);
            jsonVideosArray.put(jsonVideosString);
        }

        jsonString.put("videos", jsonVideosArray);

        JSONArray jsonCategoriesArray = new JSONArray();
        jsonCategoriesArray.put(jsonString);

        JSONObject object = new JSONObject();
        object.put("categories", jsonCategoriesArray);

        return object.toString(4);
    }


    public void createJsonFileFromString(String formattedJsonString) {
        try {
            FileWriter myWriter = new FileWriter(JSON_DB_FILE);
            myWriter.write(formattedJsonString);
            myWriter.close();
            System.out.println(">>> Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("*** An error occurred.");
            e.printStackTrace();
        }
    }
}

