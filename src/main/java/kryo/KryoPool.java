package kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

public class KryoPool {
    // Pool constructor arguments: thread safe, soft references, maximum capacity
    public static Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
        protected Kryo create() {
            Kryo kryo = new Kryo();
            // Configure the Kryo instance.
            return kryo;
        }
    };

    Pool<Output> outputPool = new Pool<Output>(true, false, 16) {
        protected Output create () {
            return new Output(1024, -1);
        }
    };

    Pool<Input> inputPool = new Pool<Input>(true, false, 16) {
        protected Input create () {
//            return new Input(1024, -1);
            return null;
        }
    };
}
