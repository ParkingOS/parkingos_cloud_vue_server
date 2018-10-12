package parkingos.com.bolink.controller;

import com.zld.proto.*;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;
import parkingos.com.bolink.models.OrderTb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
//@RequestMapping("/grpc")
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(OrderServiceController.class);


//    @RequestMapping(method = RequestMethod.GET, value = "/getList")
    public List<OrderTb> getOrdersByMapConditons(Map<String, String> map) {
//        Order u = null;
//        MapList u = null;
        OrderList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(Order order:u.getOrderList()){
            OrderTb orderTb = getOrderTbFromOrder(order);
            orderTbList.add(orderTb);
        }
//        return ResponseEntity.ok("out opark Order: 订单" + u.toString());
        return orderTbList;
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
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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


    public List<OrderTb> qryOrdersByComidAndOrderId(Long comid,String orderid,String tableName) {
        OrderList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
        if(channel!=null){
            //获取
            try{
                OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
                Order order = Order.newBuilder()
                        .setComid(comid)
                        .setOrderIdLocal(orderid)
                        .setTableName(tableName)
                        .build();
                u = stub.qryOrdersByComidAndOrderId(order);
            }catch(Exception e){
                logger.error("client to server error",e);
            }
        }
        else{
            logger.error("server is down!!");
            return null;
        }

        List<OrderTb> list = new ArrayList<>();
        for(Order order:u.getOrderList()){
            OrderTb orderTb = getOrderTbFromOrder(order);
            list.add(orderTb);
        }
        return list;
    }


    public List<Map<String,String>> selectCityDayAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(OrderMap orderMap:u.getMapList()){
            list.add(orderMap.getMapMap());
        }
        return list;
    }


    public List<Map<String,String>> selectCityParkDayAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(OrderMap orderMap:u.getMapList()){
            list.add(orderMap.getMapMap());
        }
        return list;
    }

    public List<Map<String,String>> selectCityMonthAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(OrderMap orderMap:u.getMapList()){
            list.add(orderMap.getMapMap());
        }
        return list;
    }


    public List<Map<String,String>> selectParkDayAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(OrderMap orderMap:u.getMapList()){
            list.add(orderMap.getMapMap());
        }
        return list;
    }

    public List<Map<String,String>> selectParkMonthAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(OrderMap orderMap:u.getMapList()){
            list.add(orderMap.getMapMap());
        }
        return list;
    }

    public List<Map<String,String>> selectParkCollectorAnlysis(Map<String, String> map) {
        MapList u =null;
        ManagedChannel channel = this.createGrpcChannel("grpc-server");
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
        for(OrderMap orderMap:u.getMapList()){
            list.add(orderMap.getMapMap());
        }
        return list;
    }
}