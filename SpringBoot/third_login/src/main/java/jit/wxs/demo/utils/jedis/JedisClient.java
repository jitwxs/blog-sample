package jit.wxs.demo.utils.jedis;

import java.util.List;
import java.util.Set;

/**
 * Jedis客户端接口
 * @author jitwxs
 */
public interface JedisClient {
    String set(String key, String value);

    String get(String key);

    Boolean exists(String key);

    Long del(String key);

    /**
     * 设置key过期时间（单位：s）
     */
    Long expire(String key, int seconds);

    /**
     * 获取key过期时间
     */
    Long ttl(String key);

    /**
     * 自加
     */
    Long incr(String key);

    /**
     * 设置hash类型
     */
    Long hset(String key, String field, String value);

    /**
     * 获取hash类型
     */
    String hget(String key, String field);

    /**
     * 删除hash类型中field
     */
    Long hdel(String key, String... field);

    /**
     * 指定hash中某个field是否存在
     */
    Boolean hexists(String key, String field);

    /**
     * 返回hash的value的List
     */
    List<String> hvals(String key);

    /**
     * 向set中添加成员，如果成员已存在，不再添加
     */
    Long sadd(String key, String... members);

    /**
     * 获取set所有成员
     */
    Set<String> smembers(String key);

    /**
     * 判断指定成员是否存在于set中
     */
    Boolean sismember(String key, String member);

    /**
     * 向set中删除成员
     */
    Long srem(String key, String... members);

    /**
     * 求set中成员数量
     */
    Long scard(String key);

    /**
     * 随即返回一个set成员
     */
    String srandmember(String key);
}