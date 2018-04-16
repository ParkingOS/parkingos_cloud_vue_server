package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.FixCodeTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.service.FixCodeService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.TicketService;
import parkingos.com.bolink.utils.CustomDefind;

import java.util.Map;

@Service
public class FixCodeServiceImpl implements FixCodeService {

    Logger logger = Logger.getLogger(FixCodeServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<FixCodeTb> supperSearchService;
    @Autowired
    private TicketService ticketService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setShopId(Long.parseLong(reqmap.get("shopid")));

        JSONObject result =  supperSearchService.supperSearch(fixCodeTb, reqmap);

        return result;
    }

    @Override
    public JSONObject addFixCode(FixCodeTb fixCodeTb) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state",0);
        jsonObject.put("msg","添加失败");

        int insert = commonDao.insert(fixCodeTb);
        if(insert==1){
            jsonObject.put("state",1);
            jsonObject.put("msg","添加成功");
            return jsonObject;
        }

        return jsonObject;
    }

    @Override
    public Long getId() {
        return commonDao.selectSequence(FixCodeTb.class);
    }

    @Override
    public JSONObject updateRole(FixCodeTb fixCodeTb) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state",0);
        jsonObject.put("msg","修改失败");

        int update = commonDao.updateByPrimaryKey(fixCodeTb);
        if(update==1){
            jsonObject.put("state",1);
            jsonObject.put("msg","修改成功");
            return jsonObject;
        }

        return jsonObject;
    }

    @Override
    public JSONObject userTicket(String code) {

        JSONObject result = new JSONObject();

        FixCodeTb fixCodeTb = new FixCodeTb();
        fixCodeTb.setCode(code);
        fixCodeTb=(FixCodeTb)commonDao.selectObjectByConditions(fixCodeTb);
        if(fixCodeTb!=null){

            if(fixCodeTb.getState()==1){
                result.put("state",0);
                result.put("error","优惠券已经不能领取，请联系优惠券管理员");
                return result;
            }

            if(fixCodeTb.getEndTime()<System.currentTimeMillis()/1000){//过期
                result.put("state",0);
                result.put("error","优惠券已经不能领取，请联系优惠券管理员");
                return result;
            }


            //判断商户信息
            Long shopid = fixCodeTb.getShopId();
            ShopTb shopTb = new ShopTb();
            shopTb.setId(shopid);
            shopTb.setState(0);
            shopTb = (ShopTb)commonDao.selectObjectByConditions(shopTb);
            if(shopTb==null){
                result.put("state",0);
                result.put("error","不存在该商户");
                return result;
            }

            if(fixCodeTb.getType()==1){//减免券
                //获取单张金额(时长或者金额)
                Integer amount = fixCodeTb.getAmount();
                //获取该商户的减免单位,判断是时长还是金额
                Integer ticketUnit = shopTb.getTicketUnit();
                if(ticketUnit==4){//减免金额
                    if(amount>fixCodeTb.getMoneyLimit()){
                        result.put("state",0);
                        result.put("error","固定码金额余额不足");
                        return result;
                    }else{
                        Map<String,Object> retMap = ticketService.createTicket(shopid,amount,5,0);
                        if(retMap.get("result") == -1 || retMap.get("result") == -2){
                            logger.info("生成减免劵出错，用劵失败");
                            result.put("state",0);
                            result.put("error",retMap.get("error"));
                            return result;
                        }

                        fixCodeTb.setMoneyLimit(fixCodeTb.getMoneyLimit()-amount);
                        fixCodeTb.setFreeLimit(fixCodeTb.getFreeLimit()-1);
                        commonDao.updateByPrimaryKey(fixCodeTb);


                        String ticketCode = retMap.get("code")+"";
                        String ticketurl = CustomDefind.getValue("TICKETURL")+ticketCode;
                        result.put("state",1);
                        result.put("ticketurl",ticketurl);
                        return result;
                    }
                }else{
                    if(amount>fixCodeTb.getTimeLimit()){
                        result.put("state",0);
                        result.put("error","固定码时长余额不足");
                        return result;
                    }else{
                        Map<String,Object> retMap = ticketService.createTicket(shopid,amount,3,0);
                        if(retMap.get("result") == -1 || retMap.get("result") == -2){
                            logger.info("生成减免劵出错，用劵失败");
                            result.put("state",0);
                            result.put("error",retMap.get("error"));
                            return result;
                        }

                        fixCodeTb.setTimeLimit(fixCodeTb.getTimeLimit()-amount);
                        fixCodeTb.setFreeLimit(fixCodeTb.getFreeLimit()-1);
                        commonDao.updateByPrimaryKey(fixCodeTb);

                        String ticketCode = retMap.get("code")+"";
                        String ticketurl = CustomDefind.getValue("TICKETURL")+ticketCode;
                        result.put("state",1);
                        result.put("ticketurl",ticketurl);
                        return result;
                    }
                }

            }else if(fixCodeTb.getType()==2){//全免券
                if(fixCodeTb.getFreeLimit()<1){
                    result.put("state",0);
                    result.put("error","固定码全免券余额不足");
                    return result;
                }else{
                    Map<String,Object> retMap = ticketService.createTicket(shopid,0,4,0);
                    if(retMap.get("result") == -1 || retMap.get("result") == -2){
                        logger.info("生成减免劵出错，用劵失败");
                        result.put("state",0);
                        result.put("error",retMap.get("error"));
                        return result;
                    }

                    fixCodeTb.setFreeLimit(fixCodeTb.getFreeLimit()-1);
                    commonDao.updateByPrimaryKey(fixCodeTb);

                    String ticketCode = retMap.get("code")+"";
                    String ticketurl = CustomDefind.getValue("TICKETURL")+ticketCode;
                    result.put("state",1);
                    result.put("ticketurl",ticketurl);
                    return result;
                }
            }


        }

        return result;
    }

}
