package parkingos.com.bolink.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.GetParkInfoService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/getparkinfo")
public class GetParkInfoAction {
    @Autowired
    private GetParkInfoService getParkInfoService;
    Logger logger = LoggerFactory.getLogger(GetParkInfoAction.class);
    @RequestMapping(value = "/bygroupid")
    public String getInfoById(HttpServletRequest request, HttpServletResponse resp){
        int groupid = RequestUtil.getInteger(request,"groupid",0);
        logger.debug("数据中心请求groupId:"+groupid);
        String  ret=getParkInfoService.getInfo(groupid);
        logger.debug("数据中心返回："+ret);
        StringUtils.ajaxOutput(resp,ret);
        return null;
    }
    @RequestMapping(value = "/bycomid")
    public String getInfoBycomId(HttpServletRequest request, HttpServletResponse resp){
        logger.info("数据中心请求comid"+request.getParameter("comid"));
        int comid = RequestUtil.getInteger(request,"comid",0);
        String ret =getParkInfoService.getInfoByComid(comid);
        StringUtils.ajaxOutput(resp,ret);
        return null;
    }
}
