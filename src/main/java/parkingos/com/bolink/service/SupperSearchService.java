package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface SupperSearchService<T> {


    /**
     *
     * @param t pojo类
     * @param params 查询信息
     * @return 查询条件
     */
    JSONObject supperSearch(T t,Map<String,String> params);

    Map<String,Object> getBaseSearch(T t, Map<String, String> params);
}
