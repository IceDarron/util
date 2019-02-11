package bloomfilter;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import jedispool.JedisPoolManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * https://blog.csdn.net/u011489043/article/details/78990162  Redis之bitmap
 * https://www.cnblogs.com/devilwind/p/7374017.html  redis-bitmap统计活跃用户
 * https://blog.csdn.net/qq_30242609/article/details/71024458  布隆过滤器redis实现
 * https://blog.csdn.net/Alex_zmx/article/details/81143842  布隆过滤器redis实现
 * https://blog.csdn.net/wuxing26jiayou/article/details/79544383  Redis-Bitmap详解
 * <p>
 * redis命令：
 * Redis提供的Bitmap这个“数据结构”可以实现对位的操作。Bitmaps本身不是一种数据结构，实际上就是字符串，但是它可以对字符串的位进行操作。
 * 可以把Bitmaps想象成一个以位为单位数组，数组中的每个单元只能存0或者1，数组的下标在bitmaps中叫做偏移量。单个bitmap的最大长度是512MB，即2^32个比特位。
 * <p>
 * setbit key offset value    修改位上的值，并将原位上的值返回
 * getbit key offset          返回指定位上的值
 * <p>
 * offset的0是从最左开始，即从下角标来看是 【0,1,2,3,4,5,6,7...】
 */
public class TestRedisBloomFilter {

    // 预计插入量
    private long expectedInsertions = 1000;

    // 可接受的错误率
    private double fpp = 0.001F; // false positives probability

    // 布隆过滤器的键在Redis中的前缀 利用它可以统计过滤器对Redis的使用情况
    private String redisKeyPrefix = "bf:";

    // bit数组长度
    private long numBits = optimalNumOfBits(expectedInsertions, fpp);
    // hash函数数量
    private int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);

    private Jedis jedis;

    public TestRedisBloomFilter() {
    }

    public TestRedisBloomFilter(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 计算hash函数个数
     */
    public int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    /**
     * 计算bit数组长度
     */
    public long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 判断keys是否存在于集合key中
     * getbit key offset
     *
     * @param key
     * @param target
     */
    public boolean isExist(String key, String target) {
        long[] indexArray = getIndexArray(target);
        boolean result;
        Pipeline pipeline = jedis.pipelined();
        try {
            for (long index : indexArray) {
                pipeline.getbit(getRedisKey(key), index);
            }
            result = !pipeline.syncAndReturnAll().contains(false);
        } finally {
            try {
                pipeline.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!result) {
            put(key, target);
        }
        return result;
    }

    /**
     * 将key存入redis bitmap
     */
    private void put(String key, String target) {
        long[] indexArray = getIndexArray(target);
        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
        Pipeline pipeline = jedis.pipelined();
        try {
            for (long index : indexArray) {
                pipeline.setbit(getRedisKey(key), index, true);
            }
            pipeline.sync();
        } finally {
            try {
                pipeline.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据key获取bitmap下标
     */
    private long[] getIndexArray(String target) {
        long hash1 = hash(target);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            long combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            result[i] = combinedHash % numBits;
        }
        return result;
    }

    /**
     * 获取一个hash值
     */
    private long hash(String target) {
        Charset charset = Charset.forName("UTF-8");
        return Hashing.murmur3_128().hashObject(target, Funnels.stringFunnel(charset)).asLong();
    }

    private String getRedisKey(String key) {
        return redisKeyPrefix + key;
    }

    /**
     * 主要使用redis的string类型数据结构，字符串都是二进制型式存在的bitmap。可以使用bitset bitget bitpos等。
     * 要设计不同的hash函数，确定个数
     * 设定预插入量，容错率
     * 在redis中使用string做布隆过滤器，需要加前缀来确定键，便于找到键。（非布隆过滤器要求）
     */
    public static void main(String[] args) {
        Jedis jedis = JedisPoolManager.getJedis();
        TestRedisBloomFilter t = new TestRedisBloomFilter(jedis);

        System.out.println(t.isExist("test", "aa"));
    }
}
