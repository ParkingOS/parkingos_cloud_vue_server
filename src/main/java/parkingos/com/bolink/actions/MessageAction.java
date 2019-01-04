package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.MessageService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.ExportDataExcel;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/message")
public class MessageAction {

    Logger logger = LoggerFactory.getLogger(MessageAction.class);
    @Autowired
    MessageService messageService;
    @Autowired
    SaveLogService saveLogService;

    @RequestMapping(value = "/tobuy")
    public String addCity(HttpServletRequest request,HttpServletResponse resp) {
        //购买短信条数
        Integer count = RequestUtil.getInteger(request,"count",0);
        //需要支付金额
        Double money = RequestUtil.getDouble(request,"money",0.0);
        //生成流水号
        String seed = (new Random().nextDouble() + "").substring(2, 9);
        String tradeNo = "Msg"+System.currentTimeMillis() + seed;
        //购买短信的厂商
        Long union_id = RequestUtil.getLong(request,"union_id",-1L);
        //如果细化到车场购买
        Long park_id = RequestUtil.getLong(request,"comid",-1L);

        JSONObject result = messageService.buyMessage(count,money,tradeNo,union_id,park_id);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/notice")
    public String notice(HttpServletRequest request,HttpServletResponse resp) {
        String data = readBodyFormRequsetStream(request);
        logger.info(StringUtils.decodeUTF8(data));
        JSONObject jsonObject = JSONObject.parseObject(StringUtils.decodeUTF8(data));
        Long parkId = jsonObject.getLong("park_id");
        String carNumber = jsonObject.getString("car_number");
        Long eTime = jsonObject.getLong("e_time");
        String mobile = jsonObject.getString("mobile");
        Integer day = jsonObject.getInteger("day");

//        Long parkId = RequestUtil.getLong(request,"park_id",-1L);
//        String carNumber = RequestUtil.getString(request,"car_number");
//        Long eTime = RequestUtil.getLong(request,"e_time",-1L);
//        String mobile = RequestUtil.getString(request,"mobile");
//        Integer day = RequestUtil.getInteger(request,"day",1);
//        logger.info("===>>>"+parkId);
        String result = messageService.notice(parkId,carNumber,eTime,mobile,day);
        return null;
    }


    @RequestMapping(value = "/getsendtrade")
    public String getSendTrade(HttpServletRequest request,HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = messageService.getSendTrade(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/getbuytrade")
    public String getBuyTrade(HttpServletRequest request,HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = messageService.getBuyTrade(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }



    @RequestMapping(value = "/exportsendtrade")
    public String exportSendTrade(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        String [][] heards = new String[][]{{"使用日期","STR"},{"手机号","STR"},{"使用模块","STR"}};
        //获取要到处的数据
        List<List<Object>> bodyList = messageService.exportSendTrade(reqParameterMap);

        ExportDataExcel excel = new ExportDataExcel("短信使用明细", heards, "sheet1");
        String fname = "短信使用明细";
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
        parkLogTb.setContent(uin+"("+nickname1+")"+"导出了短信流水");
        parkLogTb.setType("message");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);

        return null;
    }

    @RequestMapping(value = "/exportbuytrade")
    public String exportBuyTrade(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        String [][] heards = new String[][]{{"购买数量","STR"},{"续费日期","STR"},{"到期日期","STR"},{"支付金额","STR"},{"流水号","STR"}};
        //获取要到处的数据
        List<List<Object>> bodyList = messageService.exportBuyTrade(reqParameterMap);

        ExportDataExcel excel = new ExportDataExcel("短信充值明细", heards, "sheet1");
        String fname = "短信充值明细";
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


    private  String readBodyFormRequsetStream(HttpServletRequest request) {
        try {
            int size = request.getContentLength();
            if (size > 0) {
                InputStream is = request.getInputStream();
                int readLen = 0;
                int readLengthThisTime = 0;
                byte[] message = new byte[size];
                while (readLen != size) {
                    readLengthThisTime = is.read(message, readLen, size- readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return new String(message,"utf-8");
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "";
    }


    @RequestMapping(value = "/getcodestate")
    public String getCodeState(HttpServletRequest request,HttpServletResponse resp) {
        String tradeNo = RequestUtil.getString(request,"trade_no");


        JSONObject result = messageService.getCodeState(tradeNo);
        StringUtils.ajaxOutput(resp, result.toJSONString());

        return null;
    }

}
