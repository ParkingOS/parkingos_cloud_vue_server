package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityMemberService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CityMemberServiceImpl implements CityMemberService {

    Logger logger = Logger.getLogger(CityMemberServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<UserInfoTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setCityid((Long.parseLong(reqmap.get("cityid"))));
        userInfoTb.setState(0);

        System.out.println("=====:"+reqmap.get("oid"));


        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setState(0);
        userRoleTb.setOid(Long.parseLong(reqmap.get("oid")));
        PageOrderConfig pageOrderConfig = new PageOrderConfig();
        pageOrderConfig.setPageInfo(null,null);
        List<UserRoleTb> userRoleList = commonDao.selectListByConditions(userRoleTb,pageOrderConfig);
        List roleIdList = new ArrayList();
        for(UserRoleTb userRoleTb1 :userRoleList){
            roleIdList.add(userRoleTb1.getId());
        }

        int count = 0;
        List<UserInfoTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        Map searchMap = supperSearchService.getBaseSearch(userInfoTb,reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            UserInfoTb baseQuery =(UserInfoTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");


            //封装searchbean  城市和集团下所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.CONTAINS);
            searchBean.setFieldName("role_id");
            searchBean.setBasicValue(roleIdList);

            if (supperQuery == null) {
                supperQuery = new ArrayList<>();
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
        logger.error("============>>>>>返回数据"+result);
        return result;
//        UserInfoTb userInfoTb = new UserInfoTb();
//        userInfoTb.setCityid(Long.parseLong(reqmap.get("cityid")));
//        userInfoTb.setState(0);
//
//        JSONObject result = supperSearchService.supperSearch(userInfoTb,reqmap);
//
//        return result;

    }

    @Override
    public String getRoleByConditions(Map<String, String> reqParameterMap) {
        String result = "[]";
        Integer supperadmin = Integer.parseInt(reqParameterMap.get("supperadmin"));
        Long cityid = Long.parseLong(reqParameterMap.get("cityid"));
        Long uin = Long.parseLong(reqParameterMap.get("loginuin"));
        List<Map<String, Object>> adminRoleList = commonDao.getObjectBySql("select id as value_no,role_name as value_name from user_role_tb where type=0 and oid =(select id from zld_orgtype_tb where name like '%城市%' limit 1 ) limit 1 ");//daService.getMap("select * from user_role_tb where type=? and oid =(select id from zld_orgtype_tb where name like ? limit ? ) limit ? ",
        if(adminRoleList == null || cityid < 0){
            return result;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        String sql = "";
        if(supperadmin==1){
            list = adminRoleList;
        }else{
            sql = "select id as value_no,role_name as value_name from user_role_tb where state=0 and oid="+adminRoleList.get(0).get("oid") + " and id<>"+adminRoleList.get(0).get("id")+" and adminid in (select id from user_info_tb where cityid=(select cityid from user_info_tb where id="+uin+" )) ";
            list = commonDao.getObjectBySql(sql);
        }

        if(list != null && !list.isEmpty()){
            result = StringUtils.createJson(list);
        }
        return result;
    }


    @Override
    public JSONObject createMember(Map<String, String> reqParameterMap) {
        String str = "{\"state\":0,\"msg\":\"保存失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        String strid = "";
        String resume = reqParameterMap.get("resume");
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


        if("".equals(nickname)) nickname=null;
        if("".equals(mobile)) mobile=null;
        if("".equals(phone)) phone=null;
        if("".equals(resume)) phone=null;

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
        logger.error("=====count"+count);
        if(count>0){
            return result;
        }
        Long cityid = -1L;
        if(reqParameterMap.get("cityid")!=null&&!"undefined".equals(reqParameterMap.get("cityid"))){
            cityid = Long.parseLong(reqParameterMap.get("cityid"));
        }
        logger.error("cityid:"+cityid);


        if(cityid!=-1&&role_id==-1){
            role_id = 29L;
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
        user.setCityid(cityid);
        user.setResume(resume);
        logger.error("======>>>>>user"+user);
        int ret = commonDao.insert(user);
        if(ret==1){
            result.put("state",1);
            result.put("msg","增加成功");
        }
        return result;
    }
//
    @Override
    public JSONObject updateMember(UserInfoTb userInfoTb) {

        JSONObject result =new JSONObject();

        int ret = commonDao.updateByPrimaryKey(userInfoTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","修改成功");
        }
        return result;
    }
//
    @Override
    public JSONObject editpass(Map<String, String> reqParameterMap) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        String uin = reqParameterMap.get("id");
        String newPass = reqParameterMap.get("newpass");
        String confirmPass = reqParameterMap.get("confirmpass");

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
            userInfoTb.setId(Long.parseLong(uin));
            int ret =commonDao.updateByPrimaryKey(userInfoTb);
            result.put("state",ret);
            result.put("msg","修改密码成功");
        }else {
            result.put("msg","两次密码输入不一致，请重新输入！");
            return result;
        }
        return result;
    }
//
    @Override
    public JSONObject delmember(Map<String, String> reqParameterMap) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        Long id = Long.parseLong(reqParameterMap.get("id"));
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(id);
        userInfoTb.setState(1);
        int ret = commonDao.updateByPrimaryKey(userInfoTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }


}
