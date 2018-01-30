package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ParkOrderAnlysisService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/parkorder")
public class ParkOrderanlysisAction {

    Logger logger = Logger.getLogger(ParkOrderanlysisAction.class);

    @Autowired
    private ParkOrderAnlysisService parkOrderanlysisService;


    /*
    * 查询统计
    *
    * */
    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = parkOrderanlysisService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }


    /*
    * 点击收费员统计信息
    *
    * */
    @RequestMapping(value = "/workdetail")
    public String workdetail(HttpServletRequest request, HttpServletResponse resp){

        String bt = StringUtils.decodeUTF8(RequestUtil.processParams(request, "btime"));
        String et = RequestUtil.processParams(request, "etime");
        String fieldsstr = RequestUtil.processParams(request, "fieldsstr");
        String pay_type = RequestUtil.processParams(request, "pay_type");
        String type = RequestUtil.processParams(request, "otype");
        String date = RequestUtil.getString(request,"date");
        Long uid = RequestUtil.getLong(request,"uid",-1L);
        Long comid = RequestUtil.getLong(request,"comid",-1L);

        JSONObject result = parkOrderanlysisService.selectWorkdetail(bt,et,fieldsstr,pay_type,type,uid,comid,date);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    /*
    * 点击订单总数统计
    *
    * */
    @RequestMapping(value = "/orderdetail")
    public String orderdetail(HttpServletRequest request, HttpServletResponse resp){
        Long uid = RequestUtil.getLong(request, "uid", -2L);
//        String fieldsstr = RequestUtil.processParams(request, "fieldsstr");
        String btime = StringUtils.decodeUTF8(RequestUtil.processParams(request, "btime"));
        String etime = RequestUtil.processParams(request, "etime");
        String type = RequestUtil.processParams(request, "otype");
        Long comid = RequestUtil.getLong(request,"comid",-1L);

        JSONObject result = parkOrderanlysisService.getOrderdetail(uid,btime,etime,type,comid);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
