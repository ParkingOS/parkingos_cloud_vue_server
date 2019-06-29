package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.GroupLogMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.PrepayCardTb;
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

    Logger logger = LoggerFactory.getLogger(CityLogServiceImpl.class);

//    @Autowired
//    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkLogTb> supperSearchService;
    @Autowired
    GroupLogMapper groupLogMapper;
    @Autowired
    private CommonMethods commonMethods;



//    @Override
//    public JSONObject selectResultByConditions(Map<String, String> reqParameterMap) {
//
////        JSONObject result = new JSONObject();
//
//        ParkLogTb parkLogTb = new ParkLogTb();
//
//        Long groupid = Long.parseLong(reqParameterMap.get("groupid"));
//
//        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
//        JSONObject result = JSONObject.parseObject(str);
//        int count =0;
//        List<ParkLogTb> list =null;
//        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();
//        Map searchMap = supperSearchService.getBaseSearch(parkLogTb,reqParameterMap);
//        if(searchMap!=null&&!searchMap.isEmpty()){
//            ParkLogTb t1 =(ParkLogTb)searchMap.get("base");
//            List<SearchBean> supperQuery = null;
//            if(searchMap.containsKey("supper")) {
//                supperQuery = (List<SearchBean>) searchMap.get("supper");
//            }
//            PageOrderConfig config = null;
//            if(searchMap.containsKey("config")) {
//                config = (PageOrderConfig) searchMap.get("config");
//            }
//            if(supperQuery==null){
//                supperQuery = new ArrayList<>();
//                List parks =new ArrayList();
//
//                if(groupid !=null&&!"".equals(groupid)){
//                    parks = commonMethods.getParks(groupid);
//                }
//                if(parks!=null&&!parks.isEmpty()){
//
//                    //封装searchbean  集团或者城市下面所有车场
//                    SearchBean searchBean = new SearchBean();
//                    searchBean.setOperator(FieldOperator.CONTAINS);
//                    searchBean.setFieldName("park_id");
//                    searchBean.setBasicValue(parks);
//
//                    supperQuery.add(searchBean);
//                }else{
//                    return result;
//                }
//            }
//
//            count = commonDao.selectCountByConditions(t1,supperQuery);
//            if(count>0){
//                if (config == null) {
//                    config = new PageOrderConfig();
//                    config.setPageInfo(1, Integer.MAX_VALUE);
//                }
//                list  = commonDao.selectListByConditions(t1,supperQuery,config);
//                if (list != null && !list.isEmpty()) {
//                    for (ParkLogTb whiteListTb1 : list) {
//                        OrmUtil<ParkLogTb> otm = new OrmUtil<>();
//                        Map<String, Object> map = otm.pojoToMap(whiteListTb1);
//                        resList.add(map);
//                    }
//
//                }
//
//                result.put("rows", JSON.toJSON(resList));
//            }
//        }
//        result.put("total",count);
//        if(reqParameterMap.get("page")!=null){
//            result.put("page",Integer.parseInt(reqParameterMap.get("page")));
//        }
//        return result;
//
//
////        parkLogTb.setGroupId(Long.parseLong(reqmap.get("groupid")));
////        result = supperSearchService.supperSearch(parkLogTb,reqmap);
////        return result;
//    }

    @Override
    public JSONObject cityQuery(Map<String, String> reqParameterMap) {
        Long cityId= Long.parseLong(reqParameterMap.get("cityid"));
        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setCityId(cityId);
        JSONObject result = supperSearchService.supperSearch(parkLogTb,reqParameterMap);
        return  result;
    }

    @Override
    public JSONObject getGroupLog(Long groupid, Long logId, Integer operateType, Long parkId, String typeStart, String remark, Long timeStart, Long timeEnd, Integer rp, Integer page) {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();
        result.put("rows", JSON.toJSON(resList));
        result.put("page",page);
        result.put("total",0);

        if(parkId<0){
            //查询所有
            List parks = commonMethods.getParks(groupid);
            if(parks==null||parks.isEmpty()){
                return result;
            }
        }

        if(timeStart>0){
            timeStart = timeStart/1000;
        }
        if(timeEnd>0){
            timeEnd = timeEnd/1000;
        }

        int count=groupLogMapper.getCount(groupid, logId, operateType, parkId, typeStart, remark, timeStart, timeEnd);
        if(count>0){
            Integer offset = (page-1)*rp;
            resList = groupLogMapper.getRows(groupid, logId, operateType, parkId, typeStart, remark, timeStart, timeEnd,rp,offset);
        }
        result.put("rows", JSON.toJSON(resList));
        result.put("page",page);
        result.put("total",count);
        return result;
    }

}
