package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ShopAccountTb;
import parkingos.com.bolink.models.ShopTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopManageAction {

    Logger logger = Logger.getLogger(ShopManageAction.class);
    @Autowired
    private CommonDao commonDao;

    /**
     * 续费
     */
    @RequestMapping("/addmoney")
    public String addMoney(HttpServletRequest request, HttpServletResponse resp){
        Long shoppingmarket_id = RequestUtil.getLong(request, "shop_id", -1L);

        if(shoppingmarket_id == -1){
            StringUtils.ajaxOutput(resp, "-1");
            return null;
        }
        ShopTb queryShopTb = new ShopTb();
        queryShopTb.setId( shoppingmarket_id );

        ShopTb shopTb  = (ShopTb) commonDao.selectObjectByConditions( queryShopTb );
        System.out.println( shopTb );

        Integer ticket_time = RequestUtil.getInteger(request, "ticket_time", 0);
        //Integer ticket_time_type = RequestUtil.getInteger(request, "ticket_time_type",1);
        Integer ticket_money = RequestUtil.getInteger(request, "ticket_money", 0);
        double addmoney = RequestUtil.getDouble(request, "addmoney",0.00);
        //减免类型
        Integer ticket_type = Integer.parseInt(shopTb.getTicketType()+"");
        if(ticket_type == 1){
            if(0>=ticket_time){
                StringUtils.ajaxOutput(resp, "减免小时必须输入正整数");
                return null;
            }
        }else{
            if(0>=ticket_money){
                StringUtils.ajaxOutput(resp, "减免劵必须输入正整数");
                return null;
            }
        }
        Integer ticket_limit = 0;
        Integer ticketfree_limit = 0;
        //减免劵(小时)
        ticket_limit += ticket_time;
			/*if(ticket_time_type ==1){

			}else{
				//全免劵(张)
				ticketfree_limit += ticket_time;
			}*/
        shopTb.setTicketLimit( shopTb.getTicketLimit()+ticket_limit );
        shopTb.setTicketfreeLimit( shopTb.getTicketfreeLimit()+ticketfree_limit );
        shopTb.setTicketMoney(shopTb.getTicketMoney()+ticket_money);
        commonDao.updateByPrimaryKey( shopTb );
        //记录缴费流水
        /*String username = (String)request.getSession().getAttribute("userid");
        long userid = -1;
        String strid = "";
        if(Check.checkUin(username)){
            userid = Long.parseLong(username);
        }else{
            strid = username;
        }*/

        ShopAccountTb shopAccountTb = new ShopAccountTb();
        shopAccountTb.setShopId(Integer.parseInt( shopTb.getId()+"" ));
        shopAccountTb.setShopName(shopTb.getName());
        shopAccountTb.setTicketLimit(ticket_limit);
        shopAccountTb.setTicketfreeLimit( ticketfree_limit );
        shopAccountTb.setTicketMoney(ticket_money);
        shopAccountTb.setAddMoney( new BigDecimal( addmoney ) );
        shopAccountTb.setOperateTime( System.currentTimeMillis()/1000 );
        shopAccountTb.setOperateType( 1 );
        shopAccountTb.setParkId(21782L );
        shopAccountTb.setStrid( "IST_test" );
        shopAccountTb.setOperator( 523566L );
        commonDao.insert(shopAccountTb);
        /*int s =	daService.update("insert into shop_account_tb(shop_id,shop_name,ticket_limit,ticketfree_limit,ticket_money,add_money,operate_time,operator,park_id,strid,operate_type) values(?,?,?,?,?,?,?,?,?,?,?)",
                new Object[] { shopMap.get("id"), shopMap.get("name"), ticket_limit, ticketfree_limit, ticket_money,addmoney,System.currentTimeMillis() / 1000, userid,comid,strid,1});
        AjaxUtil.ajaxOutput(response, r+"");*/
        return null;
    }
    /**
     *添加商户
     */
    @RequestMapping("/create")
    public String create(HttpServletRequest request, HttpServletResponse resp){
        //接收参数
        Long id = RequestUtil.getLong( request,"id",-1L );
        String name = RequestUtil.processParams(request, "name");
        String address = RequestUtil.processParams(request, "address");
        String mobile = RequestUtil.processParams(request, "mobile");
        String phone = RequestUtil.processParams(request, "phone");
        Integer ticket_type = RequestUtil.getInteger(request, "ticket_type",1);
        //Integer ticket_limit = RequestUtil.getInteger(request, "ticket_limit", 0);
        //Integer ticketfree_limit = RequestUtil.getInteger(request, "ticketfree_limit", 0);
        String default_limit = RequestUtil.getString(request, "default_limit");
        double discount_percent = RequestUtil.getDouble(request, "discount_percent",100.00);//商户折扣/%
        double discount_money = RequestUtil.getDouble(request, "discount_money",1.00);//商户折扣---每小时/元
        Integer validite_time = RequestUtil.getInteger(request, "validite_time", 0);//有效期/小时
        Long create_time = System.currentTimeMillis()/1000;

        //封装
        ShopTb shopTb = new ShopTb();
        shopTb.setName( name );
        shopTb.setAddress( address );
        shopTb.setMobile( mobile );
        shopTb.setPhone( phone );
        shopTb.setTicketType(ticket_type  );
        shopTb.setDefaultLimit( default_limit );
        shopTb.setDiscountMoney( new BigDecimal( discount_money ) );
        shopTb.setDiscountPercent( new BigDecimal( discount_percent ) );
        shopTb.setValiditeTime( validite_time );
        shopTb.setCreateTime( create_time );
        shopTb.setComid( 21782L );

        if(id<0){
            //添加操作
            commonDao.insert( shopTb );
        }else{
            //修改操作
            shopTb.setId( id );
            ShopTb queryTb= new ShopTb();
            queryTb.setId( id );
            commonDao.updateByPrimaryKey( shopTb );
        }

        return null;
    }


    /**
     * 商户查询
     */
    @RequestMapping(value = "/quickquery")
    public String query(HttpServletRequest req, HttpServletResponse resp) {
        Integer pageNum = RequestUtil.getInteger(req,"page",1);
        Integer pageSize = RequestUtil.getInteger(req,"rows",30);
        System.out.println( pageNum );
        System.out.println( pageSize );
        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        ShopTb shopTb  = new ShopTb();
        shopTb.setComid(21782L);
        int total = commonDao.selectCountByConditions( shopTb );
        result.put( "total",total );
        //state状态 0为正常使用 1为删除状态
        shopTb.setState( 0 );
        int count = commonDao.selectCountByConditions(shopTb);

        if(count>0){
            /**分页处理*/
            PageOrderConfig config = new PageOrderConfig();
            config.setPageInfo(pageNum,pageSize);
            List<ShopTb> list  =commonDao.selectListByConditions(shopTb,config);
            List<Map<String,Object>> resList  = new ArrayList<>();
            if(list!=null&&!list.isEmpty()){
                for(ShopTb sb : list){
                    OrmUtil<ShopTb> otm = new OrmUtil<>();
                    Map<String,Object> map = otm.pojoToMap(sb);
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
            result.put("total",count);
            result.put("page",pageNum);
        }

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Transactional
    public String delete(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong( request,"id",-1L );
        if(id>0){
            ShopTb shopTb = new ShopTb();
            shopTb.setId( id );
            shopTb.setState( 1 );
            //删除操作将state状态修改为1
            commonDao.updateByPrimaryKey( shopTb );
        }
        return null;
    }

}
