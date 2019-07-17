package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.mybatis.mapper.UserInfoTbMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserInfoTbExample;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.AdminRoleService;
import parkingos.com.bolink.service.CityUinService;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.CustomDefind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityUinServiceImpl implements CityUinService {

    Logger logger = LoggerFactory.getLogger(CityUinServiceImpl.class);

    @Autowired
    private CommonDao commonDao;

    @Autowired
    CommonService commonService;

    @Autowired
    AdminRoleService adminRoleService;

    @Autowired
    CommonMethods commonMethods;

    @Autowired
    UserInfoTbMapper userInfoTbMapper;
    @Autowired
    RedisService redisService;


    @Override
    public JSONObject createCity(String name, String union_id, String ukey) {
        JSONObject result = new JSONObject();

        if("".equals(name)||"".equals(ukey)||"".equals(union_id)){
            result.put("state",0);
            result.put("msg","添加失败");
            return result;
        }

        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        Long id = commonDao.selectSequence(OrgCityMerchants.class);
        orgCityMerchants.setId(id);
        orgCityMerchants.setName(name);
        orgCityMerchants.setUnionId(union_id);
        orgCityMerchants.setUkey(ukey);
        orgCityMerchants.setCtime(System.currentTimeMillis()/1000);
        int res = commonDao.insert(orgCityMerchants);
        if(res==1){

            UserInfoTb userInfotb = new UserInfoTb();
            userInfotb.setCityid(id);
            userInfotb.setStrid("admin");
            userInfotb.setNickname(name+"管理员");
            userInfotb.setRoleId(29L);
            userInfotb.setRegTime(System.currentTimeMillis()/1000);
            userInfotb.setPassword("123456");
            int insertUser = commonDao.insert(userInfotb);
            System.out.println("插入user:"+insertUser);

            result.put("state",1);
            result.put("msg","添加成功");
        }
        return result;
    }

    @Override
    public JSONObject editSetting(Long cityid, Integer state, Integer type) {
        JSONObject result = new JSONObject();
        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        orgCityMerchants.setId(cityid);
        if(type==1){
            //月卡续费开始时间
            orgCityMerchants.setSelfRefillSetting(state);
        }else if(type==2){
            //月卡续费结束时间
            orgCityMerchants.setEndTimeSetting(state);
        }
        int count = commonDao.updateByPrimaryKey(orgCityMerchants);
        if(count ==1){
            result.put("state",1);
            return result;
        }
        result.put("state",0);
        result.put("errmsg","修改失败");
        return result;
    }

    @Override
    public JSONObject querySetting(Long cityid) {
        JSONObject result = new JSONObject();
        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        orgCityMerchants.setId(cityid);
        orgCityMerchants.setState(0);
        orgCityMerchants =(OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
        Map<String,Object> map = new HashMap<String,Object>();
        if(orgCityMerchants!=null){
            map.put("self_setting",orgCityMerchants.getSelfRefillSetting());
            map.put("end_time_setting",orgCityMerchants.getEndTimeSetting());
            result.put("self_setting",map);
            return result;
        }
        return null;
    }

    @Override
    public String getSingleAuth(Long cityid) {

        OrgCityMerchants city = commonService.getOrgCityById(cityid);
        if(city!=null){
            if(city.getSelfParkAuth()==null){
                city = commonService.reGetOrgCityById(cityid);
            }
            int parkAuth = city.getSelfParkAuth();
            Long roleId = 30L;
            if(parkAuth==1){
                //厂商自定义车场管理员属性   去查找该角色
                UserRoleTb userRoleTb = new UserRoleTb();
                userRoleTb.setAdminid(0L);
                userRoleTb.setCityid(city.getId());
                userRoleTb.setOid(8L);

                userRoleTb = (UserRoleTb)commonDao.selectObjectByConditions(userRoleTb);
                if(userRoleTb!=null){
                    roleId = userRoleTb.getId();
                }
            }

            //roleId就是 厂商的车场管理员id     默认的所有权限是   云平台的默认管理员的权限

            String result = adminRoleService.getAuth(30L,roleId,-1L);

            JSONObject json = JSON.parseObject(result);
            json.put("role_id",roleId);

            return json.toString();
        }

        return null;
    }

    @Override
    @Transactional
    public JSONObject setSingleAuth(Long cityid, Long roleId,String auths) {

        JSONObject result = new JSONObject();
        result.put("state",0);

        //如果厂商是第一次编辑  自定义的车场管理员
        if(roleId.equals(30L)){
            UserRoleTb userRoleTb = new UserRoleTb();
            roleId = commonDao.selectSequence(UserRoleTb.class);
            userRoleTb.setId(roleId);
            userRoleTb.setRoleName("车场管理员");
            userRoleTb.setAdminid(0L);
            userRoleTb.setCityid(cityid);
            userRoleTb.setState(0);
            userRoleTb.setOid(8L);
            int saveRole = commonDao.insert(userRoleTb);
            logger.info("===>>>>>saveRole:"+saveRole);


            //更新掉这个厂商下面的车场所有管理员  30 ===>>>>roleId

            List<Object> list =  commonMethods.getparks(cityid);
            if(list!=null&&!list.isEmpty()) {
                UserInfoTbExample example = new UserInfoTbExample();
                UserInfoTbExample.Criteria criteria = example.createCriteria();
                List<Long> parkList = new ArrayList<>();
                for (Object object : list) {
                    parkList.add(Long.parseLong(object.toString()));
                }
                criteria.andRoleIdEqualTo(30L);
                criteria.andComidIn(parkList);
                UserInfoTb userInfoTb = new UserInfoTb();
                userInfoTb.setRoleId(roleId);
                int updateUser = userInfoTbMapper.updateByExampleSelective(userInfoTb,example);

                logger.info("===>>>>>updateUser:"+updateUser);
            }


            OrgCityMerchants city = new OrgCityMerchants();
            city.setId(cityid);
            city.setSelfParkAuth(1);
            int updateCity = commonDao.updateByPrimaryKey(city);
            logger.info("===>>>>>updateCity:"+updateCity);

            //更新完厂商要  刷新缓存
            if(updateCity==1) {
                redisService.delete(CustomDefind.getValue("ORGCITYBYID") + cityid);
            }
        }

        //把该 roleId 权限 重置
        if(roleId!=30){
            result =adminRoleService.editRoleAuth(roleId,auths);
        }

        return result;
    }


}
