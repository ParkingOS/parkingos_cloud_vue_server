package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.MemberService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberManagerAction {

    Logger logger = Logger.getLogger(MemberManagerAction.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/getrole")
    public String getRole(HttpServletRequest request, HttpServletResponse resp){


        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        String result = memberService.getRoleByConditions(reqParameterMap);

        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    @RequestMapping(value = "/createmember")
    public String createMember(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.createMember(reqParameterMap);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"注册了员工"+result.get("id")+reqParameterMap.get("nickname"));
            parkLogTb.setType("member");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/editmember")
    public String editMember(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String nickname =RequestUtil.processParams(request, "nickname");
        String phone =RequestUtil.processParams(request, "phone");
        String mobile =RequestUtil.processParams(request, "mobile");
        if(mobile.equals("")){
            mobile = null;
        }
        Long role_id = RequestUtil.getLong(request, "role_id", -1L);
        Integer isview = RequestUtil.getInteger(request, "isview", -1);
        Long sex = RequestUtil.getLong(request, "sex", -1L);
        //修改时间
        Long updateTimeLong = System.currentTimeMillis()/1000;

        Long id =RequestUtil.getLong(request, "id", -1L);

        UserInfoTb userInfoTb =new UserInfoTb();
        userInfoTb.setNickname(nickname);
        userInfoTb.setPhone(phone);
        userInfoTb.setMobile(mobile);
        userInfoTb.setRoleId(role_id);
        userInfoTb.setSex(sex);
        userInfoTb.setIsview(isview);
        userInfoTb.setUpdateTime(updateTimeLong);
        userInfoTb.setId(id);
        JSONObject result = memberService.updateMember(userInfoTb);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname1);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname1+")"+"修改了员工"+id);
            parkLogTb.setType("member");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/editpass")
    public String editpass(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.editpass(reqParameterMap);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname1);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname1+")"+"修改了"+reqParameterMap.get("id")+"的密码");
            parkLogTb.setType("member");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delmember")
    public String delmember(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.delmember(reqParameterMap);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname1);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname1+")"+"删除了员工"+reqParameterMap.get("id"));
            parkLogTb.setType("member");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/isview")
    public String isview(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.isview(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
