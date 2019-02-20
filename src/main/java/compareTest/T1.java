package compareTest;

import java.util.*;

/**
 * comparable 和 comparator的区别
 *
 * comparable接口实际上是出自java.lang包 它有一个 compareTo(Object obj)方法用来排序
 *
 * comparator接口实际上是出自 java.util 包它有一个compare(Object obj1, Object obj2)方法用来排序
 */
public class T1 {
    public static void main(String[] args) {

        List<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(-1);
        arrayList.add(3);
        arrayList.add(3);
        arrayList.add(-5);
        arrayList.add(7);
        arrayList.add(4);
        arrayList.add(-9);
        arrayList.add(-7);
        System.out.println(arrayList);

        Collections.sort(arrayList, Comparator.naturalOrder());

        System.out.println(arrayList);


        TreeMap<Person, String> pdata = new TreeMap<Person, String>();
        pdata.put(new Person("张三", 30), "zhangsan");
        pdata.put(new Person("李四", 20), "lisi");
        pdata.put(new Person("王五", 10), "wangwu");
        pdata.put(new Person("小红", 5), "xiaohong");

        System.out.println(pdata);


        Map<String, String> map = new TreeMap<String, String>();
        map.put("d", "ddddd");
        map.put("b", "bbbbb");
        map.put("a", "aaaaa");
        map.put("c", "ccccc");

        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        System.out.println(list);
    }
}

class Person implements Comparable<Person> {
    private String name;
    private int age;

    public Person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * TODO重写compareTo方法实现按年龄来排序
     */
    @Override
    public int compareTo(Person o) {
        // TODO Auto-generated method stub
        if (this.age > o.getAge()) {
            return 1;
        } else if (this.age < o.getAge()) {
            return -1;
        }
        return age;
    }
}