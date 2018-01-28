package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarpicTb;
import parkingos.com.bolink.models.LiftRodTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.LiftRodService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.CustomDefind;
import parkingos.com.bolink.utils.MongoClientFactory;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiftRodServiceImpl implements LiftRodService {

    Logger logger = Logger.getLogger(LiftRodServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<LiftRodTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        LiftRodTb liftRodTb = new LiftRodTb();
        liftRodTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(liftRodTb,reqmap);
        return result;
    }

    @Override
    public byte[] getLiftRodPicture(String comid, String liftrodId) {
        if(liftrodId!=null && comid !=null){
            DB db = MongoClientFactory.getInstance().getMongoDBBuilder("zld");
            CarpicTb carpicTb = new CarpicTb();
            carpicTb.setLiftrodId(liftrodId);
            carpicTb.setComid(comid);
            carpicTb = (CarpicTb)commonDao.selectObjectByConditions(carpicTb);
            String collectionName = "";
            if(carpicTb !=null && carpicTb.getLiftpicTableName()!=null){
                collectionName = carpicTb.getLiftpicTableName();
            }
            if(collectionName==null||"".equals(collectionName)||"null".equals(collectionName)){
                logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>没有查到对应的图片！"+liftrodId);
                return new byte[0];
            }
            DBCollection collection = db.getCollection(collectionName);
            if(collection != null){
                BasicDBObject document = new BasicDBObject();
                document.put("parkid", String.valueOf(comid));
                document.put("liftrodid", liftrodId);
                DBObject obj  = collection.findOne(document);
                if(obj == null){
                    logger.error("取图片错误.....");
                    return new byte[0];
                }
                byte[] content = (byte[])obj.get("content");
                logger.error("取图片成功.....大小:"+content.length);
                db.requestDone();
                System.out.println("mongdb over.....");
                return content;
            }else{
                logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>没有查到对应的图片！"+liftrodId);
                return new byte[0];
            }
        }else{
            logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>没有查到对应的图片！"+liftrodId);
            return new byte[0];
        }
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderfield");
        reqParameterMap.remove("orderby");

        JSONObject result = selectResultByConditions(reqParameterMap);
        List<LiftRodTb> liftRodList = JSON.parseArray(result.get("rows").toString(), LiftRodTb.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(liftRodList!=null&&liftRodList.size()>0){
            String [] f = new String[]{"id","liftrod_id","ctime","uin","out_channel_id","reason","resume"};
            Map<Integer, String> reasonMap = (Map)getLiftReason(1);
            for(LiftRodTb liftRodTb : liftRodList){
//                List<String> values = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
                OrmUtil<LiftRodTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(liftRodTb);
                //判断各种字段 组装导出数据
                for(String field : f){
                    if("uin".equals(field)){
                        values.add(getUinName(Long.valueOf(map.get(field)+"")));
                    }else if("reason".equals(field)){
                        Integer key = Integer.valueOf(map.get(field)+"");
                        if(reasonMap.get(key)!=null)
                            values.add(reasonMap.get(key));
                        else {
                            values.add("无");
                        }
                    }else{
                        if("ctime".equals(field)){
                            if(map.get(field)!=null){
                                values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                            }else{
                                values.add("null");
                            }
                        }else{
                            values.add(map.get(field)+"");
                        }
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }

//    @Override
//    public String getLiftReason() {
//        String reason = CustomDefind.getValue("LIFTRODREASON");
////        if(type==0){
//            String ret = "[{value_no:-1,value_name:\"\"},{value_no:100,value_name:\"原因未选\"}";
//            if(reason!=null){
//                String res[] = reason.split("\\|");
//                for(int i=0;i<res.length;i++){
//                    ret+=",{value_no:"+i+",value_name:\""+res[i]+"\"}";
//                }
//            }
//            ret +="]";
//            return ret;
////        }else {
////        Map<Integer, String> reasonMap = new HashMap<Integer, String>();
////        if(reason!=null){
////            String res[] = reason.split("\\|");
////            for(int i=0;i<res.length;i++){
////                reasonMap.put(i, res[i]);
////            }
////        }
////            return reasonMap;
////        }
//
////        return reasonMap;
//    }


    /*
    * 读取配置文件 获得抬杆原因 两种形式返回
    *
    * */
    @Override
    public Object getLiftReason(int type) {
        String reason = CustomDefind.getValue("LIFTRODREASON");
        logger.error("lift>>>,reason:"+reason);
        if(type==0){
            String ret = "[{\"value_no\":\"-1\",\"value_name\":\"\"},{\"value_no\":\"100\",\"value_name\":\"原因未选\"}";
            if(reason!=null){
                String res[] = reason.split("\\|");
                for(int i=0;i<res.length;i++){
                    ret+=",{\"value_no\":\""+i+"\",\"value_name\":\""+res[i]+"\"}";
                }
            }
            ret +="]";
            return ret;
        }else {
            Map<Integer, String> reasonMap = new HashMap<Integer, String>();
            if(reason!=null){
                String res[] = reason.split("\\|");
                for(int i=0;i<res.length;i++){
                    reasonMap.put(i, res[i]);
                }
            }
            return reasonMap;
        }
    }

    private String getUinName(Long uin) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);

        String uinName = "";
        if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
            uinName = userInfoTb.getNickname();
        }
        return uinName;
    }
}
