package bloomfilter;

/**
 * https://blog.csdn.net/u011489043/article/details/78990162
 * https://www.cnblogs.com/devilwind/p/7374017.html
 * https://blog.csdn.net/qq_30242609/article/details/71024458
 * https://blog.csdn.net/Alex_zmx/article/details/81143842
 */
public class TestRedisBloomFilter {

    public static void main(String[] args) {
        // 主要使用redis的string类型数据结构，字符串都是二进制型式存在的bitmap。可以使用bitset bitget bitpos等。
        // 要设计不同的hash函数，确定个数
        // 设定预插入量，容错率
        // 在redis中使用string做布隆过滤器，需要加前缀来确定键，便于找到键。（非布隆过滤器要求）
    }
}
