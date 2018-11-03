package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CityVipService;
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
@RequestMapping("/cityvip")
public class CityVipManageAction {

    Logger logger = LoggerFactory.getLogger(CarRenewAction.class);


    @Autowired
    private CityVipService cityVipService;
    @Autowired
    private SaveLogService saveLogService;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityVipService.selectResultByConditions(reqParameterMap);

        StringUtils.ajaxOutput(response,result.toJSONString());
        return null;
    }



//    @RequestMapping(value = "add")
//    public String add(HttpServletRequest req, HttpServletResponse resp){
//
//        JSONObject result = cityVipService.createVip(req);
//
//        StringUtils.ajaxOutput(resp,result.toJSONString());
//
//        return null;
//    }




    @RequestMapping(value = "/importExcel")
    public String importExcel(HttpServletRequest request, HttpServletResponse resp,@RequestParam("file")MultipartFile file) throws Exception{
        try {
            String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);

            Long groupid = RequestUtil.getLong(request, "groupid", -1L);
            Long cityid = RequestUtil.getLong(request, "cityid", -1L);
            JSONObject result = cityVipService.importExcel(file, groupid, cityid, nickname, uin);
            StringUtils.ajaxOutput(resp,result.toJSONString());
        }catch (Exception e){
            logger.error("import error",e);
        }

//        if((Integer)result.get("state")==1){
//            ParkLogTb parkLogTb = new ParkLogTb();
//            parkLogTb.setOperateUser(nickname);
//            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
//            parkLogTb.setOperateType(1);
//            parkLogTb.setContent(uin+"("+nickname+")"+"导入月卡会员成功");
//            parkLogTb.setType("vip");
//            parkLogTb.setGroupId(groupid);
//            saveLogService.saveLog(parkLogTb);
//        }
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);


        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<String>> bodyList = cityVipService.exportExcel(reqParameterMap);
        String[][] heards = new String[][]{{"编号","STR"},{"套餐名称","STR"},{"所属车场","STR"},{"车主姓名","STR"},{"车牌号码","STR"},{"购买时间","STR"},{"开始时间","STR"},{"结束时间","STR"},{"应收金额","STR"},{"实收金额","STR"},{"联系电话","STR"},{"车型类型","STR"},{"单双日限行","STR"},{"备注","STR"}};

        ExportDataExcel excel = new ExportDataExcel("集团会员数据", heards, "sheet1");
        String fname = "集团会员数据";
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
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了集团会员数据");
        parkLogTb.setType("vip");
        parkLogTb.setGroupId(groupid);
        saveLogService.saveLog(parkLogTb);
        return null;
    }

}
