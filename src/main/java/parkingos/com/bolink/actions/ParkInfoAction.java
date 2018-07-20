package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.ParkInfoService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/park")
public class ParkInfoAction {

    Logger logger = Logger.getLogger(ParkInfoAction.class);

    @Autowired
    private ParkInfoService parkInfoService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String result = parkInfoService.getResultByComid(comid);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        JSONObject result = parkInfoService.updateComInfo(request);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname1);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname1+")"+"修改了停车场信息");
            parkLogTb.setType("parkinfo");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
