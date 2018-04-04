package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.CityMemberService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/citymember")
public class CityMemberManagerAction {

    Logger logger = Logger.getLogger(CityMemberManagerAction.class);

    @Autowired
    private CityMemberService cityMemberService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityMemberService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/getrole")
    public String getRole(HttpServletRequest request, HttpServletResponse resp){


        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        String result = cityMemberService.getRoleByConditions(reqParameterMap);

        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    @RequestMapping(value = "/createmember")
    public String createMember(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityMemberService.createMember(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
//
    @RequestMapping(value = "/editmember")
    public String editMember(HttpServletRequest request, HttpServletResponse resp){

        String nickname =RequestUtil.processParams(request, "nickname");
        String resume = RequestUtil.processParams(request,"resume");
        String phone =RequestUtil.processParams(request, "phone");
        String mobile =RequestUtil.processParams(request, "mobile");
//        Long role_id = RequestUtil.getLong(request, "role_id", -1L);
        //修改时间
        Long updateTimeLong = System.currentTimeMillis()/1000;

        Long id =RequestUtil.getLong(request, "id", -1L);

        UserInfoTb userInfoTb =new UserInfoTb();
        userInfoTb.setNickname(nickname);
        userInfoTb.setPhone(phone);
        userInfoTb.setMobile(mobile);
//        userInfoTb.setRoleId(role_id);
        userInfoTb.setResume(resume);
        userInfoTb.setUpdateTime(updateTimeLong);
        userInfoTb.setId(id);
        JSONObject result = cityMemberService.updateMember(userInfoTb);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
//
    @RequestMapping(value = "/editpass")
    public String editpass(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityMemberService.editpass(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
//
    @RequestMapping(value = "/delmember")
    public String delmember(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityMemberService.delmember(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


}
