package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CenterMonitorService;
import parkingos.com.bolink.service.CityLogService;
import parkingos.com.bolink.service.GetParkInfoService;
import parkingos.com.bolink.utils.*;
import parkingos.com.bolink.utils.payutils.HttpUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/centermonitor")
public class CenterMonitorAction {

    Logger logger = Logger.getLogger(CenterMonitorAction.class);
    @Autowired
    private CenterMonitorService centerMonitorService;

    @RequestMapping(value = "/centermonitordata")
    public String getCenterMonitor(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        Long groupid = RequestUtil.getLong(request, "groupid", -1L);
        logger.error("中央监控页面:" + comid + "~~~~" + groupid);
        String ret = centerMonitorService.getCenterMonitor(comid, groupid);
        logger.error("中央监控页面数据返回:" + ret);
        StringUtils.ajaxOutput(response, ret);
        return null;
    }

    /*
    * 获取特定的确认订单显示
    *
    * */
    @RequestMapping(value = "/getConfirmorder")
    public String getConfirmOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进入获取单个图片的时间>>>>>>>>>>>" + new Date().getTime());
        String event_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "event_id"));
        String car_number = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String comid = StringUtils.decodeUTF8(RequestUtil.getString(request, "comid"));

        logger.error("获得指定的确认订单的图片" + event_id + "~~~" + car_number + "~~~" + comid);

        byte[] content = centerMonitorService.getConfirmPic(event_id, Long.parseLong(comid), car_number);
        System.out.println("出来获取单个图片的时间>>>>>>>>>>>" + new Date().getTime());
        if (content.length == 0) {
            response.sendRedirect("http://sysimages.tq.cn/images/webchat_101001/common/kefu.png");
            return null;
        } else {
            try {
                String foldPath = request.getServletContext().getRealPath("/images/monitor/");
                File folder = new File(foldPath);
                if (!folder.exists() || !folder.isDirectory()) {
                    folder.mkdirs();
                }
                InputStream in = new ByteArrayInputStream(content);
                String filePath = request.getServletContext().getRealPath("/images/monitor/" + comid + "_" + event_id + "_" + car_number.substring(1) + ".jpg");
                File file = new File(filePath);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024 * 8];
                int nRead = 0;
                while ((nRead = in.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
                in.close();
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                JSONObject retObj = new JSONObject();
                retObj.put("picName", comid + "_" + event_id + "_" + car_number.substring(1) + ".jpg");
                retObj.put("event_id", event_id);
                retObj.put("car_nmber", car_number);
                StringUtils.ajaxOutput(response, retObj.toJSONString());
            } catch (Exception e) {
                // TODO: handle exception
                logger.info(e.toString());
            }
            System.out.println("mongdb over.....");
            return null;
        }
    }

    /*
    * 模糊匹配车牌列出所有确认订单及图片
    *
    * */
    @RequestMapping(value = "/matchconfirmorder")
    public String matchConfirmOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进入模糊获取多个图片的时间>>>>>>>>>>>" + new Date().getTime());
        String event_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "event_id"));
        String car_number = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String comid = StringUtils.decodeUTF8(RequestUtil.getString(request, "comid"));

        logger.error("获得模糊匹配到的确认订单的图片" + event_id + "~~~" + car_number + "~~~" + comid);

        centerMonitorService.matchConfirmPic(event_id, Long.parseLong(comid), car_number, request, response);
        System.out.println("出来获取多个图片的时间>>>>>>>>>>>" + new Date().getTime());

        return null;
    }

    /*
    *点击匹配到的图片查询具体信息
    *
    * */
    @RequestMapping(value = "/queryselectorder")
    public String querySelectOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderId = RequestUtil.getString(request, "order_id");
//        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String comid = URLDecoder.decode(RequestUtil.getString(request, "comid"), "UTF-8");


        String carNumber = StringUtils.decodeUTF8(RequestUtil.getString( request, "car_number" ));
