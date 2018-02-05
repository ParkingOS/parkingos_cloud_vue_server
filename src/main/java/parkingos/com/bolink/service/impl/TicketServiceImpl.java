package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.models.TicketTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.TicketService;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;

import javax.sql.rowset.serial.SerialArray;
import java.lang.reflect.Field;
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

                //查询停车场名称
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
                        }
                    }
                }

                for (TicketTb product : list) {
                    OrmUtil<TicketTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap( product );
                    map.put( "shop_name", shopNames.get( product.getShopId() ) );
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
                list.add( map.get( "money" ) );
                list.add( map.get( "umoney" ) );
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
}
