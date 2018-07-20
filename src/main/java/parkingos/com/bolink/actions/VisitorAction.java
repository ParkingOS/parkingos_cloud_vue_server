package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.VisitorTb;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.VisitorService;
import parkingos.com.bolink.utils.ExportDataExcel;
import parkingos.com.bolink.utils.QrCodeUtil;
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
@RequestMapping("/visitor")
public class VisitorAction {

    Logger logger = Logger.getLogger(VisitorAction.class);

    @Autowired
    private VisitorService visitorService;
    @Autowired
    private SaveLogService saveLogService;
    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = visitorService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = visitorService.exportExcel(reqParameterMap);
        String [][] heards = new String[][]{{"编号","STR"},{"车牌号","STR"},{"电话","STR"},{"申请时间","STR"},{"开始时间","STR"},{"结束时间","STR"},{"状态","STR"},{"备注","STR"}};
        ExportDataExcel excel = new ExportDataExcel("访客管理", heards, "sheet1");
        String fname = "访客管理";
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
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了访客");
        parkLogTb.setType("visitor");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);
        return null;
    }


    @RequestMapping(value = "/downloadCode")
    public String downloadCode(HttpServletRequest request, HttpServletResponse resp){

        String filename = "Visitor";

        String url = RequestUtil.getString(request,"url");
        logger.info("下载二维码图片:"+url);
        String serverPath = request.getSession().getServletContext().getRealPath("/resource/images/");
        logger.info("下载二维码图片serverPath:" + serverPath);
        try {
            QrCodeUtil.generateQRCode(url, filename, serverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QrCodeUtil.downAllFile(request,resp,"Visitor");
        return null;
    }


    @RequestMapping(value = "/editstate")
    public String edit(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String remark =RequestUtil.getString(request, "remark");
        Integer state = RequestUtil.getInteger(request, "state", 0);
        Long id = RequestUtil.getLong(request,"id",-1L);
        if(remark.length()>20){
            String str = "{\"state\":0,\"msg\":\"备注限制20字\"}";
            StringUtils.ajaxOutput(resp,str);
            return null;
        }

        logger.info("车场审核状态"+remark+"~~~"+state+"~~~"+id);

        VisitorTb visitorTb = new VisitorTb();
        visitorTb.setId(id);
        visitorTb.setRemark(remark);
        visitorTb.setState(state);

        JSONObject result = visitorService.updateVisitor(visitorTb);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"审核了访客"+id);
            parkLogTb.setType("visitor");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


}
