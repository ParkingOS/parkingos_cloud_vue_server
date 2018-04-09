package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.MonitorInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.EquipmentManageMonitorService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentManageMonitorServiceImpl implements EquipmentManageMonitorService {

    Logger logger = Logger.getLogger(EquipmentManageMonitorServiceImpl.class);


    @Autowired
    private SupperSearchService<MonitorInfoTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonMethods commonMethods;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        String comid = reqmap.get("comid");
        MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
        monitorInfoTb.setState(1);
        monitorInfoTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(monitorInfoTb,reqmap);

        return result;
    }

    @Override
    public Integer insertResultByConditions(MonitorInfoTb monitorInfoTb) {
        Integer result = commonDao.insert(monitorInfoTb);
        return result;
    }

    @Override
    public Integer updateResultByConditions(MonitorInfoTb monitorInfoTb) {
        Integer result = commonDao.updateByPrimaryKey(monitorInfoTb);
        return result;
    }

    @Override
    public Integer removeResultByConditions(MonitorInfoTb monitorInfoTb) {
        Integer result = commonDao.updateByPrimaryKey(monitorInfoTb);
        return result;
    }

    @Override
    public JSONObject selectGroupMonitors(Map<String, String> reqmap) {

        JSONObject result = new JSONObject();

        int count =0;
        List<MonitorInfoTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
        monitorInfoTb.setState(1);

        String groupid = reqmap.get("groupid");
        System.out.println("获得集团下面所有监控=====groupid:"+groupid);

        Map searchMap = supperSearchService.getBaseSearch( monitorInfoTb,reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            MonitorInfoTb baseQuery =(MonitorInfoTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");

            List parks =new ArrayList();

            if(groupid !=null&&!"".equals(groupid)){
                parks = commonMethods.getParks(Long.parseLong(groupid));
            }

            List<String> parksCon = new ArrayList<>();
            for(Object parkid:parks){
                parksCon.add(parkid+"");
            }

            System.out.println("=======parks:"+parks);

            //封装searchbean  集团或城市下面所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.CONTAINS);
            searchBean.setFieldName("comid");
            searchBean.setBasicValue(parksCon);

            if (supperQuery == null) {
                supperQuery = new ArrayList<>();
            }
            supperQuery.add( searchBean );


            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
            if(count>0){
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
                if (list != null && !list.isEmpty()) {
                    for (MonitorInfoTb monitorInfoTb1 : list) {
                        OrmUtil<MonitorInfoTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(monitorInfoTb1);
                        if(map.get("comid")!=null){
                            Long comid= Long.parseLong(map.get("comid")+"");
                            ComInfoTb comInfoTb = new ComInfoTb();
                            comInfoTb.setId(comid);
                            comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
                            map.put("comid",comInfoTb.getCompanyName());
                        }
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
