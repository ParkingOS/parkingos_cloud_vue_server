package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.ParkOrderAnlysisService;
import parkingos.com.bolink.utils.ExportExcelUtil;
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
    * 点击收费员统计信息  暂时不用
    *
    * */
    @RequestMapping(value = "/workdetail")
    public String workdetail(HttpServletRequest request, HttpServletResponse resp){

        String bt = RequestUtil.processParams(request, "btime");
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
    * 点击订单总数统计  暂时不用
    *
    * */
    @RequestMapping(value = "/orderdetail")
    public String orderdetail(HttpServletRequest request, HttpServletResponse resp){
        Long uid = RequestUtil.getLong(request, "uid", -2L);
//        String fieldsstr = RequestUtil.processParams(request, "fieldsstr");
        String btime = RequestUtil.processParams(request, "btime");
        String etime = RequestUtil.processParams(request, "etime");
        String type = RequestUtil.processParams(request, "otype");
        Long comid = RequestUtil.getLong(request,"comid",-1L);

        JSONObject result = parkOrderanlysisService.getOrderdetail(uid,btime,etime,type,comid);

        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> resList = parkOrderanlysisService.exportExcel(reqParameterMap);
        String title = "车场日报";
        String sheeatName = "sheet1";
        String headers[] =  { "收费员", "订单总数", "应收金额", "实收金额", "实收金额", "实收金额","减免金额" } ;
        String dataType []={"STR","STR","STR","STR","STR","STR","STR"};
        String[] subHeads = new String[] {"现金支付", "电子支付", "合计"};
        String[] headnum = new String[] { "1,2,0,0", "1,2,1,1","1,2,2,2","1,1,3,5","1,2,6,6"};
        String[] subheadnum = new String[] { "2,2,3,3", "2,2,4,4", "2,2,5,5"};
        ExportExcelUtil excelUtil = new ExportExcelUtil(title, headers, sheeatName, dataType, subHeads, headnum, subheadnum, new int[]{2,6});
        String fname = "车场日报";
        fname = StringUtils.encodingFileName(fname)+".xls";
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+fname);
            excelUtil.PoiWriteExcel_To2007(resList,os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
