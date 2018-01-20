package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.service.BlackUserService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class BlackUserServiceImpl implements BlackUserService {

    Logger logger = Logger.getLogger(BlackUserServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ZldBlackTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        ZldBlackTb zldBlackTb = new ZldBlackTb();
        zldBlackTb.setComid(Long.parseLong(reqmap.get("comid")));
        zldBlackTb.setState(0);
        JSONObject result = supperSearchService.supperSearch(zldBlackTb,reqmap);

        return result;
    }

    @Override
    public JSONObject editBlackUser(ZldBlackTb zldBlackTb) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(zldBlackTb);
        if(ret==1){
            zldBlackTb = (ZldBlackTb)commonDao.selectObjectByConditions(zldBlackTb);
            int ins = insertSysn(zldBlackTb,1);
            if(ins!=1){
                logger.error("======>>>>插入同步表失败");
            }
            result.put("state",1);
            result.put("msg","修改成功");
        }
        return result;
    }

    @Override
    public JSONObject deleteBlackUser(ZldBlackTb zldBlackTb) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(zldBlackTb);
        if(ret==1){
            zldBlackTb = (ZldBlackTb)commonDao.selectObjectByConditions(zldBlackTb);
            int ins = insertSysn(zldBlackTb,1);
            if(ins!=1){
                logger.error("======>>>>插入同步表失败");
            }
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }

    @Override
    public Long getId() {
        return commonDao.selectSequence(ZldBlackTb.class);
    }

    @Override
    public JSONObject createBlackUser(ZldBlackTb zldBlackTb) {
        String str = "{\"state\":0,\"msg\":\"添加失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        int ret = commonDao.insert(zldBlackTb);
        if(ret ==1){
            result.put("state",1);
            result.put("msg","添加成功");
            int ins = insertSysn(zldBlackTb,0);
            if(ins!=1){
                logger.error("======>>>>插入同步表失败");
            }
        }
        return result;
    }

    private int  insertSysn(ZldBlackTb zldBlackTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(zldBlackTb.getComid());
        syncInfoPoolTb.setTableId(zldBlackTb.getId());
        syncInfoPoolTb.setTableName("zld_black_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        return commonDao.insert(syncInfoPoolTb);
    }

}
