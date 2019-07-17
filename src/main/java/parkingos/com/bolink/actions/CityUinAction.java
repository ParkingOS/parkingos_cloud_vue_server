package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CityUinService;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cityuin")
public class CityUinAction {

    Logger logger = LoggerFactory.getLogger(CityUinAction.class);

    @Autowired
    private CityUinService cityUinService;



    @RequestMapping(value = "/create")
    public String addCity(String name,String union_id,String ukey,HttpServletResponse resp) {
        logger.error("接收数据:"+name+ukey+union_id);

        JSONObject result = cityUinService.createCity(name,union_id,ukey);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    /*
    * 自定义设置，可以修改月卡续费时间
    * type  修改开始时间和结束时间
    *
    * */
    @RequestMapping(value = "/edit")
    public String editSetting(Long cityid,Integer state,Integer type,HttpServletResponse resp) {
        logger.error("自定义设置"+cityid+"~~~~~"+state+"~~type:"+type);

        JSONObject result = cityUinService.editSetting(cityid,state,type);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    /*
    * 查询自定义设置
    *
    * */
    @RequestMapping(value = "/query")
    public String querySetting(Long cityid,HttpServletResponse resp) {
        logger.error("查询自定义设置"+cityid);
        JSONObject result = cityUinService.querySetting(cityid);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    /*
   * 个性化设置里面 编辑权限
   * 可以厂商定义自己的车场管理员的权限
   *
   * */
    @RequestMapping(value = "/getSingleAuth")
    public String getSingleAuth(Long cityid,HttpServletResponse resp) {
        logger.info("获取自定义权限："+cityid);
        String result = cityUinService.getSingleAuth(cityid);
        StringUtils.ajaxOutput(resp, result);
        return null;
    }


    /*
  * 个性化设置里面 编辑权限
  * 可以厂商定义自己的车场管理员的权限
  *
  * */
    @RequestMapping(value = "/setSingleAuth")
    public String setSingleAuth(Long cityid,Long roleId,String auths,HttpServletResponse resp) {
        logger.info("设置自定义权限："+cityid+"~~~roleId:"+roleId);
        JSONObject result = cityUinService.setSingleAuth(cityid,roleId,auths);
        StringUtils.ajaxOutput(resp, result.toString());
        return null;
    }


}
