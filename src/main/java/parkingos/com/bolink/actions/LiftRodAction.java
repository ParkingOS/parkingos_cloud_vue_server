package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.LiftRodService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/liftRod")
public class LiftRodAction {

    Logger logger = Logger.getLogger(LiftRodAction.class);

    @Autowired
    private LiftRodService liftRodService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {
        logger.error("======================");
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = liftRodService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/getLiftRodPicture")
    public String getLiftRodPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String comid = RequestUtil.getString(request, "comid");
        String liftrodId = RequestUtil.getString(request, "liftrodid");

        logger.error("==========>>>>获取图片"+liftrodId);
        logger.error("==========>>>>获取图片"+comid);

        byte[] content = liftRodService.getLiftRodPicture(comid, liftrodId);

        if (content.length == 0) {
            //测试用  之后读配置文件
            response.sendRedirect("http://192.168.199.205:12305/images/nopic.jpg");
            return null;
        } else {
            response.setDateHeader("Expires", System.currentTimeMillis() + 12 * 60 * 60 * 1000);
            response.setContentLength(content.length);
            response.setContentType("image/jpeg");
            OutputStream o = response.getOutputStream();
            o.write(content);
            o.flush();
            o.close();
        }
        return null;
    }


}
