package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityOrderService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("cityorderSpring")
public class CityOrderServiceImpl implements CityOrderService {

    Logger logger = Logger.getLogger(CityOrderServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        Double sumtotal = 0.00;
        Double cashpay = 0.00;
        Double elepay = 0.00;
        List<OrderTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        //查询今天的数据显示
        logger.error("=========..req"+reqmap.size());
        OrderTb orderTb = new OrderTb();

        orderTb.setState(1);
        orderTb.setIshd(0);

        //下拉选  车场 ,拼装参数车场参数
        String comidStr = reqmap.get("comid_start");
        if(comidStr!=null&&!"".equals(comidStr)){
            Long comid = Long.parseLong(comidStr);
            if(comid>-1){
                orderTb.setComid(comid);
            }
        }

        String endTime = reqmap.get("end_time");
        logger.error("===>>>endTime"+endTime);
        //组装出场车辆时间参数   默认今天出场
        if(endTime==null||"undefined".equals(endTime)||"".equals(endTime)){
            reqmap.put("end_time","1");
            reqmap.put("end_time_start",(TimeTools.getToDayBeginTime()+""));
            logger.error("=========..req"+reqmap.size());
        }


        Map searchMap = supperSearchService.getGroupOrCitySearch(orderTb,reqmap);
        OrderTb baseQuery =(OrderTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        count = commonDao.selectCountByConditions(baseQuery,supperQuery);
        if(count>0){
            list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            if (list != null && !list.isEmpty()) {
                for (OrderTb orderTb1 : list) {
                    OrmUtil<OrderTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap(orderTb1);
                    sumtotal+=StringUtils.formatDouble(Double.parseDouble(map.get("total")+""));
                    cashpay+= (StringUtils.formatDouble(Double.parseDouble(map.get("cash_prepay")+""))+StringUtils.formatDouble(Double.parseDouble(map.get("cash_pay")+"")));
                    elepay+= (StringUtils.formatDouble(Double.parseDouble(map.get("electronic_prepay")+""))+StringUtils.formatDouble(Double.parseDouble(map.get("electronic_pay")+"")));
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }

//        String groupid = reqmap.get("groupid");
//        String cityid = reqmap.get("cityid");
//        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);
//
//        Map searchMap = supperSearchService.getBaseSearch(orderTb,reqmap);
//        logger.info(searchMap);
//        if(searchMap!=null&&!searchMap.isEmpty()){
//            OrderTb baseQuery =(OrderTb)searchMap.get("base");
//            List<SearchBean> supperQuery = null;
//            if(searchMap.containsKey("supper"))
//                supperQuery = (List<SearchBean>)searchMap.get("supper");
//            PageOrderConfig config = null;
//            if(searchMap.containsKey("config"))
//                config = (PageOrderConfig)searchMap.get("config");
//
//            List parks =new ArrayList();
//
//            if(groupid !=null&&!"".equals(groupid)){
//                parks = commonMethods.getParks(Long.parseLong(groupid));
//            }else if(cityid !=null&&!"".equals(cityid)){
//                parks = commonMethods.getparks(Long.parseLong(cityid));
//            }
//
//            System.out.println("=======parks:"+parks);
//
//            //封装searchbean  集团或者城市下面所有车场
//            SearchBean searchBean = new SearchBean();
//            searchBean.setOperator(FieldOperator.CONTAINS);
//            searchBean.setFieldName("comid");
//            searchBean.setBasicValue(parks);
//
//            if (supperQuery == null) {
//                supperQuery = new ArrayList<>();
//            }
//            supperQuery.add( searchBean );
//
//            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
//            if(count>0){
//                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
//                if (list != null && !list.isEmpty()) {
//
//                    for (OrderTb order : list) {
//                        OrmUtil<OrderTb> otm = new OrmUtil<>();
//                        Map<String, Object> map = otm.pojoToMap(order);
//                        sumtotal+=StringUtils.formatDouble(Double.parseDouble(map.get("total")+""));
//                        cashpay+= (StringUtils.formatDouble(Double.parseDouble(map.get("cash_prepay")+""))+StringUtils.formatDouble(Double.parseDouble(map.get("cash_pay")+"")));
//                        elepay+= (StringUtils.formatDouble(Double.parseDouble(map.get("electronic_prepay")+""))+StringUtils.formatDouble(Double.parseDouble(map.get("electronic_pay")+"")));
//                        resList.add(map);
//                    }
//                    result.put("rows", JSON.toJSON(resList));
//                }
//            }
//        }
        result.put("sumtotal",String.format("%.2f",sumtotal));
        result.put("cashpay",String.format("%.2f",cashpay));
        result.put("elepay",String.format("%.2f",elepay));
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));
        logger.error("============>>>>>返回数据"+result);
        return result;
    }

}
