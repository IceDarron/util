package serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class KryoPool {

    private volatile static Pool<Kryo> kryoPool;
    private volatile static Pool<Output> outputPool;
    private volatile static Pool<Input> inputPool;

    private KryoPool() {
    }

    private static void init() {
        kryoPool = new Pool<Kryo>(true, false, 8) {
            protected Kryo create() {
                Kryo k = new Kryo();
                k.setRegistrationRequired(false);
                ((com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy) k.getInstantiatorStrategy())
                        .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
                return k;
            }
        };

        outputPool = new Pool<Output>(true, false, 16) {
            protected Output create() {
                return new Output(1024, -1);
            }
        };

        inputPool = new Pool<Input>(true, false, 16) {
            protected Input create() {
                return new Input(new byte[1024]);
            }
        };
    }

    public static Pool<Kryo> getKryoPool() {
        if (kryoPool == null) {
            synchronized (KryoPool.class) {
                if (kryoPool == null) {
                    init();
                }
            }
        }
        return kryoPool;
    }

    public static Pool<Output> getOutputPool() {
        if (outputPool == null) {
            synchronized (KryoPool.class) {
                if (outputPool == null) {
                    init();
                }
            }
        }
        return outputPool;
    }

    public static Pool<Input> getInputPool() {
        if (inputPool == null) {
            synchronized (KryoPool.class) {
                if (inputPool == null) {
                    init();
                }
            }
        }
        return inputPool;
    }
}
