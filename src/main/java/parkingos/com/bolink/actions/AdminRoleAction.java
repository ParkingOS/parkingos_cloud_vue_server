package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.CollectorSetTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.AdminRoleService;
import parkingos.com.bolink.utils.MongoDbUtils;
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

    @Autowired
    private MongoDbUtils mongoDbUtils;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = adminRoleService.selectResultByConditions(reqParameterMap);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /**
     *
     * 增加角色
     */
    @RequestMapping(value = "/addrole")
    public String addRole(HttpServletRequest request, HttpServletResponse resp){

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);//(Long)request.getSession().getAttribute("loginuin");//登录的用户id
        Long oid = RequestUtil.getLong(request, "oid", -1L);//(Long)request.getSession().getAttribute("oid");//登录角色所属组织类型
        String name = RequestUtil.getString(request, "role_name");
        Integer state = RequestUtil.getInteger(request, "state", 0);
        String resume = RequestUtil.getString(request, "resume");
        Integer func = RequestUtil.getInteger(request, "func", -1);
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setRoleName(name);
        userRoleTb.setState(state);
        userRoleTb.setResume(resume);
        userRoleTb.setType(1);
        userRoleTb.setAdminid(uin);
        userRoleTb.setOid(oid);

        JSONObject result = adminRoleService.addRole(userRoleTb,func);

        logger.error("=========>>>"+((Integer)result.get("state")==1));
        if(((Integer)result.get("state"))==1){
            mongoDbUtils.saveLogs(request, 0, 2, "添加了角色："+name);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /*
    *
    * 编辑角色
    *
    * */
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

        logger.error("=========>>>edit:"+((Integer)result.get("state")==1));

        if((Integer)result.get("state")==1){
            logger.error("====修改角色日志");
            mongoDbUtils.saveLogs(request, 0, 3, "修改了角色："+name);
        }

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

        if((Integer)result.get("state")==1){
            mongoDbUtils.saveLogs(request, 0, 4, "删除了角色："+id);
        }

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /*
    * 云平台接口  暂时不用
    *
    * */
    @RequestMapping(value = "/precollectset")
    public String precollectset(HttpServletRequest request, HttpServletResponse resp){
        Long roleid = RequestUtil.getLong(request, "roleid", -1L);
        CollectorSetTb collectorSetTb = new CollectorSetTb();
        collectorSetTb.setRoleId(roleid);
        JSONObject result = adminRoleService.precollectset(collectorSetTb);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    /*
    * 获得所有权限并回显
    *
    * */
    @RequestMapping(value = "/getroleauth")
    public String getroleauth(HttpServletRequest request, HttpServletResponse resp){
        Long loginRoleId = RequestUtil.getLong(request,"loginroleid",-1L);
        Long id = RequestUtil.getLong(request,"id",1L);

        String result = adminRoleService.getAuth(loginRoleId,id);

        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 从前台获取权限 编辑角色权限
    *
    * */
    @RequestMapping(value = "/editroleauth")
    public String editRoleAuth(HttpServletRequest request, HttpServletResponse resp){
        System.out.println("========进入方法");
        Long id = RequestUtil.getLong(request,"id",-1L);

        String auths = RequestUtil.getString(request,"auths");
        System.out.println("======更改权限id:"+id);
        System.out.println("======更改权限auths:"+auths);

        JSONObject result = adminRoleService.editRoleAuth(id,auths);

        if((Integer)result.get("state")==1){
            mongoDbUtils.saveLogs(request, 0, 3, "修改了角色权限:"+id);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }
}
