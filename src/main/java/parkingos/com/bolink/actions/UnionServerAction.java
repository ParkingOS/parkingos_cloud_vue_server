package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.CityGroupService;
import parkingos.com.bolink.service.UnionServerService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/unionserver")
public class UnionServerAction {

    Logger logger = LoggerFactory.getLogger(UnionServerAction.class);

    @Autowired
    private UnionServerService unionServerService;


    /*
    * 厂商后台增加和编辑服务商
    *
    * */
    @RequestMapping(value = "/addAndEdit")
    public String addAndEdit(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Long id = RequestUtil.getLong(request, "id", -1L);
            String name = RequestUtil.getString(request, "name");
            String address = RequestUtil.getString(request, "address");
            String phone = RequestUtil.getString(request, "phone");
            Integer state = RequestUtil.getInteger(request, "state", 1);
            Long parentId = RequestUtil.getLong(request, "parent_id", -1L);
            Long unionId = RequestUtil.getLong(request, "unionid", -1L);
            Long cityId = RequestUtil.getLong(request, "cityid", -1L);
            String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);
            logger.info("addAndEdit unionServer:name" + name +"~~address:"+ address+"~~phone:" + phone+"~~id:" + id);
            result =  unionServerService.addOrEdit(name, address, phone, state, parentId, id, unionId, cityId,nickname,uin);
//            StringUtils.ajaxOutput(resp, result.toJSONString());
//            return null;
        }catch (Exception e){
            logger.error("===>>>>addAndEdit error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    /*
    * 厂商后台服务商页面  添加员工  只可添加管理员
    *
    * */
    @RequestMapping(value = "/addMemberFromUnion")
    public String addMember(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            String name = RequestUtil.getString(request, "nickname");
            String mobile = RequestUtil.getString(request, "mobile");
            String phone = RequestUtil.getString(request, "phone");
            Long serverId = RequestUtil.getLong(request, "serverid", -1L);
            logger.info("unionServer addMember :" + name + "~" + mobile + "~" + phone);
            result= unionServerService.addMember(name, phone, mobile, serverId);
        }catch (Exception e){
            logger.error("===>>>>addMember error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }



    @RequestMapping(value = "/addMemberFromServer")
    public String addMemberFromServer(HttpServletRequest request, HttpServletResponse resp){


        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = unionServerService.addMemberFromServer(reqParameterMap);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }



    /*
    * 查询服务商员工时  通用接口
    *
    * */
    @RequestMapping(value = "/queryMember")
    public String queryMember(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
            result = unionServerService.queryMembers(reqParameterMap);
        }catch (Exception e){
            logger.error("===>>>>queryMember error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /*
    *
    * 厂商后台查看服务商员工时 该服务商所有的角色
    *
    * */
    @RequestMapping(value = "/getRolesFromUnion")
    public String getRoles(HttpServletRequest request,HttpServletResponse resp) {
        String result = "[]";
        try {
            Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
            result = unionServerService.getRoles(reqParameterMap);
        }catch (Exception e){
            logger.error("===>>>>getRoles error",e);
        }
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


    /*
       * 编辑服务商员工时  通用接口
       *
       * */
    @RequestMapping(value = "/editMember")
    public String editMember(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            String name = RequestUtil.getString(request, "nickname");
            String mobile = RequestUtil.getString(request, "mobile");
            String phone = RequestUtil.getString(request, "phone");
            Long roleId = RequestUtil.getLong(request,"role_id",-1L);
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("===>>>>>编辑服务商员工：" + name + "~~" + mobile + "~~" + phone + "~~" + id+"~~"+roleId);
            result = unionServerService.editMember(name, mobile, phone, id,roleId);
        }catch (Exception e){
            logger.error("===>>>>editMember error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }

    /*
      * 删除服务商员工时  通用接口
      *
      * */
    @RequestMapping(value = "/deleteMember")
    public String deleteMember(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("===>>>>>删除服务商员工：" + id);
            result = unionServerService.deleteMember(id);
        }catch (Exception e){
            logger.error("===>>>>deleteMember error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }


    /*
    * 修改服务商员工密码  通用接口
    *
    * */
    @RequestMapping(value = "/editMemberPass")
    public String editMemberPass(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            String newPass = RequestUtil.getString(request, "newpass");
            String confirmPass = RequestUtil.getString(request, "confirmpass");
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("===>>>>>删除服务商员工：" + id);
            result = unionServerService.editMemberPass(id, newPass, confirmPass);
        }catch (Exception e){
            logger.error("===>>>>editMemberPass error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }


    /*
    *
    * 服务商后台  运营商管理
    *
    *
    * */

    @RequestMapping(value = "/groupsByServer")
    public String groupsByServer(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Map<String, String> paramMap = RequestUtil.readBodyFormRequset(request);
            logger.info("get groups by serverId：" + paramMap);
            result= unionServerService.groupsByServer(paramMap);
        }catch (Exception e){
            logger.error("===>>>>groupsByServer error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }


    /*
    *
    * 服务商后台  车场管理
    *
    *
    * */

    @RequestMapping(value = "/parksByServer")
    public String parksByServer(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Map<String, String> paramMap = RequestUtil.readBodyFormRequset(request);
            logger.info("get groups by serverId：" + paramMap);
            result= unionServerService.parksByServer(paramMap);
        }catch (Exception e){
            logger.error("===>>>>parksByServer error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }


     /*
    *
    * 服务商后台  子服务商列表
    *
    *
    * */

    @RequestMapping(value = "/childServer")
    public String childServer(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Map<String, String> paramMap = RequestUtil.readBodyFormRequset(request);
            logger.info("get childServer by serverId：" + paramMap);
            result= unionServerService.childServer(paramMap);
        }catch (Exception e){
            logger.error("===>>>>childServer error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }

    /*
    *
    * 服务商后台获取角色列表  （有分页）
    *
    * */
    @RequestMapping(value = "/getRolesByServer")
    public String getRolesByServer(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
//            Long serverid = RequestUtil.getLong(request,"serverid",-1L);
//            Long uin = RequestUtil.getLong(request,"loginuin",-1L);
            Map<String, String> paramMap = RequestUtil.readBodyFormRequset(request);
            logger.info("getRolesByServer by server：" + paramMap);
            result= unionServerService.getRolesByServer(paramMap);
        }catch (Exception e){
            logger.error("===>>>>getRolesByServer error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    /*
   *
   * 服务商后台增加角色
   *
   * */
    @RequestMapping(value = "/addOrEditRole")
    public String addOrEditRole(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Long id = RequestUtil.getLong(request, "id", -1L);
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);
            Long oid = RequestUtil.getLong(request, "oid", -1L);
            String name = RequestUtil.getString(request, "role_name");
            String resume = RequestUtil.getString(request, "resume");
            Integer state = RequestUtil.getInteger(request, "state", 0);
            logger.info("addOrEditRole by server：" + id+"~~"+uin+"~~~"+oid+"~~"+name+"~~"+resume+"~~"+state);
            result= unionServerService.addOrEditRole(id,uin,oid,name,resume,state);
        }catch (Exception e){
            logger.error("===>>>>addOrEditRole error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }




}
