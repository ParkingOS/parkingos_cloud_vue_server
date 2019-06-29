package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.enums.AddValueServerEnum;
import parkingos.com.bolink.models.FixCodeTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.service.FixCodeService;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/fixcode")
public class FixCodeAction {

    Logger logger = LoggerFactory.getLogger(FixCodeAction.class);

    @Autowired
    private FixCodeService fixCodeService;
    @Autowired
    private ShopAcccountService shopAcccountService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = fixCodeService.selectResultByConditions(reqParameterMap);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

//    /**
//     *
//     * 增加固态二维码
//     */
    @RequestMapping(value = "/add")
    public String addRole(HttpServletRequest request, HttpServletResponse resp) throws Exception{

        JSONObject result = new JSONObject();

        String name = RequestUtil.getString(request,"name");
        Integer type = RequestUtil.getInteger(request,"type",1);
        Long shopid = RequestUtil.getLong(request,"shopid",-1L);
        Integer state = RequestUtil.getInteger(request,"state",0);
//        Integer moneyLimit = RequestUtil.getInteger(request,"money_limit",0);
        Integer freeLimit = RequestUtil.getInteger(request,"free_limit",0);
        Integer amountLimit = RequestUtil.getInteger(request,"amount_limit",0);
        String validiteTime= RequestUtil.getString(request,"validite_time");

        String begin_time = RequestUtil.processParams(request, "begin_time");
        //时区问题  进行转换
        if(!"".equals(begin_time)) {
            begin_time = begin_time.replace("Z", " UTC");//注意是空格+UTC
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
            Date d = format.parse(begin_time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            begin_time = sdf.format(d);
        }

        String end_time = RequestUtil.processParams(request, "end_time");
        if(!"".equals(end_time)) {
            end_time = end_time.replace("Z", " UTC");//注意是空格+UTC
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
            Date d = format.parse(end_time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end_time = sdf.format(d);
        }

        Long uin = RequestUtil.getLong(request,"loginuin",-1L);
        Integer validite_time = 24;
//        if(!Check.isNumber(validiteTime)){
//            result.put("state",0);
//            result.put("msg","请输入正确的有效期");
//            StringUtils.ajaxOutput(resp,result.toJSONString());
//            return null;
//        }else{
//            validite_time = Integer.parseInt(validiteTime);
//        }

        //可用的分段时间
        String timeInuse0 = RequestUtil.getString(request,"time_inuse_start");
        String timeInuse1 = RequestUtil.getString(request,"time_inuse_end");

        String timeInuse="00:00-23:59";
        if(!"".equals(timeInuse0)&&!"".equals(timeInuse1)) {
            timeInuse = timeInuse0 + "-" + timeInuse1;
        }

        //根据shopid获得该商户的用券单位
        ShopTb shopTb  =shopAcccountService.getShopByid(shopid);

        Integer ticketUnit = shopTb.getTicketUnit();
        System.out.println("该商户的优惠单位是:"+ticketUnit);


        if(type==1){//减免券
            if(ticketUnit==1||ticketUnit==2||ticketUnit==3){//时长减免
                if(amountLimit*freeLimit>shopTb.getTicketLimit()){//固定码额度大于商户总额度
                    result.put("state",0);
                    result.put("msg","商户余额不足，生成固定码失败");
                    StringUtils.ajaxOutput(resp,result.toJSONString());
                    return null;
                }
            }else if(ticketUnit==4){//金额减免
                if(amountLimit*freeLimit>shopTb.getTicketMoney()){//固定码额度大于商户总额度
                    result.put("state",0);
                    result.put("msg","商户余额不足，生成固定码失败");
                    StringUtils.ajaxOutput(resp,result.toJSONString());
                    return null;
                }
            }else if(ticketUnit==5){
                if(freeLimit>shopTb.getTicketMoney()){//固定码额度大于商户总额度
                    result.put("state",0);
                    result.put("msg","商户余额不足，生成固定码失败");
                    StringUtils.ajaxOutput(resp,result.toJSONString());
                    return null;
                }
                //生成的券是折扣券
                type=3;
            }
        }else if(type==2){//全免券
            if(freeLimit>shopTb.getTicketfreeLimit()){
                result.put("state",0);
                result.put("msg","商户余额不足，生成固定码失败");
                StringUtils.ajaxOutput(resp,result.toJSONString());
                return null;
            }
        }


        Long btime = System.currentTimeMillis()/1000;
        if(!"".equals(begin_time)){
            btime=TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(begin_time);
        }
        Long etime =  btime + validite_time*60*60;
        if(!"".equals(end_time)){
            etime=TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(end_time);
        }


        if(btime>=etime){
            result.put("state",0);
            result.put("msg","有效期错误!");
            StringUtils.ajaxOutput(resp,result.toJSONString());
            return null;
        }
        //截止有效时间

        Long id = fixCodeService.getId();
        Long ids[] = new Long[]{id};
        String[] code = StringUtils.getGRCode(ids);

        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setShopId(shopid);
        fixCodeTb.setId(id);
        fixCodeTb.setTimeInuse(timeInuse);
        fixCodeTb.setName(name);
        fixCodeTb.setValiditeTime(validite_time);
        fixCodeTb.setState(state);
        fixCodeTb.setUin(uin);
        fixCodeTb.setFreeLimit(freeLimit);//总张数
        if(ticketUnit==1||ticketUnit==2||ticketUnit==3){//时长减免
            fixCodeTb.setTimeLimit(amountLimit*freeLimit);
        }else if(ticketUnit==4){//金额减免
            fixCodeTb.setMoneyLimit(amountLimit*freeLimit);
        }
        fixCodeTb.setAmount(amountLimit);//单张额度
        //fixCodeTb.setTimeLimit(timeLimit);
        fixCodeTb.setCode(code[0]);
//        fixCodeTb.setAmount(amount);
        //fixCodeTb.setMoneyLimit(moneyLimit);
        fixCodeTb.setType(type);
        fixCodeTb.setCreateTime(System.currentTimeMillis()/1000);
        fixCodeTb.setBeginTime(btime);
        fixCodeTb.setEndTime(etime);
        fixCodeTb.setCodeSrc(CustomDefind.getValue("FIXCODEURL")+code[0]);


        result = fixCodeService.addFixCode(fixCodeTb);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
//
//    /*
//    *
//    * 编辑固定码
//    *
//    * */
    @RequestMapping(value = "/edit")
    public String editRole(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);
        Integer state = RequestUtil.getInteger(request,"state",0);

        logger.info("修改是否可用:"+id+"~~~:"+state);
        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setId(id);
        fixCodeTb.setState(state);

        JSONObject result = fixCodeService.updateRole(fixCodeTb);


        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/useticket")
    public String userTicket(HttpServletRequest request, HttpServletResponse resp){

        String code = RequestUtil.getString(request,"code");

        logger.info("固定码扫码使用优惠券:"+code);

        JSONObject result = fixCodeService.userTicket(code);

        if((int)result.get("state")==1){
            try{
                logger.info("扫码跳转用券路径"+result.getString("ticketurl"));
                resp.sendRedirect(result.getString("ticketurl"));
            }
            catch (Exception e){
                logger.info("扫码跳转用券出现异常");
            }

        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }



    @RequestMapping(value = "/downloadCode")
    public String downloadCode(HttpServletRequest request, HttpServletResponse resp){

        String filename = "FixCode";

        String url = RequestUtil.getString(request,"url");
        logger.info("下载二维码图片:"+url);
        String serverPath = request.getSession().getServletContext().getRealPath("/resource/images/");
        logger.info("下载二维码图片serverPath:" + serverPath);
        try {
            QrCodeUtil.generateQRCode(url, filename, serverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QrCodeUtil.downAllFile(request,resp,"FixCode");
        return null;
    }

    @RequestMapping(value = "/editAndPwd")
    public String editAndPwd(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request,"id",-1L);
        Integer state = RequestUtil.getInteger(request,"state",0);
        String password = RequestUtil.getString(request,"password");
        Integer usePwd = RequestUtil.getInteger(request,"pwd_state",-1);
        logger.info("修改是否可用:"+id+"~~~:"+state+"~~"+password+"~~"+usePwd);
        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setId(id);
        fixCodeTb.setState(state);
        fixCodeTb.setUsePwd(usePwd);
        fixCodeTb.setPassWord(password);

        JSONObject result = fixCodeService.updateRole(fixCodeTb);


        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    /*
    * 公众号设置密码
    *
    * */

    @RequestMapping(value = "/setpwd")
    public String setPwd(HttpServletRequest request, HttpServletResponse resp){
        JSONObject result = new JSONObject();
        Long id = RequestUtil.getLong(request,"id",-1L);
        String password = RequestUtil.getString(request,"password");
        Integer usePwd = RequestUtil.getInteger(request,"pwd_state",-1);
        logger.info("===>>>setpwd:"+password+"~"+usePwd);
        if(usePwd==1){
           if(password.length()!=4||!Check.isNumber(password)){
               result.put("state",0);
               result.put("msg","请输入4位数字密码!");
               StringUtils.ajaxOutput(resp,result.toJSONString());
               return null;
           }
        }
        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setId(id);
        fixCodeTb.setPassWord(password);
        fixCodeTb.setUsePwd(usePwd);
        result = fixCodeService.setPwd(fixCodeTb);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /*
    * 公众号设置
    *
    * */
    @RequestMapping(value = "/public")
    public String setPublic(HttpServletRequest request, HttpServletResponse resp){
        JSONObject result = new JSONObject();
        Long id = RequestUtil.getLong(request,"id",-1L);
        String appid = RequestUtil.getString(request,"appid");
        String secret = RequestUtil.getString(request,"secret");
        String concernAddress = RequestUtil.getString(request,"concern_address");
        Integer publicState = RequestUtil.getInteger(request,"public_state",0);
        if(appid.length()!=18){
            result.put("state",0);
            result.put("msg","请输入正确的appid");
            StringUtils.ajaxOutput(resp,result.toJSONString());
            return null;
        }
        if(secret.length()!=32){
            result.put("state",0);
            result.put("msg","请输入正确的secret");
            StringUtils.ajaxOutput(resp,result.toJSONString());
            return null;
        }
        ShopTb shopTb = new ShopTb();
        shopTb.setId(id);
        shopTb.setAppid(appid);
        shopTb.setSecret(secret);
        shopTb.setConcernAddress(concernAddress);
        shopTb.setPublicState(publicState);
        result = fixCodeService.setPublic(shopTb);
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/tobuy")
    public String toBuy(HttpServletRequest request, HttpServletResponse resp){

        //需要支付金额
        Double money = RequestUtil.getDouble(request,"money",0.0);
        //生成流水号
        String seed = (new Random().nextDouble() + "").substring(2, 9);
        String tradeNo = AddValueServerEnum.SHOPAPP.type+System.currentTimeMillis() + seed;
        Long park_id = RequestUtil.getLong(request,"comid",-1L);
        Long shop_id = RequestUtil.getLong(request,"shop_id",-1L);
        logger.info("buy fixcode app:"+shop_id+"~~"+money);
        JSONObject result = fixCodeService.buyApp(money,tradeNo,park_id,shop_id);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/getofficialstate")
    public String getOfficialState(HttpServletRequest request, HttpServletResponse resp){
        Long shopId = RequestUtil.getLong(request,"shop_id",-1L);
        logger.info("===>>>>get official state:"+shopId);
        int result = fixCodeService.getOfficialState(shopId);
        StringUtils.ajaxOutput(resp, result+"");
        return null;
    }

    @RequestMapping(value = "/getbuytrade")
    public String getBuyTrade(HttpServletRequest request,HttpServletResponse resp) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = fixCodeService.getBuyTrade(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


}
