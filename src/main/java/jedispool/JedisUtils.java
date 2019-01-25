package jedispool;

import com.google.common.base.Strings;

import java.util.Properties;

public class JedisUtils {

    public static String getString(Properties properties, String str, String s) {
        boolean isNullOrEmpty = Strings.isNullOrEmpty(properties.getProperty(str));
        if (isNullOrEmpty) {
            s = properties.getProperty(str);
        }
        return s;
    }

    public static Long getLong(Properties properties, String str, Long l) {
        boolean isNullOrEmpty = Strings.isNullOrEmpty(properties.getProperty(str));
        if (isNullOrEmpty) {
            l = Long.parseLong(properties.getProperty(str).replaceAll("L", "").replaceAll("l", ""));
        }
        return l;
    }

    public static int getInt(Properties properties, String str, int i) {
        boolean isNullOrEmpty = Strings.isNullOrEmpty(properties.getProperty(str));
        if (isNullOrEmpty) {
            i = Integer.parseInt(properties.getProperty(str));
        }
        return i;
    }

    public static boolean getBoolean(Properties properties, String str, Boolean bool) {
        boolean isNullOrEmpty = Strings.isNullOrEmpty(properties.getProperty(str));
        if (isNullOrEmpty) {
            bool = Boolean.parseBoolean(properties.getProperty(str));
        }
        return bool;
    }

    public static boolean isNull(String string) {
        return Strings.isNullOrEmpty(string);
    }
}
