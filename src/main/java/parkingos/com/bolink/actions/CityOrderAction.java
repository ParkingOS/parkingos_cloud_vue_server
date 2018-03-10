package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CityOrderService;
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
@RequestMapping("/cityorder")
public class CityOrderAction {

    Logger logger = Logger.getLogger(CityOrderAction.class);

    @Autowired
//    @Resource(name = "mybatis")
    @Resource(name = "cityorderSpring")
    private CityOrderService cityOrderService;

    /*
    * 集团和城市 订单 接口
    *
    * */
    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = cityOrderService.selectResultByConditions(reqParameterMap);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = cityOrderService.exportExcel(reqParameterMap);
//        new String[]{"订单编号","车场名称","所属泊位段","泊位编号","车牌号","进场方式","进场时间","出场时间","停车时长","支付方式","金额","预付金额","收款人账号","收款人名称","订单状态","结算方式","进场通道","出场通道"};
        String [][] heards = new String[][]{{"编号","STR"},{"车场名称","STR"},{"收款人账号","STR"},{"收款人名称","STR"},{"进场方式","STR"},{"车牌号","STR"},{"车型","STR"},{"进场时间","STR"},{"出场时间","STR"},{"时长","STR"},{"支付方式","STR"},{"优惠原因","STR"},{"应收金额","STR"},{"实收金额","STR"},{"电子预付金额","STR"},{"现金预付金额","STR"},{"电子结算金额","STR"},{"现金结算金额","STR"},{"减免金额","STR"},{"订单状态","STR"},{"进场通道","STR"},{"出场通道","STR"},{"订单编号","STR"}};
        ExportDataExcel excel = new ExportDataExcel("订单数据", heards, "sheet1");
        String fname = "订单数据";
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
