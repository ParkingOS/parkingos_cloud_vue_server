package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityVipService;
import parkingos.com.bolink.service.GetDataService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class CityVipServiceImpl implements CityVipService {

    Logger logger = LoggerFactory.getLogger(CityVipServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<CarowerProduct> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;
    @Autowired
    private SaveLogService saveLogService;
    @Autowired
    CommonUtils commonUtils;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        List<CarowerProduct> list =null;
        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();

        //组装所有查询参数
        CarowerProduct carowerProduct = new CarowerProduct();

        carowerProduct.setIsDelete(0L);


        String groupid = reqmap.get("groupid");
        String cityid = reqmap.get("cityid");
        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);

        Map searchMap = supperSearchService.getBaseSearch(carowerProduct,reqmap);
        logger.info(searchMap+"");
        if(searchMap!=null&&!searchMap.isEmpty()){
            CarowerProduct baseQuery =(CarowerProduct)searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if(searchMap.containsKey("supper")) {
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            }
            PageOrderConfig config = null;
            if(searchMap.containsKey("config")) {
                config = (PageOrderConfig) searchMap.get("config");
            }
            List parks =new ArrayList();

            if(groupid !=null&&!"".equals(groupid)){
                parks = commonMethods.getParks(Long.parseLong(groupid));
            }else if(cityid !=null&&!"".equals(cityid)){
                parks = commonMethods.getparks(Long.parseLong(cityid));
            }

            System.out.println("=======parks:"+parks);
            if(parks!=null&&!parks.isEmpty()){
                //封装searchbean  要
                SearchBean searchBean = new SearchBean();
                searchBean.setOperator(FieldOperator.CONTAINS);
                searchBean.setFieldName("com_id");
                searchBean.setBasicValue(parks);

                if (supperQuery == null) {
                    supperQuery = new ArrayList<SearchBean>();
                }
                supperQuery.add( searchBean );

                count = commonDao.selectCountByConditions(baseQuery,supperQuery);
                if(count>0){
                    if(config==null){
                        config = new PageOrderConfig();
                        config.setPageInfo(null,null);
                    }
                    list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
                    if (list != null && !list.isEmpty()) {
                        for (CarowerProduct carowerProduct1 : list) {
                            OrmUtil<CarowerProduct> otm = new OrmUtil<>();
                            Map<String, Object> map = otm.pojoToMap(carowerProduct1);
                            resList.add(map);
                        }
                        result.put("rows", JSON.toJSON(resList));
                    }
                }
            }
        }
        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        logger.error("============>>>>>返回数据"+result);
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

        JSONObject result = selectResultByConditions(reqParameterMap);
        List<CarowerProduct> viplist = JSON.parseArray(result.get("rows").toString(),CarowerProduct.class);

        List<List<String>> bodyList = new ArrayList<List<String>>();
        if(viplist!=null&&viplist.size()>0){
            String [] f = new String[]{"id","p_name","com_id","name","car_number","create_time","b_time","e_time","total","act_total","mobile","car_type_id","limit_day_type","remark"};
            for(CarowerProduct carowerProduct : viplist){
                List<String> values = new ArrayList<String>();
                OrmUtil<CarowerProduct> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(carowerProduct);
                for(String field : f){
                    if("p_name".equals(field)){
                        if(map.get("pid")!= null) {
                            if(Check.isNumber(map.get("pid") + "")){
                                ProductPackageTb productPackageTb = getProduct(Long.parseLong(map.get("pid") + ""));
                                if(productPackageTb!=null){
                                    values.add(productPackageTb.getpName());
                                }else{
                                    values.add("");
                                }
                            }else if("-1".equals(map.get("pid") + "")){
                                values.add("");
                            }else{
                                values.add(map.get("pid") + "");
                            }
                        }else{
                            values.add("");
                        }
                    }else if("car_type_id".equals(field)){
                        if(map.get("car_type_id")!= null){
                            CarTypeTb carTypeTb = null;
                            if(Check.isNumber(map.get("car_type_id") + "")){
                                carTypeTb = getCarType(Long.parseLong(map.get("car_type_id") + ""));
                                if(carTypeTb!=null){
                                    values.add(carTypeTb.getName());
                                }else{
                                    values.add("");
                                }
                            }else if("-1".equals(map.get("car_type_id") + "")){
                                values.add("");
                            }else{
                                values.add(map.get("car_type_id") + "");
                            }
                        }else{
                            values.add("");
                        }
                    }else if("limit_day_type".equals(field)){
                        if(map.get("limit_day_type")!=null){
                            if((Integer)map.get("limit_day_type")==1){
                                values.add("限行");
                            }else if((Integer)map.get("limit_day_type")==0){
                                values.add("不限行");
                            }
                        }else{
                            values.add("");
                        }
                    } else if("create_time".equals(field)||"e_time".equals(field)||"b_time".equals(field)){
                        if(map.get(field)!=null){
                            values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                        }else {
                            values.add("");
                        }
                    }else if("com_id".equals(field)){
                        if (Check.isLong(map.get(field)+"")){
                            Long comid =  Long.parseLong(map.get(field)+"");
                            values.add(getComName(comid));
                        }else {
                            values.add("");
                        }
                    }else{
                        values.add(map.get(field)+"");
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }

    @Override
    public JSONObject createVip(Long groupId,Long comid, String carNumber, String b_time, Integer months, Integer limit_day_type, Long pid, Long carTypeId, String mobile, String name, String address, String pLot,String remark,String nickname,Long uin) throws Exception{
        JSONObject result = new JSONObject();
        result.put("state",0);
        //时间转换
        b_time = b_time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        Date d = format.parse(b_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        b_time = sdf.format(d);

        //校验carNumber
//        if (!Check.isEmpty(carNumber)) {
//            String[] carNumStrings = carNumber.split(",");
//            if (carNumStrings != null && carNumStrings.length > 0) {
//                for (String strNum : carNumStrings) {
//                    strNum = strNum.toUpperCase();
//                    if (StringUtils.checkPlate(strNum)) {
//                        continue;
//                    } else {
//                        result.put("msg", "车牌号有误");
//                        return result;
//                    }
//                }
//            }
//        }

        //校验车位号
        if(!Check.isEmpty(pLot)){
            Pattern letter = Pattern.compile("[\u4e00-\u9fa5a-zA-Z0-9,/_-]+");
            String[] bytes = pLot.split("");
            boolean flag = true;
            for(int i=1;i<bytes.length;i++){
                Matcher matcher = letter.matcher(bytes[i]);
                if(!matcher.matches()){
                    flag = false;
                    break;
                }
            }

            if(!flag){
                result.put("state", 0);
                result.put("msg", "请输入正确的车位");
                return result;
            }
        }

        Long ntime = System.currentTimeMillis() / 1000;
        Long btime = TimeTools.getLongMilliSecondFrom_HHMMDD(b_time) / 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(btime * 1000);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months);
        Long etime = calendar.getTimeInMillis() / 1000 - 1;

        //如果有套餐编号校验
        if(pid>-1) {
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
        }


        //入库
        List<Long> comList = new ArrayList<>();
        if(comid>-1) {
            comList.add(comid);
        }else {
            ComInfoTb comInfoTb = new ComInfoTb();
            comInfoTb.setGroupid(groupId);
            comInfoTb.setState(0);
            PageOrderConfig pageOrderConfig = new PageOrderConfig();
            pageOrderConfig.setPageInfo(null,null);
            List<ComInfoTb> list = commonDao.selectListByConditions(comInfoTb,pageOrderConfig);
            if(list!=null&&list.size()>0){
                for(ComInfoTb com:list){
                    comList.add(com.getId());
                }
            }
        }
        logger.info("集团创建月卡："+carNumber+"~~"+comList);
        if(comList.size()<1){
            result.put("msg","参数错误！");
            return result;
        }

        result = createVipByComs(result,comList,groupId,pid,carTypeId,ntime,btime,etime,remark,name,address,pLot,carNumber,mobile,limit_day_type,months,nickname,uin);

        return result;
    }

    @Override
    public JSONObject editVip(Long groupId, Long id, String carNumber, Integer limit_day_type, String mobile, String name, String pLot,String nickname,Long uin) {
        JSONObject result = new JSONObject();
        //校验carNumber
//        if (!Check.isEmpty(carNumber)) {
//            String[] carNumStrings = carNumber.split(",");
//            if (carNumStrings != null && carNumStrings.length > 0) {
//                for (String strNum : carNumStrings) {
//                    strNum = strNum.toUpperCase();
//                    if (StringUtils.checkPlate(strNum)) {
//                        continue;
//                    } else {
//                        result.put("msg", "车牌号有误");
//                        return result;
//                    }
//                }
//            }
//        }

        //校验车位号
        if(!Check.isEmpty(pLot)){
            Pattern letter = Pattern.compile("[\u4e00-\u9fa5a-zA-Z0-9,/_-]+");
            String[] bytes = pLot.split("");
            boolean flag = true;
            for(int i=1;i<bytes.length;i++){
                Matcher matcher = letter.matcher(bytes[i]);
                if(!matcher.matches()){
                    flag = false;
                    break;
                }
            }

            if(!flag){
                result.put("state", 0);
                result.put("msg", "请输入正确的车位");
                return result;
            }
        }

        //验证通过  更新月卡信息
        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setId(id);
        carowerProduct.setCarNumber(carNumber.toUpperCase());
        carowerProduct.setMobile(mobile);
        carowerProduct.setpLot(pLot);
        carowerProduct.setName(name);
        carowerProduct.setLimitDayType(limit_day_type);
        int update = commonDao.updateByPrimaryKey(carowerProduct);

        if(update==1){
            result.put("state",1);
            result.put("msg","更新成功");
            ExecutorService es = ExecutorsUtil.getExecutorService();
            es.execute(new Runnable() {
                @Override
                public void run() {
                    CarowerProduct carowerProduct1 = new CarowerProduct();
                    carowerProduct1.setId(id);
                    carowerProduct1 =(CarowerProduct) commonDao.selectObjectByConditions(carowerProduct1);
                    String parkName = getParkName(carowerProduct1.getComId());
                    boolean issend1 = commonUtils.sendMessage(carowerProduct1, carowerProduct1.getComId(), id, 2);
                    int r = insertSysn(carowerProduct1, 1, carowerProduct1.getComId());
                    commonUtils.sendNotice(2, carowerProduct1.getComId(), carowerProduct1.getCarNumber(), carowerProduct1.geteTime(), 0, carowerProduct1.getMobile(), parkName);

                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
                    parkLogTb.setOperateType(2);
                    parkLogTb.setContent(uin+"("+nickname+")"+"集团修改了"+carowerProduct1.getComId()+"车场会员"+carowerProduct1.getCardId());
                    parkLogTb.setType("vip");
                    parkLogTb.setGroupId(groupId);
                    saveLogService.saveLog(parkLogTb);

                }
            });
        }
        return result;
    }

    @Override
    public JSONObject deleteVip(Long groupId, Long id, String nickname, Long uin) {
        JSONObject result = new JSONObject();
        result.put("state",0);

        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setId(id);
        carowerProduct.setIsDelete(1L);
        int delete = commonDao.updateByPrimaryKey(carowerProduct);
        if(delete==1){
            result.put("state",1);
            result.put("msg","删除成功！");
            ExecutorService es = ExecutorsUtil.getExecutorService();
            es.execute(new Runnable() {
                @Override
                public void run() {
                    CarowerProduct carowerProduct1 = new CarowerProduct();
                    carowerProduct1.setId(id);
                    carowerProduct1 =(CarowerProduct) commonDao.selectObjectByConditions(carowerProduct1);
                    String parkName = getParkName(carowerProduct1.getComId());
                    boolean issend1 = commonUtils.sendMessage(carowerProduct1, carowerProduct1.getComId(), id, 3);
                    int r = insertSysn(carowerProduct1, 2, carowerProduct1.getComId());
//                    commonUtils.sendNotice(2, carowerProduct1.getComId(), carowerProduct1.getCarNumber(), carowerProduct1.geteTime(), 0, carowerProduct1.getMobile(), parkName);

                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
                    parkLogTb.setOperateType(3);
                    parkLogTb.setContent(uin+"("+nickname+")"+"集团删除了"+carowerProduct1.getComId()+"车场会员"+carowerProduct1.getCardId());
                    parkLogTb.setType("vip");
                    parkLogTb.setGroupId(groupId);
                    saveLogService.saveLog(parkLogTb);

                }
            });
        }
        return result;
    }

    private String getParkName(Long comid) {
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb=(ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null){
            return comInfoTb.getCompanyName();
        }
        return "";
    }

    private JSONObject createVipByComs(JSONObject result, List<Long> comList,Long groupId, Long pid, Long carTypeId, Long ntime, Long btime, Long etime, String remark, String name, String address, String pLot, String carNumber, String mobile, Integer limit_day_type,Integer months,String nickname,Long uin) {
        String msg = "新建月卡成功";
        ExecutorService es = ExecutorsUtil.getExecutorService();
        for(Long comid:comList) {

            String parkName = getParkName(comid);

            Long nextid = commonDao.selectSequence(CarowerProduct.class);
            String cardId = String.valueOf(nextid);

            CarowerProduct carowerProduct = new CarowerProduct();
            carowerProduct.setComId(comid);
            carowerProduct.setCardId(cardId);
            carowerProduct.setIsDelete(0L);
            int count = commonDao.selectCountByConditions(carowerProduct);

            logger.error("======>>>>>>>>count" + count);
            if (count > 0) {
                msg+= comid+"车场卡号重复！";
//                result.put("msg", "月卡编号重复");
//                return result;
                continue;
            }

            CarowerProduct carowerProduct1 = new CarowerProduct();
            carowerProduct1.setId(nextid);
            carowerProduct1.setPid(pid);
            carowerProduct1.setCarTypeId(carTypeId);
            carowerProduct1.setCreateTime(ntime);
            carowerProduct1.setUpdateTime(ntime);
            carowerProduct1.setbTime(btime);
            carowerProduct1.seteTime(etime);
            carowerProduct1.setTotal(new BigDecimal("0.0"));
            carowerProduct1.setRemark(remark);
            carowerProduct1.setName(name);
            carowerProduct1.setAddress(address);
            carowerProduct1.setpLot(pLot);
            carowerProduct1.setActTotal(new BigDecimal("0.0"));
            carowerProduct1.setComId(comid);
            carowerProduct1.setCarNumber(carNumber.toUpperCase());
            carowerProduct1.setCardId(cardId);
            carowerProduct1.setMobile(mobile);
            carowerProduct1.setLimitDayType(limit_day_type);
            int ret = commonDao.insert(carowerProduct1);

            es.execute(new Runnable() {
                @Override
                public void run() {
                    boolean issend = commonUtils.sendMessage(carowerProduct1, comid, nextid, 1);
                    logger.info("........发送月卡数据" + issend);
                    int ins = insertSysn(carowerProduct1, 0, comid);
                    commonUtils.sendNotice(1, comid, carNumber, etime, months, mobile, parkName);
                }
            });
        }
        String comRes = "";
        if(comList.size()>1){
            comRes = "所有";
        }else{
            comRes =comList.get(0)+"";
        }
        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setOperateUser(nickname);
        parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
        parkLogTb.setOperateType(1);
        parkLogTb.setContent(uin+"("+nickname+")"+"集团给"+comRes+"车场创建了会员"+carNumber);
        parkLogTb.setType("vip");
        parkLogTb.setGroupId(groupId);
        saveLogService.saveLog(parkLogTb);
        result.put("state", 1);
        result.put("msg", msg);
        return result;
    }

    @Override
    @Transactional
    public JSONObject importExcel(MultipartFile file,Long groupid,Long cityid,String nickname,Long uin)  throws Exception{


        OrgGroupTb orgGroupTb = new OrgGroupTb();
        orgGroupTb.setId(groupid);
        orgGroupTb = (OrgGroupTb)commonDao.selectObjectByConditions(orgGroupTb);

        JSONObject result = new JSONObject();

        String errmsg ="";
        String filename = ""; // 上传文件保存到服务器的文件名
        InputStream is = null; // 当前上传文件的InputStream对象

        if(file!=null) {
            filename = file.getOriginalFilename();
            try {
                is = file.getInputStream();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        logger.info("====上传月卡会员:"+filename);

        if(is!=null&&filename!=null){
            List<Object[]> syncValues = new ArrayList<>();
            List<Object[]> insertValues = new ArrayList<>();
            List<Object[]> updateValues = new ArrayList<>();
            Long ntime= TimeTools.getToDayBeginTime();
            List<Object[]> datas = ImportExcelUtil.generateUserSql(is,filename,1);
            List<Object[]> newdatas = new ArrayList<>();
            List<Object[]> updateDatas = new ArrayList<>();
            Map<Long,Integer> comMaps = new HashMap<>();
            if(datas!=null&&!datas.isEmpty()){
                //数据格式：车场编号*	开始时间*	结束时间*	备注	姓名	金额	车牌号*
                //*是必传
                //过滤同一车场同一车牌的数据
                Map<String,Integer> comCarMap = new HashMap<>();
                int i=1;
                for(Object[] o : datas){
//                    if(o.length!=7){
//                        errmsg+=i+"行，数据长度不对，应该为7列,当前"+o.length+"列</br>";
//                        i++;
//                        continue;
//                    }
                    boolean isValid = true;
                    Long comid =null;//车场编号
                    String car_number = "";
                    if(o.length>7) {
                        car_number= o[7] + "";//车牌
                    }
                    String btime = o[1]+"";
                    String etime = o[2]+"";
                    if(Check.isLong(o[0]+"")){
                        comid = Long.valueOf(o[0]+"");
                    }
                    if(Check.isEmpty(comid+"")){
                        errmsg+=i+"行，车场编号错误："+comid;
                        //isValid = false;
                        i++;
                        continue;
                    }
                    if(Check.isEmpty(btime)){
                        errmsg+=i+"行，开始时间错误："+btime;
                        isValid = false;
                    }
                    if(Check.isEmpty(etime)){
                        errmsg+=i+"行，结束时间错误："+etime;
                        isValid = false;
                    }
                    if(Check.isEmpty(car_number)){
                        errmsg+=i+"行，车牌错误："+car_number;
                        isValid = false;
                    }
                    if(isValid){
                        logger.info("============comid:"+comid);
                        //数据库检验车场编号和月卡会员中同一车场同一车牌是否存在
                        //校验车场
                        if(comMaps.containsKey(comid)){
                            Integer ha = comCarMap.get(comid);
                            if(ha!=null&&ha==0){
                                errmsg+=i+"行，车场编号不存在："+comid;
                                isValid = false;
                            }
                        }else{
                            ComInfoTb comInfoTb = new ComInfoTb();
                            comInfoTb.setId(comid);


                            List parks =new ArrayList();
                            if(groupid != -1){
                                parks = commonMethods.getParks(groupid);
                            }else if(cityid != -1){
                                parks = commonMethods.getparks(cityid);
                            }

                            logger.info("=======parks:"+parks);
                            List<SearchBean> searchBeanList = new ArrayList<>();
                            //封装searchbean  集团和城市下面所有车场
                            if(parks!=null&&parks.size()>0) {
                                SearchBean searchBean = new SearchBean();
                                searchBean.setOperator(FieldOperator.CONTAINS);
                                searchBean.setFieldName("id");
                                searchBean.setBasicValue(parks);
                                searchBeanList.add(searchBean);
                            }

                            comInfoTb.setState(0);
                            int count = commonDao.selectCountByConditions(comInfoTb,searchBeanList);
                            if(count<1){
                                errmsg+=i+"行，车场编号不存在或者不可用："+comid;
                                isValid = false;
                            }
                            comMaps.put(comid,count);
                        }
                        if(isValid){
                            if(comCarMap.containsKey(comid+car_number)){
                                Integer ha = comCarMap.get(comid+car_number);
                                if(ha!=null&&ha==1){
                                    //errmsg+=i+"行，数据已存在：(车场-车牌)"+comid+"-"+car_number;
                                    isValid = false;
                                }
                            }else{
                                CarowerProduct carowerProduct = new CarowerProduct();
                                carowerProduct.setComId(comid);
                                carowerProduct.setCarNumber(car_number);
                                carowerProduct.setIsDelete(0L);
                                int count = commonDao.selectCountByConditions(carowerProduct);//daService.getLong("select count(id) from carower_product where com_id =? and car_number=?  ",new Object[]{comid,car_number});
                                if(count>0){
                                    //errmsg+=i+"行，数据已存在：(车场-车牌)"+comid+"-"+car_number;
                                    isValid = false;
                                    updateDatas.add(o);//加入到更新数据中
                                }
                                comCarMap.put(comid+car_number,count);
                            }
                        }

                        if(isValid){
                            newdatas.add(o);//加入到插入数据中
                        }
                        if(!Check.isEmpty(errmsg)&&!errmsg.endsWith("</br>")) {
                            errmsg += "</br>";
                        }
                    }
                    i++;
                }
                if(!newdatas.isEmpty()) {//处理插入数据
                    for (Object[] v : newdatas) {
                        System.out.println(StringUtils.objArry2String(v));
                        //System.out.println(v.length);
                        Long comid = Long.valueOf(v[0] + "");
                        Long btime = ntime;
                        if (!Check.isEmpty(v[1] + "")) {
                            btime = TimeTools.getLongMilliSecondFrom_HHMMDD(v[1] + "") / 1000;
                        }
                        Long etime = ntime;
                        if (!Check.isEmpty(v[2] + "")) {
                            etime = TimeTools.getLongMilliSecondFrom_HHMMDD(v[2] + "") / 1000 + 86399;
                        }
                        Double total = StringUtils.formatDouble(v[5]);
                        Long id = commonDao.selectSequence(CarowerProduct.class);//daService.getkey("seq_carower_product");
                        Object[] va = new Object[]{id, comid, System.currentTimeMillis() / 1000, btime, etime, v[3], v[4], total, v[7], id + "", v[6]};
                        insertValues.add(va);
                        Object[] syncVa = new Object[]{comid, "carower_product", id, ntime, 0};
                        syncValues.add(syncVa);

                    }
                }
                if(!updateDatas.isEmpty()){//处理更新数据
                    for(Object[] o : updateDatas){
                        System.out.println(StringUtils.objArry2String(o));
                        Long comid =Long.valueOf(o[0]+"");
                        Long btime = ntime;
                        if(!Check.isEmpty(o[1]+"")){
                            btime = TimeTools.getLongMilliSecondFrom_HHMMDD(o[1]+"")/1000;
                        }
                        Long etime = ntime;
                        if(!Check.isEmpty(o[2]+""))
                            etime = TimeTools.getLongMilliSecondFrom_HHMMDD(o[2]+"")/1000+86399;
                        Double total = StringUtils.formatDouble(o[5]);
                        Object[] va = new Object[]{btime,etime,o[3],o[4],total,comid,o[7],o[6]};
                        updateValues.add(va);
                        CarowerProduct carowerProduct = new CarowerProduct();
                        carowerProduct.setComId(comid);
                        carowerProduct.setCarNumber(o[7]+"");
                        carowerProduct.setIsDelete(0L);
                        carowerProduct = (CarowerProduct) commonDao.selectObjectByConditions(carowerProduct);

                        Object[] syncVa = new Object[]{comid,"carower_product",carowerProduct.getId(),ntime,1};
                        syncValues.add(syncVa);
                    }
                }
            }else{
                errmsg+="请上传正确的Excel文件";
            }
            if(!insertValues.isEmpty()) {
                int r = 0;
                for(Object[] arr:insertValues){
                    CarowerProduct carowerProduct = new CarowerProduct();
                    carowerProduct.setId((Long)arr[0]);
                    carowerProduct.setComId((Long)arr[1]);
                    carowerProduct.setCreateTime((Long)arr[2]);
                    carowerProduct.setbTime((Long)arr[3]);
                    carowerProduct.seteTime((Long)arr[4]);
                    carowerProduct.setRemark((String)arr[5]);
                    carowerProduct.setName((String)arr[6]);
                    carowerProduct.setActTotal(new BigDecimal(arr[7]+""));
                    carowerProduct.setCarNumber((String)arr[8]);
                    carowerProduct.setCardId((String)arr[9]);
                    carowerProduct.setMobile((String)arr[10]);
                    r += commonDao.insert(carowerProduct);


                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
                    parkLogTb.setOperateType(1);
                    parkLogTb.setContent(uin+"("+nickname+")"+"新建月卡会员成功:"+arr[8]);
                    parkLogTb.setType("vip");
                    parkLogTb.setGroupId(groupid);
                    saveLogService.saveLog(parkLogTb);
                }
                logger.error("批量导入月卡结果：" + r);
                errmsg +="</br>新建"+r+"条";
            }
            if(!updateValues.isEmpty()) {
                int r = 0;
                for(Object[] arr:updateValues){
                    logger.info("update==>>>"+arr);
                    CarowerProduct fields = new CarowerProduct();
                    CarowerProduct conditions = new CarowerProduct();
                    fields.setbTime((Long)arr[0]);
                    fields.seteTime((Long)arr[1]);
                    fields.setRemark((String)arr[2]);
                    fields.setName((String)arr[3]);
                    fields.setActTotal(new BigDecimal(arr[4]+""));
                    fields.setMobile((String)arr[7]);
                    conditions.setComId((Long)arr[5]);
                    conditions.setCarNumber((String)arr[6]);
                    conditions.setIsDelete(0L);
                    r += commonDao.updateByConditions(fields,conditions);

                    ParkLogTb parkLogTb = new ParkLogTb();
                    parkLogTb.setOperateUser(nickname);
                    parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
                    parkLogTb.setOperateType(2);
                    parkLogTb.setContent(uin+"("+nickname+")"+"更新月卡会员成功:"+arr[6]);
                    parkLogTb.setType("vip");
                    parkLogTb.setGroupId(groupid);
                    saveLogService.saveLog(parkLogTb);
                }
                logger.error("批量更新月卡结果：" + r);
                errmsg +="</br>更新"+r+"条";
            }
            if(!syncValues.isEmpty()){
                int r = 0;
                for(Object[] arr:syncValues){
                    SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
                    syncInfoPoolTb.setComid((Long)arr[0]);
                    syncInfoPoolTb.setTableName((String)arr[1]);
                    syncInfoPoolTb.setTableId((Long)arr[2]);
                    syncInfoPoolTb.setCreateTime((Long)arr[3]);
                    syncInfoPoolTb.setOperate((Integer) arr[4]);
                    r += commonDao.insert(syncInfoPoolTb);
                }
                logger.error("批量下发："+r);
                errmsg +="</br>下发"+r+"条";
            }
            result.put("state",1);
            logger.error(errmsg);
        }
        result.put("msg",errmsg);
        return result;

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

    private String getComName(Long comid){
        ComInfoTb comInfoTb  = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null&&comInfoTb.getCompanyName()!=null){
            return comInfoTb.getCompanyName();
        }
        return "";
    }
}
