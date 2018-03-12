package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrgGroupTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.GroupInfoService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class GroupInfoServiceImpl implements GroupInfoService {

    Logger logger = Logger.getLogger(GroupInfoServiceImpl.class);

    @Autowired
    private CommonDao commonDao;


    @Override
    public String getResultByCondition(Long groupid,Long uin) {

        JSONObject jsonObject = new JSONObject();

        logger.error("======>>>集团id:"+groupid);
        logger.error("======>>>登录账号:"+uin);
        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setId(groupid);
        orgGroupTb = (OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);

        jsonObject.put("group",orgGroupTb);
        jsonObject.put("user",userInfoTb);
        return jsonObject.toJSONString();
    }

    @Override
    public JSONObject updateGroupInfo(HttpServletRequest request) {

        String str = "{\"state\":0,\"msg\":\"更新失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        Long groupid = RequestUtil.getLong(request, "groupid", -1L);
        String name = StringUtils.decodeUTF8(RequestUtil.processParams(request, "name"));
        Integer type = RequestUtil.getInteger(request, "type", 0);
        Long uin = RequestUtil.getLong(request,"loginuin",-1L);
        String mobile = RequestUtil.processParams(request, "mobile");
        String phone = RequestUtil.processParams(request, "phone");
        String nickname = StringUtils.decodeUTF8(RequestUtil.processParams(request, "nickname"));
        String newpass = RequestUtil.processParams(request, "newpass");
        String confirmpass = RequestUtil.processParams(request, "confirmpass");
        int r = 0;
        if(!newpass.equals("")){
            int ret = editPass(uin, newpass, confirmpass);
            if(ret != 1){
                if(ret==-2){
                    result.put("msg","密码长度小于6位，请重新输入！");
                }else if(ret==-3){
                    result.put("msg","两次密码输入不一致，请重新输入！");
                }
                return result;
            }else{
                r = ret;
            }
        }
        if(r==1){
            OrgGroupTb orgGroupTb = new OrgGroupTb();
            orgGroupTb.setName(name);
            orgGroupTb.setType(type);
            orgGroupTb.setId(groupid);
            int updateGroup = commonDao.updateByPrimaryKey(orgGroupTb);

            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setMobile(mobile);
            userInfoTb.setPhone(phone);
            userInfoTb.setNickname(nickname);
            userInfoTb.setId(uin);
            int updateUser = commonDao.updateByPrimaryKey(userInfoTb);

            System.out.println("====updateGroup:"+updateGroup+"==updateUser:"+updateUser);
            result.put("state",1);
            result.put("msg","修改成功");
        }
        return result;
    }

    private int editPass(Long uin, String newPass, String confirmPass){
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setPassword(newPass);
        userInfoTb.setId(uin);

        String md5pass = newPass;
        try {
            md5pass = StringUtils.MD5(newPass);
            md5pass = StringUtils.MD5(md5pass+"zldtingchebao201410092009");
            userInfoTb.setMd5pass(md5pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int result = 0;
        if(newPass.length()<6){
            result = -2;
        }else if(newPass.equals(confirmPass)){
            int r = commonDao.updateByPrimaryKey(userInfoTb);
            result = r;
        }else {
            result = -3;
        }
        return result;
    }

}
