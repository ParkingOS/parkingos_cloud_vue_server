package parkingos.com.bolink.utils;

import org.apache.poi.hssf.util.CellRangeAddress;
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

public class ExportExcelUtil {
	/**
	 * 多行表头 dataList：导出的数据；sheetName：表头名称； head0：表头第一行列名；headnum0：第一行合并单元格的参数
	 * head1：表头第二行列名；headnum1：第二行合并单元格的参数；detail：导出的表体字段
	 * 
	 */
	 private XSSFCellStyle head_Style;
	    private SXSSFWorkbook workbook;
	    // 当前sheet
	    private SXSSFSheet sheet;
	    private SXSSFRow row = null;// 创建一行
	    private SXSSFCell cell = null;
	    private String headers[];
	    private String dataType[];
	    private int currentRow = 0;
	    private XSSFCellStyle date_Style ;
	    private XSSFCellStyle time_Style ;
	    private XSSFCellStyle string_style;
	    private XSSFCellStyle double_style;
	   
	    private String[] subHeadrs ;//第二行表头
	    private String[] headersnum;//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
	    private String[] subHeadnum;//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
	    private int [] subStartEnd;
	    
	    /**
	     * 构造函数初始化参数
	     * @param out
	     * @param title
	     * @param headers
	     * @param sheeatName
	     */
	    public ExportExcelUtil(String title, String[] headers, String sheeatName, String [] dataType,
                               String[] subHeadrs, String[] headersnum, String[] subHeadnum, int[] subStartEnd){
	        this.headers = headers;
	        this.dataType = dataType;
	        this.subHeadrs = subHeadrs;
	        this.headersnum= headersnum;
	        this.subHeadnum = subHeadnum;
	        this.subStartEnd = subStartEnd;
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
	            head_font.setFontHeightInPoints((short) 12);
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
	        	// 第一行表头标题
	            sheet = (SXSSFSheet) workbook.createSheet(sheeatName);
	            if(title!=null){
	            	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
	            	row = (SXSSFRow)sheet.createRow(currentRow);
	            	row.setHeight((short) 0x249);
	            	cell = (SXSSFCell)row.createCell(0);
	            	cell.setCellValue(title);
	            	cell.setCellStyle(head_Style);
	            	currentRow++;
	            	
//	            	sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.length - 1));
//	            	row = (SXSSFRow)sheet.createRow(currentRow);
//	            	row.setHeight((short) 0x349);
//	            	cell = (SXSSFCell)row.createCell(0);
//	            	cell.setCellValue(title);
//	            	cell.setCellStyle(head_Style);
//	            	currentRow++;
	            }
	            createSheet(headers);
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
	    private  void createSheet(String headers[])  {
	        //sheet = (SXSSFSheet) workbook.createSheet(sheetName);
	        row = (SXSSFRow) sheet.createRow(currentRow);
	        row.setHeight((short) (350));
	        for (int i = 0; i < headers.length; i++) {
	            cell = (SXSSFCell) row.createCell(i);
	            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(head_Style);
	            sheet.setColumnWidth(i, 20*256);//每行宽度
	        }
	        if(headersnum!=null){//需要合并单元
	        	// 动态合并单元格
	    		for (int i = 0; i < headersnum.length; i++) {
	    			String[] temp = headersnum[i].split(",");
	    			Integer startrow = Integer.parseInt(temp[0]);
	    			Integer overrow = Integer.parseInt(temp[1]);
	    			Integer startcol = Integer.parseInt(temp[2]);
	    			Integer overcol = Integer.parseInt(temp[3]);
	    			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
	    					startcol, overcol));
	    		}
	        }
	        currentRow++;
	        if(subHeadrs!=null&&subHeadnum!=null){
	        	 row = (SXSSFRow) sheet.createRow(currentRow);
	        	 for (int i = 0; i < headers.length; i++) {
	                 cell = (SXSSFCell) row.createCell(i);
	                 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
	                 cell.setCellStyle(head_Style);
	                 if (i > subStartEnd[0] && i < subStartEnd[1]) {
	     				for (int j = 0; j < subHeadrs.length; j++) {
	     					cell = (SXSSFCell) row.createCell(j + subStartEnd[0]+1);
	     					cell.setCellValue(subHeadrs[j]);// 给excel中第四行的3、4、5、6列赋值（"温度℃",
	     					//System.err.println(i+"="+subHeadrs[j]);
	     												// "湿度%", "温度℃", "湿度%"）
	     					cell.setCellStyle(head_Style);// 设置excel中第四行的3、4、5、6列的边框
	     				}
	     			 }
	                // sheet.setColumnWidth(i, 20*256);//每行宽度
	             }
	             	// 动态合并单元格
	     		for (int i = 0; i < subHeadnum.length; i++) {
	     			String[] temp = subHeadnum[i].split(",");
	     			Integer startrow = Integer.parseInt(temp[0]);
	     			Integer overrow = Integer.parseInt(temp[1]);
	     			Integer startcol = Integer.parseInt(temp[2]);
	     			Integer overcol = Integer.parseInt(temp[3]);
	     			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
	     					startcol, overcol));
	     		}
	     		 currentRow++;
	        }
	       
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
	            List<Object> ListCells =(List<Object>)listRows.get(i);
	           
	            for (int j = 0; j < ListCells.size(); j++) {
	                Object obj = ListCells.get(j);
	                obj = (obj==null||obj.toString().equals("null"))?"":obj;
	                cell = (SXSSFCell) row.createCell(j);
	                if(obj instanceof Integer){
	                    cell.setCellValue((Integer)obj);
	                    cell.setCellStyle(string_style);
	                }else if(obj instanceof Date){
	                    String type = dataType[j];
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
	        String headers[] =  { "日期", "天气", "自然", "自然", "调整", "调整", "备注","记录人"} ;
	        String dataType []={"DATE","DATE","DATE","DATE","DATE","DATE","STR","DOUBLE"};
	        String[] subHeads = new String[] { "温度℃", "湿度%", "温度℃","湿度%" };// 在excel中的第4行每列（合并列）的参数
			String[] headnum = new String[] { "1,2,0,0", "1,2,1,1", "1,1,2,3","1,1,4,5", "1,2,6,6", "1,2,7,7"};
			// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] subheadnum = new String[] { "2,2,2,2", "2,2,3,3", "2,2,4,4","2,2,5,5"};
			
	        File file = new File("D://test.xlsx");
	        if (file.exists())
	            file.delete();
	        file.createNewFile();
	         
	        ExportExcelUtil exportData = new ExportExcelUtil("订单报表", headers, "报表1",dataType,
	        		subHeads,headnum,subheadnum,new int[]{1,6});
	        
	        List data = new ArrayList();
	        for (int i = 0; i < 100; i++) {
	            List<Object> cellList = new ArrayList<Object>();
	            for (int j = 0; j < 7; j++) {
	                cellList.add(new Date());
	            }
	            cellList.add(0.9);
	            data.add(cellList);
	        }
	        OutputStream out = new FileOutputStream(file);
	        exportData.PoiWriteExcel_To2007(data,out);
	    }

}
