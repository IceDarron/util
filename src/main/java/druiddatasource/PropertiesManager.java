package druiddatasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesManager {
    public static final Properties LOAD_PROPERTIES(String filename) {
        Properties prop = new Properties();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(getTopoResource(filename), "utf-8"));
            prop.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static final InputStream getTopoResource(String s) {
        InputStream stream = PropertiesManager.class.getClassLoader().getResourceAsStream(s);
        return stream;
    }

    public static String getString(Properties properties, String string) {
        return properties.getProperty(string);
    }

    public static void main(String[] args) {
        Properties properties = PropertiesManager.LOAD_PROPERTIES("druid.properties");
        System.out.println(properties.keySet());
    }
}
