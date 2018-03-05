package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.LiftRodTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityLiftRodService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CityLiftRodServiceImpl implements CityLiftRodService {

    Logger logger = Logger.getLogger(CityLiftRodServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<LiftRodTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        List<LiftRodTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        LiftRodTb liftRodTb = new LiftRodTb();

        Map searchMap = supperSearchService.getGroupOrCitySearch(liftRodTb,reqmap);
        LiftRodTb baseQuery =(LiftRodTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        count = commonDao.selectCountByConditions(baseQuery,supperQuery);
        if(count>0){
            list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            if (list != null && !list.isEmpty()) {
                for (LiftRodTb liftRodTb1 : list) {
                    OrmUtil<LiftRodTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap(liftRodTb1);
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }

//        String groupid = reqmap.get("groupid");
//        String cityid = reqmap.get("cityid");
//        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);
//
//        Map searchMap = supperSearchService.getBaseSearch(liftRodTb,reqmap);
//        logger.info(searchMap);
//        if(searchMap!=null&&!searchMap.isEmpty()){
//            LiftRodTb baseQuery =(LiftRodTb)searchMap.get("base");
//            List<SearchBean> supperQuery = null;
//            if(searchMap.containsKey("supper"))
//                supperQuery = (List<SearchBean>)searchMap.get("supper");
//            PageOrderConfig config = null;
//            if(searchMap.containsKey("config"))
//                config = (PageOrderConfig)searchMap.get("config");
//
//            List parks =new ArrayList();
//
//            if(groupid !=null&&!"".equals(groupid)){
//                parks = commonMethods.getParks(Long.parseLong(groupid));
//            }else if(cityid !=null&&!"".equals(cityid)){
//                parks = commonMethods.getparks(Long.parseLong(cityid));
//            }
//
//            System.out.println("=======parks:"+parks);
//
//            //封装searchbean  城市和集团下所有车场的抬杆记录
//            SearchBean searchBean = new SearchBean();
//            searchBean.setOperator(FieldOperator.CONTAINS);
//            searchBean.setFieldName("comid");
//            searchBean.setBasicValue(parks);
//
//            if (supperQuery == null) {
//                supperQuery = new ArrayList<>();
//            }
//            supperQuery.add( searchBean );
//
//            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
//            if(count>0){
//                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
//                if (list != null && !list.isEmpty()) {
//                    for (LiftRodTb liftRodTb1 : list) {
//                        OrmUtil<LiftRodTb> otm = new OrmUtil<>();
//                        Map<String, Object> map = otm.pojoToMap(liftRodTb1);
//                        resList.add(map);
//                    }
//                    result.put("rows", JSON.toJSON(resList));
//                }
//            }
//        }
        result.put("total",count);
        result.put("page",Integer.parseInt(reqmap.get("page")));
        logger.error("============>>>>>返回数据"+result);
        return result;
    }
}
