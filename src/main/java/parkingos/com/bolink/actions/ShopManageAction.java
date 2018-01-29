package parkingos.com.bolink.actions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ShopManageService;
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

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp) {
        String result = shopManageService.delete( request );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

}
