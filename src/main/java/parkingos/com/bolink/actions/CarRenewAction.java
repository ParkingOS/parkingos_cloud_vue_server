package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CarRenewService;
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
@RequestMapping("/carrenew")
public class CarRenewAction {

    Logger logger = Logger.getLogger(CarRenewAction.class);


    @Autowired
    private CarRenewService carRenewService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = carRenewService.selectResultByConditions(reqParameterMap );

        StringUtils.ajaxOutput(response, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

//        String[]  heards = new String[]{"编号", "购买流水号", "月卡编号", "月卡续费时间", "应收金额", "实收金额", "收费员", "缴费类型", "车牌号", "用户编号", "有效期", "备注"};
        String[][]  heards = new String[][]{{"编号","STR"}, {"购买流水号","STR"}, {"月卡编号","STR"}, {"月卡续费时间","STR"}, {"应收金额","STR"}, {"实收金额","STR"}, {"收费员","STR"}, {"缴费类型","STR"}, {"车牌号","STR"}, {"用户编号","STR"}, {"有效期","STR"}, {"备注","STR"}};
        logger.info(reqParameterMap);
        //获取要到处的数据
//        List<List<String>> bodyList = carRenewService.exportExcel(reqParameterMap);
        List<List<Object>> bodyList = carRenewService.exportExcel(reqParameterMap);


        ExportDataExcel excel = new ExportDataExcel("月卡续费记录", heards, "sheet1");
        String fname = "月卡续费记录";
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

        //调用导出工具类导出...组装导出头 等
//        String fname = "月卡续费记录" + TimeTools.getDate_YY_MM_DD();
//        fname = StringUtils.encodingFileName(fname);
//        java.io.OutputStream os;
//        try {
//            response.reset();
//            response.setHeader("Content-disposition", "attachment; filename="
//                    + fname + ".xls");
//            response.setContentType("application/x-download");
//            os = response.getOutputStream();
//            ExportExcelUtil importExcel = new ExportExcelUtil("月卡续费记录",
//                    heards, bodyList);
//            importExcel.createExcelFile(os);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }

}