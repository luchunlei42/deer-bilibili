package com.chunlei.bili.thumbup.utils;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloomFilterUtil {
    @Autowired
    RedissonClient redissonClient;

    /**
     * 初始化一个布隆过滤器
     *
     * @param expectedInsertions 预期元素数量
     * @param falseProbability   误判率
     * @return 是否创建成功
     */
    public <T> boolean initBloomFilter(RBloomFilter<T> bloomFilter, long expectedInsertions, double falseProbability) {
        return bloomFilter.tryInit(expectedInsertions, falseProbability);
    }

    /**
     * 获取布隆过滤器
     */
    public <T> RBloomFilter<T> getBloomFilter(String key) {
        return redissonClient.getBloomFilter(key);
    }

    /**
     * 在布隆过滤器中增加一个值
     */
    public <T> boolean addInBloomFilter(RBloomFilter<T> bloomFilter, T value) {
        try {
            bloomFilter.add(value);
        } catch (IllegalStateException e) {
            initBloomFilter(bloomFilter, 50000L, 0.01);
        }

        return bloomFilter.add(value);
    }

    /**
     * 判断某值是否存在于过滤器中
     */
    public <T> boolean containsInBloomFilter(RBloomFilter<T> bloomFilter, T value) {
        try {
            bloomFilter.contains(value);
        } catch (IllegalStateException e) {
            initBloomFilter(bloomFilter, 50000L, 0.01);
        }

        return bloomFilter.contains(value);
    }

}
