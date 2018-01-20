package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.service.CarTypeService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/cartype")
public class CarTypeAction {

    Logger logger = Logger.getLogger(CarTypeAction.class);

    @Autowired
    private CarTypeService carTypeService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = carTypeService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String name = StringUtils.decodeUTF8(RequestUtil.getString(request, "name"));
        Integer sort = RequestUtil.getInteger(request, "sort", 0);
        Long id = carTypeService.getId();
        String cartypeId = comid+""+new Random().nextInt(1000000);
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(id);
        carTypeTb.setName(name);
        carTypeTb.setSort(sort);
        carTypeTb.setComid(comid);
        carTypeTb.setCreateTime(System.currentTimeMillis()/1000L);
        carTypeTb.setUpdateTime(System.currentTimeMillis()/1000L);
        carTypeTb.setIsDelete(0);
        carTypeTb.setCartypeId(cartypeId);

        JSONObject result = carTypeService.createCarType(carTypeTb);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){

        String name = StringUtils.decodeUTF8(RequestUtil.getString(request, "name"));
        Integer sort = RequestUtil.getInteger(request, "sort", 0);
        Long id = RequestUtil.getLong(request,"id",-1L);

        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(id);
        carTypeTb.setName(name);
        carTypeTb.setSort(sort);

        JSONObject result = carTypeService.updateCarType(carTypeTb);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(id);
        carTypeTb.setIsDelete(1);
        JSONObject result = carTypeService.deleteCarType(carTypeTb);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
