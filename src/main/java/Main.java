public class Main {
    public static void main(String[] args) {

        ChatGPTDescribeGenerator chatGPT = new ChatGPTDescribeGenerator();
        JsonFileGenerator jsonFileGenerator = new JsonFileGenerator(chatGPT);
        jsonFileGenerator.createJsonFile();
//        ThumbnailGenerator.generateThumbnails();


    }
}
//        String aaa = "s01e01.chainsawman.mp4";
//        String[] bbb = aaa.split("\\.");
//        for(int i = 0; i < bbb.length; i++){
//            System.out.println(bbb[i]);
//        }