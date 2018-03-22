package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityBlackUserService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.HashMap;
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

        Map searchMap = supperSearchService.getGroupOrCitySearch(zldBlackTb,reqmap);
        ZldBlackTb baseQuery =(ZldBlackTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        count = commonDao.selectCountByConditions(baseQuery,supperQuery);
        if(count>0){
            if(config==null){
                config = new PageOrderConfig();
                config.setPageInfo(null,null);
            }
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

        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
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

        return result;
    }


    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<ZldBlackTb> blackList = JSON.parseArray(result.get("rows").toString(), ZldBlackTb.class);

        logger.error("=========>>>>>>.导出黑名单" + blackList.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (blackList != null && blackList.size() > 0) {
            String[] f = new String[]{"id","car_number", "black_uuid","state",  "comid", "ctime",  "utime", "remark"};
            Map<Long, String> passNameMap = new HashMap<Long, String>();
            Map<Long, String> uinNameMap = new HashMap<Long, String>();
            for (ZldBlackTb zldBlackTb : blackList) {
                List<Object> values = new ArrayList<Object>();
                OrmUtil<ZldBlackTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(zldBlackTb);
                for (String field : f) {
                    Object v = map.get(field);
                    if("comid".equals(field)){
                        if(Check.isLong(map.get("comid")+"")){
                            String comName = getComName(Long.parseLong(map.get("comid")+""));
                            values.add(comName);
                        }else {
                            values.add(v+"");
                        }
                    }else if("state".equals(field)){
                        switch(Integer.valueOf(v + "")){
                            case 0:values.add("正常");break;
                            case 1:values.add("漂白");break;
                            default:values.add("");
                        }
                    }else {
                        values.add(v+"");
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
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

    private String getComName(Long comid){
        ComInfoTb comInfoTb  = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null&&comInfoTb.getCompanyName()!=null){
            return comInfoTb.getCompanyName();
        }
        return "";
    }

}
