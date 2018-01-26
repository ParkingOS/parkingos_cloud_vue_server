package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.models.TicketTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.TicketService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    Logger logger = Logger.getLogger( OrderServiceImpl.class );

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<TicketTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
         String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject( str );

        int count = 0;
        List<TicketTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<>();

        TicketTb ticketTb = new TicketTb();
       //停车场编号
        ticketTb.setComid( Long.valueOf( reqmap.get( "comid" ) ) );
        //绑定优惠类型,0为默认查询全部，3为时长减免，5为金额减免
        Integer type = 0;
        String strType = reqmap.get( "type" );
        if (strType != null && !strType.equals( "" )) {
            type = Integer.valueOf( strType );
        }
        if (type == 1) {
            ticketTb.setType( 3 );
        }else if(type==2){
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
        if (searchMap != null && !searchMap.isEmpty()) {
            List<SearchBean> supperQuery = null;
            TicketTb baseQuery = (TicketTb) searchMap.get( "base" );
            if (searchMap.containsKey( "supper" ))
                supperQuery = (List<SearchBean>) searchMap.get( "supper" );

            //shop_name模糊查询
            String shopName = reqmap.get( "shop_name" );
            if (shopName != null && !shopName.equals( "" )) {
                //商户名称模糊查询
                ShopTb shopTb = new ShopTb();
                shopTb.setComid( Long.valueOf( reqmap.get( "comid" ) ) );
                SearchBean searchBean = new SearchBean();
                searchBean.setFieldName( "name" );
                searchBean.setOperator( FieldOperator.LIKE );
                searchBean.setBasicValue(  shopName );
                List<SearchBean> searchList = new ArrayList<>();
                searchList.add( searchBean );
                List<ShopTb> shopList = commonDao.selectListByConditions( shopTb, searchList );
                //有查询结果
                if (shopList != null && !shopList.isEmpty()) {
                    searchBean.setOperator( FieldOperator.CONTAINS );
                    searchBean.setFieldName( "shop_id" );
                    List<Long> idList = new ArrayList<Long>();
                    for (ShopTb s : shopList) {
                        idList.add( s.getId() );
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

            PageOrderConfig config = null;
            if (searchMap.containsKey( "config" ))
                config = (PageOrderConfig) searchMap.get( "config" );
            count = commonDao.selectCountByConditions( baseQuery, supperQuery );
            if (count > 0) {
                list = commonDao.selectListByConditions( baseQuery, supperQuery, config );

                if (list != null && !list.isEmpty()) {

                    //查询停车场名称
                    Map<Long, String> nameMap = new HashMap<>();
                    List<Long> idList = new ArrayList<>();

                    for (TicketTb t : list) {
                        if (!idList.contains( t.getShopId() ))
                            idList.add( t.getShopId() );
                    }

                    SearchBean searchBean = new SearchBean();
                    searchBean.setFieldName( "id" );
                    searchBean.setOperator( FieldOperator.CONTAINS );
                    searchBean.setBasicValue( idList );
                    ArrayList<SearchBean> searchBeans = new ArrayList<>();
                    searchBeans.add( searchBean );
                    ShopTb shopTb = new ShopTb();
                    shopTb.setComid( Long.valueOf( reqmap.get( "comid" ) ) );

                    List<ShopTb> shopTbs = commonDao.selectListByConditions( shopTb, searchBeans );
                    if (shopTbs != null && !shopTbs.isEmpty()) {
                        for (ShopTb s : shopTbs) {
                            nameMap.put( s.getId(), s.getName() );
                        }
                    }

                    for (TicketTb product : list) {
                        OrmUtil<TicketTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap( product );
                        map.put( "shop_name", nameMap.get( product.getShopId() ) );
                        resList.add( map );
                    }
                    result.put( "rows", JSON.toJSON( resList ) );
                }
            }
        }

        result.put( "total", count );
        result.put( "page", Integer.parseInt( reqmap.get( "page" ) ) );
        return result;
    }
}
