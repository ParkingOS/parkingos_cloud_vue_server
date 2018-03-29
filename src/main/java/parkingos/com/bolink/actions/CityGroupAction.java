package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CityGroupService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/citygroup")
public class CityGroupAction {

    Logger logger = Logger.getLogger(CityGroupAction.class);

    @Autowired
    private CityGroupService cityGroupService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityGroupService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(Long id, HttpServletResponse resp) {

        JSONObject result = cityGroupService.deleteGroup(id);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/addAndEdit")
    public String addCity(String name,String latitude, String longitude ,String cityid, String operatorid,String address,Long id,HttpServletResponse resp) {
//
//        String company_name = RequestUtil.processParams(request,"company_name");
//        logger.error("接收数据:"+company_name);
        logger.error("接收数据:"+name+address+operatorid+id);

        JSONObject result = cityGroupService.addGroup(name,latitude,longitude,cityid,operatorid,address,id);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

}
