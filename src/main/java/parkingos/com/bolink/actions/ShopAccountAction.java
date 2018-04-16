package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/shopaccount")
public class ShopAccountAction {

    Logger logger = Logger.getLogger( ShopAccountAction.class );
    @Autowired
    private ShopAcccountService shopAcccountService;

    @RequestMapping("/quickquery")
    public String addMoney(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );
        logger.info( reqParameterMap );
        JSONObject result = shopAcccountService.selectResultByConditions( reqParameterMap );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result.toString() );
        return null;
    }

    @RequestMapping("/shopinfo")
    public String getShopInfoByid(HttpServletRequest request, HttpServletResponse response) {
        Long shopid = RequestUtil.getLong(request,"id",-1L);
        System.out.println("商户后台获得商户id:"+shopid);
        Map<String, Object> shopinfo = shopAcccountService.getShopInfoByid(shopid);
        System.out.println("商户后台获得商户id:"+shopinfo);
        if(shopinfo!=null){
            StringUtils.ajaxOutput(response,JSONObject.toJSONString(shopinfo));
            return null;
        }
        return null;
    }


    @RequestMapping("/edit")
    public String editShopInfoById(HttpServletRequest request, HttpServletResponse response) {
        Long id = RequestUtil.getLong(request,"id",-1L);
        String name = RequestUtil.getString(request,"name");
//        String phone = RequestUtil.getString(request,"phone");
        String address = RequestUtil.getString(request,"address");
        System.out.println("进入更改商户action"+id+name+address);

        ShopTb shopTb = new ShopTb();
        if(id!=-1){
            shopTb.setId(id);
            shopTb.setAddress(address);
            shopTb.setName(name);
        }
        int result = shopAcccountService.updateShopById(shopTb);
        StringUtils.ajaxOutput(response,"{\"state\":"+result+"}");
        return null;
    }



    @RequestMapping("/infoedit")
    public String editShopById(HttpServletRequest request, HttpServletResponse response) {
        Long id = RequestUtil.getLong(request,"id",-1L);
        Integer validiteTime = RequestUtil.getInteger(request,"validite_time",0);
        String defaultLimit = RequestUtil.getString(request,"default_limit");
        Integer handInputEnable = RequestUtil.getInteger(request,"hand_input_enable",-1);
        System.out.println("进入更改商户action"+id+defaultLimit+validiteTime+handInputEnable);

        ShopTb shopTb = new ShopTb();
        if(id!=-1){
            shopTb.setId(id);
            shopTb.setValiditeTime(validiteTime);
            shopTb.setDefaultLimit(defaultLimit);
            if(handInputEnable>-1){
                shopTb.setHandInputEnable(handInputEnable);
            }
        }
        int result = shopAcccountService.updateShopById(shopTb);
        StringUtils.ajaxOutput(response,"{\"state\":"+result+"}");
        return null;
    }


    @RequestMapping("/getshoprecharge")
    public String getRecharge(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );
        logger.info(reqParameterMap);
        JSONObject result = shopAcccountService.getRecharge( reqParameterMap );
        StringUtils.ajaxOutput( resp, result.toString() );
        return null;
    }

}
