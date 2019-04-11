package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zld.proto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.OrderTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.CityUnorderService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service("unorderSpring")
public class CityUnorderServiceImpl implements CityUnorderService {

    Logger logger = LoggerFactory.getLogger(CityUnorderServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderServer orderServer;
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    SaveLogService saveLogService;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        List<OrderTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();

        String groupIdStr = reqmap.get("groupid");
        Long cityID = -1L;
        if(!Check.isEmpty(groupIdStr)){
            Long groupId = Long.parseLong(groupIdStr);
            cityID = orderMapper.getCityIdByGroupId(groupId);
        }
        if(cityID!=null&&cityID>-1){
            reqmap.put("cityId",cityID+"");
            reqmap.put("tableName","order_tb_new_"+cityID%100);
        }else{
            reqmap.put("tableName","order_tb_new");
        }


        reqmap.put("state","0");
        reqmap.put("state_start","0");

        String createTime = reqmap.get("create_time");
        logger.error("===>>>createTime"+createTime);
//        组装 今天 参数
        if(createTime==null||"undefined".equals(createTime)||"".equals(createTime)){
            reqmap.put("create_time","1");
            reqmap.put("create_time_start",(TimeTools.getToDayBeginTime()+""));
            logger.error("=========..req"+reqmap.size());
        }
        String rp = "20";
        if(reqmap.get("rp")!=null){
            rp = reqmap.get("rp");
        }

//        count = getOrdersCountByGroupid(reqmap);
        count = orderServer.selectOrdersCount(reqmap);
        if(count>0){
            if(reqmap.get("export")==null){//不是导出
                reqmap.put("rp",rp);
            }
//            list = getOrdersListByGroupid(reqmap);
            list=orderServer.getOrdersByMapConditons(reqmap);
            if (list != null && !list.isEmpty()) {
                for (OrderTb orderTb1 : list) {
                    OrmUtil<OrderTb> otm = new OrmUtil<OrderTb>();
                    Map<String, Object> map = otm.pojoToMap(orderTb1);
                    Long start = (Long) map.get("create_time");
                    if (start != null ) {
                        map.put("duration", StringUtils.getTimeString(start, System.currentTimeMillis()/1000));
                    } else {
                        map.put("duration","");
                    }
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }

        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        logger.error("============>>>>>返回数据"+result);
        return result;
    }

    private List<OrderTb> getOrdersListByGroupid(Map<String, String> reqmap) {
        OrderTbExample example = new OrderTbExample();
        example.createCriteria().andIshdEqualTo(0);
        example.createCriteria().andStateEqualTo(0);

        example = ExampleUtis.createOrderExample(example,reqmap);
        logger.info("example~~~~~~~~"+example);
        List<OrderTb> orders = orderMapper.selectOrders(example);
        return orders;
    }

    private int getOrdersCountByGroupid(Map<String, String> reqmap) {
        OrderTbExample example = new OrderTbExample();
        example.createCriteria().andIshdEqualTo(0);
        example.createCriteria().andStateEqualTo(0);
        reqmap.remove("rp");
        example = ExampleUtis.createOrderExample(example,reqmap);
        int count = orderMapper.selectOrdersCount(example);
        logger.info("count~~~~~~~~"+count);
        return count;
    }


    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("rp");
        //标记为导出
        reqParameterMap.put("export","1");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<OrderTb> orderlist = JSON.parseArray(result.get("rows").toString(), OrderTb.class);

        logger.error("=========>>>>>>.导出订单" + orderlist.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (orderlist != null && orderlist.size() > 0) {
            String[] f = new String[]{"id","comid", "uid","c_type", "car_number", "create_time", "duration",  "state", "in_passid","order_id_local"};
            Map<Long, String> passNameMap = new HashMap<Long, String>();
            Map<Long, String> uinNameMap = new HashMap<Long, String>();
            for (OrderTb orderTb : orderlist) {
                List<Object> values = new ArrayList<Object>();
                OrmUtil<OrderTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(orderTb);
                for (String field : f) {
                    Object v = map.get(field);
                    if(v == null)
                        v = "";
                    if("c_type".equals(field)){
                        try{
                            switch(Integer.valueOf(v + "")){
                                case 0:values.add("NFC刷卡");break;
                                case 1:values.add("Ibeacon");break;
                                case 2:values.add("手机扫牌");break;
                                case 3:values.add("通道扫牌");break;
                                case 4:values.add("直付");break;
                                case 5:values.add("全天月卡");break;
                                case 6:values.add("车位二维码");break;
                                case 8:values.add("分段月卡");break;
                                default:values.add("");
                            }
                        }catch (Exception e){
                            values.add(v);
                        }
                    }else if("comid".equals(field)){
                        if (Check.isLong(v+"")){
                            Long comid =  Long.parseLong(v+"");
                            values.add(getComName(comid));
                        }else {
                            values.add("");
                        }
                    }else if ("uid".equals(field) || "out_uid".equals(field)) {
                        Long uid = -1L;
                        if (Check.isLong(v + ""))
                            uid = Long.valueOf(v + "");
                        if (uinNameMap.containsKey(uid)) {
                            values.add(uinNameMap.get(uid));
                        } else {
                            String name = getUinName(Long.valueOf(map.get(field) + ""));
                            values.add(name);
                            uinNameMap.put(uid, name);
                        }
                    } else if ("pay_type".equals(field)) {
                        switch (Integer.valueOf(v + "")) {//0:NFC,1:IBeacon,2:照牌   3通道照牌 4直付 5月卡用户
                            case 1: values.add("现金支付");break;
                            case 2: values.add("手机支付");break;
                            case 3: values.add("包月");break;
                            case 8: values.add("免费");break;
                            default: values.add("");
                        }
                    } else if("comid".equals(field)){
                        ComInfoTb comInfoTb = new ComInfoTb();
                        if(v!=null&&Check.isNumber(v+"")){
                            comInfoTb.setId(Long.parseLong(v+""));
                            comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
                            if(comInfoTb!=null){
                                values.add(comInfoTb.getCompanyName());
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add(v+"");
                        }
                    }else if("duration".equals(field)){
                        Long start = (Long)map.get("create_time");
                        if(start!=null){
                            values.add(StringUtils.getTimeString(start, System.currentTimeMillis()/1000));
                        }else{
                            values.add("");
                        }
                    }else if("state".equals(field)){
                        switch(Integer.valueOf(v + "")){
                            case 1:values.add("已支付");break;
                            case 0:values.add("未支付");break;
                            default:values.add("");
                        }
                    }else if("in_passid".equals(field) || "out_passid".equals(field)){
                        if (!"".equals(v.toString()) && Check.isNumber(v.toString())) {
                            Long passId = Long.valueOf(v.toString());
                            if (passNameMap.containsKey(passId))
                                values.add(passNameMap.get(passId));
                            else {
                                String passName = getPassName(passId);
                                values.add(passName);
                                passNameMap.put(passId, passName);
                            }
                        } else {
                            values.add(v + "");
                        }
                    }else if("create_time".equals(field) || "end_time".equals(field)){
                        if(!"".equals(v.toString())){
                            values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((v+""))*1000));
                        }else{
                            values.add("");
                        }
                    }else{
                        values.add(v + "");
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }

    @Override
    public JSONObject toZero(Long id, Long cityid, Long createTime,String money,Long groupId,Long uin,String nickname) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        String tableName = "order_tb_new";
        if(cityid>0){
            tableName+="_"+cityid%100;
            if(createTime>0){
                String date = TimeTools.getTime_yyyyMMdd_HHmmss(createTime*1000);
                date = date.substring(0,7).replace("-","");
                tableName+= "_"+date;
            }
        }
        logger.info("===>>>>>零元结算："+tableName+"~"+id);

        int update = orderServer.toZero(id,cityid,tableName,money);
        if(update==1){
            result.put("state",1);
            result.put("msg","零元结算成功");
            ExecutorService es = ExecutorsUtil.getExecutorService();
            es.execute(new Runnable() {
                @Override
                public void run() {

                    String tableName = "order_tb_new";
                    if(cityid>0){
                        tableName+="_"+cityid%100;
                        if(createTime>0){
                            String date = TimeTools.getTime_yyyyMMdd_HHmmss(createTime*1000);
                            date = date.substring(0,7).replace("-","");
                            tableName+= "_"+date;
                        }
                    }

                    Order order = Order.newBuilder()
                            .setId(id)
                            .setTableName(tableName)
                            .setCityid(cityid)
                            .build();
                    OrderTb orderTb = orderServer.qryOrderById(order);

                    boolean issend = commonUtils.sendMessage(orderTb, 2,tableName);
                    logger.info("........发送月卡数据" + issend);
                    int ins = insertSysn(orderTb, 1,tableName);

                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
                    parkLogTb.setOperateType(1);
                    parkLogTb.setContent(uin+"("+nickname+")"+"集团零元结算订单："+id);
                    parkLogTb.setType("vip");
                    parkLogTb.setGroupId(groupId);
                    saveLogService.saveLog(parkLogTb);

                }
            });

        }
        return result;
    }


    private int insertSysn(OrderTb orderTb, Integer operater,String tablename) {
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(orderTb.getComid());
        syncInfoPoolTb.setTableId(orderTb.getId());
        syncInfoPoolTb.setTableName(tablename);
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis() / 1000);
        syncInfoPoolTb.setOperate(operater);
        return commonDao.insert(syncInfoPoolTb);
    }

    private String getPassName(Long passId) {
        ComPassTb comPassTb = new ComPassTb();
        comPassTb.setId(passId);
        comPassTb = (ComPassTb)commonDao.selectObjectByConditions(comPassTb);
        if(comPassTb!=null&&comPassTb.getPassname()!=null){
            return comPassTb.getPassname();
        }
        return "";
    }

    private String getUinName(Long uin) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
        if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
            return userInfoTb.getNickname();
        }
        return "";
    }

    private String getComName(Long comid){
        ComInfoTb comInfoTb  = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null&&comInfoTb.getCompanyName()!=null){
            return comInfoTb.getCompanyName();
        }
        return "";
    }
}
