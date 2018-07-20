package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.VipService;
import parkingos.com.bolink.utils.ExportDataExcel;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
@Api(value = "vip")
@Controller
@RequestMapping("/vip")
public class VipManageAction {

    Logger logger = Logger.getLogger(CarRenewAction.class);


    @Autowired
    private VipService vipService;
    @Autowired
    private SaveLogService saveLogService;

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
        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = vipService.renewProduct(req);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"续费了月卡会员"+StringUtils.decodeUTF8(req.getParameter("card_id")));
            parkLogTb.setType("vip");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());

        return null;
    }

    @ApiOperation(value="获取指定id用户详细信息", notes="根据user的id来获取用户详细信息")
    @ApiImplicitParam(name = "id",value = "用户id", dataType = "String", paramType = "path")
    @RequestMapping(value = "/add")
    /**
     *
     *
     */
    public String add(HttpServletRequest req, HttpServletResponse resp) throws Exception{

        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);
        JSONObject result = vipService.createVip(req);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"新建了月卡会员"+result.get("id")+req.getParameter("name")+",车牌:"+req.getParameter("car_number").toUpperCase());
            parkLogTb.setType("vip");
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
        JSONObject result = vipService.editVip(req);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了月卡会员"+req.getParameter("card_id"));
            parkLogTb.setType("vip");
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

        JSONObject result = vipService.deleteCarowerProById(id,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了月卡会员"+req.getParameter("card_id"));
            parkLogTb.setType("vip");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "editCarNum")
    public String editCarNum(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        String oldCarNumber = StringUtils.decodeUTF8(RequestUtil.getString(request, "old_carnumber"));
        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString(request, "carnumber"));
        logger.error("======>>>>>修改车牌"+carNumber);
        Long id = RequestUtil.getLong(request, "id", -1L);
        JSONObject result = vipService.editCarNum(id,carNumber,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改车牌号码"+oldCarNumber+"为"+carNumber);
            parkLogTb.setType("vip");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<String>> bodyList = vipService.exportExcel(reqParameterMap);
//        String[] heards = new String[]{"编号","包月产品名称","车主手机"/*,"车主账户"*/,"名字","车牌号码","购买时间","开始时间","结束时间","金额","车型类型","单双日限行","备注"};
        String[][] heards = new String[][]{{"编号","STR"},{"包月产品名称","STR"},{"车主手机","STR"}/*,"车主账户"*/,{"名字","STR"},{"车牌号码","STR"},{"购买时间","STR"},{"开始时间","STR"},{"结束时间","STR"},{"金额","STR"},{"车位","STR"},{"车型类型","STR"},{"单双日限行","STR"},{"备注","STR"}};

        ExportDataExcel excel = new ExportDataExcel("会员数据", heards, "sheet1");
        String fname = "会员数据";
        fname = StringUtils.encodingFileName(fname);
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+fname+".xls");
            excel.PoiWriteExcel_To2007(bodyList, os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setOperateUser(nickname);
        parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
        parkLogTb.setOperateType(4);
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了月卡会员");
        parkLogTb.setType("vip");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);

        return null;
    }

}
