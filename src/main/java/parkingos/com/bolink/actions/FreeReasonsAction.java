package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.FreeReasonsService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/freereason")
public class FreeReasonsAction {

    Logger logger = Logger.getLogger(FreeReasonsAction.class);

    @Autowired
    private FreeReasonsService freeReasonsService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = freeReasonsService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/add")
    public String addReason(HttpServletRequest request, HttpServletResponse resp){

        String name = RequestUtil.getString(request, "name");
        Integer sort = RequestUtil.getInteger(request, "sort", 0);
        Long comid = RequestUtil.getLong(request,"comid",-1L);

        JSONObject result = freeReasonsService.createFreeReason(name,sort,comid);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String deleteReason(HttpServletRequest request, HttpServletResponse resp){

        Long id = RequestUtil.getLong(request, "id", -1L);

        JSONObject result = freeReasonsService.deleteFreeReason(id);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/edit")
    public String editReason(HttpServletRequest request, HttpServletResponse resp){

        Long id = RequestUtil.getLong(request, "id", -1L);
        String name = RequestUtil.getString(request, "name");
        Integer sort = RequestUtil.getInteger(request, "sort", 0);
        JSONObject result = freeReasonsService.editReason(id,name,sort);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
