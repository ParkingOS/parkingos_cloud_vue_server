package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.QrCodeTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.models.TicketTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.TicketService;
import parkingos.com.bolink.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TicketServiceImpl implements TicketService {

    Logger logger = Logger.getLogger( TicketServiceImpl.class );

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<TicketTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject( str );

        List<Map<String, Object>> resList = new ArrayList<>();
        int count = 0;

        TicketTb ticketTb = new TicketTb();
        //停车场编号
        ticketTb.setComid( Long.valueOf( reqmap.get( "comid" ) ) );
        //绑定优惠类型,0为默认查询全部，1为时长减免，2为金额减免   1,2分别对应TicketTb中的3 和 5
        Integer type = 0;
        String strType = reqmap.get( "type" );
        if (strType != null && !strType.equals( "" )) {
            type = Integer.valueOf( strType );
        }
        if (type == 1) {
            ticketTb.setType( 3 );
        } else if (type == 2) {
            ticketTb.setType( 5 );
        }
        //绑定状态  0-未使用,1-已使用，2-回收作废   -1为查询全部
        String strState = reqmap.get( "state" );
        Integer state = -1;
        if (strState != null && !strState.equals( "" )) {
            state = Integer.valueOf( strState );
        }
        if (state != -1) {
            ticketTb.setState( state );
        }

        Map searchMap = supperSearchService.getBaseSearch( ticketTb, reqmap );
        List<SearchBean> supperQuery = null;
        TicketTb baseQuery = null;
        PageOrderConfig config = null;

        List<TicketTb> list = null;
        if (searchMap != null && !searchMap.isEmpty()) {
            if (searchMap.containsKey( "supper" ))
                supperQuery = (List<SearchBean>) searchMap.get( "supper" );
            if (searchMap.containsKey( "config" ))
                config = (PageOrderConfig) searchMap.get( "config" );
            baseQuery = (TicketTb) searchMap.get( "base" );
        }
        //商户名称模糊查询
        String shopName = reqmap.get( "shop_name" );
        ShopTb shopTb = new ShopTb();
        shopTb.setComid( Long.valueOf( reqmap.get( "comid" ) ) );

        SearchBean searchBean = new SearchBean();
        List<SearchBean> searchList = new ArrayList<>();
        searchList.add( searchBean );
        Map<Long, String> shopNames = null;

        if (shopName != null && !shopName.equals( "" )) {

            searchBean.setFieldName( "name" );
            searchBean.setOperator( FieldOperator.LIKE );
            searchBean.setBasicValue( shopName );
            List<ShopTb> shopList = commonDao.selectListByConditions( shopTb, searchList );
            //有查询结果
            if (shopList != null && !shopList.isEmpty()) {
                searchBean.setOperator( FieldOperator.CONTAINS );
                searchBean.setFieldName( "shop_id" );
                List<Long> idList = new ArrayList<Long>();
                shopNames = new HashMap<>();
                for (ShopTb s : shopList) {
                    idList.add( s.getId() );
                    shopNames.put( s.getId(), s.getName() );
                }
                searchBean.setBasicValue( idList );

                if (supperQuery == null) {
                    supperQuery = new ArrayList<>();
                }
                supperQuery.add( searchBean );

            } else {
                //没有查询结果
                result.put( "total", 0 );
                result.put( "total", "" );
                result.put( "page", Integer.parseInt( reqmap.get( "page" ) ) );
                return result;
            }
        }
        logger.info( searchMap );

        count = commonDao.selectCountByConditions( baseQuery, supperQuery );
        if (count > 0) {
            list = commonDao.selectListByConditions( baseQuery, supperQuery, config );

            if (list != null && !list.isEmpty()) {

                //查询停车场名称 和ticket_unit
                Map<Long,Integer> shopUnits = new HashMap<>();
                if (shopNames == null) {
                    shopNames = new HashMap<>();
                    List<Long> idList = new ArrayList<>();
                    for (TicketTb product : list) {
                        idList.add( product.getShopId() );
                    }
                    searchBean.setOperator( FieldOperator.CONTAINS );
                    searchBean.setFieldName( "id" );
                    searchBean.setBasicValue( idList );

                    List<ShopTb> shopTbs = commonDao.selectListByConditions( shopTb, searchList );
                    if (shopTbs != null && !shopTbs.isEmpty()) {
                        for (ShopTb s : shopTbs) {
                            shopNames.put( s.getId(), s.getName() );
                            shopUnits.put( s.getId(),s.getTicketUnit() );
                        }
                    }
                }

                for (TicketTb product : list) {
                    OrmUtil<TicketTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap( product );
                    map.put( "shop_name", shopNames.get( product.getShopId() ) );
                    map.put( "ticket_unit",shopUnits.get( product.getShopId() ) );
                    resList.add( map );
                }
                result.put( "rows", JSON.toJSON( resList ) );
            }

        }

        result.put( "total", count );
        String page = reqmap.get( "page" );
        if (page != null) {
            result.put( "page", page );
        }
        return result;
    }

    @Override
    //报表导出
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //去掉分页
        reqParameterMap.put( "rp", Integer.MAX_VALUE + "" );
        reqParameterMap.put( "page", "1" );
        reqParameterMap.put( "orderby", "desc" );
        reqParameterMap.put( "orderfield", "id" );


        List<List<Object>> bodyList = new ArrayList<>();

        JSONObject result = selectResultByConditions( reqParameterMap );

        List<Map> ticketList = JSON.parseArray( result.get( "rows" ).toString(), Map.class );

        if (ticketList != null && ticketList.size() > 0) {
            for (Map map : ticketList) {
                List<Object> list = new ArrayList<>();
                list.add( map.get( "id" ) );
                list.add( map.get( "shop_name" ) );
                //list.add( map.get( "money" ) );
                //优惠时长
                Object money = map.get( "money" );
                Integer ticketUnit = (Integer) map.get("ticket_unit");
                if(ticketUnit==1&&!money.equals(0)){
                    list.add( map.get( "money" ) );
                }else{
                    list.add( "" );
                }
                if(ticketUnit==2&&!money.equals(0)){
                    list.add( map.get( "money" ) );
                }else{
                    list.add( "" );
                }
                if(ticketUnit==3&&!money.equals(0)){
                    list.add( map.get( "money" ) );
                }else{
                    list.add( "" );
                }
                Double umoney = Double.valueOf( map.get( "umoney" )+"" );
                if(umoney>0){
                    list.add(map.get( "umoney" ) );
                }else{
                    list.add( "" );
                }


                Long limit_day = Long.valueOf( map.get( "limit_day" ) + "" );
                String date = new SimpleDateFormat( "yyyy-MM-DD HH:mm:ss" ).format( new Date( limit_day * 1000 ) );
                list.add( date );
                Integer state = (Integer) map.get( "state" );
                if (state == 0) {
                    list.add( "未使用" );
                } else if (state == 1) {
                    list.add( "已使用" );
                } else if (state == 2) {
                    list.add( "回收作废" );
                } else {
                    list.add( state );
                }
                list.add( map.get( "car_number" ) );
                Integer type = (Integer) map.get( "type" );
                if (type == 3) {
                    list.add( "时长减免" );
                } else if (type == 5) {
                    list.add( "金额减免" );
                } else if (type == 4) {
                    list.add( "全免券" );
                } else {
                    list.add( type );
                }
                bodyList.add( list );
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


        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        System.out.println("日期====" + date);

        Long start = null;
        Long end = null;
        if (date == null || "".equals(date)) {
            start = TimeTools.getToDayBeginTime();
            end = TimeTools.getToDayBeginTime()+86399;
        } else {
            start = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(date.split("至")[0]);
            end = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(date.split("至")[1]);
        }
        System.out.println("开始时间和结束时间" + start + end);
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


            //封装searchbean  集团或城市下面所有车场
            SearchBean searchBean = new SearchBean();
            searchBean.setOperator(FieldOperator.BETWEEN);
            searchBean.setFieldName("create_time");
            searchBean.setStartValue(start);
            searchBean.setEndValue(end);

            if (supperQuery == null) {
                supperQuery = new ArrayList<>();
            }
            supperQuery.add(searchBean);


            count = commonDao.selectCountByConditions(baseQuery, supperQuery);
            if (count > 0) {

                //获得该商户的优惠单位
                ShopTb shopTb = new ShopTb();
                shopTb.setId(Long.parseLong(reqmap.get("shopid")));
                shopTb = (ShopTb)commonDao.selectObjectByConditions(shopTb);
                if (shopTb!=null){
                    logger.info("该商户的优惠单位:"+shopTb.getTicketUnit());
                }

                list = commonDao.selectListByConditions(baseQuery, supperQuery, config);
                if (list != null && !list.isEmpty()) {
                    for (TicketTb ticketTb1 : list) {
                        OrmUtil<TicketTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(ticketTb1);
                        if(shopTb!=null){
                            map.put("ticket_unit",shopTb.getTicketUnit());
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
    public Map<String, Object> createTicket(Long shop_id,Integer reduce,Integer type,Integer isAuto) {



        logger.info("后台创建优惠券:"+isAuto+"~~~~"+type+"~~~~"+reduce);

        Integer free = 0;//全免劵(张)
        Map<String, Object> rMap = new HashMap<String, Object>();
        if(shop_id == -1){
            rMap.put("result", -1);
            rMap.put("error", "商户编号>>"+shop_id+"不存在");
            return rMap;
        }
        logger.error(">>>>>>>>>>打印优惠券，优惠券类型type:"+type+",优惠时长time："+reduce+",优惠金额amount："+reduce+",商户shop_id:"+shop_id);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        ShopTb shopTb = new ShopTb();
        shopTb.setId(shop_id);
        ShopTb shopInfo =  (ShopTb) commonDao.selectObjectByConditions(shopTb);
        Integer ticket_limit = shopInfo.getTicketLimit()== null ? 0: shopInfo.getTicketLimit();
        Integer ticketfree_limit = shopInfo.getTicketfreeLimit() == null ? 0 : shopInfo.getTicketfreeLimit();
        Integer ticket_money = shopInfo.getTicketMoney() == null ? 0 : shopInfo.getTicketMoney();
        //未设置有效期,默认24小时
        Integer validite_time = shopInfo.getValiditeTime() == null ? 24: shopInfo.getValiditeTime();
        Long btime = System.currentTimeMillis()/1000;
        //截止有效时间
        Long etime =  btime + validite_time*60*60;
        //判断商户额度是否可以发劵
        if(type == 3){//优惠券-时长
            if(reduce <= 0){
                logger.error("优惠券额度必须为正数"+",商户shop_id:"+shop_id);
                rMap.put("result", -2);
                rMap.put("error", "优惠券额度必须为正数");
                return rMap;
            }
            if(ticket_limit < (reduce)){
                logger.error("优惠券额度已用完，还剩余额度"+ticket_limit+",优惠券时长time："+reduce+",商户shop_id:"+shop_id);
                rMap.put("result", -2);
                rMap.put("error", "优惠券额度不够");
                return rMap;
            }
        }else if(type == 5){//优惠券-金额
            if(reduce <= 0){
                logger.error("优惠券额度必须为正数"+",商户shop_id:"+shop_id);
                rMap.put("result", -2);
                rMap.put("error", "优惠券额度必须为正数");
                return rMap;
            }
            if(ticket_money < (reduce)){
                logger.error("优惠券额度已用完，还剩余额度"+ticket_money+",优惠券金额amount："+reduce+",商户shop_id:"+shop_id);
                rMap.put("result", -2);
                rMap.put("error", "优惠券额度不够");
                return rMap;
            }
        }else if(type==4){
            free = 1;
            reduce = 0;
            if(ticketfree_limit <= 0){
                logger.error("全免券额度已用完，还剩余额度"+ticketfree_limit+",商户shop_id:"+shop_id);
                rMap.put("result", -2);
                rMap.put("error", "全免券额度已用完");
                return rMap;
            }
        }
        List<Map<String, Object>> bathSql = new ArrayList<Map<String,Object>>();
        //取当前最大减免劵的id然后+1
        Long ticketid = qryMaxTicketId()+1;
//        Long ticketid = commonDao.selectSequence(TicketTb.class);
        String code = null;
        Long ticketids[] = new Long[]{ticketid};
        String []codes = StringUtils.getGRCode(ticketids);
        if(codes.length > 0){
            code = codes[0];
        }
        if(code != null){
            //生成一张劵
            TicketTb ticketTb = new TicketTb();
            ticketTb.setId(ticketid);
            ticketTb.setCreateTime(btime);
            ticketTb.setLimitDay(etime);
            if(type==3){
                ticketTb.setMoney(reduce);
            }else if(type==5){
                BigDecimal amountDecimal = new BigDecimal(reduce+"");
                ticketTb.setUmoney(amountDecimal);
            }
            ticketTb.setState(0);
            ticketTb.setComid(shopInfo.getComid());
            ticketTb.setType(type);
            ticketTb.setShopId(shop_id);
            Integer insertTicket = commonDao.insert(ticketTb);

            //生成code
            QrCodeTb qrCodeTb = new QrCodeTb();
            qrCodeTb.setCtime(System.currentTimeMillis()/1000);
            qrCodeTb.setType(5);
            qrCodeTb.setCode(code);
            qrCodeTb.setIsauto(isAuto);
            qrCodeTb.setTicketid(ticketid);
            qrCodeTb.setComid(shopInfo.getComid());
            Integer insertQrcode = commonDao.insert(qrCodeTb);

            //更新商户额度
            ShopTb shopTbInfo = new ShopTb();
            if(type==3){
                shopTbInfo.setTicketLimit(shopInfo.getTicketLimit()-reduce);
            }else if(type==5){
                shopTbInfo.setTicketMoney(shopInfo.getTicketMoney()-reduce);
            }else if(type==4){
                shopTbInfo.setTicketfreeLimit(shopInfo.getTicketfreeLimit()-free);
            }
            ShopTb shopConditions = new ShopTb();
            shopConditions.setId(shopInfo.getId());
            Integer updateShop = commonDao.updateByConditions(shopTbInfo,shopConditions);
            logger.error("打印优惠券结果："+insertTicket+","+insertQrcode+","+updateShop+",商户shop_id:"+shop_id);
            if(insertTicket==1 && insertQrcode==1 && updateShop==1){
                rMap.put("ticket_url", CustomDefind.getValue("TICKETURL")+code);
                rMap.put("state", 1);
                rMap.put("code", code);
                return rMap;
            }
        }
        rMap.put("result", -1);
        rMap.put("error","生成减免劵出错，用劵失败");
        return rMap;
    }

    private Long qryMaxTicketId() {
        TicketTb ticketConditions = new TicketTb();
        PageOrderConfig pageOrderConfig = new PageOrderConfig();
        pageOrderConfig.setOrderInfo("id","desc");
        List<TicketTb> ticketTbs = commonDao.selectListByConditions(ticketConditions,pageOrderConfig);
        if(ticketTbs!=null && ticketTbs.size()>0){
            return ticketTbs.get(0).getId();
        }else{
            return 0L;//没有生成过减免劵
        }
    }

    @Override
    public Map<String, Object> ifChangeCode(HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("state",0);
        String code = RequestUtil.getString(request,"code");
        logger.info("扫描二维码的code值:"+code);
        if(code!=null&&!"".equals(code)){
            QrCodeTb qrCodeTb = new QrCodeTb();
            qrCodeTb.setCode(code);
            qrCodeTb = (QrCodeTb)commonDao.selectObjectByConditions(qrCodeTb);
            if(qrCodeTb!=null){
                logger.info("qrcodeTb不为空:"+qrCodeTb);
                //是否要一直轮询查询, 确认是否自动更新二维码
                Integer isauto = qrCodeTb.getIsauto();
                if(isauto!=null&&isauto==1){//自动更新
//                    while (true){
                        Long ticketid = qrCodeTb.getTicketid();
                        TicketTb ticketTb = new TicketTb();
                        ticketTb.setId(ticketid);
                        ticketTb = (TicketTb)commonDao.selectObjectByConditions(ticketTb);
                        if(ticketTb!=null){
                            if(ticketTb.getState()==1){
                                retMap.put("state",1);
//                                break;
                            }
                        }
//                    }

                }
            }
        }

        return retMap;
    }
}