//        try {
//            carNumber = new String( carNumber.getBytes( "ISO-8859-1" ), "UTF-8" );
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        logger.error(RequestUtil.getString(request,"car_number"));
//        logger.error(carNumber);
//        logger.error(StringUtils.decodeUTF8(carNumber));

        logger.error("点击匹配到的订单查询出具体信息条件" + orderId + "~~~" + carNumber + "~~~" + comid);

        Map orderMap = centerMonitorService.getSelectOrder(comid, carNumber);

        logger.error("点击匹配到的订单查询出具体信息结果" + orderMap);
        StringUtils.ajaxOutput(response, JSONObject.toJSONString(orderMap));
        return null;
    }

    /*
    *
    * 确认订单通知车场
    *
    * */
    @RequestMapping(value = "/balanceorderinfo")
    public String balanceOrderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String orderId = RequestUtil.getString(request, "order_id");
        String carNumber =  StringUtils.decodeUTF8(RequestUtil.getString(request, "car_number"));
        String channel_id = RequestUtil.getString(request, "channel_id");
        String event_id = RequestUtil.getString(request, "event_id");
        String comid = URLDecoder.decode(RequestUtil.getString(request, "comid"), "UTF-8");

        logger.error("确认订单通知车场" + "~~~" + orderId + "~~~" + carNumber + "~~~" + channel_id + "~~~" + event_id + "~~~" + comid);

        centerMonitorService.sendMessageToPark(orderId, carNumber, channel_id, event_id, comid, response);

        return null;
    }

    /*
    *
    * 抬杆通知
    * */
    @RequestMapping(value = "/liftrod")
    public String liftRod(HttpServletRequest request, HttpServletResponse response) throws Exception {


        String channel_id = StringUtils.decodeUTF8(RequestUtil.getString(request, "channel_id"));
        String channel_name = StringUtils.decodeUTF8(RequestUtil.getString(request, "channel_name"));
        String comid = StringUtils.decodeUTF8(RequestUtil.getString(request, "comid"));

        logger.error("进入抬杆通知:"+channel_id+"~~~"+channel_name+"~~~"+comid);
        centerMonitorService.liftRodMessage(channel_id,channel_name,comid,response);
        logger.error("抬杆通知发送完成");
        return null;
    }

    /*
    * 对讲通知 切换大屏
    *
    * */
    @RequestMapping(value = "/callinform")
    public String callInform(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //是不是从另一个服务器过来的
        Integer fromOther = RequestUtil.getInteger(request,"fromother",0);
        logger.error("this is what times "+fromOther);
        //电话Id
        String channel_uniqueid = RequestUtil.processParams(request, "channel_uniqueid");
        //主叫号
        String callerid_num = RequestUtil.processParams(request, "callerid_num");
        logger.error("中央监控>>>>>呼入主叫号:" + callerid_num + ">>>>");
        //被叫号
        String exten = RequestUtil.processParams(request, "exten");
        logger.error("中央监控>>>>>呼入被叫号:" + exten + ">>>>");

        centerMonitorService.callInform(fromOther,callerid_num,exten,response);


        return null;


    }

    /*
    *
    * 点击小视频框获得 抬杆  需要的数据
    *
    * */

    @RequestMapping(value = "/querychannel")
    public String qryChannelByMonitId(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long monitor_id = RequestUtil.getLong(request, "monitor_id", -1L);

//        Map channleMap = daService.getMap("select mi.*,cp.passname,cp.channel_id as channelid  from monitor_info_tb mi,com_pass_tb cp where cp.id = mi.channel_id and mi.id=? ", new Object[]{monitor_id});
//        String json = StringUtils.createJson(channleMap);
        Map liftMap = centerMonitorService.qryChannelByMonitId(monitor_id);
        StringUtils.ajaxOutput(response, JSONObject.toJSONString(liftMap));

        return null;


    }

    @RequestMapping(value = "/testExport")
    public String exportCode(String id,  HttpServletResponse response, HttpServletRequest request) {
        String path="";

        //自定义二维码存放路径
//        path = request.getServletContext().getRealPath("/")+"static\\customCode\\";
        path ="C:\\Users\\Administrator\\Desktop\\Visitor.png";
        //装备数量
//        int  equipmentNum = Integer.valueOf(equ.getEquipmentNum()).intValue();

//        String beginName=school.getOrganizationno()+equ.getBatchNumber()+equ.getEquipmentCode();
        //excel名称
        String sheetName="标签二维码";
        try {
            //创建工作薄
            /*Workbook wb = new SXSSFWorkbook(500);
            //创建工作表
            Sheet sheet = wb.createSheet(sheetName);
            Drawing patriarch = sheet.createDrawingPatriarch();*/

            // 创建一个工作薄
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建一个sheet
            HSSFSheet sheet = wb.createSheet();

            CellStyle style = wb.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//            Font titleFont = wb.createFont();
//            titleFont.setFontName("Arial");
//            titleFont.setFontHeightInPoints((short) 16);
//            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
//            style.setFont(titleFont);
            //创建第一行
//            Row titleRow=sheet.createRow(0);
//            titleRow.setHeightInPoints(40);//设置行高
//            //创建单元格
//            Cell titleCell=titleRow.createCell(0);
//            titleCell.setCellValue(sheetName);
//            titleCell.setCellStyle(style);
            //设置合并单元格
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,3));
            int rowNum=2;//计算行数，规定一行4列
            int lastRowNum =5;
