package parkingos.com.bolink.actions;

import com.mongodb.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.GetParkInfoService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/getparkinfo")
public class GetParkInfoAction {
    @Autowired
    private GetParkInfoService getParkInfoService;
    @RequestMapping(value = "/bygroupid")
    public String getNicknameById(HttpServletRequest request, HttpServletResponse resp){
        int groupid = RequestUtil.getInteger(request,"groupid",0);
       String  ret=getParkInfoService.getInfo(groupid);

        StringUtils.ajaxOutput(resp,ret);
        return null;
    }
}
