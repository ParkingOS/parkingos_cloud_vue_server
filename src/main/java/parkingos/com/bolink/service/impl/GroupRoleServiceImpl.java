package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.AuthRoleTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserRoleTb;
import parkingos.com.bolink.service.GroupRoleService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.*;

@Service
public class GroupRoleServiceImpl implements GroupRoleService {

    Logger logger = Logger.getLogger(GroupRoleServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<UserRoleTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {


//        String str = "{\"total\":12,\"page\":1,\"money\":0.0,\"rows\":[]}";
//        JSONObject result = JSONObject.parseObject(str);

//        int count =0;
//        List<UserRoleTb> list =null;
//        List<Map<String, Object>> resList =new ArrayList<>();

        Long uin = Long.parseLong(reqmap.get("loginuin"));
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setState(0);
        //这一集团下的角色
        userRoleTb.setAdminid(uin);

        JSONObject result = supperSearchService.supperSearch(userRoleTb,reqmap);

//        Map searchMap = supperSearchService.getBaseSearch(userRoleTb,reqmap);
//        logger.info(searchMap);
//        if(searchMap!=null&&!searchMap.isEmpty()){
//            UserRoleTb baseQuery =(UserRoleTb)searchMap.get("base");
//            List<SearchBean> supperQuery = null;
//            if(searchMap.containsKey("supper"))
//                supperQuery = (List<SearchBean>)searchMap.get("supper");
//            PageOrderConfig config = null;
//            if(searchMap.containsKey("config"))
//                config = (PageOrderConfig)searchMap.get("config");
//
//
//            String sql = "select id from user_info_tb where groupid= "+reqmap.get("groupid")+" and role_id="+reqmap.get("roleid");
//            List<Map> list1 = commonDao.getObjectBySql(sql);
//            List idList =new ArrayList();
//            for(Map map:list1){
//                idList.add(Long.parseLong(map.get("id")+""));
//            }
//
//            //封装searchbean  要求同一级账号登录员工可以看到相同的内容
//            SearchBean searchBean = new SearchBean();
//            searchBean.setOperator(FieldOperator.CONTAINS);
//            searchBean.setFieldName("adminid");
//            searchBean.setBasicValue(idList);
//
//            if (supperQuery == null) {
//                supperQuery = new ArrayList<>();
//            }
//            supperQuery.add( searchBean );
//
//            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
//            if(count>0){
//                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
//                if (list != null && !list.isEmpty()) {
//                    for (UserRoleTb product : list) {
//                        OrmUtil<UserRoleTb> otm = new OrmUtil<>();
//                        Map<String, Object> map = otm.pojoToMap(product);
//                        resList.add(map);
//                    }
//                    result.put("rows", JSON.toJSON(resList));
//                }
//            }
//        }
//        result.put("total",count);
//        result.put("page",Integer.parseInt(reqmap.get("page")));

        return result;
    }

    @Override
    public JSONObject addRole(UserRoleTb userRoleTb, Integer func) {
        String str = "{\"state\":0,\"msg\":\"保存失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int is_inspect = 0;
        int is_collector = 0;
        int is_opencard = 0;
        switch (func) {
            case 1:
                is_collector = 1;
                break;
            case 2:
                is_inspect = 1;
                break;
            case 3:
                is_opencard = 1;
                break;
            default:
                break;
        }
        userRoleTb.setIsCollector(is_collector);
        userRoleTb.setIsInspect(is_inspect);
        userRoleTb.setIsOpencard(is_opencard);
        logger.error("=======>>>>>" + userRoleTb);
        int ret = commonDao.insert(userRoleTb);
        if (ret == 1) {
            result.put("msg", "保存成功");
        }
        result.put("state", ret);
        return result;
    }

    @Override
    public JSONObject deleteRole(UserRoleTb userRoleTb) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.updateByPrimaryKey(userRoleTb);
        if (ret == 1) {
            result.put("state", 1);
            result.put("msg", "删除成功");
        }
        return result;
    }

