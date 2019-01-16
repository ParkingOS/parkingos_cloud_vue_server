package parkingos.com.bolink.service.redis.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import parkingos.com.bolink.service.redis.RedisService;

import java.util.concurrent.TimeUnit;

@Component
public class RedisServiceImpl implements RedisService {


    Logger logger = Logger.getLogger(RedisService.class);
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key,value);
        }catch (Exception e){
            logger.error("redis is invalid",e);
        }
    }

    @Override
    public void set(String key, String value, Integer timeOut) {
        try {
            redisTemplate.opsForValue().set(key,value,timeOut,TimeUnit.SECONDS);
        }catch (Exception e){
            logger.error("redis is invalid");
        }
    }

    @Override
    public String get(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            return value!=null?(String)value:null;
        }catch (Exception e){
            logger.error("redis is invalid");
        }
        return null;
    }

    @Override
    public String getAndSet(String key, String values) {
        try {
            Object value = redisTemplate.opsForValue().getAndSet(key,values);
            return  value==null?null:(String)value;
        }catch (Exception e){
            logger.error("redis is invalid");
        }
        return  null;
    }

    @Override
    public void setLong(String key, Long value) {
        try {
            redisTemplate.opsForValue().increment(key,value);
        }catch (Exception e){
            logger.error("redis is invalid");
        }
    }

    @Override
    public Long getLong(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if(value instanceof Long)
                return (Long)value;
        }catch (Exception e){
            logger.error("redis is invalid");
        }
        return null;
    }
}
