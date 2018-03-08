package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.LiftRodService;
import parkingos.com.bolink.utils.CustomDefind;
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
@RequestMapping("/liftRod")
public class LiftRodAction {

    Logger logger = Logger.getLogger(LiftRodAction.class);

    @Autowired
    private LiftRodService liftRodService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {
        logger.error("======================");
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = liftRodService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/getLiftRodPicture")
    public String getLiftRodPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String comid = RequestUtil.getString(request, "comid");
        String liftrodId = RequestUtil.getString(request, "liftrodid");
        //集团 抬杆,获得id 根据id获得comid
        String id = RequestUtil.getString(request,"id");
        logger.error("==========>>>>获取图片"+id);
        if(id!=null&&!"undefined".equals(id)&&!"".equals(id)){
            comid = getComidByLift(Long.parseLong(id));
        }
        logger.error("==========>>>>获取图片"+liftrodId);
        logger.error("==========>>>>获取图片"+comid);

        byte[] content = liftRodService.getLiftRodPicture(comid, liftrodId);

        if (content.length == 0) {
            System.out.println("==========>>>>获取图片"+CustomDefind.IMAGEURL);
            //测试用  之后读配置文件
            response.sendRedirect(CustomDefind.IMAGEURL+"/images/nopic.jpg");
//            response.sendRedirect("http://120.25.121.204:8080/cloud/images/nopic.jpg");
            return null;
        } else {
            response.setDateHeader("Expires", System.currentTimeMillis() + 12 * 60 * 60 * 1000);
            response.setContentLength(content.length);
            response.setContentType("image/jpeg");
            OutputStream o = response.getOutputStream();
            o.write(content);
            o.flush();
            o.close();
        }
        return null;
    }

    private String getComidByLift(long liftId) {
        return liftRodService.getComidByLift(liftId);
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

//        String [] heards = new String[]{"编号","抬杆编号","时间","收费员","通道","原因","备注"};
        String [][] heards = new String[][]{{"编号","STR"},{"抬杆编号","STR"},{"时间","STR"},{"收费员","STR"},{"通道","STR"},{"原因","STR"},{"备注","STR"}};
        //获取要到处的数据
//        List<List<String>> bodyList = liftRodService.exportExcel(reqParameterMap);
        List<List<Object>> bodyList = liftRodService.exportExcel(reqParameterMap);

        ExportDataExcel excel = new ExportDataExcel("抬杆记录", heards, "sheet1");
        String fname = "抬杆记录";
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
//        String fname = "抬杆记录" + TimeTools.getDate_YY_MM_DD();
//        fname = StringUtils.encodingFileName(fname);
//        java.io.OutputStream os;
//        try {
//            response.reset();
//            response.setHeader("Content-disposition", "attachment; filename="
//                    + fname + ".xls");
//            response.setContentType("application/x-download");
//            os = response.getOutputStream();
//            ExportExcelUtil importExcel = new ExportExcelUtil("抬杆记录",
//                    heards, bodyList);
//            importExcel.createExcelFile(os);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @RequestMapping(value = "/getLiftReason")
    public String getLiftReason(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String result = (String)liftRodService.getLiftReason(0);
        StringUtils.ajaxOutput(response, result);
        return null;
    }
}