    @Override
    public JSONObject updateRole(UserRoleTb userRoleTb, Integer func) {
        logger.error("=====>>>开始更新角色+" + userRoleTb + "===" + func);
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        Long auth_flag = -1L;
        int is_inspect = 0;
        int is_collector = 0;
        int is_opencard = 0;
        switch (func) {
            case 1:
                auth_flag = 2L;
                is_collector = 1;
                break;
            case 2:
                auth_flag = 16L;
                is_inspect = 1;
                break;
            case 3:
                auth_flag = 17L;
                is_opencard = 1;
                break;
            default:
                break;
        }
        userRoleTb.setIsCollector(is_collector);
        userRoleTb.setIsInspect(is_inspect);
        userRoleTb.setIsOpencard(is_opencard);

        int ret = commonDao.updateByPrimaryKey(userRoleTb);
        logger.error("=====>>>更新角色" + ret);
        if (ret == 1) {
            UserInfoTb fields = new UserInfoTb();
            fields.setAuthFlag(auth_flag);
            UserInfoTb conditions = new UserInfoTb();
            conditions.setRoleId(userRoleTb.getId());
            int update = commonDao.updateByConditions(fields, conditions);
            logger.error("=====>>>>>>更新user" + update);
            result.put("state", 1);
            result.put("msg", "修改成功");
        }
        return result;
    }

