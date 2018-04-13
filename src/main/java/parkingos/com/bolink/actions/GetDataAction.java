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
    * 获得车场所有月卡套餐
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

    @RequestMapping(value = "/getallunion")
    public String getAllUnion(HttpServletRequest request, HttpServletResponse resp){
        Long cityid = RequestUtil.getLong(request, "cityid", -1L);
        String result = getDataService.getAllUnion(cityid);
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
    * 获得集团下面所有的通道
    *
    */
    @RequestMapping(value = "/getgroupchannels")
    public String getGroupChannelTypes(HttpServletRequest request, HttpServletResponse resp){
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String result = getDataService.getGroupChannelTypes(groupid);
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

    /*
    *
    * 获得集团或者城市下面所有车场
    * */
    @RequestMapping(value = "/cityparks")
    public String getAllParks(HttpServletRequest request, HttpServletResponse resp){
        String groupid = request.getParameter("groupid");
        String cityid = request.getParameter("cityid");
        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);
        String result = getDataService.getAllParks(groupid,cityid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
   *
   * 获得集团或者城市下面所有 收费员
   * */
    @RequestMapping(value = "/allcollectors")
    public String getAllCollectors(HttpServletRequest request, HttpServletResponse resp){
        String groupid = request.getParameter("groupid");
        String cityid = request.getParameter("cityid");
        System.out.println("=====allcollectors.groupid:"+groupid+"===cityid:"+cityid);
        String result = getDataService.getAllCollectors(groupid,cityid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    /*
  *
  * 获得集团或者城市下面所有 套餐
  * */
    @RequestMapping(value = "/getAllPackage")
    public String getAllPackage(HttpServletRequest request, HttpServletResponse resp){
        String groupid = request.getParameter("groupid");
        String cityid = request.getParameter("cityid");
        System.out.println("=====groupid:"+groupid+"======cityid:"+cityid);
        String result = getDataService.getAllPackage(groupid,cityid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


    /*
*
* 获得这个车场是不是支持叠加用券
* */
    @RequestMapping(value = "/getSuperimposed")
    public String getSuperimposed(HttpServletRequest request, HttpServletResponse resp){
        String comid = request.getParameter("comid");
        if(comid==null||"".equals(comid)){
            return null;
        }
        System.out.println("是不是支持叠加用券=====comid:"+comid);
        String result = getDataService.getSuperimposed(comid);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }
}
