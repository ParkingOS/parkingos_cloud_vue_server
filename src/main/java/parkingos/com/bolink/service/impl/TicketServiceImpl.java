package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.QrCodeTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.models.TicketTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.TicketService;
import parkingos.com.bolink.utils.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.joda.time.LocalDate;

@Service
public class TicketServiceImpl implements TicketService {

    Logger logger = Logger.getLogger(TicketServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<TicketTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        int count = 0;

        TicketTb ticketTb = new TicketTb();
        //停车场编号
        ticketTb.setComid(Long.valueOf(reqmap.get("comid")));
        //绑定优惠类型,0为默认查询全部，1为时长减免，2为金额减免   1,2分别对应TicketTb中的3 和 5
        Integer type = 0;
        String strType = reqmap.get("type");
        if (strType != null && !strType.equals("")) {
            type = Integer.valueOf(strType);
        }
        if (type == 1) {
            ticketTb.setType(3);
        } else if (type == 2) {
            ticketTb.setType(5);
        }
        //绑定状态  0-未使用,1-已使用，2-回收作废   -1为查询全部
        String strState = reqmap.get("state");
        Integer state = -1;
        if (strState != null && !strState.equals("")) {
            state = Integer.valueOf(strState);
        }
        if (state != -1) {
            ticketTb.setState(state);
        }

        if(reqmap.get("create_time")==null){
            reqmap.put("create_time","between");
            reqmap.put("create_time_start",TimeTools.getToDayBeginTime()+"");
            reqmap.put("create_time_end",TimeTools.getToDayBeginTime()+86400+"");
        }

        Map searchMap = supperSearchService.getBaseSearch(ticketTb, reqmap);
        List<SearchBean> supperQuery = null;
        TicketTb baseQuery = null;
        PageOrderConfig config = null;

        List<TicketTb> list = null;
        if (searchMap != null && !searchMap.isEmpty()) {
            if (searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            if (searchMap.containsKey("config"))
                config = (PageOrderConfig) searchMap.get("config");
            baseQuery = (TicketTb) searchMap.get("base");
        }
        //商户名称模糊查询
        String shopName = reqmap.get("shop_name");
        ShopTb shopTb = new ShopTb();
        shopTb.setComid(Long.valueOf(reqmap.get("comid")));

        SearchBean searchBean = new SearchBean();
        List<SearchBean> searchList = new ArrayList<SearchBean>();
        searchList.add(searchBean);
        Map<Long, String> shopNames = null;
        Map<Long, Integer> shopUnits = null;

        if (shopName != null && !shopName.equals("")) {

            searchBean.setFieldName("name");
            searchBean.setOperator(FieldOperator.LIKE);
            searchBean.setBasicValue(shopName);
            List<ShopTb> shopList = commonDao.selectListByConditions(shopTb, searchList);
            //有查询结果
            if (shopList != null && !shopList.isEmpty()) {
                searchBean.setOperator(FieldOperator.CONTAINS);
                searchBean.setFieldName("shop_id");
                List<Long> idList = new ArrayList<Long>();
                shopNames = new HashMap<>();
                shopUnits = new HashMap<>();
                for (ShopTb s : shopList) {
                    idList.add(s.getId());
                    shopNames.put(s.getId(), s.getName());
                    shopUnits.put(s.getId(), s.getTicketUnit());
                }
                searchBean.setBasicValue(idList);

                if (supperQuery == null) {
                    supperQuery = new ArrayList<>();
                }
                supperQuery.add(searchBean);

            } else {
                //没有查询结果
                result.put("total", 0);
                result.put("total", "");
                result.put("page", Integer.parseInt(reqmap.get("page")));
                return result;
            }
        }
        logger.info(searchMap);

        count = commonDao.selectCountByConditions(baseQuery, supperQuery);
        if (count > 0) {
            list = commonDao.selectListByConditions(baseQuery, supperQuery, config);

            if (list != null && !list.isEmpty()) {

                //查询停车场名称 和ticket_unit
                if (shopNames == null) {
                    shopUnits = new HashMap<>();
                    shopNames = new HashMap<>();
                    List<Long> idList = new ArrayList<>();
                    for (TicketTb product : list) {
                        idList.add(product.getShopId());
                    }
                    searchBean.setOperator(FieldOperator.CONTAINS);
                    searchBean.setFieldName("id");
                    searchBean.setBasicValue(idList);

                    List<ShopTb> shopTbs = commonDao.selectListByConditions(shopTb, searchList);
                    if (shopTbs != null && !shopTbs.isEmpty()) {
                        for (ShopTb s : shopTbs) {
                            shopNames.put(s.getId(), s.getName());
                            shopUnits.put(s.getId(), s.getTicketUnit());
                        }
                    }
                }

                for (TicketTb product : list) {
                    OrmUtil<TicketTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap(product);
                    map.put("shop_name", shopNames.get(product.getShopId()));
                    map.put("ticket_unit", shopUnits.get(product.getShopId()));
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }

        }

        result.put("total", count);
        String page = reqmap.get("page");
        if (page != null) {
            result.put("page", page);
        }
        return result;
    }

    @Override
    //报表导出
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //去掉分页
        reqParameterMap.put("rp", Integer.MAX_VALUE + "");
        reqParameterMap.put("page", "1");
        reqParameterMap.put("orderby", "desc");
        reqParameterMap.put("orderfield", "id");


        List<List<Object>> bodyList = new ArrayList<>();

        JSONObject result = selectResultByConditions(reqParameterMap);

        List<Map> ticketList = JSON.parseArray(result.get("rows").toString(), Map.class);

        if (ticketList != null && ticketList.size() > 0) {
            for (Map map : ticketList) {
                logger.error("~~~~~~"+map);
                List<Object> list = new ArrayList<>();
                list.add(map.get("id"));
                list.add(map.get("shop_name"));
                //list.add( map.get( "money" ) );

                Integer state = (Integer) map.get("state");
                if (state == 0) {
                    list.add("未使用");
                } else if (state == 1) {
                    list.add("已使用");
                } else if (state == 2) {
                    list.add("回收作废");
                } else {
                    list.add(state);
                }
                list.add(map.get("car_number"));

                //优惠时长
                Integer money = (Integer) map.get("money");
                Integer ticketUnit = (Integer) map.get("ticket_unit");
                if(money>0&&ticketUnit!=null){
                    if (ticketUnit == 1 ) {
                        list.add(map.get("money")+"分钟");
                    }
                    else if (ticketUnit == 2 ) {
                        list.add(map.get("money")+"小时");
                    }
                    else if (ticketUnit == 3 ) {
                        list.add(map.get("money")+"天");
                    }else{
                        list.add("");
                    }
                }else{
                    list.add("");
                }

                Double umoney = Double.valueOf(map.get("umoney") + "");
                if (umoney!=null&&umoney > 0) {
                    list.add(map.get("umoney")+"元");
                } else {
                    list.add("");
                }
                Integer type = (Integer) map.get("type");
                if(type!=null){
                    if (type == 3) {
                        list.add("时长减免");
                    } else if (type == 5) {
                        list.add("金额减免");
                    } else if (type == 4) {
                        list.add("全免券");
                    } else {
                        list.add(type);
                    }
                }else{
                    list.add("");
                }

                if(map.get("create_time")!=null){
                    Long create_time = Long.valueOf(map.get("create_time") + "");
                    String date1 = TimeTools.getTime_yyyyMMdd_HHmmss(create_time*1000);
//                    new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date(create_time * 1000));
                    list.add(date1);
                }else{
                    list.add("");
                }

                if(map.get("limit_day")!=null){
                    Long limit_day = Long.valueOf(map.get("limit_day") + "");
                    String date = TimeTools.getTime_yyyyMMdd_HHmmss(limit_day*1000);//new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date(limit_day * 1000));
                    list.add(date);
                }else{
                    list.add("");
                }

                if(map.get("use_time")!=null) {
                    Long use_time = Long.valueOf(map.get("use_time") + "");
                    String date2 = TimeTools.getTime_yyyyMMdd_HHmmss(use_time*1000);//new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date(use_time * 1000));
                    list.add(date2);
                }else {
                    list.add("");
                }

                bodyList.add(list);
            }
        }
        return bodyList;
    }

    @Override
    public JSONObject getTicketLog(Map<String, String> reqmap) {
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        TicketTb ticketTb = new TicketTb();
        ticketTb.setShopId(Long.parseLong(reqmap.get("shopid")));


        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("create_time")));

