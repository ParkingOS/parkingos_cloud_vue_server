package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.service.SaveLogService;
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
@RequestMapping("/order")
public class OrderManagerAction {

    Logger logger = Logger.getLogger(OrderManagerAction.class);

    @Autowired
//    @Resource(name = "mybatis")
    @Resource(name = "orderSpring")
    private OrderService orderService;
    @Autowired
    private SaveLogService saveLogService;
//    @Autowired
//    private OrderServiceController orderServiceController;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = orderService.selectResultByConditions(reqParameterMap);
//        JSONObject result =new JSONObject();
//        List<OrderTb>  list = orderServiceController.getOrdersByMapConditons(reqParameterMap);
//        result.put("rows",list);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/getOrderPicture")
    public String getOrderPicture(HttpServletRequest request, HttpServletResponse resp) {
        String orderid = RequestUtil.getString(request,"orderid");
        Long comid = RequestUtil.getLong(request, "comid", -1L);

        //根据传过来的id 来判断车场  集团时候用
        Long id = RequestUtil.getLong(request,"id",-1L);
        if(id!=-1){
            comid = orderService.getComidByOrder(id);
        }
        logger.error("==========>>>>获取图片..orderid..comid==>" + orderid+"==>"+comid);
        JSONObject result = orderService.getPicResult(orderid, comid);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/carpicsup")
    public String carpicsup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String orderid = RequestUtil.getString(request, "orderid");
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String type = RequestUtil.getString(request, "typeNew");
        Integer currentnum = RequestUtil.getInteger(request,"currentnum",-1);
        byte[] content = orderService.getCarPics(orderid,comid,type,currentnum);
        if(content.length==0){
            response.sendRedirect("http://sysimages.tq.cn/images/webchat_101001/common/kefu.png");
            return null;
        }else{
            response.setDateHeader("Expires", System.currentTimeMillis()+12*60*60*1000);
            response.setContentLength(content.length);
            response.setContentType("image/jpeg");
            OutputStream o = response.getOutputStream();
            o.write(content);
            o.flush();
            o.close();
            System.out.println("mongdb over.....");
        }
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

//        List<List<String>> bodyList = orderService.exportExcel(reqParameterMap);
//        String[] heards =  new String[]{"编号","进场方式","车牌号","进场时间","出场时间","时长","支付方式","应收金额","实收金额","电子预付金额","现金预付金额","电子结算金额","现金结算金额","减免金额","优惠原因","入场收费员","收款人","状态 ","进场通道 ","出场通道 "};
        List<List<Object>> bodyList = orderService.exportExcel(reqParameterMap);
        String [][] heards = new String[][]{{"进场方式","STR"},{"车牌号","STR"},{"车型","STR"},{"进场时间","STR"},{"出场时间","STR"},{"时长","STR"},{"支付方式","STR"},{"优惠原因","STR"},{"应收金额","STR"},{"实收金额","STR"},{"电子预付金额","STR"},{"现金预付金额","STR"},{"电子结算金额","STR"},{"现金结算金额","STR"},{"减免金额","STR"},{"入场收费员","STR"},{"收款人","STR"},{"状态","STR"},{"进场通道","STR"},{"出场通道","STR"},{"订单编号","STR"}};
        ExportDataExcel excel = new ExportDataExcel("订单数据", heards, "sheet1");
        String fname = "订单数据";
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
        parkLogTb.setContent(uin+"("+nickname1+")"+"导出了订单数据");
        parkLogTb.setType("order");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);
        return null;
    }
}
