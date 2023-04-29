import converters.DefaultConverter;
import converters.FileNamesConverter;

public class FileNamesConverterFactory {
    public static FileNamesConverter getFileNameConverter(String name) {
        int count = name.split(".").length;
        if (count == 3) {
            return new DefaultConverter(name);
        }
        return new DefaultConverter(name);
    }
}

//        switch (countOfDotes) {
//            case 2 -> return new DefaultConverter();
//            default -> return new DefaultConverter();
//        };