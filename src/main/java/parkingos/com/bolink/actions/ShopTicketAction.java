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
        String[][] heards = new String[][]{{"编号", "STR"}, {"商户名称", "STR"}, {"优惠时长", "STR"}, {"优惠金额", "STR"}, {"到期时间", "STR"}, {"状态", "STR"}, {"车牌号", "STR"}, {"优惠类型", "STR"}};

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
}
