package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.PrepayCardUseTradeService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequestMapping("/prepayuse")
public class PrepayCardUseTradeAction {

    Logger logger = LoggerFactory.getLogger(PrepayCardUseTradeAction.class);


    @Autowired
    private PrepayCardUseTradeService prepayCardUseTradeService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = prepayCardUseTradeService.selectResultByConditions(reqParameterMap );

        StringUtils.ajaxOutput(response, result.toJSONString());
        return null;
    }

}