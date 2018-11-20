package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.LiftRodService;
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
@RequestMapping("/liftRod")
public class LiftRodAction {

    Logger logger = LoggerFactory.getLogger(LiftRodAction.class);

    @Autowired
    private LiftRodService liftRodService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = liftRodService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/getLiftRodPicture")
    public String getLiftRodPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String comid = RequestUtil.getString(request, "comid");
        String liftrodId = StringUtils.decodeUTF8(RequestUtil.getString(request, "liftrodid"));
        //集团 抬杆,获得id 根据id获得comid
        String id = RequestUtil.getString(request,"id");
        logger.error("==========>>>>获取图片"+id+"~~~liftrodId"+liftrodId+"comid"+comid);
//        if(id!=null&&!"undefined".equals(id)&&!"".equals(id)){
//            comid = getComidByLift(Long.parseLong(id));
//        }

        liftRodService.getLiftRodPicture(comid, liftrodId,response);

//        if (content.length == 0) {
//            //测试用  之后读配置文件
//            response.sendRedirect(CustomDefind.IMAGEURL+"/images/nopic.jpg");
////            response.sendRedirect("http://120.25.121.204:8080/cloud/images/nopic.jpg");
//            return null;
//        } else {
//            response.setDateHeader("Expires", System.currentTimeMillis() + 12 * 60 * 60 * 1000);
//            response.setContentLength(content.length);
//            response.setContentType("image/jpeg");
//            OutputStream o = response.getOutputStream();
//            o.write(content);
//            o.flush();
//            o.close();
//        }
        return null;
    }

    private String getComidByLift(long liftId) {
        return liftRodService.getComidByLift(liftId);
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

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
        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setOperateUser(nickname);
        parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
        parkLogTb.setOperateType(4);
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了抬杆数据");
        parkLogTb.setType("liftrod");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);

        return null;
    }

    @RequestMapping(value = "/getLiftReason")
    public String getLiftReason(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String result = (String)liftRodService.getLiftReason(0);
        StringUtils.ajaxOutput(response, result);
        return null;
    }
}
