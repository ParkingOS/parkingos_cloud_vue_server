package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service("spring")
public class OrderServiceImpl implements OrderService {

    Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;


    @Override
    public int selectCountByConditions(OrderTb orderTb) {
        return 0;
    }
    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        OrderTb orderTb = new OrderTb();
        orderTb.setComid(21782L);
        JSONObject result = supperSearchService.supperSearch(orderTb,reqmap);
        String str = "{\"total\":12,\"page\":1,\"parkinfo\":\"场内停车52辆,临停车52辆,空车位:948辆\",\"rows\":[]}";
        /*JSONObject result = JSONObject.parseObject(str);


        int count =0;
        List<OrderTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        Map searchMap = supperSearchService.getBaseSearch(new OrderTb(),reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            OrderTb baseQuery =(OrderTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");
            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
            if(count>0){
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);

                if (list != null && !list.isEmpty()) {
                    for (OrderTb product : list) {
                        OrmUtil<OrderTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(product);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
//        OrderTb orderTb = new OrderTb();
//        orderTb.setComid(21782L);
//        int count = commonDao.selectCountByConditions(orderTb);
//        if(count>0){
//            PageOrderConfig config = new PageOrderConfig();
//            config.setPageInfo(Integer.parseInt(reqmap.get("page")), Integer.parseInt(reqmap.get("rp")));
//            List<OrderTb> list = commonDao.selectListByConditions(orderTb, config);
//            List<Map<String, Object>> resList = new ArrayList<>();
//            if (list != null && !list.isEmpty()) {
//                for (OrderTb order : list) {
//                    OrmUtil<OrderTb> otm = new OrmUtil<>();
//                    Map<String, Object> map = otm.pojoToMap(order);
//                    resList.add(map);
//                }
//                result.put("rows", JSON.toJSON(resList));
//            }
//        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));*/
        return result;
    }

}
