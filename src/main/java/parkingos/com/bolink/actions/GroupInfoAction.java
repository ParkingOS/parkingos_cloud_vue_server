package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.GroupInfoService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/group")
public class GroupInfoAction {

    Logger logger = Logger.getLogger(GroupInfoAction.class);

    @Autowired
    private GroupInfoService groupInfoService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        Long uin = RequestUtil.getLong(request,"loginuin",-1L);
        String result = groupInfoService.getResultByCondition(groupid,uin);

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result);
        return null;
    }


    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){

        JSONObject result = groupInfoService.updateGroupInfo(request);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
