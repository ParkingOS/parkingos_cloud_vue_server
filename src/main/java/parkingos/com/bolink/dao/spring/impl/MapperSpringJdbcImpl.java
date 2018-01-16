package parkingos.com.bolink.dao.spring.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonMapper;
import parkingos.com.bolink.dao.spring.MybatisGeneratorSql;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class MapperSpringJdbcImpl extends JdbcTemplate implements CommonMapper {

    @Autowired
    private MybatisGeneratorSql generatorSql;

    Logger logger = Logger.getLogger(MybatisGeneratorSql.class);


    /**
     * 依赖注入dataSource
     */
    @Autowired
    @Override
    public void setDataSource(@Qualifier("master")DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Override
    public List<Map<String, Object>> selectByConditions(List<String> selective, String tableName, Map<String, Object> params,
                                                        PageOrderConfig pageOrderConfig, List<SearchBean> searchBeans) {
        Map<String,Object> preSqlMap = generatorSql.selectByConditions(selective,tableName,params,pageOrderConfig,searchBeans);
        if(preSqlMap!=null){
            String sql =(String) preSqlMap.get("sql");
            logger.info(preSqlMap);
            List<Object> values = (List<Object>)preSqlMap.get("values");
            Object[] objects = null;
            if(params != null){
                objects = new Object[params.size()];
                objects = values.toArray(objects);
            }
            return queryForList(sql,objects);
        }

        return null;
    }

    @Override
    public int selectCountByConditions(String tableName, Map<String, Object> params, List<SearchBean> searchBeans) {
        Map<String,Object> preSqlMap = generatorSql.selectCountByConditions(tableName,params,searchBeans);
        if(preSqlMap!=null) {
            String sql = (String) preSqlMap.get("sql");
            List<Object> values = (List<Object>)preSqlMap.get("values");
            Object[] objects = null;
            if(params != null){
                objects = new Object[params.size()];
                objects = values.toArray(objects);
            }
            logger.info(preSqlMap);
            return queryForObject(sql,objects, Integer.class);
        }
        return 0;
    }

    @Override
    public Map<String, Object> selectByPrimaryKey(List<String> selective, String tableName, Long id) {
        Map<String,Object> preSqlMap = generatorSql.selectByPrimaryKey(selective,tableName,id);
        if(preSqlMap!=null) {
            String sql = (String) preSqlMap.get("sql");
            List<Object> values = (List<Object>)preSqlMap.get("values");
            Object[] objects = null;
            if(selective != null){
                objects = new Object[selective.size()];
                objects = values.toArray(objects);
            }
            logger.info(preSqlMap);
            return queryForObject(sql,objects,Map.class);
        }
        return null;
    }

    @Override
    public int deleteByConditions(String tableName, Map<String, Object> params, List<SearchBean> searchBeans) {
        Map<String,Object> preSqlMap = generatorSql.deleteByConditions(tableName,params,searchBeans);
        return doUpdate(preSqlMap,params.size());
    }

    @Override
    public int insert(String tableName, Map<String, Object> params) {
        Map<String,Object> preSqlMap = generatorSql.insert(tableName,params);
        return doUpdate(preSqlMap,params.size());
    }

    @Override
    public int updateByConditions(String tableName, Map<String, Object> fields,
                                  Map<String, Object> params, List<SearchBean> searchBeans) {
        Map<String,Object> preSqlMap = generatorSql.updateByConditions(tableName,fields,params,searchBeans);
        return doUpdate(preSqlMap,params.size());
    }

    @Override
    public int updateByPrimaryKey(String tableName, Map<String, Object> params) {
        Map<String,Object> preSqlMap = generatorSql.updateByPrimaryKey(tableName,params);
        return doUpdate(preSqlMap,params.size());
    }

    @Override
    public Long selectSequence(String seqName) {
        String sql = generatorSql.selectSequence(seqName);
        return queryForObject(sql,Long.class);
    }

    @Override
    public List<Map<String, Object>> selectObjectBySql(String sql) {
        if(sql!=null&&(sql.toUpperCase().trim().startsWith("SELECT"))&&sql.toUpperCase().trim().contains("WHERE"))
            return queryForList(sql);
        return null;
    }

    private int doUpdate( Map<String,Object> preSqlMap,int length){
        if(preSqlMap!=null) {
            String sql = (String) preSqlMap.get("sql");
            List<Object> values = (List<Object>)preSqlMap.get("values");
            Object[] objects = null;
            if(length>0){
                objects = new Object[length];
                objects = values.toArray(objects);
            }
            logger.info(preSqlMap);
            return update(sql,objects);
        }
        return 0;
    }
}
