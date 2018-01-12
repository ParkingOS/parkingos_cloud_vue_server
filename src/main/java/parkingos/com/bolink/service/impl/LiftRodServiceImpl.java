package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.zld.common_dao.dao.CommonDao;
import com.zld.common_dao.qo.PageOrderConfig;
import com.zld.common_dao.qo.SearchBean;
import com.zld.common_dao.util.OrmUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.models.CarpicTb;
import parkingos.com.bolink.models.LiftRodTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.LiftRodService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.MongoClientFactory;

import java.util.ArrayList;
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

        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count = 0;
        List<LiftRodTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<>();
        Map searchMap = supperSearchService.getBaseSearch(new LiftRodTb(), reqmap);
        logger.info(searchMap);
        if (searchMap != null && !searchMap.isEmpty()) {
            LiftRodTb baseQuery = (LiftRodTb) searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if (searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            PageOrderConfig config = null;
            if (searchMap.containsKey("config"))
                config = (PageOrderConfig) searchMap.get("config");
            count = commonDao.selectCountByConditions(baseQuery, supperQuery);
            if (count > 0) {
                list = commonDao.selectListByConditions(baseQuery, supperQuery, config);

                if (list != null && !list.isEmpty()) {
                    for (LiftRodTb product : list) {
                        OrmUtil<LiftRodTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(product);
                        UserInfoTb userInfoTb = new UserInfoTb();
                        if (map.get("uin") != null && !"".equals(map.get("uin"))) {
                            Long uin = Long.parseLong(map.get("uin") + "");
                            userInfoTb.setId(uin);
                            userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
                            if (userInfoTb != null && userInfoTb.getNickname() != null) {
                                map.put("uin", userInfoTb.getNickname());
                            }
                        }
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }

//        LiftRodTb liftRodTb = new LiftRodTb();
//        liftRodTb.setComid(21782L);
//        int count = commonDao.selectCountByConditions(liftRodTb);
//        if(count>0){
//            PageOrderConfig config = new PageOrderConfig();
//            config.setPageInfo(Integer.parseInt(reqmap.get("page")[0]), Integer.parseInt(reqmap.get("rp")[0]));
//            List<LiftRodTb> list = commonDao.selectListByConditions(liftRodTb, config);
//            List<Map<String, Object>> resList = new ArrayList<>();
//            if (list != null && !list.isEmpty()) {
//                for (LiftRodTb liftRod : list) {
//                    OrmUtil<LiftRodTb> otm = new OrmUtil<>();
//                    Map<String, Object> map = otm.pojoToMap(liftRod);
//                    UserInfoTb userInfoTb = new UserInfoTb();
//                    if(map.get("uin")!=null&&!"".equals(map.get("uin"))){
//                        Long uin = Long.parseLong(map.get("uin")+"");
//                        userInfoTb.setId(uin);
//                        userInfoTb =(UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
//                        if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
//                            map.put("uin",userInfoTb.getNickname());
//                        }
//                    }
//                    resList.add(map);
//                }
//                result.put("rows", JSON.toJSON(resList));
//            }
//        }
        result.put("total", count);
        result.put("page", Integer.parseInt(reqmap.get("page")));
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
}
