package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkAccountTb;
import parkingos.com.bolink.service.ParkAccountService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class ParkAccountServiceImpl implements ParkAccountService {

    Logger logger = Logger.getLogger(ParkAccountServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkAccountTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        JSONObject result = supperSearchService.supperSearch(new ParkAccountTb(),reqmap);
        /*String str = "{\"total\":12,\"page\":1,\"money\":0.0,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        int count =0;
        List<ParkAccountTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        Map searchMap = supperSearchService.getBaseSearch(new ParkAccountTb(),reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            ParkAccountTb baseQuery =(ParkAccountTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");
            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
            if(count>0){
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
                Double total = 0.0;
                if (list != null && !list.isEmpty()) {
                    for (ParkAccountTb product : list) {
                        OrmUtil<ParkAccountTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(product);
                        if(map.get("amount")!=null){
                            total += Double.parseDouble(map.get("amount")+"");
                        }
                        resList.add(map);
                    }
                    result.put("money",total);
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));*/
        return result;
    }

}
