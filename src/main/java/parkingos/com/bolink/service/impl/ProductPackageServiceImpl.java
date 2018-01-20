package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarowerProduct;
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.ProductPackageService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProductPackageServiceImpl implements ProductPackageService {

    Logger logger = Logger.getLogger(ProductPackageServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ProductPackageTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setComid(Long.parseLong(reqmap.get("comid")));
        productPackageTb.setIsDelete(0L);
        JSONObject result = supperSearchService.supperSearch(productPackageTb,reqmap);
        return result;
    }

    @Override
    public Long getId() {
        return commonDao.selectSequence(ProductPackageTb.class);
    }

    @Override
    public JSONObject createProduct(ProductPackageTb productPackageTb) {

        String str = "{\"state\":0,\"msg\":\"增加失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.insert(productPackageTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","增加成功");
            int ins = insertSysn(productPackageTb,0);
            if(ins!=1){
                logger.error("=======>>>>>>插入同步表失败");
            }
        }
        return result;
    }

    @Override
    public JSONObject deleteProduct(Long id, Long comid) {

        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setPid(id);
        int count = commonDao.selectCountByConditions(carowerProduct);
        if(count>0){
            result.put("msg","该套餐已被使用，共"+count+"条，请在会员管理中解除绑定后删除");
            return result;
        }

        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setIsDelete(1L);
        productPackageTb.setId(id);
        int ret = commonDao.updateByPrimaryKey(productPackageTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","删除成功");
            productPackageTb = (ProductPackageTb)commonDao.selectObjectByConditions(productPackageTb);
            int ins = insertSysn(productPackageTb,1);
            if(ins!=1){
                logger.error("=======>>>>插入同步表失败");
            }
        }
        return result;
    }

    @Override
    public JSONObject editProduct(Map<String, String> reqParameterMap) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        //月卡套餐id
        Long id = Long.parseLong(reqParameterMap.get("id"));
        //车场编号
        Long comid = Long.parseLong(reqParameterMap.get("comid"));
        //修改时间
        Long updateTime = System.currentTimeMillis()/1000;
        //月卡套餐描述
        String describe = "";
        if(reqParameterMap.get("describe")!=null&&!"undefined".equals(reqParameterMap.get("describe"))){
            describe =StringUtils.decodeUTF8(reqParameterMap.get("describe"));
        }
        String p_name ="";
        if(reqParameterMap.get("p_name")!=null&&!"undefined".equals(reqParameterMap.get("p_name"))){
            p_name =StringUtils.decodeUTF8(reqParameterMap.get("p_name"));
        }
        String period = "";
        if(reqParameterMap.get("period")!=null&&!"undefined".equals(reqParameterMap.get("period"))){
            period =StringUtils.decodeUTF8(reqParameterMap.get("period"));
        }
        Long carTypeId =-1L;
        if(reqParameterMap.get("car_type_id")!=null){
            carTypeId = Long.parseLong(reqParameterMap.get("car_type_id"));
        }
        Double price = 0.0;//RequestUtil.getDouble(request, "price", 0.0);
        if(reqParameterMap.get("price")!=null){
            price = Double.parseDouble(reqParameterMap.get("price"));
        }
        BigDecimal bigDecimal = new BigDecimal(price+"");

        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setId(id);
        productPackageTb.setUpdateTime(updateTime);
        productPackageTb.setComid(comid);
        productPackageTb.setCarTypeId(carTypeId+"");
        productPackageTb.setPrice(bigDecimal);
        productPackageTb.setpName(p_name);
        productPackageTb.setDescribe(describe);
        productPackageTb.setPeriod(period);

        int ret = commonDao.updateByPrimaryKey(productPackageTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","修改成功");
            int ins = insertSysn(productPackageTb,1);
            if(ins!=1){
                logger.error("=======>>>>>插入同步表失败");
            }
        }
        return result;
    }

    private int insertSysn(ProductPackageTb productPackageTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(productPackageTb.getComid());
        syncInfoPoolTb.setTableId(productPackageTb.getId());
        syncInfoPoolTb.setTableName("product_package_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        return commonDao.insert(syncInfoPoolTb);
    }

}
