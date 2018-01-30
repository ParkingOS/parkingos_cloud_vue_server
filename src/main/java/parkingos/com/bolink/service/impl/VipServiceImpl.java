package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.service.GetDataService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.VipService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Service
public class VipServiceImpl implements VipService {

    Logger logger = Logger.getLogger(VipServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private GetDataService getDataService;

    @Autowired
    private SupperSearchService<CarowerProduct> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        Long comid = Long.parseLong(reqmap.get("comid"));
        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setIsDelete(0L);
        carowerProduct.setComId(comid);
        JSONObject result = supperSearchService.supperSearch(carowerProduct, reqmap);

        return result;
    }

    @Override
    public Long getkey() {
        return commonDao.selectSequence(CarowerProduct.class);
    }

    @Override
    public Integer selectCountByConditions(CarowerProduct carowerProduct) {
        return commonDao.selectCountByConditions(carowerProduct);
    }

    @Override
    public JSONObject deleteCarowerProById(Long id, Long comid) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setId(id);
        carowerProduct.setIsDelete(1L);
        int ret = commonDao.updateByPrimaryKey(carowerProduct);
        if (ret == 1) {
            result.put("state", 1);
            result.put("msg", "删除成功");
            int ins = insertSysn(carowerProduct, 0, comid);
            if (ins != 1) {
                logger.error("====>>>>>插入同步表失败");
            }
        }
        return result;
    }

    @Override
    public JSONObject createVip(HttpServletRequest req) {

        String str = "{\"state\":0,\"msg\":\"添加失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = RequestUtil.getLong(req,"comid",-1L);
        Long pid = RequestUtil.getLong(req, "p_name", -1L);
        //车主手机
        String mobile = StringUtils.decodeUTF8(RequestUtil.processParams(req, "mobile").trim());
        String name = StringUtils.decodeUTF8(RequestUtil.processParams(req, "name").trim());
        String address = StringUtils.decodeUTF8(RequestUtil.processParams(req, "address").trim());
        //起始时间
        String b_time = RequestUtil.processParams(req, "b_time");
        //购买月数
        Integer months = RequestUtil.getInteger(req, "months", 1);
        //修改月卡会员编号cardId为主键id
        Long nextid = commonDao.selectSequence(CarowerProduct.class);
        String cardId = String.valueOf(nextid);

        Integer flag = RequestUtil.getInteger(req, "flag", -1);
        //备注
        String remark = StringUtils.decodeUTF8(RequestUtil.processParams(req, "remark"));
        String carNumber = StringUtils.decodeUTF8(RequestUtil.processParams(req, "car_number"));
        logger.error("=======>>>>>carNumber"+carNumber);
        //实收金额
        String acttotal = RequestUtil.processParams(req, "act_total");

        Long ntime = System.currentTimeMillis() / 1000;
        Long btime = TimeTools.getLongMilliSecondFrom_HHMMDD(b_time) / 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(btime * 1000);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months);
        Long etime = calendar.getTimeInMillis() / 1000 - 1;

        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setComId(21782L);
        carowerProduct.setCardId(cardId);
        carowerProduct.setIsDelete(0L);
        int count = commonDao.selectCountByConditions(carowerProduct);

        logger.error("======>>>>>>>>count"+count);
        if (count > 0) {
            result.put("msg", "月卡编号重复");
            return result;
        }

        //金额
        Double total = Double.parseDouble(getDataService.getprodsum(pid, months));
        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setId(pid);
        productPackageTb = (ProductPackageTb) commonDao.selectObjectByConditions(productPackageTb);
        Long limitDay = null;
        if (productPackageTb != null && productPackageTb.getLimitday() != null) {
            limitDay = productPackageTb.getLimitday();
        }
        logger.error("======>>>>>>limitDay"+limitDay);
        if (limitDay != null) {
            if (limitDay < etime) {//超出有效期
                result.put("msg", "产品已超出有效期，请重新选择产品或更改购买月数");
                return result;
            }
        }

        Double act_total = total;
        if (!acttotal.equals("")) {
            try{
                act_total = StringUtils.formatDouble(Double.valueOf(acttotal));
            }catch (Exception e){
                result.put("msg","请正确填写实收金额");
                return result;
            }
        }
        logger.error("=======>>>>>act_total"+act_total);

        Long uin =-1L;
        //添加生成月卡会员时的车主编号
        if(carNumber != null && !carNumber.equals("")){
            String [] carNumStrings = carNumber.split(",");
            Long validuin = -1L;
            if(carNumStrings != null && carNumStrings.length>0){
                for(String strNum :carNumStrings){
                    strNum.toUpperCase();
                    logger.error("==>>>.strNum"+strNum);
                    if(StringUtils.checkPlate(strNum)){
                        CarInfoTb carInfoTb = new CarInfoTb();
                        carInfoTb.setCarNumber(strNum);
                        carInfoTb = (CarInfoTb) commonDao.selectObjectByConditions(carInfoTb);
                        if (carInfoTb != null && carInfoTb.getId() != null) {
                            uin = carInfoTb.getUin();
                        }
                        if(uin>0){
                            validuin = uin;
                        }
                        //修改或添加车牌时查询此车牌是否已经对应有月卡会员记录
                        String subCar = strNum.startsWith("无")?strNum:"%"+strNum.substring(1)+"%";
                        logger.error("======>>>>>subCar"+subCar);
                        String sql = "select pid, car_number from  carower_product where com_id=" + comid + " and is_delete=0 and car_number like '" + subCar+"'";
                        logger.error("======>>>sql"+sql);
                        List<Map> carinfoList = commonDao.getObjectBySql(sql);
                        logger.error("=====>>>>>carinfoList"+carinfoList.size());
                        Long pidMonth = -1L;
                        boolean isMonthUser = false;
                        if(carinfoList != null && !carinfoList.isEmpty() && carinfoList.size()>0){
                            for (Iterator iterator = carinfoList.iterator(); iterator
                                    .hasNext();) {
                                Map uinmap = (Map) iterator.next();
                                pidMonth = (Long)uinmap.get("pid");
                                String carNumUnique = String.valueOf(uinmap.get("car_number"));
                                logger.error("====>>>>>>pidMonth"+pidMonth);
                                logger.error("====>>>>>>carNumUnique"+carNumUnique);
                                if (pidMonth != null && pidMonth != -1) {
                                    String isMonthUsersql = "select p.b_time,p.e_time,p.type from product_package_tb p,carower_product c where c.pid=p.id and p.comid=" + comid + " and c.car_number='" + carNumUnique + "' order by c.id desc ";
                                    List<Map<String, Object>> list = commonDao.getObjectBySql(isMonthUsersql);
                                    if (list != null && list.size() > 0) {
                                        isMonthUser = true;
                                    }
                                }
								/*
								 * 假如改车牌已经注册过月卡会员，车牌一致时才能继续添加，否则提示已注册
								 * 其中A,B和B,A也代表车牌是一致的
								 */
                                boolean isSameCarNumber = isCarNumberSame(carNumber, carNumUnique);
                                logger.error("=====>>>>>>isSameCarNumber"+isSameCarNumber);
                                if(isMonthUser && isSameCarNumber){
                                    isMonthUser = false;
                                    carNumber = carNumUnique;
                                }
                            }
                            logger.error("=====>>>>isMonthUser"+isMonthUser);
                            if(isMonthUser){
                                result.put("msg", "该车牌已注册为月卡会员,请修改车牌");
                                return result;
                            }
                        }
                    }else{
                        result.put("msg", "车牌号有误");
                        return result;
                    }
                }
                uin = validuin;
            }
        }
        Long cartypeId =-1L;
        if(productPackageTb!=null&&productPackageTb.getCarTypeId()!=null){
            try{
                cartypeId =Long.parseLong(productPackageTb.getCarTypeId());
            }catch (Exception e){

            }
        }
        logger.error(">>>>>>>>>>>>>>>>"+cartypeId);

        Integer limit_day_type = RequestUtil.getInteger(req, "limit_day_type", 0);
        //组装增加会员参数插入数据库
        CarowerProduct carowerProduct1 = new CarowerProduct();
        carowerProduct1.setId(nextid);
        carowerProduct1.setUin(uin);
        carowerProduct1.setPid(pid);
        carowerProduct1.setCreateTime(ntime);
        carowerProduct1.setUpdateTime(ntime);
        carowerProduct1.setbTime(btime);
        carowerProduct1.seteTime(etime);
        carowerProduct1.setTotal(new BigDecimal(total+""));
        carowerProduct1.setRemark(remark);
        carowerProduct1.setName(name);
        carowerProduct1.setAddress(address);
        carowerProduct1.setActTotal(new BigDecimal(act_total+""));
        carowerProduct1.setComId(comid);
        carowerProduct1.setCarNumber(carNumber);
        carowerProduct1.setCardId(nextid+"");
        carowerProduct1.setCarTypeId(cartypeId);
        carowerProduct1.setLimitDayType(limit_day_type);
        int ret = commonDao.insert(carowerProduct1);

        //******添加月卡消费记录*******
        Integer renewId = (commonDao.selectSequence(CardRenewTb.class)).intValue();
        String tradeNo  = TimeTools.getTimeYYYYMMDDHHMMSS()+""+comid;
        //对于字符串类型 最好都要进行编码解码处理  防止中文乱码
        String operater = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname"));
        //组装插入月卡消费记录数据
        CardRenewTb cardRenewTb = new CardRenewTb();
        cardRenewTb.setId(renewId);
        cardRenewTb.setTradeNo(tradeNo);
        cardRenewTb.setCardId(cardId);
        cardRenewTb.setPayTime(ntime.intValue());
        cardRenewTb.setAmountReceivable(total+"");
        cardRenewTb.setAmountPay(act_total+"");
        cardRenewTb.setCollector(operater);
        cardRenewTb.setPayType("现金");
        cardRenewTb.setCarNumber(carNumber);
        cardRenewTb.setUserId(name);
        cardRenewTb.setResume(remark);
        cardRenewTb.setBuyMonth(months);
        cardRenewTb.setComid(comid+"");
        cardRenewTb.setCreateTime(ntime.intValue());
        cardRenewTb.setUpdateTime(ntime.intValue());
        int renew = commonDao.insert(cardRenewTb);

        if(ret==1&&renew==1){
            result.put("state",1);
            result.put("msg","添加成功");
           int ins = insertSysn(carowerProduct1,0,comid);
           if(ins!=1){
               logger.error("======>>>>>>>>>插入同步表失败");
           }
        }

        return result;
    }

    @Override
    public JSONObject editCarNum(Long id, String carNumber, Long comid) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setId(id);
        carowerProduct = (CarowerProduct) commonDao.selectObjectByConditions(carowerProduct);
        String carNumberBefore = "";
        if (carowerProduct != null) {
            carNumberBefore = carowerProduct.getCarNumber();
        }
        int ret = 0;
        logger.error("=====>>>>>更改车牌carNumber"+carNumber);
        logger.error("=======>>>更改车牌carNumberBefore"+carNumberBefore);
        if (carNumber != null && !carNumber.equals("")) {
            if (!carNumber.equals(carNumberBefore)) {
                //对修改车牌的逻辑加一层校验，验证车牌是否有效
                //添加修改月卡会员车牌时的车主编号
                Long uin = -1L;
                String[] carNumStrings = carNumber.split(",");
                Long validuin = -1L;
                logger.error("===========carNumStrings"+carNumStrings.length);
                if (carNumStrings != null && carNumStrings.length > 0) {
                    for (String strNum : carNumStrings) {
                        strNum.toUpperCase();
                        if (StringUtils.checkPlate(strNum)) {
                            CarInfoTb carInfoTb = new CarInfoTb();
                            carInfoTb.setCarNumber(strNum);
                            carInfoTb = (CarInfoTb) commonDao.selectObjectByConditions(carInfoTb);
                            if (carInfoTb != null && carInfoTb.getId() != null) {
                                uin = carInfoTb.getUin();
                            }
                            if (uin > 0) {
                                validuin = uin;
                            }
                            //修改或添加车牌时查询此车牌是否已经对应有月卡会员记录
                            String subCar = strNum.startsWith("无") ? strNum : "%" + strNum.substring(1) + "%";
                            String sql = "select pid, car_number from  carower_product where com_id=" + comid + " and is_delete=0 and car_number like '" + subCar +"'";
                            List<Map> carinfoList = commonDao.getObjectBySql(sql);
                            Long pid = -1L;
                            boolean isMonthUser = false;
                            //定义月卡会员记录中对应的车牌
                            String carNumUnique = "";
                            if (carinfoList != null && !carinfoList.isEmpty() && carinfoList.size() > 0) {
                                for (Iterator iterator = carinfoList.iterator(); iterator
                                        .hasNext(); ) {
                                    Map uinmap = (Map) iterator.next();
                                    pid = (Long) uinmap.get("pid");
                                    carNumUnique = String.valueOf(uinmap.get("car_number"));
                                    if (pid != null && pid != -1) {
                                        String isMonthUsersql = "select p.b_time,p.e_time,p.type from product_package_tb p," +
                                                "carower_product c where c.pid=p.id and p.comid=" + comid + " and c.car_number='" + carNumUnique + "' order by c.id desc ";
                                        List<Map<String, Object>> list = commonDao.getObjectBySql(isMonthUsersql);
                                        if (list != null && list.size() > 0) {
                                            isMonthUser = true;
                                        }
                                    }
                                    boolean isSameCarNumber = isCarNumberSame(carNumber, carNumUnique);
                                    if (isMonthUser && isSameCarNumber) {
                                        isMonthUser = false;
                                    } else if (isMonthUser && carinfoList.size() < 2) {
                                        isMonthUser = false;
                                    }
                                }
                                if (isMonthUser) {
                                    result.put("msg", "该车牌:" + carNumberBefore + "已注册为月卡会员,请修改车牌");
                                    return result;
                                }
                            }
                        } else {
                            result.put("msg", "车牌号有误");
                            return result;
                        }
                    }
                    uin = validuin;
                }
                CarowerProduct carowerProduct1 = new CarowerProduct();
                if (id > 0 && carNumber.length() > 6) {
                    carowerProduct1.setId(id);
                    carowerProduct1.setCarNumber(carNumber);
                    carowerProduct1.setUin(uin);
                    ret = commonDao.updateByPrimaryKey(carowerProduct1);
                }
                if (ret > 0) {
                    result.put("state",1);
                    result.put("msg","修改车牌成功");
                    int r = insertSysn(carowerProduct1, 1, comid);
                    logger.error("parkadmin or admin:" + carowerProduct1 + " add comid:" + comid + " vipuser ,add sync ret:" + r);
                    String allSql = "select * from carower_product where car_number= '" + carNumberBefore + "' and com_id=" + comid + " and is_delete=0";
                    List<Map> list = commonDao.getObjectBySql(allSql);
                    for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                        Map uinmap = (Map) iterator.next();
                        Long idLong = Long.valueOf(String.valueOf(uinmap.get("id")));
                        //根据查询出来对应的月卡会员id修改所有的车牌记录
                        CarowerProduct carowerProduct2 = new CarowerProduct();
                        carowerProduct2.setId(idLong);
                        carowerProduct2.setCarNumber(carNumber);
                        int ins = commonDao.updateByPrimaryKey(carowerProduct2);
                        if (ins == 1) {
                            insertSysn(carowerProduct2, 1, comid);
                            logger.error(">>>>>>>>>>>>>>新添加月卡会员修改记录id:" + idLong);
                        }
//                        mongoDbUtils.saveLogs( request,0, 3, "修改了车牌："+carNumber);
                    }
//                    mongoDbUtils.saveLogs( request,0, 3, "修改了车牌："+carNumber);
                } else {
                    logger.error(">>>>>>>>>>>>>>>>对应的月卡会员车牌编号未修改成功，id：" + id);
                }
            } else {
                result.put("state", 1);
                result.put("msg", "修改车牌成功");
            }
        } else {
            result.put("msg", "修改车牌车牌不能为空");
            return result;
        }
        return result;
    }

    @Override
    public JSONObject renewProduct(HttpServletRequest req) {
        String str = "{\"state\":0,\"msg\":\"续费失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = RequestUtil.getLong(req,"comid",-1L);
        Long pid = RequestUtil.getLong(req, "p_name", -1L);
        String name = StringUtils.decodeUTF8(RequestUtil.processParams(req, "name").trim());
        //起始时间
//        String b_time = RequestUtil.processParams(req, "b_time");
        //购买月数
        Integer months = RequestUtil.getInteger(req, "months", 1);

        Long id = RequestUtil.getLong(req, "id", -1L);

        //备注
        String remark = StringUtils.decodeUTF8(RequestUtil.processParams(req, "remark"));
        //实收金额
        String acttotal = RequestUtil.processParams(req, "act_total");

        Long ntime = System.currentTimeMillis()/1000;
//        Long btime = TimeTools.getLongMilliSecondFrom_HHMMDD(b_time)/1000+86400;
        Long btime = RequestUtil.getLong(req,"b_time",ntime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(btime*1000);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+months);
        Long etime = calendar.getTimeInMillis()/1000-1;

        //金额
        Double total = Double.parseDouble(getDataService.getprodsum(pid, months));
        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setId(pid);
        productPackageTb = (ProductPackageTb) commonDao.selectObjectByConditions(productPackageTb);
        Long limitDay = null;
        if (productPackageTb != null && productPackageTb.getLimitday() != null) {
            limitDay = productPackageTb.getLimitday();
        }
        if (limitDay != null) {
            if (limitDay < etime) {//超出有效期
                result.put("msg", "产品已超出有效期，请重新选择产品或更改购买月数");
                return result;
            }
        }

        Double act_total = total;
        if (!acttotal.equals("")) {
            try{
                act_total = StringUtils.formatDouble(Double.valueOf(acttotal));
            }catch (Exception e){
                result.put("msg","请正确填写金额");
                return result;
            }

        }

        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setId(id);
        carowerProduct.seteTime(etime);
        int ret = commonDao.updateByPrimaryKey(carowerProduct);
        if(ret==1){
            carowerProduct = (CarowerProduct)commonDao.selectObjectByConditions(carowerProduct);

            //******添加月卡消费记录*******
            Integer renewId = (commonDao.selectSequence(CardRenewTb.class)).intValue();
            String tradeNo  = TimeTools.getTimeYYYYMMDDHHMMSS()+""+comid;
            String operater = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname"));
            //组装插入月卡消费记录数据
            CardRenewTb cardRenewTb = new CardRenewTb();
            cardRenewTb.setId(renewId);
            cardRenewTb.setTradeNo(tradeNo);
            cardRenewTb.setCardId(carowerProduct.getCardId());
            cardRenewTb.setPayTime(ntime.intValue());
            cardRenewTb.setAmountReceivable(total+"");
            cardRenewTb.setAmountPay(act_total+"");
            cardRenewTb.setCollector(operater);
            cardRenewTb.setPayType("现金");
            cardRenewTb.setCarNumber(carowerProduct.getCarNumber());
            cardRenewTb.setUserId(name);
            cardRenewTb.setResume(remark);
            cardRenewTb.setBuyMonth(months);
            cardRenewTb.setComid(comid+"");
            cardRenewTb.setCreateTime(ntime.intValue());
            cardRenewTb.setUpdateTime(ntime.intValue());
            cardRenewTb.setLimitTime(etime);
            int renew = commonDao.insert(cardRenewTb);

            if(renew==1){
                result.put("state",1);
                result.put("msg","续费成功");
                int ins = insertSysn(carowerProduct,1,comid);
                if(ins!=1){
                    logger.error("======>>>>插入同步表失败");
                }
            }

        }
//            daService.update("insert into sync_info_pool_tb(comid,table_name,table_id,create_time,operate) values(?,?,?,?,?)", new Object[]{comid,"card_renew_tb",renewId,System.currentTimeMillis()/1000,0});
        return result;
    }

    private int insertSysn(CarowerProduct carowerProduct, Integer operater, Long comid) {
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(comid);
        syncInfoPoolTb.setTableId(carowerProduct.getId());
        syncInfoPoolTb.setTableName("carower_product");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis() / 1000);
        syncInfoPoolTb.setOperate(operater);
        return commonDao.insert(syncInfoPoolTb);
    }

    private boolean isCarNumberSame(String carNumber, String carNumberMonth) {
        int length1 = carNumber.length();
        int length2 = carNumberMonth.length();
        int length4 = 0;
        //判断月卡会员表中车牌号中所含有的车牌个数
        if (carNumberMonth != null && length2 > 0) {
            String[] carNumberMonths = carNumberMonth.split(",");
            for (String str : carNumberMonths) {
                if (str != null && str.length() > 0) {
                    length4++;
                }
            }
        }
        //先判断车牌号长度是否一致
        if (carNumber != null && length1 > 0 && length1 == length2) {
            int length3 = 0;
            String[] strings = carNumber.split(",");
            for (String str : strings) {
                if (carNumberMonth.contains(str)) {
                    length3++;
                }
            }
            //判断是否一致
            if (length3 == length4) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public List<List<String>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderfield");
        reqParameterMap.remove("orderby");

        JSONObject result = selectResultByConditions(reqParameterMap);
        List<CarowerProduct> orderlist = JSON.parseArray(result.get("rows").toString(),CarowerProduct.class);

        logger.error("=========>>>>>>.导出订单" + orderlist.size());

        List<List<String>> bodyList = new ArrayList<List<String>>();
        if(orderlist!=null&&orderlist.size()>0){
//            mongoDbUtils.saveLogs( request,0, 5, "导出会员数量："+list.size());
            String [] f = new String[]{"id","p_name","mobile"/*,"uin"*/,"name","car_number","create_time","b_time","e_time","total","car_type_id","limit_day_type","remark"};
            for(CarowerProduct carowerProduct : orderlist){
                List<String> values = new ArrayList<String>();
                OrmUtil<CarowerProduct> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(carowerProduct);
                for(String field : f){
                    if("p_name".equals(field)){
                        if(map.get("pid")!= null) {
                            ProductPackageTb productPackageTb = getProduct(Long.parseLong(map.get("pid") + ""));
                            if(productPackageTb!=null){
                                values.add(productPackageTb.getpName());
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add("");
                        }
                    }else if("car_type_id".equals(field)){
                        if(map.get("car_type_id")!= null){
                            CarTypeTb carTypeTb = null;
                            try{
                                carTypeTb = getCarType(Long.parseLong(map.get("car_type_id") + ""));
                            }catch (Exception e){
                                values.add(map.get("car_type_id")+"");
                            }
                            if(carTypeTb!=null){
                                values.add(carTypeTb.getName());
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add("");
                        }
                    }else if("limit_day_type".equals(field)){
                        if(map.get("limit_day_type")!=null){
                            if((Integer)map.get("limit_day_type")==0){
                                values.add("不限行");
                            }else if((Integer)map.get("limit_day_type")==1){
                                values.add("限行");
                            }
                        }else{
                            values.add("");
                        }
                    } else{
                        if("create_time".equals(field)||"b_time".equals(field)||"e_time".equals(field)){
                            if(map.get(field)!=null){
                                values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                            }else {
                                values.add("");
                            }
                        }else{
                            values.add(map.get(field)+"");
                        }
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }

    private CarTypeTb getCarType(long car_type_id) {
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setId(car_type_id);
        return (CarTypeTb)commonDao.selectObjectByConditions(carTypeTb);
    }

    private ProductPackageTb getProduct(long pid) {
        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setId(pid);
        return (ProductPackageTb)commonDao.selectObjectByConditions(productPackageTb);
    }

}
