package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    static Properties properties = new Properties();

    static {
        String[] candidates = {
                "src/test/resources/config/config.properties",
                "src/test/resources/Config/config.properties"
        };

        for (String path : candidates) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        properties.load(fis);
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}