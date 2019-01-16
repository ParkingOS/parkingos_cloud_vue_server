package parkingos.com.bolink.service.redis;



public interface RedisService {


    /**
     * 设置缓存
     * @param key key
     * @param value 值
     */
    void set(String key, String value);


    /**
     * 设置缓存
     * @param key key
     * @param value 值
     * @param timeOut 有效期 单位秒
     */
    void set(String key, String value, Integer timeOut);


    /**
     * 取缓存值
     * @param key key
     * @return 存储的值
     */
    String get(String key);


    /**
     * 设置并取回上次缓存的值
     * @param key key
     * @param values 新值
     * @return 旧值
     */
    String getAndSet(String key, String values);

    /**
     * 设置缓存，存入整数
     * @param key key
     * @param value
     */
    void setLong(String key, Long value);

    /**
     * 取出缓存的整数
     * @param key key
     * @return 整数
     */
    Long getLong(String key);

}
