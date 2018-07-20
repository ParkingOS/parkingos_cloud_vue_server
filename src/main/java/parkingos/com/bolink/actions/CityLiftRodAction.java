package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CityLiftRodService;
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
@RequestMapping("/cityliftRod")
public class CityLiftRodAction {

    Logger logger = Logger.getLogger(CityLiftRodAction.class);

    @Autowired
    private CityLiftRodService cityLiftRodService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityLiftRodService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        String [][] heards = new String[][]{{"编号","STR"},{"抬杆编号","STR"},{"时间","STR"},{"收费员","STR"},{"通道","STR"},{"原因","STR"},{"备注","STR"}};
        List<List<Object>> bodyList = cityLiftRodService.exportExcel(reqParameterMap);

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
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了抬杆记录");
        parkLogTb.setType("liftrod");
        parkLogTb.setGroupId(groupid);
        saveLogService.saveLog(parkLogTb);
        return null;
    }

}
