package parkingos.com.bolink.dao.spring;


import org.springframework.stereotype.Component;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MybatisGeneratorSql {


    public Map<String,Object> selectByConditions(List<String> selective, String tableName,
                                                 Map<String, Object> params,
                                                 PageOrderConfig pageOrderConfig, List<SearchBean> searchBeans){
        StringBuffer sql = new StringBuffer(creatFieldSql(selective,tableName));
        Map<String,Object> baseSql = createBaseSql(params);
        String bsql = (String)baseSql.get("sql");
        sql.append(bsql);
        List<Object> values = (List<Object>)baseSql.get("values");
        Map<String,Object> supperSql = createSupperSql(searchBeans);
        if(supperSql!=null){
            if(!bsql.contains("WHERE"))
                sql.append(" WHERE ");
            else
                sql.append(" AND ");

            bsql = (String)supperSql.get("sql");
            sql.append(bsql);
            List<Object> suppervalues = (List<Object>)supperSql.get("values");
            values.addAll(suppervalues);
        }
        sql.append(getPageLimitSql(pageOrderConfig));
        Map<String,Object> result = new HashMap<>();
        result.put("sql",sql.toString());
        result.put("values",values);
        return result;
    }

    public Map<String,Object> selectCountByConditions(String tableName, Map<String, Object> params,
                                          List<SearchBean> searchBeans){
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM "+tableName+" ");

        Map<String,Object> baseSql = createBaseSql(params);
        String bsql = (String)baseSql.get("sql");
        sql.append(bsql);
        List<Object> values = (List<Object>)baseSql.get("values");

        Map<String,Object> supperSql = createSupperSql(searchBeans);
        if(supperSql!=null){
            if(!bsql.contains("WHERE"))
                sql.append(" WHERE ");
            else
                sql.append(" AND ");

            bsql = (String)supperSql.get("sql");
            sql.append(bsql);
            List<Object> suppervalues = (List<Object>)supperSql.get("values");
            values.addAll(suppervalues);
        }

        Map<String,Object> result = new HashMap<>();
        result.put("sql",sql.toString());
        result.put("values",values);
        return result;
    }

    public Map<String,Object> selectByPrimaryKey(List<String> selective, String tableName, Long id){
        StringBuffer sql = new StringBuffer(creatFieldSql(selective,tableName));
        sql.append(" WHERE ID=? ");
        List<Object> values = new ArrayList<>();
        values.add(id);
        Map<String,Object> result = new HashMap<>();
        result.put("sql",sql.toString());
        result.put("values",values);
        return result;
    }

    public  Map<String,Object> deleteByConditions(String tableName, Map<String, Object> params,
                                     List<SearchBean> searchBeans){
        StringBuffer sql = new StringBuffer("DELETE FROM "+tableName);
        Map<String,Object> baseSql = createBaseSql(params);
        String bsql = (String)baseSql.get("sql");
        sql.append(bsql);
        List<Object> values = (List<Object>)baseSql.get("values");
        Map<String,Object> supperSql = createSupperSql(searchBeans);
        List<Object> suppervalues = (List<Object>)supperSql.get("values");
        values.addAll(suppervalues);
        if(values.isEmpty()){
            return null;
        }
        Map<String,Object> result = new HashMap<>();
        result.put("sql",sql.toString());
        result.put("values",values);
        return result;
    }
    public Map<String,Object> insert(String tableName, Map<String, Object> params){
        StringBuffer sql = new StringBuffer("INSERT INTO "+tableName);
        StringBuffer fields = new StringBuffer();
        StringBuffer vs = new StringBuffer();
        List<Object> values = new ArrayList<>();
        if(params!=null){
            int i=0;
            for(String key : params.keySet()){
                if(i==0){
                    fields.append("("+key);
                    vs.append("(?");

                }else if(i<params.size()-1){

                    fields.append(","+key);
                    vs.append(",?");
                }else {
                    if(i==0){
                        fields.append(")");
                        vs.append(")");
                    }else{
                        fields.append(","+key+")");
                        vs.append(",?)");
                    }
                }
                values.add(params.get(key));
                i++;
            }
            Map<String,Object> result = new HashMap<>();
            result.put("sql",sql.toString()+fields.toString()+" values "+vs.toString());
            result.put("values",values);
            return result;
        }
        return null;
    }
    public Map<String,Object> updateByConditions(String tableName, Map<String, Object> fields,
                                     Map<String, Object> params, List<SearchBean> searchBeans){
        StringBuffer sql = new StringBuffer("UPDATE "+tableName+" SET ");
        Map<String,Object> resultMap = new HashMap<>();
        List<Object> values = new ArrayList<>();
        if(fields!=null&&!fields.isEmpty()){
            int i=0;
            for(String key : fields.keySet()){
                if(i==0)
                    sql.append(key +" =? ");
                else
                    sql.append(","+key+"=? ");
                values.add(fields.get(key));
                i++;
            }
            if(params!=null){
                Map<String,Object> baseSql = createBaseSql(params);
                String bsql = (String)baseSql.get("sql");
                sql.append(bsql);
                List<Object> basevalues = (List<Object>)baseSql.get("values");
                values.addAll(basevalues);
                Map<String,Object> supperSql = createSupperSql(searchBeans);
                if(supperSql!=null){
                    if(!bsql.contains("WHERE"))
                        sql.append(" WHERE ");
                    else
                        sql.append(" AND ");

                    bsql = (String)supperSql.get("sql");
                    sql.append(bsql);
                    List<Object> suppervalues = (List<Object>)supperSql.get("values");
                    values.addAll(suppervalues);
                }
                if(sql.toString().contains("WHERE")){
                    Map<String,Object> result = new HashMap<>();
                    result.put("sql",sql.toString());
                    result.put("values",values);
                    return result;
                }
            }
        }
        return null;
    }
    public  Map<String,Object> updateByPrimaryKey(String tableName, Map<String, Object> params){
        StringBuffer sql = new StringBuffer("UPDATE "+tableName+" SET ");
        Map<String,Object> resultMap = new HashMap<>();
        List<Object> values = new ArrayList<>();
        if(params!=null&&params.get("id")!=null){
            Long id =(Long)params.get("id");
            int i=0;
            for(String key : params.keySet()){
                if(!key.equals("id")){
                    if(i==0){
                        sql.append(key +"= ?");
                    }else{
                        sql.append(" , "+key +"= ?");
                    }
                    values.add(params.get(key));
                    i++;
                }
            }
            resultMap.put("sql",sql.toString()+" WHERE ID=? ");
            values.add(id);
            resultMap.put("values",values);
        }else {
            return null;
        }
        return resultMap;
    }

    public String selectSequence(String seqName){
        String sql = "SELECT nextval('"+seqName+"'::REGCLASS)";
        return sql;
    }

    private String creatFieldSql(List<String> selective,String tableName){
        StringBuffer sql = new StringBuffer("SELECT ");
        if(selective!=null){
            int i = 0;
            for(String f : selective){
                if(i==0){
                    sql.append(f);
                }else{
                    sql.append(","+f.toUpperCase());
                }
                i++;
            }
        }else {
            sql.append(" * ");
        }
        sql.append("FROM "+tableName);
        return sql.toString();
    }

    private Map<String,Object> createBaseSql( Map<String, Object> params){
        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();
        if(params!=null&&!params.isEmpty()){
            sql.append(" WHERE ");
            int i = 0;
            for(String key : params.keySet()){
                if(i==0){
                    sql.append(key.toUpperCase() +"=? ");
                }else{
                    sql.append("AND " +key +"=? ");
                }
                values.add(params.get(key));
                i++;
            }
        }
        Map<String,Object> result= new HashMap<>();
        result.put("sql",sql.toString());
        result.put("values",values);
        return result;
    }

    private Map<String,Object> createSupperSql(List<SearchBean> searchBeans){
//        System.out.println("========>>MybatisGeneratorSql"+searchBeans.size());
        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();
        Map<String,Object> result= new HashMap<>();
        if(searchBeans==null||searchBeans.isEmpty()){
            return null;
        }
        int j =0;
        for(SearchBean bean:searchBeans){
            Object baseValue = bean.getBasicValue();
            String valueType = baseValue instanceof  List ? "list":"string";
            if(j==0){
                sql.append(bean.getFieldName());
            }else{
                sql.append(" AND "+bean.getFieldName());
            }

            switch (bean.getOperator()){
                case "gt":
                    sql.append(" > ?");
                    values.add(bean.getStartValue()==null?bean.getBasicValue():bean.getStartValue());
                    break;
                case "ge":
                    sql.append(" >= ?");
                    values.add(bean.getStartValue()==null?bean.getBasicValue():bean.getStartValue());
                    break;
                case "lt" :
                    sql.append(" < ?");
                    values.add(bean.getEndValue()==null?bean.getBasicValue():bean.getEndValue());
                    break;
                case "le":
                    sql.append(" <= ?");
                    values.add(bean.getEndValue()==null?bean.getBasicValue():bean.getEndValue());
                    break;
                case "bt":
                    sql.append(" BETWEEN ? AND ? ");
                    values.add(bean.getStartValue());
                    values.add(bean.getEndValue());
                    break;
                case "like":
                    sql.append(" like ? ");
                    if(valueType.contains("string"))
                        values.add("%"+bean.getBasicValue()+"%");
                    else
                        values.add("%"+((List)bean.getBasicValue()).get(0)+"%");
                    break;
                case "equal":
                    sql.append(" = ? ");
                    values.add(bean.getBasicValue());
                    break;
                case "not":
                    sql.append(" not in(");
                    List<Object> value =(List<Object>)bean.getBasicValue();
                    int index =0;
                    for(Object o : value){
                        if(index==0)
                            sql.append(" ? ");
                        else
                            sql.append(",?");
                        values.add(o);
                        index++;
                    }
                    sql.append(")");
                    break;
                case "in":
                    sql.append(" IN( ");
                    List<Object> items =(List<Object>)bean.getBasicValue();
                    int i =0;
                    for(Object o : items){
                        if(i==0)
                            sql.append("? ");
                        else
                            sql.append(",?");
                        values.add(o);
                        i++;
                    }
                    sql.append(")");
                    break;
            }
            j++;
//            result.put("sql",sql.toString());
//            result.put("values",values);
//            return result;

        }
        /**
         * case GREATER_THAN_AND_EQUAL:
         return "ge";
         case LESS_THAN:
         return "lt";
         case LESS_THAN_AND_EQUAL:
         return "le";
         case CONTAINS:
         return "in";
         case LIKE:
         return "like";
         case BETWEEN:
         return "bt";
         default:
         return "equal";
         }
         */
        result.put("sql",sql.toString());
        result.put("values",values);
        return result;
    }

    private String getPageLimitSql(PageOrderConfig pageOrderConfig){
        StringBuffer sql = new StringBuffer(" ORDER BY ");
        if(pageOrderConfig.getOrderField()!=null){
            sql.append(pageOrderConfig.getOrderField());
        }else{
            sql.append("ID");
        }
        if(pageOrderConfig.getOrderType()!=null){
            sql.append(" "+pageOrderConfig.getOrderType());
        }else {
            sql.append(" DESC ");
        }
        if(pageOrderConfig.getLimit()!=null&&pageOrderConfig.getOffset()!=null)
            sql.append(" LIMIT "+pageOrderConfig.getLimit()+" OFFSET "+pageOrderConfig.getOffset());
        return sql.toString();
    }

}
