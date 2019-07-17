package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.mybatis.mapper.ParkInfoMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.*;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class CityParkServiceImpl implements CityParkService {

    Logger logger = LoggerFactory.getLogger(CityParkServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ComInfoTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;
    @Autowired
    private ParkInfoMapper parkInfoMapper;
    @Autowired
    private SaveLogService saveLogService;
    @Autowired
    @Resource(name = "orderSpring")
    private OrderService orderService;
    @Autowired
    RedisService redisService;
    @Autowired
    CommonService commonService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count = 0;
        List<ComInfoTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();

        ComInfoTb comInfoTb = new ComInfoTb();
//        comInfoTb.setState(0);

        String groupidStart = reqmap.get("groupid_start");
        if (!Check.isEmpty(groupidStart)) {
            comInfoTb.setGroupid(Long.parseLong(groupidStart));
        }
        String groupid = reqmap.get("groupid");
        String cityid = reqmap.get("cityid");

        Map searchMap = supperSearchService.getBaseSearch(comInfoTb, reqmap);
        if (searchMap != null && !searchMap.isEmpty()) {
            ComInfoTb baseQuery = (ComInfoTb) searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if (searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            PageOrderConfig config = null;
            if (searchMap.containsKey("config"))
                config = (PageOrderConfig) searchMap.get("config");

            List parks = new ArrayList();

            if (groupid != null && !"".equals(groupid)) {
                parks = commonMethods.getParks(Long.parseLong(groupid));
            } else if (cityid != null && !"".equals(cityid)) {
                parks = commonMethods.getparks(Long.parseLong(cityid));
            }

            if (parks == null || parks.size() < 1) {
                return result;
            }

            //封装searchbean  城市和集团下所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.CONTAINS);
            searchBean.setFieldName("id");
            searchBean.setBasicValue(parks);

            if (supperQuery == null) {
                supperQuery = new ArrayList<SearchBean>();
            }
            supperQuery.add(searchBean);

            count = commonDao.selectCountByConditions(baseQuery, supperQuery);
            if (count > 0) {
                list = commonDao.selectListByConditions(baseQuery, supperQuery, config);
                if (list != null && !list.isEmpty()) {
                    for (ComInfoTb comInfoTb1 : list) {
                        OrmUtil<ComInfoTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(comInfoTb1);

                        Long parkid = comInfoTb1.getId();

                        int empty = commonService.getParkEmpty(parkid.intValue());
                        map.put("empty", empty);

                        List<HashMap<String, Object>> tokenList = getParkStatusbc(parkid);
                        if (tokenList != null && tokenList.size() > 0 && tokenList.get(0).get("beat_time") != null) {
                            map.put("beat_time", tokenList.get(0).get("beat_time"));
                        }

                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total", count);
        result.put("page", Integer.parseInt(reqmap.get("page")));
//        logger.error("============>>>>>返回数据" + result);
        return result;
    }

    @Override
    public JSONObject createPark(HttpServletRequest request) {

        JSONObject result = new JSONObject();
        result.put("state", 0);
        result.put("msg", "创建车场失败");

        Long id = RequestUtil.getLong(request, "id", -1L);
        String bolinkid = RequestUtil.getString(request, "bolink_id");
        Long cityid = RequestUtil.getLong(request, "cityid", -1L);

        Long groupId = RequestUtil.getLong(request, "groupid", -1L);


        String union_id = RequestUtil.getString(request,"unionid");

        //服务商登录 注册车场，参数为serverid
        Long server_id = RequestUtil.getLong(request, "serverid", -1L);
        Long bolinkServerId = -1L;

        String union_key ="";

        int fromServerCreatePark = 0;
        int fromGroupCreatePark = 0;
        int fromUnionCreatePark = 0;
        if(!Check.isEmpty(union_id)){
            fromUnionCreatePark=1;
            OrgCityMerchants orgCityMerchants = commonService.getCityByUnionId(union_id);
            union_key = orgCityMerchants.getUkey();
            cityid = orgCityMerchants.getId();
        }else if(server_id>0){
            //服务商注册   这时候的serverId  是云平台的主键
            fromServerCreatePark = 1;
            UnionServerTb unionServerTb = new UnionServerTb();
            unionServerTb.setId(server_id);
            unionServerTb=(UnionServerTb)commonDao.selectObjectByConditions(unionServerTb);
//            bolinkServerId = unionServerTb.getBolinkServerId();

            //选择的服务商   这的列表就是泊链的列表
            bolinkServerId = RequestUtil.getLong(request, "server_id", -1L);;

            Long unionId = unionServerTb.getUnionId();
            OrgCityMerchants orgCityMerchants = commonService.getCityByUnionId(unionId+"");
            union_id = unionId+"";
            union_key = orgCityMerchants.getUkey();
            cityid = orgCityMerchants.getId();
        }else if(groupId>0){
            //集团注册
            fromGroupCreatePark=1;
            OrgGroupTb orgGroupTb = new OrgGroupTb();
            orgGroupTb.setId(groupId);
            orgGroupTb=(OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);
            cityid = orgGroupTb.getCityid();
            OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
            orgCityMerchants.setId(cityid);
            orgCityMerchants =(OrgCityMerchants)commonDao.selectObjectByConditions(orgCityMerchants);
            union_key = orgCityMerchants.getUkey();
            cityid = orgCityMerchants.getId();
            union_id = orgCityMerchants.getUnionId();
        }


        //上面是获取ukey  ，如果groupId小于0  证明不是集团注册，有可能是厂商或者服务商注册，获取一下参数值
        if (groupId == -1) {
            groupId = RequestUtil.getLong(request, "group_id", -1L);
        }

        String operator_id = "";
        //查询operator_id   以及如果只选择了运营商
        if(groupId>0){
            //根据groupId查询 serverId
            OrgGroupTb orgGroupTb = new OrgGroupTb();
            orgGroupTb.setId(groupId);
            orgGroupTb =(OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);
            if(orgGroupTb!=null){
                operator_id = orgGroupTb.getOperatorid();
            }
        }

        //如果server_id小于0  证明不是服务商注册，有可能是厂商注册，获取一下参数值

        if(server_id==-1){
            server_id= RequestUtil.getLong(request, "server_id", -1L);
            //如果取到server_id值  那证明是从厂商注册，厂商的服务商列表是泊链的。
            if(server_id>0){
                fromUnionCreatePark = 1;
            }
        }

        //去查询bolinkServerId
        if(bolinkServerId<0&&fromUnionCreatePark>0){
            bolinkServerId = server_id;
        }

        logger.info("注册车场cityId+groupid+serverId" + cityid + "~~" + groupId+"~~"+server_id);

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String company = RequestUtil.processParams(request, "company_name");
        company = company.replace("\r", "").replace("\n", "");
        String mobile = RequestUtil.processParams(request, "mobile");
        if (mobile.length() > 15) {
            result.put("msg", "手机号输入有误");
            return result;
        }
        Integer parking_total = RequestUtil.getInteger(request, "parking_total", 0);
        Integer state = RequestUtil.getInteger(request, "state", 0);
        Double longitude = RequestUtil.getDouble(request, "longitude", 0d);
        Double latitude = RequestUtil.getDouble(request, "latitude", 0d);


        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setState(state);
        comInfoTb.setCompanyName(company);
        comInfoTb.setMobile(mobile);
        comInfoTb.setParkingTotal(parking_total);

        //新加可以修改集团
        comInfoTb.setGroupid(groupId);

        //可以修改服务商
        UnionServerTb unionServerTb = new UnionServerTb();
        unionServerTb.setBolinkServerId(bolinkServerId);
        unionServerTb=(UnionServerTb)commonDao.selectObjectByConditions(unionServerTb);
        if(unionServerTb!=null){
            comInfoTb.setServerId(bolinkServerId);
            comInfoTb.setCloudServerId(unionServerTb.getId());
        }


        logger.info("===>>>>>unionId:" + union_id + "~~~~cityid:" + cityid);
        if (id == -1) {

            if (!Check.isEmpty(bolinkid)) {
                ComInfoTb infoTb = new ComInfoTb();
                infoTb.setBolinkId(bolinkid);
                infoTb.setUnionId(union_id);
                int infoCount = commonDao.selectCountByConditions(infoTb);
                if (infoCount > 0) {
                    result.put("msg", "创建失败,互联车场编号重复");
                    return result;
                }
            }

            comInfoTb.setLatitude(new BigDecimal(latitude));
            comInfoTb.setLongitude(new BigDecimal(longitude));
            comInfoTb.setBolinkId(bolinkid);

            //获取id
            Long comid = commonDao.selectSequence(ComInfoTb.class);
            comInfoTb.setId(comid);
            comInfoTb.setCityid(cityid);
            //添加自动生成车场16位秘钥的逻辑
            String ukey = StringUtils.createRandomCharData(16);
            comInfoTb.setUkey(ukey);
            comInfoTb.setCreateTime(System.currentTimeMillis() / 1000);
            comInfoTb.setUnionId(union_id);

            //判断车场是否要上传到泊链,如果没有写bolinkid,那么上传
            if (bolinkid == null || "".equals(bolinkid)) {

                comInfoTb.setBolinkId(comid + "");
                //查询他的厂商编号以及服务商编号 (查询有集团编号的)
                String url = CustomDefind.UNIONIP + "newpark/addpark";
                JSONObject paramMap = new JSONObject();
                paramMap.put("park_id", comid);
                paramMap.put("name", company);
                paramMap.put("address", "");
                paramMap.put("phone", mobile);
                paramMap.put("lng", longitude);
                paramMap.put("lat", latitude);
                paramMap.put("total_plot", parking_total);
                paramMap.put("empty_plot", parking_total);
                paramMap.put("price_desc", "0元/次");
                paramMap.put("remark", "");
                paramMap.put("union_id", union_id);
                paramMap.put("server_id", bolinkServerId);
                paramMap.put("operator_id", operator_id);
                paramMap.put("rand", Math.random());
                paramMap.put("is_cloud_park", 1);
                String ret = "";
                try {
                    logger.info(url + paramMap);


                    String _signStr = paramMap.toJSONString() + "key=" + union_key;
                    logger.info(_signStr);
                    String _sign = StringUtils.MD5(_signStr).toUpperCase();
                    logger.info(_sign);
                    JSONObject json = new JSONObject();
                    json.put("data", paramMap.toJSONString());
                    json.put("sign", _sign);

                    HttpProxy httpProxy = new HttpProxy();
                    ret = httpProxy.doHeadPost(url, json.toJSONString());

                    logger.info(ret);
                    JSONObject object = JSONObject.parseObject(ret);
                    if (object != null) {
                        Integer uploadState = Integer.parseInt(object.get("state") + "");
                        logger.info("上传车场：" + uploadState + "");
                        if (uploadState == 1) {
                            logger.info("insert cominfo:"+comInfoTb);
                            int insert = commonDao.insert(comInfoTb);
                            logger.error("上传车场个数:1,云平台新建车场:" + insert);
                            result.put("state", 1);
                            result.put("msg", "新建车场成功!");


                            //新建车场的时候  生成车场管理员  因为厂商可以自定义权限，所以需要修改

                            OrgCityMerchants city = commonService.getOrgCityById(cityid);
                            int parkAuth = 0;
                            if(city!=null){
                                parkAuth = city.getSelfParkAuth();
                            }

                            Long roleId = 30L;
                            if(parkAuth==1){
                                //有自己的设置，查找自己管理员id
                                UserRoleTb userRoleTb = new UserRoleTb();
                                userRoleTb.setAdminid(0L);
                                userRoleTb.setCityid(city.getId());
                                userRoleTb.setOid(8L);

                                userRoleTb = (UserRoleTb)commonDao.selectObjectByConditions(userRoleTb);
                                if(userRoleTb!=null){
                                    roleId = userRoleTb.getId();
                                }
                            }

                            UserInfoTb user= new UserInfoTb();
                            Long userId = commonDao.selectSequence(UserInfoTb.class);
                            user.setId(userId);
                            user.setNickname(company+"管理员");
                            user.setPassword(userId+"");
                            user.setStrid(userId+"");
                            user.setRegTime(System.currentTimeMillis()/1000);
                            user.setRoleId(roleId);
                            user.setAuthFlag(1L);
                            user.setUserId(userId+"");
                            user.setComid(comid);
                            user.setMobile(mobile);
                            user.setPhone(mobile);
                            commonDao.insert(user);
                            logger.info("==>>>>新建员工success:");

                            if (fromGroupCreatePark > 0) {
                                ParkLogTb parkLogTb = new ParkLogTb();
                                parkLogTb.setOperateUser(nickname);
                                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                                parkLogTb.setOperateType(1);
                                parkLogTb.setContent(uin + "(" + nickname + ")" + "新建了车场" + comid + company);
                                parkLogTb.setType("parkinfo");
                                parkLogTb.setGroupId(groupId);
                                saveLogService.saveLog(parkLogTb);
                            }

                            if (fromUnionCreatePark > 0) {
                                ParkLogTb parkLogTb = new ParkLogTb();
                                parkLogTb.setOperateUser(nickname);
                                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                                parkLogTb.setOperateType(1);
                                parkLogTb.setContent(uin + "(" + nickname + ")" + "新建了车场" + comid + company);
                                parkLogTb.setType("parkinfo");
                                parkLogTb.setCityId(cityid);
                                saveLogService.saveLog(parkLogTb);
                            }




                            return result;
                        } else {
                            result.put("state", 0);
                            logger.error(object.get("errmsg") + "");
                            String errmsg = object.get("errmsg") + "";
                            if (errmsg.contains("运营商编号")) {
                                result.put("msg", "新建车场失败,互联运营集团编号错误");
                                return result;
                            }
                            result.put("msg", "新建车场失败,上传到互联失败");
                        }
                    }
                } catch (Exception e) {
                    logger.error("新建车场失败===", e);
                }
            } else {//如果填写了泊链车场编号,证明泊链那边有车场,去验证所填泊链编号是否正确
                String url = CustomDefind.UNIONIP + "park/checkparkid";
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("park_id", bolinkid);
                paramMap.put("union_id", union_id);
                String param = StringUtils.createJson(paramMap);
                try {
//                    String ret = HttpsProxy.doPost(url, param, "utf-8", 20000, 20000);
                    HttpProxy httpProxy = new HttpProxy();
                    String ret = httpProxy.doHeadPost(url, param);
                    JSONObject object = JSONObject.parseObject(ret);
                    Integer checkstate = Integer.parseInt(object.get("state") + "");
                    if (checkstate == 1) {//泊链车场编号正确
                        int insert = commonDao.insert(comInfoTb);
                        logger.error("填写了泊链编号进行校验新建车场:" + insert);
                        if (insert == 1) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("park_id", bolinkid);
                            jsonObject.put("union_id", union_id);
                            url = CustomDefind.UNIONIP + "newpark/updatepark";
                            try {
                                jsonObject.put("is_cloud_park", 1);
                                jsonObject.put("type", 3);
                                jsonObject.put("rand", Math.random());
                                jsonObject.put("server_id", bolinkServerId);


                                String _signStr = jsonObject.toJSONString() + "key=" + union_key;
                                System.out.println(_signStr);
                                String _sign = StringUtils.MD5(_signStr).toUpperCase();
                                System.out.println(_sign);
                                JSONObject json = new JSONObject();
                                json.put("data", jsonObject.toJSONString());
                                json.put("sign", _sign);

                                httpProxy = new HttpProxy();
                                ret = httpProxy.doHeadPost(url, json.toJSONString());
                                logger.info("=======>>>>" + ret);
                            } catch (Exception e) {
                                logger.error("update bolink park state error", e);
                            }
                            result.put("state", insert);
                            result.put("msg", "新建车场成功");
                            return result;
                        }
                    } else {
                        result.put("state", 0);
                        result.put("msg", "新建车场失败,互联车场编号错误");
                        return result;
                    }
                } catch (Exception e) {
                    logger.error("去泊链查询operatorid是否存在出现异常");
                }

            }
//            }
        } else {

            //有id只是证明是在更新 但是id  不是云平台的主键id

            //如果现在修改了车场的所属集团
            ComInfoTb con = commonService.getComInfoByUnionIdAndParkId(union_id,bolinkid);
            Long groupIdBefore = con.getGroupid();
            if(!groupIdBefore.equals(groupId)&&groupId>0){
                //如果以前的车场有集团，那么需要更新之前的统计报表
                StaticAnalysisTb staticCon = new StaticAnalysisTb();
                staticCon.setParkId(con.getId());
                StaticAnalysisTb updateSta = new StaticAnalysisTb();
                updateSta.setGroupId(groupId);
                int updateStatic = commonDao.updateByConditions(updateSta, staticCon);
                logger.info("更新车场所属集团:" + updateStatic);
            }

            comInfoTb.setUpdateTime(System.currentTimeMillis() / 1000);
            comInfoTb.setId(con.getId());
            int update = commonDao.updateByPrimaryKey(comInfoTb);

            if (update == 1) {
                //清除缓存
                commonService.deleteCachPark(union_id, bolinkid);

                result.put("state", 1);
                result.put("msg", "修改车场成功");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("park_id", bolinkid);
                jsonObject.put("union_id", union_id);
                String url = CustomDefind.UNIONIP + "newpark/updatepark";
                try {
                    jsonObject.put("is_cloud_park", 1);
                    jsonObject.put("type", 3);
                    jsonObject.put("name", company);
                    jsonObject.put("phone",mobile );
                    jsonObject.put("total_plot", parking_total);
                    jsonObject.put("rand", Math.random());
                    jsonObject.put("server_id", bolinkServerId);


                    String _signStr = jsonObject.toJSONString() + "key=" + union_key;
                    logger.info(_signStr);
                    String _sign = StringUtils.MD5(_signStr).toUpperCase();
                    logger.info(_sign);
                    JSONObject json = new JSONObject();
                    json.put("data", jsonObject.toJSONString());
                    json.put("sign", _sign);

                    HttpProxy httpProxy = new HttpProxy();
                    String ret = httpProxy.doHeadPost(url, json.toJSONString());
                    logger.info("=======>>>>" + ret);
                } catch (Exception e) {
                    logger.error("update bolink park state error", e);
                }

                if (fromGroupCreatePark > 0) {
                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                    parkLogTb.setOperateType(2);
                    parkLogTb.setContent(uin + "(" + nickname + ")" + "修改了车场" + id);
                    parkLogTb.setType("parkinfo");
                    parkLogTb.setGroupId(groupId);
                    saveLogService.saveLog(parkLogTb);
                }

                if (fromUnionCreatePark > 0) {
                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                    parkLogTb.setOperateType(2);
                    parkLogTb.setContent(uin + "(" + nickname + ")" + "修改了车场" + id);
                    parkLogTb.setType("parkinfo");
                    parkLogTb.setCityId(cityid);
                    saveLogService.saveLog(parkLogTb);
                }

            }
        }
        return result;
    }

    @Override
    public JSONObject editpark(ComInfoTb comInfoTb) {
        JSONObject result = new JSONObject();
        result.put("state", 0);
        result.put("msg", "修改车场失败");

        Double longitude = comInfoTb.getLongitude().doubleValue();
        Double latitude = comInfoTb.getLatitude().doubleValue();
        if (longitude == 0 || latitude == 0) {
            result.put("msg", "请标注地理位置");
            return result;
        }

        //判断地图位置是否冲突
        ComInfoTb newCominfoTb = new ComInfoTb();
        newCominfoTb.setLongitude(new BigDecimal(longitude).setScale(6, BigDecimal.ROUND_HALF_UP));
        newCominfoTb.setLatitude(new BigDecimal(latitude).setScale(6, BigDecimal.ROUND_HALF_UP));
        int count = commonDao.selectCountByConditions(newCominfoTb);
        if (count > 0) {
            result.put("msg", "地理位置冲突，请重新标注!");
            return result;
        }

        //重新处理参数 封装
        String company = comInfoTb.getCompanyName();
        company = company.replace("\r", "").replace("\n", "");
        comInfoTb.setCompanyName(company);
        String address = comInfoTb.getAddress();
        address = address.replace("\r", "").replace("\n", "");
        comInfoTb.setAddress(address);

        if (comInfoTb.getId() != null && comInfoTb.getId() != -1) {
            comInfoTb.setUpdateTime(System.currentTimeMillis() / 1000);
            int update = commonDao.updateByPrimaryKey(comInfoTb);
            if (update == 1) {
                result.put("state", 1);
                result.put("msg", "修改车场成功");
            }
        }

        return result;
    }

    @Override
    public JSONObject deletepark(ComInfoTb comInfoTb) {
        JSONObject result = new JSONObject();
        result.put("state", 0);
        result.put("msg", "禁用失败");

        String unionId = comInfoTb.getUnionId();
        String parkId = comInfoTb.getBolinkId();

        ComInfoTb comInfoTb1 = commonService.getComInfoByUnionIdAndParkId(unionId, parkId);
        Long cityId = comInfoTb1.getCityid();

        OrgCityMerchants city = new OrgCityMerchants();
        city.setId(cityId);
        city.setState(0);
        city = (OrgCityMerchants) commonDao.selectObjectByConditions(city);
        if (city != null && city.getUnionId() != null) {

            JSONObject paramMap = new JSONObject();
            paramMap.put("park_id", comInfoTb.getBolinkId());
            paramMap.put("union_id", city.getUnionId());
//                    paramMap.put("is_cloud_park", 0);
            //type 2 禁用   type 3  更改is_cloud_park 参数
            paramMap.put("type", 2);
            paramMap.put("rand", Math.random());
            String url = CustomDefind.UNIONIP + "newpark/updatepark";
            try {
                String ukey = city.getUkey();
                String _signStr = paramMap.toJSONString() + "key=" + ukey;
                System.out.println(_signStr);
                String _sign = StringUtils.MD5(_signStr).toUpperCase();
                System.out.println(_sign);
                JSONObject json = new JSONObject();
                json.put("data", paramMap.toJSONString());
                json.put("sign", _sign);

                HttpProxy httpProxy = new HttpProxy();

                String ret = httpProxy.doHeadPost(url, json.toJSONString());
                logger.info("====>>>>>>>>>>>>>>>" + ret);
                if(!Check.isEmpty(ret)){
                     JSONObject jsonRes = JSON.parseObject(ret);
                     if(jsonRes.containsKey("state")&&jsonRes.getInteger("state")==1){
                         ComInfoTb update = new ComInfoTb();
                         update.setState(2);
                         int upCount = commonDao.updateByConditions(update,comInfoTb);
                         if(upCount>0){
                             result.put("state", 1);
                             result.put("msg", "禁用成功");
                         }
                     }
                }
            } catch (Exception e) {
                logger.error("update bolink park state error", e);
            }
        }

        return result;
    }

    @Override
    public JSONObject setpark(Long comid) {

        JSONObject result = new JSONObject();

        List<Map<String, Object>> parkList = commonDao.getObjectBySql("select * from com_info_tb where id=" + comid);
//        Integer parking_type = 0;
        String info = "";
        if (parkList != null) {
            info = "名称：" + parkList.get(0).get("company_name") + "，地址：" + parkList.get(0).get("address") + "<br/>创建时间："
                    + TimeTools.getTime_yyyyMMdd_HHmm((Long) parkList.get(0).get("create_time") * 1000) + "，车位总数：" + parkList.get(0).get("parking_total")
                    + "，分享车位：" + parkList.get(0).get("share_number") + "，经纬度：(" + parkList.get(0).get("longitude") + "," + parkList.get(0).get("latitude") + ")";
//            parking_type = (Integer)parkList.get(0).get("parking_type");
            result.put("info", info);
        }
        return result;
    }

    @Override
    public JSONObject resetParkData(Long comid, Long loginuin, String password) {
        JSONObject result = new JSONObject();
        result.put("state", 0);
        result.put("msg", "重置失败");

        //根据登录厂商的账号查询密码 进行匹配
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(loginuin);
        userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
        if (userInfoTb != null && userInfoTb.getPassword() != null) {
            if (!password.equals(userInfoTb.getPassword())) {
                result.put("msg", "密码错误");
                return result;
            }
        } else {
            result.put("msg", "用户不存在");
            return result;
        }

        //密码正确，根据车场编号搜索需要重置的数据
        try {
//            OrderTb orderTb = new OrderTb();
//            orderTb.setComid(comid);
//            orderTb.setIshd(0);
            PageOrderConfig pageOrderConfig = new PageOrderConfig();
            pageOrderConfig.setPageInfo(null, null);
//            List<OrderTb> orderTbList = commonDao.selectListByConditions(orderTb,pageOrderConfig);
//            if(orderTbList!=null&&orderTbList.size()>0){
//                for(OrderTb order:orderTbList){
//                    order.setIshd(1);
//                    commonDao.updateByPrimaryKey(order);
//                }
//            }

            orderService.resetDataByComid(comid);

            logger.error(comid + "重置订单完成,开始重置抬杆数据");
            LiftRodTb liftRodTb = new LiftRodTb();
            liftRodTb.setComid(comid);
            liftRodTb.setIsDelete(0);
            List<LiftRodTb> liftRodTbList = commonDao.selectListByConditions(liftRodTb, pageOrderConfig);
            if (liftRodTbList != null && liftRodTbList.size() > 0) {
                for (LiftRodTb liftRod : liftRodTbList) {
                    liftRod.setIsDelete(1);
                    commonDao.updateByPrimaryKey(liftRod);
                }
            }
            logger.error(comid + "重置抬杆数据完成");
        } catch (Exception e) {
            result.put("msg", "重置失败，重置过程出现异常");
            return result;
        }
        result.put("state", 1);
        result.put("msg", "重置数据成功！");
        return result;
    }

    private String getPrice(Long parkId) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        //开始小时
        int bhour = calendar.get(Calendar.HOUR_OF_DAY);
        List<Map<String, Object>> priceList = commonDao.getObjectBySql("select * from price_tb where comid=" + parkId + " and state=0 and pay_type=0 order by id desc");
        if (priceList == null || priceList.size() == 0) {//没有按时段策略
            priceList = commonDao.getObjectBySql("select * from price_tb where comid=" + parkId + " and state=0 and pay_type=1 order by id desc");
            ;
            if (priceList == null || priceList.size() == 0) {//没有按次策略，返回提示
                return "0元/次";
            } else {//有按次策略，直接返回一次的收费
                Map timeMap = priceList.get(0);
                Integer unit = (Integer) timeMap.get("unit");
                if (unit != null && unit > 0) {
                    if (unit > 60) {
                        String t = "";
                        if (unit % 60 == 0)
                            t = unit / 60 + "小时";
                        else
                            t = unit / 60 + "小时 " + unit % 60 + "分钟";
                        return timeMap.get("price") + "元/" + t;
                    } else {
                        return timeMap.get("price") + "元/" + unit + "分钟";
                    }
                } else {
                    return timeMap.get("price") + "元/次";
                }
            }
            //发短信给管理员，通过设置好价格
        } else {//从按时段价格策略中分拣出日间和夜间收费策略
            if (priceList.size() > 0) {
                //logger.error(priceList);
                for (Map map : priceList) {
                    Integer btime = (Integer) map.get("b_time");
                    Integer etime = (Integer) map.get("e_time");
                    Double price = Double.valueOf(map.get("price") + "");
                    Double fprice = Double.valueOf(map.get("fprice") + "");
                    Integer ftime = (Integer) map.get("first_times");
                    if (ftime != null && ftime > 0) {
                        if (fprice > 0)
                            price = fprice;
                    }
                    if (btime < etime) {//日间
                        if (bhour >= btime && bhour < etime) {
                            return price + "元/" + map.get("unit") + "分钟";
                        }
                    } else {
                        if (bhour >= btime || bhour < etime) {
                            return price + "元/" + map.get("unit") + "分钟";
                        }
                    }
                }
            }
        }
        return "0.0元/小时";
    }


    private List<HashMap<String, Object>> getParkStatusbc(Long parkid) {
        List<HashMap<String, Object>> parkState = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> parkLoginList = parkInfoMapper.getParkLogin(parkid + "");
        if (parkLoginList != null && parkLoginList.size() > 0) {
            for (HashMap<String, Object> loginmap : parkLoginList) {
                HashMap<String, Object> parkstatusmap = new HashMap<String, Object>();
                Long logintime = (Long) loginmap.get("logintime");
                String localid = (String) loginmap.get("localid");
                String sourceIp = (String) loginmap.get("sourceIp");
                logger.info("==>>>>localId:" + localid + "~~" + sourceIp + "~~" + parkid);
                String cacheKey = "parkingos_dobeat_" + parkid + "_" + localid + sourceIp;
                if (cacheKey.length() > 200) {
                    cacheKey = "parkingos_dobeat_" + StringUtils.MD5(parkid + "_" + localid + sourceIp);
                }
                logger.info("===>>>>cacheKey:" + cacheKey);
                Long beattime = null;
                if (redisService.get(cacheKey) != null) {
                    beattime = Long.parseLong(redisService.get(cacheKey) + "");
                }
                logger.info("===>>>>>beatTime:" + beattime);
                if (localid == null) localid = "";

                if (beattime != null) {
                    parkstatusmap.put("beat_time", beattime);
                } else {
                    parkstatusmap.put("beat_time", logintime);
                }
                parkState.add(parkstatusmap);
            }
        }

        return parkState;
    }

    /**
     * 判断车场是否在线
     *
     * @param time
     * @param delayTime
     * @return
     * @time 2017年 下午12:03:41
     * @author QuanHao
     */
    private boolean isParkOnline(long time, int delayTime) {
        long curTime = System.currentTimeMillis() / 1000;
        long margin = curTime - time;
        if (margin - delayTime <= 0) {
            return true;
        }
        return false;
    }

}
