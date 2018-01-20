package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CollectorSetTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.service.AdminRoleService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.List;
import java.util.Map;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    Logger logger = Logger.getLogger(AdminRoleServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<UserRoleTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        Long uin = Long.parseLong(reqmap.get("loginuin"));
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setAdminid(uin);
        userRoleTb.setState(0);
        JSONObject result = supperSearchService.supperSearch(userRoleTb,reqmap);
        return result;
    }

    @Override
    public JSONObject addRole(UserRoleTb userRoleTb, Integer func) {
        String str = "{\"state\":0,\"msg\":\"保存失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int is_inspect = 0;
        int is_collector = 0;
        int is_opencard = 0;
        switch (func) {
            case 1:
                is_collector = 1;
                break;
            case 2:
                is_inspect = 1;
                break;
            case 3:
                is_opencard = 1;
                break;
            default:
                break;
        }
        userRoleTb.setIsCollector(is_collector);
        userRoleTb.setIsInspect(is_inspect);
        userRoleTb.setIsOpencard(is_opencard);
        logger.error("=======>>>>>"+userRoleTb);
        int ret = commonDao.insert(userRoleTb);
        if(ret==1){
            result.put("msg","保存成功");
        }
        result.put("state",ret);
        return result;
    }

    @Override
    public JSONObject deleteRole(UserRoleTb userRoleTb) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(userRoleTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }

    @Override
    public JSONObject updateRole(UserRoleTb userRoleTb, Integer func) {
        logger.error("=====>>>开始更新角色+"+userRoleTb+"==="+func);
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        Long auth_flag = -1L;
        int is_inspect = 0;
        int is_collector = 0;
        int is_opencard = 0;
        switch (func) {
            case 1:
                auth_flag = 2L;
                is_collector = 1;
                break;
            case 2:
                auth_flag = 16L;
                is_inspect = 1;
                break;
            case 3:
                auth_flag = 17L;
                is_opencard = 1;
                break;
            default:
                break;
        }
        userRoleTb.setIsCollector(is_collector);
        userRoleTb.setIsInspect(is_inspect);
        userRoleTb.setIsOpencard(is_opencard);

        int ret = commonDao.updateByPrimaryKey(userRoleTb);
        logger.error("=====>>>更新角色"+ret);
        if(ret==1){
            UserInfoTb fields = new UserInfoTb();
            fields.setAuthFlag(auth_flag);
            UserInfoTb conditions = new UserInfoTb();
            conditions.setRoleId(userRoleTb.getId());
            int update = commonDao.updateByConditions(fields,conditions);
            logger.error("=====>>>>>>更新user"+update);
            result.put("state",1);
            result.put("msg","修改成功");
        }
        return result;
    }

    @Override
    public JSONObject precollectset(CollectorSetTb collectorSetTb) {
        String str = "{\"state\":0,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        PageOrderConfig pageOrderConfig = new PageOrderConfig();
        pageOrderConfig.setPageInfo(1,1);
        pageOrderConfig.setOrderInfo("id","desc");
        List<CollectorSetTb> list = commonDao.selectListByConditions(collectorSetTb,pageOrderConfig);
        if(list.size()>0){
            result.put("state",1);
            result.put("rows", JSON.toJSON(list));
        }
        return result;
    }
}
