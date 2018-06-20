package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.service.CarTypeService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.VisitorService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitorServiceImpl implements VisitorService {

    Logger logger = Logger.getLogger(VisitorServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<VisitorTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        VisitorTb visitorTb = new VisitorTb();
        visitorTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(visitorTb,reqmap);
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {
        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<VisitorTb> blackList = JSON.parseArray(result.get("rows").toString(), VisitorTb.class);

        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (blackList != null && blackList.size() > 0) {
            String[] f = new String[]{"id","car_number", "mobile","create_time",  "begin_time", "end_time",  "state", "remark"};
            Map<Long, String> passNameMap = new HashMap<Long, String>();
            Map<Long, String> uinNameMap = new HashMap<Long, String>();
            for (VisitorTb visitorTb : blackList) {
                List<Object> values = new ArrayList<Object>();
                OrmUtil<VisitorTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(visitorTb);
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
                            case 0:values.add("待审批");break;
                            case 1:values.add("已通过");break;
                            default:values.add("已拒绝");
                        }
                    }else if(field.contains("time")){
                        if(v!=null&&!"".equals(v.toString())){
                            values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((v+""))*1000));
                        }else{
                            values.add("");
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

    @Override
    public JSONObject updateVisitor(VisitorTb visitorTb) {
        String str = "{\"state\":0,\"msg\":\"操作失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int update = commonDao.updateByPrimaryKey(visitorTb);
        if(update==1){
            visitorTb = (VisitorTb)commonDao.selectObjectByConditions(visitorTb);
            insertSysn(visitorTb,1);
            result.put("state",1);
            result.put("msg","更改成功");
        }

        return result;
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


    private void insertSysn(VisitorTb visitorTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(visitorTb.getComid());
        syncInfoPoolTb.setTableId(visitorTb.getId());
        syncInfoPoolTb.setTableName("visitor_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        commonDao.insert(syncInfoPoolTb);
    }
}
