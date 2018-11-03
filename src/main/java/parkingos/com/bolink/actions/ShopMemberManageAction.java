package parkingos.com.bolink.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ShopMemberManageService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/shopmember")
public class ShopMemberManageAction {

    Logger logger = LoggerFactory.getLogger( ShopMemberManageAction.class );

    @Autowired
    private ShopMemberManageService shopMemberManageService;


    @RequestMapping(value = "/editpass")
    public String editpass(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopMemberManageService.editpass( req );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopMemberManageService.delete( req );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }


    @RequestMapping(value = "/quickquery")
    public String query(HttpServletRequest req, HttpServletResponse resp) {

        String result = shopMemberManageService.quickquery( req );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    @RequestMapping(value = "/create")
    public String create(HttpServletRequest req, HttpServletResponse resp) {
        String result = shopMemberManageService.create( req );
        StringUtils.ajaxOutput( resp, result );
        return null;
    }

    @RequestMapping(value = "/getrole")
    public String getRole(HttpServletRequest request, HttpServletResponse resp){


//        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        Long roleId = RequestUtil.getLong(request,"loginroleid",-1L);
        Long shopId = RequestUtil.getLong(request,"shopid",-1L);

        String result = shopMemberManageService.getRoleByConditions(roleId,shopId);

        StringUtils.ajaxOutput(resp,result);
        return null;
    }

    @RequestMapping(value = "/getshopusers")
    public String getShopUsers(HttpServletRequest request, HttpServletResponse resp){


//        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        Long shopId = RequestUtil.getLong(request,"shopid",-1L);
        String result = shopMemberManageService.getShopUsers(shopId);

        StringUtils.ajaxOutput(resp,result);
        return null;
    }

}
