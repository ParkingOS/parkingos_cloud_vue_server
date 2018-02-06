package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.service.ShopMemberManageService;
import parkingos.com.bolink.utils.CustomDefind;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShopMemberManageServiceImpl implements ShopMemberManageService {

    Logger logger = Logger.getLogger( ShopMemberManageServiceImpl.class );

    @Autowired
    private CommonDao commonDao;

    @Override
    public String quickquery(HttpServletRequest req) {

        Integer pageNum = RequestUtil.getInteger( req, "page", 1 );
        Integer pageSize = RequestUtil.getInteger( req, "rp", 10 );
        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject( str );

        Long shop_id = RequestUtil.getLong( req, "shop_id", -1L );

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
    public String editpass(HttpServletRequest request) {

        String uin = RequestUtil.processParams( request, "id" );
        String newPass = RequestUtil.processParams( request, "newpass" );
        String confirmPass = RequestUtil.processParams( request, "confirmpass" );

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId( Long.valueOf( uin ) );
        userInfoTb.setPassword( newPass );

        int update = 0;
        if (newPass.length() > 5 && newPass.equals( confirmPass )) {
            String md5Pass = "";
            if(md5Pass.length()<32){
                //md5密码 ，生成规则：原密码md5后，加上'zldtingchebao201410092009'再次md5
                md5Pass = StringUtils.MD5(newPass);
                md5Pass = StringUtils.MD5(md5Pass +"zldtingchebao201410092009");
            }
            userInfoTb.setMd5pass( md5Pass );
            update = commonDao.updateByPrimaryKey( userInfoTb );
        }
        return "{\"state\":" + update + "}";
    }


    @Override
    public String create(HttpServletRequest request) {

        String nickname = RequestUtil.processParams( request, "nickname" );
        try {
            nickname = new String( nickname.getBytes( "ISO-8859-1" ), "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String phone = RequestUtil.processParams( request, "phone" );
        String mobile = RequestUtil.processParams( request, "mobile" );
        Long comid = Long.valueOf( RequestUtil.processParams( request, "comid" ) );
        Long role = RequestUtil.getLong( request, "auth_flag", 15L );//14:负责人 15：工作人员
        Long shop_id = RequestUtil.getLong( request, "shop_id", -1L );
        if (nickname.equals( "" )) nickname = null;
        if (phone.equals( "" )) phone = null;
        if (mobile.equals( "" )) mobile = null;
        Long time = System.currentTimeMillis() / 1000;

        //用户id 判断是做添加还是做修改
        Long userid = RequestUtil.getLong( request, "userId", -1L );

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setComid( comid );
        userInfoTb.setNickname( nickname );
        userInfoTb.setShopId( shop_id );
        userInfoTb.setPhone( phone );
        userInfoTb.setMobile( mobile );
        userInfoTb.setAuthFlag( role );

        int count = 0;
        if (userid == -1) {
            //添加操作
            Long squen = commonDao.selectSequence( UserInfoTb.class );
            userInfoTb.setStrid( "test" + squen );
            userInfoTb.setPassword( squen+"" );
            userInfoTb.setRegTime( time );
            userInfoTb.setId( squen );
            String md5Pass = "";
            if(md5Pass.length()<32){
                //md5密码 ，生成规则：原密码md5后，加上'zldtingchebao201410092009'再次md5
                md5Pass = StringUtils.MD5(squen+"");
                md5Pass = StringUtils.MD5(md5Pass +"zldtingchebao201410092009");
            }
            userInfoTb.setMd5pass( md5Pass );
            count = commonDao.insert( userInfoTb );
        } else {
            //修改操作
            userInfoTb.setId( userid );
            count = commonDao.updateByPrimaryKey( userInfoTb );
        }
        return "{\"state\":" + count + "}";
    }

    @Override
    public String delete(HttpServletRequest req) {
        Long id = RequestUtil.getLong( req, "id", -1L );
        int delete = 0;
        if (id > 0) {
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setId( id );
            userInfoTb.setState( 1 );
            //删除操作将state状态修改为1
            delete = commonDao.updateByPrimaryKey( userInfoTb );
        }
        return "{\"state\":" + delete + "}";
    }
}
