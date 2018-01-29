package parkingos.com.bolink.actions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ShopMemberManageService;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/shopmember")
public class ShopMemberManageAction {

    Logger logger = Logger.getLogger( ShopMemberManageAction.class );

    @Autowired
    private ShopMemberManageService shopMemberManageService;


    @RequestMapping(value = "/editpass")
    public String editpass(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopMemberManageService.editpass( req );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopMemberManageService.delete( req );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }


    @RequestMapping(value = "/quickquery")
    public String query(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopMemberManageService.quickquery( req );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    @RequestMapping(value = "/create")
    public String create(HttpServletRequest req, HttpServletResponse resp) {
        String result = shopMemberManageService.create( req );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }
}
