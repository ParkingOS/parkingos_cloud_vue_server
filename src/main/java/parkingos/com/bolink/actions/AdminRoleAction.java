package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.CollectorSetTb;
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

    @RequestMapping(value = "/precollectset")
    public String precollectset(HttpServletRequest request, HttpServletResponse resp){
        Long roleid = RequestUtil.getLong(request, "roleid", -1L);
        CollectorSetTb collectorSetTb = new CollectorSetTb();
        collectorSetTb.setRoleId(roleid);
        JSONObject result = adminRoleService.precollectset(collectorSetTb);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/collectset")
    public String collectset(HttpServletRequest request, HttpServletResponse resp){

        Long id = RequestUtil.getLong(request, "role_id", -1L);
        Long roleId = RequestUtil.getLong(request, "roleid", -1L);
        Integer photoset1 = RequestUtil.getInteger(request, "photoset1", 0);
        Integer photoset2 = RequestUtil.getInteger(request, "photoset2", 0);
        Integer photoset3 = RequestUtil.getInteger(request, "photoset3", 0);
        String prepayset1=RequestUtil.getString(request, "prepayset1");
        String prepayset2=RequestUtil.getString(request, "prepayset2");
        String prepayset3=RequestUtil.getString(request, "prepayset3");
        String print_sign1=StringUtils.decodeUTF8(RequestUtil.getString(request, "print_sign1"));
        String print_sign2=StringUtils.decodeUTF8(RequestUtil.getString(request, "print_sign2"));
        String print_sign3=StringUtils.decodeUTF8(RequestUtil.getString(request, "print_sign3"));
        String print_sign4=StringUtils.decodeUTF8(RequestUtil.getString(request, "print_sign4"));
        Integer changePrePay = RequestUtil.getInteger(request, "change_prepay", 0);
        Integer view_plot = RequestUtil.getInteger(request, "view_plot", 0);
        Integer isprepay = RequestUtil.getInteger(request, "isprepay", 0);
        Integer hidedetail = RequestUtil.getInteger(request, "hidedetail", 0);
        Integer is_sensortime = RequestUtil.getInteger(request, "is_sensortime", 0);
        String collpwd = RequestUtil.processParams(request, "collpwd");
        String signpwd = RequestUtil.processParams(request, "signpwd");
        Integer signout_valid = RequestUtil.getInteger(request, "signout_valid", 0);
        Integer is_show_card = RequestUtil.getInteger(request, "is_show_card", 0);
        Integer print_order_place2 = RequestUtil.getInteger(request, "print_order_place2", 0);
        Integer is_duplicate_order = RequestUtil.getInteger(request, "is_duplicate_order", 1);
        Integer is_print_name = RequestUtil.getInteger(request, "is_print_name", 1);
//        JSONObject result = adminRoleService.precollectset(collectorSetTb);
//        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/editroleauth")
    public String editRoleAuth(HttpServletRequest request, HttpServletResponse resp){
        Long loginRoleId = RequestUtil.getLong(request,"loginroleid",-1L);
        Long id = RequestUtil.getLong(request,"id",1L);

        return null;
    }
}
