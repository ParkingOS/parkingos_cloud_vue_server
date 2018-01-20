package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.utils.ExportExcelUtil;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderManagerAction {

    Logger logger = Logger.getLogger(OrderManagerAction.class);

    @Autowired
//    @Resource(name = "mybatis")
    @Resource(name = "orderSpring")
    private OrderService orderService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = orderService.selectResultByConditions(reqParameterMap);
        logger.info(result);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/getOrderPicture")
    public String getOrderPicture(HttpServletRequest request, HttpServletResponse resp) {
        Long orderid = RequestUtil.getLong(request, "orderid", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        logger.error("==========>>>>获取图片" + orderid+"=="+comid);
        JSONObject result = orderService.getPicResult(orderid, comid);
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/carpicsup")
    public String carpicsup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Long orderid = RequestUtil.getLong(request, "orderid", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String type = RequestUtil.getString(request, "typeNew");
        Integer currentnum = RequestUtil.getInteger(request,"currentnum",-1);
        byte[] content = orderService.getCarPics(orderid,comid,type,currentnum);
        if(content.length==0){
            response.sendRedirect("http://sysimages.tq.cn/images/webchat_101001/common/kefu.png");
            return null;
        }else{
            response.setDateHeader("Expires", System.currentTimeMillis()+12*60*60*1000);
            response.setContentLength(content.length);
            response.setContentType("image/jpeg");
            OutputStream o = response.getOutputStream();
            o.write(content);
            o.flush();
            o.close();
            System.out.println("mongdb over.....");
        }
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<String>> bodyList = orderService.exportExcel(reqParameterMap);
        String[] heards = new String[]{"编号","包月产品名称","车主手机"/*,"车主账户"*/,"名字","车牌号码","购买时间","开始时间","结束时间","金额","车型类型","单双日限行","备注"};

        String fname = "会员数据" + TimeTools.getDate_YY_MM_DD();
        fname = StringUtils.encodingFileName(fname);
        java.io.OutputStream os;
        try {
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="
                    + fname + ".xls");
            response.setContentType("application/x-download");
            os = response.getOutputStream();
            ExportExcelUtil importExcel = new ExportExcelUtil("会员数据",
                    heards, bodyList);
            importExcel.createExcelFile(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
