package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CityUinService;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cityuin")
public class CityUinAction {

    Logger logger = Logger.getLogger(CityUinAction.class);

    @Autowired
    private CityUinService cityUinService;



    @RequestMapping(value = "/create")
    public String addCity(String name,String union_id,String ukey,HttpServletResponse resp) {
        logger.error("接收数据:"+name+ukey+union_id);

        JSONObject result = cityUinService.createCity(name,union_id,ukey);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }
}
