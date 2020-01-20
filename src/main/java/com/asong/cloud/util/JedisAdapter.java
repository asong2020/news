package com.asong.cloud.util;

/**
 * create by asong on 2020/1/16
 */

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private Jedis jedis = null;
    private JedisPool pool = null;


    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }

    private Jedis getJedis()
    {
        return pool.getResource();
    }

    /**
     * 得到value
     * @param key
     * @return
     */
    public String get(String key)
    {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return getJedis().get(key);
        }catch (Exception e)
        {
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 设置键值
     * @param key
     * @param value
     */
    public void set(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.set(key,value);
        }catch (Exception e)
        {
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 将一个或多个成员添加到集合当中
     * @param key
     * @param value
     * @return
     */
    public long sadd(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e)
        {
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 从一组中删除一个或多个成员
     * @param key
     * @param value
     * @return
     */
    public long srem(String key,String value)
    {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e)
        {
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 确定给定是否给集合成员
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key,String value)
    {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e)
        {
            logger.error("发生异常"+e.getMessage());
            return false;
        }
        finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 获取集合中的成员数
     * @param key
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 自2.0.0起可用。
     *
     * 时间复杂度： O（1）
     *
     * 设置key为保留字符串，value并key在给定的秒数后设置为超时。
     * @param key
     * @param value
     */
    public void setex(String key, String value) {
        // 验证码, 防机器注册，记录上次注册时间，有效期3天
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 在列表前添加一个或多个元素
     * @param key
     * @param value
     * @return
     */
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除并获取列表中最后一个元素，或者阻塞直到一个可用
     * @param timeout
     * @param key
     * @return
     */
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
