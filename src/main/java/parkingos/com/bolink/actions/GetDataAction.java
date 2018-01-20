package parkingos.com.bolink.actions;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.GetDataService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/getdata")
public class GetDataAction {

    Logger logger = Logger.getLogger(GetDataAction.class);

    @Autowired
    private GetDataService getDataService;

    @RequestMapping(value = "/nickname")
    public String getNicknameById(HttpServletRequest request, HttpServletResponse resp){
        Long id = RequestUtil.getLong(request, "id", -1L);
        String result = getDataService.getNicknameById(id);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


    @RequestMapping(value = "/cartype")
    public String getCarTypeById(HttpServletRequest request, HttpServletResponse resp){

        Long id =RequestUtil.getLong(request, "id", -1L);
        String result = getDataService.getCarTypeById(id);
        StringUtils.ajaxOutput(resp,result);
        return null;
    }
}