    @Override
    public String getAuth(Long loginRoleId, Long id) {

        //查询所有权限
        String allsql = "select id,pid,nname as name,sub_auth,sub_auth as sub_auth_name from auth_tb where state =0 order by id";
        List<Map<String, Object>> allAuthsList = commonDao.getObjectBySql(allsql);
        //查父权限
        String parentsql = "select auth_id,sub_auth from auth_role_tb where role_id =" + loginRoleId;
        List<Map<String, Object>> parentAuthsList = commonDao.getObjectBySql(parentsql);
        //查自己权限
        String ownsql = "select auth_id,ar.sub_auth,pid from auth_role_tb ar left join auth_tb at on ar.auth_id= at.id where role_id =" + id;
        List<Map<String, Object>> ownAuthsList = commonDao.getObjectBySql(ownsql);
        //查角色名称
        UserRoleTb userRoleTb = new UserRoleTb();
        userRoleTb.setId(id);
        userRoleTb = (UserRoleTb) commonDao.selectObjectByConditions(userRoleTb);

        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : parentAuthsList) {
            Long autId = (Long) map.get("auth_id");
            String subAuth = (String) map.get("sub_auth");
            for (Map<String, Object> amap : allAuthsList) {
                Long aid = (Long) amap.get("id");

                String sub_auth = (String) amap.get("sub_auth");
                if (autId.equals(aid)) {
                    if (subAuth != null && !subAuth.equals("")) {
                        String s1[] = subAuth.split(",");
                        String s2[] = sub_auth.split(",");
                        String newSubAuth = "";
                        String newSUbAuthid = "";
                        if (s2.length > 0) {
                            for (String index : s1) {
                                if (!newSubAuth.equals("")) {
                                    newSubAuth += ",";
                                    newSUbAuthid += ",";
                                }
                                Integer in = Integer.valueOf(index);
                                if (in > s2.length - 1) {
//										if(s2.length>1)
//											newSubAuth +=index;
                                } else {
                                    newSubAuth += s2[in];
                                    newSUbAuthid += in;
                                }
                            }
                        }
                        amap.put("sub_auth_name", newSubAuth);
                        amap.put("sub_auth", newSUbAuthid);
                    }
                    subList.add(amap);
                    break;
                }
            }
        }
        Collections.sort(subList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1,
                               Map<String, Object> o2) {
                Long id1 = (Long) o1.get("id");
                Long id2 = (Long) o2.get("id");
                Long index = id1 - id2;
                return index.intValue();
            }
        });

        //返回所有权限的map  和名称
        Map allMap = new HashMap();

        //所有权限
        List list = new ArrayList();
        for (Map newMap : subList) {
            //总菜单父级    系统管理====车型管理=====车型设定====  最多三层
            if ("0".equals(newMap.get("pid").toString())) {//系统管理   月卡会员
                Map map1 = new HashMap();
                List newList = new ArrayList();
                map1.put("subname", newMap.get("name"));
                map1.put("ischeck", false);
                map1.put("pid", newMap.get("id"));
                for (Map ownMap : ownAuthsList) {
                    if (ownMap.get("auth_id").equals(newMap.get("id"))) {
                        map1.put("ischeck", true);
                        break;
                    }
                }
                //寻找父级下面是否有子级
                for (Map newMap2 : subList) {
                    if (!newMap.get("id").equals(newMap2.get("pid"))) {

                    } else {//车型管理   总菜单下有子级  //xufeijilu
                        Map map2 = new HashMap();
                        map2.put("subname", newMap2.get("name"));
                        map2.put("ischeck", false);
                        map2.put("sid", newMap2.get("id"));
                        for (Map ownMap : ownAuthsList) {
                            if (ownMap.get("auth_id").equals(newMap2.get("id"))) {
                                map2.put("ischeck", true);
                                break;
                            }
                        }
                        List newList2 = new ArrayList();
                        //判断 下面是不是还有子级
                        int i = 0;
                        for (Map newMap3 : subList) {
                            Map map3 = new HashMap();
                            if (!newMap2.get("id").equals(newMap3.get("pid"))) {
                                i++;
                            } else {//车型设定  也有可能没有

                                map3.put("subname", newMap3.get("name"));
                                map3.put("ischeck", false);
                                map3.put("ssid", newMap3.get("id"));
                                for (Map ownMap : ownAuthsList) {
                                    if (ownMap.get("auth_id").equals(newMap3.get("id"))) {
                                        map3.put("ischeck", true);
                                        break;
                                    }
                                }
                                String[] strArr = newMap3.get("sub_auth_name").toString().split(",");
                                String[] stridArr = newMap3.get("sub_auth").toString().split(",");
                                List newList3 = new ArrayList();
                                if (strArr != null && strArr.length > 0) {
                                    for (int j = 0; j < strArr.length; j++) {
                                        Map map4 = new HashMap();
                                        map4.put("subname", strArr[j]);
                                        map4.put("subid", stridArr[j]);
                                        map4.put("ischeck", false);
                                        int mm=0;
                                        for (Map ownMap : ownAuthsList) {
                                            if (ownMap.get("auth_id").equals(newMap3.get("id"))) {
                                                if (ownMap.get("sub_auth") != null) {
                                                    String ownSub = ownMap.get("sub_auth").toString();
                                                    if (ownSub != null) {
                                                        String[] ownStrArr = ownSub.split(",");
                                                        for (String num : ownStrArr) {
                                                            if (num.equals(stridArr[j])) {
                                                                map4.put("ischeck", true);
                                                            }
                                                        }
                                                    }
                                                }
                                            }else{
                                                mm++;
                                            }
                                        }
                                        if (!"".equals(map4.get("subid"))){
                                            newList3.add(map4);
                                        }
                                    }
                                }
                                map3.put("subpermission", newList3);
                                newList2.add(map3);
                            }
                        }
                        //如果这成立  证明已经没有子级了
                        if (i == subList.size()) {
                            String[] strArr2 = newMap2.get("sub_auth_name").toString().split(",");
                            String[] stridArr2 = newMap2.get("sub_auth").toString().split(",");
                            if (strArr2 != null && strArr2.length > 0) {
                                for (int k = 0; k < strArr2.length; k++) {
                                    Map map5 = new HashMap();
                                    map5.put("ischeck", false);
                                    map5.put("subname", strArr2[k]);
                                    map5.put("subid", stridArr2[k]);
                                    for (Map ownMap : ownAuthsList) {
                                        if (ownMap.get("auth_id").equals(newMap2.get("id"))) {
                                            if (ownMap.get("sub_auth") != null) {
                                                String ownSub = (String) ownMap.get("sub_auth");
                                                if (ownSub != null) {
                                                    String[] ownStrArr = ownSub.split(",");
                                                    for (String num : ownStrArr) {
                                                        if (num.equals(stridArr2[k])) {
                                                            map5.put("ischeck", true);
                                                        } else {
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if(!"".equals(map5.get("subid"))){
                                        newList2.add(map5);
                                    }

                                }
                            }
                        }
                        map2.put("subpermission", newList2);
                        newList.add(map2);
                    }
                }
                map1.put("subpermission", newList);
                list.add(map1);
            }
        }


        allMap.put("allAuth", list);

        allMap.put("nickname", userRoleTb.getRoleName());
        System.out.println(JSON.toJSON(allMap).toString());
        return JSON.toJSON(allMap).toString();
    }

    @Override
    public JSONObject editRoleAuth(Long id, String auths) {

        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        System.out.println("======>>>更改权限id:"+id);

        Map jsonMap = JSON.parseObject(auths, Map.class);
        System.out.println("=======更改权限jsonMap:" + jsonMap);
        for (Object obj : jsonMap.keySet()) {
            System.out.println("key为：" + obj + "值为：" + jsonMap.get(obj));
        }

        AuthRoleTb authRoleTb = new AuthRoleTb();
        authRoleTb.setRoleId(id);
        int r = commonDao.deleteByConditions(authRoleTb);
        logger.error("删除auth_role数据:" + r);

//        System.out.println("=======jsonMap:" +(jsonMap.get("allAuth") instanceof String) );
//        List authlist = JSON.parseArray(jsonMap.get("allAuth").toString());
//        System.out.println("=======authlist:" + authlist.size());
        List authlist =(List)jsonMap.get("allAuth");

        int res = 0;
        int all = 0;

        for (int i = 0; i < authlist.size(); i++) {
            System.out.println("=======jsonMap:" +((Map)authlist.get(0) instanceof Map) );
//            Map map = JSON.parseObject(authlist.get(i).toString(),Map.class);
            Map map = (Map)authlist.get(i);
//            System.out.println("=======是否为true:" + map.get("ischeck"));
            if ("true".equals(map.get("ischeck").toString())) {
                AuthRoleTb pauthRoleTb = new AuthRoleTb();
                //对应auth_tb中的主键
                pauthRoleTb.setAuthId( ((Integer)map.get("pid")).longValue());
                //这个 角色的id ,并不是主键Id
                pauthRoleTb.setRoleId(id);
                pauthRoleTb.setSubAuth("");
                int first =commonDao.insert(pauthRoleTb);
                logger.info("====>>>插入最顶级权限:"+first);

                res++;
                //插入下一级菜单
                List authList = (List) map.get("subpermission");
                if (authList != null && authList.size() > 0) {
                    for (int j = 0; j < authList.size(); j++) {
                        //每一个一级菜单
                        Map map1 = (Map) authList.get(j);
                        if ("true".equals(map1.get("ischeck").toString())) {//证明肯定有子级
                            //判断是不是最后一级菜单  肯定有值
                            List sauthList = (List) map1.get("subpermission");
                            if(sauthList.size()>0){
                                if (((Map) sauthList.get(0)).containsKey("subid")) {//证明已经是最后一级菜单
                                    String sub_auth ="";
                                    int kk = 0;
                                    for (int k = 0; k < sauthList.size(); k++) {
                                        Map map2 = (Map) sauthList.get(k);
                                        //拼装sub_auth参数
                                        if("true".equals(map2.get("ischeck").toString())){
                                            if(kk==0){
                                                sub_auth+=map2.get("subid");
                                            }else{
                                                sub_auth +=","+ map2.get("subid");
                                            }
                                            kk++;
                                        }
                                    }
                                    AuthRoleTb sauthRoleTb = new AuthRoleTb();
                                    sauthRoleTb.setRoleId(id);
                                    sauthRoleTb.setAuthId(((Integer)map1.get("sid")).longValue());
                                    sauthRoleTb.setSubAuth(sub_auth);
                                    int second =commonDao.insert(sauthRoleTb);
                                    logger.info("====>>>插入子级权限:"+second);

                                    res++;

                                } else {//还有下一级菜单
                                    AuthRoleTb ssauthRoleTb = new AuthRoleTb();
                                    ssauthRoleTb.setRoleId(id);
                                    ssauthRoleTb.setAuthId(((Integer)map1.get("sid")).longValue());
                                    ssauthRoleTb.setSubAuth("");
                                    int third = commonDao.insert(ssauthRoleTb);
                                    logger.info("====>>>插入次级权限:"+third);
                                    String subauth = "";
                                    for(int m =0;m<sauthList.size();m++){
                                        Map map3 = (Map)sauthList.get(m);
                                        if("true".equals(map3.get("ischeck").toString())){
                                            List subList =(List) map3.get("subpermission");
                                            int nn=0;
                                            for(int n=0;n<subList.size();n++){
                                                Map map4 = (Map) subList.get(n);
                                                if("true".equals(map4.get("ischeck").toString())){
                                                    if(nn==0){
                                                        subauth+=map4.get("subid");
                                                    }else{
                                                        subauth+=","+ map4.get("subid");
                                                    }
                                                    nn++;
                                                }
                                            }
                                            AuthRoleTb subAuthRole = new AuthRoleTb();
                                            subAuthRole.setRoleId(id);
                                            subAuthRole.setSubAuth(subauth);
                                            subAuthRole.setAuthId(((Integer)map3.get("ssid")).longValue());
                                            int four =commonDao.insert(subAuthRole);
                                            logger.error("========>>插入最终权限"+four);

                                            res++;
                                        }
                                    }
                                }
                            }else{//日报统计  和月报统计  没有下一级菜单
                                AuthRoleTb sauthRoleTb = new AuthRoleTb();
                                sauthRoleTb.setRoleId(id);
                                sauthRoleTb.setAuthId(((Integer)map1.get("sid")).longValue());
                                sauthRoleTb.setSubAuth("");
                                int second =commonDao.insert(sauthRoleTb);
                            }
                        }
                    }
                }
            }else{
                all++;
            }
        }

        if(all==authlist.size()){//所有的都没有勾选
            res=1;
        }
        if(res!=0) {
            result.put("state", 1);
            result.put("msg", "修改成功");
        }
        return result;
    }
}
