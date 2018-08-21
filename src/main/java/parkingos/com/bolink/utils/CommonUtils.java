package parkingos.com.bolink.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.qo.PageOrderConfig;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonUtils<T> {
    Logger logger = Logger.getLogger(CommonUtils.class);
    @Autowired
    CommonDao commonDao;

    /**
     * 获取下发数据的TCP通道
     *
     * @param comid
     * @return
     */
    public String getChannel(String comid) {
       /* String channelPass = "";
        ParkTokenTb tokenTb = new ParkTokenTb();
        tokenTb.setParkId(comid);
        tokenTb = commonDao.selectObjectBySelective(tokenTb);
//		Map parkMap = null;// daService.getMap("select * from park_token_tb where park_id=? order by id desc ", new Object[]{comid});
        if (tokenTb != null ) {
            String localId = tokenTb.getLocalId();
            if (!Check.isEmpty(localId)) {
                channelPass = comid + "_" + localId;
            }
        } else {
            channelPass = comid;
        }
        logger.error("sdk comid:" + channelPass);
        return channelPass;*/
        String channelPass = "";
        ParkTokenTb tokenTb = new ParkTokenTb();
        tokenTb.setParkId(comid);
        PageOrderConfig orderConfig = new PageOrderConfig();
        orderConfig.setOrderInfo("id","desc");
        orderConfig.setPageInfo(1,null);
//        tokenTb.setOrderField("id");
//        tokenTb.setOrderType("desc");
//        tokenTb.setPageSize(0);
        List<ParkTokenTb> tokenTbs = commonDao.selectListByConditions(tokenTb,orderConfig);//.selectObjectBySelective(tokenTb);
        //Map parkMap = dataBaseService.getMap("select * from park_token_tb where park_id=?", new Object[]{comid});
        if(tokenTbs!=null&&!tokenTbs.isEmpty())
            tokenTb = tokenTbs.get(0);
        if(tokenTb != null ){
            String localId =tokenTb.getLocalId();// String.valueOf(parkMap.get("local_id"));
            if(!Check.isEmpty(localId)){
                channelPass += comid+"_"+localId;
            }
        }else{
            channelPass = comid;
        }
        return channelPass;
    }
//    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = { new MyX509TrustManager() };
//            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//            sslContext.init(null, tm, new java.security.SecureRandom());
//            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
//            URL url = new URL(requestUrl);
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setSSLSocketFactory(ssf);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            conn.setRequestMethod(requestMethod);
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//            // 当outputStr不为null时向输出流写数据
//            if (null != outputStr) {
//                OutputStream outputStream = conn.getOutputStream();
//                // 注意编码格式
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//            // 从输入流读取返回内容
//            InputStream inputStream = conn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            // 释放资源
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            inputStream = null;
//            conn.disconnect();
//            return buffer.toString();
//        } catch (ConnectException ce) {
//            ce.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//


    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] arg0,
                    String arg1)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] arg0,
                    String arg1)
                    throws java.security.cert.CertificateException {
            }
        }
        };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取下发数据的TCP通道
     *
     * @param comid
     * @return
     */
    public ParkTokenTb getChannel(Long comid) {
        ParkTokenTb tokenTb = new ParkTokenTb();
        tokenTb.setParkId(comid + "");
        tokenTb.setReceiveCloud(1);
        List<ParkTokenTb> tokens = commonDao.selectListByConditions(tokenTb);
        logger.info(tokens);
        if (tokens != null && !tokens.isEmpty()) {
            return tokens.get(0);
        }
        return null;
    }

    public boolean doSendMessage(JSONObject message, ParkTokenTb parkTokenTb) {
        logger.error(parkTokenTb);
        String ip = parkTokenTb.getServerIp();//(String)parkTokenTb.get("server_ip");
        String localId = parkTokenTb.getLocalId();// (String)parkTokenTb.get("local_id");
        if (Check.isEmpty(localId))
            localId = parkTokenTb.getParkId();
        else {
            localId = parkTokenTb.getParkId() + "_" + parkTokenTb.getLocalId();// parkTokenTb.get("park_id")+"_"+localId;
        }
        logger.error(localId);
        JSONObject jsonObject = JSONObject.parseObject("{}");
        jsonObject.put("channelid", localId);
        jsonObject.put("data",message);
        String url = "http://" + ip + ":8080/zld/sendmesgtopark";
        logger.error(url);
        logger.error(jsonObject);
        String ret = new HttpProxy().doHeadPost(url, StringUtils.encodeUTF8(jsonObject.toString()));
        logger.error(ret);
        if (ret != null&&!"".equals(ret)) {
            JSONObject result = JSONObject.parseObject(ret);
            if (result.containsKey("result"))
                return result.getBooleanValue("result");
        }
        return false;
    }


    public boolean sendMessage(T t, Long comid, Long tableId, Integer operate) {
        logger.info("开始发送数据。。。。。。。。");
        String tableName  = t.getClass().getName();
        tableName = camel2Underline(tableName.substring(tableName.lastIndexOf(".")));
//        operate += 1;
        ParkTokenTb parkTokenTb = getChannel(comid);
        String result="0";
        logger.info("开始发送数据。。。。。。。。"+tableName+"~~~"+parkTokenTb);
        if(parkTokenTb==null){
            logger.error("sdk 没有在线，第一次发送失败");
            return false;
        }
        if (tableName.equals("carower_product")) {
            result= sendcardMember( tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送月卡会员信息结果" + result);
        } else if (tableName.equals("price_tb")) {
             result = sendPriceInfo( tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送价格信息结果" + result);
        } else if (tableName.equals("product_package_tb")) {
             result = sendProductPackageInfo(tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送月卡套餐信息结果" + result);
        } else if (tableName.equals("user_info_tb")) {
             result = sendUserInfo(tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送收费员信息结果" + result);
        } else if (tableName.equals("car_type_tb")) {
             result = sendCarTypeInfo( tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送车型信息结果" + result);
        } else if (tableName.equals("zld_black_tb")) {
             result = sendBlackUser(tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送黑名单信息结果" + result);
        } else if (tableName.equals("card_renew_tb")) {
             result = sendCardReNewInfo(tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送月卡续费信息结果" + result);
        }  else if(tableName.equals("com_pass_tb")){
             result = sendComPassInfo(tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送通道结果" + result);
        } else if(tableName.equals("visitor_tb")){
             result = sendVisitorInfo(tableId, comid, operate,parkTokenTb);
            logger.error(">>>>>>>>>>>>>>>>>>>>>发送访客结果" + result);
        }
        if("1".equals(result)){
            return true;
        }
        return false;
    }

    public static String camel2Underline(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern= Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString().toLowerCase();
    }

    private String sendVisitorInfo(Long tableId, Long comid, Integer operate, ParkTokenTb parkTokenTb) {
        VisitorTb visitor = new VisitorTb();
        visitor.setId(tableId);
        visitor = (VisitorTb)commonDao.selectObjectByConditions(visitor);
        JSONObject jsonSend = new JSONObject();
        String result = "0";
        if (visitor != null ) {
            jsonSend.put("visitor_id", visitor.getId());
            jsonSend.put("car_number", visitor.getCarNumber());//black.getBlackUuid());
            jsonSend.put("begin_time", visitor.getBeginTime());//black.getCarNumber());
            jsonSend.put("end_time", visitor.getEndTime());//black.getOperator());
            jsonSend.put("mobile", visitor.getMobile());//black.getCtime());
            jsonSend.put("remark", visitor.getRemark());//black.getRemark());
            jsonSend.put("operate_type", operate);
            jsonSend.put("state", visitor.getState());
        } else {
            logger.error(">>>>>>>>>>>>>没有查询到访客");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "visitor_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);

        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }

    /**
     * 下发黑名单
     * @param tableId 表数据编号
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendBlackUser(Long tableId, Long comid, Integer operate,ParkTokenTb parkTokenTb) {
//        Map<String,Object> black = pgOnlyReadService.getMap("select * from zld_black_tb where id = ? ", new Object[]{tableId});
        ZldBlackTb black = new ZldBlackTb();
        black.setId(tableId);
        black = (ZldBlackTb)commonDao.selectObjectByConditions(black);
        //定义下传数据的json对象
        JSONObject jsonSend = new JSONObject();
        //操作类型
        String result = "0";
        //查询出对应的需要下传的数据
        if (black != null ) {
            jsonSend.put("black_uuid", black.getBlackUuid());//black.getBlackUuid());
            jsonSend.put("car_number", black.getCarNumber());//black.getCarNumber());
            jsonSend.put("operator", black.getOperator());//black.getOperator());
            jsonSend.put("create_time", black.getCtime());//black.getCtime());
            jsonSend.put("resume", black.getRemark());//black.getRemark());
            jsonSend.put("operate_type", operate);
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "blackuser_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);

        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }
    /**
     * 下发月卡充值记录
     * @param tableId 表数据编号
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendCardReNewInfo(Long tableId, Long comid, Integer operate,ParkTokenTb parkTokenTb) {
//        Map<String,Object> renewTb = pgOnlyReadService.getMap("select * from card_renew_tb where id = ? ", new Object[]{tableId});
        CardRenewTb renewTb = new CardRenewTb();
        renewTb.setId(tableId.intValue());
        renewTb = (CardRenewTb)commonDao.selectObjectByConditions(renewTb);
        //定义下传数据的json对象
        JSONObject jsonSend = new JSONObject();
        //操作类型
        String result = "0";
        //查询出对应的需要下传的数据
        if (renewTb != null ) {
            jsonSend.put("trade_no",renewTb.getTradeNo());//renewTb.getTradeNo() );
            jsonSend.put("card_id",renewTb.getCardId());//renewTb.getCardId());
            jsonSend.put("pay_time", renewTb.getPayTime());//renewTb.getPayTime());
            jsonSend.put("amount_receivable",renewTb.getAmountReceivable());
            jsonSend.put("pay_type",renewTb.getPayType());
            jsonSend.put("collector",renewTb.getCollector());
            jsonSend.put("buy_month",renewTb.getBuyMonth());
            jsonSend.put("car_number",renewTb.getCarNumber());
            jsonSend.put("user_id",renewTb.getUserId());
            jsonSend.put("resume",renewTb.getResume());
            jsonSend.put("start_time",renewTb.getStartTime());
            jsonSend.put("operate_type", operate);
            jsonSend.put("amount_pay",renewTb.getAmountPay());
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "month_pay_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);
        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }

    /**
     * 下发车型数据
     * @param tableId 表数据编号
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendCarTypeInfo(Long tableId, Long comid, Integer operate,ParkTokenTb parkTokenTb) {
//        Map<String,Object> carTypeTb = pgOnlyReadService.getMap("select * from car_type_tb where id = ? ", new Object[]{tableId});
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(tableId);
        carTypeTb = (CarTypeTb)commonDao.selectObjectByConditions(carTypeTb);
        //定义下传数据的json对象
        JSONObject jsonSend = new JSONObject();
        //操作类型
        String result = "0";
        //查询出对应的需要下传的数据
        if (carTypeTb != null ) {
            jsonSend.put("car_type_id",carTypeTb.getCartypeId());
            jsonSend.put("name", carTypeTb.getName());
            jsonSend.put("create_time",carTypeTb.getCreateTime());
            jsonSend.put("sort",carTypeTb.getSort());
            jsonSend.put("update_time", carTypeTb.getUpdateTime());
            jsonSend.put("operate_type", operate);
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "car_type_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);
        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }



    /**
     * 将云后台修改的收费员信息发送到收费系统

     * @param tableId 表主键
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendUserInfo(Long tableId, Long comid,
                                Integer operate,ParkTokenTb parkTokenTb) {
        String result = "0";
        JSONObject jsonSend = new JSONObject();
        //操作类型
//        Map<String,Object> userInfoTb = pgOnlyReadService.getMap("select * from user_info_tb where id = ? ",
//                new Object[]{tableId});
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(tableId);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);

        if (userInfoTb != null ) {
            if (operate == 2) {
                jsonSend.put("update_time", userInfoTb.getUpdateTime());
            }
            Long sex = userInfoTb.getSex()==null?1L:userInfoTb.getSex();
            if(sex==0||sex==1){
                jsonSend.put("sex",sex);
            }
            jsonSend.put("user_id", userInfoTb.getUserId());
            jsonSend.put("name", userInfoTb.getNickname());
            jsonSend.put("create_time",userInfoTb.getRegTime());
            jsonSend.put("operate_type", operate);
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "collector_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);
        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }

    /**
     * 将云后台修改的月卡套餐信息发送到车场收费系统

     * @param tableId 表主键
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendProductPackageInfo(Long tableId,Long comid, Integer operate,ParkTokenTb parkTokenTb) {
        String result = "0";
        //定义下传数据的json对象
        JSONObject jsonSend = new JSONObject();
        //操作类型
//        Map<String,Object> packageTb = commonDao.getObjectBySql("select * from product_package_tb where id = ? ");
        ProductPackageTb packageTb = new ProductPackageTb();
        packageTb.setId(tableId);
        packageTb = (ProductPackageTb)commonDao.selectObjectByConditions(packageTb);
        if (packageTb != null ) {
            //修改时间
            jsonSend.put("update_time",packageTb.getUpdateTime());
            String carTypdId = packageTb.getCarTypeId();
            if(Check.isLong(carTypdId)){
                carTypdId = getCarTypd(Long.valueOf(carTypdId));
            }
            jsonSend.put("car_type",carTypdId);
            jsonSend.put("package_id", packageTb.getCardId());
            jsonSend.put("name",packageTb.getpName());
            jsonSend.put("create_time", packageTb.getCreateTime());
            jsonSend.put("describe",packageTb.getDescribe());
            jsonSend.put("price", packageTb.getPrice());
            jsonSend.put("update_time",packageTb.getUpdateTime());
            jsonSend.put("period",packageTb.getPeriod());
            jsonSend.put("operate_type", operate);
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "month_card_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);
        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }

    /**
     * 将云后台修改的价格息发送到车场收费系统

     * @param tableId 表主键
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendPriceInfo(Long tableId, Long comid,
                                 Integer operate,ParkTokenTb parkTokenTb) {
        String result = "0";
        JSONObject jsonSend = new JSONObject();
        //操作类型
        PriceTb priceTb = new PriceTb();
        priceTb.setId(tableId);
        priceTb = (PriceTb)commonDao.selectObjectByConditions(priceTb);
        if (priceTb != null ) {
            //修改时间
            jsonSend.put("update_time", priceTb.getUpdateTime());
            jsonSend.put("price_id",priceTb.getPriceId());
            jsonSend.put("car_type",priceTb.getCarTypeZh());
            jsonSend.put("create_time",priceTb.getCreateTime());
            jsonSend.put("describe",priceTb.getDescribe());
            jsonSend.put("operate_type", operate);
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }

        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "price_sync");
        jsonMesg.put("data", jsonSend);
        logger.error(jsonMesg);
        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }

    /**
     * 将云后台修改的月卡会员的信息发送到车场收费系统

     * @param tableId 表主键
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendcardMember(Long tableId, Long comid,
                                  Integer operate,ParkTokenTb parkTokenTb) {
        String result = "0";
        JSONObject jsonSend = new JSONObject();
        //操作类型
//        Map<String,Object> product = pgOnlyReadService.getMap("select * from carower_product where id = ? ",
//                new Object[]{tableId});
        CarowerProduct product = new CarowerProduct();
        product.setId(tableId);
        product = (CarowerProduct)commonDao.selectObjectByConditions(product);
        if (product != null ) {
            logger.error(">>>>>>>查询需要同步的月卡会员信息：" + product);
            //根据文档说明下传具体的数据
            Long pid = product.getPid();
            jsonSend.put("pid", getProudetId(pid));

            jsonSend.put("card_id", product.getCardId());
            jsonSend.put("update_time",  product.getUpdateTime());
            jsonSend.put("create_time",  product.getCreateTime());
            jsonSend.put("begin_time",  product.getbTime());
            jsonSend.put("end_time", product.geteTime());
            jsonSend.put("name",  product.getName());
            jsonSend.put("car_number",  product.getCarNumber());
            String carTypeId=getCarTypd(product.getCarTypeId());
            jsonSend.put("car_type_id", carTypeId);
            jsonSend.put("limit_day_type",  product.getLimitDayType());
            jsonSend.put("price", StringUtils.formatDouble(product.getActTotal()));
            jsonSend.put("tel", product.getMobile());
            jsonSend.put("remark",product.getRemark());
            jsonSend.put("p_lot",  product.getpLot()==null?"":product.getpLot());
            jsonSend.put("amount_receivable", StringUtils.formatDouble(product.getTotal()));
            jsonSend.put("operate_type", operate);
            logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>传输的数据内容为：" + jsonSend);
        } else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }
        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "month_member_sync");
        jsonMesg.put("data", jsonSend);
//		jsonSend.put("service_name", "month_member_sync");

        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }

    /**
     * 将云后台修改的通道的信息发送到车场系统

     * @param tableId 表主键
     * @param comid 车场编号
     * @param operate 操作
     * @return 操作结果
     */
    private String sendComPassInfo(Long tableId, Long comid,
                                   Integer operate,ParkTokenTb parkTokenTb){
        String result = "0";
//        Map<String,Object> comPassTb = pgOnlyReadService.getMap("select * from com_pass_tb where id = ? ",
//                new Object[]{tableId});
        ComPassTb comPassTb = new ComPassTb();
        comPassTb.setId(tableId);
        comPassTb = (ComPassTb)commonDao.selectObjectByConditions(comPassTb);

        JSONObject jsonSend = new JSONObject();
        if(comPassTb!=null){
            jsonSend.put("passname",comPassTb.getPassname());
            jsonSend.put("passtype",comPassTb.getPasstype());
            jsonSend.put("operate_type",operate);
            jsonSend.put("channel_id",comPassTb.getChannelId());//comPassTb.getparkTokenTb());

        }else {
            logger.error(">>>>>>>>>>>>>没查到对应的需要下传的信息，可能是删除操作");
            return result;
        }

        JSONObject jsonMesg = new JSONObject();
        jsonMesg.put("service_name", "gate_sync");
        jsonMesg.put("data", jsonSend);
        boolean isSend = doSendMessage(jsonMesg,parkTokenTb);
        logger.error(">>>>>>>>>>>>>>云端发送数据到停车收费系统结果：" + isSend);
        if (isSend) {
            result = "1";
        }
        return result;
    }




    private String getCarTypd(Long id){
        if(Check.isEmpty(id+""))
            return id+"";
        CarTypeTb typeTb = new CarTypeTb();
        typeTb.setId(id);
        typeTb = (CarTypeTb)commonDao.selectObjectByConditions(typeTb);
        if(typeTb!=null&&typeTb.getCartypeId()!=null)
            return typeTb.getCartypeId();
        return id+"";
    }

    private String getProudetId(Long id){
        if(Check.isEmpty(id+""))
            return id+"";
        ProductPackageTb packageTb = new ProductPackageTb();
        packageTb.setId(id);
        packageTb = (ProductPackageTb)commonDao.selectObjectByConditions(packageTb);
        if(packageTb!=null&&packageTb.getCardId()!=null)
            return packageTb.getCardId();
        return id+"";
    }


}
