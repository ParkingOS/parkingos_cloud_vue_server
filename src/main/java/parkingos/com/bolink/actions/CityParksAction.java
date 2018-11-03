package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CityParkService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/cityparks")
public class CityParksAction {

    Logger logger = LoggerFactory.getLogger(CityParksAction.class);

    @Autowired
    private CityParkService cityParkService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityParkService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    /*
    * 创建和修改车场接口
    *
    * */
    @RequestMapping(value = "/editpark")
    public String addpark(HttpServletRequest request, HttpServletResponse resp) {

        JSONObject result = cityParkService.createPark(request);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

//    @RequestMapping(value = "/editpark")
//    public String editpark(ComInfoTb comInfoTb, HttpServletResponse resp) {
//        System.out.println("修改车场:"+comInfoTb);
//        JSONObject result = cityParkService.editpark(comInfoTb);
//        //把结果返回页面
//        StringUtils.ajaxOutput(resp, result.toJSONString());
//        return null;
//    }

    @RequestMapping(value = "/deletepark")
    public String deletepark(HttpServletRequest request, HttpServletResponse resp) {

        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long id = Long.parseLong(request.getParameter("id"));
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(id);
        comInfoTb.setState(1);
        JSONObject result = null;
        if(id!=null){
            result = cityParkService.deletepark(comInfoTb);
        }

        if((Integer)result.get("state")==1&&groupid>0){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了车场"+id);
            parkLogTb.setType("parkinfo");
            parkLogTb.setGroupId(groupid);
            saveLogService.saveLog(parkLogTb);
        }
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/setpark")
    public String setpark(HttpServletRequest request, HttpServletResponse resp) {
        Long comid = RequestUtil.getLong(request, "id", -1L);


        JSONObject result = cityParkService.setpark(comid);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    /*
    *
    * 厂商可以重置车场订单和抬杆数据
    *
    * */
    @RequestMapping(value = "/resetdata")
    public String resetdata(HttpServletRequest request, HttpServletResponse resp) {
        Long comid = RequestUtil.getLong(request, "id", -1L);
        Long loginuin = RequestUtil.getLong(request,"loginuin",-1L);
        String password = RequestUtil.getString(request,"password");
        JSONObject result = cityParkService.resetParkData(comid,loginuin,password);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }
}
