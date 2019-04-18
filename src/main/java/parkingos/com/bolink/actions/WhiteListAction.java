package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.WhiteListService;
import parkingos.com.bolink.utils.ExportDataExcel;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/white")
public class WhiteListAction {

    Logger logger = LoggerFactory.getLogger(WhiteListAction.class);

    @Autowired
    private WhiteListService whiteListService;
    @Autowired
    CommonService commonService;


    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = whiteListService.parkQuery(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp) {

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);

        Long groupid = commonService.getGroupIdByComid(comid);

        String remark = RequestUtil.getString(request, "remark");
        String carNumber =  RequestUtil.getString(request, "car_number");
        Long btime = RequestUtil.getLong(request,"b_time",-1L);
        Long etime = RequestUtil.getLong(request,"e_time",-1L);
        String userName = RequestUtil.getString(request,"user_name");
        String mobile = RequestUtil.getString(request,"mobile");
        String carLocation = RequestUtil.getString(request,"car_location");
        logger.info("===.>>>:"+carNumber+"~~"+comid+"~~"+userName);
        Integer endType = RequestUtil.getInteger(request,"end_type",0);
        JSONObject result = whiteListService.add(remark,carNumber,btime,etime,comid,userName,mobile,carLocation,nickname,uin,groupid,1,endType);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp) {

        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);

        Long groupid = commonService.getGroupIdByComid(comid);

        Long id = RequestUtil.getLong(request,"id",-1L);
        String remark = RequestUtil.getString(request, "remark");
        String carNumber =  RequestUtil.getString(request, "car_number");
        Long btime = RequestUtil.getLong(request,"b_time",-1L);
        Long etime = RequestUtil.getLong(request,"e_time",-1L);
        String userName = RequestUtil.getString(request,"user_name");
        String mobile = RequestUtil.getString(request,"mobile");
        String carLocation = RequestUtil.getString(request,"car_location");
        Integer endType = RequestUtil.getInteger(request,"end_type",0);


        logger.info("===.>>>车场修改白名单:"+carNumber+"~~"+comid+"~~"+userName+"~~"+id);
        JSONObject result = whiteListService.edit(id,remark,carNumber,btime,etime,comid,userName,mobile,carLocation,nickname,uin,groupid,1,endType);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }



    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp) {

        Long id = RequestUtil.getLong(request,"id",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);


        Long groupid = commonService.getGroupIdByComid(comid);

        logger.info("===.>>>white delete:"+id);
        JSONObject result = whiteListService.delete(id,nickname,uin,comid,groupid,1);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }


    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {

        Long comid = RequestUtil.getLong(request,"comid",-1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = whiteListService.parkExportExcel(reqParameterMap,comid,nickname,uin);
        /*
        * 编号、车牌号、状态（正常/已过期）、开始时间、结束时间、更新时间、备注、操作；→→→二级：车主姓名、车主电话、车位
        * */
        String [][] heards = new String[][]{{"编号","STR"},{"车牌号","STR"},{"状态","STR"},{"车主姓名","STR"},{"车主电话","STR"},{"车位","STR"},{"开始时间","STR"},{"结束时间","STR"},{"更新时间","STR"},{"备注","STR"}};
        ExportDataExcel excel = new ExportDataExcel("车场白名单", heards, "sheet1");
        String fname = "车场白名单";
        fname = StringUtils.encodingFileName(fname)+".xls";
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+fname);
            excel.PoiWriteExcel_To2007(bodyList, os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


}
