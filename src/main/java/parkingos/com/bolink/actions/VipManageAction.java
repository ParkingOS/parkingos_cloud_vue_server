package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.service.VipService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/vip")
public class VipManageAction {

    Logger logger = Logger.getLogger(CarRenewAction.class);


    @Autowired
    private VipService vipService;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = vipService.selectResultByConditions(reqParameterMap);

        StringUtils.ajaxOutput(response,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "renewproduct")
    public String renewproduct(HttpServletRequest req, HttpServletResponse resp){

        JSONObject result = vipService.renewProduct(req);

        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }


    @RequestMapping(value = "add")
    public String add(HttpServletRequest req, HttpServletResponse resp){

        JSONObject result = vipService.createVip(req);

        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(HttpServletRequest req, HttpServletResponse resp){
        Long id = RequestUtil.getLong(req, "id", -1L);
        Long comid = RequestUtil.getLong(req,"comid",-1L);

        JSONObject result = vipService.deleteCarowerProById(id,comid);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "editCarNum")
    public String editCarNum(HttpServletRequest request, HttpServletResponse resp){
        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString(request, "carnumber"));
        logger.error("======>>>>>修改车牌"+carNumber);
        Long id = RequestUtil.getLong(request, "id", -1L);
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        JSONObject result = vipService.editCarNum(id,carNumber,comid);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
