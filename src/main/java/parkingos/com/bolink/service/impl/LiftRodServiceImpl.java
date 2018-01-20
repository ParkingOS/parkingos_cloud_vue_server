package parkingos.com.bolink.service.impl;

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
import parkingos.com.bolink.service.LiftRodService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.MongoClientFactory;

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
}
