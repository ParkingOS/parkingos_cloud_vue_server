package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.UnionServerTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.UnionService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UnionServiceImpl implements UnionService {

    Logger logger = LoggerFactory.getLogger(UnionServiceImpl.class);

    @Autowired
    private SupperSearchService supperSearchService;
    @Autowired
    CommonDao commonDao;

    @Override
    public JSONObject getRoles(Map<String, String> paramMap) {
        Long uin = Long.parseLong(paramMap.get("loginuin"));
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setState(0);
        userRoleTb.setAdminid(uin);
        JSONObject result = supperSearchService.supperSearch(userRoleTb,paramMap);
        return  result;
    }

    @Override
    public JSONObject addOrEditRole(Long id, Long uin, Long oid, String name, String resume, Integer state) {
        String str = "{\"state\":0,\"msg\":\"操作失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setRoleName(name);
        userRoleTb.setState(state);
        userRoleTb.setResume(resume);
        userRoleTb.setType(1);
        userRoleTb.setAdminid(uin);
        userRoleTb.setOid(oid);
        int count = 0;
        if(id!=null&&id>0){
            //编辑操作
            userRoleTb.setId(id);
            count = commonDao.updateByPrimaryKey(userRoleTb);
        }else {
            count = commonDao.insert(userRoleTb);
        }

        if(count==1){
            result.put("state",1);
            result.put("msg","操作成功！");
        }
        return result;
    }

    @Override
    public JSONObject addMember(Map<String, String> reqParameterMap) {
        String str = "{\"state\":0,\"msg\":\"保存失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        String strid = "";
        String nickname =reqParameterMap.get("nickname");
        String phone =reqParameterMap.get("phone");
        String mobile =reqParameterMap.get("mobile");
        if(mobile!=null&&mobile.length()>15){
            result.put("msg","手机长度不大于15位");
            return result;
        }
        if(phone!=null&&phone.length()>15){
            result.put("msg","电话长度不大于15位");
            return result;
        }

        Long role_id =-1L;
        if(reqParameterMap.get("role_id")!=null&&!"".equals(reqParameterMap.get("role_id"))){
            role_id = Long.parseLong(reqParameterMap.get("role_id"));
        }
        if(role_id<0){
            result.put("msg","请选择角色!");
            return result;
//            role_id = 771L;
        }

        Long cityId = Long.parseLong(reqParameterMap.get("cityid"));

        Long time = System.currentTimeMillis()/1000;
        //用户表
        Long nextid = commonDao.selectSequence(UserInfoTb.class);
        //定义登录账号为主键id
        strid = String.valueOf(nextid);
        //定义user_id
        String userId = strid;
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setStrid(strid);
        int count = commonDao.selectCountByConditions(userInfoTb);
        logger.info("=====count"+count);
        if(count>0){
            return result;
        }

        UserInfoTb user= new UserInfoTb();
        user.setId(nextid);
        user.setNickname(nickname);
        user.setPassword(strid);
        user.setStrid(strid);
        user.setRegTime(time);
        user.setMobile(mobile);
        user.setPhone(phone);
        user.setRoleId(role_id);
        user.setUserId(userId);
        user.setCityid(cityId);
        logger.info("======>>>>>user"+user);
        int ret = commonDao.insert(user);
        if(ret==1){
            result.put("state",1);
            result.put("msg","增加成功");
        }
        return result;
    }

    @Override
    public JSONObject queryMembers(Map<String, String> reqmap) {
        logger.info("====>>>>查询厂商员工："+reqmap);

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        Long cityId= Long.parseLong(reqmap.get("cityid"));
        String oid = reqmap.get("oid");
//        Long serverId =-1L;
//        if("11".equals(oid)){
//            //这个是服务商登录，serverid 是云平台的主键id
//            serverId= Long.parseLong(reqmap.get("serverid"));
//        }else{
//            Long bolinkServerId = Long.parseLong(reqmap.get("serverid"));
//            UnionServerTb unionServerTb = new UnionServerTb();
//            unionServerTb.setBolinkServerId(bolinkServerId);
//            unionServerTb =(UnionServerTb)commonDao.selectObjectByConditions(unionServerTb);
//            if(unionServerTb==null){
//                return result;
//            }
//            serverId = unionServerTb.getId();
//        }


        UserInfoTb userInfoTb = new UserInfoTb();

        userInfoTb.setCityid(cityId);
        userInfoTb.setState(0);

        int userCount = commonDao.selectCountByConditions(userInfoTb);
        if(userCount==0){
            return result;
        }


        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setState(0);
        userRoleTb.setOid(Long.parseLong(oid));

        PageOrderConfig pageOrderConfig = new PageOrderConfig();
        pageOrderConfig.setPageInfo(null,null);
        List<UserRoleTb> userRoleList = commonDao.selectListByConditions(userRoleTb,pageOrderConfig);
        List roleIdList = new ArrayList();
        for(UserRoleTb userRoleTb1 :userRoleList){
            roleIdList.add(userRoleTb1.getId());
        }

        int count = 0;
        List<UserInfoTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();

        Map searchMap = supperSearchService.getBaseSearch(userInfoTb,reqmap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            UserInfoTb baseQuery =(UserInfoTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper")) {
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            }
            PageOrderConfig config = null;
            if(searchMap.containsKey("config")) {
                config = (PageOrderConfig) searchMap.get("config");
            }


            //封装searchbean  城市和集团下所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.CONTAINS);
            searchBean.setFieldName("role_id");
            searchBean.setBasicValue(roleIdList);

            if (supperQuery == null) {
                supperQuery = new ArrayList<SearchBean>();
            }
            supperQuery.add( searchBean );

            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
            if(count>0){
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
                if (list != null && !list.isEmpty()) {
                    for (UserInfoTb userInfoTb1 : list) {
                        OrmUtil<UserInfoTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(userInfoTb1);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));
        return result;
    }

    @Override
    public String getAllRoles(Map<String, String> reqParameterMap) {
        String result = "[]";
        Long cityid = Long.parseLong(reqParameterMap.get("cityid"));
        Long oid = Long.parseLong(reqParameterMap.get("oid"));
        List<Map<String, Object>> adminRoleList = commonDao.getObjectBySql("select * from user_role_tb where type=0 and oid = "+oid+" limit 1 ");//daService.getMap("select * from user_role_tb where type=? and oid =(select id from zld_orgtype_tb where name like ? limit ? ) limit ? ",
        if(adminRoleList == null || cityid < 0){
            return result;
        }

        String sql = "select id as value_no,role_name as value_name from user_role_tb where state=0 and oid="+oid+" and adminid in (select id from user_info_tb where cityid="+cityid+" ) ";
        List<Map<String, Object>> list = commonDao.getObjectBySql(sql);
        Map<String, Object> map = new HashMap<>();
        map.put("value_no",adminRoleList.get(0).get("id"));
        map.put("value_name",adminRoleList.get(0).get("role_name"));
        list.add(map);
        if(list != null && !list.isEmpty()){
            result = StringUtils.createJson(list);
        }
        return result;
    }

    @Override
    public JSONObject editMember(String name, String mobile, String phone, Long id, Long roleId) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","编辑失败");
        if(id<0){
            return result;
        }

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setNickname(name);
        userInfoTb.setMobile(mobile);
        userInfoTb.setPhone(phone);
        userInfoTb.setId(id);
        if(roleId!=null&&roleId>0){
            userInfoTb.setRoleId(roleId);
        }
        int count = commonDao.updateByPrimaryKey(userInfoTb);
        if(count==1){
            result.put("state",1);
            result.put("msg","编辑成功");
        }

        return result;
    }

    @Override
    public JSONObject deleteMember(Long id) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","删除失败");
        if(id<0){
            return result;
        }
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(id);
        userInfoTb.setState(1);
        int count = commonDao.updateByPrimaryKey(userInfoTb);
        if(count==1){
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }

    @Override
    public JSONObject editMemberPass(Long id, String newPass, String confirmPass) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        String md5pass = newPass;
        try {
            md5pass = StringUtils.MD5(newPass);
            md5pass = StringUtils.MD5(md5pass+"zldtingchebao201410092009");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(newPass.length()<6){
            result.put("msg","密码长度小于6位，请重新输入！");
            return result;
        }else if(newPass.equals(confirmPass)){
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setPassword(newPass);
            userInfoTb.setMd5pass(md5pass);
            userInfoTb.setId(id);
            int ret =commonDao.updateByPrimaryKey(userInfoTb);
            result.put("state",ret);
            result.put("msg","修改密码成功");
        }else {
            result.put("msg","两次密码输入不一致，请重新输入！");
            return result;
        }
        return result;
    }
}
