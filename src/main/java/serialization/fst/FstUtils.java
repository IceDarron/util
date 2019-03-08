package serialization.fst;

import org.nustaq.serialization.FSTConfiguration;

public class FstUtils {

    static FSTConfiguration configuration = FSTConfiguration
            // .createDefaultConfiguration();
            .createStructConfiguration();

    public static byte[] serialize(Object obj) {
        return configuration.asByteArray(obj);
    }

    public static Object unserialize(byte[] sec) {
        return configuration.asObject(sec);
    }

}
