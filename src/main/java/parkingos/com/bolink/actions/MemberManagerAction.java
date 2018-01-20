package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.MemberService;
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

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.createMember(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/editmember")
    public String editMember(HttpServletRequest request, HttpServletResponse resp){

        String nickname =StringUtils.decodeUTF8(RequestUtil.processParams(request, "nickname"));
        String strid =StringUtils.decodeUTF8(RequestUtil.processParams(request, "strid"));
        String phone =RequestUtil.processParams(request, "phone");
        String mobile =RequestUtil.processParams(request, "mobile");
        if(mobile.equals("")){
            mobile = null;
        }
        Long role_id = RequestUtil.getLong(request, "role_id", -1L);
        Integer isview = RequestUtil.getInteger(request, "isview", -1);
        //修改时间
        Long updateTimeLong = System.currentTimeMillis()/1000;

        Long id =RequestUtil.getLong(request, "id", -1L);

        UserInfoTb userInfoTb =new UserInfoTb();
        userInfoTb.setNickname(nickname);
        userInfoTb.setStrid(strid);
        userInfoTb.setPhone(phone);
        userInfoTb.setMobile(mobile);
        userInfoTb.setRoleId(role_id);
        userInfoTb.setIsview(isview);
        userInfoTb.setUpdateTime(updateTimeLong);
        userInfoTb.setId(id);
        JSONObject result = memberService.updateMember(userInfoTb);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/editpass")
    public String editpass(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.editpass(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delmember")
    public String delmember(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = memberService.delmember(reqParameterMap);

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
