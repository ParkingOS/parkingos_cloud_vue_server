package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.ShopMemberManageService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.CustomDefind;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class ShopMemberManageServiceImpl implements ShopMemberManageService {

    Logger logger = LoggerFactory.getLogger( ShopMemberManageServiceImpl.class );

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SaveLogService saveLogService;
    @Autowired
    private SupperSearchService<UserInfoTb> supperSearchService;

    @Override
    public String quickquery(HttpServletRequest req) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(req);

//        Integer pageNum = RequestUtil.getInteger( req, "page", 1 );
//        Integer pageSize = RequestUtil.getInteger( req, "rp", 10 );
//        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
//        JSONObject result = JSONObject.parseObject( str );

        Long shop_id = RequestUtil.getLong( req, "shop_id", -1L );
        if(shop_id==-1){
            shop_id=RequestUtil.getLong( req, "shopid", -1L );
        }

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setShopId( shop_id );
        //state状态 0为正常使用 1为删除状态
        userInfoTb.setState( 0 );

//        int count = commonDao.selectCountByConditions( userInfoTb );
//        result.put( "total", count );

        JSONObject result =  supperSearchService.supperSearch(userInfoTb,reqParameterMap);

//        if (count > 0) {
//            /**分页处理*/
//            PageOrderConfig config = new PageOrderConfig();
//            config.setPageInfo( pageNum, pageSize );
//            List<UserInfoTb> list = commonDao.selectListByConditions( userInfoTb, config );
//            List<Map<String, Object>> resList = new ArrayList<>();
//            if (list != null && !list.isEmpty()) {
//                for (UserInfoTb sb : list) {
//                    OrmUtil<UserInfoTb> otm = new OrmUtil<>();
//                    Map<String, Object> map = otm.pojoToMap( sb );
//                    resList.add( map );
//                }
//                result.put( "rows", JSON.toJSON( resList ) );
//            }
//            result.put( "total", count );
//            result.put( "page", pageNum );
//        }
        return result.toJSONString();

    }

    @Override
    public String editpass(HttpServletRequest request) {
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String id = RequestUtil.processParams( request, "id" );
        String newPass = RequestUtil.processParams( request, "newpass" );
        String confirmPass = RequestUtil.processParams( request, "confirmpass" );

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId( Long.valueOf( id ) );
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
            if(update==1){
                ParkLogTb parkLogTb = new ParkLogTb();
                parkLogTb.setOperateUser(nickname);
                parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
                parkLogTb.setOperateType(2);
                parkLogTb.setContent(uin+"("+nickname+")"+"修改了员工密码"+id);
                parkLogTb.setType("shop");
                parkLogTb.setParkId(comid);
                saveLogService.saveLog(parkLogTb);
            }

        }
        return "{\"state\":" + update + "}";
    }

    @Override
    public String getRoleByConditions(Long roleId, Long shopId) {
        String result = "[]";
        List list = null;
        String sql =  "select id as value_no,role_name as value_name from user_role_tb where oid =(select id from zld_orgtype_tb WHERE NAME like '商户%' AND state=0) and state =0 and (adminid in(SELECT id from user_info_tb where state=0 and shop_id = "+shopId+" and auth_flag>0) or adminid=0)";
        if(shopId>0&&roleId>0){
//            list = commonDao.getObjectBySql("select id as value_no,role_name as value_name from user_role_tb where adminid" +
//                    " in(select id from user_info_tb where shop_id="+shopId+" and role_id="+roleId+") and state = 0 ");
            list = commonDao.getObjectBySql(sql);
        }
        if(list!=null&&list.size()>0){
           result = StringUtils.createJson(list);
        }
        return result;
    }

    @Override
    public String getShopUsers(Long shopId) {
        String result = "[]";
        String sql =  "select id as value_no,nickname as value_name from user_info_tb where shop_id="+shopId+"and state =0";
        List list =commonDao.getObjectBySql(sql);
        if(list!=null&&list.size()>0){
            result = StringUtils.createJson(list);
        }
        return result;
    }


    @Override
    public String create(HttpServletRequest request) {


        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        String nickname = StringUtils.decodeUTF8(StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname")));

        String phone = RequestUtil.processParams( request, "phone" );
        String mobile = RequestUtil.processParams( request, "mobile" );
        Long role = RequestUtil.getLong( request, "auth_flag", 15L );//14:负责人 15：工作人员
        Long shop_id = RequestUtil.getLong( request, "shop_id", -1L );
        if(shop_id==-1){
            shop_id=RequestUtil.getLong( request, "shopid", -1L );
        }


        Long sex =  RequestUtil.getLong(request,"sex",-1L);
        Long roleId = RequestUtil.getLong(request,"role_id",-1L);
        if (nickname.equals( "" )) nickname = null;
        if (phone.equals( "" )) phone = null;
        if (mobile.equals( "" )) mobile = null;
        Long time = System.currentTimeMillis() / 1000;

        //用户id 判断是做添加还是做修改
        Long userid = RequestUtil.getLong( request, "userId", -1L );
        if(userid==-1){
            userid=RequestUtil.getLong( request, "id", -1L );
        }

        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setComid( comid );
        userInfoTb.setNickname( nickname );
        userInfoTb.setShopId( shop_id );
        userInfoTb.setPhone( phone );
        userInfoTb.setMobile( mobile );
        userInfoTb.setAuthFlag( role );
        if(roleId>-1){
            userInfoTb.setRoleId(roleId);
        }
        userInfoTb.setSex(sex);

        int count = 0;


        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setOperateUser(nickname1);
        parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
        parkLogTb.setType("shop");
        parkLogTb.setParkId(comid);


        if (userid == -1) {
            //添加操作
            Long squen = commonDao.selectSequence( UserInfoTb.class );
            String strid = CustomDefind.getValue("UNIONVALUE")+squen;
            userInfoTb.setStrid(strid);
            userInfoTb.setPassword(strid);
//            userInfoTb.setStrid( "test" + squen );
//            userInfoTb.setPassword( "test" + squen );
            userInfoTb.setRegTime( time );
            userInfoTb.setId( squen );

            if(roleId==-1){
                UserRoleTb userRoleTb = new UserRoleTb();
                userRoleTb.setOid(Long.parseLong(CustomDefind.getValue("SHOPOID")));
                userRoleTb.setType(0);
                userRoleTb = (UserRoleTb)commonDao.selectObjectByConditions(userRoleTb);
                if(userRoleTb!=null){
                    userInfoTb.setRoleId(userRoleTb.getId());
                }
            }

            String md5Pass = "";
            if(md5Pass.length()<32){
                //md5密码 ，生成规则：原密码md5后，加上'zldtingchebao201410092009'再次md5
//                md5Pass = StringUtils.MD5("test" + squen);
                md5Pass = StringUtils.MD5(strid);
                md5Pass = StringUtils.MD5(md5Pass +"zldtingchebao201410092009");
            }
            userInfoTb.setMd5pass( md5Pass );
            count = commonDao.insert( userInfoTb );

            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname1+")"+"增加了商户员工"+strid);

        } else {
            //修改操作
            userInfoTb.setId( userid );
            count = commonDao.updateByPrimaryKey( userInfoTb );

            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname1+")"+"修改了商户员工"+userid);
        }
        saveLogService.saveLog(parkLogTb);
        return "{\"state\":" + count + "}";
    }

    @Override
    public String delete(HttpServletRequest req) {

        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String nickname1 = StringUtils.decodeUTF8(RequestUtil.getString(req,"nickname1"));
        Long uin = RequestUtil.getLong(req, "loginuin", -1L);

        Long id = RequestUtil.getLong( req, "id", -1L );
        int delete = 0;
        if (id > 0) {
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setId( id );
            userInfoTb.setState( 1 );
            //删除操作将state状态修改为1
            delete = commonDao.updateByPrimaryKey( userInfoTb );
            if(delete==1) {
                ParkLogTb parkLogTb = new ParkLogTb();
                parkLogTb.setOperateUser(nickname1);
                parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
                parkLogTb.setOperateType(3);
                parkLogTb.setContent(uin + "(" + nickname1 + ")" + "删除了商户员工" + id);
                parkLogTb.setType("shop");
                parkLogTb.setParkId(comid);
                saveLogService.saveLog(parkLogTb);
            }
        }
        return "{\"state\":" + delete + "}";
    }
}