        if(date==null|| "".equals(date)){
            reqmap.put("create_time","between");
            reqmap.put("create_time_start",TimeTools.getToDayBeginTime()+"");
            reqmap.put("create_time_end",TimeTools.getToDayBeginTime()+86399+"");
        }

        String state = reqmap.get("state");
        if("-1".equals(state)){
            reqmap.remove("state");
        }
        int count = 0;
        List<TicketTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<>();

        Map searchMap = supperSearchService.getBaseSearch(ticketTb, reqmap);
        if (searchMap != null && !searchMap.isEmpty()) {
            TicketTb baseQuery = (TicketTb) searchMap.get("base");
            List<SearchBean> supperQuery = null;
            if (searchMap.containsKey("supper"))
                supperQuery = (List<SearchBean>) searchMap.get("supper");
            PageOrderConfig config = null;
            if (searchMap.containsKey("config"))
                config = (PageOrderConfig) searchMap.get("config");



            count = commonDao.selectCountByConditions(baseQuery, supperQuery);
            if (count > 0) {

                //获得该商户的优惠单位
                ShopTb shopTb = new ShopTb();
                shopTb.setId(Long.parseLong(reqmap.get("shopid")));
                shopTb = (ShopTb) commonDao.selectObjectByConditions(shopTb);
                if (shopTb != null) {
                    logger.info("该商户的优惠单位:" + shopTb.getTicketUnit());
                }

                list = commonDao.selectListByConditions(baseQuery, supperQuery, config);
                if (list != null && !list.isEmpty()) {
                    for (TicketTb ticketTb1 : list) {
                        OrmUtil<TicketTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(ticketTb1);
                        if (shopTb != null) {
                            map.put("ticket_unit", shopTb.getTicketUnit());
                        }
                        resList.add(map);
                    }
                    result.put("rows", JSON.toJSON(resList));
                }
            }
        }
        result.put("total", count);
        result.put("page", Integer.parseInt(reqmap.get("page")));

