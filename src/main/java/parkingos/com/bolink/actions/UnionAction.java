package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.AdminCityService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.UnionService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/city")
public class UnionAction {

    Logger logger = LoggerFactory.getLogger(UnionAction.class);

    @Autowired
    private UnionService unionService;
    @Autowired
    SaveLogService saveLogService;

    /*
 *
 * 厂商后台获取角色列表  （有分页）
 *
 * */
    @RequestMapping(value = "/getRoles")
    public String getRoles(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {
            Map<String, String> paramMap = RequestUtil.readBodyFormRequset(request);
            logger.info("getRoles by union：" + paramMap);
            result= unionService.getRoles(paramMap);
        }catch (Exception e){
            logger.error("===>>>>getRoles error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /*
 *
 * 厂商后台增加角色
 *
 * */
    @RequestMapping(value = "/addOrEditRole")
    public String addOrEditRole(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        try {

            String nickname = RequestUtil.getString(request, "nickname1");
            Long cityid = RequestUtil.getLong(request, "cityid", -1L);

            Long id = RequestUtil.getLong(request, "id", -1L);
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);
            Long oid = RequestUtil.getLong(request, "oid", -1L);
            String name = RequestUtil.getString(request, "role_name");
            String resume = RequestUtil.getString(request, "resume");
            Integer state = RequestUtil.getInteger(request, "state", 0);
            logger.info("addOrEditRole by server：" + id+"~~"+uin+"~~~"+oid+"~~"+name+"~~"+resume+"~~"+state);
            result= unionService.addOrEditRole(id,uin,oid,name,resume,state);

            if(result.getInteger("state")!=null&&result.getInteger("state")>0){
                ParkLogTb parkLogTb = new ParkLogTb();
                parkLogTb.setOperateUser(nickname);
                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                if(id>0) {
                    parkLogTb.setOperateType(2);
                    parkLogTb.setContent(uin + "(" + nickname + ")" + "修改了角色" + id);
                }else{
                    parkLogTb.setOperateType(1);
                    parkLogTb.setContent(uin + "(" + nickname + ")" + "增加了角色" + name);
                }
                parkLogTb.setType("adminrole");
                parkLogTb.setCityId(cityid);
                saveLogService.saveLog(parkLogTb);
            }

        }catch (Exception e){
            logger.error("===>>>>addOrEditRole error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }



    @RequestMapping(value = "/addMember")
    public String addMember(HttpServletRequest request, HttpServletResponse resp){
        String nickname = RequestUtil.getString(request, "nickname1");
        Long cityid = RequestUtil.getLong(request, "cityid", -1L);
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = unionService.addMember(reqParameterMap);

        if(result.getInteger("state")!=null&&result.getInteger("state")>0){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin + "(" + nickname + ")" + "增加了员工" + reqParameterMap.get("nickname"));
            parkLogTb.setType("member");
            parkLogTb.setCityId(cityid);
            saveLogService.saveLog(parkLogTb);
        }

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
            result = unionService.queryMembers(reqParameterMap);
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
    * 厂商后台查看员工时 该厂商商所有的角色
    *
    * */
    @RequestMapping(value = "/getAllRoles")
    public String getAllRoles(HttpServletRequest request,HttpServletResponse resp) {
        String result = "[]";
        try {
            Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
            result = unionService.getAllRoles(reqParameterMap);
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
            String nickname = RequestUtil.getString(request, "nickname1");
            Long cityid = RequestUtil.getLong(request, "cityid", -1L);
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);

            String name = RequestUtil.getString(request, "nickname");
            String mobile = RequestUtil.getString(request, "mobile");
            String phone = RequestUtil.getString(request, "phone");
            Long roleId = RequestUtil.getLong(request,"role_id",-1L);
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("===>>>>>编辑厂商员工：" + name + "~~" + mobile + "~~" + phone + "~~" + id+"~~"+roleId);
            result = unionService.editMember(name, mobile, phone, id,roleId);

            if(result.getInteger("state")!=null&&result.getInteger("state")>0){
                ParkLogTb parkLogTb = new ParkLogTb();
                parkLogTb.setOperateUser(nickname);
                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                parkLogTb.setOperateType(2);
                parkLogTb.setContent(uin + "(" + nickname + ")" + "编辑了员工" + id);
                parkLogTb.setType("member");
                parkLogTb.setCityId(cityid);
                saveLogService.saveLog(parkLogTb);
            }

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
            String nickname = RequestUtil.getString(request, "nickname1");
            Long cityid = RequestUtil.getLong(request, "cityid", -1L);
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);

            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("===>>>>>删除厂商员工：" + id);
            result = unionService.deleteMember(id);

            if(result.getInteger("state")!=null&&result.getInteger("state")>0){
                ParkLogTb parkLogTb = new ParkLogTb();
                parkLogTb.setOperateUser(nickname);
                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                parkLogTb.setOperateType(3);
                parkLogTb.setContent(uin + "(" + nickname + ")" + "删除了员工" + id);
                parkLogTb.setType("member");
                parkLogTb.setCityId(cityid);
                saveLogService.saveLog(parkLogTb);
            }

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
            String nickname = RequestUtil.getString(request, "nickname1");
            Long cityid = RequestUtil.getLong(request, "cityid", -1L);
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);

            String newPass = RequestUtil.getString(request, "newpass");
            String confirmPass = RequestUtil.getString(request, "confirmpass");
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("===>>>>>编辑员工密码：" + id);
            result = unionService.editMemberPass(id, newPass, confirmPass);

            if(result.getInteger("state")!=null&&result.getInteger("state")>0){
                ParkLogTb parkLogTb = new ParkLogTb();
                parkLogTb.setOperateUser(nickname);
                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                parkLogTb.setOperateType(2);
                parkLogTb.setContent(uin + "(" + nickname + ")" + "编辑了员工密码" + id);
                parkLogTb.setType("member");
                parkLogTb.setCityId(cityid);
                saveLogService.saveLog(parkLogTb);
            }

        }catch (Exception e){
            logger.error("===>>>>editMemberPass error",e);
            result.put("state",0);
            result.put("msg","系统异常");
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;

    }



}
