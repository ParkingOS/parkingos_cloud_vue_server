package parkingos.com.bolink.orderserver;

import com.zld.proto.*;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.utils.Check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class OrderServer extends BaseServer {
    private Logger logger = LoggerFactory.getLogger(OrderServer.class);

    @Autowired
    private OrderMapper orderMapper;

//    @RequestMapping(method = RequestMethod.GET, value = "/getList")
    public List<OrderTb> getOrdersByMapConditons(Map<String, String> map) {
//        Order u = null;
//        MapList u = null;
        OrderList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();

                u = stub.getOrdersByMapConditons(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<OrderTb> orderTbList = new ArrayList<>();
        if(u!=null) {
            for (Order order : u.getOrderList()) {
                OrderTb orderTb = getOrderTbFromOrder(order);
                orderTbList.add(orderTb);
            }
        }
//        return ResponseEntity.ok("out opark Order: 订单" + u.toString());
        return orderTbList;
    }

    private String getGrpcName(long cityId) {
        if(cityId%2==0){
            return "grpc-server2";
        }else{
            return "grpc-server1";
        }
    }

    private OrderTb getOrderTbFromOrder(Order order) {
        OrderTb orderTb = new OrderTb();
        orderTb.setComid(order.getComid());
        orderTb.setCarNumber(order.getCarNumber());
        orderTb.setId(order.getId());
        orderTb.setState(order.getState());
        orderTb.setOrderIdLocal(order.getOrderIdLocal());
        orderTb.setCarpicTableName(order.getCarpicTableName());
        orderTb.setCarType(order.getCarType());
        if(order.getEndTime()>0){
            orderTb.setEndTime(order.getEndTime());
        }
        orderTb.setAmountReceivable(new BigDecimal(order.getAmountReceivable()+""));
        orderTb.setElectronicPrepay(new BigDecimal(order.getElectronicPrepay()+""));
        orderTb.setElectronicPay(new BigDecimal(order.getElectronicPay()+""));
        orderTb.setCashPrepay(new BigDecimal(order.getCashPrepay()+""));
        orderTb.setCashPay(new BigDecimal(order.getCashPay()+""));
        orderTb.setReduceAmount(new BigDecimal(order.getReduceAmount()+""));
        orderTb.setCreateTime(order.getCreateTime());
        orderTb.setTotal(new BigDecimal(order.getTotal()+""));
        orderTb.setUin(order.getUin());
        orderTb.setcType(order.getCType());
        orderTb.setOutUid(order.getOutUid());
        orderTb.setUid(order.getUid());
        orderTb.setPayType(order.getPayType());
        orderTb.setInPassid(order.getInPassid());
        orderTb.setOutPassid(order.getOutPassid());
        orderTb.setFreereasons(order.getFreereasons());
        return orderTb;
    }


    public int selectOrdersCount(Map<String, String> map) {
        OrderCount u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return 0;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);;
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();

                u = stub.selectOrdersCount(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public Map<String,String> selectMoneyByExample(Map<String, String> map) {
        OrderMap u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();

                u = stub.selectMoneyByExample(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }
        return u.getMapMap();
    }


//    public List<OrderTb> qryOrdersByComidAndOrderId(Long comid,String orderid,String tableName) {
//        OrderList u =null;
//        ManagedChannel channel = this.createGrpcChannel("grpc-server");
//        if(channel!=null){
//            //获取
//            try{
//                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
//                Order order = Order.newBuilder()
//                        .setComid(comid)
//                        .setOrderIdLocal(orderid)
//                        .setTableName(tableName)
//                        .build();
//                u = stub.qryOrdersByComidAndOrderId(order);
//            }catch(Exception e){
//                logger.error("client to server error",e);
//            }
//        }
//        else{
//            logger.error("server is down!!");
//            return null;
//        }
//
//        List<OrderTb> list = new ArrayList<>();
//        if(u!=null) {
//            for (Order order : u.getOrderList()) {
//                OrderTb orderTb = getOrderTbFromOrder(order);
//                list.add(orderTb);
//            }
//        }
//        return list;
//    }


    public List<Map<String,String>> selectCityDayAnlysis(Map<String, String> map) {
        MapList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectCityDayAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                Map<String,String> resultMap = new HashMap<>();
                Map<String,String> map1 = orderMap.getMapMap();
                String comid = map1.get("comid");
                String parkName = getParkNameById(comid);
                resultMap.putAll(map1);
                resultMap.put("comid",parkName);
                list.add(resultMap);
            }
        }

//        List<Map<String,String>> list = new ArrayList<>();
//        if(u!=null) {
//            for (OrderMap orderMap : u.getMapList()) {
//                list.add(orderMap.getMapMap());
//            }
//        }
        return list;
    }


    public List<Map<String,String>> selectCityParkDayAnlysis(Map<String, String> map) {
        MapList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectCityParkDayAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                Map<String,String> resultMap = new HashMap<>();
                Map<String,String> map1 = orderMap.getMapMap();
                String comid = map1.get("comid");
                String parkName = getParkNameById(comid);
                resultMap.putAll(map1);
                resultMap.put("comid",parkName);
                list.add(resultMap);
            }
        }

