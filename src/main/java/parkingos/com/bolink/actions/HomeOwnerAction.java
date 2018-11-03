package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import parkingos.com.bolink.models.HomeOwnerTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.HomeOwnerService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.ExportDataExcel;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homeowner")
public class HomeOwnerAction {

    Logger logger = LoggerFactory.getLogger(HomeOwnerAction.class);

    @Autowired
    private HomeOwnerService ownerService;
    @Autowired
    private SaveLogService saveLogService;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = ownerService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

//    @RequestMapping(value = "/delete")
//    public String delete(Long id, HttpServletResponse resp) {
//
//
//        JSONObject result = ownerService.deleteOwner(id);
//        //把结果返回页面
//        StringUtils.ajaxOutput(resp, result.toJSONString());
//        return null;
//    }


//    @RequestMapping(value = "/edit")
//    public String edit(HttpServletRequest request, HttpServletResponse resp) {
//
//        Long id  =RequestUtil.getLong(request,"id",-1L);
//        Integer state = RequestUtil.getInteger(request,"id",0);
//
//
//        JSONObject result = ownerService.editOwner(id);
//        //把结果返回页面
//        StringUtils.ajaxOutput(resp, result.toJSONString());
//        return null;
//    }

    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp) {
        JSONObject result = new JSONObject();
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long id = RequestUtil.getLong(request,"id",-1L);
        String name = RequestUtil.getString(request, "name");
        String homeNumber = RequestUtil.getString(request, "home_number");
        String phone = RequestUtil.getString(request, "phone").replaceAll(" ","");
        String identityCard = RequestUtil.getString(request, "identity_card");
        String remark = RequestUtil.getString(request, "remark");
        Integer state = RequestUtil.getInteger(request, "state", 0);
        Long comid = RequestUtil.getLong(request, "comid", -1L);

        if(!Check.isNumber(phone)||phone.length()!=11){
            result.put("state",0);
            result.put("msg","请输入正确的手机号");
            StringUtils.ajaxOutput(resp, result.toJSONString());
            return null;
        }
        HomeOwnerTb homeOwnerTb = new HomeOwnerTb();
        if(id>-1){
            homeOwnerTb.setId(id);
        }
        homeOwnerTb.setComid(comid);
        homeOwnerTb.setHomeNumber(homeNumber);
        homeOwnerTb.setIdentityCard(identityCard);
        homeOwnerTb.setName(name);
        homeOwnerTb.setRemark(remark);
        homeOwnerTb.setPhone(phone);
        homeOwnerTb.setState(state);

        result = ownerService.addOwner(homeOwnerTb);

        if ((Integer) result.get("state") == 1) {
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
            parkLogTb.setOperateType(1);
            if(id>-1) {
                parkLogTb.setContent(uin + "(" + nickname + ")" + "更新了业主" + phone);
            }else{
                parkLogTb.setContent(uin + "(" + nickname + ")" + "添加了业主" + phone);
            }
            parkLogTb.setType("homeowner");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp, result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        List<List<Object>> bodyList = ownerService.exportExcel(reqParameterMap);
        String[][] heards = new String[][]{{"姓名", "STR"}, {"房号", "STR"}, {"手机号", "STR"}, {"身份证号", "STR"}, {"状态", "STR"}, {"备注", "STR"}};
        ExportDataExcel excel = new ExportDataExcel("业主管理", heards, "sheet1");
        String fname = "业主管理";
        fname = StringUtils.encodingFileName(fname) + ".xls";
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + fname);
            excel.PoiWriteExcel_To2007(bodyList, os);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setOperateUser(nickname);
        parkLogTb.setOperateTime(System.currentTimeMillis() / 1000);
        parkLogTb.setOperateType(4);
        parkLogTb.setContent(uin + "(" + nickname + ")" + "导出了业主信息");
        parkLogTb.setType("homeowner");
        parkLogTb.setParkId(comid);
        saveLogService.saveLog(parkLogTb);
        return null;
    }


    @RequestMapping(value = "/importExcel")
    public JSONObject insertUserInfo( HttpServletRequest request, HttpServletResponse response) {
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request, "nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        JSONObject result = new JSONObject();
        try {
            String msg = "";
            Integer state = 0;
//            String fileUrl = "/files/excel/";
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("file");
            InputStream is = multipartFile.getInputStream();
            if (is != null) {

//                boolean is2003Excel = false;
//                boolean is2007Excel = false;
//                if(POIFSFileSystem.hasPOIFSHeader(is)) {
//                    System.out.println("2003及以下");
//                    is2003Excel = true;
//                }
//                if(POIXMLDocument.hasOOXMLHeader(is)) {
//                    System.out.println("2007及以上");
//                    is2007Excel = true;
//                }
//
////			支持office2007
//                if (is2007Excel) {
//                    wb = new XSSFWorkbook(in);
//                }
//                //支持office2003
//                if(is2003Excel){
//                    wb = new HSSFWorkbook(in);
//                }

                Workbook wb = WorkbookFactory.create(is);
                CellStyle style = wb.createCellStyle();
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                List<HomeOwnerTb> homeOwnerTbList = new ArrayList<HomeOwnerTb>();
                int rowCount = 0;
                boolean temp = true;
                try {
                    Sheet st = wb.getSheetAt(0);
                    int rowNum = st.getLastRowNum(); //获取Excel最后一行索引，从零开始，所以获取到的是表中最后一行行数减一
                    int colNum = st.getRow(0).getLastCellNum();//获取Excel列数
                    for (int r = 1; r <= rowNum; r++) {//读取每一行，第一行为标题，从第二行开始
                        rowCount = r;
                        Row row = st.getRow(r);
                        HomeOwnerTb userInfo = new HomeOwnerTb();
                        for (int l = 0; l < colNum; l++) {//读取每一行的每一列
                            Cell cell = row.getCell(l);
                            if (cell != null) {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            String phone ="";
                            if (l == 0 || l == 2) {// 这两列必填
                                if (cell != null && !"".equals(cell.toString().trim())) {
                                    if(l==2){
                                        phone = cell.getStringCellValue().replaceAll(" ","");
                                        if(phone.length()!=11||!Check.isNumber(phone)){
                                            msg+= "第"+(r+1)+"行手机号格式错误,请确认:"+phone;
                                            temp = false;
                                        }
                                    }
                                    System.out.print(cell + "\t");
                                } else {
                                    System.out.print("该项不能为空" + "\t");
                                    msg+= "第"+(r+1)+"行第"+(l+1)+"列必填，请确认！";
                                    temp = false;
                                    //给Excel中为空格的必填项添加背景色
                                }
                            } else {//身份证号和工作背景
                                System.out.print(cell + "\t");
                            }
                            if (temp) {
                                String message ="";
                                try{
                                    message=cell.getStringCellValue();
                                }catch (Exception e){

                                }
                                switch (l) {
                                    case 0:
                                        userInfo.setName(message);
                                        break;
                                    case 1:
                                        userInfo.setHomeNumber(message);
                                        break;
                                    case 2:
                                        userInfo.setPhone(phone);
                                        break;
                                    case 3:
                                        userInfo.setIdentityCard(message);
                                        break;
                                    case 4:
                                        if("禁用".equals(message)){
                                            userInfo.setState(1);
                                        }else{
                                            userInfo.setState(0);
                                        }
                                        break;
                                    case 5:
                                        userInfo.setRemark(message);
                                        break;
                                }
                            }else{
                                break;
                            }
                        }
                        if(!temp){
                            break;
                        }else{
                            logger.info("=======>>>>"+userInfo);
                            userInfo.setComid(comid);
                            homeOwnerTbList.add(userInfo);
                        }
                    }
                    if (temp) {//Excel完全没有问题
                        ownerService.saveOrUpdateAll(homeOwnerTbList,nickname,uin);
                        state = 1;
                        msg = "导入成功";
                    } else {//Excel存在必填项为空的情况
                        state = 2;
//                        msg = "Excel数据格式有问题，请下载表格，并将其中标红色的部分填写完整";
//                        String filePath = request.getSession().getServletContext().getRealPath("files/excel");
//                        String fileName ="业主信息" + String.valueOf(System.currentTimeMillis() / 1000) + ".xlsx";
//                        OutputStream out = new FileOutputStream(new File(filePath + "/" + fileName));
//                        wb.write(out);
//                        out.close();
//                        fileUrl = fileUrl + fileName;
                    }
                } catch (Exception e) {
                    System.out.println("第" + rowCount + "行出错");
                    msg = "第" + (rowCount+1) + "行出错";
                    e.printStackTrace();
                }
            }
            is.close();

            result.put("state", state);
            result.put("msg", msg);
//            result.put("fileUrl", fileUrl);
//            String urlString = "<script type='text/javascript'>window.parent.insertResult('" + result.toString() + "')</script>";
//            PrintWriter out = response.getWriter();
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("text/html;charset=UTF-8");
//            out.write(urlString);
//            out.flush();
//            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                result.put("state", 0);
                result.put("msg", "excel数据格式有问题，导入失败");
//                String urlString = "<script type='text/javascript'>window.parent.insertResult('" + result.toString() + "')</script>";
//                PrintWriter out = response.getWriter();
//                response.setCharacterEncoding("utf-8");
//                response.setContentType("text/html;charset=UTF-8");
//                out.write(urlString);
//                out.flush();
//                out.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        StringUtils.ajaxOutput(response, result.toJSONString());
        return null;
    }


}
