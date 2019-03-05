package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.PostManageMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkChannelsTb;
import parkingos.com.bolink.service.PostManageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PostManageServiceImpl implements PostManageService {

    Logger logger = LoggerFactory.getLogger(PostManageServiceImpl.class);
    @Autowired
    private PostManageMapper postManageMapper;
    @Autowired
    private CommonDao commonDao;


    @Override
    public JSONObject getChannels(Long comid,Integer rp,Integer page) {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> list = new ArrayList<>();
        int count = postManageMapper.getChannelsCount(comid);
        if(count>0) {
            Integer offset = (page - 1) * rp;
            list = postManageMapper.getChannels(comid, rp, offset);
        }
        result.put("total",count);
        result.put("rows", JSON.toJSON(list));
        result.put("page",page);
        return result;
    }

    @Override
    public JSONObject editName(Long comid,String name,String localId) {
        JSONObject result = new JSONObject();
        ParkChannelsTb parkChannelsTb = new ParkChannelsTb();
        int doCount = 0;

        String localIdNoVersion = localId.replace(localId.substring(localId.indexOf("_"),localId.indexOf("_",localId.indexOf("_")+1)),"");
        parkChannelsTb.setParkId(comid);
        parkChannelsTb.setLocalIdNoVersion(localIdNoVersion);
        int count =commonDao.selectCountByConditions(parkChannelsTb);
        if(count<1){
            parkChannelsTb.setLocalName(name);
            doCount=commonDao.insert(parkChannelsTb);
        }else {
            ParkChannelsTb update = new ParkChannelsTb();
            update.setLocalName(name);
            doCount = commonDao.updateByConditions(update,parkChannelsTb);
        }
        result.put("state",doCount);
        return result;
    }
}
