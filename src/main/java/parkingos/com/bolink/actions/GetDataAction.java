package parkingos.com.bolink.actions;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.GetDataService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/getdata")
public class GetDataAction {

    Logger logger = Logger.getLogger(GetDataAction.class);

    @Autowired
    private GetDataService getDataService;

    /*
    *
    * 获得收费员nickname
    *
    * */
    @RequestMapping(value = "/nickname")
    public String getNicknameById(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request, "id", -1L);
        String result = getDataService.getNicknameById(id);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 获得车辆型号
    *
    * */
    @RequestMapping(value = "/getcartype")
    public String getCarType(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String result = getDataService.getCarType(comid,groupid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 获得总金额   month*price
    *
    * */
    @RequestMapping(value = "/getprodsum")
    public String getprodsum(HttpServletRequest request, HttpServletResponse resp){

        Long prodId = RequestUtil.getLong(request, "p_name", -1L);
        logger.error("=====>>>>+prodId"+prodId);
        Integer months = RequestUtil.getInteger(request, "months", 0);
        logger.error("=====>>>"+months);

        String result = getDataService.getprodsum(prodId,months);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 获得所有月卡套餐
    * */
    @RequestMapping(value = "/getpname")
    public String getpname(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String result = getDataService.getpname(comid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 获得所有的user 收费员
    *
    * */
    @RequestMapping(value = "/getalluser")
    public String getalluser(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String result = getDataService.getalluser(comid,groupid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
    * 获得监控名称
    *
    * */
    @RequestMapping(value = "/getMonitorName")
    public String getMonitorName(HttpServletRequest request, HttpServletResponse resp){
        String comid = RequestUtil.processParams(request, "comid");
        String result = getDataService.getMonitorName(comid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }
    /*
    * 获得通道类型
    *
    * */
    @RequestMapping(value = "/getChannelType")
    public String getChannelType(HttpServletRequest request, HttpServletResponse resp){
        String comid = RequestUtil.processParams(request, "comid");
        String result = getDataService.getChannelType(comid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }
    /*
    * 获得工作站点
    *
    * */
    @RequestMapping(value = "/getWorkSiteId")
    public String getWorkSiteId(HttpServletRequest request, HttpServletResponse resp){
        String comid = RequestUtil.processParams(request, "comid");
        String result = getDataService.getWorkSiteId(comid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }
}
