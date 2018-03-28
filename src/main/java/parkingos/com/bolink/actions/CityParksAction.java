package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.service.CityParkService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/cityparks")
public class CityParksAction {

    Logger logger = Logger.getLogger(CityParksAction.class);

    @Autowired
    private CityParkService cityParkService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityParkService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    /*
    * 创建和修改车场接口
    *
    * */
    @RequestMapping(value = "/editpark")
    public String addpark(HttpServletRequest request, HttpServletResponse resp) {
//        System.out.println("创建车场:"+comInfoTb);

        System.out.println("进入创建车场");
        JSONObject result = cityParkService.createPark(request);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

//    @RequestMapping(value = "/editpark")
//    public String editpark(ComInfoTb comInfoTb, HttpServletResponse resp) {
//        System.out.println("修改车场:"+comInfoTb);
//        JSONObject result = cityParkService.editpark(comInfoTb);
//        //把结果返回页面
//        StringUtils.ajaxOutput(resp, result.toJSONString());
//        return null;
//    }

    @RequestMapping(value = "/deletepark")
    public String deletepark(HttpServletRequest request, HttpServletResponse resp) {
        Long id = Long.parseLong(request.getParameter("id"));
        System.out.println("删除车场id:"+id);
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(id);
        comInfoTb.setState(1);
        JSONObject result = null;
        if(id!=null){
            result = cityParkService.deletepark(comInfoTb);
        }
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/setpark")
    public String setpark(HttpServletRequest request, HttpServletResponse resp) {
        Long comid = RequestUtil.getLong(request, "id", -1L);

        System.out.println("设置车场:"+comid);
        JSONObject result = cityParkService.setpark(comid);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }
}
