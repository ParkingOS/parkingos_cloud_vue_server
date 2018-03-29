package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.ParkInfoMapper;
import parkingos.com.bolink.service.GetParkInfoService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GetParkInfoServiceImpl implements GetParkInfoService {
    @Autowired
    private ParkInfoMapper parkInfoMapper;

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
        parseTmtoDate(entryCarList);
        parseTmtoDate(outCarList);
        int berthtotal = parkInfoMapper.getBerthTotal(groupid);
        //获取今日电子支付，现金支付，减免金额的统计
        double cashPay = parkInfoMapper.getCashPay(tday, groupid);
        double electronicPay = parkInfoMapper.getElectronicPay(tday, groupid);
        double reduceamount = parkInfoMapper.getReduceAmount(tday, groupid);
        HashMap<String, Object> cashPaymap = new HashMap<String, Object>();
        HashMap<String, Object> electronicPaymap = new HashMap<String, Object>();
        HashMap<String, Object> reduceamap = new HashMap<String, Object>();
        HashMap<String, Object> totalIncomemap = new HashMap<String, Object>();
        totalIncomemap.put("elePay", electronicPay);
        totalIncomemap.put("cashPay", cashPay);
        cashPaymap.put("name", "电子");
        cashPaymap.put("value", electronicPay);
        electronicPaymap.put("name", "现金");
        electronicPaymap.put("value", cashPay);
        reduceamap.put("name", "减免");
        reduceamap.put("value", reduceamount);
        List<HashMap<String, Object>> totalIncomPie = new ArrayList<HashMap<String, Object>>();
        totalIncomPie.add(cashPaymap);
        totalIncomPie.add(electronicPaymap);
        totalIncomPie.add(reduceamap);
        //获取收费排行数据
        List<HashMap<String, Object>> parkRankList = parkInfoMapper.getParkRank(tday, groupid);
        //获取车辆进场，离场，在场的数量统计
        int inCars = parkInfoMapper.getEntryCount(tday, groupid);
        int outCars = parkInfoMapper.getExitCount(tday, groupid);
        int inPark = parkInfoMapper.getInparkCount(tday, groupid);
        HashMap<String, Object> countMap = new HashMap<String, Object>();
        countMap.put("inCars", inCars);
        countMap.put("outCars", outCars);
        countMap.put("inPark", inPark);
        //计算泊位使用率
        double parkOnpecent =  inPark*100/berthtotal ;
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        HashMap<String, Object> berthPercentData = new HashMap<String, Object>();
        berthPercentData.put("time",hour);
        berthPercentData.put("percent",parkOnpecent);
        //计算车场在线
        List<HashMap<String,Object>> parkState = getParkStatus(groupid);
        retMap.put("inPartData", entryCarList); //存入进场车辆
        retMap.put("outPartData", outCarList); //存入离场车辆
        retMap.put("totalIncomPie", totalIncomPie); //存入金额分类统计list
        retMap.put("totalIncome", totalIncomemap);//今日收入统计
        retMap.put("parkRank", parkRankList); //收入排行
        retMap.put("inOutCarsCount", countMap);//进出车统计
        retMap.put("berthPercentData", berthPercentData);//泊位使用率
        retMap.put("parkState", parkState);
        String result = JSON.toJSON(retMap).toString();
        return result;
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