//        List<Map<String,String>> list = new ArrayList<>();
//        if(u!=null) {
//            for (OrderMap orderMap : u.getMapList()) {
//                list.add(orderMap.getMapMap());
//            }
//        }
        return list;
    }

    public List<Map<String,String>> selectCityMonthAnlysis(Map<String, String> map) {
        MapList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectCityMonthAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }


    public List<Map<String,String>> selectParkDayAnlysis(Map<String, String> map) {
        MapList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectParkDayAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                Map<String,String> resultMap = new HashMap<>();
                Map<String,String> map1 = orderMap.getMapMap();
                String userId = map1.get("name");
                String parkName = getUserName(userId);
                resultMap.putAll(map1);
                resultMap.put("name",parkName);
                list.add(resultMap);
            }
        }

//        List<Map<String,String>> list = new ArrayList<>();
//        if(u!=null) {
//            for (OrderMap orderMap : u.getMapList()) {
//                list.add(orderMap.getMapMap());
//            }
//        }
        return list;
    }

    public List<Map<String,String>> selectParkMonthAnlysis(Map<String, String> map) {
        MapList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectParkMonthAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> selectParkCollectorAnlysis(Map<String, String> map) {
        MapList u =null;
        String cityId = map.get("cityId");
        if(cityId==null||"".equals(cityId)){
            logger.error("===>>>>cityId error:"+map);
            return null;
        }
        String grpcServer = getGrpcName(Long.parseLong(cityId));
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderMap orderMap = OrderMap.newBuilder()
                        .putAllMap(map).build();
                u = stub.selectParkCollectorAnlysis(orderMap);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                Map<String,String> resultMap = new HashMap<>();
                Map<String,String> map1 = orderMap.getMapMap();
                String userId = map1.get("name");
                String parkName = getUserName(userId);
                resultMap.putAll(map1);
                resultMap.put("name",parkName);
                list.add(resultMap);
            }
        }

//        List<Map<String,String>> list = new ArrayList<>();
//        if(u!=null) {
//            for (OrderMap orderMap : u.getMapList()) {
//                list.add(orderMap.getMapMap());
//            }
//        }
        return list;
    }


    public int resetDataByComid(Long comid,String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .build();
                u = stub.resetDataByComid(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
            return u.getCount();
        }
        else{
            logger.error("server is down!!");
            return -1;
        }

    }

    public OrderTb getSelectOrder(long l, String carNumber, String tableName,Long cityId) {
        Order u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(l)
                        .setCarNumber(carNumber)
                        .setTableName(tableName)
                        .build();

                u = stub.getSelectOrder(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }
        OrderTb orderTb = getOrderTbFromOrder(u);
        return orderTb;
    }

    public List<OrderTb> getOrdersByCars(long comid, List<String> carNumberList, String tableName,Long cityId) {
        OrderList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                OrderCars order = OrderCars.newBuilder()
                        .setComid(comid)
                        .addAllCarNumber(carNumberList)
                        .setTableName(tableName)
                        .build();//getOrderCars(comid,carNumberList,tableName);
                logger.info("===OrderCars"+order);
                u = stub.getOrdersByCars(order);
                logger.info("===OrderCars"+u);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<OrderTb> list = new ArrayList<>();
        if(u!=null) {
            for (Order order : u.getOrderList()) {
                OrderTb orderTb = getOrderTbFromOrder(order);
                list.add(orderTb);
            }
        }
        return list;
    }

