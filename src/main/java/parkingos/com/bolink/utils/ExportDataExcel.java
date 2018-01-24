package parkingos.com.bolink.utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportDataExcel {
    private XSSFCellStyle head_Style;
    private SXSSFWorkbook workbook;
    // 当前sheet
    private SXSSFSheet sheet;
    private SXSSFRow row = null;// 创建一行
    private SXSSFCell cell = null;
    private String headers[][];
    private int currentRow = 0;
    private XSSFCellStyle date_Style ;
    private XSSFCellStyle time_Style ;
    private XSSFCellStyle string_style;
    private XSSFCellStyle double_style;
    /**
     * 构造函数初始化参数
     * @param title
     * @param headers
     * @param sheeatName
     */
    public ExportDataExcel(String title,String[][] headers,String sheeatName){
        this.headers = headers;
        try{
            workbook=new SXSSFWorkbook();
            this.head_Style=(XSSFCellStyle) this.workbook.createCellStyle();
            head_Style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            head_Style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            head_Style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            head_Style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            head_Style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            head_Style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            head_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            head_Style.setWrapText(true);
            XSSFFont head_font = (XSSFFont) workbook.createFont();
            head_font.setFontName("宋体");// 设置头部字体为宋体
            head_font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
            head_font.setFontHeightInPoints((short) 11);
            this.head_Style.setFont(head_font);// 单元格样式使用字体
             
            XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();
             
            XSSFFont data_font = (XSSFFont) workbook.createFont();
            data_font.setFontName("宋体");// 设置头部字体为宋体
            data_font.setFontHeightInPoints((short) 10);
             
            this.date_Style = (XSSFCellStyle) this.workbook.createCellStyle();
            date_Style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            date_Style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            date_Style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            date_Style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            date_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            date_Style.setFont(data_font);// 单元格样式使用字体
            head_Style.setWrapText(true);
            date_Style.setDataFormat(format.getFormat("yyyy-m-d"));
             
            this.time_Style = (XSSFCellStyle) this.workbook.createCellStyle();
            time_Style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            time_Style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            time_Style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            time_Style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            time_Style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            time_Style.setFont(data_font);// 单元格样式使用字体
            time_Style.setDataFormat(format.getFormat("yyyy-m-d h:mm:s"));
             
            this.string_style = (XSSFCellStyle) this.workbook.createCellStyle();
            string_style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            string_style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            string_style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            string_style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            string_style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            string_style.setFont(data_font);// 单元格样式使用字体
            
            this.double_style = (XSSFCellStyle) this.workbook.createCellStyle();
            double_style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            double_style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            double_style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            double_style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            double_style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            double_style.setFont(data_font);// 单元格样式使用字体
            double_style.setDataFormat(format.getFormat("0.00000"));
            createSheet( sheeatName,headers);
        }catch(Exception exc)
        {
            exc.printStackTrace();
        }
         
    }
    /**
     * 创建表头
     * @param sheetName
     * @param headers
     */
    private  void createSheet(String sheetName,String headers[][])  {
        sheet = (SXSSFSheet) workbook.createSheet(sheetName);
        row = (SXSSFRow) sheet.createRow(currentRow);
        row.setHeight((short) (350));
        for (int i = 0; i < headers.length; i++) {
            cell = (SXSSFCell) row.createCell(i);
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(headers[i][0]);
            cell.setCellStyle(head_Style);
            sheet.setColumnWidth(i, 20*256);//每行宽度
        }
        currentRow++;
    }
    /**
     * 导出excel
     * @param listRows
     * @throws ParseException
     */
    public synchronized void PoiWriteExcel_To2007(List listRows,OutputStream out) throws ParseException {
        for (int i = 0; i < listRows.size(); i++) {
            row = (SXSSFRow) sheet.createRow(currentRow);
            row.setHeight((short) (300));
            ArrayList ListCells = (ArrayList)listRows.get(i);
            for (int j = 0; j < ListCells.size(); j++) {
                Object obj = ListCells.get(j);
                obj = (obj==null||obj.toString().equals("null"))?"":obj;
                cell = (SXSSFCell) row.createCell(j);
                if(obj instanceof Integer){
                    cell.setCellValue((Integer)obj);
                    cell.setCellStyle(string_style);
                }else if(obj instanceof Date){
                    String type = headers[j][1];
                    if("DATE".equals(type)){
                        cell.setCellValue((Date)obj);
                        cell.setCellStyle(date_Style);
                    }else if("TIME".equals(type)){
                        cell.setCellValue((Date)obj);
                        cell.setCellStyle(time_Style);
                    }else{
                    	cell.setCellValue(obj+"");
                    	cell.setCellStyle(string_style);
                    }
                }else if(obj instanceof Double){
                	cell.setCellValue((Double)obj);
                    cell.setCellStyle(double_style);
                }else{
                    cell.setCellValue(obj+"");
                    cell.setCellStyle(string_style);
                }
            }
            currentRow ++;
        }
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    /**
     * 测试导出
     * @param args
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        String headers[][] = {{"日期","DATE"},{"标题","TIME"},{"其他","DOUBLE"}} ; 
        File file = new File("D://test.xlsx");
        if (file.exists())
            file.delete();
        file.createNewFile();
         
        ExportDataExcel exportData = new ExportDataExcel("test", headers, "test");
         
        List data = new ArrayList();
        for (int i = 0; i < 100; i++) {
            List<Object> cellList = new ArrayList<Object>();
            for (int j = 0; j < 2; j++) {
                cellList.add(new Date());
            }
            cellList.add(0.9);
            data.add(cellList);
        }
        OutputStream out = new FileOutputStream(file);
        exportData.PoiWriteExcel_To2007(data,out);
    }
}
