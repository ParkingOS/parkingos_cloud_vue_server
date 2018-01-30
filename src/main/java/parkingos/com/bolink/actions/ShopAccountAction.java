package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/shopaccount")
public class ShopAccountAction {

    Logger logger = Logger.getLogger( ShopAccountAction.class );
    @Autowired
    private ShopAcccountService shopAcccountService;

    @RequestMapping("/quickquery")
    public String addMoney(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );
        logger.info( reqParameterMap );
        JSONObject result = shopAcccountService.selectResultByConditions( reqParameterMap );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result.toString() );

        return null;
    }
}
