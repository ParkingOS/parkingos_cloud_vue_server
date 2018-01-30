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

import java.util.List;
import java.util.Map;

@Service
public class GetDataServiceImpl implements GetDataService {

    Logger logger = Logger.getLogger(GetDataServiceImpl.class);

    @Autowired
    private CommonDao commonDao;


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
}
