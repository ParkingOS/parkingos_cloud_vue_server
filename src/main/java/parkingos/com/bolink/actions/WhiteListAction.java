package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.service.BlackUserService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.service.WhiteListService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/groupwhite")
public class GroupWhiteListAction {

    Logger logger = LoggerFactory.getLogger(GroupWhiteListAction.class);

    @Autowired
    private WhiteListService whiteListService;


    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = whiteListService.groupQuery(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp) {

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long groupid = RequestUtil.getLong(request, "groupid", -1L);

        String remark = RequestUtil.getString(request, "remark");
        String carNumber =  RequestUtil.getString(request, "car_number");
        Long btime = RequestUtil.getLong(request,"b_time",-1L);
        Long etime = RequestUtil.getLong(request,"e_time",-1L);
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String userName = RequestUtil.getString(request,"user_name");
        String mobile = RequestUtil.getString(request,"mobile");
        String carLocation = RequestUtil.getString(request,"car_location");
        logger.info("===.>>>:"+carNumber+"~~"+comid+"~~"+userName);
        JSONObject result = whiteListService.add(remark,carNumber,btime,etime,comid,userName,mobile,carLocation,nickname,uin,groupid,2);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp) {

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long groupid = RequestUtil.getLong(request, "groupid", -1L);


        Long id = RequestUtil.getLong(request,"id",-1L);
        String remark = RequestUtil.getString(request, "remark");
        String carNumber =  RequestUtil.getString(request, "car_number");
        Long btime = RequestUtil.getLong(request,"b_time",-1L);
        Long etime = RequestUtil.getLong(request,"e_time",-1L);
        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String userName = RequestUtil.getString(request,"user_name");
        String mobile = RequestUtil.getString(request,"mobile");
        String carLocation = RequestUtil.getString(request,"car_location");
        logger.info("===.>>>集团修改白名单:"+carNumber+"~~"+comid+"~~"+userName+"~~"+id);
        JSONObject result = whiteListService.edit(id,remark,carNumber,btime,etime,comid,userName,mobile,carLocation,nickname,uin,groupid,2);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }



    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp) {

        Long id = RequestUtil.getLong(request,"id",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long groupid = RequestUtil.getLong(request, "groupid", -1L);

        logger.info("===.>>>white delete:"+id);
        JSONObject result = whiteListService.delete(id,nickname,uin,-1L,groupid,2);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/importExcel")
    public String importExcel(HttpServletRequest request, HttpServletResponse resp,@RequestParam("file")MultipartFile file) throws Exception{
        try {
            String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
            Long uin = RequestUtil.getLong(request, "loginuin", -1L);
            Long groupid = RequestUtil.getLong(request, "groupid", -1L);
            JSONObject result = whiteListService.importExcel(file,nickname,uin,groupid);
            StringUtils.ajaxOutput(resp,result.toJSONString());
        }catch (Exception e){
            logger.error("import error",e);
        }

//        if((Integer)result.get("state")==1){
//            ParkLogTb parkLogTb = new ParkLogTb();
//            parkLogTb.setOperateUser(nickname);
//            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
//            parkLogTb.setOperateType(1);
//            parkLogTb.setContent(uin+"("+nickname+")"+"导入月卡会员成功");
//            parkLogTb.setType("vip");
//            parkLogTb.setGroupId(groupid);
//            saveLogService.saveLog(parkLogTb);
//        }
        return null;
    }


}
