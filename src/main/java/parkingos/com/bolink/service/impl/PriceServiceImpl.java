package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.PriceTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.PriceService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.CommonUtils;

import java.util.Map;

@Service
public class PriceServiceImpl implements PriceService {

    Logger logger = Logger.getLogger(PriceServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<PriceTb> supperSearchService;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        PriceTb priceTb = new PriceTb();
        priceTb.setComid(Long.parseLong(reqmap.get("comid")));
        priceTb.setIsDelete(0L);
        JSONObject result = supperSearchService.supperSearch(priceTb,reqmap);
        return result;
    }

    @Override
    public Long getId() {
        return commonDao.selectSequence(PriceTb.class);
    }

    @Override
    public JSONObject createPrice(PriceTb priceTb) {
        String str = "{\"state\":0,\"msg\":\"添加失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.insert(priceTb);
        if(ret==1){
            boolean issend = commonUtils.sendMessage(priceTb,priceTb.getComid(),priceTb.getId(),1);
            insertSysn(priceTb,0);
            result.put("state",1);
            result.put("msg","添加成功");
        }
        return result;
    }

    @Override
    public JSONObject updatePrice(PriceTb priceTb) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(priceTb);
        if(ret==1){
            priceTb = (PriceTb)commonDao.selectObjectByConditions(priceTb);
            boolean issend = commonUtils.sendMessage(priceTb,priceTb.getComid(),priceTb.getId(),2);
            insertSysn(priceTb,1);
            result.put("state",1);
            result.put("msg","修改成功");
        }
        return result;
    }

    @Override
    public JSONObject deletePrice(PriceTb priceTb) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(priceTb);
        if(ret==1){
            priceTb = (PriceTb)commonDao.selectObjectByConditions(priceTb);
            boolean issend = commonUtils.sendMessage(priceTb,priceTb.getComid(),priceTb.getId(),3);
            insertSysn(priceTb,2);
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }

    private void insertSysn(PriceTb priceTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(priceTb.getComid());
        syncInfoPoolTb.setTableId(priceTb.getId());
        syncInfoPoolTb.setTableName("price_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        syncInfoPoolTb.setUpdateTime(System.currentTimeMillis()/1000);
        commonDao.insert(syncInfoPoolTb);
    }

}
