package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.FreeReasonsService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/freereason")
public class FreeReasonsAction {

    Logger logger = Logger.getLogger(FreeReasonsAction.class);

    @Autowired
    private FreeReasonsService freeReasonsService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = freeReasonsService.selectResultByConditions(reqParameterMap);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/add")
    public String addReason(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String name = RequestUtil.getString(request, "name");
        Integer sort = RequestUtil.getInteger(request, "sort", 0);

        JSONObject result = freeReasonsService.createFreeReason(name,sort,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"增加了免费原因"+result.get("id")+name);
            parkLogTb.setType("freereason");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String deleteReason(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long id = RequestUtil.getLong(request, "id", -1L);

        JSONObject result = freeReasonsService.deleteFreeReason(id);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了免费原因"+id);
            parkLogTb.setType("freereason");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        //把结果返回页面


        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/edit")
    public String editReason(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);


        Long id = RequestUtil.getLong(request, "id", -1L);
        String name = RequestUtil.getString(request, "name");
        Integer sort = RequestUtil.getInteger(request, "sort", 0);
        JSONObject result = freeReasonsService.editReason(id,name,sort);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了免费原因"+id);
            parkLogTb.setType("freereason");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
