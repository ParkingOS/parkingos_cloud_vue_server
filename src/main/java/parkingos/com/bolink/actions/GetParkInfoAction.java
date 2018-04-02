package parkingos.com.bolink.actions;

import com.mongodb.util.Hash;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.GetParkInfoService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/getparkinfo")
public class GetParkInfoAction {
    @Autowired
    private GetParkInfoService getParkInfoService;
    Logger logger = Logger.getLogger(GetParkInfoAction.class);
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
        int comid = RequestUtil.getInteger(request,"comid",0);
        logger.debug("数据中心请求groupId:"+comid);
        String ret =getParkInfoService.getInfoByComid(comid);
        logger.debug("数据中心返回:"+ret);
        StringUtils.ajaxOutput(resp,ret);
        return null;
    }
}
