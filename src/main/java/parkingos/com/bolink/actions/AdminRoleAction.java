package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.AdminRoleService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/adminrole")
public class AdminRoleAction {

    Logger logger = Logger.getLogger(AdminRoleAction.class);

    @Autowired
    private AdminRoleService adminRoleService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = adminRoleService.selectResultByConditions(reqParameterMap);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/addrole")
    public String addRole(HttpServletRequest request, HttpServletResponse resp){

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);//(Long)request.getSession().getAttribute("loginuin");//登录的用户id
        Long oid = RequestUtil.getLong(request, "oid", -1L);//(Long)request.getSession().getAttribute("oid");//登录角色所属组织类型
        String name = StringUtils.decodeUTF8(RequestUtil.getString(request, "role_name"));
        Integer state = RequestUtil.getInteger(request, "state", 0);
        String resume = StringUtils.decodeUTF8(RequestUtil.getString(request, "resume"));
        Integer func = RequestUtil.getInteger(request, "func", -1);
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setRoleName(name);
        userRoleTb.setState(state);
        userRoleTb.setResume(resume);
        userRoleTb.setType(1);
        userRoleTb.setAdminid(uin);
        userRoleTb.setOid(oid);

        JSONObject result = adminRoleService.addRole(userRoleTb,func);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/editrole")
    public String editRole(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);
        String name = StringUtils.decodeUTF8(RequestUtil.getString(request, "role_name"));
        String resume = StringUtils.decodeUTF8(RequestUtil.getString(request, "resume"));
        Integer func = RequestUtil.getInteger(request, "func", -1);
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(id);
        userRoleTb.setRoleName(name);
        userRoleTb.setResume(resume);

        JSONObject result = adminRoleService.updateRole(userRoleTb,func);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/deleterole")
    public String deleteRole(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);

        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(id);
        userRoleTb.setState(1);
        JSONObject result = adminRoleService.deleteRole(userRoleTb);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
