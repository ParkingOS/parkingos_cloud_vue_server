package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.GroupRoleService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.MongoDbUtils;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/grouprole")
public class GroupRoleAction {

    Logger logger = LoggerFactory.getLogger(GroupRoleAction.class);

    @Autowired
    private GroupRoleService groupRoleService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = groupRoleService.selectResultByConditions(reqParameterMap);

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
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);//(Long)request.getSession().getAttribute("loginuin");//登录的用户id
        Long oid = RequestUtil.getLong(request, "oid", -1L);//(Long)request.getSession().getAttribute("oid");//登录角色所属组织类型
        String name = RequestUtil.getString(request, "role_name");
        Integer state = RequestUtil.getInteger(request, "state", 0);
        String resume = RequestUtil.getString(request, "resume");
        Integer func = RequestUtil.getInteger(request, "func", -1);
        Long id = groupRoleService.getId();
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(id);
        userRoleTb.setRoleName(name);
        userRoleTb.setState(state);
        userRoleTb.setResume(resume);
        userRoleTb.setType(1);
        userRoleTb.setAdminid(uin);
        userRoleTb.setOid(oid);

        JSONObject result = groupRoleService.addRole(userRoleTb,func);

        if(((Integer)result.get("state"))==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"增加了角色"+id+name);
            parkLogTb.setType("adminrole");
            parkLogTb.setGroupId(groupid);
            saveLogService.saveLog(parkLogTb);
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
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long id = RequestUtil.getLong(request,"id",-1L);
        String name = StringUtils.decodeUTF8(RequestUtil.getString(request, "role_name"));
        String resume = StringUtils.decodeUTF8(RequestUtil.getString(request, "resume"));
        Integer func = RequestUtil.getInteger(request, "func", -1);
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(id);
        userRoleTb.setRoleName(name);
        userRoleTb.setResume(resume);

        JSONObject result = groupRoleService.updateRole(userRoleTb,func);


        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了角色"+id);
            parkLogTb.setType("adminrole");
            parkLogTb.setGroupId(groupid);
            saveLogService.saveLog(parkLogTb);
        }

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/deleterole")
    public String deleteRole(HttpServletRequest request, HttpServletResponse resp){

        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long id = RequestUtil.getLong(request,"id",-1L);

        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(id);
        userRoleTb.setState(1);
        JSONObject result = groupRoleService.deleteRole(userRoleTb);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了角色"+id);
            parkLogTb.setType("adminrole");
            parkLogTb.setGroupId(groupid);
            saveLogService.saveLog(parkLogTb);
        }

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

        String result = groupRoleService.getAuth(loginRoleId,id);

        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 从前台获取权限 编辑角色权限
    *
    * */
    @RequestMapping(value = "/editroleauth")
    public String editRoleAuth(HttpServletRequest request, HttpServletResponse resp){

        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long id = RequestUtil.getLong(request,"id",-1L);

        String auths = RequestUtil.getString(request,"auths");

        JSONObject result = groupRoleService.editRoleAuth(id,auths);

        if((Integer)result.get("state")==1&&groupid!=null&&groupid>0){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了角色权限");
            parkLogTb.setType("adminrole");
            parkLogTb.setGroupId(groupid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }
}
