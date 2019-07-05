package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.CameraTb;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.service.CameraManageService;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cameraManage")
public class CameraManageAction {

    Logger logger = LoggerFactory.getLogger(CameraManageAction.class);

    @Autowired
    CameraManageService cameraManageService;
    @Autowired
    OrmUtil ormUtil;
    @Autowired
    RedisService redisService;
    @Autowired
    CommonService commonService;


    /*
    *
    * 总后台查询 相机列表
    *
    * */
    @RequestMapping(value = "/adminQuery")
    public String adminQuery(HttpServletRequest request,HttpServletResponse resp) {
        Integer pageNumber = RequestUtil.getInteger(request,"page",1);
        Integer pageSize = RequestUtil.getInteger(request,"rp",20);

        Long cityid = RequestUtil.getLong(request,"cityid",-1L);
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        PageHelper.startPage(pageNumber, pageSize);
        List<CameraTb> list= cameraManageService.adminManage(cityid,comid);
        PageInfo<CameraTb> ret = new PageInfo<>(list);

        //只有把pageInfo 放入vo对象或者里面  list才能显示。然后再获取list集合。

        JSONObject result = new JSONObject();
        result.put("rows",ret);
        result.put("total",ret.getTotal());
        result.put("page",pageNumber);

        List<Map<String,Object>> mapList = new ArrayList<>();
        if(ret.getSize()>0) {
            List<CameraTb> cameraList = JSON.parseArray(result.getJSONObject("rows").get("list").toString(),CameraTb.class);
            for(CameraTb cameraTb:cameraList){
                Map<String,Object> map = ormUtil.pojoToMap(cameraTb);
                String camId = cameraTb.getCamId();
                Object object =redisService.get("dobeat_" + camId);
                if(object!=null){
                    map.put("beat_time",object);
                }
                Long cityId = cameraTb.getCityid();
                Long parkId = cameraTb.getComid();
                OrgCityMerchants city = commonService.getOrgCityById(cityId);
                if(city!=null){
                    map.put("city_name",city.getName());
                }else{
                    map.put("city_name","");
                }
                ComInfoTb comInfoTb = commonService.getComInfoByComid(parkId);
                if(comInfoTb!=null){
                    map.put("park_name",comInfoTb.getCompanyName());
                }else{
                    map.put("park_name","");
                }
                mapList.add(map);
            }
        }

        result.put("rows",mapList);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toString());
        return null;
    }

    /*
    *
    * 绑定车场
    * */
    @RequestMapping(value = "/bindPark")
    public String bindPark(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result;
        try {
            Long comid = RequestUtil.getLong(request, "comid", -1L);
            Long cityid = RequestUtil.getLong(request, "cityid", -1L);
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("==>>>>>相机绑定车场：" + id + "~~~" + cityid + "~~" + comid);
            result= cameraManageService.bindPark(comid, cityid, id);
        }catch (Exception e){
            logger.error("===>>>>bind park error",e);
            result = new JSONObject();
            result.put("state",0);
            result.put("errmsg","系统异常");
        }
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }



    /*
        *
        * 车场后台查询 相机列表
        *
        * */
    @RequestMapping(value = "/parkQuery")
    public String parkQuery(HttpServletRequest request,HttpServletResponse resp) {
        Integer pageNumber = RequestUtil.getInteger(request,"page",1);
        Integer pageSize = RequestUtil.getInteger(request,"rp",20);
        String camName = RequestUtil.getString(request,"cam_name");
        String camId = RequestUtil.getString(request,"cam_id");
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        PageHelper.startPage(pageNumber, pageSize);
        List<CameraTb> list= cameraManageService.parkQuery(comid,camName,camId);
        PageInfo<CameraTb> ret = new PageInfo<>(list);

        //只有把pageInfo 放入vo对象或者里面  list才能显示。然后再获取list集合。

        JSONObject result = new JSONObject();
        result.put("rows",ret);
        result.put("total",ret.getTotal());
        result.put("page",pageNumber);

        List<Map<String,Object>> mapList = new ArrayList<>();
        if(ret.getSize()>0) {
            List<CameraTb> cameraList = JSON.parseArray(result.getJSONObject("rows").get("list").toString(),CameraTb.class);
            for(CameraTb cameraTb:cameraList){
                Map<String,Object> map = ormUtil.pojoToMap(cameraTb);
                String camIdQuery = cameraTb.getCamId();
                Object object =redisService.get("dobeat_" + camIdQuery);
                if(object!=null){
                    map.put("beat_time",object);
                }
                mapList.add(map);
            }
        }

        result.put("rows",mapList);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toString());
        return null;
    }




    /*
    *
    * 车场后台修改相机名称
    * */
    @RequestMapping(value = "/editName")
    public String editName(HttpServletRequest request,HttpServletResponse resp) {
        JSONObject result;
        try {
            String camName = RequestUtil.getString(request,"cam_name");
            Long id = RequestUtil.getLong(request, "id", -1L);
            logger.info("==>>>>>相机编辑名称：" + id + "~~~" + camName);
            result= cameraManageService.editName(id, camName);
        }catch (Exception e){
            logger.error("===>>>>bind park error",e);
            result = new JSONObject();
            result.put("state",0);
            result.put("errmsg","系统异常");
        }
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }



}