//    private OrderCars getOrderCars(long comid, List<String> carNumberList, String tableName) {
//        logger.info("====carNumberList"+carNumberList+"~~"+carNumberList.size());
//        if(carNumberList!=null &&carNumberList.size()==1) {
//            OrderCars order = OrderCars.newBuilder()
//                    .setComid(comid)
//                    .setCarNumber(0, carNumberList.get(0))
//                    .setTableName(tableName)
//                    .build();
//            return order;
//        }
//
//        if(carNumberList!=null &&carNumberList.size()==2) {
//            OrderCars order = OrderCars.newBuilder()
//                    .setComid(comid)
//                    .setCarNumber(0, carNumberList.get(0))
//                    .setCarNumber(1, carNumberList.get(1))
//                    .setTableName(tableName)
//                    .build();
//            return order;
//        }
//        if(carNumberList!=null &&carNumberList.size()==3) {
//            OrderCars order = OrderCars.newBuilder()
//                    .setComid(comid)
//                    .setCarNumber(0, carNumberList.get(0))
//                    .setCarNumber(1, carNumberList.get(1))
//                    .setCarNumber(2, carNumberList.get(2))
//                    .setTableName(tableName)
//                    .build();
//            return order;
//        }
//        if(carNumberList!=null &&carNumberList.size()==2) {
//            OrderCars order = OrderCars.newBuilder()
//                    .setComid(comid)
//                    .setCarNumber(0, carNumberList.get(0))
//                    .setCarNumber(1, carNumberList.get(1))
//                    .setCarNumber(2, carNumberList.get(2))
//                    .setCarNumber(3, carNumberList.get(3))
//                    .setTableName(tableName)
//                    .build();
//            return order;
//        }
//        return null;
//    }

    public List<Map<String,String>> getEntryCar(long tday, long l, String tableName,Long cityId) {
        MapList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(l)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCar(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }
        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getExitCar(long tday, long l, String tableName,Long cityId) {
        MapList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(l)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCar(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                Map<String,String> resultMap = new HashMap<>();
                resultMap.putAll(orderMap.getMapMap());
                String parkId = orderMap.getMapMap().get("parkName");
                String parkName = getParkNameById(parkId);
                resultMap.put("parkName",parkName);
                list.add(resultMap);
            }
        }
        return list;
    }

    public List<Map<String,String>> getParkRank(long tday, int groupid, String tableName,Long cityId) {
        MapList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getParkRank(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                Map<String,String> resultMap = new HashMap<>();
                Map<String,String> map = orderMap.getMapMap();
                String parkId = map.get("parkName");
                String parkName = getParkNameById(parkId);
                resultMap.putAll(map);
                resultMap.put("parkName",parkName);
                list.add(resultMap);
            }
        }
        logger.info("getParkRank~~~~~~~~~"+list);
        return list;
    }

    public int getEntryCount(long tday, int groupid, String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCount(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public int getExitCount(long tday, int groupid, String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(groupid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCount(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public int getInparkCount(long tday, int groupid, String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setGroupid(Long.parseLong(groupid+""))
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getInparkCount(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        return u.getCount();
    }

    public List<Map<String,String>> getEntryCarByComid(long tday, int comid, String tableName,Long cityId) {
        MapList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        logger.info("=====>>>>>>grpcServer"+grpcServer+"~~~"+cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCarByComid(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        logger.info("get maplist:"+u);
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getExitCarByComid(long tday, int comid, String tableName,Long cityId) {
        MapList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCarByComid(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        logger.info("get maplist:"+u);
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
                list.add(orderMap.getMapMap());
            }
        }
        return list;
    }

    public List<Map<String,String>> getRankByout(long tday, int comid, String tableName,Long cityId) {
        MapList u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return null;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getRankByout(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<Map<String,String>> list = new ArrayList<>();
        if(u!=null) {
            for (OrderMap orderMap : u.getMapList()) {
//                list.add(orderMap.getMapMap());
                Map<String,String> resultMap = new HashMap<>();
                Map<String,String> map = orderMap.getMapMap();
                String userId = map.get("parkName");
                String userName = orderMapper.getUserInfo(Long.parseLong(userId));
                resultMap.putAll(map);
                resultMap.put("parkName",userName);
                list.add(resultMap);
            }
        }
        return list;
    }

    public int getEntryCountbc(long tday, int comid, String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getEntryCountbc(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        if(u!=null){
            return u.getCount();
        }
        return 0;
    }

    public int getExitCountbc(long tday, int comid, String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setEndTime(tday).build();
                u = stub.getExitCountbc(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        if(u!=null){
            return u.getCount();
        }
        return 0;
    }

    public int getInparkCountbc(long tday, int comid, String tableName,Long cityId) {
        OrderCount u =null;
        if(cityId==null||cityId<0){
            logger.error("===>>>>cityId error:"+cityId);
            return 0;
        }
        String grpcServer = getGrpcName(cityId);
        ManagedChannel channel = this.createGrpcChannel(grpcServer);
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setTableName(tableName)
                        .setCreateTime(tday).build();
                u = stub.getInparkCountbc(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return 0;
        }
        if(u!=null){
            return u.getCount();
        }
        return 0;
    }

    private String getParkNameById(String comid) {
        if(Check.isLong(comid)) {
            Map<String, Object> map = orderMapper.getParkNameById(Long.parseLong(comid));
            if (map != null && map.get("name") != null) {
                return map.get("name") + "";
            }
        }
        return comid;
    }

    private String getUserName(String userId) {

        if(Check.isLong(userId)) {
            String name = orderMapper.getUserInfo(Long.parseLong(userId));
            if (name != null) {
                return name;
            }
        }
        return userId;
    }

}