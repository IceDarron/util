package serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import serialization.Target;

import java.io.ByteArrayOutputStream;

public class Test {
    public static void main(String[] args) {

        Target bean = new Target();
        bean.setUsername("xxxxx");
        bean.setPassword("123456");
        bean.setAge(1000000);

        // 测试Kryo工具类 提供了通过ThreadLocal方式获取Kryo实例
        byte[] tempByteArray = KryoUtils.writeToByteArray(bean);
        System.out.println("序列化size：" + tempByteArray.length);
        Target target = KryoUtils.readFromByteArray(tempByteArray);
        System.out.println(target);

        // 通过Kryo线程池获取实例
        Pool<Kryo> poolKryo = KryoPool.getKryoPool();
        Pool<Output> poolOutput = KryoPool.getOutputPool();
        Pool<Input> poolInput = KryoPool.getInputPool();
        Kryo kryo = poolKryo.obtain();
        Output output = poolOutput.obtain();
        Input input = poolInput.obtain();

        kryo.register(serialization.Target.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        output.setOutputStream(byteArrayOutputStream);
        kryo.writeClassAndObject(output, bean);
        output.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        System.out.println("序列化size：" + bytes.length);

        input.setBuffer(bytes);
        Target t = (Target) kryo.readClassAndObject(input);
        System.out.println(target);

        poolKryo.free(kryo);
    }

}
