package com.asong.cloud.service;


import com.asong.cloud.util.JedisAdapter;
import com.asong.cloud.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 获取喜欢状态
     * @param userId  用户
     * @param entityType 新闻类型
     * @param entityId 新闻ID
     * @return
     */
    public int getLikeStatus(int userId,int entityType,int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId)))
        {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId))?-1:0;
    }

    /**
     * 添加喜欢
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long like(int userId,int entityType,int entityId){
        //再喜欢集合增加
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        //从反对集合里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        //返回喜欢集合成员个数
        return jedisAdapter.scard(likeKey);
    }

    /**
     * 取消喜欢
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long disLike(int userId,int entityType,int entityId)
    {
        //在反对集合里面增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        // 从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        //返回喜欢的个数
        return jedisAdapter.scard(likeKey);
    }
}
