package parkingos.com.bolink.actions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.service.ShopManageService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/shop")
public class ShopManageAction {

    Logger logger = Logger.getLogger( ShopManageAction.class );

    @Autowired
    private ShopManageService shopManageService;

    /**
     * 续费
     */
    @RequestMapping("/addmoney")
    public String addMoney(HttpServletRequest request, HttpServletResponse resp) {
        String result = shopManageService.addMoney( request );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    /**
     * 添加商户
     */
    @RequestMapping("/create")
    public String create(HttpServletRequest request, HttpServletResponse resp) {

        String result = shopManageService.create( request );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }


    /**
     * 商户查询
     */
    @RequestMapping(value = "/quickquery")
    public String query(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopManageService.quickquery( req );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    /**changeSuperimposed
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp) {
        String result = shopManageService.delete( request );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }



    /**
     * 删除
     */
    @RequestMapping("/changeSuperimposed")
    public String changeSuperimposed(HttpServletRequest request, HttpServletResponse resp) {
        //车场是否支持叠加用券  默认不支持
        Integer superimposed = RequestUtil.getInteger(request,"superimposed",0);
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        logger.info( "商户是否支持叠加用券"+superimposed+"   comid:"+comid );
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb.setSuperimposed(superimposed);
        int count = shopManageService.updateComSuperimposed(comInfoTb);
        logger.error("更改车场是否可叠加用券:"+count);
        StringUtils.ajaxOutput( resp, count+"" );
        return null;
    }

}
