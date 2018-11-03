package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.service.BlackUserService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/blackuser")
public class BlackUserAction {

    Logger logger = LoggerFactory.getLogger(BlackUserAction.class);

    @Autowired
    private BlackUserService blackUserService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = blackUserService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp) {

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long id = RequestUtil.getLong(request, "id", -1L);
        String remark = RequestUtil.getString(request, "remark");
        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        Long ntime = System.currentTimeMillis() / 1000;
        Integer state = RequestUtil.getInteger(request, "state", 0);
        String operator = RequestUtil.getString(request, "operator");
        if ("".equals(operator)) {
            operator = RequestUtil.getString(request, "loginuin");
        }
        ZldBlackTb zldBlackTb = new ZldBlackTb();
        zldBlackTb.setId(id);
        zldBlackTb.setState(state);
        zldBlackTb.setRemark(remark);
        zldBlackTb.setCarNumber(carNumber);
        zldBlackTb.setUtime(ntime);
        zldBlackTb.setOperator(operator);
        JSONObject result = blackUserService.editBlackUser(zldBlackTb);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了黑名单"+id);
            parkLogTb.setType("blackuser");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
//        把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp) {

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long id = RequestUtil.getLong(request, "id", -1L);

        ZldBlackTb zldBlackTb = new ZldBlackTb();
        zldBlackTb.setId(id);
        zldBlackTb.setState(1);

        JSONObject result = blackUserService.deleteBlackUser(zldBlackTb);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了黑名单"+id);
            parkLogTb.setType("blackuser");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp) {

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String remark = RequestUtil.getString(request, "remark");
        String carNumber =  RequestUtil.getString(request, "car_number");
        String operator = RequestUtil.getString(request, "operator");
        if("".equals(operator)){
            operator = RequestUtil.getString(request,"loginuin");
        }
        Long ntime = System.currentTimeMillis()/1000;
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String blackUUID = comid+"_"+new Random().nextInt(1000000);
        Long nextid = blackUserService.getId();

        ZldBlackTb zldBlackTb =new ZldBlackTb();
        zldBlackTb.setId(nextid);
        zldBlackTb.setComid(comid);
        zldBlackTb.setRemark(remark);
        zldBlackTb.setCarNumber(carNumber);
        zldBlackTb.setOperator(operator);
        zldBlackTb.setCtime(ntime);
        zldBlackTb.setBlackUuid(blackUUID);
        zldBlackTb.setUtime(ntime);
        zldBlackTb.setState(0);
        zldBlackTb.setUin(-1L);

        JSONObject result = blackUserService.createBlackUser(zldBlackTb);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"增加了黑名单"+nextid+carNumber);
            parkLogTb.setType("blackuser");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

}
