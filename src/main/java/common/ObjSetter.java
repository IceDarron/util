package common;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>用于给任意对象一些空数据，使用反射机制</p>
 *
 * @author : rongxuning
 * @date : 2019-09-14 22:02
 **/
public class ObjSetter {

    public static String DEFAULT_STRING = "Empty";
    public static Integer DEFAULT_INTEGER = -1;
    public static Long DEFAULT_LONG = -1L;
    public static Double DEFAULT_DOUBLE = -1D;
    public static BigDecimal DEFAULT_BIGDECIMAL = new BigDecimal(-1);

    public static <T> T sweepPropertiesWithDefault(Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            ObjSetter.sweepProperties(obj, DEFAULT_STRING, DEFAULT_INTEGER, DEFAULT_LONG, DEFAULT_DOUBLE, DEFAULT_BIGDECIMAL);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void sweepPropertiesWithDefault(Object obj) {
        ObjSetter.sweepProperties(obj, DEFAULT_STRING, DEFAULT_INTEGER, DEFAULT_LONG, DEFAULT_DOUBLE, DEFAULT_BIGDECIMAL);
    }

    public static void sweepProperties(Object obj, String str, Integer i, Long l) {
        sweepProperties(obj, str, i, l, Double.valueOf(i), BigDecimal.valueOf(i));
    }

    public static void sweepProperties(Object obj, String str, Integer i, Long l, Double d, BigDecimal b) {
        try {
            Method[] ms = obj.getClass().getMethods();
            for (Method m : ms) {
                if (m.getName().startsWith("set") && !m.getName().equals("setId")) {
                    Class c = m.getParameterTypes()[0];
                    if (c.getSimpleName().equals("String")) {
                        m.invoke(obj, str);
                    }
                    if (c.getSimpleName().equals("Date")) {
                        m.invoke(obj, new Date());
                    }
                    if (c.getSimpleName().equals("Integer")) {
                        m.invoke(obj, i);
                    }
                    if (c.getSimpleName().equals("Double")) {
                        m.invoke(obj, d);
                    }
                    if (c.getSimpleName().equals("Long")) {
                        m.invoke(obj, l);
                    }
                    if (c.getSimpleName().equals("BigDecimal")) {
                        m.invoke(obj, b);
                    }
                    if (c.getSimpleName().equals("Byte")) {
                        m.invoke(obj, Byte.valueOf(i.toString()));
                    }
                }
                if (m.getName().equals("setDeleted")) {
                    m.invoke(obj, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}