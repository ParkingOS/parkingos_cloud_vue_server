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
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.service.ProductPackageService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.ArrayList;
import java.util.List;
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

        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        int count =0;
        List<ProductPackageTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        Map searchMap = supperSearchService.getBaseSearch(new ProductPackageTb(),reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            ProductPackageTb baseQuery =(ProductPackageTb)searchMap.get("base");
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
                    for (ProductPackageTb product : list) {
                        OrmUtil<ProductPackageTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(product);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));
        return result;
    }

}
