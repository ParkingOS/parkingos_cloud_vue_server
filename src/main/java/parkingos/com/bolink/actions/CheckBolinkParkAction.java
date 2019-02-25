package parkingos.com.bolink.actions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CheckBolinkParkService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/checkpark")
public class CheckBolinkParkAction {

    Logger logger = LoggerFactory.getLogger(CheckBolinkParkAction.class);

    @Autowired
    private CheckBolinkParkService checkBOlinkParkService;

    @RequestMapping(value = "/docheck")
    public String doCheck(HttpServletRequest request, HttpServletResponse resp){

        String unionId  = RequestUtil.getString(request,"union_id");
        String parkId = RequestUtil.getString(request,"park_id");

        logger.info("===>>>>泊链来云平台校验车场接口:"+unionId+"~~~"+parkId);

        String result = checkBOlinkParkService.checkPark(unionId,parkId);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


}
