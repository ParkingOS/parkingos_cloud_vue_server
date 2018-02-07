package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.FreeReasonsTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.FreeReasonsService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class FreeReasonsServiceImpl implements FreeReasonsService {

    Logger logger = Logger.getLogger(FreeReasonsServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<FreeReasonsTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        FreeReasonsTb freeReasonsTb = new FreeReasonsTb();
        freeReasonsTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(freeReasonsTb,reqmap);
        return result;
    }

    @Override
    public JSONObject createFreeReason(String name, Integer sort, Long comid) {
        String str = "{\"state\":0,\"msg\":\"添加失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        Long id = commonDao.selectSequence(FreeReasonsTb.class);

        FreeReasonsTb freeReasonsTb = new FreeReasonsTb();
        freeReasonsTb.setName(name);
        freeReasonsTb.setSort(sort);
        freeReasonsTb.setId(id);
        freeReasonsTb.setComid(comid);
        int ret = commonDao.insert(freeReasonsTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","添加成功");
            insertSysn(freeReasonsTb,0);
        }
        return result;
    }

    @Override
    public JSONObject deleteFreeReason(Long id) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        FreeReasonsTb freeReasonsTb = new FreeReasonsTb();
        freeReasonsTb.setId(id);
        int count = commonDao.selectCountByConditions(freeReasonsTb);
        logger.info("======>>>>删除:"+count);
        if(count==1){
            freeReasonsTb = (FreeReasonsTb)commonDao.selectObjectByConditions(freeReasonsTb);
            int res = commonDao.deleteByConditions(freeReasonsTb);
            logger.info("======>>>>删除:"+res);
            if(res==1){
                result.put("state",1);
                result.put("msg","删除成功");
                insertSysn(freeReasonsTb,2);
            }
        }
        return result;
    }

    @Override
    public JSONObject editReason(Long id, String name, Integer sort) {
        String str = "{\"state\":0,\"msg\":\"更新失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        FreeReasonsTb freeReasonsTb = new FreeReasonsTb();
        freeReasonsTb.setId(id);
        freeReasonsTb.setName(name);
        freeReasonsTb.setSort(sort);
        int res = commonDao.updateByPrimaryKey(freeReasonsTb);
        if(res==1){
            result.put("state",1);
            result.put("msg","更新成功");
            insertSysn(freeReasonsTb,1);
        }

        return result;
    }

    private void insertSysn(FreeReasonsTb freeReasonsTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(freeReasonsTb.getComid());
        syncInfoPoolTb.setTableId(freeReasonsTb.getId());
        syncInfoPoolTb.setTableName("free_reasons_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        commonDao.insert(syncInfoPoolTb);
    }

}
