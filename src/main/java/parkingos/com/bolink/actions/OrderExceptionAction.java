package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.OrderExceptionService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orderexception")
public class OrderExceptionAction {

    Logger logger = LoggerFactory.getLogger(OrderExceptionAction.class);

    @Autowired
    private OrderExceptionService orderExceptionService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = orderExceptionService.selectResultByConditions(reqParameterMap);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/export")
    public String export(HttpServletRequest request, HttpServletResponse resp){
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = orderExceptionService.exportExcel(reqParameterMap);
//        new String[]{"订单编号","车场名称","所属泊位段","泊位编号","车牌号","进场方式","进场时间","出场时间","停车时长","支付方式","金额","预付金额","收款人账号","收款人名称","订单状态","结算方式","进场通道","出场通道"};
        String [][] heards = new String[][]{{"更新时间","STR"},{"车牌号","STR"},{"订单号","STR"},{"入场时间","STR"},{"出场时间","STR"},{"现金结算","STR"},{"更新金额","STR"},{"减免金额","STR"},{"更新减免","STR"},{"出场收费员","STR"},{"更新收费员","STR"}};
        ExportDataExcel excel = new ExportDataExcel("异常订单数据", heards, "sheet1");
        String fname = "异常订单数据";
        fname = StringUtils.encodingFileName(fname)+".xls";
        try {
            OutputStream os = resp.getOutputStream();
            resp.reset();
            resp.setHeader("Content-disposition", "attachment; filename="+fname);
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
        parkLogTb.setContent(uin+"("+nickname+")"+"导出了异常订单数据");
        parkLogTb.setType("order");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);
        return null;

    }

}
