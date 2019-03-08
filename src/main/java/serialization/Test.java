package serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import serialization.fst.FstUtils;
import serialization.kryo.KryoPool;
import serialization.kryo.KryoUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Test {

    // 测试各种序列化的性能
    public static void main(String[] args) throws IOException {
        Target bean = new Target();
        bean.setUsername("xxxxx");
        bean.setPassword("123456");
        bean.setAge(1000000);
        System.out.println("序列化&反序列化 性能测试：");
        // 压缩量级
        int maxSerializationCount = 1000000;

        // fst
        long fstSize = 0;
        long fstTime = System.currentTimeMillis();
        for (int i = 0; i < maxSerializationCount; i++) {
            byte[] serialize = FstUtils.serialize(bean);
            fstSize += serialize.length;
            Target target = (Target) FstUtils.unserialize(serialize);
        }
        System.out.println("fst序列化方案[序列化" + maxSerializationCount + "次]耗时：" + (System.currentTimeMillis() - fstTime) + "ms size:=" + fstSize);

        // kryo-ThreadLocal方式
        long kryoSize = 0;
        long kryoTime = System.currentTimeMillis();
        for (int i = 0; i < maxSerializationCount; i++) {
            byte[] serialize = KryoUtils.writeToByteArray(bean);
            kryoSize += serialize.length;
            Target target = (Target) KryoUtils.readFromByteArray(serialize);
        }
        System.out.println("kryo序列化方案[序列化" + maxSerializationCount + "次]耗时：" + (System.currentTimeMillis() - kryoTime) + "ms size:=" + kryoSize);

        // kryo-Pool方式
        Pool<Kryo> poolKryo = KryoPool.getKryoPool();
        Pool<Output> poolOutput = KryoPool.getOutputPool();
        Pool<Input> poolInput = KryoPool.getInputPool();
        Kryo kryo = poolKryo.obtain();
        Output output = poolOutput.obtain();
        Input input = poolInput.obtain();

        kryo.register(serialization.Target.class);

        long kryoPoolSize = 0;
        long kryoPoolTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < maxSerializationCount; i++) {
            output.setOutputStream(byteArrayOutputStream);
            kryo.writeClassAndObject(output, bean);
            output.flush();
            byte[] serialize = byteArrayOutputStream.toByteArray();
            kryoPoolSize += serialize.length;
            input.setBuffer(serialize);
            Target target = (Target) kryo.readClassAndObject(input);

            output.reset();
            byteArrayOutputStream.reset();
        }
        System.out.println("kryo序列化方案[序列化" + maxSerializationCount + "次]耗时：" + (System.currentTimeMillis() - kryoPoolTime) + "ms size:=" + kryoPoolSize);

        poolKryo.free(kryo);
        poolOutput.free(output);
        poolInput.free(input);
    }
}
