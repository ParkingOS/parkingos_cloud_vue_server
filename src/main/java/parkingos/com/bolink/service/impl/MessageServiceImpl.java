package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.MessageService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.CommonUtils;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private SupperSearchService supperSearchService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CommonMethods commonMethods;

    @Override
    public JSONObject buyMessage(Integer count, Double money, String tradeNo, Long union_id, Long park_id) {
        JSONObject result = new JSONObject();
        result.put("state",0);

//        ComInfoTb comInfoTb = new ComInfoTb();
//        comInfoTb.setId(park_id);
//        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        ComInfoTb comInfoTb = commonService.getComInfoByComid(park_id);
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

        ShortMessageTb shortMessageTb = new ShortMessageTb();
        shortMessageTb.setCount(count);
        shortMessageTb.setCtime(System.currentTimeMillis()/1000);
        shortMessageTb.setMoney(new BigDecimal(money+""));
        shortMessageTb.setTradeNo(tradeNo);
        shortMessageTb.setState(0);
        shortMessageTb.setUnionId(union_id);
        shortMessageTb.setParkId(park_id);
        shortMessageTb.setUnionName(unionName);
        shortMessageTb.setParkName(parkName);
//        shortMessageTb.setEtime(TimeTools.getTwoYearsSeconds());

        int insert = commonDao.insert(shortMessageTb);
        if(insert==1) {
            result.put("state", 1);
            result.put("money",money);
            result.put("trade_no",tradeNo);
        }

        return result;
    }

    @Override
    public String notice(Long parkId, String carNumber, Long eTime, String mobile,Integer day) {
        String parkName = getParkName(parkId);
        commonUtils.sendNotice(4,parkId,carNumber,eTime,day,mobile,parkName);
        return null;
    }

    private String getParkName(Long parkId) {
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(parkId);
        comInfoTb =(ComInfoTb) commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null){
            return comInfoTb.getCompanyName();
        }
        return "";
    }

    @Override
    public JSONObject getSendTrade(Map<String, String> reqmap) {
        Long comid = Long.parseLong(reqmap.get("comid"));

        int count = getMessageCount(comid,1);
        SendMessageTb sendMessageTb = new SendMessageTb();
        sendMessageTb.setParkId(comid);
        sendMessageTb.setState(1);
        sendMessageTb.setFromGroup(0);
        JSONObject result = supperSearchService.supperSearch(sendMessageTb, reqmap);
        result.put("message_count",count);
        return result;
    }

    @Override
    public JSONObject getGroupSendTrade(Map<String, String> reqmap) {
        Long groupid = Long.parseLong(reqmap.get("groupid"));
        int count = getMessageCount(groupid,2);
        SendMessageTb sendMessageTb = new SendMessageTb();
        sendMessageTb.setGroupId(groupid);
        sendMessageTb.setState(1);
        JSONObject result = supperSearchService.supperSearch(sendMessageTb, reqmap);
        result.put("message_count",count);
        return result;
    }

    @Override
    public List<List<Object>> exportGroupSendTrade(Map<String, String> reqParameterMap) {
        reqParameterMap.remove("orderby");
        JSONObject result =getGroupSendTrade(reqParameterMap);
        List<SendMessageTb> sendList = JSON.parseArray(result.get("rows").toString(), SendMessageTb.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(sendList!=null&&sendList.size()>0){
            String [] f = new String[]{"park_info","ctime","mobile","type"};
            for(SendMessageTb sendMessageTb : sendList){
                List<Object> values = new ArrayList<Object>();
                OrmUtil<SendMessageTb> otm = new OrmUtil<SendMessageTb>();
                Map map = otm.pojoToMap(sendMessageTb);
                //判断各种字段 组装导出数据
                for(String field : f){
                    Object value = map.get(field);
                    if("type".equals(field)){
                        if(value!=null){
                            int type = (int)value;
                            if(type==1){
                                values.add("月卡模块");
                            }else if(type==2){
                                values.add("访客模块");
                            }
                            else{
                                values.add("");
                            }
                        }
                    }else{
                        if("ctime".equals(field)){
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

    @Override
    public JSONObject getSelectParks(Long groupId) {
        JSONObject result = new JSONObject();

        if(groupId<0){
            return result;
        }

        List<Object> allParks = commonMethods.getParks(groupId);
        if(allParks==null||allParks.isEmpty()){
            return result;
        }

        List<Map> resultList = new ArrayList<>();

        GroupMessageTb groupMessageTb = new GroupMessageTb();
        groupMessageTb.setGroupId(groupId);
        groupMessageTb = (GroupMessageTb)commonDao.selectObjectByConditions(groupMessageTb);

        if(groupMessageTb!=null){
            Integer selectAll = groupMessageTb.getSelectAll();
            if(selectAll==1){
                for(Object object:allParks){
                    Map map = new HashMap();
                    map.put("id",object);
                    ComInfoTb comInfoTb = commonService.getComInfoByComid((Long)object);
                    map.put("name",comInfoTb.getCompanyName());

                    SendMessageTb sendMessageTb = new SendMessageTb();
                    sendMessageTb.setParkId((Long)object);
                    sendMessageTb.setGroupId(groupId);
                    sendMessageTb.setFromGroup(1);
                    sendMessageTb.setState(1);
                    int userCount = commonDao.selectCountByConditions(sendMessageTb);
                    map.put("use_count",userCount);
                    map.put("flag",true);
                    resultList.add(map);
                }
            }else{
                String selectParks = groupMessageTb.getParks();
                String selects[] = selectParks.split(",");
                for(Object object:allParks){
                    Map map = new HashMap();
                    map.put("id",object);
                    ComInfoTb comInfoTb = commonService.getComInfoByComid((Long)object);
                    map.put("name",comInfoTb.getCompanyName());
                    map.put("flag",false);
                    SendMessageTb sendMessageTb = new SendMessageTb();
                    sendMessageTb.setParkId((Long)object);
                    sendMessageTb.setFromGroup(1);
                    sendMessageTb.setGroupId(groupId);
                    sendMessageTb.setState(1);
                    int userCount = commonDao.selectCountByConditions(sendMessageTb);
                    map.put("use_count",userCount);
                    for(String id:selects){
                        if(id.equals(object+"")){
                            map.put("flag",true);
                            break;
                        }
                    }
                    resultList.add(map);
                }
            }
        }else{
            for(Object object:allParks){
                Map map = new HashMap();
                map.put("id",object);
                ComInfoTb comInfoTb = commonService.getComInfoByComid((Long)object);
                map.put("name",comInfoTb.getCompanyName());

//                SendMessageTb sendMessageTb = new SendMessageTb();
//                sendMessageTb.setParkId((Long)object);
//                sendMessageTb.setFromGroup(1);
//                sendMessageTb.setState(1);
//                sendMessageTb.setGroupId(groupId);
//                int userCount = commonDao.selectCountByConditions(sendMessageTb);
                map.put("use_count",0);
                map.put("flag",false);
                resultList.add(map);
            }
        }
        result.put("data",resultList);
        return result;
    }

    @Override
    public JSONObject setSelectParks(Long groupId, String parks,Integer selectAll) {

        JSONObject result = new JSONObject();
        result.put("state",0);
        GroupMessageTb con = new GroupMessageTb();
        con.setGroupId(groupId);
        int count = commonDao.selectCountByConditions(con);

        int doCount = 0;
        if(selectAll==1){
            if(count>0){
                GroupMessageTb update = new GroupMessageTb();
                update.setSelectAll(1);
                update.setUtime(System.currentTimeMillis()/1000);
                doCount = commonDao.updateByConditions(update,con);
            }else{
                con.setCtime(System.currentTimeMillis()/1000);
                con.setSelectAll(1);
                doCount = commonDao.insert(con);
            }
        }else{
            String selectParks = "";
            JSONArray arr = JSON.parseArray(parks);
            if(arr!=null&&arr.size()>0) {
                for (Object object : arr) {
                    selectParks += object + ",";
                }
            }
            if(count>0){
                GroupMessageTb update = new GroupMessageTb();
                update.setSelectAll(0);
                update.setParks(selectParks);
                update.setUtime(System.currentTimeMillis()/1000);
                doCount = commonDao.updateByConditions(update,con);
            }else{
                con.setCtime(System.currentTimeMillis()/1000);
                con.setSelectAll(0);
                con.setParks(selectParks);
                doCount = commonDao.insert(con);
            }

        }

        if(doCount==1){
            result.put("state",1);
        }
        return result;

    }


    private int getMessageCount(Long id,int type) {
        ShortMessageAccount shortMessageAccount = new ShortMessageAccount();
        if(type==1) {
            shortMessageAccount.setParkId(id);
        }else if (type==2){
            shortMessageAccount.setGroupId(id);
        }
        shortMessageAccount=(ShortMessageAccount)commonDao.selectObjectByConditions(shortMessageAccount);
        if(shortMessageAccount!=null){
            return shortMessageAccount.getCount();
        }
        return 0;
    }

    @Override
    public JSONObject getBuyTrade(Map<String, String> reqmap) {
        Long comid = Long.parseLong(reqmap.get("comid"));
        int count = getMessageCount(comid,1);
        ShortMessageTb shortMessageTb = new ShortMessageTb();
        shortMessageTb.setParkId(comid);
        shortMessageTb.setState(1);
        JSONObject result = supperSearchService.supperSearch(shortMessageTb, reqmap);
        result.put("message_count",count);
        return result;
    }

    @Override
    public JSONObject getGroupBuyTrade(Map<String, String> reqmap) {
        Long groupid = Long.parseLong(reqmap.get("groupid"));
        int count = getMessageCount(groupid,2);
        ShortMessageTb shortMessageTb = new ShortMessageTb();
        shortMessageTb.setGroupId(groupid);
        shortMessageTb.setState(1);
        JSONObject result = supperSearchService.supperSearch(shortMessageTb, reqmap);
        result.put("message_count",count);
        return result;
    }

    @Override
    public List<List<Object>> exportSendTrade(Map<String, String> reqParameterMap) {
        reqParameterMap.remove("orderby");

        JSONObject result =getSendTrade(reqParameterMap);
        List<SendMessageTb> sendList = JSON.parseArray(result.get("rows").toString(), SendMessageTb.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(sendList!=null&&sendList.size()>0){
            String [] f = new String[]{"ctime","mobile","type"};
            for(SendMessageTb sendMessageTb : sendList){
                List<Object> values = new ArrayList<Object>();
                OrmUtil<SendMessageTb> otm = new OrmUtil<SendMessageTb>();
                Map map = otm.pojoToMap(sendMessageTb);
                //判断各种字段 组装导出数据
                for(String field : f){
                    Object value = map.get(field);
                    if("type".equals(field)){
                        if(value!=null){
                            int type = (int)value;
                            if(type==1){
                                values.add("月卡模块");
                            }else if(type==2){
                                values.add("访客模块");
                            }
                            else{
                                values.add("");
                            }
                        }
                    }else{
                        if("ctime".equals(field)){
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

    @Override
    public List<List<Object>> exportBuyTrade(Map<String, String> reqParameterMap,int type) {
        reqParameterMap.remove("orderby");
        JSONObject result =null;
        if(type==1){
            result = getBuyTrade(reqParameterMap);
        }else if(type==2){
            result=getGroupBuyTrade(reqParameterMap);
        }
        List<ShortMessageTb> buyList = JSON.parseArray(result.get("rows").toString(), ShortMessageTb.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(buyList!=null&&buyList.size()>0){
            String [] f = new String[]{"count","ctime","etime","money","trade_no"};
            for(ShortMessageTb shortMessageTb : buyList){
                List<Object> values = new ArrayList<Object>();
                OrmUtil<ShortMessageTb> otm = new OrmUtil<ShortMessageTb>();
                Map map = otm.pojoToMap(shortMessageTb);
                //判断各种字段 组装导出数据
                for(String field : f){
                    Object value = map.get(field);
                    if("money".equals(field)){
                        if(value!=null){
                            values.add(map.get(field)+"元");
                        }
                    }else{
                        if("ctime".equals(field)||"etime".equals(field)){
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

    @Override
    public JSONObject getCodeState(String tradeNo) {
        KfcAuthcodePay kfcAuthcodePay = new KfcAuthcodePay();
        kfcAuthcodePay.setOutTradeNo(tradeNo);
        JSONObject result = new JSONObject();
        kfcAuthcodePay=(KfcAuthcodePay)commonDao.selectObjectByConditions(kfcAuthcodePay);
        if(kfcAuthcodePay==null){
            result.put("state",2);
        }else{
            int state = kfcAuthcodePay.getPayState();
            if(state==1){
                result.put("state",1);
            }else if(state==0){
                result.put("state",2);
            }
        }

        return result;
    }

    @Override
    public JSONObject buyGroupMessage(Integer count, Double money, String tradeNo, Long groupId) {

        JSONObject result = new JSONObject();
        result.put("state",0);

        OrgGroupTb orgGroupTb = commonService.getOrgGroupById(groupId);
        if(orgGroupTb==null){
            return result;
        }

        Long union_id = -1L;
        String groupName = orgGroupTb.getName();
        if(orgGroupTb.getCityid()>0) {
            union_id = orgGroupTb.getCityid();
        }
        String unionName = "";
        if(union_id>0){
            OrgCityMerchants orgCityMerchants = commonService.getOrgCityById(union_id);
            if(orgCityMerchants!=null){
                unionName=orgCityMerchants.getName();
            }
        }

        ShortMessageTb shortMessageTb = new ShortMessageTb();
        shortMessageTb.setCount(count);
        shortMessageTb.setCtime(System.currentTimeMillis()/1000);
        shortMessageTb.setMoney(new BigDecimal(money+""));
        shortMessageTb.setTradeNo(tradeNo);
        shortMessageTb.setState(0);
        shortMessageTb.setUnionId(union_id);
        shortMessageTb.setGroupId(groupId);
        shortMessageTb.setUnionName(unionName);
        shortMessageTb.setGroupName(groupName);

        int insert = commonDao.insert(shortMessageTb);
        if(insert==1) {
            result.put("state", 1);
            result.put("money",money);
            result.put("trade_no",tradeNo);
        }

        return result;
    }
}
