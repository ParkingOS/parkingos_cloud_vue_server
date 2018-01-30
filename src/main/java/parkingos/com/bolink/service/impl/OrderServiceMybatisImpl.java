package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.OrderTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.OrderTbMapper;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("mybatis")
public class OrderServiceMybatisImpl implements OrderService {

    Logger logger = Logger.getLogger(OrderServiceMybatisImpl.class);

    @Autowired
    OrderTbMapper orderTbMapper;
    @Override
    public int selectCountByConditions(OrderTb orderTb) {
        OrderTbExample example = new OrderTbExample();
        return orderTbMapper.countByExample(example);
        // return 0;
    }

    @Override
    public JSONObject selectResultByConditions(Map<String, String> map) {
        OrderTbExample example = new OrderTbExample();
//        List<OrderTbExample.Criteria> criterias = example.getOredCriteria();
//
//        example.or(false);
      // example.createCriteria().andComidEqualTo(21872L);
       //example.createCriteria().andCreateTimeGreaterThan(1515749348L);
       //example.createCriteria().andCarNumberEqualTo("苏LYLYL7");

        //example.createCriteria().andIdEqualTo(35869766L);
        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);
        List<Map<String, Object>> resList =new ArrayList<>();
        /**
         {pay_type_start=0,pay_type=0,
         trade_no=trade,
         car_number=,
         pay_time=1,  pay_time_start=1515409267000,
         orderfield=id,orderby=desc,
         card_id=,
         id=3,id_start=1111,id_end=
         user_id=between, user_id_end=33, user_id_start=22,
         }
         */
        for(String key : map.keySet()){
            switch (key){
                case "id":
                   String value = map.get("id");
                   String startValue = map.get("id_start");
                   String endValue = map.get("id_end");
                   if(value.equals("between")){
                       if(Check.isLong(startValue)&&Check.isLong(endValue)){
                           example.createCriteria().andIdBetween(Long.valueOf(startValue),Long.valueOf(endValue));
                       }
                   }else if("1".equals(value)){
                       if(Check.isLong(startValue))
                           example.createCriteria().andIdGreaterThanOrEqualTo(Long.valueOf(startValue));
                   }else if("2".equals(value)){
                       if(Check.isLong(endValue))
                           example.createCriteria().andIdLessThanOrEqualTo(Long.valueOf(endValue));
                   }else if("3".equals(value)){
                       if(Check.isLong(startValue))
                           example.createCriteria().andIdEqualTo(Long.valueOf(startValue));
                   }
                   break;
                case "":
                    break;
                default:
                    break;
            }
        }
        logger.info(example);

        int count = orderTbMapper.countByExample(example);
        result.put("total",count);
        List<OrderTb> cardRenewTbList=null;
        if(count>0)
            cardRenewTbList= orderTbMapper.selectByExample(example);
        if (cardRenewTbList != null && !cardRenewTbList.isEmpty()) {
            for (OrderTb t2 : cardRenewTbList) {
                OrmUtil<OrderTb> otm = new OrmUtil<>();
                Map<String, Object> maps = otm.pojoToMap(t2);
                resList.add(maps);
            }

            result.put("rows", JSON.toJSON(resList));
        }
        return result;
    }

    @Override
    public JSONObject getPicResult(String orderid, Long comid) {
        return null;
    }

    @Override
    public byte[] getCarPics(String orderid, Long comid, String type, Integer currentnum) {
        return new byte[0];
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {
        return null;
    }

}
