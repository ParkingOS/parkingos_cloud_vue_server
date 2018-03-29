package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityParkService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CityParkServiceImpl implements CityParkService {

    Logger logger = Logger.getLogger(CityParkServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ComInfoTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        List<ComInfoTb> list =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        ComInfoTb comInfoTb = new ComInfoTb();
//        comInfoTb.setState(0);

//        Map searchMap = supperSearchService.getGroupOrCitySearch(liftRodTb,reqmap);
//        LiftRodTb baseQuery =(LiftRodTb)searchMap.get("base");
//        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
//        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");
//
//        count = commonDao.selectCountByConditions(baseQuery,supperQuery);
//        if(count>0){
//            list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
//            if (list != null && !list.isEmpty()) {
//                for (LiftRodTb liftRodTb1 : list) {
//                    OrmUtil<LiftRodTb> otm = new OrmUtil<>();
//                    Map<String, Object> map = otm.pojoToMap(liftRodTb1);
//                    resList.add(map);
//                }
//                result.put("rows", JSON.toJSON(resList));
//            }
//        }

        String groupid = reqmap.get("groupid");
        String cityid = reqmap.get("cityid");
        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);

        Map searchMap = supperSearchService.getBaseSearch(comInfoTb,reqmap);
        logger.info(searchMap);
        if(searchMap!=null&&!searchMap.isEmpty()){
            ComInfoTb baseQuery =(ComInfoTb)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>)searchMap.get("supper");
            PageOrderConfig config = null;
            if(searchMap.containsKey("config"))
                config = (PageOrderConfig)searchMap.get("config");

            List parks =new ArrayList();

            if(groupid !=null&&!"".equals(groupid)){
                parks = commonMethods.getParks(Long.parseLong(groupid));
            }else if(cityid !=null&&!"".equals(cityid)){
                parks = commonMethods.getparks(Long.parseLong(cityid));
            }

            System.out.println("=======parks:"+parks);


            if(parks==null||parks.size()<1){
                return result;
            }

            //封装searchbean  城市和集团下所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.CONTAINS);
            searchBean.setFieldName("id");
            searchBean.setBasicValue(parks);

            SearchBean searchBean1  = new SearchBean();
            searchBean1.setOperator(FieldOperator.CONTAINS);
            searchBean1.setFieldName("state");
            ArrayList stateList = new ArrayList<Integer>();
            stateList.add(0);
            stateList.add(2);
            searchBean1.setBasicValue(stateList);

            if (supperQuery == null) {
                supperQuery = new ArrayList<>();
            }
            supperQuery.add( searchBean );
            supperQuery.add(searchBean1);

            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
            if(count>0){
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
                if (list != null && !list.isEmpty()) {
                    for (ComInfoTb comInfoTb1 : list) {
                        OrmUtil<ComInfoTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(comInfoTb1);
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

    @Override
    public JSONObject createPark(HttpServletRequest request) {

        JSONObject result= new JSONObject();
        result.put("state",0);
        result.put("msg","创建车场失败");

        String bolinkid = RequestUtil.getString(request,"bolink_id");
        System.out.println("创建车场===bolinkid:"+bolinkid);
        if(bolinkid!=null&&!"".equals(bolinkid)){
            if(Check.isNumber(bolinkid)){
                //验证填写的泊链编号在yun是不是重复
                Long comid = Long.parseLong(bolinkid);
                ComInfoTb comInfoTb = new ComInfoTb();
                comInfoTb.setId(comid);
                int count = commonDao.selectCountByConditions(comInfoTb);
                if(count>0){
                    result.put("msg","创建失败,编号重复");
                    return result;
                }
            }
        }

        Long id = RequestUtil.getLong(request,"id",-1L);
        Long groupId = RequestUtil.getLong(request,"groupid",-1L);
        String company =StringUtils.decodeUTF8(RequestUtil.processParams(request, "company_name"));
        company = company.replace("\r", "").replace("\n", "");
        String address =StringUtils.decodeUTF8(RequestUtil.processParams(request, "address"));
        address = address.replace("\r", "").replace("\n", "");
        String mobile =RequestUtil.processParams(request, "mobile");
        if(mobile.length()>15){
            result.put("msg","手机号输入有误");
            return result;
        }
        Integer parking_total =RequestUtil.getInteger(request, "parking_total", 0);
        Integer state =RequestUtil.getInteger(request, "state", 0);
        Integer city = RequestUtil.getInteger(request, "city", 0);
        Double longitude =RequestUtil.getDouble(request, "longitude",0d);
        Double latitude =RequestUtil.getDouble(request, "latitude",0d);


        if(longitude == 0 || latitude == 0){
            result.put("msg","请标注地理位置");
            return result;
        }

        //判断地图位置是否冲突
        ComInfoTb newCominfoTb = new ComInfoTb();
        newCominfoTb.setLongitude(new BigDecimal(longitude));
        newCominfoTb.setLatitude(new BigDecimal(latitude));
        int count = commonDao.selectCountByConditions(newCominfoTb);
        if(count>0){
            result.put("msg","地理位置冲突，请重新标注!");
            return result;
        }


        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setState(state);
        comInfoTb.setCompanyName(company);
        comInfoTb.setLatitude(new BigDecimal(latitude));
        comInfoTb.setLongitude(new BigDecimal(longitude));
        comInfoTb.setAddress(address);
        comInfoTb.setCity(city);
        comInfoTb.setMobile(mobile);
        comInfoTb.setParkingTotal(parking_total);
        comInfoTb.setBolinkId(bolinkid);

        if(id==-1){
            if(groupId==null||groupId==-1){
                groupId = RequestUtil.getLong(request,"group_id",-1L);
                if(groupId==null||groupId==-1){
                    result.put("msg","请选择运营集团");
                    return result;
                }
            }
            //获取id
            Long comid = commonDao.selectSequence(ComInfoTb.class);
            comInfoTb.setId(comid);
            comInfoTb.setGroupid(groupId);
            //添加自动生成车场16位秘钥的逻辑
            String ukey = StringUtils.createRandomCharData(16);
            comInfoTb.setUkey(ukey);
            comInfoTb.setCreateTime(System.currentTimeMillis()/1000);
            int insert = commonDao.insert(comInfoTb);
            if(insert==1){
                result.put("state",1);
                result.put("msg","新建车场成功");
            }
        }else{
            comInfoTb.setId(id);
            comInfoTb.setUpdateTime(System.currentTimeMillis()/1000);
            int update = commonDao.updateByPrimaryKey(comInfoTb);
            if(update==1){
                result.put("state",1);
                result.put("msg","修改车场成功");
            }
        }
        return result;
    }

    @Override
    public JSONObject editpark(ComInfoTb comInfoTb) {
        JSONObject result= new JSONObject();
        result.put("state",0);
        result.put("msg","修改车场失败");

        Double longitude =comInfoTb.getLongitude().doubleValue();
        Double latitude =comInfoTb.getLatitude().doubleValue();
        if(longitude == 0 || latitude == 0){
            result.put("msg","请标注地理位置");
            return result;
        }

        //判断地图位置是否冲突
        ComInfoTb newCominfoTb = new ComInfoTb();
        newCominfoTb.setLongitude(new BigDecimal(longitude));
        newCominfoTb.setLatitude(new BigDecimal(latitude));
        int count = commonDao.selectCountByConditions(newCominfoTb);
        if(count>0){
            result.put("msg","地理位置冲突，请重新标注!");
            return result;
        }

        //重新处理参数 封装
        String company =comInfoTb.getCompanyName();
        company = company.replace("\r", "").replace("\n", "");
        comInfoTb.setCompanyName(company);
        String address =comInfoTb.getAddress();
        address = address.replace("\r", "").replace("\n", "");
        comInfoTb.setAddress(address);

        if(comInfoTb.getId()!=null&&comInfoTb.getId()!=-1){
            comInfoTb.setUpdateTime(System.currentTimeMillis()/1000);
            int update = commonDao.updateByPrimaryKey(comInfoTb);
            if(update==1){
                result.put("state",1);
                result.put("msg","修改车场成功");
            }
        }

        return result;
    }

    @Override
    public JSONObject deletepark(ComInfoTb comInfoTb) {
        JSONObject result= new JSONObject();
        result.put("state",0);
        result.put("msg","删除失败");

        int count = commonDao.updateByPrimaryKey(comInfoTb);
        if(count ==1){
            result.put("state",1);
            result.put("msg","删除成功");
        }
        return result;
    }

    @Override
    public JSONObject setpark(Long comid) {

        JSONObject result = new JSONObject();

        List<Map<String, Object>> parkList = commonDao.getObjectBySql("select * from com_info_tb where id="+comid);
//        Integer parking_type = 0;
        String info="";
        if(parkList!=null){
            info ="名称："+parkList.get(0).get("company_name")+"，地址："+parkList.get(0).get("address")+"<br/>创建时间："
                    + TimeTools.getTime_yyyyMMdd_HHmm((Long)parkList.get(0).get("create_time")*1000)+"，车位总数："+parkList.get(0).get("parking_total")
                    +"，分享车位："+parkList.get(0).get("share_number")+"，经纬度：("+parkList.get(0).get("longitude")+","+parkList.get(0).get("latitude")+")";
//            parking_type = (Integer)parkList.get(0).get("parking_type");
            result.put("info",info);
        }
        return result;
    }
}
