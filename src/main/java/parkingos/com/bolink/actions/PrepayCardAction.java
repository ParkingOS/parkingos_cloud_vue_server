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
@RequestMapping("/prepaycard")
public class PrepayCardAction {

    Logger logger = LoggerFactory.getLogger(PrepayCardAction.class);

    @Autowired
    private PrepayCardService prepayCardService;
    @Autowired
    private SaveLogService saveLogService;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = prepayCardService.selectResultByConditions(reqParameterMap);
        StringUtils.ajaxOutput(response,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "renewproduct")
    public String renewproduct(HttpServletRequest req, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = prepayCardService.renewProduct(req);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"续费了储值卡"+StringUtils.decodeUTF8(req.getParameter("card_id")));
            parkLogTb.setType("prepaycard");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest req, HttpServletResponse resp) throws Exception{

        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = prepayCardService.createPrepayCard(req);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"新建了储值卡"+result.get("id")+req.getParameter("name")+",车牌:"+req.getParameter("car_number").toUpperCase());
            parkLogTb.setType("prepaycard");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }


    @RequestMapping(value = "edit")
    public String edit(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = prepayCardService.editCard(req);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了储值卡"+req.getParameter("card_id"));
            parkLogTb.setType("prepaycard");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(HttpServletRequest req, HttpServletResponse resp){

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        Long id = RequestUtil.getLong(req, "id", -1L);
        Long comid = RequestUtil.getLong(req,"comid",-1L);

        JSONObject result = prepayCardService.deleteCard(id,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了储值卡"+req.getParameter("card_id"));
            parkLogTb.setType("prepaycard");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


}
