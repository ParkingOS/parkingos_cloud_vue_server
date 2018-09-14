package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ParkuserWorkRecordTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.MemberService;
import parkingos.com.bolink.service.ParkuserWorkRecordService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkuserWorkRecordServiceImpl implements ParkuserWorkRecordService {

    Logger logger = Logger.getLogger(ParkuserWorkRecordServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkuserWorkRecordTb> supperSearchService;
    @Autowired
    private MemberService memberService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        ParkuserWorkRecordTb parkuserWorkRecordTb = new ParkuserWorkRecordTb();
        parkuserWorkRecordTb.setParkId(Long.parseLong(reqmap.get("comid")));
        JSONObject result = new JSONObject();
//        JSONObject result = supperSearchService.supperSearch(parkuserWorkRecordTb,reqmap);
//        List<Map<String,Object>> rows = (List)result.get("rows");
        List<Long> userIdList= new ArrayList<Long>();
        if(reqmap.get("uinName")!=null&&!"".equals(reqmap.get("uinName"))){
            Map<String, String> newMap = new HashMap<String, String>();
            newMap.put("comid",reqmap.get("comid"));
            String uinName = reqmap.get("uinName");
            newMap.put("nickname",uinName);
            newMap.put("fieldsstr","nickname");
            JSONObject users = memberService.selectResultByConditions(newMap);
            List<Map<String,Object>> userList = (List)users.get("rows");
            if(userList.size()>0){
                for(Map<String,Object> user:userList){
                    userIdList.add((Long)user.get("id"));
                }
            }
        }

        Map<String,Object> searchMap = supperSearchService.getBaseSearch(parkuserWorkRecordTb,reqmap);
        int count =0;
        List<ParkuserWorkRecordTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();
        if(searchMap!=null&&!searchMap.isEmpty()){
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");

            //组装查询bean   FieldOperator.NOT = not in;  auth_flag 不等于14,15
            SearchBean bean = null;
            if(userIdList.size()>0){
                bean = new SearchBean();
                bean.setOperator(FieldOperator.CONTAINS);
                bean.setFieldName("uid");
                bean.setBasicValue(userIdList);
            }else if(reqmap.get("uinName")!=null&&!"".equals(reqmap.get("uinName"))){
                result.put("total",count);
                result.put("rows",resList);
                return result;
            }


            //把bean对象放到高级查询中
            if(supperQuery==null){
                supperQuery = new ArrayList<>();
            }
            if(bean!=null) {
                supperQuery.add(bean);
            }

            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");
            count = commonDao.selectCountByConditions(parkuserWorkRecordTb,supperQuery);
            logger.error("======>>>>>"+count);
            if(count>0){
                if(config==null){
                    config = new PageOrderConfig();
                    config.setPageInfo(1,Integer.MAX_VALUE);
                }
                list = commonDao.selectListByConditions(parkuserWorkRecordTb,supperQuery,config);

                if (list != null && !list.isEmpty()) {
                    Map<Long,String> nameMap = new HashMap<>();
                    for (ParkuserWorkRecordTb workRecordTb : list) {
                        OrmUtil<ParkuserWorkRecordTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(workRecordTb);

                        Long uid = (Long)map.get("uid");
                        if(nameMap.containsKey(uid)){
                            map.put("uidAndName",nameMap.get(uid)+"("+uid+")");
                        }else{
                            UserInfoTb userInfoTb = new UserInfoTb();
                            String name = "";
                            if(uid!=null&&uid>-1){
                                userInfoTb.setId(uid);
                                userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
                                if(userInfoTb!=null){
                                    name = userInfoTb.getNickname();
                                }
                            }
                            nameMap.put(uid,name);
                            map.put("uidAndName",name+"("+uid+")");
                        }
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total",count);
        return result;
    }

}
