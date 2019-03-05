package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.service.BlackUserService;
import parkingos.com.bolink.service.PostManageService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/postmanage")
public class PostManageAction {

    Logger logger = LoggerFactory.getLogger(PostManageAction.class);

    @Autowired
    private PostManageService postManageService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        //rp: 20  page: 1
        Integer rp = RequestUtil.getInteger(request,"rp",20);
        Integer page =RequestUtil.getInteger(request,"page",1);
        logger.info("==>>>>get channels:"+comid);
        JSONObject result = postManageService.getChannels(comid,rp,page);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp) {

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

//        Long id = RequestUtil.getLong(request, "id", -1L);
        String name = RequestUtil.getString(request,"local_name");
        String localId = RequestUtil.getString(request,"local_id");
        logger.info("===>>>>修改岗亭管理:"+comid+"~~"+name+"~~~"+localId);
        JSONObject result = postManageService.editName(comid,name,localId);
//        把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


}
