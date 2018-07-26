package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CenterMonitorService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/centermonitor")
public class CenterMonitorAction {

    Logger logger = Logger.getLogger(CenterMonitorAction.class);
    @Autowired
    private CenterMonitorService centerMonitorService;

    @RequestMapping(value = "/centermonitordata")
    public String getCenterMonitor(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        Long groupid = RequestUtil.getLong(request, "groupid", -1L);
        logger.error("中央监控页面:" + comid + "~~~~" + groupid);
        String ret = centerMonitorService.getCenterMonitor(comid, groupid);
        logger.error("中央监控页面数据返回:" + ret);
        StringUtils.ajaxOutput(response, ret);
        return null;
    }

    /*
    * 获取特定的确认订单显示
    *
    * */
    @RequestMapping(value = "/getConfirmorder")
    public String getConfirmOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进入获取单个图片的时间>>>>>>>>>>>" + new Date().getTime());
        String event_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "event_id"));
        String car_number = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String comid = StringUtils.decodeUTF8(RequestUtil.getString(request, "comid"));

        logger.error("获得指定的确认订单的图片" + event_id + "~~~" + car_number + "~~~" + comid);

        byte[] content = centerMonitorService.getConfirmPic(event_id, Long.parseLong(comid), car_number);
        System.out.println("出来获取单个图片的时间>>>>>>>>>>>" + new Date().getTime());
        if (content.length == 0) {
            response.sendRedirect("http://sysimages.tq.cn/images/webchat_101001/common/kefu.png");
            return null;
        } else {
            try {
                String foldPath = request.getServletContext().getRealPath("/images/monitor/");
                File folder = new File(foldPath);
                if (!folder.exists() || !folder.isDirectory()) {
                    folder.mkdirs();
                }
                InputStream in = new ByteArrayInputStream(content);
                String filePath = request.getServletContext().getRealPath("/images/monitor/" + comid + "_" + event_id + "_" + car_number.substring(1) + ".jpg");
                File file = new File(filePath);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024 * 8];
                int nRead = 0;
                while ((nRead = in.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
                in.close();
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                JSONObject retObj = new JSONObject();
                retObj.put("picName", comid + "_" + event_id + "_" + car_number.substring(1) + ".jpg");
                retObj.put("event_id", event_id);
                retObj.put("car_nmber", car_number);
                StringUtils.ajaxOutput(response, retObj.toJSONString());
            } catch (Exception e) {
                // TODO: handle exception
                logger.info(e.toString());
            }
            System.out.println("mongdb over.....");
            return null;
        }
    }

    /*
    * 模糊匹配车牌列出所有确认订单及图片
    *
    * */
    @RequestMapping(value = "/matchconfirmorder")
    public String matchConfirmOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进入模糊获取多个图片的时间>>>>>>>>>>>" + new Date().getTime());
        String event_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "event_id"));
        String car_number = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String comid = StringUtils.decodeUTF8(RequestUtil.getString(request, "comid"));

        logger.error("获得模糊匹配到的确认订单的图片" + event_id + "~~~" + car_number + "~~~" + comid);

        centerMonitorService.matchConfirmPic(event_id, Long.parseLong(comid), car_number, request, response);
        System.out.println("出来获取多个图片的时间>>>>>>>>>>>" + new Date().getTime());

        return null;
    }

    /*
    *点击匹配到的图片查询具体信息
    *
    * */
    @RequestMapping(value = "/queryselectorder")
    public String querySelectOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderId = RequestUtil.getString(request, "order_id");
//        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String comid = URLDecoder.decode(RequestUtil.getString(request, "comid"), "UTF-8");


        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString( request, "car_number" ));
//        try {
//            carNumber = new String( carNumber.getBytes( "ISO-8859-1" ), "UTF-8" );
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        logger.error(RequestUtil.getString(request,"car_number"));
//        logger.error(carNumber);
//        logger.error(StringUtils.decodeUTF8(carNumber));

        logger.error("点击匹配到的订单查询出具体信息条件" + orderId + "~~~" + carNumber + "~~~" + comid);

        Map orderMap = centerMonitorService.getSelectOrder(comid, carNumber);

        logger.error("点击匹配到的订单查询出具体信息结果" + orderMap);
        StringUtils.ajaxOutput(response, JSONObject.toJSONString(orderMap));
        return null;
    }

    /*
    *
    * 确认订单通知车场
    *
    * */
    @RequestMapping(value = "/balanceorderinfo")
    public String balanceOrderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String orderId = RequestUtil.getString(request, "order_id");
        String carNumber =  StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String channel_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "channel_id"));
        String event_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "event_id"));
        String comid = RequestUtil.getString(request, "comid");

        logger.error("确认订单通知车场" + "~~~" + orderId + "~~~" + carNumber + "~~~" + channel_id + "~~~" + event_id + "~~~" + comid);

        centerMonitorService.sendMessageToPark(orderId, carNumber, channel_id, event_id, comid, response);

        return null;
    }

    /*
    *
    * 抬杆通知
    * */
    @RequestMapping(value = "/liftrod")
    public String liftRod(HttpServletRequest request, HttpServletResponse response) throws Exception {


        String channel_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "channel_id"));
        String channel_name = StringUtils.decodeUTF8(RequestUtil.getString(request, "channel_name"));
        String comid = StringUtils.decodeUTF8(RequestUtil.getString(request, "comid"));

        logger.error("进入抬杆通知:"+channel_id+"~~~"+channel_name+"~~~"+comid);
        centerMonitorService.liftRodMessage(channel_id,channel_name,comid,response);
        logger.error("抬杆通知发送完成");
        return null;
    }

    /*
    * 对讲通知 切换大屏
    *
    * */
    @RequestMapping(value = "/callinform")
    public String callInform(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //是不是从另一个服务器过来的
        Integer fromOther = RequestUtil.getInteger(request,"fromother",0);
        logger.error("this is what times "+fromOther);
        //电话Id
        String channel_uniqueid = RequestUtil.processParams(request, "channel_uniqueid");
        //主叫号
        String callerid_num = RequestUtil.processParams(request, "callerid_num");
        logger.error("中央监控>>>>>呼入主叫号:" + callerid_num + ">>>>");
        //被叫号
        String exten = RequestUtil.processParams(request, "exten");
        logger.error("中央监控>>>>>呼入被叫号:" + exten + ">>>>");

        centerMonitorService.callInform(fromOther,callerid_num,exten,response);


        return null;


    }

    /*
    *
    * 点击小视频框获得 抬杆  需要的数据
    *
    * */

    @RequestMapping(value = "/querychannel")
    public String qryChannelByMonitId(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long monitor_id = RequestUtil.getLong(request, "monitor_id", -1L);

//        Map channleMap = daService.getMap("select mi.*,cp.passname,cp.channel_id as channelid  from monitor_info_tb mi,com_pass_tb cp where cp.id = mi.channel_id and mi.id=? ", new Object[]{monitor_id});
//        String json = StringUtils.createJson(channleMap);
        Map liftMap = centerMonitorService.qryChannelByMonitId(monitor_id);
        StringUtils.ajaxOutput(response, JSONObject.toJSONString(liftMap));

        return null;


    }

}

