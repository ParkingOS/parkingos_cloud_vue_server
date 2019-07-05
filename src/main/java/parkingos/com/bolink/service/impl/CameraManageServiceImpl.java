package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.CameraTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.CameraTbMapper;
import parkingos.com.bolink.models.CameraTb;
import parkingos.com.bolink.service.CameraManageService;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.Check;

import java.util.List;

@Service
public class CameraManageServiceImpl implements CameraManageService {

    Logger logger = LoggerFactory.getLogger(CameraManageServiceImpl.class);

    @Autowired
    CameraTbMapper cameraTbMapper;
    @Autowired
    RedisService redisService;

    @Override
    public List<CameraTb> adminManage(Long cityid,Long comid) {

        CameraTbExample example =new CameraTbExample();
        if(cityid>0){
            example.createCriteria().andCityidEqualTo(cityid);
        }
        if(comid>0){
            example.createCriteria().andComidEqualTo(comid);
        }
        List<CameraTb> list =  cameraTbMapper.selectByExample(example);
        logger.info("===>>>>>>>>>"+list);
        return list;
    }

    @Override
    public JSONObject bindPark(Long comid, Long cityid, Long id) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","更新失败");

        CameraTb cameraTb = new CameraTb();
        cameraTb.setId(id);
        cameraTb.setComid(comid);
        cameraTb.setCityid(cityid);

        int update =cameraTbMapper.updateByPrimaryKeySelective(cameraTb);
        if(update==1){
            //更新缓存
            cameraTb = cameraTbMapper.selectByPrimaryKey(id);
            String camId = cameraTb.getCamId();
            redisService.set("camera_"+camId,cameraTb);

            result.put("state",1);
            result.put("msg","更新成功");
        }
        return result;
    }

    @Override
    public List<CameraTb> parkQuery(Long comid, String camName, String camId) {
        CameraTbExample example =new CameraTbExample();
        if(comid>0){
            example.createCriteria().andComidEqualTo(comid);
        }
        if(Check.isEmpty(camName)){
            example.createCriteria().andCamNameLike("%"+camName+"%");
        }
        if(Check.isEmpty(camId)){
            example.createCriteria().andCamIdLike("%"+camId+"%");
        }
        List<CameraTb> list =  cameraTbMapper.selectByExample(example);
        logger.info("===>>>>>>>>>"+list);
        return list;
    }

    @Override
    public JSONObject editName(Long id, String camName) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","更新失败");

        CameraTb cameraTb = new CameraTb();
        cameraTb.setId(id);
        cameraTb.setCamName(camName);

        int update =cameraTbMapper.updateByPrimaryKeySelective(cameraTb);
        if(update==1){
            cameraTb = cameraTbMapper.selectByPrimaryKey(id);
            String camId = cameraTb.getCamId();
            redisService.set("camera_"+camId,cameraTb);

            result.put("state",1);
            result.put("msg","更新成功");
        }
        return result;
    }

}
