package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.directwebremoting.ScriptSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.CenterMonitorMapper;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.mybatis.mapper.ParkInfoMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.dwr.DWRScriptSessionListener;
import parkingos.com.bolink.dwr.Push;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.LiftRodTb;
import parkingos.com.bolink.models.LiftrodInfoTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.*;
import parkingos.com.bolink.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CenterMonitorServiceImpl implements CenterMonitorService {

    Logger logger = Logger.getLogger(CenterMonitorServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private ParkInfoMapper parkInfoMapper;
    @Autowired
    private CenterMonitorMapper centerMonitorMapper;
    @Autowired
    private CityParkOrderAnlysisService cityParkOrderanlysisService;
    @Autowired
    private ParkOrderAnlysisService parkOrderanlysisService;
    @Autowired
    private OrderMapper orderMapper;
    DecimalFormat af1 = new DecimalFormat("0");

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    public String getCenterMonitor(Long comid, Long groupid) {

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long tday = calendar.getTimeInMillis() / 1000;

        List<HashMap<String, Object>> ss = new ArrayList<>();//泊位使用率
        HashMap<String, Object> countMap = new HashMap<>();//进出车统计
        HashMap<String, Object> totalIncomemap = new HashMap<>();//今日收入统计
        List<HashMap<String, Object>> confirmOrders = new ArrayList<>();//所有的确认订单
        Map<String, Object> videoMap = new HashMap<String, Object>();//需要返回的播放列表
        List<Map<String, Object>> list =new ArrayList<>();
        if (groupid > 0) {
            Long cityid = orderMapper.getCityIdByGroupId(groupid);
            //获取今日电子支付，现金支付，减免金额的统计
            Map<String, String> parammap = new HashMap<String, String>();
            parammap.put("groupid", groupid + "");
            String todyyymmdd = TimeTools.getDate_YY_MM_DD();
            parammap.put("date", todyyymmdd + " 00:00:00至" + todyyymmdd + "23:59:59");
            JSONObject retjson = cityParkOrderanlysisService.selectResultByConditions(parammap);
            JSONArray retarry = retjson.getJSONArray("rows");
            Double cashPay = 0d;
            Double electronicPay = 0d;
            Double freePay = 0d;
            if (retarry.size() > 0) {
                JSONObject object = (JSONObject) retarry.get(retarry.size() - 1);
                if (object.getString("cash_pay") != null && !"".equals(object.getString("cash_pay"))) {
                    cashPay = Double.parseDouble(object.getString("cash_pay"));
                    System.out.print("cashpay" + cashPay);
                }
                if (object.getString("electronic_pay") != null && !"".equals(object.getString("electronic_pay"))) {
                    electronicPay = Double.parseDouble(object.getString("electronic_pay"));
                    System.out.print("electronicPay" + electronicPay);
                }
                if (object.getString("free_pay") != null && !"".equals(object.getString("free_pay"))) {
                    freePay = Double.parseDouble(object.getString("free_pay"));
                    System.out.print("electronicPay" + electronicPay);
                }
            }
            ;

            totalIncomemap.put("elePay", af1.format(electronicPay));
            totalIncomemap.put("cashPay", af1.format(cashPay));
            totalIncomemap.put("freePay", af1.format(freePay));

            //获取车辆进场，离场，在场的数量统计
            int inCars = parkInfoMapper.getEntryCount(tday, groupid.intValue(),cityid);
            int outCars = parkInfoMapper.getExitCount(tday, groupid.intValue(),cityid);
            int inPark = parkInfoMapper.getInparkCount(tday, groupid.intValue(),cityid);
            countMap = new HashMap<String, Object>();
            countMap.put("inCars", inCars);
            countMap.put("outCars", outCars);
            countMap.put("inPark", inPark);
            //计算泊位使用率
            List<HashMap<String, Object>> parkidList = parkInfoMapper.getParkIdByGroupId(groupid.intValue());
            if(!parkidList.isEmpty()){
                ss = parkInfoMapper.getBerthPercent(parkidList, tday);
            }
            if(ss!=null&&ss.size()>0) {
                for (HashMap<String, Object> map : ss) {
                    Long asum = (Long) map.get("asum");
                    Long usum = (Long) map.get("usum");
                    DecimalFormat df = new DecimalFormat("#");
                    double pecent = 0d;
                    if (usum != 0) {
                        pecent = (float) usum * 100 / asum;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH");
                    long time = (long) map.get("create_time");
                    Date date = new Date(time * 1000);
                    map.put("time", sdf.format(date));
                    map.put("percent", df.format(pecent));
                }
            }

            //获取集团下面所有的确认订单
            confirmOrders = centerMonitorMapper.getConfirmOrdersByGroupid(groupid+"");
            //获取所有的播放资源  然后存到videoMap里面进行返回
            list = centerMonitorMapper.getMonitorsByGroupid(groupid+"");



        } else if (comid > 0) {

            Long cityid=-1L;
            Long groupId = orderMapper.getGroupIdByComId(comid);
            if(groupId!=null&&groupId>-1){
                cityid = orderMapper.getCityIdByGroupId(groupid);
            }else {
                cityid = orderMapper.getCityIdByComId(comid);
            }
            //获取今日电子支付，现金支付，减免金额的统计
            Map<String, String> parammap = new HashMap<String, String>();
            parammap.put("comid", comid + "");
            String todyyymmdd = TimeTools.getDate_YY_MM_DD();
            parammap.put("date", todyyymmdd + " 00:00:00至" + todyyymmdd + " 23:59:59");
            JSONObject retjson = parkOrderanlysisService.selectResultByConditions(parammap);
            JSONArray retarry = retjson.getJSONArray("rows");
            Double cashPay = 0d;
            Double electronicPay = 0d;
            Double freePay = 0d;
            if (retarry.size() > 0) {
                JSONObject object = (JSONObject) retarry.get(retarry.size() - 1);
                if (object.getString("cash_pay") != null && !"".equals(object.getString("cash_pay"))) {
                    cashPay = Double.parseDouble(object.getString("cash_pay"));
                    System.out.print("cashpay" + cashPay);
                }
                if (object.getString("electronic_pay") != null && !"".equals(object.getString("electronic_pay"))) {
                    electronicPay = Double.parseDouble(object.getString("electronic_pay"));
                    System.out.print("electronicPay" + electronicPay);
                }
                if (object.getString("free_pay") != null && !"".equals(object.getString("free_pay"))) {
                    freePay = Double.parseDouble(object.getString("free_pay"));
                    System.out.print("electronicPay" + electronicPay);
                }
            }

            totalIncomemap.put("elePay", af1.format(electronicPay));
            totalIncomemap.put("cashPay", af1.format(cashPay));
            totalIncomemap.put("freePay", af1.format(freePay));


            //获取车辆进场，离场，在场的数量统计
            int inCars = parkInfoMapper.getEntryCountbc(tday, comid.intValue(),cityid);
            int outCars = parkInfoMapper.getExitCountbc(tday, comid.intValue(),cityid);
            int inPark = parkInfoMapper.getInparkCountbc(tday, comid.intValue(),cityid);
            countMap = new HashMap<String, Object>();
            countMap.put("inCars", inCars);
            countMap.put("outCars", outCars);
            countMap.put("inPark", inPark);
            //计算泊位使用率
            HashMap<String, Object> tempmap = new HashMap<String, Object>();
            List<HashMap<String, Object>> parkidList = new ArrayList<HashMap<String, Object>>();
            tempmap.put("parkid", comid);
            parkidList.add(tempmap);
            ss = parkInfoMapper.getBerthPercent(parkidList, tday);
            for (HashMap<String, Object> map : ss) {
                Long asum = (Long) map.get("asum");
                Long usum = (Long) map.get("usum");
                DecimalFormat df = new DecimalFormat("#");
                double pecent = 0d;
                if (usum != 0) {
                    pecent = (float) usum * 100 / asum;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH");
                long time = (long) map.get("create_time");
                Date date = new Date(time * 1000);
                map.put("time", sdf.format(date));
                map.put("percent", df.format(pecent));
            }

            //获取集团下面所有的确认订单
            confirmOrders = centerMonitorMapper.getConfirmOrdersByComid(comid+"");
            //获取车场下面所有的播放列表
            list = centerMonitorMapper.getMonitorsByComid(comid+"");
        }


        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                videoMap.put("video" + (i + 1), (String) list.get(i).get("play_src"));
                videoMap.put("monitor" + (i + 1), list.get(i).get("id"));
            }
        }

        retMap.put("totalIncome", totalIncomemap);//今日收入统计
        retMap.put("inOutCarsCount", countMap);//进出车统计
        retMap.put("berthPercentData", ss);//泊位使用率
        retMap.put("confirmOrders", confirmOrders);//确认订单
        retMap.put("videoMap",videoMap);
        String result = JSON.toJSON(retMap).toString();
        return result;

    }

    @Override
    public byte[]  getConfirmPic(String eventId, long comid, String car_number) {

        logger.error("getConfirmPic from mongodb>>>>>>>>>eventId=" + eventId + ">>>>>>>comid=" + comid);
        if (eventId != null) {
            DB db = MongoClientFactory.getInstance().getMongoDBBuilder("zld");//
            //查询出mongodb中存入的对应个表名
            Map picMap = new HashMap();
            picMap = centerMonitorMapper.getPicMap(eventId,comid+"");
            String collectionName = "";
            if (picMap != null && !picMap.isEmpty()) {
                collectionName = (String) picMap.get("confirmpic_table_name");
            }
            if (collectionName == null || "".equals(collectionName) || "null".equals(collectionName)) {
                logger.error(">>>>>>>>>>>>>查询图片错误........");
                return new byte[0];
            }
            logger.error("table:" + collectionName);
            DBCollection collection = db.getCollection(collectionName);
            if (collection != null) {
                BasicDBObject document = new BasicDBObject();
                document.put("parkid", String.valueOf(comid));
                document.put("event_id", eventId);
                DBObject obj = collection.findOne(document);
                if (obj == null) {
                    logger.error("取图片错误.....");
                    return new byte[0];
                }
                byte[] content = (byte[]) obj.get("content");
                logger.error("取图片成功.....大小:" + content.length);
                return content;

            } else {
                return new byte[0];
            }
        } else {
            return new byte[0];
        }
    }

    @Override
    public void matchConfirmPic(String eventId, long comid, String carNumber,HttpServletRequest request,HttpServletResponse response) {

        logger.error("matchConfirmPic from mongodb>>>>>>>>>channelId=" + eventId + ">>>>>>>comid=" + comid + ">>>>>>>>>carnumber" + carNumber);
        //获取需要人工确认的订单事件
        List<Map<String, Object>> mactchOrderList = queryBlurOrdersByCarnumber(comid, carNumber);
        JSONArray jsonArray = new JSONArray();
        if (mactchOrderList == null || mactchOrderList.isEmpty()) {
            StringUtils.ajaxOutput(response, jsonArray.toString());
            return;
        }
        for (int i = 0; i < mactchOrderList.size(); i++) {
            System.out.println("进入获取多个图片开始进行第" + (i + 1) + "次循环的时间>>>>>>>>>>>" + new Date().getTime());
            Map<String, Object> orderMap = mactchOrderList.get(i);
            if (orderMap.get("order_id_local") == null) {
                continue;
            }
            String orderid = (String) orderMap.get("order_id_local");
            String car_number = (String) orderMap.get("car_number");
            DB db = MongoClientFactory.getInstance().getMongoDBBuilder("zld");//
            //根据编号查询出mongodb中存入的对应个表
            Map map = centerMonitorMapper.matchPicMap(orderid,comid+"");
            String collectionName = "";
            if (map != null && !map.isEmpty()) {
                collectionName = (String) map.get("carpic_table_name");
            }

            if (collectionName == null || "".equals(collectionName) || "null".equals(collectionName)) {
                logger.error(">>>>>>>>>>>>>根据车牌" + carNumber + "匹配到orderid" + orderid + "查询图片错误........");
                continue;
            }
            logger.error(">>>>>>>>>>>>>根据车牌" + carNumber + "匹配到orderid" + orderid + ">>>>>table:" + collectionName);
            DBCollection collection = db.getCollection(collectionName);
            if (collection != null) {
                BasicDBObject document = new BasicDBObject();
                document.put("parkid", String.valueOf(comid));
                document.put("orderid", orderid);
                document.put("gate", "in");
                DBObject obj = collection.findOne(document);
                if (obj == null) {
                    //AjaxUtil.ajaxOutput(response, "");
                    logger.error(">>>>>>>>>>>>>根据车牌" + carNumber + "匹配到orderid" + orderid + "取图片错误.....");
                    continue;
                }
                byte[] content = (byte[]) obj.get("content");
                System.out.println("进入获取多个图片开始第" + (i + 1) + "次循环得到图片的时间>>>>>>>>>>>" + new Date().getTime());
                logger.error(">>>>>>>>>>>>>根据车牌" + carNumber + "匹配到orderid" + orderid + "取图片成功.....大小:" + content.length);
                db.requestDone();
                try {
                    String foldPath = request.getServletContext().getRealPath("/images/monitor/");
                    File folder = new File(foldPath);
                    if (!folder.exists() || !folder.isDirectory()) {
                        folder.mkdirs();
                    }
                    InputStream in = new ByteArrayInputStream(content);
                    String filePath = request.getServletContext().getRealPath("/images/monitor/" + comid + "_" + orderid + ".jpg");
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
                    System.out.println("进入获取多个图片开始第" + (i + 1) + "次循环生成图片的时间>>>>>>>>>>>" + new Date().getTime());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("orderId", orderid);
                    jsonObject.put("carNumber", car_number);
                    jsonObject.put("picName", comid + "_" + orderid + ".jpg");
                    jsonArray.add(jsonObject);
                } catch (Exception e) {
                    // TODO: handle exception
                    logger.info(e.toString());
                }
                System.out.println("mongdb over.....");
            }
        }

        response.setHeader("Content-type", "text/html;charset=UTF-8");
        StringUtils.ajaxOutput(response, jsonArray.toString());
        return;
    }

    @Override
    public Map getSelectOrder(String comid, String carNumber) {
        Long cityid =-1L;
        Long groupid = orderMapper.getGroupIdByComId(Long.parseLong(comid));
        if(groupid!=null&&groupid>-1){
            cityid = orderMapper.getCityIdByGroupId(groupid);
        }else {
            cityid = orderMapper.getCityIdByComId(Long.parseLong(comid));
        }
        Map ordermap = centerMonitorMapper.getSelectOrder(Long.parseLong(comid),carNumber,cityid);
        return ordermap;
    }

    @Override
    public void sendMessageToPark(String orderId, String carNumber, String channel_id, String event_id, String comid, HttpServletResponse response) throws Exception{

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("comid", comid + "");
        params.put("inform_time", TimeTools.getLongMilliSeconds());
        params.put("channel_id", StringUtils.decodeUTF8(channel_id));
        params.put("order_id", orderId);
        params.put("event_id", StringUtils.decodeUTF8(event_id));
        params.put("car_number", StringUtils.decodeUTF8(carNumber));
//        params.put("action", "balanceOrderInfo");
        logger.error(params);

//        JSONObject object = JSONObject.parseObject(ret);
        String message = StringUtils.createLinkString(params);
        logger.error("balanceOrderInfo message:" + message);
//        String url = "http://127.0.0.1/zld/centermonitor.do?action=balanceOrderInfo&" + message;
        //测试代码
        String url = "http://"+CustomDefind.getValue("DOMAIN")+"/zld/centermonitor.do?action=balanceOrderInfo&"+message;
//        String url = "http://test.bolink.club/zld/centermonitor.do";
        logger.error("balanceOrderInfo url:" + url);
        String result = new HttpProxy().doGet(url);
//        String result = HttpsProxy.doPost(url, message, "utf-8", 20000, 20000);
        logger.error("去车场通知消息的返回:" + result);
        Map<String, String> retMap = new HashMap<String, String>();
        if ("1".equals(result)) {
            try {
                Thread.sleep(500);//等0.5s
                //查询未处理该eventId的手动匹配订单

                Map confirmOrder = centerMonitorMapper.getConfirmOrder(event_id,comid);
                if (confirmOrder == null) {
                    retMap.put("succsess", "1");
                    retMap.put("message", "处理成功");
                    retMap.put("img", "balSuc.png");
                } else {
                    Thread.sleep(500);//等0.5s
                    confirmOrder = centerMonitorMapper.getConfirmOrder(event_id,comid);
                    if (confirmOrder == null) {
                        retMap.put("succsess", "1");
                        retMap.put("message", "处理成功");
                        retMap.put("img", "balSuc.png");
                    } else {
                        Thread.sleep(1000);//等1s
                        if (confirmOrder == null) {
                            retMap.put("succsess", "1");
                            retMap.put("message", "处理成功");
                            retMap.put("img", "balSuc.png");
                        } else {
                            retMap.put("succsess", "0");
                            retMap.put("message", "处理失败");
                            retMap.put("img", "balFail.png");
                        }
                    }
                }
                //查询事件是否存在
            } catch (Exception e) {
                // TODO: handle exception
                logger.error(e.getMessage());
            }
        } else {
            retMap.put("succsess", "0");
            retMap.put("message", "处理失败");
            retMap.put("img", "balFail.png");
        }
        StringUtils.ajaxOutput(response,JSONObject.toJSONString(retMap));
    }

    @Override
    public void liftRodMessage(String channel_id, String channel_name, String comid, HttpServletResponse response) throws Exception{
        Map retMap = new HashMap();
        if (channel_id == null || channel_id.isEmpty()) {
            retMap.put("succsess", "0");
            retMap.put("message", "处理失败，通道号为空");
        } else {
            //先初始化抬杆状态 -1
            LiftrodInfoTb liftrodInfoTb = new LiftrodInfoTb();
            liftrodInfoTb.setState(-1L);
            LiftrodInfoTb conditions = new LiftrodInfoTb();
            conditions.setChannelId(channel_id);
            conditions.setComid(comid);
            conditions.setOperate(0L);
            int update = commonDao.updateByConditions(liftrodInfoTb,conditions);
            logger.error("liftRod 初始化抬杆状态:" + update);
            HttpProxy httpProxy = new HttpProxy();
//            String url = "http://127.0.0.1/zld/sendmsgtopark.do?action=sendliftrodmsg";
            //测试代码
            String url = "http://"+CustomDefind.getValue("DOMAIN")+"/zld/sendmsgtopark.do?action=sendliftrodmsg";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("comid", comid);
            params.put("channelName", StringUtils.decodeUTF8(channel_name));
            params.put("channelId", StringUtils.decodeUTF8(channel_id));
            params.put("operate", 0);
            String result = httpProxy.doPostTwo(url, params);
            System.out.println(result);
            if ("1".equals(result)) {
                //发送消息后查询数据库是否完成操作
                Long state = -1L;
                for (int i = 0; i < 30; i++) {
                    Thread.sleep(100);

                    Map map = centerMonitorMapper.getLiftRodInfo(channel_id,comid);

                    if (map != null && !map.isEmpty()) {
                        state = Long.valueOf(String.valueOf(map.get("state")));
                    }
                    if (state == 1) {
                        break;
                    } else {
                        continue;
                    }
                }
                if (state == 1) {
                    retMap.put("succsess", "1");
                    retMap.put("message", "处理成功");
                } else {
                    retMap.put("succsess", "0");
                    retMap.put("message", "处理失败");
                }
            } else {
                retMap.put("succsess", "0");
                retMap.put("message", "处理失败");
            }
        }
       StringUtils.ajaxOutput(response,JSONObject.toJSONString(retMap));

    }

    @Override
    public void callInform(Integer fromOther,String callerid_num, String exten,HttpServletResponse response) {
//        Map<String, Object> monMap = daService.getMap("select p.group_phone,p.park_phone, m.play_src,m.id from phone_info_tb p, monitor_info_tb m where m.id=p.monitor_id and p.tele_phone= ? and m.state= ? and p.state= ?", new Object[]{Long.parseLong(callerid_num), 1, 1});
        Map<String, Object> monMap  = centerMonitorMapper.getMonitorMap(Long.parseLong(callerid_num));
        if (monMap == null || monMap.isEmpty()) {
            StringUtils.ajaxOutput(response, "-1");
            return ;
        }
        //判断主机号性质 1-集团 0-车场
        int mainPhoneType = -1;
        if (Long.parseLong(exten) == Long.parseLong(monMap.get("group_phone").toString())) {
            mainPhoneType = 1;
        }
        if (Long.parseLong(exten) == Long.parseLong(monMap.get("park_phone").toString())) {
            mainPhoneType = 0;
        }
        logger.error("中央监控>>>>>呼入主机机号类型(1-集团 0-车场)：" + mainPhoneType + ">>>>");
        if (mainPhoneType == -1) {
            logger.error("中央监控>>>>>推送消息的主叫号>>>" + callerid_num + "和被叫号" + exten + "在云平台没有对应关系");
        } else {
            //推送弹视频的消息
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("play_src", monMap.get("play_src"));
            message.put("main_phone_type", mainPhoneType);
            message.put("id", monMap.get("id"));

            logger.error(" 中央监控>>>>>发起推送消息" + gson.toJson(message));
            Collection<ScriptSession> sessions = DWRScriptSessionListener.getScriptSessions();
            if (sessions != null && sessions.size() > 0) {//有dwr监听事件再推送消息
                Push.popCenteVideo(gson.toJson(message), sessions);
            }
            String ip ="";
            try{
                ip =  InetAddress.getLocalHost().getHostAddress();
            }catch (Exception e){

            }
            logger.error("现在服务器的地址是"+ip);
            if(fromOther==0){
                if(ip.contains("53")){//yun web1
                    //去另一台服务器推送消息
                    logger.error("现在服务器的地址是要跳转到201");
                    HttpProxy httpProxy = new HttpProxy();
                    String result = httpProxy.doGet("http://120.79.98.201/cloud/centermonitor/callinform?callerid_num="+callerid_num+"&exten="+exten+"&fromother=1");
                    logger.error("现在服务器的地址是发送结果"+result);
//                      response.sendRedirect("http://120.79.98.201/tcbcloud/monitor.do?action=callinform&callerid_num="+callerid_num+"&exten="+exten+"&fromother=1");
                }else if(ip.contains("33")){//yun web2
                    logger.error("现在服务器的地址是要跳转到182");
                    HttpProxy httpProxy = new HttpProxy();
                    String result = httpProxy.doGet("http://120.76.158.182/cloud/centermonitor/callinform?callerid_num="+callerid_num+"&exten="+exten+"&fromother=1");
                    logger.error("现在服务器的地址是发送结果"+result);
//                        response.sendRedirect("http://120.76.158.182/tcbcloud/monitor.do?action=callinform&callerid_num="+callerid_num+"&exten="+exten+"&fromother=1");
                }
            }else{
                logger.error("=====>>>>>>已经跳过一次了 再跳就多了");
            }
        }
    }

    @Override
    public Map qryChannelByMonitId(Long monitor_id) {
        Map liftmap = centerMonitorMapper.qryChannelByMonitId(monitor_id);
        return liftmap;
    }

    private List<Map<String,Object>> queryBlurOrdersByCarnumber(long comid, String carNumber) {

        Long groupid = orderMapper.getGroupIdByComId(comid);
        Long cityid = -1L;
        if(groupid!=null&&groupid>-1){
            cityid = orderMapper.getCityIdByGroupId(groupid);
        }else {
            cityid = orderMapper.getCityIdByComId(comid);
        }

        System.out.println("进来获取集合");
        List<Object> params = new ArrayList<Object>();
        params.add(comid);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //匹配除省份，无牌车首字符可能不是中文(c >= 0x4E00 &&  c <= 0x9FA5)
        char firstChar = carNumber.charAt(0);
        if (firstChar > 0x9FA5 || firstChar < 0x4E00) {
            carNumber = "无" + carNumber;
        }

        List<String> carNumberList = new ArrayList<>();
        carNumberList.add(carNumber.substring(1));

        list = centerMonitorMapper.getCarByNameLike(comid,carNumberList,cityid);

        if (list == null || list.size() == 0) {
            // 2 模糊匹配除去两位
            carNumberList.clear();
            carNumberList.add(carNumber.substring(1, carNumber.length() - 1));
            carNumberList.add(carNumber.substring(2));
            list = centerMonitorMapper.getCarByNameLike(comid,carNumberList,cityid);

            if (list == null || list.size() == 0) {
                //3  模糊匹配除去三位
                carNumber = carNumber.substring(1);
                carNumberList.clear();
                carNumberList.add(carNumber.substring(1, carNumber.length() - 2));
                carNumberList.add(carNumber.substring(2, carNumber.length() - 1));
                carNumberList.add(carNumber.substring(3));
                list = centerMonitorMapper.getCarByNameLike(comid,carNumberList,cityid);

                if (list == null || list.size() == 0) {
                    //4 模糊匹配除去四位
                    carNumber = carNumber.substring(1);
                    carNumberList.clear();
                    carNumberList.add(carNumber.substring(1, carNumber.length() - 3));
                    carNumberList.add(carNumber.substring(2, carNumber.length() - 2));
                    carNumberList.add(carNumber.substring(3, carNumber.length() - 1));
                    carNumberList.add(carNumber.substring(4, carNumber.length()));
                    list = centerMonitorMapper.getCarByNameLike(comid,carNumberList,cityid);

                }
            }
        }
        return list;

    }
}
