package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.LiftRodTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityLiftRodService;
import parkingos.com.bolink.service.LiftRodService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

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
    @Autowired
    private LiftRodService liftRodService;

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
            if(config==null){
                config = new PageOrderConfig();
                config.setPageInfo(1,Integer.MAX_VALUE);
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            }else{
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            }
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
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        logger.error("============>>>>>返回数据"+result);
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderfield");
        reqParameterMap.remove("orderby");

        JSONObject result = selectResultByConditions(reqParameterMap);
        List<LiftRodTb> liftRodList = JSON.parseArray(result.get("rows").toString(), LiftRodTb.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(liftRodList!=null&&liftRodList.size()>0){
            String [] f = new String[]{"id","liftrod_id","ctime","uin","out_channel_id","reason","resume"};
            Map<Integer, String> reasonMap = (Map)liftRodService.getLiftReason(1);
            for(LiftRodTb liftRodTb : liftRodList){
//                List<String> values = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
                OrmUtil<LiftRodTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(liftRodTb);
                //判断各种字段 组装导出数据
                for(String field : f){
                    if("uin".equals(field)){
                        values.add(getUinName(Long.valueOf(map.get(field)+"")));
                    }else if("reason".equals(field)){
                        Integer key = Integer.valueOf(map.get(field)+"");
                        if(reasonMap.get(key)!=null)
                            values.add(reasonMap.get(key));
                        else {
                            values.add("无");
                        }
                    }else{
                        if("ctime".equals(field)){
                            if(map.get(field)!=null){
                                values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                            }else{
                                values.add("null");
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

    private String getUinName(Long uin) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);

        String uinName = "";
        if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
            uinName = userInfoTb.getNickname();
        }
        return uinName;
    }
}
