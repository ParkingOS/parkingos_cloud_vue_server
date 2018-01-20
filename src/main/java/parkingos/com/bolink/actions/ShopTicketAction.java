package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.TicketService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/shopticket")
public class ShopTicketAction {
    Logger logger = Logger.getLogger( ShopManageAction.class );
    @Autowired
    private TicketService ticketService;

    @RequestMapping("/quickquery")
    public String addMoney(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );

        JSONObject result = ticketService.selectResultByConditions( reqParameterMap );
        //把结果返回页面
        StringUtils.ajaxOutput( resp, result.toJSONString() );
        return null;
    }
}
