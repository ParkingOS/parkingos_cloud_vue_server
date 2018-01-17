package parkingos.com.bolink.dao.spring;


import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * 通用数据库操作接口
 * @author Laoyao
 */
public interface CommonMapper {

    List<Map<String, Object>> selectByConditions( List<String> selective,
                                                 String tableName,
                                                  Map<String, Object> params,
                                                  PageOrderConfig pageOrderConfig,
                                                 List<SearchBean> searchBeans);


    int selectCountByConditions(String tableName,
                                Map<String, Object> params,
                                 List<SearchBean> searchBeans);

    Map<String,Object> selectByPrimaryKey(List<String> selective,
                                          String tableName,
                                          Long id);

	int deleteByConditions(String tableName,
                           Map<String, Object> params,
                           List<SearchBean> searchBeans);

    int insert(String tableName,
               Map<String, Object> params);
	

    int updateByConditions(String tableName,
                           Map<String, Object> fields,
                           Map<String, Object> params,
                            List<SearchBean> searchBeans);
    
    int updateByPrimaryKey(String tableName,
                           Map<String, Object> params);

    Long selectSequence(String seqName);

    List<Map<String, Object>> selectObjectBySql(String sql);
}
