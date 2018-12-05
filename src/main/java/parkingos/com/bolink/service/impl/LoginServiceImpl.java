package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.service.LoginService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SaveLogService saveLogService;


    @Override
    public JSONObject getResultByUserNameAndPass(String userId, String cpasswd) {
        //测试期间设置登录有效期为1小时
        JSONObject result = JSONObject.parseObject("{}");
        JSONObject user = JSONObject.parseObject("{}");
        result.put("state", true);

        ParkLogTb parkLogTb = new ParkLogTb();

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
//            if("admin".equals(userId)){
//                userInfoTb.setPassword(null);
//                userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
//                if(userInfoTb==null){
//                    result.put("state", true);
//                    result.put("msg", "新建账号");
//                    user.put("oid", CustomDefind.getValue("UNIONADMIN"));//开源云之后新建admin账号
//                    result.put("user",user);
//                    return result;
//                }else{
//                    result.put("state", false);
//                    result.put("msg", "账号或密码错误");
//                    return result;
//                }
//            }
            result.put("state", false);
            result.put("msg", "账号或密码错误");
            return result;
        }

        String comids = CustomDefind.SECRETPARK;
        logger.info("====>>>>>>"+comids);
        String[] comidArr = comids.split(",");
        for(String parkId:comidArr){
            if(parkId.equals(userInfoTb.getComid()+"")){
                user.put("secret_park",1);
                break;
            }
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
            Long shopId = -1L;
            ShopTb shopTb = new ShopTb();
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
                user.put("orgname",orgname);
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
                        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
                        if(comInfoTb!=null){
                            user.put("name",comInfoTb.getCompanyName());
                            if(comInfoTb.getCityid()!=null&&comInfoTb.getCityid()>-1){
                                Long cityid = comInfoTb.getCityid();
                                OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
                                orgCityMerchants.setId(cityid);
                                orgCityMerchants.setState(0);
                                orgCityMerchants = (OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
                                if(orgCityMerchants!=null&&orgCityMerchants.getSelfRefillSetting()!=null){
                                    user.put("self_setting",orgCityMerchants.getSelfRefillSetting());
                                }
                            }else{
                                Long groupid = comInfoTb.getGroupid();
                                OrgGroupTb orgGroupTb = new OrgGroupTb();
                                orgGroupTb.setId(groupid);
                                orgGroupTb.setState(0);
                                orgGroupTb= (OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);
                                if (orgGroupTb!=null&&orgGroupTb.getCityid()!=null){
                                    Long cityid = orgGroupTb.getCityid();
                                    OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
                                    orgCityMerchants.setId(cityid);
                                    orgCityMerchants.setState(0);
                                    orgCityMerchants = (OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
                                    if(orgCityMerchants!=null&&orgCityMerchants.getSelfRefillSetting()!=null){
                                        user.put("self_setting",orgCityMerchants.getSelfRefillSetting());
                                    }
                                }
                            }
                        }

                        parkLogTb.setParkId(userInfoTb.getComid());

                        user.put("comid", userInfoTb.getComid());
                        user.put("parkid", userInfoTb.getComid());
                    }
                } else if (orgname.contains("渠道")) {
                    user.put("isadmin", 1);
                    user.put("cloudname", "智慧停车云-渠道云 ");
                    user.put("chanid", userInfoTb.getChanid());
//                    user.put("comid", -1);
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
//                    user.put("comid", -1);
                    OrgGroupTb orgGroupTb = new OrgGroupTb();
                    orgGroupTb.setId(userInfoTb.getGroupid());
                    orgGroupTb.setState(0);
                    int groupcount = commonDao.selectCountByConditions(orgGroupTb);
                    logger.error(">>>>>>>>>>>>>" + groupcount);
                    if (groupcount == 0) {
                        result.put("state", false);
                        result.put("msg", "集团不存在！");
                        return result;
                    }else{
                        parkLogTb.setGroupId(userInfoTb.getGroupid());
                        orgGroupTb=(OrgGroupTb) commonDao.selectObjectByConditions(orgGroupTb);
                        if(orgGroupTb!=null){
                            user.put("name",orgGroupTb.getName());
                        }
                    }
                } else if (orgname.contains("城市")) {
                    user.put("isadmin", 1);
                    user.put("cloudname", "智慧停车云-城市云 ");
                    user.put("cityid", userInfoTb.getCityid());
//                    user.put("comid", -1);
                    OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
                    orgCityMerchants.setId(userInfoTb.getCityid());
                    orgCityMerchants.setState(0);
                    int groupcount = commonDao.selectCountByConditions(orgCityMerchants);
                    if (groupcount == 0) {
                        result.put("state", false);
                        result.put("msg", "城市不存在！");
                        return result;
                    }else{
                        orgCityMerchants = (OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
                        if(orgCityMerchants!=null){
                            user.put("name",orgCityMerchants.getName());
                        }
                    }
                }else if (orgname.contains("商户")) {
                    user.put("isadmin", 1);
                    user.put("shopid", userInfoTb.getShopId());
                    shopTb.setId(userInfoTb.getShopId());
                    shopTb.setState(0);
                    int shopcount = commonDao.selectCountByConditions(shopTb);
                    if (shopcount == 0) {
                        result.put("state", false);
                        result.put("msg", "商户不存在！");
                        return result;
                    }else{
                        shopTb=(ShopTb)commonDao.selectObjectByConditions(shopTb);
                        if(shopTb!=null){
                            shopId = shopTb.getId();
                            user.put("name",shopTb.getName());
                            user.put("use_fix_code",shopTb.getUseFixCode());
                        }
                    }
                }
            }
            List<Map<String, Object>> authList = null;

            //所有权限
            String allsql = "select * from auth_tb where oid = "+userRoleTb.getOid()+" and state = 0";

            logger.info("allsql "+allsql);
            List<Map> allAuthList = commonDao.getObjectBySql(allsql);
            user.put("allauth", allAuthList);

            if (roleId == 0) {//总管理员拥有所有权限
                user.put("name","总后台");
               // AuthTb authTb = new AuthTb();
               // authTb.setOid(userRoleTb.getOid());
               // authTb.setState(0);
               // String sql = "select actions,id auth_id,nname,pid,url,sort,sub_auth childauths from " +
               //         "auth_tb where oid= "+userRoleTb.getOid()+" and state=0 ";
               // authList = commonDao.getObjectBySql(sql);

                String sql = "select actions,id auth_id,nname,pid,url,sort,sub_auth childauths from auth_tb where oid= "+userRoleTb.getOid()+" and state=0 ";
                authList = commonDao.getObjectBySql(sql);//commonDao.selectListByConditions(authTb);

//                if (authList != null) {
//                    for (Map<String, Object> map : authList) {
//                        if (map.get("childauths") != null) {
//                            String childauths = (String) map.get("childauths");
//                            if (!childauths.equals("")) {
//                                String[] subs = childauths.split(",");
//                                String subauth = null;
//                                for (int i = 0; i < subs.length; i++) {
//                                    if (i == 0) {
//                                        subauth = "" + i;
//                                    } else {
//                                        subauth += "," + i;
//                                    }
//                                }
//                                map.put("sub_auth", subauth);
//                            }
//                        }
//                    }
//                }

            } else {
                //读取权限
                String sql = "select a.actions,auth_id,nname,a.pid,a.url,a.sort,ar.sub_auth from auth_role_tb ar left join auth_tb a on ar.auth_id=a.id where role_id= "+roleId+" and a.state=0 order by  a.sort ";
                if(shopId>-1){
                    if(shopTb.getUseFixCode()==0){
                        sql = "select a.actions,auth_id,nname,a.pid,a.url,a.sort,ar.sub_auth from auth_role_tb ar left join auth_tb a on ar.auth_id=a.id where role_id= "+roleId+" and a.state=0  and a.nname not like '%固定码%' order by  a.sort ";
                    }
                }
                authList = commonDao.getObjectBySql(sql);

                logger.info("huoqu quanxian sql "+sql);
                for (Map<String, Object> map : authList) {
                    Long autId = (Long) map.get("auth_id");
                    String subAuth = (String) map.get("sub_auth");
                    for (Map map1 : allAuthList) {
                        Long aid = (Long)map1.get("id");
                        String sub_auth = (String) map1.get("sub_auth");
                        if (autId.equals(aid)) {
                            if (subAuth != null && !subAuth.equals("")) {
                                String s1[] = subAuth.split(",");
                                String s2[] = sub_auth.split(",");
                                String newSubAuth = "";
                                if (s2.length > 0) {
                                    for (String index : s1) {
                                        if (!newSubAuth.equals("")) {
                                            newSubAuth += ",";
                                        }
                                        Integer in = Integer.valueOf(index);
                                        if (in > s2.length - 1) {
//										if(s2.length>1)
//											newSubAuth +=index;
                                        } else {
                                            newSubAuth += s2[in];
                                        }
                                    }
                                }
                                map.put("sub_auth", newSubAuth);
                                break;
                            }
                        }
                    }
                }
            }

            user.put("ishdorder", userInfoTb.getOrderHid());
            user.put("authlist", authList);
            user.put("menuauthlist", StringUtils.createJson(authList));


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
        }
        user.put("nickname", nickname);

        user.put("userid", userId);
        user.put("lastlogin", System.currentTimeMillis() / 1000);
        result.put("user", user);

        //写日志
        parkLogTb.setOperateUser(nickname);
        parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
        parkLogTb.setOperateType(0);
        parkLogTb.setContent(userInfoTb.getId()+"("+nickname+")"+"登录了yun平台");
        parkLogTb.setType("login");
        saveLogService.saveLog(parkLogTb);


        //更新用户最新登录时间
        UserInfoTb con = new UserInfoTb();
        con.setId(userInfoTb.getId());
        con.setLogonTime(System.currentTimeMillis()/1000);
        commonDao.updateByPrimaryKey(con);

        return result;
    }

    @Override
    public JSONObject getCkey(String userId, String mobile, Integer userType) {
        //根据手机号查询车场,匹配用户id
        JSONObject result = new JSONObject();
        UserInfoTb userInfoTb = new UserInfoTb();
        Long id = -1L;
        try{
            id=Long.parseLong(userId);
        }catch(Exception e){
            logger.error("userid 解析异常"+userId,e);
        }
        if(id!=null&&id>-1){
            userInfoTb.setId(id);
        }else {
            userInfoTb.setStrid(userId);
        }

        userInfoTb  = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
        if(userInfoTb!=null){
            if(mobile.equals(userInfoTb.getMobile())){
                long code = Math.round(Math.random()*(9999-1000+1)+1000);
//                memcacheUtils.setCache(mobile+code, code+"", 300);
                String encode = Encryption.encryptToAESPKCS5(code+"", Encryption.KEY);
                logger.error("ckey:"+code+" encode:"+encode);
                result.put("state", 1);
                result.put("ckey", encode);
                result.put("userid",userInfoTb.getId());
            }else{
                result.put("state", 0);
                result.put("errmsg", "该手机号不是您绑定的手机");
            }
        }else{
            result.put("state",0);
            result.put("errmsg","用户不存在");
        }
        return result;
    }

    @Override
    public JSONObject regUser(String mobile, Long userid) {

        JSONObject result = new JSONObject();
        result.put("state", 0);

        //生成code,存库
        Long cTime = System.currentTimeMillis()/1000;
        Long code = Math.round(Math.random()*(9999-1000+1)+1000);
        logger.info("reguser>>>生成验证码:"+code);
        //存入数据库
        VerificationCodeTb verificationCodeTb = new VerificationCodeTb();
        verificationCodeTb.setMobile(mobile);
        verificationCodeTb = (VerificationCodeTb)commonDao.selectObjectByConditions(verificationCodeTb);
        VerificationCodeTb con = new VerificationCodeTb();
        int update = 0;
        if(verificationCodeTb!=null){
            con.setVerificationCode(code.intValue());
            con.setCreateTime(cTime);
            update = commonDao.updateByConditions(con,verificationCodeTb);
        }else{
            verificationCodeTb = new VerificationCodeTb();
            verificationCodeTb.setMobile(mobile);
            verificationCodeTb.setCreateTime(cTime);
            verificationCodeTb.setState(1L);
            verificationCodeTb.setUin(userid);
            verificationCodeTb.setVerificationCode(code.intValue());
            update = commonDao.insert(verificationCodeTb);
        }

        if(update==0){
            result.put("errmsg", "验证码错误");
            return result;
        }

        //4.发送验证码
        try {
            String sendResult = AliMessageUtil.sendMessage(mobile,code+"");
            if("1".equals(sendResult)){
                logger.info("reguser>>>验证码已发送:"+code);
                result.put("state", 1);
            }else{
                result.put("errmsg", "发送验证码失败!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject checkCode(String mobile, String code) {

        JSONObject result = new JSONObject();
        result.put("state", 0);
        //从数据库中查
        VerificationCodeTb con = new VerificationCodeTb();
        con.setMobile(mobile);
        con = (VerificationCodeTb) commonDao.selectObjectByConditions(con);
        Long creTime = null;
        Long curTime = System.currentTimeMillis()/1000;
        if (con!=null){
            creTime = con.getCreateTime();
        }else{
            result.put("errmsg", "验证码错误");
            return result;
        }

        if(curTime-creTime>3000){
            //过期
            result.put("errmsg", "验证码已过期");
            return result;
        }
        Integer _code =  con.getVerificationCode();
        if(code.equals(_code+"")){
            result.put("state", 1);
            return result;
        }else{
            result.put("state", 2);//验证码错误
            result.put("errmsg", "验证码错误");
            return result;
        }
    }

    @Override
    public JSONObject resetPwd(String passwd, Long userId) {

        JSONObject result = new JSONObject();
        result.put("state",0);
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setPassword(passwd);
        userInfoTb.setId(userId);
        int update = commonDao.updateByPrimaryKey(userInfoTb);

        if(update==1){
            result.put("state",1);
        }
        return result;
    }

    @Override
    public String sendCode(String mobile, Long userid, String ticket, String randstr,String ip) {
        /*
        *   aid (必填)	2043299115
            AppSecretKey (必填)	0ZDpi7hCP4sTt1HpdBhnUeA**
            Ticket (必填)	验证码客户端验证回调的票据
            Randstr (必填)	验证码客户端验证回调的随机串
            UserIP (必填)	提交验证的用户的IP地址（eg: 10.127.10.2）
        * */

//        ticket="t020uTO_h0GZEKFfByfZmIX5C7I2se7Kso00hXvDzCsErEh8hqBceCjr_Tpa0vvwssoBgL1UrL2ENBMIu5DD_jWXcj0a65gdJHfwrnxSsPaHsTRuIXEPW4ljA**";
//        randstr="@PIH";
        JSONObject jsonData = new JSONObject();
        jsonData.put("state",0);
        String url="https://ssl.captcha.qq.com/ticket/verify?Ticket="+ticket+"&Randstr="+randstr+"&UserIP="+ip+"&aid=2043299115&AppSecretKey=0ZDpi7hCP4sTt1HpdBhnUeA**";
        logger.info("进行发送验证码校验:"+url);

        HttpProxy httpProxy = new HttpProxy();
        String result = httpProxy.doGet(url);
        logger.info("验证码校验结果:"+result);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if("1".equals(jsonResult.get("response")+"")){
            String sendUrl = "http://"+CustomDefind.getValue("DOMAIN")+"/cloud/user/reguser";
            HttpProxy httpProxy1 = new HttpProxy();
            Map<String,Object> map = new HashMap<>();
            map.put("userid",userid);
            map.put("mobile",mobile);
            String codeResult = httpProxy1.doPostTwo(sendUrl,map);
            logger.info("send code result:"+codeResult);
            return codeResult;

        }
        jsonData.put("msg",result);
        return jsonData.toString();
    }
}
