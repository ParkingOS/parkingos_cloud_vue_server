package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.UserInfo;
import com.lycheepay.gateway.client.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.service.ShopMemberManageService;
import parkingos.com.bolink.utils.CustomDefind;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShopMemberManageServiceImpl implements ShopMemberManageService{
    @Autowired
    private CommonDao commonDao;

    @Override
    public String quickquery(HttpServletRequest req, HttpServletResponse resp) {

        /*String fieldsstr = RequestUtil.processParams(request, "fieldsstr");
        Integer pageNum = RequestUtil.getInteger(request, "page", 1);
        Integer pageSize = RequestUtil.getInteger(request, "rp", 20);
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        List<Object> params = new ArrayList<Object>();
        String sql = "select * from user_info_tb where state=? and shop_id=? ";
        String sqlcount = "select count(*) from user_info_tb where state=? and shop_id=? ";*/

        Integer pageNum = RequestUtil.getInteger( req, "page", 1 );
        Integer pageSize = RequestUtil.getInteger( req, "rp", 10 );
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject( str );

        Long shop_id = RequestUtil.getLong( req,"shop_id" ,-1L);

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setShopId( shop_id );
        //state状态 0为正常使用 1为删除状态
        userInfoTb.setState( 0 );

        int count = commonDao.selectCountByConditions( userInfoTb );
        result.put( "total", count );

        if (count > 0) {
            /**分页处理*/
            PageOrderConfig config = new PageOrderConfig();
            config.setPageInfo( pageNum, pageSize );
            List<UserInfoTb> list = commonDao.selectListByConditions( userInfoTb, config );
            List<Map<String, Object>> resList = new ArrayList<>();
            if (list != null && !list.isEmpty()) {
                for (UserInfoTb sb : list) {
                    OrmUtil<UserInfoTb> otm = new OrmUtil<>();
                    Map<String, Object> map = otm.pojoToMap( sb );
                    resList.add( map );
                }
                result.put( "rows", JSON.toJSON( resList ) );
            }
            result.put( "total", count );
            result.put( "page", pageNum );
        }
        return result.toJSONString();

    }

    @Override
    public String editpass(HttpServletRequest request, HttpServletResponse resp) {

        String uin =RequestUtil.processParams(request, "id");
        String newPass = RequestUtil.processParams(request, "newpass");
        String confirmPass = RequestUtil.processParams(request, "confirmpass");

        UserInfoTb userInfoTb=new UserInfoTb();
        userInfoTb.setId( Long.valueOf( uin ) );
        userInfoTb.setPassword( newPass );

        int update = 0;
        if(newPass.length()>5&&newPass.equals(confirmPass)){
           update =commonDao.updateByPrimaryKey( userInfoTb );
        }
        return "{\"state\":"+update+"}";
    }


    @Override
    public String create(HttpServletRequest request, HttpServletResponse resp) {

        String strid = "";
        String nickname =RequestUtil.processParams( request,"nickname" );
        String phone =RequestUtil.processParams(request, "phone");
        String mobile =RequestUtil.processParams(request, "mobile");
        Long comid = Long.valueOf( RequestUtil.processParams(request, "comid") );
        Long role =RequestUtil.getLong(request, "auth_flag", 15L);//14:负责人 15：工作人员
        Long shop_id = RequestUtil.getLong( request,"shop_id",-1L );
        if(nickname.equals("")) nickname=null;
        if(phone.equals("")) phone=null;
        if(mobile.equals("")) mobile=null;
        Long time = System.currentTimeMillis()/1000;

        //用户id 判断是做添加还是做修改
        Long userid = RequestUtil.getLong( request,"userId",-1L );

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setComid( comid );
        userInfoTb.setNickname( nickname );
        userInfoTb.setShopId( shop_id );
        userInfoTb.setPhone( phone );
        userInfoTb.setMobile( mobile );
        userInfoTb.setAuthFlag( role );
        userInfoTb.setPassword( "123456" );
        int count = 0;
        if(userid==-1){
            //添加操作
            Long squen = commonDao.selectSequence( UserInfoTb.class );
            userInfoTb.setStrid( "test"+squen );
            userInfoTb.setRegTime( time );
            count = commonDao.insert( userInfoTb );
        }else{
            //修改操作
            userInfoTb.setId( userid );
            count = commonDao.updateByPrimaryKey( userInfoTb );
        }


        return "{\"state\":"+count+"}";
    }

    @Override
    public String delete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = RequestUtil.getLong( req, "id", -1L );
        int delete = 0;
        if (id > 0) {
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setId( id );
            userInfoTb.setState( 1 );
            //删除操作将state状态修改为1
            delete = commonDao.updateByPrimaryKey( userInfoTb );
        }

        return "{\"state\":"+delete+"}";
    }
}
