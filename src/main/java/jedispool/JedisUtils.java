package jedispool;

import com.google.common.base.Optional;

import java.util.Properties;

public class JedisUtils {

    private static Optional<String> possible;

    public static String getString(Properties properties, String str, String s) {
        possible = Optional.fromNullable(properties.getProperty(str));
        if (possible.isPresent()) {
            s = properties.getProperty(str);
        }
        return s;
    }

    public static Long getLong(Properties properties, String str, Long l) {
        possible = Optional.fromNullable(properties.getProperty(str));
        if (possible.isPresent()) {
            l = Long.parseLong(properties.getProperty(str).replaceAll("L","").replaceAll("l",""));
        }
        return l;
    }

    public static int getInt(Properties properties, String str, int i) {
        possible = Optional.fromNullable(properties.getProperty(str));
        if (possible.isPresent()) {
            i = Integer.parseInt(properties.getProperty(str));
        }
        return i;
    }

    public static boolean getBoolean(Properties properties, String str, Boolean bool) {
        possible = Optional.fromNullable(properties.getProperty(str));
        if (possible.isPresent()) {
            bool = Boolean.parseBoolean(properties.getProperty(str));
        }
        return bool;
    }

    public static boolean isNull(String string) {
        possible = Optional.fromNullable(string);
        return !possible.isPresent();
    }
}
