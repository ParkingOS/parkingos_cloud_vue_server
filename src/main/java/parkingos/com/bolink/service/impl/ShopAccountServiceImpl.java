package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.FieldOperator;
import parkingos.com.bolink.models.ShopAccountTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.ShopAcccountService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.OrmUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopAccountServiceImpl implements ShopAcccountService {

    Logger logger = Logger.getLogger( ShopAccountServiceImpl.class );

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ShopAccountTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject( str );

        int count = 0;
        List<ShopAccountTb> list = null;
        List<Map<String, Object>> resList = new ArrayList<>();

        ShopAccountTb shopAccountTb = new ShopAccountTb();
        //登陆停车场
        shopAccountTb.setParkId( Long.valueOf( reqmap.get( "comid" ) ) );
        //流水类型
        if (reqmap.get( "operate_type" ) != null && !reqmap.get( "operate_type" ).equals( "" )) {
            Integer operate_type = Integer.valueOf( reqmap.get( "operate_type" ) );
            if (operate_type != 0) {
                shopAccountTb.setOperateType( operate_type );
            }
        }

        Map searchMap = supperSearchService.getBaseSearch( shopAccountTb, reqmap );
        List<SearchBean> supperQuery = null;
        ShopAccountTb baseQuery = null;
        PageOrderConfig config = null;

        if (searchMap != null && !searchMap.isEmpty()) {
            baseQuery = (ShopAccountTb) searchMap.get( "base" );
            if (searchMap.containsKey( "supper" )) {
                supperQuery = (List<SearchBean>) searchMap.get( "supper" );
            }
            if (searchMap.containsKey( "config" ))
                config = (PageOrderConfig) searchMap.get( "config" );
        }

        List<SearchBean> nameList = new ArrayList<>();
        SearchBean searchBean = new SearchBean();
        nameList.add( searchBean );
        Map<Long, String> names = null;
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setComid( Long.valueOf( reqmap.get( "comid" ) ) );
        //操作人名模糊查询
        String nickname = reqmap.get( "nickname" );

        if (nickname != null && !nickname.equals( "" )) {
            searchBean.setFieldName( "nickname" );
            searchBean.setOperator( FieldOperator.LIKE );
            searchBean.setBasicValue( nickname );

            List<UserInfoTb> searchUsers = commonDao.selectListByConditions( userInfoTb, nameList );

            if (searchUsers != null && !searchUsers.isEmpty()) {
                searchBean.setOperator( FieldOperator.CONTAINS );
                searchBean.setFieldName( "operator" );
                List<Long> idList = new ArrayList<Long>();
                names = new HashMap<>();
                for (UserInfoTb u : searchUsers) {
                    idList.add( u.getId() );
                    names.put( u.getId(), u.getNickname() );
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

            if (names == null) {
                names = new HashMap<>();
                //查询名称
                List<Long> idList = new ArrayList<>();

                for (ShopAccountTb s : list) {
                    if (!idList.contains( s.getOperator() ))
                        idList.add( s.getOperator() );
                }

                searchBean.setFieldName( "id" );
                searchBean.setOperator( FieldOperator.CONTAINS );
                searchBean.setBasicValue( idList );

                List<UserInfoTb> users = commonDao.selectListByConditions( userInfoTb, nameList );
                if (users != null && !users.isEmpty()) {
                    for (UserInfoTb u : users) {
                        names.put( u.getId(), u.getNickname() );
                    }
                }
            }

            if (list != null && !list.isEmpty()) {
                for (ShopAccountTb product : list) {
                    OrmUtil<ShopAccountTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap( product );
                    map.put( "nickname", names.get( product.getOperator() ) );
                    resList.add( map );
                }
                result.put( "rows", JSON.toJSON( resList ) );
            }

        }
        result.put( "total", count );
        result.put( "page", Integer.parseInt( reqmap.get( "page" ) ) );
        logger.info( result );
        return result;
    }
}
