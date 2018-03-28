package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.service.AdminCityService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class AdminCityServiceImpl implements AdminCityService {

    Logger logger = Logger.getLogger(AdminCityServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrgCityMerchants> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {


        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        orgCityMerchants.setState(0);
        JSONObject result = supperSearchService.supperSearch(orgCityMerchants,reqmap);
        return result;
    }

    @Override
    public JSONObject addCity(String name, String union_id, String ukey, Long id) {
        JSONObject result = new JSONObject();
        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        orgCityMerchants.setName(name);
        orgCityMerchants.setUnionId(union_id);
        orgCityMerchants.setUkey(ukey);
        if(id==null){
            orgCityMerchants.setCtime(System.currentTimeMillis()/1000);
            int res = commonDao.insert(orgCityMerchants);
            if(res==1){
                result.put("state",1);
                result.put("msg","添加成功");
            }
        }else{
            orgCityMerchants.setId(id);
            int res = commonDao.updateByPrimaryKey(orgCityMerchants);
            if(res==1){
                result.put("state",1);
                result.put("msg","修改成功");
            }
        }

        return result;
    }

    @Override
    public JSONObject deleteCity(Long id) {
        JSONObject result = new JSONObject();
        if(id>0){
            OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
            orgCityMerchants.setId(id);
            orgCityMerchants.setState(1);
            int res = commonDao.updateByPrimaryKey(orgCityMerchants);
            if(res==1){
                result.put("state",1);
                result.put("msg","删除成功");
            }
        }
        return result;
    }


}