        return result;
    }

    @Override
    public Map<String, Object> createTicket(Long shop_id, Integer reduce, Integer type, Integer isAuto, Integer number,Integer timeRange,Long uin) {


        logger.info("后台创建优惠券:" + isAuto + "~~~~" + type + "~~~~" + reduce);

        Integer free = 0;//全免劵(张)
        Map<String, Object> rMap = new HashMap<String, Object>();
        if (shop_id == -1) {
            rMap.put("state", -1);
            rMap.put("error", "商户编号>>" + shop_id + "不存在");
            return rMap;
        }
        logger.error(">>>>>>>>>>打印优惠券，优惠券类型type:" + type + ",优惠时长time：" + reduce + ",优惠金额amount：" + reduce + ",商户shop_id:" + shop_id);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        ShopTb shopTb = new ShopTb();
        shopTb.setId(shop_id);
        shopTb.setState(0);
        ShopTb shopInfo = (ShopTb) commonDao.selectObjectByConditions(shopTb);
        if(shopInfo==null){
            rMap.put("state", -1);
            rMap.put("error", "商户不存在");
            return rMap;
        }
        Integer ticket_limit = shopInfo.getTicketLimit() == null ? 0 : shopInfo.getTicketLimit();
        Integer ticketfree_limit = shopInfo.getTicketfreeLimit() == null ? 0 : shopInfo.getTicketfreeLimit();
        Integer ticket_money = shopInfo.getTicketMoney() == null ? 0 : shopInfo.getTicketMoney();
        //未设置有效期,默认24小时
        Integer validite_time = shopInfo.getValiditeTime() == null ? 24 : shopInfo.getValiditeTime();
        Long btime = System.currentTimeMillis() / 1000;

        logger.info("=======btime"+btime+"~~~~"+validite_time+"~~~"+validite_time.longValue());
        //截止有效时间
        Long etime = btime + validite_time.longValue() * 60 * 60;
        logger.info("=======etime"+etime);
        //判断商户额度是否可以发劵
        if (type == 3) {//优惠券-时长
            if (reduce <= 0) {
                logger.error("优惠券额度必须为正数" + ",商户shop_id:" + shop_id);
                rMap.put("state", -2);
                rMap.put("error", "优惠券额度必须为正数");
                return rMap;
            }

            if (ticket_limit < (reduce * number)) {
                logger.error("优惠券额度已用完，还剩余额度" + ticket_limit + ",优惠券时长time：" + reduce + ",商户shop_id:" + shop_id);
                rMap.put("state", -2);
                rMap.put("error", "优惠券额度不够");
                return rMap;
            }

        } else if (type == 5) {//优惠券-金额
            if (reduce <= 0) {
                logger.error("优惠券额度必须为正数" + ",商户shop_id:" + shop_id);
                rMap.put("state", -2);
                rMap.put("error", "优惠券额度必须为正数");
                return rMap;
            }
            if (ticket_money < (reduce * number)) {
                logger.error("优惠券额度已用完，还剩余额度" + ticket_money + ",优惠券金额amount：" + reduce + ",商户shop_id:" + shop_id);
                rMap.put("state", -2);
                rMap.put("error", "优惠券额度不够");
                return rMap;
            }
        } else if (type == 4) {
            free = 1;
            reduce = 0;
            if (ticketfree_limit < number) {
                logger.error("全免券额度已用完，还剩余额度" + ticketfree_limit + ",商户shop_id:" + shop_id);
                rMap.put("state", -2);
                rMap.put("error", "优惠券额度不够");
                return rMap;
            }
        }
        List<Map<String, Object>> bathSql = new ArrayList<Map<String, Object>>();
        //取当前最大减免劵的id然后+1
        Long ticketid = qryMaxTicketId() + 1;
//        Long ticketid = commonDao.selectSequence(TicketTb.class);
        String code = null;
        Long ticketids[] = new Long[]{ticketid};
        String[] codes = StringUtils.getGRCode(ticketids);
        if (codes.length > 0) {
            code = codes[0];
        }
        if (code != null) {
            //生成一张劵
            TicketTb ticketTb = new TicketTb();
            ticketTb.setId(ticketid);
            ticketTb.setCreateTime(btime);
            ticketTb.setLimitDay(etime);
            ticketTb.setRemark("");
            if (type == 3) {
                ticketTb.setMoney(reduce);
            } else if (type == 5) {
                BigDecimal amountDecimal = new BigDecimal(reduce + "");
                ticketTb.setUmoney(amountDecimal);
            }
            ticketTb.setState(0);
            ticketTb.setComid(shopInfo.getComid());
            ticketTb.setType(type);
            ticketTb.setUin(uin);
            ticketTb.setShopId(shop_id);
            ticketTb.setTimeRange(timeRange);
            Integer insertTicket = commonDao.insert(ticketTb);

            //生成code
            QrCodeTb qrCodeTb = new QrCodeTb();
            qrCodeTb.setCtime(System.currentTimeMillis() / 1000);
            qrCodeTb.setType(5);
            qrCodeTb.setCode(code);
            qrCodeTb.setIsauto(isAuto);
            qrCodeTb.setTicketid(ticketid);
            qrCodeTb.setComid(shopInfo.getComid());
            Integer insertQrcode = commonDao.insert(qrCodeTb);

            //更新商户额度
            ShopTb shopTbInfo = new ShopTb();
            if (type == 3) {
                shopTbInfo.setTicketLimit(shopInfo.getTicketLimit() - reduce);
            } else if (type == 5) {
                shopTbInfo.setTicketMoney(shopInfo.getTicketMoney() - reduce);
            } else if (type == 4) {
                shopTbInfo.setTicketfreeLimit(shopInfo.getTicketfreeLimit() - free);
            }
            ShopTb shopConditions = new ShopTb();
            shopConditions.setId(shopInfo.getId());
            Integer updateShop = commonDao.updateByConditions(shopTbInfo, shopConditions);
            logger.error("打印优惠券结果：" + insertTicket + "," + insertQrcode + "," + updateShop + ",商户shop_id:" + shop_id);
            if (insertTicket == 1 && insertQrcode == 1 && updateShop == 1) {
                rMap.put("ticket_url", CustomDefind.getValue("TICKETURL") + code);
                rMap.put("state", 1);
                rMap.put("code", code);
                return rMap;
            }
        }
        rMap.put("state", -1);
        rMap.put("error", "生成减免劵出错，用劵失败");
        return rMap;
    }

    private Long qryMaxTicketId() {
        TicketTb ticketConditions = new TicketTb();
        PageOrderConfig pageOrderConfig = new PageOrderConfig();
        pageOrderConfig.setOrderInfo("id", "desc");
        List<TicketTb> ticketTbs = commonDao.selectListByConditions(ticketConditions, pageOrderConfig);
        if (ticketTbs != null && ticketTbs.size() > 0) {
            return ticketTbs.get(0).getId();
        } else {
            return 0L;//没有生成过减免劵
        }
    }

    @Override
    public Map<String, Object> ifChangeCode(HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("state", 0);
        String code = RequestUtil.getString(request, "code");
        logger.info("扫描二维码的code值:" + code);
        if (code != null && !"".equals(code)) {
            QrCodeTb qrCodeTb = new QrCodeTb();
            qrCodeTb.setCode(code);
            qrCodeTb = (QrCodeTb) commonDao.selectObjectByConditions(qrCodeTb);
            if (qrCodeTb != null) {
                logger.info("qrcodeTb不为空:" + qrCodeTb);
                //是否要一直轮询查询, 确认是否自动更新二维码
                Integer isauto = qrCodeTb.getIsauto();
                if (isauto != null && isauto == 1) {//自动更新
//                    while (true){
                    Long ticketid = qrCodeTb.getTicketid();
                    TicketTb ticketTb = new TicketTb();
                    ticketTb.setId(ticketid);
                    ticketTb = (TicketTb) commonDao.selectObjectByConditions(ticketTb);
                    if (ticketTb != null) {
                        if (ticketTb.getState() == 1) {
                            retMap.put("state", 1);
//                                break;
                        }
                    }
//                    }

                }
            }
        }

        return retMap;
    }

    @Override
    public List<String> getCodeList(Long shopId, Integer reduce, Integer type, Integer number, String code, String serverPath,Integer timeRange,Long uin) {

        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        //生成第一张的二维码
        String url = CustomDefind.getValue("TICKETURL") + code;
        logger.info("下载二维码图片:" + url);
        logger.info("下载二维码图片serverPath:" + serverPath);
        try {
            QrCodeUtil.generateQRCode(url, code, serverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (number > 1) {
            for (int i = 0; i < number - 1; i++) {
                Map map = createTicket(shopId, reduce, type, 0, 1,timeRange,uin);
                code = map.get("code") + "";
                codeList.add(code);
                url = CustomDefind.getValue("TICKETURL") + code;
                try {
                    QrCodeUtil.generateQRCode(url, code, serverPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("code list" + codeList);


        return codeList;
    }

    @Override
    public void exportCode(String code, HttpServletRequest request, HttpServletResponse response) {


        Map<Long, String> nameMap = new HashMap<>();
        Map<Long, Integer> unitMap = new HashMap<>();
        String path = request.getSession().getServletContext().getRealPath("/resource/images/" +code);
        File file = new File(path);
        File[] codeList=file.listFiles();

        //自定义二维码存放路径

        //excel名称
        String sheetName = "商家优惠券"+TimeTools.getTime_yyyyMMdd_HHmmss(System.currentTimeMillis()).substring(5,10).replace("-", "") ;//+ LocalDate.now().toString().substring(4).replace("-", "");
        sheetName = StringUtils.encodingFileName(sheetName);
        try {
            //创建工作薄
            /*Workbook wb = new SXSSFWorkbook(500);
            //创建工作表
            Sheet sheet = wb.createSheet(sheetName);
            Drawing patriarch = sheet.createDrawingPatriarch();*/

            // 创建一个工作薄
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建一个sheet
            HSSFSheet sheet = wb.createSheet();
//            sheet.setAutobreaks(true);
//            sheet.setZoom(1,2);


            HSSFPrintSetup print = (HSSFPrintSetup) sheet.getPrintSetup();
            print.setScale((short) 85);//设置打印缩放70%

            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//            Font titleFont = wb.createFont();
//            titleFont.setFontName("Arial");
//            titleFont.setFontHeightInPoints((short) 16);
//            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
//            style.setFont(titleFont);
            //创建第一行
//            Row titleRow=sheet.createRow(0);
//            titleRow.setHeightInPoints(40);//设置行高
//            //创建单元格
//            Cell titleCell=titleRow.createCell(0);
//            titleCell.setCellValue(sheetName);
//            titleCell.setCellStyle(style);
            //设置合并单元格
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,3));
            int rowNum = codeList.length*2;//计算行数，规定一行4列
            int lastRowNum = 1;
//            if(equipmentNum%4==0) {
//                rowNum = equipmentNum/4;
//            }else {
//                rowNum = equipmentNum/4+1;
//            }
//            int lastRowNum = equipmentNum%4;
            //设置4列宽度

//            sheet.setColumnWidth(1, 42*200);
//            sheet.setColumnWidth(2, 42*200);
//            sheet.setColumnWidth(3, 42*200);
            for (int i = 0; i < rowNum; i++) {
                Row row = sheet.createRow(i);//创建行
                row.setHeightInPoints(120);//设置行高
                if(i%2==0){
                    String codestr = codeList[i/2].getName().substring(0,19);
                    //根据code获得商户的信息。
                    QrCodeTb qrCodeTb = new QrCodeTb();
                    qrCodeTb.setCode(codestr);
                    qrCodeTb = (QrCodeTb)commonDao.selectObjectByConditions(qrCodeTb);
                    Long ticketid = qrCodeTb.getTicketid();
                    TicketTb ticketTb = new TicketTb();
                    ticketTb.setId(ticketid);
                    ticketTb = (TicketTb)commonDao.selectObjectByConditions(ticketTb);
                    Long limitDay = ticketTb.getLimitDay();
                    Long shopid = ticketTb.getShopId();
                    Integer type = ticketTb.getType();
                    String name = "";
                    Integer unit = 2;
                    if(nameMap.containsKey(shopid)){
                        name = nameMap.get(shopid);
                        unit = unitMap.get(shopid);

                    }else{
                        ShopTb shopTb = new ShopTb();
                        shopTb.setId(shopid);
                        shopTb = (ShopTb)commonDao.selectObjectByConditions(shopTb);
                        name = shopTb.getName();
                        unit = shopTb.getTicketUnit();
                        unitMap.put(shopid,unit);
                        nameMap.put(shopid,name);
                    }
                    String reduce = "";
                    if(type==3){
                        if(unit==1){
                            reduce = ticketTb.getMoney()+"分钟";
                        }else if(unit==2){
                            reduce = ticketTb.getMoney()+"小时";
                        }else if(unit==3){
                            reduce = ticketTb.getMoney()+"天";
                        }
                    }else if(type==5){
                        reduce = ticketTb.getUmoney()+"元";
                    }else{
                        reduce = "全免";
                    }

                    sheet.setColumnWidth(0, 50*140);
                    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                    //创建单元格
                    Cell cell=row.createCell(0);
                    style = wb.createCellStyle();
                    style.setWrapText(true);//设置自动换行
                    //垂直居中
                    style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                    //打印时候的左边距
                    sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.1);
                    //设置边线
//                        style.setBorderBottom(CellStyle.BORDER_THIN);
//                        style.setBorderTop(CellStyle.BORDER_THIN);
//                        style.setBorderRight(CellStyle.BORDER_THIN);
                    //设置单元边框颜色
//                        style.setTopBorderColor(HSSFColor.RED.index);
//                        style.setRightBorderColor(HSSFColor.RED.index);
//                        style.setBottomBorderColor(HSSFColor.RED.index);

                    //设置字体
                        HSSFFont font = wb.createFont();
                        font.setFontName("微软雅黑");
                        font.setFontHeightInPoints((short) 10);//设置字体大小

                        style.setFont(font);
                    StringBuilder sb=new StringBuilder();
                    sb.append("商家优惠券");
                    sb.append("\r\n");
                    sb.append("券号:"+ticketid);
                    sb.append("\r\n");
                    sb.append("商家:"+name);
                    sb.append("\r\n");
                    sb.append("优惠额度:"+reduce);
                    sb.append("\r\n");
                    sb.append("有效期止:"+TimeTools.getTime_yyyyMMdd_HHmmss(limitDay*1000));
                    cell.setCellValue(sb.toString());
                    cell.setCellStyle(style);
                }else{
                    path = request.getSession().getServletContext().getRealPath("/resource/images/" +code+"/"+codeList[(i-1)/2].getName());
//                        path = ""+codeList[i].getName();
                    logger.info("====>>>"+path);
                    sheet.setColumnWidth(0, 50 * 140);

                    //设置边线
                    style = wb.createCellStyle();
                        style.setBorderBottom(CellStyle.BORDER_THIN);
//                        style.setBorderTop(CellStyle.BORDER_THIN);
//                        style.setBorderRight(CellStyle.BORDER_THIN);
                    //设置单元边框颜色
//                        style.setTopBorderColor(HSSFColor.RED.index);
//                        style.setRightBorderColor(HSSFColor.RED.index);
                        style.setBottomBorderColor(HSSFColor.RED.index);
                    Cell cell=row.createCell(0);
                    cell.setCellStyle(style);
                    BufferedImage bufferImg = null;//图片
                    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//                        //获取二维码
                    bufferImg = ImageIO.read(new File(path));

                    ImageIO.write(bufferImg, "jpg", byteArrayOut);
                    //插入Excel表格
                    HSSFClientAnchor anchor = new HSSFClientAnchor(5, 5, 0, 0,
                            (short) 0, i, (short) 1, i + 1);
                    patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                            .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }

            }
            response.reset();
//            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + sheetName + ".xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<List<Object>> exportLog(Map<String, String> reqParameterMap,Integer hiddenType) {
        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.put("rp", Integer.MAX_VALUE + "");
        reqParameterMap.put("page", "1");
        reqParameterMap.put("orderby", "desc");
        reqParameterMap.put("orderfield", "id");

        JSONObject result = getTicketLog(reqParameterMap);
        List<Map> ticketList = JSON.parseArray(result.get("rows").toString(), Map.class);
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if(ticketList!=null&&ticketList.size()>0){


            String [] f = new String[]{"car_number","money","umoney","uin","state","type","use_time","create_time","limit_day"};
            if(hiddenType==1){
                f = new String[]{"car_number","umoney","uin","state","type","use_time","create_time","limit_day"};
            }else if(hiddenType==2){
                f= new String[]{"car_number","money","uin","state","type","use_time","create_time","limit_day"};
            }
            for(Map map : ticketList){
                List<Object> values = new ArrayList<Object>();
                Map<Long,String> nameMap = new HashMap<>();
                //判断各种字段 组装导出数据
                for(String field : f){
                    if("uin".equals(field)){
                        String name ="";
                        if(map.get(field)!=null) {
                            if (nameMap.containsKey(map.get(field))) {
                                name = nameMap.get(map.get(field));
                            } else {
                                name = getUinName(Long.valueOf(map.get(field) + ""));
                                nameMap.put(Long.valueOf(map.get(field) + ""), name);
                            }
                            values.add(name);
                        }else{
                            values.add("");
                        }

                    }else if("create_time".equals(field)||"use_time".equals(field)||"limit_day".equals(field)){
                        if(map.get(field)!=null){
                            values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field)+""))*1000));
                        }else{
                            values.add("");
                        }
                    }else if("state".equals(field)){
                        if(map.get(field)!=null){
                            Integer state = (Integer)map.get(field);
                            if (state == 0) {
                                values.add("未使用");
                            } else if (state == 1) {
                                values.add("已使用");
                            } else if (state == 2) {
                                values.add("回收作废");
                            } else {
                                values.add(state);
                            }
                        }else{
                            values.add("");
                        }
                    }else if("type".equals(field)){
                        if(map.get(field)!=null){
                            Integer type = (Integer)map.get(field);
                            if (type == 3) {
                                values.add("时长减免");
                            } else if (type == 5) {
                                values.add("金额减免");
                            } else if (type == 4) {
                                values.add("全免券");
                            } else {
                                values.add(type);
                            }
                        }else{
                            values.add("");
                        }
                    }else if("money".equals(field)){
                        if(map.get(field)!=null){
                            Integer money = (Integer)map.get(field);
                            if(money>0){
                                Integer ticketUnit = (Integer) map.get("ticket_unit");
                                if (ticketUnit == 1) {
                                    values.add(money+"分钟");
                                } else if (ticketUnit == 2) {
                                    values.add(money+"小时");
                                } else if (ticketUnit == 3) {
                                    values.add(money+"天");
                                } else {
                                    values.add("");
                                }
                            }else{
                                values.add("");
                            }

                        }else{
                            values.add("");
                        }
                    }else if("umoney".equals(field)){
                        if(map.get(field)!=null){
                            Double umoney = Double.valueOf(map.get("umoney") + "");
                            if(umoney>0){
                                values.add(umoney+"元");
                            }else{
                                values.add("");
                            }
                        }else{
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


    private String getUinName(Long uin) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);

        String uinName = "";
        if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
            uinName = userInfoTb.getNickname();
        }
        return uinName;
    }
}
