package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.GetDataService;
import parkingos.com.bolink.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetDataServiceImpl implements GetDataService {

    Logger logger = Logger.getLogger(GetDataServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonMethods commonMethods;


    @Override
    public String  getNicknameById(Long id) {
        String str = "{\"nickname\":\"无\"}";
        JSONObject result = JSONObject.parseObject(str);
        if(id>0) {
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setId(id);
            userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
            if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
                String nickname = userInfoTb.getNickname();
                result.put("nickname",nickname);
            }
        }
        return result.toJSONString();
    }


    @Override
    public String getCarType(Long comid, Long groupid) {
//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";

        List<Map<String,Object>> tradsList =null;

        if(comid!=-1){
            String sql = "select id,name from car_type_tb  where comid ="+comid+" and is_delete=0";
            tradsList = commonDao.getObjectBySql(sql);
        }else if(groupid!=-1){
            String sql = "select id,name from car_type_tb  where comid in (select id from com_info_tb where groupid="+groupid+") and is_delete=0";
            tradsList = commonDao.getObjectBySql(sql);
        }

        if(tradsList!=null&&tradsList.size()>0){
            for(int i = 0;i<tradsList.size();i++){
                Map map= tradsList.get(i);
                if(i==0){
                    result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("name")+"\"}";
                }else{
                    result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("name")+"\"}";
                }
            }
        }

//        if(tradsList!=null&&tradsList.size()>0){
//            for(Map map : tradsList){
//                result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("name")+"\"}";
//            }
//        }
        result+="]";
        return result;
    }

    @Override
    public String getprodsum(Long prodId, Integer months) {

        Double total = 0d;
        if(prodId != null && prodId > 0 && months != null && months > 0){
            Double price = 0d;
            ProductPackageTb productPackageTb = new ProductPackageTb();
            productPackageTb.setId(prodId);
            productPackageTb = (ProductPackageTb) commonDao.selectObjectByConditions(productPackageTb);
            if(productPackageTb!=null&&productPackageTb.getPrice()!=null){
                price = Double.parseDouble(productPackageTb.getPrice()+"");
            }
            total = months*price;
        }
        return total+"";
    }

    @Override
    public String getpname(Long comid) {
//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";
        logger.error("=========>>>>>comid"+comid);
        if(comid!=-1){
            logger.error("开始获取套餐");
            String sql = "select id,p_name from product_package_tb where (comid="+comid;
            List<Map<String,Object>>  pList = null;
            ComInfoTb comInfoTb = new ComInfoTb();
            comInfoTb.setPid(comid);
            List comsList = commonDao.selectListByConditions(comInfoTb);
            Object[] parm = new Object[comsList.size()+1];
            parm[0] = comid;
            for (int i = 1; i < parm.length; i++) {
                long comidoth = Long.parseLong(((Map)comsList.get(i-1)).get("id")+"");
                parm[i] = comidoth;
                sql += " or comid ="+parm[i];
            }
            logger.error("======>>>>>>获取月卡套餐"+sql);
            pList = commonDao.getObjectBySql(sql +") and is_delete=0 ");
            if(pList!=null&&pList.size()>0){
                for(int i = 0;i<pList.size();i++){
                    Map map = pList.get(i);
                    if(i==0){
                        result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("p_name")+"\"}";
                    }else{
                        result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("p_name")+"\"}";
                    }
                }
            }
//            if(pList!=null&&pList.size()>0){
//                for(Map map : pList){
//                    result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("p_name")+"\"}";
//                }
//            }
        }
        result+="]";
        return result;
    }

    @Override
    public String getalluser(Long comid, Long groupid) {
        String sql = "";
        if(groupid!=-1&&comid!=-1){
            sql = "select id,nickname from user_info_tb where (comid="+comid+" or groupid="+groupid+") and state=0 and auth_flag in(1,2)";
        }else if(comid!=-1){
            sql = "select id,nickname from user_info_tb where comid="+comid+" and state=0 and auth_flag in(1,2)";
        }

        List<Map<String, Object>> tradsList =commonDao.getObjectBySql(sql);
//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"全部\"}";
        String result = "[";
        if(tradsList!=null&&tradsList.size()>0){
            for(int i = 0;i<tradsList.size();i++){
                Map map= tradsList.get(i);
                String nickname = (String)map.get("nickname");
                if(StringUtils.isEmpty(nickname) || "null".equals(nickname)){
                    nickname = "无";
                }
                if(i==0){
                    result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+nickname+"\"}";
                }else{
                    result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+nickname+"\"}";
                }
            }
        }

//        if(tradsList!=null&&tradsList.size()>0){
//            for(Map map : tradsList){
//                String nickname = (String)map.get("nickname");
//                if(StringUtils.isEmpty(nickname) || "null".equals(nickname)){
//                    nickname = "无";
//                }
//                result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("nickname")+"\"}";
//            }
//        }
        result+="]";
        return result;
    }

    @Override
    public String getMonitorName(String comid) {
       // String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";
        logger.error("=========>>>>>comid="+comid);
        if(!comid.equals("-1")){
            String sql = "select id,name from monitor_info_tb where state = 1 and comid = "+"\'"+comid+"\'";
            List<Map<String,Object>>  pList = null;
            logger.error(sql);
            pList = commonDao.getObjectBySql(sql);
            if(pList!=null&&pList.size()>0){
                for(int i = 0;i<pList.size();i++){
                    Map map = pList.get(i);
                    if(i==0){
                        result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("name")+"\"}";
                    }else{
                        result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("name")+"\"}";
                    }
                }
            }
        }
        result+="]";
        return result;
    }

    @Override
    public String getChannelType(String comid) {
        String result = "[";
        logger.error("=========>>>>>comid="+comid);
        if(!comid.equals("-1")){
            String sql = "select id,passname from com_pass_tb where state = 0 and comid = "+"\'"+comid+"\'";
            List<Map<String,Object>>  pList = null;
            logger.error(sql);
            pList = commonDao.getObjectBySql(sql);
            if(pList!=null&&pList.size()>0){
                for(int i = 0;i<pList.size();i++){
                    Map map = pList.get(i);
                    if(i==0){
                        result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("passname")+"\"}";
                    }else{
                        result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("passname")+"\"}";
                    }
                }
            }
        }
        result+="]";
        return result;
    }

    @Override
    public String getWorkSiteId(String comid) {
        String result = "[";
        logger.error("=========>>>>>comid="+comid);
        if(!comid.equals("-1")){
            String sql = "select id,worksite_name from com_worksite_tb where state = 0 and comid = "+"\'"+comid+"\'";
            List<Map<String,Object>>  pList = null;
            logger.error(sql);
            pList = commonDao.getObjectBySql(sql);
            if(pList!=null&&pList.size()>0){
                for(int i = 0;i<pList.size();i++){
                    Map map = pList.get(i);
                    if(i==0){
                        result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("worksite_name")+"\"}";
                    }else{
                        result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("worksite_name")+"\"}";
                    }
                }
            }
        }
        result+="]";
        return result;
    }

    @Override
    public String getAllParks(String groupid, String cityid) {

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String sql = "select id,company_name from com_info_tb ";
        List<Object> parks = null;
        if(cityid != null&&!"".equals(cityid)){
            parks = commonMethods.getparks(Long.parseLong(cityid));
        }else if(groupid !=null&&!"".equals(groupid)){
            parks = commonMethods.getParks(Long.parseLong(groupid));
        }
        if(parks != null && !parks.isEmpty()){
            String preParams  ="";
            for(Object parkid : parks){
                if(preParams.equals(""))
                    preParams =parkid+"";
                else
                    preParams += ","+parkid;
            }
            sql += " where id in ("+preParams+") ";
            list = commonDao.getObjectBySql(sql);
        }
//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";
        if(list != null && !list.isEmpty()){
            int i = 1;
            for(Map map : list){
                if(i==1){
                    result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("company_name")+"\"}";
                }else{
                    result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("company_name")+"\"}";
                }
                i++;
            }
        }
        result += "]";
        return result;
    }

    @Override
    public String getAllCollectors(String groupid, String cityid) {
        List<Map<String, Object>> collList=null;
        if(cityid!=null&&!"".equals(cityid)){
            collList= getcollectors(Long.parseLong(cityid));
        }else if(groupid!=null&&!"".equals(groupid)) {
            collList= getgroupcollectors(Long.parseLong(groupid));
        }

//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";
        if(collList != null && !collList.isEmpty()){
            int i = 1;
            for(Map map : collList){
                if(i==1){
                    result += "{\"value_no\":\"" + map.get("id") + "\",\"value_name\":\"" + map.get("nickname") + "\"}";
                }else {
                    result += ",{\"value_no\":\"" + map.get("id") + "\",\"value_name\":\"" + map.get("nickname") + "\"}";
                }
                i++;
            }
        }
        result += "]";
        return result;
    }

    @Override
    public String getAllPackage(String groupid,String cityid) {
        List<Map<String,Object>>  pList = null;
//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";
        if(groupid!=null&&!"".equals(groupid)){
            String sql = "select id,p_name from product_package_tb where comid in(select id from com_info_tb " +
                    "where groupid =  "+groupid;
            pList = commonDao.getObjectBySql(sql +") and is_delete=0 ");

        }else if(cityid!=null&&!"".equals(cityid)){
            String sql = "select id,p_name from product_package_tb where comid in(select id from com_info_tb " ;
            List<Object> groupList = commonMethods.getGroups(Long.parseLong(cityid));
            if(groupList!=null&&groupList.size()>0){
                String params = "";
                for(Object id:groupList){
                    if(params.equals(""))
                        params =id+"";
                    else
                        params += ","+id;
                }
                sql += "where groupid in ("+params+")";
                pList = commonDao.getObjectBySql(sql);
            }
        }
        if(pList!=null&&pList.size()>0){
            int i = 1;
            for(Map map : pList){
                if(i==1){
                    result += "{\"value_no\":\"" + map.get("id") + "\",\"value_name\":\"" + map.get("p_name") + "\"}";
                }else {
                    result += ",{\"value_no\":\"" + map.get("id") + "\",\"value_name\":\"" + map.get("p_name") + "\"}";
                }
                i++;
            }
        }
        result+="]";
        return result;
    }

    @Override
    public String getAllUnion(Long cityid) {
        List<Map<String,Object>>  unionList = null;
//        String result = "[{\"value_no\":\"-1\",\"value_name\":\"请选择\"}";
        String result = "[";

        String sql = "select id,name from org_group_tb where state = 0 and cityid = "+ cityid;


        unionList = commonDao.getObjectBySql(sql);

        if(unionList!=null&&unionList.size()>0){
            int i = 0;
            for(Map map : unionList){
                if(i==0){
                    result += "{\"value_no\":\"" + map.get("id") + "\",\"value_name\":\"" + map.get("name") + "\"}";
                }else {
                    result += ",{\"value_no\":\"" + map.get("id") + "\",\"value_name\":\"" + map.get("name") + "\"}";
                }
                i++;
            }
        }
        result+="]";
        return result;
    }

    @Override
    public String getSuperimposed(String comid) {
        String str = "{\"superimposed\":\"不支持\"}";
        JSONObject result = JSONObject.parseObject(str);
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(Long.parseLong(comid));
        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null){
            if(comInfoTb.getSuperimposed()==0){
                result.put("superimposed","不支持");
            }else if(comInfoTb.getSuperimposed()==1){
                result.put("superimposed","支持");
            }
        }
        return result.toString();
    }

    @Override
    public String getGroupChannelTypes(Long groupid) {
        String result = "[";
        logger.error("获取集团下面所有通道=========>>>>>groupid="+groupid);
        if(groupid>-1){
            String sql = "select id,passname from com_pass_tb ";
            List parks = commonMethods.getParks(groupid);
            String params = "";
            if(parks!=null&&parks.size()>0){
                for(Object id:parks){
                    if(params.equals(""))
                        params =id+"";
                    else
                        params += ","+id;
                }
                sql += "where comid in ("+params+")  and state = 0";
            }else{
                sql+="where state =0 ";
            }

            List<Map<String,Object>>  pList = null;
            logger.error("获取集团下面所有通道========="+sql);
            pList = commonDao.getObjectBySql(sql);
            if(pList!=null&&pList.size()>0){
                for(int i = 0;i<pList.size();i++){
                    Map map = pList.get(i);
                    if(i==0){
                        result+="{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("passname")+"\"}";
                    }else{
                        result+=",{\"value_no\":\""+map.get("id")+"\",\"value_name\":\""+map.get("passname")+"\"}";
                    }
                }
            }
        }
        result+="]";
        return result;
    }

    private List<Map<String, Object>> getcollectors(Long cityid){
        try {
            if(cityid != null && cityid > 0){
                List<Object> idList = commonMethods.getcollctors(cityid);
                if(idList != null && !idList.isEmpty()){
                    return getAllCollectorsByIdlist(idList);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("获得收费员列表"+e.getMessage());
        }
        return null;
    }
    private List<Map<String, Object>> getgroupcollectors(Long groupid){
        try {
            if(groupid != null && groupid > 0){
                List<Object> idList = commonMethods.getCollctors(groupid);
                if(idList != null && !idList.isEmpty()){
                    return getAllCollectorsByIdlist(idList);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("获得收费员列表"+e.getMessage());
        }
        return null;
    }

    private List<Map<String,Object>> getAllCollectorsByIdlist(List<Object> idList){
        String preParams  ="";
        for(Object o : idList){
            if(preParams.equals(""))
                preParams =o+"";
            else
                preParams += ","+o;
        }

        List<Map<String, Object>> collList = commonDao.getObjectBySql("select id,nickname " +
                " from user_info_tb where id in ("+preParams+")");
        return collList;
    }
}
