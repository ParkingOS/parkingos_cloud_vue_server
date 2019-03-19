package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.service.BigScreenService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


@Service
public class BigScreenServiceImpl implements BigScreenService {

    Logger logger = LoggerFactory.getLogger(BigScreenServiceImpl.class);

    @Autowired
    CommonDao commonDao;
    @Autowired
    SupperSearchService supperSearchService;

    @Override
    public JSONObject getState(Long comid) {
        JSONObject result = new JSONObject();
        result.put("state",1);
        result.put("warn",-1);
        BigScreenAccount bigScreenAccount = new BigScreenAccount();
        bigScreenAccount.setParkId(comid);
        int count = commonDao.selectCountByConditions(bigScreenAccount);
        if(count<=0){
            result.put("state",0);
            return result;
        }
        bigScreenAccount =(BigScreenAccount)commonDao.selectObjectByConditions(bigScreenAccount);
        result.put("end_time",bigScreenAccount.getEndTime());
        if(bigScreenAccount.getEndTime()<System.currentTimeMillis()/1000){
            result.put("state",2);
            result.put("warn",-1);
            return result;
        }
        if(bigScreenAccount.getBeginTime()>System.currentTimeMillis()/1000){
            result.put("begin_time", TimeTools.getTimeStr_yyyy_MM_dd(bigScreenAccount.getBeginTime()));
            result.put("state",3);
            return result;
        }

        int day = getNoticeDay(bigScreenAccount.getEndTime());
        if(day<=3){
            result.put("warn",day);
        }else {
            result.put("warn",0);
        }

        return result;
    }

    @Override
    public JSONObject buyBigScreen(Integer buyMonth, Double money, String tradeNo, Long park_id) {
        JSONObject result = new JSONObject();
        result.put("state",0);

        Long union_id =-1L;
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(park_id);
        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb==null){
            return result;
        }

        String parkName = comInfoTb.getCompanyName()+"("+comInfoTb.getBolinkId()+")";
        if(comInfoTb.getCityid()>0){
            union_id = comInfoTb.getCityid();
        }else{
            OrgGroupTb orgGroupTb= new OrgGroupTb();
            orgGroupTb.setId(comInfoTb.getGroupid());
            orgGroupTb=(OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);
            if(orgGroupTb!=null){
                union_id = orgGroupTb.getCityid();
            }
        }

        String unionName = "";
        if(union_id>0){
            OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
            orgCityMerchants.setId(union_id);
            orgCityMerchants=(OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
            if(orgCityMerchants!=null){
                unionName=orgCityMerchants.getName();
            }
        }

        BigScreenTrade trade = new BigScreenTrade();
        trade.setTradeNo(tradeNo);
        trade.setBuyMonth(buyMonth);
        trade.setMoney(new BigDecimal(money+""));
        trade.setParkId(park_id);
        trade.setParkName(parkName);
        trade.setState(0);
        trade.setUnionId(union_id);
        trade.setUnionName(unionName);
        trade.setCtime(System.currentTimeMillis()/1000);

        Long beginTime = System.currentTimeMillis()/1000;
        BigScreenAccount account = new BigScreenAccount();
        account.setParkId(park_id);
        account =(BigScreenAccount)commonDao.selectObjectByConditions(account);
        if(account!=null&&account.getEndTime()!=null){
            if(account.getEndTime()>beginTime) {
                beginTime = account.getEndTime();
            }
        }

        trade.setBeginTime(beginTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(beginTime * 1000);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + buyMonth);
        Long etime = calendar.getTimeInMillis() / 1000 - 1;

        trade.setEndTime(etime);




        int insert = commonDao.insert(trade);
        if(insert==1) {
            result.put("state", 1);
            result.put("trade_no",tradeNo);
        }

        return result;
    }

//    @Override
//    public String getBigScreenData(Long comid, int type) {
//        JSONObject result = new JSONObject();
//        result.put("end_time",System.currentTimeMillis()/1000);
//        MessagePriceTb messagePriceTb = new MessagePriceTb();
//        messagePriceTb.setType(type);
//        List<MessagePriceTb> list = commonDao.selectListByConditions(messagePriceTb);
//        if(list!=null&&!list.isEmpty()){
//            result.put("price",list);
//        }
//        BigScreenAccount account = new BigScreenAccount();
//        account.setParkId(comid);
//        int count = commonDao.selectCountByConditions(account);
//        if(count>0){
//            account = (BigScreenAccount)commonDao.selectObjectByConditions(account);
//            if(account!=null&&account.getEndTime()!=null){
//                result.put("end_time",account.getEndTime());
//            }
//        }
//        return result.toString();
//    }

    @Override
    public JSONObject getBuyTrade(Map<String, String> reqParameterMap) {
        Long comid = Long.parseLong(reqParameterMap.get("comid"));
        int limitDay = getLimitDay(comid);
        if(limitDay<0){
            limitDay=0;
        }
        BigScreenTrade trade = new BigScreenTrade();
        trade.setParkId(comid);
        trade.setState(1);
        JSONObject result = supperSearchService.supperSearch(trade, reqParameterMap);
        result.put("limit_day",limitDay);
        return result;
    }

    @Override
    public List<List<Object>> exportBuyTrade(Map<String, String> reqParameterMap) {
        reqParameterMap.remove("orderby");
        JSONObject result =getBuyTrade(reqParameterMap);
        List<BigScreenTrade> buyList = JSON.parseArray(result.get("rows").toString(), BigScreenTrade.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(buyList!=null&&buyList.size()>0){
            String [] f = new String[]{"buy_month","pay_time","begin_time","end_time","money","trade_no"};
            for(BigScreenTrade trade : buyList){
                List<Object> values = new ArrayList<Object>();
                OrmUtil<BigScreenTrade> otm = new OrmUtil<BigScreenTrade>();
                Map map = otm.pojoToMap(trade);
                //判断各种字段 组装导出数据
                for(String field : f){
                    Object value = map.get(field);
                    if("money".equals(field)){
                        if(value!=null){
                            values.add(map.get(field)+"元");
                        }
                    }else{
                        if("pay_time".equals(field)||"end_time".equals(field)||"begin_time".equals(field)){
                            if(map.get(field)!=null){
                                values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add(map.get(field)+"");
                        }
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }

    private int getLimitDay(Long comid) {
        BigScreenAccount bigScreenAccount =new BigScreenAccount();
        bigScreenAccount.setParkId(comid);
        bigScreenAccount=(BigScreenAccount)commonDao.selectObjectByConditions(bigScreenAccount);
        if(bigScreenAccount!=null){
            return getNoticeDay(bigScreenAccount.getEndTime());
        }
        return 0;
    }

    private int getNoticeDay(Long endTime) {
        Double day =(endTime.doubleValue()-System.currentTimeMillis()/1000)/86400;
        return (int) Math.ceil(day);
    }

//    public static void main(String[] args){
//        System.out.print(getNoticeDay(1545984000L));
//    }
}
