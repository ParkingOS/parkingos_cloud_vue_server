package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CarTypeService;
import parkingos.com.bolink.service.SaveLogService;
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
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = carTypeService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/add")
//    public String add(sting, HttpServletResponse resp){
    public String add(HttpServletRequest request, HttpServletResponse resp){
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String name =RequestUtil.getString(request, "name");
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

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"增加了车型"+id+name);
            parkLogTb.setType("cartype");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long comid = RequestUtil.getLong(request,"comid",-1L);

        String name =RequestUtil.getString(request, "name");
        Integer sort = RequestUtil.getInteger(request, "sort", 0);
        Long id = RequestUtil.getLong(request,"id",-1L);

        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(id);
        carTypeTb.setName(name);
        carTypeTb.setSort(sort);

        JSONObject result = carTypeService.updateCarType(carTypeTb);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了车型"+id+name);
            parkLogTb.setType("cartype");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp){
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));

        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        Long id = RequestUtil.getLong(request,"id",-1L);
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(id);
        carTypeTb.setIsDelete(1);
        JSONObject result = carTypeService.deleteCarType(carTypeTb);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了车型"+id);
            parkLogTb.setType("cartype");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
