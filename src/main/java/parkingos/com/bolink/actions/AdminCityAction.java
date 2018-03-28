package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.AdminCityService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminCityAction {

    Logger logger = Logger.getLogger(AdminCityAction.class);

    @Autowired
    private AdminCityService adminCityService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = adminCityService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(Long id, HttpServletResponse resp) {


        JSONObject result = adminCityService.deleteCity(id);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/addAndEdit")
    public String addCity(String name,String union_id,String ukey,Long id,HttpServletResponse resp) {
        logger.error("接收数据:"+name+ukey+union_id+id);

        JSONObject result = adminCityService.addCity(name,union_id,ukey,id);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

}
