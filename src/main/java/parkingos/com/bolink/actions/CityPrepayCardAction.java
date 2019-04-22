package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.PrepayCardService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequestMapping("/cityprepaycard")
public class CityPrepayCardAction {

    Logger logger = LoggerFactory.getLogger(CityPrepayCardAction.class);

    @Autowired
    private PrepayCardService prepayCardService;
    @Autowired
    private SaveLogService saveLogService;


    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = prepayCardService.groupQuery(reqParameterMap);
        StringUtils.ajaxOutput(response,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "renewproduct")
    public String renewproduct(HttpServletRequest req, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(req,"park_id",-1L);
        Long groupId = RequestUtil.getLong(req,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = prepayCardService.renewProduct(req,comid);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"续费了"+comid+"车场的储值卡"+StringUtils.decodeUTF8(req.getParameter("card_id")));
            parkLogTb.setType("prepaycard");
            parkLogTb.setGroupId(groupId);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest req, HttpServletResponse resp) throws Exception{

        Long comid = RequestUtil.getLong(req,"park_id",-1L);
        Long groupId = RequestUtil.getLong(req,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = prepayCardService.createPrepayCard(req,comid,groupId);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            if(comid>-1) {
                parkLogTb.setContent(uin + "(" + nickname + ")" + "新建了" + comid + "车场的储值卡"  + req.getParameter("name") + ",车牌:" + req.getParameter("car_number").toUpperCase());
            }else{
                parkLogTb.setContent(uin + "(" + nickname + ")" + "新建了所有车场的储值卡"  + req.getParameter("name") + ",车牌:" + req.getParameter("car_number").toUpperCase());
            }
            parkLogTb.setType("prepaycard");
            parkLogTb.setGroupId(groupId);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }


    @RequestMapping(value = "edit")
    public String edit(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        Long groupId= RequestUtil.getLong(req,"groupid",-1L);
        Long comid = RequestUtil.getLong(req,"park_id",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = prepayCardService.editCard(req,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了"+comid+"车场的储值卡"+req.getParameter("card_id"));
            parkLogTb.setType("prepaycard");
            parkLogTb.setGroupId(groupId);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(HttpServletRequest req, HttpServletResponse resp){

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        Long groupId = RequestUtil.getLong(req,"groupid",-1L);

        Long id = RequestUtil.getLong(req, "id", -1L);
        Long comid = RequestUtil.getLong(req,"park_id",-1L);

        JSONObject result = prepayCardService.deleteCard(id,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了"+comid+"车场的储值卡"+req.getParameter("card_id"));
            parkLogTb.setType("prepaycard");
            parkLogTb.setGroupId(groupId);
//            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


}
