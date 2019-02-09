package bloomfilter;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

/**
 * https://blog.csdn.net/top_code/article/details/51280862  样例
 * https://github.com/google/guava  源码
 */
public class TestGuavaBloomFilter {

    // 定义布隆过滤器
    private final static BloomFilter<String> dealIdBloomFilter = BloomFilter.create(new Funnel<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void funnel(String arg0, PrimitiveSink arg1) {

            arg1.putString(arg0, Charsets.UTF_8);
        }

    }, 1024 * 1024 * 32, 0.0000001D); // false positive probability: 1.0E-7 千万分之一

    /**
     * 判断是否已存在，不存在则放入
     */
    public static boolean containsId(String id) {
        if (Strings.isNullOrEmpty(id)) {
            System.out.println("id is null!");
            return true;
        }
        boolean exists = dealIdBloomFilter.mightContain(id);
        if (!exists) {
            dealIdBloomFilter.put(id);
        }
        return exists;
    }

    public static void main(String[] args) {
        String id = "0"; // 测试ID
        System.out.println(dealIdBloomFilter);
        System.out.println("test_id has been exist: " + containsId(id));
        System.out.println("total id`s num: " +  dealIdBloomFilter.approximateElementCount()); // 近似测量计数
        System.out.println("test_id has been exist: " + containsId(id));
        System.out.println("total id`s num: " +  dealIdBloomFilter.approximateElementCount());
    }
}
