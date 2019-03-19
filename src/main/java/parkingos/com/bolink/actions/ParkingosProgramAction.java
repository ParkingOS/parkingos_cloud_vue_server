package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ParkingosProgramService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/parkingos")
public class ParkingosProgramAction {

    Logger logger = LoggerFactory.getLogger(ParkingosProgramAction.class);
    @Autowired
    private ParkingosProgramService parkingosProgramService;

    @RequestMapping(value = "/getdata")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        Long userId= RequestUtil.getLong(request,"user_id",-1L);
        Integer isManager = RequestUtil.getInteger(request,"is_manager",0);

        logger.info("===>>>>小程序获取日报和收费员统计:"+comid+"~~"+userId+"~~"+isManager);
        JSONObject result = parkingosProgramService.getMoneyData(comid,userId,isManager);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
