package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityBlackUserService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CityBlackUserServiceImpl implements CityBlackUserService {

    Logger logger = Logger.getLogger(CityBlackUserServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ZldBlackTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        List<ZldBlackTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        //查询某一车场
        logger.error("=========..req"+reqmap.size());
        ZldBlackTb zldBlackTb = new ZldBlackTb();

        String comidStr = reqmap.get("comid_start");

        if(comidStr!=null&&!"".equals(comidStr)){
            Long comid = Long.parseLong(comidStr);
            if(comid>-1){
                zldBlackTb.setComid(comid);
            }
        }


//        String groupid = reqmap.get("groupid");
//        String cityid = reqmap.get("cityid");
//        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);

        Map searchMap = supperSearchService.getGroupOrCitySearch(zldBlackTb,reqmap);
        ZldBlackTb baseQuery =(ZldBlackTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        count = commonDao.selectCountByConditions(baseQuery,supperQuery);
        if(count>0){
            list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            if (list != null && !list.isEmpty()) {
                for (ZldBlackTb zldBlackTb1 : list) {
                    OrmUtil<ZldBlackTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap(zldBlackTb1);
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }

//        Map searchMap = supperSearchService.getBaseSearch(zldBlackTb,reqmap);
//        logger.info(searchMap);
//        if(searchMap!=null&&!searchMap.isEmpty()){
//            ZldBlackTb baseQuery =(ZldBlackTb)searchMap.get("base");
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
//            //封装searchbean  集团和城市下面所有车场
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
//
//            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
//            if(count>0){
//                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
//                if (list != null && !list.isEmpty()) {
//                    for (ZldBlackTb zldBlackTb1 : list) {
//                        OrmUtil<ZldBlackTb> otm = new OrmUtil<>();
//                        Map<String, Object> map = otm.pojoToMap(zldBlackTb1);
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

    @Override
    public JSONObject editBlackUser(Long id ,Integer state) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        ZldBlackTb zldBlackTb = new ZldBlackTb();
        zldBlackTb.setId(id);

        int res =0;
        if(state==0)
            state=1;
        else if(state==1)
            state=0;

        zldBlackTb.setState(state);
        zldBlackTb.setUtime(System.currentTimeMillis()/1000);

        int flag =state;
        if(state==1){
            flag  = 2;
        }
        if(id!=-1)
            res = commonDao.updateByPrimaryKey(zldBlackTb);
        if(res==1){
            ZldBlackTb zldBlackTb1 = new ZldBlackTb();
            zldBlackTb1.setId(id);
            zldBlackTb1 = (ZldBlackTb)commonDao.selectObjectByConditions(zldBlackTb1);
            if(zldBlackTb1!=null&&zldBlackTb1.getState()!=null){
                insertSysn(zldBlackTb1,1);
            }
            result.put("state",1);
            result.put("msg","修改成功");
        }

//        int ret = commonDao.updateByPrimaryKey(zldBlackTb);
//        if(ret==1){
//            zldBlackTb = (ZldBlackTb)commonDao.selectObjectByConditions(zldBlackTb);
//            int ins = insertSysn(zldBlackTb,1);
//            if(ins!=1){
//                logger.error("======>>>>插入同步表失败");
//            }
//            result.put("state",1);
//            result.put("msg","修改成功");
//        }
        return result;
    }

    private int  insertSysn(ZldBlackTb zldBlackTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(zldBlackTb.getComid());
        syncInfoPoolTb.setTableId(zldBlackTb.getId());
        syncInfoPoolTb.setTableName("zld_black_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        return commonDao.insert(syncInfoPoolTb);
    }

}
