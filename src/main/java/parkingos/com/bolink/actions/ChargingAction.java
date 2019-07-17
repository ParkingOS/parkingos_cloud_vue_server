package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkChargingTb;
import parkingos.com.bolink.service.ChargingService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/charging")
public class ChargingAction {

    Logger logger = LoggerFactory.getLogger(ChargingAction.class);

    @Autowired
    private ChargingService chargingService;
    @Autowired
    private SupperSearchService supperSearchService;

    @RequestMapping("add")
    public String add(HttpServletRequest request, HttpServletResponse resp){
        String name = RequestUtil.getString(request,"name");
        String remark = RequestUtil.getString(request,"remark");
        String js = RequestUtil.getString(request,"js_content");
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        JSONObject result = chargingService.addJs(name,remark,js,comid);
        StringUtils.ajaxOutput(resp,result.toString());
        return null;
    }

    @RequestMapping("query")
    public String query(HttpServletRequest request, HttpServletResponse resp){
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        ParkChargingTb parkChargingTb = new ParkChargingTb();
        parkChargingTb.setParkId(comid);
        parkChargingTb.setState(0);
        JSONObject result = supperSearchService.supperSearch(parkChargingTb,reqParameterMap);
        StringUtils.ajaxOutput(resp,result.toString());
        return null;
    }

    @RequestMapping("edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){

        Long id = RequestUtil.getLong(request,"id",-1L);
        String name = RequestUtil.getString(request,"name");
        String remark = RequestUtil.getString(request,"remark");
        String jsContent = RequestUtil.getString(request,"js_content");

        JSONObject result = chargingService.editParkCharging(id,name,remark,jsContent);
        StringUtils.ajaxOutput(resp,result.toString());
        return null;
    }

    @RequestMapping("delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp){

        Long id = RequestUtil.getLong(request,"id",-1L);

        JSONObject result = chargingService.deleteParkCharging(id);
        StringUtils.ajaxOutput(resp,result.toString());
        return null;
    }


    @RequestMapping("testjs")
    public String testJs(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);
        Long createTime = RequestUtil.getLong(request,"begin_time",-1L);
        Long endTime = RequestUtil.getLong(request,"end_time",-1L);
        String result = chargingService.testJs(id,createTime,endTime);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


    @RequestMapping("openorclose")
    public String openOrClose(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);
        Integer isOpen = RequestUtil.getInteger(request,"is_open",0);
        JSONObject result = chargingService.openOrClose(id,isOpen);
        StringUtils.ajaxOutput(resp,result.toString());
        return null;
    }

    @RequestMapping("cloudfirst")
    public String cloudFirst(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        Integer cloudFirst = RequestUtil.getInteger(request,"cloud_first",0);
        logger.info("====平台优先:"+comid+"~~"+cloudFirst);
        JSONObject result = chargingService.cloudFirst(comid,cloudFirst);
        StringUtils.ajaxOutput(resp,result.toString());
        return null;
    }

    @RequestMapping("getfirstornot")
    public String getFirstOrNot(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        logger.info("====getfirstornot:"+comid);
        int result = chargingService.getFirstOrNot(comid);
        StringUtils.ajaxOutput(resp,result+"");
        return null;
    }

}
