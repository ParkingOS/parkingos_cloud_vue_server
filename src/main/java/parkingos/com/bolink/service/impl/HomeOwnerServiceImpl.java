package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.HomeOwnerTb;
import parkingos.com.bolink.service.HomeOwnerService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HomeOwnerServiceImpl implements HomeOwnerService {

    Logger logger = Logger.getLogger(HomeOwnerServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<HomeOwnerTb> supperSearchService;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        HomeOwnerTb homeOwnerTb = new HomeOwnerTb();
        homeOwnerTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(homeOwnerTb,reqmap);
        return result;
    }

    @Override
    public JSONObject deleteOwner(Long id) {
        return null;
    }

    @Override
    public JSONObject addOwner(HomeOwnerTb homeOwnerTb) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        result.put("msg","新建业主失败");
        int insert = commonDao.insert(homeOwnerTb);
        if(insert==1){
            result.put("state",1);
            result.put("msg","新建业主成功");
        }
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {
        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<HomeOwnerTb> blackList = JSON.parseArray(result.get("rows").toString(), HomeOwnerTb.class);

        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (blackList != null && blackList.size() > 0) {
            String[] f = new String[]{"name","home_number", "phone","identity_card",  "state", "remark"};
            for (HomeOwnerTb homeOwnerTb : blackList) {
                List<Object> values = new ArrayList<Object>();
                OrmUtil<HomeOwnerTb> otm = new OrmUtil<HomeOwnerTb>();
                Map map = otm.pojoToMap(homeOwnerTb);
                for (String field : f) {
                    Object v = map.get(field);
                    if("state".equals(field)){
                        switch(Integer.valueOf(v + "")){
                            case 0:values.add("正常");break;
                            case 1:values.add("禁用");break;
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
}
