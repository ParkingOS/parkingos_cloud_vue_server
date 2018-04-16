package parkingos.com.bolink.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 * @time 2017-8-8
 * @author QuanHao
 */
public class QrCodeUtil {
	static Logger logger = Logger.getLogger(QrCodeUtil.class);
	
	/**
	 * 获取服务器根目录
	 * @param request
	 * @return
	 * @time 2017年 下午2:40:19
	 * @author QuanHao
	 */
	public static String getServerPath(HttpServletRequest request){
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String contextPath = request.getContextPath();
		int indexOf = realPath.lastIndexOf(contextPath.substring(1));
		return realPath.substring(0, indexOf);
	}
	
	/**
	 * 获得文件浏览器访问路径
	 * @param request 
	 * @param filename 文件名称
	 * @param ex 后缀
	 * @param filedirs 文件目录
	 * @return 文件浏览器访问路径
	 * @time 2017年 下午2:46:43
	 * @author QuanHao
	 */
	public static String getFileSrcPath(HttpServletRequest request, String filename, String ex, String[] filedirs){
		String filedir = "";
		if(filedirs.length>0){
			for (int i = 0; i < filedirs.length; i++) {
				filedir += "/"+filedirs[i];
			}
			filedir += "/";
		}
		return request.getScheme()+"://"+request.getServerName()+ filedir + filename + "." + ex;
	}
	
	/**
	 * 生成二维码
	 * @param qrUrl 二维码地址
	 * @param filename 二维码文件名称
	 * @param serverPath 服务器根地址
	 * @return 二维码在服务器存放目录
	 * @throws Exception
	 * @time 2017年 下午2:56:57
	 * @author QuanHao
	 */
	public static String generateQRCode(String qrUrl,String filename,String serverPath) throws Exception{
		
		//根据url生成二维码图片
		int qrcodeWidth = 300;
        int qrcodeHeight = 300;
        String qrcodeFormat = "png";
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrUrl, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
        BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);
		
        String filePath = serverPath;
        logger.error(">>>fileDir"+filePath);
        
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		String qrFile = serverPath;
		qrFile += File.separator + filename + "." + qrcodeFormat;
		logger.error(">>>qrFile: "+qrFile);
		
		File qrcodeFile = new File(qrFile);
		if(qrcodeFile.exists()){
			boolean delete = qrcodeFile.delete();
			logger.error("删除已存在二维码图片:"+delete);
		}
		logger.error(">>>>>>生成二维码图片");
		ImageIO.write(image, qrcodeFormat, qrcodeFile);
		MatrixToImageWriter.writeToFile(bitMatrix, qrcodeFormat, qrcodeFile);
		return qrFile;
	}
	
	/**
	 * 清空文件夹
	 * @param serverPath 服务器根地址
	 * @param filedirs 文件目录
	 * @time 2017年 下午4:23:01
	 * @author QuanHao
	 */
	public static void clearFileDir(String serverPath, String[] filedirs){
		String qrFile = serverPath;
		for (int i = 0; i < filedirs.length; i++) {
			qrFile += File.separator + filedirs[i];
		}
		File file = new File(qrFile);
		if(file.exists()){
			String[] children = file.list();
			for(int i=0;i<children.length;i++){
				logger.error(children[i]);
				File child = new File(file, children[i]);
				boolean delete = child.delete();
				logger.error(delete);
			}
		}
	}

	/**
	 * 生成物料二维码
	 * @param big
	 * @param qrCode
	 * @param outFile
	 */
	public static final void produceQrCode(BufferedImage big, String qrCode, String outFile) {
		try {
			BufferedImage small = ImageIO.read(new File(qrCode));
			Graphics2D g = big.createGraphics();
			int qrcodeWidth = 410;
			int qrcodeHeight = 410;
			int qrcodeX = 607;
			int qrcodeY = 67;
			g.drawImage(small, qrcodeX, qrcodeY, qrcodeWidth ,qrcodeHeight, null);
			g.dispose();
			ImageIO.write(big, "png", new File(outFile));
			big.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 导出物料二维码压缩包
	 * @param request
	 * @param response
	 */
	public static final void downAllFile(HttpServletRequest request, HttpServletResponse response,String unionName){
		try
		{
			String fileName = unionName+".png";
			String filePath = request.getSession().getServletContext().getRealPath("/resource/images/"+unionName);
			String downFilePath = request.getSession().getServletContext().getRealPath("/resource/images/"+unionName+".png");
			//FileUtil.fileToZip(filePath, downFilePath);
			//下载
			FileUtil.downFile(downFilePath, fileName, response);
		}catch(Exception e){
			// TODO: handle exception
		}
	}
}