//            if(equipmentNum%4==0) {
//                rowNum = equipmentNum/4;
//            }else {
//                rowNum = equipmentNum/4+1;
//            }
//            int lastRowNum = equipmentNum%4;
            //设置4列宽度

//            sheet.setColumnWidth(1, 42*200);
//            sheet.setColumnWidth(2, 42*200);
//            sheet.setColumnWidth(3, 42*200);
            for(int i=0;i<rowNum;i++) {
                Row row = sheet.createRow(i);//创建行
                row.setHeightInPoints(120);//设置行高
                if(i==rowNum-1) {//最后一行时根据具体数量生成
                    for(int j=0;j<lastRowNum;j++) {
                        sheet.setColumnWidth(j, 50*150);
                        BufferedImage bufferImg=null;//图片
                        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//                        //创建单元格
//                        Cell cell=row.createCell(j);
                        style = wb.createCellStyle();
                        style.setWrapText(true);//设置自动换行
                        //垂直居中
                        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                        //设置边线
                        style.setBorderBottom(CellStyle.BORDER_THIN);
                        style.setBorderTop(CellStyle.BORDER_THIN);
                        style.setBorderRight(CellStyle.BORDER_THIN);
                        //设置单元边框颜色
                        style.setTopBorderColor(HSSFColor.RED.index);
                        style.setRightBorderColor(HSSFColor.RED.index);
                        style.setBottomBorderColor(HSSFColor.RED.index);
//                        StringBuilder sb=new StringBuilder();
//                        sb.append("物品编号：111");
//                        sb.append("\r\n");
//                        sb.append("物品名称：打印纸");
//                        sb.append("\r\n");
//                        sb.append("购入日期：2018-06-19 15:51:29");
//                        cell.setCellValue(sb.toString());
//                        cell.setCellStyle(style);
//                        //获取二维码
                        bufferImg = ImageIO.read(new File(path));
//                        System.out.println(path+beginName+(4*i+j+1)+".jpg");
                        ImageIO.write(bufferImg, "jpg", byteArrayOut);
                        //插入Excel表格
                        HSSFClientAnchor anchor = new HSSFClientAnchor(20, 10,10, 10,
                                (short) j, i, (short)(j+1) , i+1);
                        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                                .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                }
                else {
                    for(int j=0;j<lastRowNum;j++) {
                        sheet.setColumnWidth(j, 50*155);
                        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                        //创建单元格
                        Cell cell=row.createCell(j);
                        style = wb.createCellStyle();
                        style.setWrapText(true);//设置自动换行
                        //垂直居中
                        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                        //设置边线
                        style.setBorderBottom(CellStyle.BORDER_THIN);
                        style.setBorderTop(CellStyle.BORDER_THIN);
                        style.setBorderRight(CellStyle.BORDER_THIN);
                        //设置单元边框颜色
                        style.setTopBorderColor(HSSFColor.RED.index);
                        style.setRightBorderColor(HSSFColor.RED.index);
                        style.setBottomBorderColor(HSSFColor.RED.index);
                        StringBuilder sb=new StringBuilder();
                        sb.append("物品编号：222");
                        sb.append("\r\n");
                        sb.append("物品编号：222");
                        sb.append("\r\n");
                        sb.append("物品编号：222");
                        sb.append("\r\n");
                        sb.append("物品名称：打印纸");
                        sb.append("\r\n");
                        sb.append("购入日期：2018-06-19 15:52:38");
                        cell.setCellValue(sb.toString());
                        cell.setCellStyle(style);
                        BufferedImage bufferImg=null;//图片
                        System.out.println(path);
                        //获取二维码
//                        bufferImg = ImageIO.read(new File(path));
//                        ImageIO.write(bufferImg, "jpg", byteArrayOut);
                        /**
                         * 该构造函数有8个参数
                         * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
                         * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和 rowNum，
                         * excel中的cellNum和rowNum的index都是从0开始的
                         *
                         */
                        //插入Excel表格
//                        HSSFClientAnchor anchor = new HSSFClientAnchor(500, 10,0,0,
//                                (short) j, (i+1), (short)(j+1) , (i+2));
//                        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
//                                .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                }
            }
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(
                    (sheetName+ LocalDate.now()+".xls").getBytes("utf-8"), "iso-8859-1"));
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

