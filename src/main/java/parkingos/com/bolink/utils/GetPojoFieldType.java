package parkingos.com.bolink.utils;



import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GetPojoFieldType<T> {

    public  Map<String, Integer> getFieldTypes(T t,Map<String,String> params) {
        Map<String,Integer> resutlMap = new HashMap<>();
        if (t == null) {
            return null;
        } else {
            Field[] declaredFields = t.getClass().getDeclaredFields();
            Field[] arr$ = declaredFields;
            String tableName = t.getClass().getName();
            for(int i$ = 0; i$ < arr$.length; ++i$) {
                Field field = arr$[i$];
                Type type = field.getGenericType();
                String fieldName = OrmUtil.camel2Underline(field.getName());
                if(isSelectField(tableName,fieldName,params)){
                    resutlMap.put(fieldName,FieldTypes.SELECT);
                }else if(fieldName.contains("time")){
                    resutlMap.put(fieldName,FieldTypes.DATE);
                }else if(type.toString().contains(FieldTypes._LONG)||type.toString().contains(FieldTypes._INTEGER)){
                    resutlMap.put(fieldName,FieldTypes.INT);
                }else if(type.toString().contains(FieldTypes._STRING)) {
                    resutlMap.put(fieldName, FieldTypes.STRING);
                }else if(type.toString().contains(FieldTypes._DOUBLE)) {
                    resutlMap.put(fieldName, FieldTypes.DOUBLE);
                }else if(type.toString().contains(FieldTypes._BIGDECIMAL)) {
                    resutlMap.put(fieldName, FieldTypes.DOUBLE);
                }
               /* if(type ==java.lang.Long.TYPE||type==Integer.TYPE||type==Double.TYPE){
                    System.out.println(field.getName()+":"+type.toString());
                }else {
                    ParameterizedType pt  = (ParameterizedType)type;
                    System.out.println((Class<?>)pt.getActualTypeArguments()[0]);
                }*/
            }
            System.out.println(resutlMap);
            return resutlMap;
        }
    }

   /* public static void  main(String [] args){
        GetPojoFieldType<CardRenewTb> gpt = new GetPojoFieldType<>();
        gpt.getFieldTypes(CardRenewTb);
    }*/

    /**
     * 新加的表，要注意把在页面是选择类型的字段注册在这里
     * 过滤是选择条件的字段，来自于页面的定义
     * @param tableName 表名
     * @param field 字段名
     * @return 是否是选择字段
     */
    private boolean isSelectField(String tableName,String field,Map<String,String> params){
        /*if(tableName.contains("CardRenewTb")){
            if(field.equals("pay_type"))
                return true;
        }*/
        //是否是下拉框选项
        if(!params.containsKey(field+"_end")){
            if(params.get(field)!=null&&params.get(field+"_start")!=null){
                if(params.get(field).equals(params.get(field+"_start"))){
                    return true;
                }
            }
        }
        return false;
    }

}
