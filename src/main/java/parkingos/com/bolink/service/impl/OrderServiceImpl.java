package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.models.CarpicTb;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.MongoClientFactory;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("orderSpring")
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
        orderTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(orderTb, reqmap);
        String str = "{\"total\":12,\"page\":1,\"parkinfo\":\"场内停车52辆,临停车52辆,空车位:948辆\",\"rows\":[]}";
        return result;
    }

    @Override
    public JSONObject getPicResult(Long orderid, Long comid) {

        String str = "{\"in\":[],\"out\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        DB db = MongoClientFactory.getInstance().getMongoDBBuilder("zld");
        //根据订单编号查询出mongodb中存入的对应个表名
        //Map map = daService.getMap("select * from order_tb where order_id_local=? and comid=?", new Object[]{orderid,comid});
        OrderTb orderTb = new OrderTb();
        orderTb.setOrderIdLocal(orderid+"");
        orderTb.setComid(comid);
        orderTb = (OrderTb) commonDao.selectObjectByConditions(orderTb);
        String collectionName = "";
        if(orderTb !=null && orderTb.getCarpicTableName()!=null){
            collectionName = orderTb.getCarpicTableName();
        }
        logger.error("====>>获得订单图片..collectionName"+collectionName);
//        DBCollection collection = db.getCollection("collectionName");
        DBCollection collection = db.getCollection(collectionName);
        logger.error("======>>>>.获取订单图片..."+collection);

        List<String> inlist = new ArrayList<>();
        List<String> outlist = new ArrayList<>();
        inlist.add("/order/carpicsup?comid="+comid+"&typeNew=in&orderid="+orderid);
        outlist.add("/order/carpicsup?comid="+comid+"&typeNew=out&orderid="+orderid);

        if(collection!=null){
            BasicDBObject document = new BasicDBObject();
            document.put("parkid", String.valueOf(comid));
            document.put("orderid", orderid+"");
            document.put("gate", "in");
            Long insize  = collection.count(document);
            document.put("gate", "out");
            Long outsize =collection.count(document);

            if(insize>1){
                for (int i = 0; i <insize ; i++) {
                    inlist.add("/order/carpicsup?comid="+comid+"&typeNew=in&currentnum="+i+"&orderid="+orderid);
                }
            }
            if(outsize>1){
                for (int i = 0; i <outsize ; i++) {
                    outlist.add("/order/carpicsup?comid="+comid+"&typeNew=out&currentnum="+i+"&orderid="+orderid);
                }
            }
        }

        result.put("in", JSON.toJSON(inlist));
        result.put("out", JSON.toJSON(outlist));
        return result;
    }

    @Override
    public byte[] getCarPics(Long orderid, Long comid, String type, Integer currentnum) {
        logger.error("getcarPic from mongodb file:orderid="+orderid+"type="+type+",comid:"+comid+",currentnum="+currentnum);
        if(orderid!=null && type !=null){
            DB db = MongoClientFactory.getInstance().getMongoDBBuilder("zld");//
            //根据订单编号查询出mongodb中存入的对应个表名
            //Map map = daService.getMap("select * from carpic_tb where order_id=? and comid=?", new Object[]{orderidlocal,String.valueOf(comid)});
            CarpicTb carpicTb = new CarpicTb();
            carpicTb.setOrderId(orderid+"");
            carpicTb.setComid(comid+"");
            carpicTb = (CarpicTb)commonDao.selectObjectByConditions(carpicTb);

            logger.error(carpicTb);
            String collectionName = "";
            if(carpicTb !=null && carpicTb.getCarpicTableName()!=null){
                collectionName = carpicTb.getCarpicTableName();
            }

            logger.error("table:"+collectionName);
            if(collectionName==null||"".equals(collectionName)||"null".equals(collectionName)){
                logger.error(">>>>>>>>>>>>>查询图片错误........");
                return new byte[0];
            }

            DBCollection collection = db.getCollection(collectionName);
            if(collection != null){
                BasicDBObject document = new BasicDBObject();
                document.put("parkid", String.valueOf(comid));
                document.put("orderid", orderid+"");
                document.put("gate", type);
                if(currentnum>=0){
                    document.put("currentnum", currentnum);
                }
                DBObject obj  = collection.findOne(document);
                if(obj == null){
                    logger.error("取图片错误.....");
                    return new byte[0];
                }
                byte[] content = (byte[])obj.get("content");
                db.requestDone();
                logger.error("取图片成功.....大小:"+content.length);
                System.out.println("mongdb over.....");
                return content;

            }else{
                return new byte[0];
//                response.sendRedirect("http://sysimages.tq.cn/images/webchat_101001/common/kefu.png");
            }
        }else {
            return new byte[0];
//            response.sendRedirect("http://sysimages.tq.cn/images/webchat_101001/common/kefu.png");
        }
    }

    @Override
    public List<List<String>> exportExcel(Map<String, String> reqParameterMap) {
        JSONObject result = selectResultByConditions(reqParameterMap);
        List<OrderTb> orderlist = JSON.parseArray(result.get("rows").toString(),OrderTb.class);

        logger.error("=========>>>>>>.导出订单" + orderlist.size());

        List<List<String>> bodyList = new ArrayList<List<String>>();
        if(orderlist!=null&&orderlist.size()>0){
//            mongoDbUtils.saveLogs( request,0, 5, "导出会员数量："+list.size());
            String [] f = new String[]{"id","p_name","mobile"/*,"uin"*/,"name","car_number","create_time","b_time","e_time","total","car_type_id","limit_day_type","remark"};
            for(OrderTb orderTb : orderlist){
                List<String> values = new ArrayList<String>();
                OrmUtil<OrderTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(orderTb);
                for(String field : f){
                    if("p_name".equals(field)){
                        if(map.get("pid")!= null) {
                            ProductPackageTb productPackageTb = getProduct(Long.parseLong(map.get("pid") + ""));
                            if(productPackageTb!=null){
                                values.add(productPackageTb.getpName());
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add("");
                        }
                    }else if("car_type_id".equals(field)){
                        if(map.get("car_type_id")!= null){
                            CarTypeTb carTypeTb = null;
                            try{
                                carTypeTb = getCarType(Long.parseLong(map.get("car_type_id") + ""));
                            }catch (Exception e){
                                values.add(map.get("car_type_id")+"");
                            }
                            if(carTypeTb!=null){
                                values.add(carTypeTb.getName());
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add("");
                        }
                    }else if("limit_day_type".equals(field)){
                        if(map.get("limit_day_type")!=null){
                            if((Integer)map.get("limit_day_type")==0){
                                values.add("不限行");
                            }else if((Integer)map.get("limit_day_type")==1){
                                values.add("限行");
                            }
                        }else{
                            values.add("");
                        }
                    } else{
                        if("create_time".equals(field)||"b_time".equals(field)||"e_time".equals(field)){
                            if(map.get(field)!=null){
                                values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                            }else {
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

    private CarTypeTb getCarType(long car_type_id) {
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(car_type_id);
        return (CarTypeTb)commonDao.selectObjectByConditions(carTypeTb);
    }

    private ProductPackageTb getProduct(long pid) {
        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setId(pid);
        return (ProductPackageTb)commonDao.selectObjectByConditions(productPackageTb);
    }

}
