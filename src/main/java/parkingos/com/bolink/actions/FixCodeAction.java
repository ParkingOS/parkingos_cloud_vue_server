package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.FixCodeTb;
import parkingos.com.bolink.service.FixCodeService;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.utils.CustomDefind;
import parkingos.com.bolink.utils.QrCodeUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/fixcode")
public class FixCodeAction {

    Logger logger = Logger.getLogger(FixCodeAction.class);

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
    public String addRole(HttpServletRequest request, HttpServletResponse resp){

        Integer type = RequestUtil.getInteger(request,"type",1);
        Long shopid = RequestUtil.getLong(request,"shopid",-1L);
        Integer state = RequestUtil.getInteger(request,"state",0);
//        Integer moneyLimit = RequestUtil.getInteger(request,"money_limit",0);
        Integer freeLimit = RequestUtil.getInteger(request,"free_limit",0);
        Integer amountLimit = RequestUtil.getInteger(request,"amount_limit",0);
        Integer validite_time = RequestUtil.getInteger(request,"validite_time",24);
//        Integer amount = RequestUtil.getInteger(request,"amount",0);

        //根据shopid获得该商户的用券单位
        Integer ticketUnit = shopAcccountService.getTicketUnitById(shopid);
        System.out.println("该商户的优惠单位是:"+ticketUnit);


        Long btime = System.currentTimeMillis()/1000;
        //截止有效时间
        Long etime =  btime + validite_time*60*60;
        Long id = fixCodeService.getId();
        Long ids[] = new Long[]{id};
        String[] code = StringUtils.getGRCode(ids);

        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setShopId(shopid);
        fixCodeTb.setId(id);
        fixCodeTb.setState(state);
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
        fixCodeTb.setCreateTime(btime);
        fixCodeTb.setEndTime(etime);
        fixCodeTb.setCodeSrc(CustomDefind.getValue("FIXCODEURL")+code[0]);


        JSONObject result = fixCodeService.addFixCode(fixCodeTb);

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

        if(result.get("state")==1){
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

}
