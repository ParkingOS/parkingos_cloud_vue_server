package parkingos.com.bolink.service;

import java.util.Map;

public interface SupperSearchService<T> {


    /**
     *
     * @param t pojo类
     * @param params 查询信息
     * @return 查询条件
     */
    Map<String,Object> getBaseSearch(T t,Map<String,String> params);
}
