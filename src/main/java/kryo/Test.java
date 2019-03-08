package kryo;

import com.esotericsoftware.kryo.Kryo;

public class Test {
    public static void main(String[] args) {
        // 测试Kryo工具类 提供了通过ThreadLocal方式获取Kryo实例
        byte[] tempByteArray = KryoUtil.writeToByteArray("test");
        Object object = KryoUtil.readFromByteArray(tempByteArray);
        System.out.println(object);

        // 通过Kryo线程池获取实例
        Kryo kryo = KryoPool.kryoPool.obtain();
        // ...
        KryoPool.kryoPool.free(kryo);
    }

}
