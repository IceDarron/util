package serialization.fst;

import serialization.Target;

public class Test {

    public static void main(String[] args) {

        Target bean = new Target();
        bean.setUsername("xxxxx");
        bean.setPassword("123456");
        bean.setAge(1000000);

        byte[] serialize = FstUtils.serialize(bean);
        System.out.println("序列化size：" + serialize.length);
        Target target = (Target) FstUtils.unserialize(serialize);
        System.out.println(target);
    }
}
