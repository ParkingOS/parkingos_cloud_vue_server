package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityLogService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CityLogServiceImpl implements CityLogService {

    Logger logger = Logger.getLogger(CityLogServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkLogTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        JSONObject result = new JSONObject();

        int count =0;
        List<ParkLogTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();



        String groupid = reqmap.get("groupid");
        String cityid = reqmap.get("cityid");
        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);

        Map searchMap = supperSearchService.getBaseSearch( new ParkLogTb(),reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            ParkLogTb baseQuery =(ParkLogTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");

            List parks =new ArrayList();

            if(groupid !=null&&!"".equals(groupid)){
                parks = commonMethods.getParks(Long.parseLong(groupid));
            }else if(cityid !=null&&!"".equals(cityid)){
                parks = commonMethods.getparks(Long.parseLong(cityid));
            }

            System.out.println("=======parks:"+parks);

            //封装searchbean  集团或城市下面所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.CONTAINS);
            searchBean.setFieldName("park_id");
            searchBean.setBasicValue(parks);

            if (supperQuery == null) {
                supperQuery = new ArrayList<>();
            }
            supperQuery.add( searchBean );


            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
            if(count>0){
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
                if (list != null && !list.isEmpty()) {
                    for (ParkLogTb parkLogTb : list) {
                        OrmUtil<ParkLogTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(parkLogTb);
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));
        logger.error("============>>>>>返回数据"+result);
        return result;
    }

}
