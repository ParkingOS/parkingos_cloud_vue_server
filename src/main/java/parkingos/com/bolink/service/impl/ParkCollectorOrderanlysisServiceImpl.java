package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.ParkCollectorOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParkCollectorOrderanlysisServiceImpl implements ParkCollectorOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(ParkCollectorOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private OrderServer orderServer;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        //reqmap 里面放comid_start

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = Long.parseLong(reqmap.get("comid"));

        Long groupId = orderMapper.getGroupIdByComId(comid);
        Long cityId = -1L;
        if(groupId!=null&&groupId>-1){
            cityId = orderMapper.getCityIdByGroupId(groupId);
        }else{
            cityId = orderMapper.getGroupIdByComId(comid);
        }
        String tableName = "order_tb_new";
        if(cityId!=null&&cityId>-1){
            tableName = "order_tb_new_"+cityId%100;
        }

        String date = reqmap.get("date");
        Long start = null;
        if(date==null||"".equals(date)){
            start = TimeTools.getToDayBeginTime();
        }else{
            start = Long.parseLong(date);
        }
        Long end = start+24*60*60;

        reqmap.put("end_time","between");
        reqmap.put("tableName",tableName);
        reqmap.put("end_time_start",start+"");
        reqmap.put("end_time_end",end+"");
        reqmap.put("comid_start",comid+"");
        List<Map<String,String>> backList = orderServer.selectParkCollectorAnlysis(reqmap);

//        String sql = "select count(*) scount,sum(amount_receivable) amount_receivable, " +
//                "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
//                "sum(reduce_amount) reduce_pay,out_uid from "+tableName+" where end_time between " + start + " and " + end;
//        String free_sql = "select count(*) scount,sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay,out_uid  from "+tableName+" where end_time between "+ start + " and " + end;
//        String groupby = " group by out_uid";
//
//        String outUid = reqmap.get("out_uid");
//        Long out_uid = -1L;
//        if(!Check.isEmpty(outUid)){
//            out_uid=Long.parseLong(outUid);
//        }
//        if(out_uid==-1){
//            sql +=" and out_uid>-1 and state= 1  and ishd=0 and comid ="+comid;
//            free_sql +=" and out_uid>-1 and state= 1 and ishd=0 and comid ="+comid;
//        }else{
//            sql +=" and out_uid="+outUid+" and state= 1  and ishd=0 and comid ="+comid;
//            free_sql +=" and out_uid="+outUid+" and state= 1 and ishd=0 and comid ="+comid;
//        }
//
////        if(cityId!=null&&cityId>-1){
////            sql +=" and mod(cityid,100)="+cityId%100;
////            free_sql +=" and mod(cityid,100)="+cityId%100;
////        }
//
//
//
////        //总订单集合
//        List<Map<String, Object>> totalList =commonDao.getObjectBySql(sql +groupby);
//        //免费订单集合
//        List<Map<String, Object>> freeList = commonDao.getObjectBySql(free_sql +" and pay_type=8 "+groupby);//pgOnlyReadService.getAllMap(free_sql +" and pay_type=8 group by out_uid,comid order by scount desc ",params);
//        int totalCount = 0;//总订单数
//        double totalMoney = 0.0;//订单金额
//        double cashMoney = 0.0;//现金支付金额
//        double elecMoney = 0.0;//电子支付金额
//        double actFreeMoney = 0.0;//免费金额+减免支付
//        double actRecMoney =0.0;//电子结算+现金结算
//        List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
//        if(totalList != null && totalList.size() > 0) {
//            for (Map<String, Object> totalOrder : totalList) {
//                if (totalOrder.containsKey("out_uid")) {
//                    Long userid = (Long) totalOrder.get("out_uid");
//                    List<Map<String, Object>> list = commonDao.getObjectBySql("select nickname from user_info_tb where id =" + userid);
////                    logger.error("=========车场:" + list.get(0));
//                    if(list!=null&&list.size()>0) {
////
//                        totalOrder.put("name", list.get(0).get("nickname"));
////
//                    }else{
//                        totalOrder.put("name", "");
//                    }
//                }
//                totalCount += Integer.parseInt(totalOrder.get("scount") + "");
//
//                totalMoney += Double.parseDouble(totalOrder.get("amount_receivable") + "");
//
//                totalOrder.put("sdate", totalOrder.get("e_time"));
//                //格式化应收
//                totalOrder.put("amount_receivable",String.format("%.2f",StringUtils.formatDouble(Double.parseDouble(totalOrder.get("amount_receivable")+""))));
//
//                //现金支付
//                cashMoney +=StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"));
//                totalOrder.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"))));
//                //电子支付
//                elecMoney += StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"));
//                totalOrder.put("electronic_pay", String.format("%.2f", StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"))));
//                //每一行的合计 = 现金支付+电子支付
//                totalOrder.put("act_total", String.format("%.2f",StringUtils.formatDouble(Double.parseDouble(totalOrder.get("cash_pay")+"")+Double.parseDouble(totalOrder.get("electronic_pay")+""))));
//
//                //减免支付
//                double reduceAmount = StringUtils.formatDouble(Double.parseDouble((totalOrder.get("reduce_pay") == null ? "0.00" : totalOrder.get("reduce_pay") + "")));
//                double actFreePay = reduceAmount;
//                //遍历免费集合
//                if (freeList != null && freeList.size() > 0) {
//                    for (Map<String, Object> freeOrder : freeList) {
//                        if(freeOrder.get("out_uid").equals(totalOrder.get("out_uid"))){
//                            double freePay = StringUtils.formatDouble(Double.parseDouble((freeOrder.get("free_pay") == null ? "0.00" : freeOrder.get("free_pay") + "")));
//                            actFreePay = freePay+reduceAmount;
//                            logger.error("========>>>>actFreePay"+actFreePay);
//                        }
//                    }
//                }
//                totalOrder.put("free_pay",  String.format("%.2f",actFreePay));
//                actFreeMoney+=actFreePay;
//                backList.add(totalOrder);
//            }
//        }
//
//        if(backList.size()>0){
//            Map sumMap = new HashMap();
//            sumMap.put("name","合计");
//            sumMap.put("sdate","合计");
//            sumMap.put("scount",totalCount);
//            sumMap.put("amount_receivable",String.format("%.2f",StringUtils.formatDouble(totalMoney)));
//            sumMap.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(cashMoney)));
//            sumMap.put("electronic_pay",String.format("%.2f",StringUtils.formatDouble(elecMoney)));
//            sumMap.put("act_total",String.format("%.2f",StringUtils.formatDouble((cashMoney+elecMoney))));
//            sumMap.put("free_pay",String.format("%.2f",StringUtils.formatDouble(actFreeMoney)));
//            backList.add(sumMap);
//
//        }

        result.put("rows",JSON.toJSON(backList));
        return result;


    }


    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<Object> resList = JSON.parseArray(result.get("rows").toString());

        logger.error("=========>>>>>>.导出订单" + resList.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (resList != null && resList.size() > 0) {
            for (Object object : resList) {
                Map<String,Object> map = (Map)object;
                List<Object> values = new ArrayList<Object>();
                values.add(map.get("name"));
                values.add(map.get("scount"));
                values.add(map.get("amount_receivable"));
                values.add(map.get("cash_pay"));
                values.add(map.get("electronic_pay"));
                values.add(map.get("act_total"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
