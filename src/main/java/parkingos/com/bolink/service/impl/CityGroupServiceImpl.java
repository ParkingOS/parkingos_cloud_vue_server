package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.models.OrgGroupTb;
import parkingos.com.bolink.service.CityGroupService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.CustomDefind;
import parkingos.com.bolink.utils.HttpProxy;

import java.util.Map;

@Service
public class CityGroupServiceImpl implements CityGroupService {

    Logger logger = LoggerFactory.getLogger(CityGroupServiceImpl.class);

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

        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        orgCityMerchants.setId(Long.parseLong(cityid));
        orgCityMerchants =(OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
        String unionId = orgCityMerchants.getUnionId();


        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setName(name);
        orgGroupTb.setAddress(address);
        orgGroupTb.setCityid(Long.parseLong(cityid));
        orgGroupTb.setOperatorid(operatorid);

        //如果填写了ope
        if(!Check.isEmpty(operatorid)) {
            if (!Check.isNumber(operatorid)) {
                result.put("state", 0);
                result.put("msg", "互联运营编号错误");
                return result;
            }


            //如果operatorid 是数字格式，去校验泊链是不是有该运营商
            String askUrl = "http://" + CustomDefind.getValue("DOMAIN") + "/zld/ask/getOpeIsExist?id=" + operatorid + "&union_id=" + unionId;
            String flag = new HttpProxy().doGet(askUrl);
            logger.info("===>>>校验ope是否存在：" + flag);
            if (flag == null) {
                result.put("state", 0);
                result.put("msg", "稍后重试!");
                return result;
            }
            if ("0".equals(flag)) {
                result.put("state", 0);
                result.put("msg", "不存在该互联运营编号!");
                return result;
            }

            if(id!=null&&id>0){
                OrgGroupTb con = new OrgGroupTb();
                con.setId(id);
                con = (OrgGroupTb)commonDao.selectObjectByConditions(con);
                if(!operatorid.equals(con.getOperatorid())){
                    OrgGroupTb opeCOn = new OrgGroupTb();
                    opeCOn.setOperatorid(operatorid);
                    int count = commonDao.selectCountByConditions(opeCOn);
                    if (count > 0) {
                        result.put("state", 0);
                        result.put("msg", "运营商编号重复!");
                        return result;
                    }
                }
            }else{
                OrgGroupTb con = new OrgGroupTb();
                con.setOperatorid(operatorid);
                int count = commonDao.selectCountByConditions(con);
                if (count > 0) {
                    result.put("state", 0);
                    result.put("msg", "已经注册过该运营商!");
                    return result;
                }
            }

        }

        if(id==null){

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
