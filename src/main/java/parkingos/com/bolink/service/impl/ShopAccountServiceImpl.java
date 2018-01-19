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

    Logger logger = Logger.getLogger( OrderServiceImpl.class );

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

        Map searchMap = supperSearchService.getBaseSearch( new ShopAccountTb(), reqmap );


        if (searchMap != null && !searchMap.isEmpty()) {
            ShopAccountTb baseQuery = (ShopAccountTb) searchMap.get( "base" );
            //绑定登陆停车场
            if(baseQuery==null){
                baseQuery=new ShopAccountTb();
            }
            baseQuery.setParkId( Long.valueOf( reqmap.get( "comid" ) ));
            //流水类型
            if (reqmap.get( "operate_type" ) != null && !reqmap.get( "operate_type" ).equals( "" )) {
                Integer operate_type = Integer.valueOf( reqmap.get( "operSystem.outte_type" ) );
                if (operate_type != 0) {
                    baseQuery.setOperateType( operate_type );
                }
            }

            List<SearchBean> supperQuery = null;
            if (searchMap.containsKey( "supper" )) {
                supperQuery = (List<SearchBean>) searchMap.get( "supper" );
            }

            //操作人名模糊查询
            String nickname = reqmap.get( "nickname" );
            if (nickname != null && !nickname.equals( "" )) {
                List<SearchBean> nameList = new ArrayList<>();
                SearchBean searchBean = new SearchBean();
                searchBean.setFieldName( "nickname" );
                searchBean.setOperator( FieldOperator.LIKE );
                searchBean.setBasicValue( "%" + nickname + "%" );
                nameList.add( searchBean );
                List<UserInfoTb> searchUser = commonDao.selectListByConditions( new UserInfoTb(), nameList );

                if (searchUser != null && !searchUser.isEmpty()) {
                    searchBean.setOperator( FieldOperator.CONTAINS );
                    searchBean.setFieldName( "operator" );
                    List<Long> idList = new ArrayList<Long>();
                    for (UserInfoTb u : searchUser) {
                        idList.add( u.getId() );
                    }
                    searchBean.setBasicValue( idList );
                    if (supperQuery == null) {
                        supperQuery = new ArrayList<>();
                        supperQuery.add( searchBean );
                    } else {
                        supperQuery.add( searchBean );
                    }
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

                //查询名称
                Map<Long, String> nameMap = new HashMap<>();
                List<Long> idList = new ArrayList<>();

                for (ShopAccountTb shopAccountTb : list) {
                    if (!idList.contains( shopAccountTb.getOperator() ))
                        idList.add( shopAccountTb.getOperator() );
                }

                SearchBean searchBean = new SearchBean();
                searchBean.setFieldName( "id" );
                searchBean.setOperator( FieldOperator.CONTAINS );
                searchBean.setBasicValue( idList );
                List<SearchBean> searchBeans = new ArrayList<>();
                searchBeans.add( searchBean );
                UserInfoTb userInfo = new UserInfoTb();
                userInfo.setComid( Long.valueOf( reqmap.get( "comid" ) ) );

                List<UserInfoTb> users = commonDao.selectListByConditions( userInfo, searchBeans );
                if (users != null && !users.isEmpty()) {
                    for (UserInfoTb u : users) {
                        nameMap.put( u.getId(), u.getNickname() );
                    }
                }

                if (list != null && !list.isEmpty()) {
                    for (ShopAccountTb product : list) {
                        OrmUtil<ShopAccountTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap( product );
                        map.put( "nickname", nameMap.get( product.getOperator() ) );
                        resList.add( map );
                    }
                    result.put( "rows", JSON.toJSON( resList ) );
                }
            }
        }
        result.put( "total", count );
        result.put( "page", Integer.parseInt( reqmap.get( "page" ) ) );
        logger.info( result );
        return result;
    }
}
