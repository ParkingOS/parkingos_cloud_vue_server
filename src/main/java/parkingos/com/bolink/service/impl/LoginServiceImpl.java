package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.service.LoginService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.ZLDType;

import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    Logger logger = Logger.getLogger(LoginServiceImpl.class);

    @Autowired
    private CommonDao commonDao;


    @Override
    public JSONObject getResultByUserNameAndPass(String userId, String cpasswd) {
        //测试期间设置登录有效期为1小时
        JSONObject result = JSONObject.parseObject("{}");
        JSONObject user = JSONObject.parseObject("{}");
        result.put("state", true);

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setState(0);
        userInfoTb.setPassword(cpasswd);
        if (Check.checkUin(userId)) {
            userInfoTb.setId(Long.valueOf(userId));
        } else {
            userInfoTb.setStrid(userId);
        }
        userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
        if (userInfoTb == null) {
            result.put("state", false);
            result.put("msg", "账号或密码错误");
            return result;
        }

        Long role = -1L;
        if (userInfoTb.getAuthFlag() != null) {
            role = Long.valueOf(userInfoTb.getAuthFlag().toString());
        }
        Long roleId = userInfoTb.getRoleId();

        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(roleId);
        userRoleTb = (UserRoleTb) commonDao.selectObjectByConditions(userRoleTb);

        user.put("isadmin", 0);
        user.put("loginroleid", roleId);
        user.put("adminid", -1);
        user.put("supperadmin", 0);
        user.put("loginuin", userInfoTb.getId());
        user.put("comid", userInfoTb.getComid());
        if (userRoleTb != null) {
            if (roleId == 0 || roleId == 8) {
                user.put("supperadmin", 1);
            }
        }

        if (roleId != null && roleId > -1) {
            user.put("roleid", roleId);

            ZldOrgtypeTb zldOrgtypeTb = new ZldOrgtypeTb();
            zldOrgtypeTb.setId(userRoleTb.getOid());
            zldOrgtypeTb = (ZldOrgtypeTb) commonDao.selectObjectByConditions(zldOrgtypeTb);
            if (zldOrgtypeTb == null) {
                result.put("state", false);
                result.put("msg", "组织类型不存在！");
                return result;
            } else {
                user.put("oid", userRoleTb.getOid());
                String orgname = zldOrgtypeTb.getName();
                if (orgname.contains("车场")) {
                    ComInfoTb comInfoTb = new ComInfoTb();
                    comInfoTb.setId(userInfoTb.getComid());
                    comInfoTb.setState(0);
                    int count = commonDao.selectCountByConditions(comInfoTb);

                    if (count == 0) {
                        result.put("state", false);
                        result.put("msg", "车场不存在或者车场未通过审核！");
                        return result;
                    } else {
//                        Map<String, Object> comMap = daService.getMap("select chanid from com_info_tb where id=? ",
//                                new Object[]{user.get("comid")});
//                        if(comMap != null && comMap.get("chanid") != null){
//                            Long chanid = (Long)comMap.get("chanid");
//                            if(chanid > 0){
//                                Map<String, Object> map = daService.getMap("select * from logo_tb where type=? and orgid=? ",
//                                        new Object[]{0, chanid});
//                                if(map != null&& map.get("url_fir") != null){
//                                    logourl = "cloudlogo.do?action=downloadlogo&type=0&orgid="+chanid+"&number=0&r="+Math.random();
//                                }
//                            }
//                        }
                        user.put("comid", userInfoTb.getComid());
                        user.put("parkid", userInfoTb.getComid());
                    }
                } else if (orgname.contains("渠道")) {
                    user.put("isadmin", 1);
                    user.put("cloudname", "智慧停车云-渠道云 ");
                    user.put("chanid", userInfoTb.getChanid());
                    user.put("comid", 0);
                    OrgChannelTb orgChannelTb = new OrgChannelTb();
                    orgChannelTb.setId(userInfoTb.getChanid());
                    int chancount = commonDao.selectCountByConditions(orgChannelTb);
                    if (chancount == 0) {
                        result.put("state", false);
                        result.put("msg", "渠道不存在！");
                        return result;
                    } else {
//                        Map<String, Object> map = daService.getMap("select * from logo_tb where type=? and orgid=? ",
//                                new Object[]{0, user.get("chanid")});
//                        if(map != null&& map.get("url_fir") != null){
//                            logourl = "cloudlogo.do?action=downloadlogo&type=0&orgid="+user.get("chanid")+"&number=0&r="+Math.random();
//                        }
                    }
                } else if (orgname.contains("集团")) {
                    user.put("isadmin", 1);
                    user.put("cloudname", "智慧城市云 ");
                    user.put("groupid", userInfoTb.getGroupid());
                    user.put("comid", 0);
                    OrgGroupTb orgGroupTb = new OrgGroupTb();
                    orgGroupTb.setId(userInfoTb.getGroupid());
                    orgGroupTb.setState(0);
                    int groupcount = commonDao.selectCountByConditions(orgGroupTb);
                    logger.error(">>>>>>>>>>>>>" + groupcount);
                    if (groupcount == 0) {
                        result.put("state", false);
                        result.put("msg", "集团不存在！");
                        return result;
                    }
                } else if (orgname.contains("城市")) {
                    user.put("isadmin", 1);
                    user.put("cloudname", "智慧停车云-城市云 ");
                    user.put("cityid", userInfoTb.getCityid());
                    user.put("comid", 0);
                    OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
                    orgCityMerchants.setId(userInfoTb.getGroupid());
                    orgCityMerchants.setState(0);
                    int groupcount = commonDao.selectCountByConditions(orgCityMerchants);
                    if (groupcount == 0) {
                        result.put("state", false);
                        result.put("msg", "城市不存在！");
                        return result;
                    }
                }
            }
            List<Map<String, Object>> authList = null;
            if (roleId == 0) {//总管理员拥有所有权限

               // AuthTb authTb = new AuthTb();
               // authTb.setOid(userRoleTb.getOid());
               // authTb.setState(0);
               // String sql = "select actions,id auth_id,nname,pid,url,sort,sub_auth childauths from " +
               //         "auth_tb where oid= "+userRoleTb.getOid()+" and state=0 ";
               // authList = commonDao.getObjectBySql(sql);

                String sql = "select actions,id auth_id,nname,pid,url,sort,sub_auth childauths from auth_tb where oid= "+userRoleTb.getOid()+" and state=0 ";
                authList = commonDao.getObjectBySql(sql);//commonDao.selectListByConditions(authTb);

                if (authList != null) {
                    for (Map<String, Object> map : authList) {
                        if (map.get("childauths") != null) {
                            String childauths = (String) map.get("childauths");
                            if (!childauths.equals("")) {
                                String[] subs = childauths.split(",");
                                String subauth = null;
                                for (int i = 0; i < subs.length; i++) {
                                    if (i == 0) {
                                        subauth = "" + i;
                                    } else {
                                        subauth += "," + i;
                                    }
                                }
                                map.put("sub_auth", subauth);
                            }
                        }
                    }
                }
            } else {
                //读取权限
                String sql = "select a.actions,auth_id,nname,a.pid,a.url,a.sort,a.sub_auth " +
                        "from auth_role_tb ar left join" +
                        " auth_tb a on ar.auth_id=a.id" +
                        " where role_id= " + roleId +" and a.state=0 order by  a.sort ";
                authList = commonDao.getObjectBySql(sql);
            }
            user.put("ishdorder", userInfoTb.getOrderHid());
            user.put("authlist", authList);
            user.put("menuauthlist", StringUtils.createJson(authList));

            AuthTb authTb = new AuthTb();
            authTb.setOid(userRoleTb.getOid());
            authTb.setState(0);
            List<AuthTb> allAuthList = commonDao.selectListByConditions(authTb);

            user.put("allauth", allAuthList);
        } else {
            //role: 0总管理员，1停车场后台管理员 ，2车场收费员，3财务，4车主  5市场专员 6录入员
            if (role.intValue() == ZLDType.ZLD_COLLECTOR_ROLE || role.intValue() == ZLDType.ZLD_CAROWER_ROLE || role.intValue() == ZLDType.ZLD_KEYMEN) {//车场收费员及车主不能登录后台
                result.put("state", false);
                result.put("msg", "没有查询后台数据权限，请联系管理员!！");
                return result;
            } else if (role.intValue() == ZLDType.ZLD_PARKADMIN_ROLE) {
                user.put("target", "parkmanage");
                user.put("cloudname", "智慧停车云-车场云 ");
                ComInfoTb comInfoTb = new ComInfoTb();
                comInfoTb.setId(userInfoTb.getComid());
                comInfoTb.setState(0);
                int count = commonDao.selectCountByConditions(comInfoTb);

                if (count == 0) {
                    result.put("state", false);
                    result.put("msg", "车场不存在或者车场未通过审核！");
                    return result;
                }
            } else if (role.intValue() == ZLDType.ZLD_ACCOUNTANT_ROLE) {
                user.put("target", "finance");
            } else if (role.intValue() == ZLDType.ZLD_CARDOPERATOR) {
                user.put("target", "cardoperator");
            } else if (role.intValue() == ZLDType.ZLD_MARKETER) {//市场专员 登录后台
                user.put("marketerid", userInfoTb.getId());
                user.put("target", "marketer");
            } else if (role.intValue() == ZLDType.ZLD_RECORDER || role.intValue() == ZLDType.ZLD_KEFU || role.intValue() == ZLDType.ZLD_QUERYKEFU) {
                user.put("target", "recorder");
            } else if (role == 0) {//总管理员
                user.put("supperadmin", 1);
            } else {
                result.put("state", false);
                result.put("msg", "没有登录权限！");
                return result;
            }
        }
        String nickname = "";
        if (userInfoTb.getNickname() != null) {
            nickname = userInfoTb.getNickname();
            user.put("nickname", nickname);
        }

        user.put("userid", userId);
        user.put("lastlogin", System.currentTimeMillis() / 1000);
        result.put("user", user);
        return result;
    }
}
