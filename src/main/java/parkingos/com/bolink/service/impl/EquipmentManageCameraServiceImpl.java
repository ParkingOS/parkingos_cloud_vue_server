package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComCameraTb;
import parkingos.com.bolink.service.EquipmentManageCameraService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class EquipmentManageCameraServiceImpl implements EquipmentManageCameraService {

    Logger logger = Logger.getLogger(EquipmentManageCameraServiceImpl.class);

    @Autowired
    private SupperSearchService<ComCameraTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        JSONObject result = supperSearchService.supperSearch(new ComCameraTb(),reqmap);
        /*String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        int count =0;
        List<ZldBlackTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        Map searchMap = supperSearchService.getBaseSearch(new ZldBlackTb(),reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            ZldBlackTb baseQuery =(ZldBlackTb)searchMap.get("base");
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
                    for (ZldBlackTb product : list) {
                        OrmUtil<ZldBlackTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(product);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));*/
        return result;
    }

    @Override
    public Integer insertResultByConditions(ComCameraTb comCameraTb) {
        Integer result = commonDao.insert(comCameraTb);
        return result;
    }

    @Override
    public Integer updateResultByConditions(ComCameraTb comCameraTb) {
        Integer result = commonDao.updateByPrimaryKey(comCameraTb);
        return result;
    }

    @Override
    public Integer removeResultByConditions(ComCameraTb comCameraTb) {
        Integer result = commonDao.updateByPrimaryKey(comCameraTb);
        return result;
    }

}
