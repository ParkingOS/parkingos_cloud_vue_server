package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CityGroupService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/citygroup")
public class CityGroupAction {

    Logger logger = LoggerFactory.getLogger(CityGroupAction.class);

    @Autowired
    private CityGroupService cityGroupService;
    @Autowired
    SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityGroupService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(Long id, HttpServletResponse resp,String nickname1,Long loginuin,Long cityid) {
        logger.info("===>>>>>删除运营集团："+id+"~~"+nickname1+"~~"+loginuin+"~~"+cityid);
        JSONObject result = cityGroupService.deleteGroup(id);
        //把结果返回页面
        if(result.getInteger("state")==1&&cityid>0){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname1);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(loginuin+"("+nickname1+")"+"删除了集团"+id);
            parkLogTb.setType("group");
            parkLogTb.setCityId(cityid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/addAndEdit")
    public String addCity(String name, String nickname1,Long loginuin,String cityid, String operatorid,String address,Long id,Long serverid,HttpServletResponse resp) {
        logger.info("addAndEdit 运营集团接收数据:"+name+address+operatorid+id +"~~cityid:"+cityid+"~~serverId:"+serverid);

        JSONObject result = cityGroupService.addGroup(name,cityid,operatorid,id,serverid);
        if(result.getInteger("state")==1&&!Check.isEmpty(cityid)){
            //厂商注册和编辑
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname1);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            if(id>0){
                parkLogTb.setOperateType(2);
                parkLogTb.setContent(loginuin+"("+nickname1+")"+"编辑了集团"+id);
            }else{
                parkLogTb.setOperateType(1);
                parkLogTb.setContent(loginuin+"("+nickname1+")"+"注册了集团:"+name);
            }
            parkLogTb.setType("group");
            parkLogTb.setCityId(Long.parseLong(cityid));
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

}
