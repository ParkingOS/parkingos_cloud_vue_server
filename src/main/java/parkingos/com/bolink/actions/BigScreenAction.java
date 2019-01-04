package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.AddValueService;
import parkingos.com.bolink.service.BigScreenService;
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
import java.util.Random;

@Controller
@RequestMapping("/bigscreen")
public class BigScreenAction {

    Logger logger = LoggerFactory.getLogger(BigScreenAction.class);
    @Autowired
    BigScreenService bigScreenService;
    @Autowired
    SaveLogService saveLogService;

    @RequestMapping(value = "/getstate")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        logger.info("get bigScreen state:"+comid);
        JSONObject result = bigScreenService.getState(comid);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/tobuy")
    public String toBuy(HttpServletRequest request, HttpServletResponse resp){

        Integer buyMonth = RequestUtil.getInteger(request,"buy_month",0);
        //需要支付金额
        Double money = RequestUtil.getDouble(request,"money",0.0);
        //生成流水号
        String seed = (new Random().nextDouble() + "").substring(2, 9);
        String tradeNo = "Screen"+System.currentTimeMillis() + seed;
        Long park_id = RequestUtil.getLong(request,"comid",-1L);

        JSONObject result = bigScreenService.buyBigScreen(buyMonth,money,tradeNo,park_id);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


//    @RequestMapping(value = "/getbigscreenprice")
//    public String getBigScreenPrice(HttpServletRequest request, HttpServletResponse resp){
//        Long comid = RequestUtil.getLong(request,"comid",-1L);
//        String result = bigScreenService.getBigScreenData(comid,2);
//        StringUtils.ajaxOutput(resp,result);
//        return null;
//    }

    @RequestMapping(value = "/getbuytrade")
    public String getBuyTrade(HttpServletRequest request,HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = bigScreenService.getBuyTrade(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/exportbuytrade")
    public String exportBuyTrade(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        String [][] heards = new String[][]{{"购买月数","STR"},{"续费日期","STR"},{"开始日期","STR"},{"到期日期","STR"},{"支付金额","STR"},{"流水号","STR"}};
        //获取要到处的数据
        List<List<Object>> bodyList = bigScreenService.exportBuyTrade(reqParameterMap);

        ExportDataExcel excel = new ExportDataExcel("大屏充值明细", heards, "sheet1");
        String fname = "大屏充值明细";
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
        parkLogTb.setOperateUser(nickname1);
        parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
        parkLogTb.setOperateType(4);
        parkLogTb.setContent(uin+"("+nickname1+")"+"导出了充值流水");
        parkLogTb.setType("message");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);

        return null;
    }


}
