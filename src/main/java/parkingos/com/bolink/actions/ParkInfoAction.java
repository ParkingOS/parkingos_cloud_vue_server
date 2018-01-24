package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ParkInfoService;
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

        JSONObject result = parkInfoService.updateComInfo(request);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
