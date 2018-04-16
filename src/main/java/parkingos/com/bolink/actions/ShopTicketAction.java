package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.TicketService;
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
@RequestMapping("/shopticket")
public class ShopTicketAction {
    Logger logger = Logger.getLogger( ShopTicketAction.class );
    @Autowired
    private TicketService ticketService;

    @RequestMapping("/quickquery")
    //优惠券查询
    public String addMoney(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );
        logger.info( reqParameterMap );
        JSONObject result = ticketService.selectResultByConditions( reqParameterMap );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result.toJSONString() );
        return null;
    }

    @RequestMapping("/exportExcel")
    //优惠券导出
    public String exportExcel(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );

        List<List<Object>> bodyList = ticketService.exportExcel( reqParameterMap );
        String[][] heards = new String[][]{{"编号", "STR"}, {"商户名称", "STR"}, {"优惠时长(分钟)", "STR"}, {"优惠时长(小时)", "STR"}, {"优惠时长(天)", "STR"}, {"优惠金额", "STR"}, {"到期时间", "STR"}, {"状态", "STR"}, {"车牌号", "STR"}, {"优惠类型", "STR"}};

        ExportDataExcel excel = new ExportDataExcel( "优惠券数据", heards, "sheet1" );
        String fname = "优惠券数据";
        fname = StringUtils.encodingFileName( fname );
        try {
            OutputStream os = resp.getOutputStream();
            resp.reset();
            resp.setHeader( "Content-disposition", "attachment; filename=" + fname + ".xls" );
            excel.PoiWriteExcel_To2007( bodyList, os );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    /*
    * 优惠券明细
    *
    * */
    @RequestMapping("/getticketlog")
    //优惠券查询
    public String getTicketLog(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );
        logger.info( reqParameterMap );
        JSONObject result = ticketService.getTicketLog( reqParameterMap );
        logger.info( result );
        StringUtils.ajaxOutput( resp, result.toJSONString() );
        return null;
    }

    /*
    *   根据输入的额度生成二维码返回页面
    *
    * */
    @RequestMapping("/createticket")
    //优惠券查询
    public String createTicket(HttpServletRequest request, HttpServletResponse resp) {
        logger.info("进入商户后台创建二维码的方法");
//        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset( request );
//        logger.info( reqParameterMap );
        Long shop_id = RequestUtil.getLong(request,"shopid",-1L);
        Integer reduce = RequestUtil.getInteger(request, "reduce", 0);
        Integer type = RequestUtil.getInteger(request, "type", 3);
        //判断页面是不是选中自动更新选项
        Integer isAuto = RequestUtil.getInteger(request,"isauto",0);
        Map<String,Object> mapResult = ticketService.createTicket(shop_id,reduce,type,isAuto);
        StringUtils.ajaxOutput( resp, JSONObject.toJSONString(mapResult) );
        return null;
    }


    /*
    *  根据得到的code值一直轮询这张券是否使用,如果使用,那么更换二维码
    *
    * */
    @RequestMapping("/ifchangecode")
    //优惠券查询
    public String ifChangeCode(HttpServletRequest request, HttpServletResponse resp) {

        logger.info("进入轮询查询是否自动更新的方法");
        Map<String,Object> mapResult = ticketService.ifChangeCode( request );
        StringUtils.ajaxOutput( resp, JSONObject.toJSONString(mapResult) );
        return null;
    }



}
