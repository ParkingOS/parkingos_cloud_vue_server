package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.ParkInfoService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class ParkInfoServiceImpl implements ParkInfoService {

    Logger logger = Logger.getLogger(ParkInfoServiceImpl.class);

    @Autowired
    private CommonDao commonDao;


    @Override
    public String getResultByComid(Long comid) {
        logger.error("======>>>车场comid"+comid);
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb = (ComInfoTb) commonDao.selectObjectByConditions(comInfoTb);
        logger.error("======>>comInfoTb"+comInfoTb);
        Map<String, Object> comMap = null;
        if (comInfoTb != null) {
            OrmUtil<ComInfoTb> om = new OrmUtil<ComInfoTb>();
            comMap = om.pojoToMap(comInfoTb);
        }
        if (comMap != null) {
            StringBuffer comBuffer = new StringBuffer("[");
            for (String key : comMap.keySet()) {
                comBuffer.append("{\"name\":\"" + key + "\",\"value\":\"" + comMap.get(key) + "\"},");
            }
            String result = comBuffer.toString();
            result = result.substring(0, result.length() - 1) + "]";
            result = result.replace("null", "");
            return result;
        }
        return "[]";
    }

    @Override
    public JSONObject updateComInfo(HttpServletRequest request) {

        String str = "{\"state\":0,\"msg\":\"更新失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String company = StringUtils.decodeUTF8(RequestUtil.processParams(request, "company_name"));
        String address =StringUtils.decodeUTF8(RequestUtil.processParams(request, "address"));
        String phone =RequestUtil.processParams(request, "phone");
        String mobile =RequestUtil.processParams(request, "mobile");
        String property =StringUtils.decodeUTF8(RequestUtil.processParams(request, "property"));
        String id =RequestUtil.processParams(request, "id");
        Integer type = RequestUtil.getInteger(request, "type", 0);
        Integer parking_type = RequestUtil.getInteger(request, "parking_type", 0);
        Integer parking_total = RequestUtil.getInteger(request, "parking_total", 0);
        Integer share_number = RequestUtil.getInteger(request, "share_number", 0);
        Integer nfc = RequestUtil.getInteger(request, "nfc", 0);
        Integer etc = RequestUtil.getInteger(request, "etc", 0);
        Integer book = RequestUtil.getInteger(request, "book", 0);
        Integer navi = RequestUtil.getInteger(request, "navi", 0);
        Integer monthlypay = RequestUtil.getInteger(request, "monthlypay", 0);
        Integer epay = RequestUtil.getInteger(request, "epay", 0);
        Integer isnight = RequestUtil.getInteger(request, "isnight", 0);//夜晚停车，0:支持，1不支持
        Long invalid_order = RequestUtil.getLong(request, "invalid_order", 0L);

//        String sql = "update com_info_tb set company_name=?,address=?,phone=?,mobile=?,property=?," +
//                "parking_total=?,share_number=?,parking_type=?,type=?,update_time=?,nfc=?,etc=?,book=?,navi=?,monthlypay=?" +
//                ",isnight=?,epay=?,invalid_order=? where id=?";
//        Object [] values = new Object[]{company,address,phone,mobile,property,parking_total,share_number,parking_type,type,
//                System.currentTimeMillis()/1000,nfc,etc,book,navi,monthlypay,isnight,epay,invalid_order,Long.valueOf(id)};
//        int update = daService.update(sql, values);

        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(Long.parseLong(id));
        comInfoTb.setCompanyName(company);
        comInfoTb.setAddress(address);
        comInfoTb.setPhone(phone);
        comInfoTb.setMobile(mobile);
        comInfoTb.setProperty(property);
        comInfoTb.setParkingTotal(parking_total);
        comInfoTb.setShareNumber(share_number);
        comInfoTb.setParkingType(parking_type);
        comInfoTb.setType(type);
        comInfoTb.setUpdateTime(System.currentTimeMillis()/1000);
        comInfoTb.setNfc(nfc);
        comInfoTb.setEtc(etc);
        comInfoTb.setBook(book);
        comInfoTb.setNavi(navi);
        comInfoTb.setMonthlypay(monthlypay);
        comInfoTb.setIsnight(isnight);
        comInfoTb.setEpay(epay);
        comInfoTb.setInvalidOrder(invalid_order);


        int update = commonDao.updateByPrimaryKey(comInfoTb);
        logger.error("=====>>>车场更新"+update);
        if(update==1){
            result.put("state",1);
            result.put("msg","更新成功");
            SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
            syncInfoPoolTb.setComid(comid);
            syncInfoPoolTb.setTableName("com_info_tb");
            syncInfoPoolTb.setTableId(Long.parseLong(id));
            syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
            syncInfoPoolTb.setOperate(1);
            int ins =commonDao.insert(syncInfoPoolTb);
            if(ins!=1){
                logger.error("======>>>车场插入同步表失败");
            }
//				Map comMap = daService.getMap("select * from com_info_tb where id=?", new Object[]{Long.valueOf(id)});
//				ParkingMap.updateParkingMap(comMap);
//            if(publicMethods.isEtcPark(comid)){
//                int re = daService.update("insert into sync_info_pool_tb(comid,table_name,table_id,create_time,operate) values(?,?,?,?,?)", new Object[]{comid,"com_info_tb",Long.valueOf(id),System.currentTimeMillis()/1000,1});
//                logger.error("parkadmin or admin:"+operater+" edit comid:"+comid+" com_info_tb ,add sync ret:"+re);
//            }else{
//                logger.error("parkadmin or admin:"+operater+" edit comid:"+comid+" com_info_tb");
//            }
//            mongoDbUtils.saveLogs(request, 0, 3, "修改了车场（编号："+id+"）");
        }
        return result;
    }


}
