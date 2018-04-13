package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.ParkInfoMapper;
import parkingos.com.bolink.service.CityParkOrderAnlysisService;
import parkingos.com.bolink.service.GetParkInfoService;
import parkingos.com.bolink.service.ParkOrderAnlysisService;
import parkingos.com.bolink.utils.TimeTools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GetParkInfoServiceImpl implements GetParkInfoService {
    @Autowired
    private ParkInfoMapper parkInfoMapper;
    @Autowired
    private ParkOrderAnlysisService parkOrderanlysisService;
    @Autowired
    private CityParkOrderAnlysisService cityParkOrderanlysisService;
    DecimalFormat af1 = new DecimalFormat("0");
    @Override
    public String getInfo(int groupid) {
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long tday = calendar.getTimeInMillis() / 1000;
        //获取进场和离场数据
        List<HashMap<String, Object>> entryCarList = parkInfoMapper.getEntryCar(tday, Long.parseLong(groupid + ""));
        List<HashMap<String, Object>> outCarList = parkInfoMapper.getExitCar(tday, Long.parseLong(groupid + ""));
        Collections.reverse(entryCarList);
        Collections.reverse(outCarList);
        parseTmtoDate(entryCarList);
        parseTmtoDate(outCarList);
        int parkingtotal = parkInfoMapper.getBerthTotal(groupid);
        //获取今日电子支付，现金支付，减免金额的统计
        Map<String,String> parammap = new HashMap<String,String>();
        parammap.put("groupid",groupid+"");
        String todyyymmdd=TimeTools.getDate_YY_MM_DD();
        parammap.put("date",todyyymmdd+" 00:00:00至"+todyyymmdd+"23:59:59");
        JSONObject retjson=cityParkOrderanlysisService.selectResultByConditions(parammap);
        JSONArray retarry = retjson.getJSONArray("rows");
        Double cashPay=0d;
        Double electronicPay=0d;
        Double freePay=0d;
        if(retarry.size()>0){
            JSONObject object =(JSONObject)retarry.get(retarry.size()-1);
            if(object.getString("cash_pay")!=null&&!"".equals(object.getString("cash_pay"))) {
                cashPay = Double.parseDouble(object.getString("cash_pay"));
                System.out.print("cashpay"+cashPay);
            }
            if(object.getString("electronic_pay")!=null&&!"".equals(object.getString("electronic_pay"))) {
                electronicPay = Double.parseDouble(object.getString("electronic_pay"));
                System.out.print("electronicPay"+electronicPay);
            }
            if(object.getString("free_pay")!=null&&!"".equals(object.getString("free_pay"))) {
                freePay = Double.parseDouble(object.getString("free_pay"));
                System.out.print("electronicPay"+electronicPay);
            }
        };
       /* Double cashPay  = parkInfoMapper.getCashPay(tday, groupid);
        Double electronicPay = parkInfoMapper.getElectronicPay(tday, groupid);
        Double reduceamount = parkInfoMapper.getReduceAmount(tday, groupid);
        Double freeamount = parkInfoMapper.getFreeAmount(tday, groupid);
        if(cashPay == null){
            cashPay=0d;
        }
        if(electronicPay == null){
            electronicPay=0d;
        }
        if(reduceamount == null){
            reduceamount=0d;
        }*/
        HashMap<String, Object> cashPaymap = new HashMap<String, Object>();
        HashMap<String, Object> electronicPaymap = new HashMap<String, Object>();
        HashMap<String, Object> reduceamap = new HashMap<String, Object>();
        HashMap<String, Object> totalIncomemap = new HashMap<String, Object>();
        totalIncomemap.put("elePay", af1.format(electronicPay));
        totalIncomemap.put("cashPay", af1.format(cashPay));
        totalIncomemap.put("freePay", af1.format(freePay));
        cashPaymap.put("name", "电子");
        cashPaymap.put("value", af1.format(electronicPay));
        electronicPaymap.put("name", "现金");
        electronicPaymap.put("value", af1.format(cashPay));
        reduceamap.put("name", "减免");
        reduceamap.put("value", af1.format(freePay));
        List<HashMap<String, Object>> totalIncomPie = new ArrayList<HashMap<String, Object>>();
        totalIncomPie.add(cashPaymap);
        totalIncomPie.add(electronicPaymap);
        totalIncomPie.add(reduceamap);
        //获取收费排行数据
        List<HashMap<String, Object>> parkRankList = parkInfoMapper.getParkRank(tday, groupid);
        for(HashMap<String, Object> map:parkRankList){
            map.put("total",af1.format(map.get("total")));
        }
        //获取车辆进场，离场，在场的数量统计
        int inCars = parkInfoMapper.getEntryCount(tday, groupid);
        int outCars = parkInfoMapper.getExitCount(tday, groupid);
        int inPark = parkInfoMapper.getInparkCount(tday, groupid);
        HashMap<String, Object> countMap = new HashMap<String, Object>();
        countMap.put("inCars", inCars);
        countMap.put("outCars", outCars);
        countMap.put("inPark", inPark);
        //计算泊位使用率
        List<HashMap<String,Object>> parkidList = parkInfoMapper.getParkIdByGroupId(groupid);
        List<HashMap<String,Object>> ss =parkInfoMapper.getBerthPercent(parkidList,tday);
        for(HashMap<String,Object> map :ss){
            Long asum = (Long)map.get("asum");
            Long usum = (Long) map.get("usum");
            DecimalFormat df = new DecimalFormat("#");
            double pecent=0d;
            if(usum !=0){
                pecent =  (float)usum*100/asum ;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            long time = (long) map.get("create_time");
            Date date = new Date(time * 1000);
            map.put("time", sdf.format(date));
            map.put("percent",df.format(pecent));
        }
        DecimalFormat df = new DecimalFormat("#");
        double parkOnpecent=0d;
        if(inPark !=0){
            parkOnpecent =  (float)inPark*100/parkingtotal ;
        }
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        HashMap<String, Object> berthPercentData = new HashMap<String, Object>();
        berthPercentData.put("time",hour);
        berthPercentData.put("percent",df.format(parkOnpecent));
        //计算车场在线
        List<HashMap<String,Object>> parkState = getParkStatus(groupid);
        //查询抬杆异常信息
        List<HashMap<String,Object>> exceptionEvents = getExceptions(groupid,"groupid",tday);
        retMap.put("inPartData", entryCarList); //存入进场车辆
        retMap.put("outPartData", outCarList); //存入离场车辆
        retMap.put("totalIncomPie", totalIncomPie); //存入金额分类统计list
        retMap.put("totalIncome", totalIncomemap);//今日收入统计
        retMap.put("parkRank", parkRankList); //收入排行
        retMap.put("inOutCarsCount", countMap);//进出车统计
        retMap.put("berthPercentData", ss);//泊位使用率
        retMap.put("parkState", parkState); //车场状态
        retMap.put("exceptionEvents", exceptionEvents);//车场异常信息
        String result = JSON.toJSON(retMap).toString();
        return result;
    }

    @Override
    public String getInfoByComid(int comid) {
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long tday = calendar.getTimeInMillis() / 1000;
        //获取进场和离场数据
        List<HashMap<String, Object>> entryCarList = parkInfoMapper.getEntryCarByComid(tday, comid);
        List<HashMap<String, Object>> outCarList = parkInfoMapper.getExitCarByComid(tday, comid);
        Collections.reverse(entryCarList);
        Collections.reverse(outCarList);
        parseTmtoDate(entryCarList);
        parseTmtoDate(outCarList);
        int berthtotal = parkInfoMapper.getBerthTotalbc(comid);
        //获取今日电子支付，现金支付，减免金额的统计
        Map<String,String> parammap = new HashMap<String,String>();
        parammap.put("comid",comid+"");
        String todyyymmdd=TimeTools.getDate_YY_MM_DD();
        parammap.put("date",todyyymmdd+" 00:00:00至"+todyyymmdd+" 23:59:59");
        JSONObject retjson=parkOrderanlysisService.selectResultByConditions(parammap);
        JSONArray retarry = retjson.getJSONArray("rows");
        Double cashPay=0d;
        Double electronicPay=0d;
        Double freePay=0d;
        if(retarry.size()>0){
            JSONObject object =(JSONObject)retarry.get(retarry.size()-1);
            if(object.getString("cash_pay")!=null&&!"".equals(object.getString("cash_pay"))) {
                cashPay = Double.parseDouble(object.getString("cash_pay"));
                System.out.print("cashpay"+cashPay);
            }
            if(object.getString("electronic_pay")!=null&&!"".equals(object.getString("electronic_pay"))) {
                electronicPay = Double.parseDouble(object.getString("electronic_pay"));
                System.out.print("electronicPay"+electronicPay);
            }
            if(object.getString("free_pay")!=null&&!"".equals(object.getString("free_pay"))) {
                freePay = Double.parseDouble(object.getString("free_pay"));
                System.out.print("electronicPay"+electronicPay);
            }
        };
       /* Double cashPay  = parkInfoMapper.getCashPaybc(tday, comid);
        Double electronicPay = parkInfoMapper.getElectronicPaybc(tday, comid);
        Double reduceamount = parkInfoMapper.getReduceAmountbc(tday, comid);
        Double freeamount = parkInfoMapper.getFreeAmountbc(tday, comid);
        if(cashPay == null){
            cashPay=0d;
        }
        if(electronicPay == null){
            electronicPay=0d;
        }
        if(reduceamount == null){
            reduceamount=0d;
        }*/
        HashMap<String, Object> cashPaymap = new HashMap<String, Object>();
        HashMap<String, Object> electronicPaymap = new HashMap<String, Object>();
        HashMap<String, Object> reduceamap = new HashMap<String, Object>();
        HashMap<String, Object> totalIncomemap = new HashMap<String, Object>();
        totalIncomemap.put("elePay", af1.format(electronicPay));
        totalIncomemap.put("cashPay", af1.format(cashPay));
        totalIncomemap.put("freePay", af1.format(freePay));
        cashPaymap.put("name", "电子");
        cashPaymap.put("value", af1.format(electronicPay));
        electronicPaymap.put("name", "现金");
        electronicPaymap.put("value", af1.format(cashPay));
        reduceamap.put("name", "减免");
        reduceamap.put("value", af1.format(freePay));
        List<HashMap<String, Object>> totalIncomPie = new ArrayList<HashMap<String, Object>>();
        totalIncomPie.add(cashPaymap);
        totalIncomPie.add(electronicPaymap);
        totalIncomPie.add(reduceamap);
        //获取收费排行数据
        List<HashMap<String, Object>> parkRankList = parkInfoMapper.getRankByout(tday, comid);
        if(parkRankList !=null && parkRankList.size()>0){
            for (HashMap<String, Object> map:parkRankList){
                map.put("total",af1.format(map.get("total")));
                Long uin =(Long) map.get("uid");
                map.put("parkName", uin);
                if(uin !=null) {
                    String username = parkInfoMapper.getUserInfo(uin);
                    if (username != null && !"".equals(username))
                        map.put("parkName", username);
                }
                //uid为-1时不显示
                if(uin == -1){
                    map.put("parkName", "");
                }
            }
        }
        //获取车辆进场，离场，在场的数量统计
        int inCars = parkInfoMapper.getEntryCountbc(tday, comid);
        int outCars = parkInfoMapper.getExitCountbc(tday, comid);
        int inPark = parkInfoMapper.getInparkCountbc(tday, comid);
        HashMap<String, Object> countMap = new HashMap<String, Object>();
        countMap.put("inCars", inCars);
        countMap.put("outCars", outCars);
        countMap.put("inPark", inPark);
        //计算泊位使用率
        HashMap<String,Object> tempmap = new  HashMap<String,Object>();
        List<HashMap<String,Object>> parkidList = new ArrayList<HashMap<String,Object>>();
        tempmap.put("parkid",comid);
        parkidList.add(tempmap);
        List<HashMap<String,Object>> ss =parkInfoMapper.getBerthPercent(parkidList,tday);
        for(HashMap<String,Object> map :ss){
            Long asum = (Long)map.get("asum");
            Long usum = (Long) map.get("usum");
            DecimalFormat df = new DecimalFormat("#");
            double pecent=0d;
            if(usum !=0){
                pecent =  (float)usum*100/asum ;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            long time = (long) map.get("create_time");
            Date date = new Date(time * 1000);
            map.put("time", sdf.format(date));
            map.put("percent",df.format(pecent));
        }
        double parkOnpecent=0d;
        DecimalFormat df = new DecimalFormat("#");
        if(inPark !=0){
            parkOnpecent =  (float)inPark*100/berthtotal ;
        }
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        HashMap<String, Object> berthPercentData = new HashMap<String, Object>();
        berthPercentData.put("time",hour);
        berthPercentData.put("percent",df.format(parkOnpecent));
        //计算车场在线
        List<HashMap<String,Object>> parkState = getParkStatusbc(comid);
        List<HashMap<String,Object>> exceptionEvents = getExceptions(comid,"comid",tday);

        retMap.put("inPartData", entryCarList); //存入进场车辆
        retMap.put("outPartData", outCarList); //存入离场车辆
        retMap.put("totalIncomPie", totalIncomPie); //存入金额分类统计list
        retMap.put("totalIncome", totalIncomemap);//今日收入统计
        retMap.put("parkRank", parkRankList); //收入排行
        retMap.put("inOutCarsCount", countMap);//进出车统计
        retMap.put("berthPercentData", ss);//泊位使用率
        retMap.put("parkState", parkState);//在线状态
        retMap.put("exceptionEvents", exceptionEvents);//车场异常信息
        String result = JSON.toJSON(retMap).toString();
        return result;
    }
   private List<HashMap<String,Object>> getExceptions(int id,String sflag,long ctime){
       List<HashMap<String,Object>> exceptionEvents = new ArrayList<HashMap<String,Object>>();
        if("comid".equals(sflag)){
           exceptionEvents = parkInfoMapper.getExpByCid(id,ctime);
        }else if("groupid".equals(sflag)){
             exceptionEvents = parkInfoMapper.getExpByGid(id,ctime);
        }
       if(exceptionEvents!=null && exceptionEvents.size()>0){
           SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
           for (HashMap<String,Object> map:exceptionEvents){
               Long uin =(Long) map.get("uin");
               if(uin !=null) {
                   String username = parkInfoMapper.getUserInfo(uin);
                   if (username != null && !"".equals(username))
                       map.put("uin", username);
               }
               Long time = (Long) map.get("ctime");
               if(time !=null) {
                   Date date = new Date(time * 1000);
                   map.put("time", sdf.format(date));
                   map.remove("ctime");
               }
           }

       }
    return exceptionEvents;
   }
    /**
     * 把从数据库查出的long时间秒值转为时间：分钟格式的时间字符串
     *
     * @param list
     */
    private void parseTmtoDate(List<HashMap<String, Object>> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (HashMap<String, Object> map : list) {
            long time = (long) map.get("timemills");
            Date date = new Date(time * 1000);
            map.put("time", sdf.format(date));
            map.remove("timemills");

        }

    }

    private List<HashMap<String, Object>> getParkStatus(int groupid) {
        List<HashMap<String,Object>> parkState = new ArrayList<HashMap<String,Object>>();
        List<HashMap<String,Object>> parkidList = parkInfoMapper.getParkIdByGroupId(groupid);
        if (parkidList != null && parkidList.size() > 0) {
            for (HashMap<String,Object> parkidmap : parkidList) {
                long  parkid = (long)parkidmap.get("parkid");
                String parkName = (String)parkidmap.get("parkName");
                HashMap<String,Object> parkstatusmap = new  HashMap<String,Object>();
                List<HashMap<String, Object>> parkLoginList = parkInfoMapper.getParkLogin(parkid + "");
                if (parkLoginList != null && parkLoginList.size() > 0) {
                    HashMap<String, Object> loginmap = parkLoginList.get(0);
                    Long beattime = (Long) loginmap.get("beattime");
                    Long logintime = (Long) loginmap.get("logintime");
                    boolean isonline = false;
                    if(beattime!=null) {
                        //心跳在60秒内证明在线
                        isonline=isParkOnline(beattime.longValue(),60);

                      if(!isonline){
                          isonline=isParkOnline(logintime.longValue(),10);
                      }
                    }
                      if(isonline){
                          parkstatusmap.put("parkName",parkName);
                          parkstatusmap.put("state",1);
                      }else{
                          parkstatusmap.put("parkName",parkName);
                          parkstatusmap.put("state",0);
                      }
                    }else{
                    parkstatusmap.put("parkName",parkName);
                    parkstatusmap.put("state",0);
                }
                parkState.add(parkstatusmap);
            }
        }
        return parkState;
    }

    private List<HashMap<String, Object>> getParkStatusbc(int parkid) {
        List<HashMap<String, Object>> parkState = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> parkLoginList = parkInfoMapper.getParkLogin(parkid + "");
        if (parkLoginList != null && parkLoginList.size() > 0) {
            for (HashMap<String, Object> loginmap : parkLoginList){
                HashMap<String, Object> parkstatusmap = new HashMap<String, Object>();
            Long beattime = (Long) loginmap.get("beattime");
            Long logintime = (Long) loginmap.get("logintime");
            String localid = (String) loginmap.get("localid");
            if(localid == null)localid="";
            boolean isonline = false;
            if (beattime != null) {
                //心跳在60秒内证明在线
                isonline = isParkOnline(beattime.longValue(), 60);

                if (!isonline) {
                    isonline = isParkOnline(logintime.longValue(), 10);
                }
            }
            if (isonline) {
                parkstatusmap.put("state", 1);
                parkstatusmap.put("localid", localid.substring(localid.indexOf("_")+1));
            } else {
                parkstatusmap.put("state", 0);
                parkstatusmap.put("localid", localid.substring(localid.indexOf("_")+1));
            }
                parkState.add(parkstatusmap);
        }
    }

        return parkState;
    }
    /**
     * 判断车场是否在线
     * @param time
     * @param delayTime
     * @return
     * @time 2017年 下午12:03:41
     * @author QuanHao
     */
    private boolean isParkOnline(long time,int delayTime){
        long curTime = System.currentTimeMillis()/1000;
        long margin = curTime-time;
        if(margin-delayTime<=0){
            return true;
        }
        return false;
    }
}

