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
import parkingos.com.bolink.service.ParkLogService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParkLogServiceImpl implements ParkLogService {

    Logger logger = Logger.getLogger(ParkLogServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkLogTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        JSONObject result = new JSONObject();

        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setParkId(Long.parseLong(reqmap.get("comid")));
        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        System.out.println("日期===="+date);

        Long start = null;
        Long end = null;
        if (date == null||"".equals(date)) {
            reqmap.put("operate_time","1");
            reqmap.put("operate_time_start",(TimeTools.getToDayBeginTime())+"");
            result = supperSearchService.supperSearch(parkLogTb, reqmap);
        } else {
            start = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(date.split("至")[0]);
            end = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(date.split("至")[1]);
            System.out.println("开始时间和结束时间"+start+end);
            int count = 0;
            List<ParkLogTb> list = null;
            List<Map<String, Object>> resList = new ArrayList<>();

            Map searchMap = supperSearchService.getBaseSearch(parkLogTb, reqmap);
            if (searchMap != null && !searchMap.isEmpty()) {
                ParkLogTb baseQuery = (ParkLogTb) searchMap.get("base");
                List<SearchBean> supperQuery = null;
                if (searchMap.containsKey("supper"))
                    supperQuery = (List<SearchBean>) searchMap.get("supper");
                PageOrderConfig config = null;
                if (searchMap.containsKey("config"))
                    config = (PageOrderConfig) searchMap.get("config");


                //封装searchbean  集团或城市下面所有车场
                SearchBean searchBean = new SearchBean();
                searchBean.setOperator(FieldOperator.BETWEEN);
                searchBean.setFieldName("operate_time");
                searchBean.setStartValue(start);
                searchBean.setEndValue(end);

                if (supperQuery == null) {
                    supperQuery = new ArrayList<>();
                }
                supperQuery.add(searchBean);


                count = commonDao.selectCountByConditions(baseQuery, supperQuery);
                if (count > 0) {
                    list = commonDao.selectListByConditions(baseQuery, supperQuery, config);
                    if (list != null && !list.isEmpty()) {
                        for (ParkLogTb parkLogTb1 : list) {
                            OrmUtil<ParkLogTb> otm = new OrmUtil<>();
                            Map<String, Object> map = otm.pojoToMap(parkLogTb1);
                            resList.add(map);
                        }
                        result.put("rows", JSON.toJSON(resList));
                    }
                }
            }
            result.put("total", count);
            result.put("page", Integer.parseInt(reqmap.get("page")));
        }

//        JSONObject result = supperSearchService.supperSearch(parkLogTb,reqmap);

        return result;
    }

}
