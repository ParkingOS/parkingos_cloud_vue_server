package parkingos.com.bolink.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
	private static final Logger log = Logger.getLogger(FileUtil.class);

	/**
	 * DOC 从文件里读取数据.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFromFile(InputStream in) throws FileNotFoundException, IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * DOC 往文件里写入数据.
	 * 
	 * @throws IOException
	 */
	public static void writeToFile(String content, String path) {
		FileWriter writer = null;
		try {
			File file = new File(path);// 指定要读取的文件
			if (!file.exists()) {// 如果文件不存在，则创建该文件
				file.createNewFile();
			}
			writer = new FileWriter(file);// 获取该文件的输出流
			writer.write(content);// 写内容
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				if(writer != null )
				{
					// 清空缓冲区，立即将输出流里的内容写到文件里
					writer.flush();
					// 关闭输出流，施放资源
					writer.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建ZIP文件
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public static void fileToZip(String sourcePath, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
		} catch (FileNotFoundException e) {
			log.info("创建ZIP文件失败", e);
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				log.info("创建ZIP文件失败", e);
			}

		}
	}

	/**
	 * 生成rar压缩文件（内部调用）
	 * 
	 * @param file
	 * @param parentPath
	 * @param zos
	 */
	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				for (File f : files) {
					writeZip(f, parentPath, zos);
				}
			} else {
				FileInputStream fis = null;
				DataInputStream dis = null;
				try {
					fis = new FileInputStream(file);
					dis = new DataInputStream(new BufferedInputStream(fis));
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[4096];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					log.info("创建ZIP文件失败", e);
				} catch (IOException e) {
					log.info("创建ZIP文件失败", e);
				} finally {
					try {
						if (dis != null) {
							dis.close();
						}
					} catch (IOException e) {
						log.info("创建ZIP文件失败", e);
					}
				}
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @throws IOException
	 */
	public static void downFile(String filePath, String fileName, HttpServletResponse response) throws IOException {
		// 设置Content-Disposition
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-disposition",
				"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		// 读取文件
		InputStream in = new FileInputStream(filePath);
		OutputStream out = response.getOutputStream();
		// 写文件
		byte[] b = new byte[4096];
		int bread;
		while ((bread = in.read(b)) != -1) {
			out.write(b, 0, bread);
		}
		in.close();
		out.close();
	}

	/**
	 * 复制备份
	 * 
	 * @throws IOException
	 */
	public static void copyFile(String fromFilePath, String toFilePath) throws IOException {
		// 读取文件
		InputStream in = new FileInputStream(fromFilePath);
		OutputStream out = new FileOutputStream(toFilePath);
		// 写文件
		byte[] b = new byte[4096];
		int bread = 0;

		while ((bread = in.read(b)) > 0) {
			out.write(b, 0, bread);
		}
		in.close();
		out.close();
	}

	public static void copyFolder(String path, String copyPath) throws IOException {
		File filePath = new File(path);
		DataInputStream read;
		DataOutputStream write;
		if (filePath.isDirectory()) {
			File[] list = filePath.listFiles();
			for (int i = 0; i < list.length; i++) {
				String newPath = path + File.separator + list[i].getName();
				String newCopyPath = copyPath + File.separator + list[i].getName();
				copyFolder(newPath, newCopyPath);
			}
		} else if (filePath.isFile()) {
			read = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
			write = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(copyPath)));
			byte[] buf = new byte[1024 * 512];
			while (read.read(buf) != -1) {
				write.write(buf);
			}
			read.close();
			write.close();
		} else {
			System.out.println("请输入正确的文件名或路径名");
		}
	}

	/**
	 * 文件里插入字符
	 *
	 */
	public static void WriteStringToFile(String fileName, long pos, String insertContent) throws IOException {
		File file = File.createTempFile("tmp", null);
		file.deleteOnExit();
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(pos);
		raf.writeBytes(insertContent);
		raf.close();
	}

	// 创建文件夹
	public static boolean makeDirs(String filePath) {
		File folder = new File(filePath);
		if(folder.exists()){
			delAllFile(filePath);
		}
		return folder.mkdirs();
	}

	// 删除指定文件夹下所有文件
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// 删除指定文件夹
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
