package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.HomeOwnerTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.HomeOwnerService;
import parkingos.com.bolink.service.SaveLogService;
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

@Controller
@RequestMapping("/homeowner")
public class HomeOwnerAction {

    Logger logger = Logger.getLogger(HomeOwnerAction.class);

    @Autowired
    private HomeOwnerService ownerService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = ownerService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

//    @RequestMapping(value = "/delete")
//    public String delete(Long id, HttpServletResponse resp) {
//
//
//        JSONObject result = ownerService.deleteOwner(id);
//        //把结果返回页面
//        StringUtils.ajaxOutput(resp, result.toJSONString());
//        return null;
//    }


//    @RequestMapping(value = "/edit")
//    public String edit(HttpServletRequest request, HttpServletResponse resp) {
//
//        Long id  =RequestUtil.getLong(request,"id",-1L);
//        Integer state = RequestUtil.getInteger(request,"id",0);
//
//
//        JSONObject result = ownerService.editOwner(id);
//        //把结果返回页面
//        StringUtils.ajaxOutput(resp, result.toJSONString());
//        return null;
//    }

    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp) {

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String name = RequestUtil.getString(request,"name");
        String homeNumber = RequestUtil.getString(request,"home_number");
        String phone = RequestUtil.getString(request,"phone");
        String identityCard = RequestUtil.getString(request,"identity_card");
        String remark = RequestUtil.getString(request,"remark");
        Integer state = RequestUtil.getInteger(request,"id",0);
        Long comid  =RequestUtil.getLong(request,"comid",-1L);
        HomeOwnerTb homeOwnerTb = new HomeOwnerTb();
        homeOwnerTb.setComid(comid);
        homeOwnerTb.setHomeNumber(homeNumber);
        homeOwnerTb.setIdentityCard(identityCard);
        homeOwnerTb.setName(name);
        homeOwnerTb.setRemark(remark);
        homeOwnerTb.setPhone(phone);
        homeOwnerTb.setState(state);

        JSONObject result = ownerService.addOwner(homeOwnerTb);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"注册了业主"+name);
            parkLogTb.setType("homeowner");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = ownerService.exportExcel(reqParameterMap);
        String [][] heards = new String[][]{{"姓名","STR"},{"房号","STR"},{"手机号","STR"},{"身份证号","STR"},{"状态","STR"},{"备注","STR"}};
        ExportDataExcel excel = new ExportDataExcel("业主管理", heards, "sheet1");
        String fname = "业主管理";
        fname = StringUtils.encodingFileName(fname)+".xls";
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+fname);
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
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了业主信息");
        parkLogTb.setType("homeowner");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);
        return null;
    }

}
