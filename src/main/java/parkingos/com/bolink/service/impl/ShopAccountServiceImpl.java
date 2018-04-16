package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ShopAccountTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopAccountServiceImpl implements ShopAcccountService {

    Logger logger = Logger.getLogger(ShopAccountServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ShopAccountTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count = 0;
        List<ShopAccountTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<>();

        ShopAccountTb shopAccountTb = new ShopAccountTb();
        //登陆停车场
        shopAccountTb.setParkId(Long.valueOf(reqmap.get("comid")));
        //流水类型
        if (reqmap.get("operate_type") != null && !reqmap.get("operate_type").equals("")) {
            Integer operate_type = Integer.valueOf(reqmap.get("operate_type"));
            if (operate_type != 0) {
                shopAccountTb.setOperateType(operate_type);
            }
        }

        Map searchMap = supperSearchService.getBaseSearch(shopAccountTb, reqmap);
        List<SearchBean> supperQuery = null;
        ShopAccountTb baseQuery = null;
        PageOrderConfig config = null;

        if (searchMap != null && !searchMap.isEmpty()) {
            baseQuery = (ShopAccountTb) searchMap.get("base");
            if (searchMap.containsKey("supper")) {
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            }
            if (searchMap.containsKey("config"))
                config = (PageOrderConfig) searchMap.get("config");
        }

        List<SearchBean> nameList = new ArrayList<>();
        SearchBean searchBean = new SearchBean();
        nameList.add(searchBean);
        Map<Long, String> names = null;
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setComid(Long.valueOf(reqmap.get("comid")));
        //操作人名模糊查询
        String nickname = reqmap.get("nickname");

        if (nickname != null && !nickname.equals("")) {
            searchBean.setFieldName("nickname");
            searchBean.setOperator(FieldOperator.LIKE);
            searchBean.setBasicValue(nickname);

            List<UserInfoTb> searchUsers = commonDao.selectListByConditions(userInfoTb, nameList);

            if (searchUsers != null && !searchUsers.isEmpty()) {
                searchBean.setOperator(FieldOperator.CONTAINS);
                searchBean.setFieldName("operator");
                List<Long> idList = new ArrayList<Long>();
                names = new HashMap<>();
                for (UserInfoTb u : searchUsers) {
                    idList.add(u.getId());
                    names.put(u.getId(), u.getNickname());
                }
                searchBean.setBasicValue(idList);

                if (supperQuery == null) {
                    supperQuery = new ArrayList<>();
                }
                supperQuery.add(searchBean);

            } else {
                //没有查询结果
                result.put("total", 0);
                result.put("total", "");
                result.put("page", Integer.parseInt(reqmap.get("page")));
                return result;
            }
        }
        logger.info(searchMap);

        count = commonDao.selectCountByConditions(baseQuery, supperQuery);
        if (count > 0) {
            list = commonDao.selectListByConditions(baseQuery, supperQuery, config);

            if (names == null) {
                names = new HashMap<>();
                //查询名称
                List<Long> idList = new ArrayList<>();

                for (ShopAccountTb s : list) {
                    if (!idList.contains(s.getOperator()))
                        idList.add(s.getOperator());
                }

                searchBean.setFieldName("id");
                searchBean.setOperator(FieldOperator.CONTAINS);
                searchBean.setBasicValue(idList);

                List<UserInfoTb> users = commonDao.selectListByConditions(userInfoTb, nameList);
                if (users != null && !users.isEmpty()) {
                    for (UserInfoTb u : users) {
                        names.put(u.getId(), u.getNickname());
                    }
                }
            }

            if (list != null && !list.isEmpty()) {
                //查询商户额度类型
                ShopTb shopTb = new ShopTb();
                shopTb.setState(0);
                shopTb.setComid(Long.valueOf(reqmap.get("comid")));

                List<ShopTb> shops = commonDao.selectListByConditions(shopTb);
                Map<Long, Integer> shopUnits = new HashMap<>();

                if (shops != null && !shops.isEmpty()) {
                    for (ShopTb shop : shops) {
                        shopUnits.put(shop.getId(), shop.getTicketUnit());
                    }
                }

                for (ShopAccountTb product : list) {
                    OrmUtil<ShopAccountTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap(product);
                    map.put("nickname", names.get(product.getOperator()));
                    map.put("ticket_unit", shopUnits.get(Long.valueOf(product.getShopId())));
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }

        }
        result.put("total", count);
        result.put("page", Integer.parseInt(reqmap.get("page")));
        logger.info(result);
        return result;
    }

    @Override
    public Map<String, Object> getShopInfoByid(Long shopid) {
        Map<String, Object> resultMap = new HashMap<>();
        ShopTb shopTb = new ShopTb();
        shopTb.setId(shopid);
        shopTb = (ShopTb) commonDao.selectObjectByConditions(shopTb);
        if (shopTb != null) {
            OrmUtil<ShopTb> otm = new OrmUtil<>();
            resultMap = otm.pojoToMap(shopTb);
            return resultMap;
        }
        return null;
    }

    @Override
    public int updateShopById(ShopTb shopTb) {

        return commonDao.updateByPrimaryKey(shopTb);
    }

    @Override
    public JSONObject getRecharge(Map<String, String> reqmap) {
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        ShopAccountTb shopAccountTb = new ShopAccountTb();
        shopAccountTb.setShopId(Integer.parseInt(reqmap.get("shopid")));


        if (reqmap.get("operate_type") != null && !reqmap.get("operate_type").equals("")) {
            Integer operate_type = Integer.valueOf(reqmap.get("operate_type"));
            shopAccountTb.setOperateType(operate_type);
        }

        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        System.out.println("日期====" + date);

        Long start = null;
        Long end = null;
        if (date == null || "".equals(date)) {
            start = TimeTools.getToDayBeginTime();
            end = TimeTools.getToDayBeginTime()+86399;
        } else {
            start = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(date.split("至")[0]);
            end = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(date.split("至")[1]);
        }
        System.out.println("开始时间和结束时间" + start + end);
        int count = 0;
        List<ShopAccountTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<>();

        Map searchMap = supperSearchService.getBaseSearch(shopAccountTb, reqmap);
        if (searchMap != null && !searchMap.isEmpty()) {
            ShopAccountTb baseQuery = (ShopAccountTb) searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if (searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            PageOrderConfig config = null;
            if (searchMap.containsKey("config"))
                config = (PageOrderConfig) searchMap.get("config");


            //封装searchbean  集团或城市下面所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.BETWEEN);
            searchBean.setFieldName("operate_time");
            searchBean.setStartValue(start);
            searchBean.setEndValue(end);

            if (supperQuery == null) {
                supperQuery = new ArrayList<>();
            }
            supperQuery.add(searchBean);


            count = commonDao.selectCountByConditions(baseQuery, supperQuery);
            if (count > 0) {

                //获得该商户的优惠单位
                ShopTb shopTb = new ShopTb();
                shopTb.setId(Long.parseLong(reqmap.get("shopid")));
                shopTb = (ShopTb)commonDao.selectObjectByConditions(shopTb);
                if (shopTb!=null){
                    logger.info("该商户的优惠单位:"+shopTb.getTicketUnit());
                }

                list = commonDao.selectListByConditions(baseQuery, supperQuery, config);
                if (list != null && !list.isEmpty()) {
                    for (ShopAccountTb shopAccountTb1 : list) {
                        OrmUtil<ShopAccountTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(shopAccountTb1);
                        if(shopTb!=null){
                            map.put("ticket_unit",shopTb.getTicketUnit());
                        }
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total", count);
        result.put("page", Integer.parseInt(reqmap.get("page")));

        return result;
    }

    @Override
    public Integer getTicketUnitById(Long shopid) {
        ShopTb shopTb = new ShopTb();
        shopTb.setId(shopid);
        shopTb = (ShopTb)commonDao.selectObjectByConditions(shopTb);
        if(shopTb!=null){
            return shopTb.getTicketUnit();
        }
        return null;
    }
}
