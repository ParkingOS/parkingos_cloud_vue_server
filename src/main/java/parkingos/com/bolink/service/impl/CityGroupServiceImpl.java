package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrgGroupTb;
import parkingos.com.bolink.service.CityGroupService;
import parkingos.com.bolink.service.SupperSearchService;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CityGroupServiceImpl implements CityGroupService {

    Logger logger = Logger.getLogger(CityGroupServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrgGroupTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {


        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setState(0);
        orgGroupTb.setCityid(Long.parseLong(reqmap.get("cityid")));
        JSONObject result = supperSearchService.supperSearch(orgGroupTb,reqmap);
        return result;
    }

    @Override
    public JSONObject deleteGroup(Long id) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","删除失败");
        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setState(1);
        orgGroupTb.setId(id);
        int res = commonDao.updateByPrimaryKey(orgGroupTb);
        if(res==1){
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }

    @Override
    public JSONObject addGroup(String name,String latitude, String longitude ,String cityid, String operatorid, String address, Long id) {
        JSONObject result = new JSONObject();
        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setName(name);
        orgGroupTb.setAddress(address);
        orgGroupTb.setCityid(Long.parseLong(cityid));
        orgGroupTb.setOperatorid(operatorid);
        if(id==null){
            orgGroupTb.setLatitude(new BigDecimal(latitude));
            orgGroupTb.setLongitude(new BigDecimal(longitude));
            orgGroupTb.setCreateTime(System.currentTimeMillis()/1000);
            int res = commonDao.insert(orgGroupTb);
            if(res==1){
                result.put("state",1);
                result.put("msg","添加成功");
            }
        }else{
            orgGroupTb.setId(id);
            int res = commonDao.updateByPrimaryKey(orgGroupTb);
            if(res==1){
                result.put("state",1);
                result.put("msg","修改成功");
            }
        }

        return result;
    }
}
