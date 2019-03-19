package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.service.ParkCollectorOrderAnlysisService;
import parkingos.com.bolink.service.ParkOrdeBalanceService;
import parkingos.com.bolink.service.ParkingosProgramService;
import parkingos.com.bolink.utils.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParkingosProgramServiceImpl implements ParkingosProgramService {

    Logger logger = LoggerFactory.getLogger(ParkingosProgramServiceImpl.class);
    @Autowired
    private ParkOrdeBalanceService parkOrderBalanceService;
    @Autowired
    private ParkCollectorOrderAnlysisService parkCollectorOrderAnlysisService;


    @Override
    public JSONObject getMoneyData(Long comid, Long userId,Integer isManager) {
        //如果没有收费员  证明是车场结算日报
        Map<String,String> reqMap = new HashMap<>();
        reqMap.put("comid",comid+"");
        JSONObject jsonObject = null;
        Double cashPay=0d;
        Double electronicPay=0d;
        Double freePay=0d;
        if(userId<0){
            jsonObject = parkOrderBalanceService.getBalanceTrade(reqMap);
            JSONArray retarry = jsonObject.getJSONArray("rows");
            if(retarry!=null&&retarry.size()>0) {
                retarry.remove(retarry.size() - 1);
                Collections.reverse(retarry);
            }
            jsonObject.clear();
            jsonObject.put("rows",retarry);
            return jsonObject;
//            if(retarry!=null&&retarry.size()>0){
//                JSONObject object =(JSONObject)retarry.get(retarry.size()-1);
//                if(object.getString("cash_total")!=null&&!"".equals(object.getString("cash_total"))) {
//                    cashPay = Double.parseDouble(object.getString("cash_total"));
//                }
//                if(object.getString("free_pay")!=null&&!"".equals(object.getString("free_pay"))) {
//                    freePay = Double.parseDouble(object.getString("free_pay"));
//                }
//                if(object.getString("ele_total")!=null&&!"".equals(object.getString("ele_total"))) {
//                    electronicPay = Double.parseDouble(object.getString("ele_total"));
//                }
//            }

        }else{
            //如果不是车场管理员  那么只能看到自己的日报
            if(isManager==0) {
                reqMap.put("out_uid", "3");
                reqMap.put("out_uid_start", userId + "");
            }
            /*
            * resultMap.put("cash_pay",cash_pay_money+"");
            resultMap.put("cash_prepay",cash_prepay_money+"");
            resultMap.put("ele_prepay",ele_prepay_money+"");
            resultMap.put("act_total",act_money+"");
            resultMap.put("free_pay",free_money+"");
            * */

            jsonObject = parkCollectorOrderAnlysisService.selectResultByConditions(reqMap);
            JSONArray retarry = jsonObject.getJSONArray("rows");
            if(retarry!=null&&retarry.size()>0) {
                retarry.remove(retarry.size() - 1);
            }
            jsonObject.clear();
            jsonObject.put("rows",retarry);
            return jsonObject;
//            if(retarry!=null&&retarry.size()>0){
//                JSONObject object =(JSONObject)retarry.get(retarry.size()-1);
//                if(object.getString("cash_pay")!=null&&!"".equals(object.getString("cash_pay"))) {
//                    cashPay += Double.parseDouble(object.getString("cash_pay"));
//                }
//                if(object.getString("cash_prepay")!=null&&!"".equals(object.getString("cash_prepay"))) {
//                    cashPay += Double.parseDouble(object.getString("cash_prepay"));
//                }
//                if(object.getString("free_pay")!=null&&!"".equals(object.getString("free_pay"))) {
//                    freePay = Double.parseDouble(object.getString("free_pay"));
//                }
//                if(object.getString("ele_prepay")!=null&&!"".equals(object.getString("ele_prepay"))) {
//                    electronicPay = Double.parseDouble(object.getString("ele_prepay"));
//                }
//            }
        }

//        jsonObject.clear();
//        jsonObject.put("cash_pay", StringUtils.formatDouble(cashPay));
//        jsonObject.put("ele_pay", StringUtils.formatDouble(electronicPay));
//        jsonObject.put("free_pay", StringUtils.formatDouble(freePay));
//        jsonObject.put("act_total", StringUtils.formatDouble(StringUtils.formatDouble(cashPay)+StringUtils.formatDouble(electronicPay)));
//        return jsonObject;
    }
}
