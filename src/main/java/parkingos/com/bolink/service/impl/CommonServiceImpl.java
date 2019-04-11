package parkingos.com.bolink.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.models.OrgGroupTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.CustomDefind;

import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CommonDao commonDao;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderMapper orderMapper;


    @Override
    public Long getUnionIdByComid(Long parkId) {
        Long unionId =-1L;
        Object unionIdStr = redisService.get(CustomDefind.getValue("REDISPARKID4UNIONID")+parkId);
        logger.info("===>>>redis获取unionIdStr:"+unionIdStr);
        if(unionIdStr==null){
            Long cityId = getCityIdByComid(parkId);
            logger.info("===>>>redis获取cityId:"+cityId);
            if(cityId!=null&&cityId>0){
                OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
                orgCityMerchants.setId(cityId);
                orgCityMerchants = (OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
                unionIdStr = orgCityMerchants.getUnionId();
                if(!Check.isNumber(unionIdStr+"")){
                    return -1L;
                }
                redisService.set(CustomDefind.getValue("REDISPARKID4UNIONID")+parkId,unionIdStr+"",3600);
            }else {
                return -1L;
            }
        }
        unionId = Long.parseLong(unionIdStr+"");
        return unionId;
    }



    @Override
    public String getUkeyByUnionId(Long unionId) {
        Object ukey = redisService.get(CustomDefind.getValue("REDISUNION4UKEY")+unionId);
        logger.info("===>>>>redis 取ukey:"+ukey);
        if(ukey==null) {
            OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
            orgCityMerchants.setUnionId(unionId+"");
            orgCityMerchants.setState(0);
            orgCityMerchants = (OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
            if(orgCityMerchants!=null&&orgCityMerchants.getUkey()!=null){
                ukey = orgCityMerchants.getUkey();
                redisService.set(CustomDefind.getValue("REDISUNION4UKEY")+unionId,ukey+"",7200);
            }else{
                return null;
            }
        }
        return ukey+"";
    }

    @Override
    public String getBolinkIdByComid(Long parkId) {
        Object bolinkId = redisService.get(CustomDefind.getValue("REDISPARKID4BOLINKID")+parkId);
        logger.info("==>>>>根据comid从redis取bolinkId"+bolinkId);
        if(bolinkId==null){
            ComInfoTb comInfoTb = getComInfo(parkId);
            if(comInfoTb!=null){
                redisService.set(CustomDefind.getValue("REDISPARKID4BOLINKID")+parkId,comInfoTb.getBolinkId(),7200);
                return comInfoTb.getBolinkId();
            }
        }
        return bolinkId+"";
    }

    @Override
    public Long getCityIdByComid(Long comid) {

        try {
            Object cityStr = redisService.get(CustomDefind.getValue("REDISKEY4CITY") + comid);
            if (cityStr != null) {
                return Long.parseLong(cityStr+"");
            }
        }catch (Exception e){
            logger.error("redis get error ===>>>>");
        }
        //根据车场编号查询集团编号
        Long groupid = getGroupIdByComId(comid);
        Long cityid=-1L;
        if(groupid!=null&&groupid>0){
            cityid = getCityIdByGroupId(groupid);
        }else{
            cityid = getCityIdByComId(comid);
        }
        //缓存先放一小时  看效果
        try {
            redisService.set(CustomDefind.getValue("REDISKEY4CITY") + comid, cityid + "", 3600);
        }catch (Exception e){
            logger.error("redis set error===>>>");
        }
        return cityid;
    }

    @Override
    /*
    * type:1收入记录    2支出记录
    *
    *
    * */
    public String getTableNameByComid(Long comid,int type) {
        Long unionId = getUnionIdByComid(comid);
        String bolinkTable = "";
        if(type==1) {
            bolinkTable= "bolink_income_tb_";
        }else if(type==2){
            bolinkTable= "bolink_expense_tb_";
        }
        if (unionId != null && unionId > 0) {
            bolinkTable += unionId % 10;
        }
        return bolinkTable;
    }

    @Override
    public String getUserNameById(Long userId) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(userId);
        userInfoTb=(UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
        if(userInfoTb!=null){
            return userInfoTb.getNickname();
        }
        return "";
    }

    @Override
    public String getTableNameByGroupId(Long groupid, int type) {
        Long unionId = getUnionIdByGroupId(groupid);
        String bolinkTable = "";
        if(type==1) {
            bolinkTable= "bolink_income_tb_";
        }else if(type==2){
            bolinkTable= "bolink_expense_tb_";
        }
        if (unionId != null && unionId > 0) {
            bolinkTable += unionId % 10;
        }
        return bolinkTable;
    }

    @Override
    public String getParkNameById(Long comid) {
        Map<String, Object> map = orderMapper.getParkNameById(comid);
        if (map != null && map.get("name") != null) {
            return map.get("name") + "";
        }
        return comid+"";
    }

    @Override
    public Long getGroupIdByComid(Long parkId) {
        Object group = redisService.get(CustomDefind.getValue("REDISCOMID4GROUPID")+parkId);
        if(group==null) {
            Long groupid = getGroupIdByComId(parkId);
            redisService.set(CustomDefind.getValue("REDISCOMID4GROUPID")+parkId,groupid+"",7500);
            return groupid;
        }else{
            return Long.parseLong(group+"");
        }
//        return getGroupIdByComId(parkId);
    }

    private Long getUnionIdByGroupId(Long groupid) {
        Object union = redisService.get(CustomDefind.getValue("REDISGROUPID4UNIONID")+groupid);
        if(union==null){
            Long cityId = getCityIdByGroupId(groupid);
            OrgCityMerchants cityMerchants = new OrgCityMerchants();
            cityMerchants.setId(cityId);
            cityMerchants = (OrgCityMerchants)commonDao.selectObjectByConditions(cityMerchants);
            if(cityMerchants!=null){
                if(Check.isLong(cityMerchants.getUnionId())) {
                    redisService.set(CustomDefind.getValue("REDISGROUPID4UNIONID")+groupid,cityMerchants.getUnionId(),3600);
                    return Long.parseLong(cityMerchants.getUnionId());
                }else{
                    redisService.set(CustomDefind.getValue("REDISGROUPID4UNIONID")+groupid,"99",3600);
                    return 99L;
                }
            }
        }
        return Long.parseLong(union+"");
    }

    private Long getCityIdByGroupId(Long groupid) {
        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setId(groupid);
        orgGroupTb = (OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);
        if(orgGroupTb!=null){
            return orgGroupTb.getCityid();
        }
        return -1L;
    }

    private Long getCityIdByComId(Long comid) {
        ComInfoTb comInfoTb= getComInfo(comid);
        if(comInfoTb!=null){
            return comInfoTb.getCityid();
        }
        return -1L;
    }

    private Long getGroupIdByComId(Long comid) {
        ComInfoTb comInfoTb= getComInfo(comid);
        if(comInfoTb!=null){
            return comInfoTb.getGroupid();
        }
        return -1L;
    }

    private ComInfoTb getComInfo(Long parkId) {
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(parkId);
        return (ComInfoTb) commonDao.selectObjectByConditions(comInfoTb);
    }
}
