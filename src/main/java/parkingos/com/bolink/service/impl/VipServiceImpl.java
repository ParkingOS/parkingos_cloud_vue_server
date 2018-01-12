package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zld.common_dao.dao.CommonDao;
import com.zld.common_dao.qo.PageOrderConfig;
import com.zld.common_dao.qo.SearchBean;
import com.zld.common_dao.util.OrmUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.models.CarowerProduct;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.VipService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class VipServiceImpl implements VipService {

    Logger logger = Logger.getLogger(VipServiceImpl.class);

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private SupperSearchService<CarowerProduct> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        logger.info(reqmap);
        //测试期间设置登录有效期为1小时
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);
        int count =0;
        List<CarowerProduct> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        Map searchMap = supperSearchService.getBaseSearch(new CarowerProduct(),reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            CarowerProduct baseQuery =(CarowerProduct)searchMap.get("base");
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
                    for (CarowerProduct product : list) {
                        OrmUtil<CarowerProduct> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(product);
                        if (map.get("car_type_id") != null && !"".equals(map.get("car_type_id"))) {
                            Long carTypeId = Long.parseLong(map.get("car_type_id")+"");
                            CarTypeTb carTypeTb = new CarTypeTb();
                            if (carTypeId != null) {
                                carTypeTb.setId(carTypeId);
                                carTypeTb = (CarTypeTb) commonDao.selectObjectByConditions(carTypeTb);
                            }
                            if (carTypeTb != null && carTypeTb.getName() != null) {
                                map.put("car_type_id", carTypeTb.getName());
                            }
                        }
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }

//        CarowerProduct carowerProduct = new CarowerProduct();
//        carowerProduct.setComId(21782L);
//        carowerProduct.setIsDelete(0L);
//        int count = commonDao.selectCountByConditions(carowerProduct);

//        if (count > 0) {
//            /**分页处理*/
//            PageOrderConfig config = new PageOrderConfig();
//            config.setPageInfo(Integer.parseInt(reqmap.get("page")), Integer.parseInt(reqmap.get("rp")));
//            List<CarowerProduct> list = commonDao.selectListByConditions(carowerProduct, config);
//            List<Map<String, Object>> resList = new ArrayList<>();
//            if (list != null && !list.isEmpty()) {
//                for (CarowerProduct product : list) {
//                    OrmUtil<CarowerProduct> otm = new OrmUtil<>();
//                    Map<String, Object> map = otm.pojoToMap(product);
//                    if (map.get("car_type_id") != null && !"".equals(map.get("car_type_id"))) {
//                        Long carTypeId = Long.parseLong(map.get("car_type_id")+"");
//                        CarTypeTb carTypeTb = new CarTypeTb();
//                        if (carTypeId != null) {
//                            carTypeTb.setId(carTypeId);
//                            carTypeTb = (CarTypeTb) commonDao.selectObjectByConditions(carTypeTb);
//                        }
//                        if (carTypeTb != null && carTypeTb.getName() != null) {
//                            map.put("car_type_id", carTypeTb.getName());
//                        }
//                    }
//                    resList.add(map);
//                }
//                result.put("rows", JSON.toJSON(resList));
//            }
//        }
        result.put("total", count);
        result.put("page", Integer.parseInt(reqmap.get("page")));

        return result;
    }

    @Override
    public Long getkey() {
        return commonDao.selectSequence(CarowerProduct.class);
    }

    @Override
    public Integer selectCountByConditions(CarowerProduct carowerProduct) {
        return commonDao.selectCountByConditions(carowerProduct);
    }
}
