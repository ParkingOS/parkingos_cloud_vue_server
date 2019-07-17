package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.MemberService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.CommonUtils;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<UserInfoTb> supperSearchService;
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    CommonService commonService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        Long id = -1L;
        if(!Check.isEmpty(reqmap.get("comid"))) {
            id = Long.parseLong(reqmap.get("comid"));
        }

        String unionId = reqmap.get("union_id");
        String parkId = reqmap.get("bolink_id");
        if(!Check.isEmpty(unionId)&&!Check.isEmpty(parkId)){
            ComInfoTb com = commonService.getComInfoByUnionIdAndParkId(unionId,parkId);
            if(com==null){
                return result;
            }
            id = com.getId();
        }

        if(id<0){
            logger.info("===>>>>:不存在车场");
            return result;
        }

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setComid(id);
        userInfoTb.setState(0);


        Map<String,Object> searchMap = supperSearchService.getBaseSearch(userInfoTb,reqmap);
        int count =0;
        List<UserInfoTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();
        if(searchMap!=null&&!searchMap.isEmpty()){
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");

            //组装查询bean   FieldOperator.NOT = not in;  auth_flag 不等于14,15
            SearchBean bean = new SearchBean();
            bean.setOperator(FieldOperator.NOT);
            bean.setFieldName("auth_flag");
            //参数集合形式
            List<Integer> paramList = new ArrayList<Integer>();
            paramList.add(14);
            paramList.add(15);
            bean.setBasicValue(paramList);

            // auth_flag >0
            SearchBean searchBean = new SearchBean();
            searchBean.setFieldName("auth_flag");
            searchBean.setStartValue(0);
            searchBean.setOperator(FieldOperator.GREATER_THAN);


            //把bean对象放到高级查询中
            if(supperQuery==null){
                supperQuery = new ArrayList<>();
            }
            supperQuery.add(bean);
            supperQuery.add(searchBean);

            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");
            count = commonDao.selectCountByConditions(userInfoTb,supperQuery);
            logger.error("======>>>>>"+count);
            if(count>0){
                if(config==null){
                    config = new PageOrderConfig();
                    config.setPageInfo(1,Integer.MAX_VALUE);
                }
                list = commonDao.selectListByConditions(userInfoTb,supperQuery,config);

                if (list != null && !list.isEmpty()) {
                    for (UserInfoTb user : list) {
                        OrmUtil<UserInfoTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(user);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }

//        JSONObject result = supperSearchService.supperSearch(userInfoTb,reqmap);
//        return result;
        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        return result;
    }

    @Override
    public String getRoleByConditions(Map<String, String> reqParameterMap) {

        String result = "[]";

        Long  comid = -1L;
        if(Check.isEmpty(reqParameterMap.get("comid"))){
            String unionId = reqParameterMap.get("union_id");
            String parkId = reqParameterMap.get("bolink_id");
            ComInfoTb com = commonService.getComInfoByUnionIdAndParkId(unionId,parkId);
            if(com==null){
                return result;
            }
            comid = com.getId();
        }else{
            comid = Long.parseLong(reqParameterMap.get("comid"));
        }

        if(comid<0){
            return result;
        }

        //因为加了厂商可以自定义车场管理员权限   所以需要查询是不是用云平台的管理员
        //云平台的管理员 adminid =0 ，  如果是厂商自定义的  那么adminid =0 and cityid = ?
        int parkAuth = 0;

        Long cityId = commonService.getCityIdByComid(comid);
        if(cityId!=null&&cityId>0){
            OrgCityMerchants city = commonService.getOrgCityById(cityId);
            if(city!=null){
                parkAuth = city.getSelfParkAuth();
            }
        }

        String sql = "select id as value_no,role_name as value_name from user_role_tb where oid =(select id from zld_orgtype_tb WHERE NAME = '停车场' AND state=0) and state =0 and (adminid in (SELECT id from user_info_tb where state=0 and comid = "+comid+" and auth_flag>0 and auth_flag!=14 and auth_flag!=15) or adminid =0) ";

        if(parkAuth==1){
            sql=  "select id as value_no,role_name as value_name from user_role_tb where oid =(select id from zld_orgtype_tb WHERE NAME = '停车场' AND state=0) and state =0 and (adminid in (SELECT id from user_info_tb where state=0 and comid = "+comid+" and auth_flag>0 and auth_flag!=14 and auth_flag!=15) or (adminid =0 and cityid= "+cityId+")) ";
        }
        List list = commonDao.getObjectBySql(sql);

        if(list!=null&&!list.isEmpty()){
            result = StringUtils.createJson(list);
        }
        return result;
    }

    @Override
    public JSONObject createMember(Map<String, String> reqParameterMap) {
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
//        Long auth_flag =-1L;
//        if(reqParameterMap.get("auth_flag")!=null&&!"".equals(reqParameterMap.get("auth_flag"))){
//            auth_flag =Long.parseLong(reqParameterMap.get("auth_flag"));
//        }
//        String loginuin = reqParameterMap.get("loginuin");

        Long role_id =-1L;
        if(reqParameterMap.get("role_id")!=null&&!"".equals(reqParameterMap.get("role_id"))){
            role_id = Long.parseLong(reqParameterMap.get("role_id"));
        }

        Long sex = -1L;
        if(reqParameterMap.get("sex")!=null&&!"".equals(reqParameterMap.get("sex"))){
            sex = Long.parseLong(reqParameterMap.get("sex"));
        }

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

        Long comId = -1L;
        if(!Check.isEmpty(reqParameterMap.get("comid"))){
            comId = Long.parseLong(reqParameterMap.get("comid"));
        }

        String unionId = reqParameterMap.get("union_id");
        String parkId = reqParameterMap.get("bolink_id");
        if(!Check.isEmpty(unionId)&&!Check.isEmpty(parkId)) {
            ComInfoTb com = commonService.getComInfoByUnionIdAndParkId(unionId, parkId);
            if (com == null) {
                return result;
            }
            comId =com.getId();
        }



        Long groupId = -1L;
        if(reqParameterMap.get("groupid")!=null&&!"undefined".equals(reqParameterMap.get("groupid"))&&!"".equals(reqParameterMap.get("groupid"))){
            groupId = Long.parseLong(reqParameterMap.get("groupid"));
        }
        if(groupId==null||groupId<0){
            groupId = commonService.getGroupIdByComid(comId);
        }
        logger.info("groupid:"+groupId);
        Long cityid = -1L;
        if(reqParameterMap.get("cityid")!=null&&!"undefined".equals(reqParameterMap.get("cityid"))&&!"".equals(reqParameterMap.get("cityid"))){
            cityid = Long.parseLong(reqParameterMap.get("cityid"));
        }


//        if(role_id == 30){
//            auth_flag = 1L;
//        }

        UserInfoTb user= new UserInfoTb();
        user.setId(nextid);
        user.setNickname(nickname);
        user.setPassword(strid);
        user.setStrid(strid);
        user.setRegTime(time);
        user.setMobile(mobile);
        user.setPhone(phone);
        user.setAuthFlag(1L);
        user.setComid(comId);
        user.setRoleId(role_id);
        user.setUserId(userId);
        user.setCityid(cityid);
        user.setGroupid(groupId);
        user.setSex(sex);
        logger.error("======>>>>>user"+user);
        int ret = commonDao.insert(user);
        if(ret==1){

            result.put("state",1);
            result.put("msg","增加成功");
            result.put("id",nextid);
            //不支持ETCPark,支持的话再加
            commonUtils.sendMessage(user,user.getComid(),nextid,1);
            insertSysn(user,0);
        }
        return result;
    }

    @Override
    public JSONObject updateMember(UserInfoTb userInfoTb) {

        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(userInfoTb);
        if(ret==1){
            UserInfoTb condition = new UserInfoTb();
            condition.setId(userInfoTb.getId());
            userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(condition);
            commonUtils.sendMessage(userInfoTb,userInfoTb.getComid(),userInfoTb.getId(),2);
            insertSysn(userInfoTb,1);
            result.put("state",1);
            result.put("msg","修改成功");
        }
        return result;
    }

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
            if(ret==1){
                userInfoTb =(UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
                commonUtils.sendMessage(userInfoTb,userInfoTb.getComid(),userInfoTb.getId(),2);
                insertSysn(userInfoTb,1);
//                if(publicMethods.isEtcPark(comid)){
//                    int ret = daService.update("insert into sync_info_pool_tb(comid,table_name,table_id,create_time,operate) values(?,?,?,?,?)", new Object[]{comid,"user_info_tb",Long.valueOf(uin),System.currentTimeMillis()/1000,1});
//                    logger.error("parkadmin or admin:"+loginuin+" edit parkuserpass:"+uin+",password:"+newPass+",ret:"+result+",add sync ret："+ret);
//                }else{
//                    logger.error("parkadmin or admin:"+loginuin+" edit parkuserpass:"+uin+",password:"+newPass+",ret:"+result);
//                }
            }
            result.put("state",ret);
            result.put("msg","修改密码成功");
        }else {
            result.put("msg","两次密码输入不一致，请重新输入！");
            return result;
        }
        return result;
    }

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
            userInfoTb =(UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
            commonUtils.sendMessage(userInfoTb,userInfoTb.getComid(),id,3);
            insertSysn(userInfoTb,2);
        }
        return result;
    }

    @Override
    public JSONObject isview(Map<String, String> reqParameterMap) {

        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        Long id = Long.parseLong(reqParameterMap.get("id"));
        Integer isview =Integer.parseInt(reqParameterMap.get("isview"));
        int ret = 0;
        if(id!=-1&&isview!=-1){
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setId(id);
            userInfoTb.setIsview(isview);
            ret = commonDao.updateByPrimaryKey(userInfoTb);
            if(ret==1){
                userInfoTb =(UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
                commonUtils.sendMessage(userInfoTb,userInfoTb.getComid(),id,2);
                insertSysn(userInfoTb,1);
            }
            result.put("state",ret);
            result.put("msg","修改成功");
        }
        return result;
    }


    private void insertSysn(UserInfoTb userInfoTb,Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(userInfoTb.getComid());
        syncInfoPoolTb.setTableId(userInfoTb.getId());
        syncInfoPoolTb.setTableName("user_info_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        syncInfoPoolTb.setUpdateTime(System.currentTimeMillis()/1000);
        commonDao.insert(syncInfoPoolTb);
    }

}
