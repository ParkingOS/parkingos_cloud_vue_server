package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.GroupInfoService;
import parkingos.com.bolink.service.SaveLogService;
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
    @Autowired
    private SaveLogService saveLogService;

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
        Long groupid = RequestUtil.getLong(request,"groupid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        JSONObject result = groupInfoService.updateGroupInfo(request);

        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了运营集团信息");
            parkLogTb.setType("groupinfo");
            parkLogTb.setGroupId(groupid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

}
