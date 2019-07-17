package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.ParkChargingTb;
import parkingos.com.bolink.service.ChargingService;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.Check;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
@Service
public class ChargingServiceImpl implements ChargingService {

    Logger logger = LoggerFactory.getLogger(ChargingServiceImpl.class);
    @Autowired
    private CommonDao commonDao;


    @Override
    public JSONObject addJs(String name, String remark, String js,Long comid) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","添加失败");

        ParkChargingTb parkChargingTb = new ParkChargingTb();
        parkChargingTb.setParkId(comid);
        parkChargingTb.setState(0);

        int count = commonDao.selectCountByConditions(parkChargingTb);
        if(count>0){
            result.put("msg","该车场已经存在价格函数!");
            return result;
        }

        parkChargingTb.setJsContent(js);
        parkChargingTb.setName(name);
        parkChargingTb.setRemark(remark);

        int insert = commonDao.insert(parkChargingTb);
        if(insert==1){
            result.put("state",1);
            result.put("msg","添加成功");
        }

        return result;
    }

    @Override
    public JSONObject editParkCharging(Long id, String name, String remark, String jsContent) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","修改失败！");
        ParkChargingTb parkChargingTb = new ParkChargingTb();
        parkChargingTb.setId(id);
        parkChargingTb.setRemark(remark);
        parkChargingTb.setName(name);
        parkChargingTb.setJsContent(jsContent);
        int update = commonDao.updateByPrimaryKey(parkChargingTb);
        if(update==1){
            result.put("state",1);
            result.put("msg","修改成功！");
        }
        return result;
    }

    @Override
    public JSONObject deleteParkCharging(Long id) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","删除失败！");
        ParkChargingTb pa = new ParkChargingTb();
        pa.setState(1);
        pa.setId(id);
        int delete = commonDao.updateByPrimaryKey(pa);
        if(delete==1){
            result.put("state",1);
            result.put("msg","删除成功！");
        }
        return result;
    }

    @Override
    public String testJs(Long id,Long createTime,Long endTime) {


        if(createTime.toString().length()>10){
            createTime = createTime/1000;
        }
        if(endTime.toString().length()>10){
            endTime = endTime/1000;
        }
        ParkChargingTb parkChargingTb = new ParkChargingTb();
        parkChargingTb.setId(id);
        parkChargingTb = (ParkChargingTb)commonDao.selectObjectByConditions(parkChargingTb);
        if(parkChargingTb==null){
            return "0";
        }
        String jsContent = parkChargingTb.getJsContent();
        if(Check.isEmpty(jsContent)){
            return "0";
        }
        String result = "0";
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            engine.eval(jsContent);
            // javax.script.Invocable 是一个可选的接口
            // 检查脚本引擎是否被实现!
            // 注意：JavaScript engine 实现了 Invocable 接口
            Invocable inv = (Invocable) engine;
            // 执行这个名字为 "hello"的全局的函数
            result = inv.invokeFunction("getPrice", createTime, endTime)+"";
            logger.info("===>>>>get result from js:"+result);
            return result;
        }catch (Exception e){
            logger.error("===>>>>>js 解析异常",e);
        }
        return result;
    }

    @Override
    public JSONObject openOrClose(Long id, Integer isOpen) {

        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","更新失败！");
        ParkChargingTb pa = new ParkChargingTb();
        pa.setIsOpen(isOpen);
        pa.setId(id);
        int delete = commonDao.updateByPrimaryKey(pa);
        if(delete==1){
            //通知泊链更新状态




            result.put("state",1);
            result.put("msg","更新成功！");
        }
        return result;
    }

    @Override
    public JSONObject cloudFirst(Long comid, Integer cloudFirst) {
        JSONObject result = new JSONObject();
        result.put("state", 0);
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb.setCloudFirst(cloudFirst);
        int update = commonDao.updateByPrimaryKey(comInfoTb);
        if(update==1) {
            result.put("state", update);
            result.put("msg","更新成功！");
        }
        return result;
    }

    @Override
    public int getFirstOrNot(Long comid) {
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb=(ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null){
            return comInfoTb.getCloudFirst();
        }
        return 0;
    }

}
