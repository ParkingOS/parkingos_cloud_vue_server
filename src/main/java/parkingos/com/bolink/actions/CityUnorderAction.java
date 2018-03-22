package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CityUnorderService;
import parkingos.com.bolink.utils.ExportDataExcel;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/unorder")
public class CityUnorderAction {

    Logger logger = Logger.getLogger(CityUnorderAction.class);

    @Autowired
//    @Resource(name = "citymybatis")
    @Resource(name = "unorderSpring")
    private CityUnorderService cityUnorderService;

    /*
    * 集团和城市在场车辆接口
    *
    * */
    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityUnorderService.selectResultByConditions(reqParameterMap);
        logger.info(result);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = cityUnorderService.exportExcel(reqParameterMap);
        String [][] heards = new String[][]{{"编号","STR"},{"所属车场","STR"},{"进场收费员","STR"},{"进场方式","STR"},{"车牌号","STR"},{"进场时间","STR"},{"停车时长","STR"},{"状态","STR"},{"进场通道","STR"},{"车场订单编号","STR"}};
        ExportDataExcel excel = new ExportDataExcel("在场车辆数据", heards, "sheet1");
        String fname = "在场车辆数据";
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
        return null;
    }
}
