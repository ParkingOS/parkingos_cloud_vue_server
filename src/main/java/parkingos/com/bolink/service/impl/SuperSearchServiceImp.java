package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SuperSearchServiceImp<T> implements SupperSearchService<T> {

    Logger logger = Logger.getLogger(SupperSearchService.class);
    @Autowired
    private CommonDao commonDao;


    public  Map<String,Object> getBaseSearch(T t ,Map<String,String> params){
        //返回结果
        Map<String,Object> result = new HashMap<>();

        //分页，排序
        // orderfield=id,orderby=desc,
        PageOrderConfig config = new PageOrderConfig();
        String orderField = params.get("orderfield");
        String orderBy= params.get("orderby");
        if(!Check.isEmpty(orderField)&&!Check.isEmpty(orderBy)){
            config.setOrderInfo(orderField,orderBy);
            if(params.containsKey("rp")&&params.containsKey("page")){
                Integer pageSize =Integer.valueOf(params.get("rp"));
                Integer pageNum = Integer.valueOf(params.get("page"));
                config.setPageInfo(pageNum,pageSize);
            }
            result.put("config",config);
        }
        //需要查询的字段
        String fields = params.get("fieldsstr");
        logger.info(fields);
        //没有查询字段，返回   带着基本条件
        if(fields==null||"".equals(fields.trim())){
            result.put("base",t);
            return result;
        }
        //需要查询的字段数组
        String []queryFields = fields.split("__");

        GetPojoFieldType<T> getFieldTypeTool = new GetPojoFieldType<>();
        //取出所有字段的类型
        Map<String ,Integer> fieldTypes = getFieldTypeTool.getFieldTypes(t,params);
        //复杂查询字段

        List<String> supperQueryFields = new ArrayList<>();
        try {
            //基本查询字段
            List<String> baseFields = new ArrayList<>();
            for(String key : queryFields){
                //取出字段类型
                Integer fieldType = fieldTypes.get(key);
                if(fieldType== FieldTypes.SELECT){
                    baseFields.add(key);
                }else{
                    supperQueryFields.add(key);
                }
            }
            /**复杂查询**/
            if(!supperQueryFields.isEmpty()){
                List<SearchBean> searchBeanList = getSearchBeans(supperQueryFields,fieldTypes,baseFields,params);
                if(!searchBeanList.isEmpty()){
                    result.put("supper",searchBeanList);
                }
            }
            /**基础条件**/
            if(!baseFields.isEmpty()){
                T pojo = getBaseSearch(baseFields,fieldTypes,params,t);
                result.put("base",pojo);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        if(!result.containsKey("base"))
            result.put("base",t);
        return result;
    }

    @Override
    public JSONObject supperSearch(T t, Map<String, String> params) {
        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        int count =0;
        List<T> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        Map searchMap = getBaseSearch(t,params);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            T t1 =(T)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");
            count = commonDao.selectCountByConditions(t1,supperQuery);
            if(count>0){
                if (config == null) {
                    config = new PageOrderConfig();
                    config.setPageInfo(1, Integer.MAX_VALUE);
                }
                list  = commonDao.selectListByConditions(t1,supperQuery,config);
                if (list != null && !list.isEmpty()) {
                    for (T t2 : list) {
                        OrmUtil<T> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(t2);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        //result.put("page",Integer.parseInt(params.get("page")));
         if(params.get("page")!=null){
            result.put("page",Integer.parseInt(params.get("page")));
        }
        return result;
    }

    /**
     * 高级查询组件
     * @param params
     * @return
     */
    public List<SearchBean> getSearchBeans(List<String> supperQeuryFields, Map<String,
            Integer> fieldTypes, List<String> baseFields, Map<String,String> params){
        List<SearchBean> resultList = new ArrayList<>();

   
        for(String key : supperQeuryFields){
            Integer fieldType = fieldTypes.get(key);

            if(fieldType== FieldTypes.STRING){
                String value = params.get(key);
                if(Check.isEmpty(value))
                    continue;
                SearchBean bean = new SearchBean();
                bean.setFieldName(key);
                bean.setOperator( FieldOperator.LIKE);
                bean.setBasicValue(params.get(key));
                resultList.add(bean);
            }
            /**
             {pay_type_start=0,pay_type=0,
             trade_no=trade,
             car_number=,
             pay_time=1,  pay_time_start=1515409267000,
             orderfield=id,orderby=desc,
             card_id=,
             id=3,id_start=1111,id_end=
             user_id=between, user_id_end=33, user_id_start=22,
             }
             */
            else if(fieldType== FieldTypes.INT||fieldType== FieldTypes.DOUBLE||fieldType== FieldTypes.DATE){
                String operate = params.get(key);
                if(operate!=null){
                    SearchBean bean  = new SearchBean();
                    if(operate.equals( FieldOperateTypes.BETWEEN)){
                        bean.setOperator( FieldOperator.BETWEEN);
                        bean.setFieldName(key);
                        String start = params.get(key+"_start");
                        String end = params.get(key+"_end");
                        if(Check.isEmpty(start)|| Check.isEmpty(end)){
                            continue;
                        }
                        if(fieldType== FieldTypes.INT){
                            //时间类型不确定  前台页面传的值有可能是s,有可能是ms
                            if (start.length() > 10) {
                                bean.setStartValue(Long.valueOf(start)/1000);
                            } else {
                                bean.setStartValue(Long.valueOf(start));
                            }
                            if (end.length() > 10) {
                                bean.setEndValue(Long.valueOf(end)/1000);
                            } else {
                                bean.setEndValue(Long.valueOf(end));
                            }
                        }else if(fieldType== FieldTypes.DOUBLE){
                            bean.setStartValue(Double.valueOf( params.get(key+"_start") ));
                            bean.setEndValue(Double.valueOf( params.get(key+"_end") ));
                        }else if(fieldType== FieldTypes.DATE){
                            bean.setStartValue(Long.valueOf(start)/1000);
                            bean.setEndValue(Long.valueOf(end)/1000);
                        }

                        resultList.add(bean);
                    }else if(operate.equals( FieldOperateTypes.GREATER_THAN_OR_EQUAL)){
                        bean.setOperator( FieldOperator.GREATER_THAN_AND_EQUAL);
                        bean.setFieldName(key);
                        String start = params.get(key+"_start");
                        if(Check.isEmpty(start)){
                            continue;
                        }
                        if(fieldType== FieldTypes.INT){
                            bean.setStartValue(Long.valueOf( start ));
                        }else if(fieldType== FieldTypes.DOUBLE){
                            bean.setStartValue(Double.valueOf( start ));
                        }else if(fieldType== FieldTypes.DATE){
                            bean.setStartValue(Long.valueOf(start)/1000);
                        }
                        //bean.setStartValue(params.get(key+"_start"));
                        resultList.add(bean);
                    }else if(operate.equals( FieldOperateTypes.LESS_THAN_OR_EQUAL)){
                        bean.setOperator( FieldOperator.LESS_THAN_AND_EQUAL);
                        bean.setFieldName(key);
                        String end = params.get(key+"_end");
                        if(Check.isEmpty(end)){
                            continue;
                        }
                        if(fieldType== FieldTypes.INT){
                            bean.setEndValue(Long.valueOf( end ));
                        }else if(fieldType== FieldTypes.DOUBLE){
                            bean.setEndValue(Double.valueOf( end ));

                        }else if(fieldType==FieldTypes.DATE){
//                            bean.setStartValue(Long.valueOf(end)/1000);
                            bean.setEndValue(Long.valueOf(end)/1000);

                        }
                        resultList.add(bean);
                    }else if(operate.equals( FieldOperateTypes.EQUAL)){
                        baseFields.add(key);
                    }
                }
            }
        }
        return  resultList;
    }

    private T getBaseSearch(List<String> fields ,Map<String,Integer> fieldTypes,Map<String,String> params,T t){
//        T pojo = null;
        try {

            for(String f : fields){
                String value = params.get(f);
                Integer fieldType = fieldTypes.get(f);
                logger.info(f+":"+value);
                if(fieldType!= FieldTypes.STRING){
                    value = params.get(f+"_start");
                }
                if(!Check.isEmpty(value)){
                    Field field = t.getClass().getDeclaredField( StringUtils.underline2Camel(f));
                    String type = field.getType().toString();
                    field.setAccessible(true);

                    logger.info(f+":=====>>>>>>"+type);
                    logger.info(f+":=====>>>>>>"+value);
                    //时间类型不确定  前台页面传的值有可能是s,有可能是ms
                    if(type.contains(FieldTypes._INTEGER)){
                        if(value.length()>10){
                            field.set(t,Integer.valueOf(value.substring(0,10)));
                        }else{
                            field.set(t,Integer.valueOf(value));
                        }
//                        field.set(t,Integer.valueOf(value));
                    // 时间类型不确定  前台页面传的值有可能是s,有可能是ms
                    }else if(type.contains(FieldTypes._LONG)){
//                        field.set(t,Long.valueOf(value));
                        if(value.length()>10){
                            field.set(t,Long.valueOf(value)/1000);
                        }else{
                            field.set(t,Long.valueOf(value));
                        }
                    }else if(type.contains(FieldTypes._STRING)){

                        field.set(t,value);
                    }else if(type.contains( FieldTypes._BIGDECIMAL)){
                        field.set(t, BigDecimal.valueOf(Double.valueOf(value)));
                    }else if(type.contains( FieldTypes._DOUBLE)){
                        field.set(t,Double.valueOf(value));
                    }
                }
            }
//            pojo = t.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
