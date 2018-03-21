package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CityParkOrderAnlysisService;
import parkingos.com.bolink.utils.ExportExcelUtil;
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
@RequestMapping("/cityparkorder")
public class CityParkOrderanlysisAction {

    Logger logger = Logger.getLogger(CityParkOrderanlysisAction.class);

    @Autowired
    private CityParkOrderAnlysisService cityParkOrderanlysisService;


    /*
    * 查询统计
    *
    * */
    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = cityParkOrderanlysisService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> resList = cityParkOrderanlysisService.exportExcel(reqParameterMap);
        String title = "集团车场日报";
        String sheeatName = "sheet1";
        String headers[] =  { "日期", "车场", "应收金额", "实收金额", "实收金额", "实收金额","减免金额" } ;
        String dataType []={"STR","STR","STR","STR","STR","STR","STR"};
        String[] subHeads = new String[] {"现金支付", "电子支付", "合计"};
        String[] headnum = new String[] { "1,2,0,0", "1,2,1,1","1,2,2,2","1,1,3,5","1,2,6,6"};
        String[] subheadnum = new String[] { "2,2,3,3", "2,2,4,4", "2,2,5,5"};
        ExportExcelUtil excelUtil = new ExportExcelUtil(title, headers, sheeatName, dataType, subHeads, headnum, subheadnum, new int[]{2,6});
        String fname = "集团车场日报";
        fname = StringUtils.encodingFileName(fname)+".xls";
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+fname);
            excelUtil.PoiWriteExcel_To2007(resList,os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
