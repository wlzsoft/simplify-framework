package com.meizu.simplify.utils;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.enums.DateFormatEnum;
import com.meizu.simplify.utils.enums.EncodingEnum;
import com.meizu.simplify.utils.enums.Measure;
import com.meizu.simplify.utils.enums.SpecialCharacterEnum;
/**
 * <p><b>Title:</b><i>文件操作工具类</i></p>
 * <p>Desc: 提供各种对文件系统进行操作的工具. 
 * 在应用程序中，经常需要对文件系统进行操作，例如复制、移动、删除文件，查找文件的路径，对文件进行写操作等， 类FileUtil提供了与此相关的各种工具。
 * </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月27日 下午1:07:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月27日 下午1:07:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class FileUtil {
	private static final int DEFAULT_BUFFER_SIZE = 4 * Measure.K;
	/**
	 * 构造方法，禁止实例化
	 */
	private FileUtil() {
	}
	/**
	 * 方法用途: 创建目录，如果父目录不存在则建立父目录, 类似目录格式 "E:/dd/aa/bb"<br>
	 * 操作步骤: TODO<br>
	 * @param targetDir  必须是绝对路径
	 */
	public static void createDirectory(String targetDir) {
		File dir = new File(targetDir);
		if (dir.exists()) {
			return;
		}
//		方式1：
		boolean success = dir.mkdirs();
		if(!success) {
			throw new RuntimeException("目录:[" + dir + "]创建失败，可能包含特殊字符!");
		}
//		方式二：
		/*dir.mkdir();
		createDirectory(dir.getParentFile().getAbsolutePath());*/
	}
	
	/**
	 * 方法用途: 从文件完整路径中截取完整的文件目录<br>
	 * 操作步骤: TODO<br>
	 * @param filePath 文件完整路径
	 * @return 返回从文件完整路径中截取完整的文件目录
	 */
	public static String getFullFileDir(String filePath) {
		int lastSlashIndex = filePath.lastIndexOf(SpecialCharacterEnum.DOUBLE_SLASH.toString());
		int lastBackSlashIndex = filePath.lastIndexOf(SpecialCharacterEnum.BACKSLASH.toString());
		// 如果没找到斜杠和反斜杠则返回空字符串
		// 如果斜杠位置在反斜杠位置之后则从斜杠位置截取文件目录
		// 否则从反斜杠位置截取文件目录
		if (lastSlashIndex == -1 && lastBackSlashIndex == -1) {
			return "";
		} else if (lastSlashIndex > lastBackSlashIndex) {
			return StringUtil.substringBeforeLast(filePath, SpecialCharacterEnum.DOUBLE_SLASH.toString());
		} else {
			return StringUtil.substringBeforeLast(filePath,SpecialCharacterEnum.BACKSLASH.toString());
		}
	}
	
	/**
	 * 方法用途: 创建一个新文件<br>
	 * 操作步骤: 根据文件的完整路径创建一个新文件,如果目录不存在时先创建目录再创建文件<br>
	 * @param targetPath 文件完整路径
	 * @return 返回创建的File文件对象
	 */
	public static File createFile(String targetPath) {
		try {
			String targetDir = getFullFileDir(targetPath);
			String fileName = targetPath.replace(targetDir, "");
			File file = createFile(targetDir,fileName,true);
			return file;
		} catch (IOException e) {
			throw new UncheckedException("创建文件时发生错误。", e);
		}
	}
	
	/**
	 * 方法用途: 创建一个新文件<br>
	 * 操作步骤: TODO<br>
	 * @param targetDir
	 * @param fileName
	 * @param overwrite
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String targetDir, String fileName, boolean overwrite) throws IOException {

		createDirectory(targetDir);

		File file = new File(targetDir, fileName);

		if (file.exists() && !overwrite) {
			throw new IOException("the file " + targetDir + " exists! But overwrite is FALSE!");
		}

		if (!file.exists()) {
			boolean createSuccess = file.createNewFile();
			if (!createSuccess) {
				throw new IOException("创建文件[ " + targetDir+fileName + "] 失败!");
			}
		}
		return file;

	}
	
	
	/**
	 * 方法用途: 保存数据流到文件中<br>
	 * 操作步骤: TODO<br>
	 * @param instream
	 * @param targetDir
	 * @param fileName
	 * @param overwrite
	 * @throws IOException
	 */
	public static void saveFile(InputStream instream, String targetDir, String fileName, boolean overwrite) throws IOException {

		File file = createFile(targetDir,fileName,overwrite);

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		BufferedInputStream binStream = new BufferedInputStream(instream);
		try {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int readCount = 0;
			while ((readCount = binStream.read(buffer)) != -1) {
				if (readCount < DEFAULT_BUFFER_SIZE) {
					out.write(buffer, 0, readCount);
				} else {
					out.write(buffer);
				}
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if(binStream != null) {
				binStream.close();
			}
		}
	}
	
	

	/**
	 * 方法用途: 保存文本到文件中<br>
	 * 操作步骤: TODO<br>
	 * @param targetFile
	 * @param content 添加到文件中的文本内容
	 * @throws Exception
	 */
	public static void saveFile(File targetFile, String content) {
		OutputStreamWriter fw = null;
		PrintWriter out = null;
		try {
			fw = new OutputStreamWriter(new FileOutputStream(targetFile), EncodingEnum.UTF_8.toString());
			out = new PrintWriter(fw);
			out.print(content);
		} catch (Exception ex) {
			throw new UncheckedException(ex);
		} finally {
			if (out != null) {
				out.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static boolean updateFile(String fileString, String filePath,String encoding) {
		boolean flag = false;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
			bw.append(fileString);
			bw.flush();
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	/**
	 * 方法用途: 保存图片文件到硬盘上<br>
	 * 操作步骤: TODO<br>
	 * @param in
	 * @param path
	 * @param fileName
	 * @param formatName
	 * @param overwrite
	 * @throws IOException
	 */
	public static void saveImage(InputStream in, String path, String fileName, String formatName, boolean overwrite) throws IOException {

		createDirectory(path);
		File f = new File(path, fileName);

		if (f.exists() && !overwrite) {
			throw new IOException("the file " + path + " exists! But overwrite is FALSE!");
		}

		BufferedImage bi = ImageIO.read(in);
		if (bi == null) {
			throw new IOException("从InputStream " + in + " 读取图像数据时失败！");
		}

		boolean success = ImageIO.write(bi, formatName, f);
		if (!success) {
			throw new IOException("把文件保存为 " + formatName + " 格式时失败！");
		}
	}

	/**
	 * 方法用途: 保存图片文件到硬盘上<br>
	 * 操作步骤: TODO<br>
	 * @param im
	 * @param path
	 * @param fileName
	 * @param formatName
	 * @param overwrite
	 * @throws IOException
	 */
	public static void saveImage(RenderedImage im, String path, String fileName, String formatName, boolean overwrite) throws IOException {

		createDirectory(path);

		File f = new File(path, fileName);

		if (f.exists() && !overwrite) {
			throw new IOException("the file " + path + " exists! But overwrite is FALSE!");
		}

		boolean success = ImageIO.write(im, formatName, f);

		if (!success) {
			throw new IOException("把文件保存为 " + formatName + " 格式时发生异常！");
		}
	}



	/**
	 * 方法用途: 路径目录分隔符转换<br>
	 * 操作步骤: File自带的File.separatorChar可以实现这个方法的功能，但是一些外来的地址，会出现路径的格式不可控的问题，所以需要做路径转换<br>
	 * @param path
	 * @return
	 */
	public static String separatorCharChange(String path) {
		if (path == null) {
			return null;
		}
		String s = System.getProperty(SpecialCharacterEnum.FILE_SEPARATOR.toString());
		String WIN = "\\";
		String UNIX = "/";

		String regex = null;
		String replacement = null;

		if (s.equals(WIN)) {
			regex = "[/]{1,}";
			replacement = "\\\\";
		} else if (s.equals(UNIX)) {
			regex = "[\\\\]{1,}";
			replacement = SpecialCharacterEnum.BACKSLASH.toString();
		} else {
			throw new IllegalStateException("unknown FILE_SEPARATOR, / OR \\ expected!");
		}

		String result = path.replaceAll(regex, replacement);
		return result;
	}
	
	/**
	 * 方法用途: 复制文件,并设置编码<br>
	 * 操作步骤: TODO<br>
	 * @param srcFilePath
	 * @param destFilePath
	 * @param srcCoding
	 * @param destCoding
	 * @return
	 */
	public static boolean copyFile(String srcFilePath, String destFilePath,String srcCoding, String destCoding) {
		boolean flag = false;
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					srcFilePath), srcCoding));
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(destFilePath), destCoding));
			char[] cbuf = new char[1024 * 5];
			int len = cbuf.length;
			int off = 0;
			int ret = 0;
			while ((ret = br.read(cbuf, off, len)) > 0) {
				off += ret;
				len -= ret;
			}
			bw.write(cbuf, 0, off);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (bw != null)
					bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	/**
	 * 
	 * 方法用途: 复制文件<br>
	 * 操作步骤: TODO<br>
	 * @param sourceFile
	 * @param targetFile
	 * @return number of byte written
	 * @throws IOException
	 */
	public static long copyFile(String sourceFile, String targetFile)  {
		return copyFile(new File(sourceFile),new File(targetFile));
	}
	
	/**
	 * 方法用途: 拷贝源文件到目标文件<br>
	 * 操作步骤: TODO<br>
	 * @param sourceFile 源文件地址
	 * @param targetFile 目标文件地址
	 * @return
	 * @throws Exception
	 */
	public static long copyFile(File sourceFile, File targetFile) {
		if (sourceFile == null || targetFile == null || !sourceFile.isFile())
			return -1;
//		boolean flag = false;
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		long size = 0;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			// 缓冲数组
			byte[] b = new byte[DEFAULT_BUFFER_SIZE];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
				size += len;
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
//			flag = true;
		} catch (Exception e) {
			size = 0;
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return size;
//		return flag;
	}
	/**
	 * 方法用途: 复制文件夹<br>
	 * 操作步骤: TODO<br>
	 * @param sourceDirPath
	 * @param targetDirPath
	 * @param sourceDestCoding
	 * @param targetDestCoding
	 * @return
	 */
	public static boolean copyDirectiory(String sourceDirPath,
			String targetDirPath, String sourceDestCoding,
			String targetDestCoding) {
		boolean flag = false;
		try {
			// 创建目标文件夹
			(new File(targetDirPath)).mkdirs();
			// 获取源文件夹当前下的文件或目录
			File[] file = (new File(sourceDirPath)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					// 复制文件
					String type = file[i].getName().substring(
							file[i].getName().lastIndexOf(".") + 1);

					if (type.equalsIgnoreCase("txt"))
						copyFile(file[i].getAbsolutePath(), targetDirPath
								+ file[i].getName(), sourceDestCoding,
								targetDestCoding);
					else
						copyFile(file[i].getAbsolutePath(), targetDirPath
								+ file[i].getName());
				}
				if (file[i].isDirectory()) {
					// 复制目录
					String sourceDir = sourceDirPath + File.separator
							+ file[i].getName();
					String targetDir = targetDirPath + File.separator
							+ file[i].getName();
					copyDirectiory(sourceDir, targetDir);
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 方法用途: This class copies an input files of a directory to another directory not include subdir<br>
	 * 操作步骤: TODO<br>
	 * @param sourceDir sourceDir the directory to copy from such as:/home/bqlr/images
	 * @param targetDir targetDir the target directory
	 * @throws Exception
	 */
	public static void copyDir(String sourceDir, String targetDir) throws Exception {
		File dest = new File(targetDir);
		File source = new File(sourceDir);

		String[] files = source.list();
		try {
			createDirectory(targetDir);
		} catch (Exception ex) {
			throw new Exception("CopyDir:" + ex.getMessage());
		}

		for (int i = 0; i < files.length; i++) {
			String sourcefile = source + File.separator + files[i];
			String destfile = dest + File.separator + files[i];
			File temp = new File(sourcefile);
			if (temp.isFile()) {
				try {
					copyFile(sourcefile, destfile);
				} catch (Exception ex) {
					throw new Exception("CopyDir:" + ex.getMessage());
				}
			}
		}
	}
	
	/**
	 * 方法用途: 复制文件夹<br>
	 * 操作步骤: TODO<br>
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */
	public static boolean copyDirectiory(String sourceFilePath,
			String targetFilePath) {
		boolean flag = false;
		try {
			// 新建目标目录
			(new File(sourceFilePath)).mkdirs();
			// 获取源文件夹当前下的文件或目录
			File[] file = (new File(sourceFilePath)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					// 源文件
					File sourceFile = file[i];
					// 目标文件
					File targetFile = new File(new File(targetFilePath)
							.getAbsolutePath()
							+ File.separator + file[i].getName());
					copyFile(sourceFile.getAbsolutePath(), targetFile
							.getAbsolutePath());
				}
				if (file[i].isDirectory()) {
					// 准备复制的源文件夹
					String dir1 = sourceFilePath + "/" + file[i].getName();
					// 准备复制的目标文件夹
					String dir2 = targetFilePath + "/" + file[i].getName();
					copyDirectiory(dir1, dir2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	
	/**
	 * 方法用途: 将输入流复制到输出流<br>
	 * 操作步骤: TODO<br>
	 * @param input 输入流
	 * @param output 输出流
	 */
	public static void copyInToOut(InputStream input, OutputStream output) {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		try {
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			throw new UncheckedException("从输入流复制到输出流时发生异常", e);
		}
	}
	
	/**
	 * 方法用途: 剪切文件夹<br>
	 * 操作步骤: TODO<br>
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */
	public static boolean cutDirectiory(String sourceFilePath,
			String targetFilePath) {
		boolean flag = false;
		if (copyDirectiory(sourceFilePath, targetFilePath)) {
			flag = deleteDirectory(sourceFilePath);
		}
		return flag;
	}
	
	
	/**
	 * 方法用途: 剪切文件<br>
	 * 操作步骤: TODO<br>
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */
	public static boolean cutFile(String sourceFilePath, String targetFilePath) {
		boolean flag = false;
		if (copyFile(sourceFilePath, targetFilePath)>0) {
			flag = deleteFile(sourceFilePath);
		}
		return flag;
	}
	
	/**
	 * 方法用途:  This class moves an input file to output file<br>
	 * 操作步骤: TODO<br>
	 * @param input input file to move from
	 * @param output output file
	 * @throws Exception
	 */
	public static void move(String input, String output) throws Exception {
		File inputFile = new File(input);
		File outputFile = new File(output);
		try {
			inputFile.renameTo(outputFile);
		} catch (Exception ex) {
			throw new Exception("Can not mv" + input + " to " + output + ex.getMessage());
		}
	}

	/**
	 * 方法用途: Move file<br>
	 * 操作步骤: TODO<br>
	 * @param from
	 * @param to
	 * @return number of byte written
	 * @throws IOException
	 */
	public static long moveFile(File from, File to) throws IOException {
		if (from == null || to == null || !from.isFile())
			return -1;

		long size = 0;
		if (from.getParent().equals(to.getParent())) {
			from.renameTo(to);
			FileInputStream fis = new FileInputStream(to);
			size = fis.available();
			fis.close();
		} else {
			size = copyFile(from, to);
			from.delete();
		}

		return size;
	}

	/**
	 * 方法用途: File system operation, delete one directory recursively<br>
	 * 操作步骤: TODO<br>
	 * @param dir
	 */
	public static void deleteDirectory(File dir) {
		if (dir == null || !dir.isDirectory())
			return;

		File[] children = dir.listFiles();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (children[i].isFile()) {
					children[i].delete();
				} else if (children[i].isDirectory()) {
					deleteDirectory(children[i]);
				}
			}
		}
		dir.delete();
	}
	

	/**
	 * 方法用途: 删除给定的文件或者目录<br>
	 * 操作步骤: TODO<br>
	 * @param targetFile
	 * @param ignoreFileNotFoundException
	 * @throws FileNotFoundException 如果ignoreFileNotFoundException为true，当找不到给定的文件或者目录时，忽略这个异常；否则，抛出这个异常
	 */
	public static void deleteFile(String targetFile, boolean ignoreFileNotFoundException) throws FileNotFoundException {

		File f = new File(targetFile);

		if (!f.exists()) {
			if (ignoreFileNotFoundException) {
				return;
			} else {
				throw new FileNotFoundException("path: " + targetFile);
			}
		}

		if (f.isFile()) {
			boolean success = f.delete();

			if (!success) {
				throw new RuntimeException("delete file " + f + " failed!");
			}

		} else if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; fs != null && i < fs.length; i++) {
				deleteFile(fs[i].getPath(), ignoreFileNotFoundException);
			}

			boolean success = f.delete();
			if (!success) {
				throw new RuntimeException("delete file " + f + " failed!");
			}
		}
	}
	
	/**
	 * 删除文件方法，如果删除不掉，将该文件加入删除池，下次进行调用时将尝试删除池中的文件
	 * 
	 * @param file
	 *            file
	 */
	public static void deleteFile(File file) {
		file.delete();// 尝试删除文件
		if (file.exists()) {
			deleteFilesPool.add(file);
		}
		checkDeletePool();
	}
	
	/**  
	 * 根据路径删除指定的目录或文件，无论存在与否  
	 *@param sPath  要删除的目录或文件  
	 *@return 删除成功返回 true，否则返回 false
	 */
	public boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在   
		if (!file.exists()) { // 不存在返回 false   
			return flag;
		} else {
			// 判断是否为文件   
			if (file.isFile()) { // 为文件时调用删除文件方法   
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法   
				return deleteDirectory(sPath);
			}
		}
	}
	/**  
	 * 删除单个文件  
	 * @param   sPath    被删除文件的文件名  
	 * @return 单个文件删除成功返回true，否则返回false  
	 */
	public static boolean deleteFile(String sourceFilePath) {
		boolean flag = false;
		File file = new File(sourceFilePath);
		// 路径为文件且不为空则进行删除   
		if (file.isFile() && file.exists()) {
			file.delete();
//			file.deleteOnExit();
			flag = true;
		}
		return flag;
	}

	/**  
	 * 删除目录（文件夹）以及目录下的文件  
	 * @param   sPath 被删除目录的文件路径  
	 * @return  目录删除成功返回true，否则返回false  
	 */
	public static boolean deleteDirectory(String sourceFilePath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符   
		if (!sourceFilePath.endsWith(File.separator)) {
			sourceFilePath = sourceFilePath + File.separator;
		}
		File dirFile = new File(sourceFilePath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出   
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)   
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件   
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} //删除子目录   
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		//删除当前目录   
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查池，删除池中文件，如果删除成功则同时从池中移除。
	 */
	private static void checkDeletePool() {
		File file;
		for (int i = deleteFilesPool.size() - 1; i >= 0; i--) {
			file = (File) deleteFilesPool.get(i);
			file.delete();
			if (file.exists() == false) {
				deleteFilesPool.remove(i);
			}
		}
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(POOL_FILE));
			out.writeObject(deleteFilesPool);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 删除文件夹（不管是否文件夹为空）<br>
	 * 注意：非原子操作，删除文件夹失败时，并不能保证没有文件被删除。 * <br>
	 * <b>示例: </b> <br>
	 * FileUtils.deleteFolder(&quot;/home/tmp&quot;) 删除成功返回true.<br>
	 * FileUtils.deleteFolder(&quot;C:\\test&quot;) 删除成功返回true.</br>
	 * 
	 * @param delFolder
	 *            待删除的文件夹
	 * @return 如果删除成功则返回true，否则返回false
	 */
	public static boolean deleteFolder(File delFolder) {
		// 目录是否已删除
		boolean hasDeleted = true;
		// 得到该文件夹下的所有文件夹和文件数组
		File[] allFiles = delFolder.listFiles();

		for (int i = 0; i < allFiles.length; i++) {
			// 为true时操作
			if (hasDeleted) {
				if (allFiles[i].isDirectory()) {
					// 如果为文件夹,则递归调用删除文件夹的方法
					hasDeleted = deleteFolder(allFiles[i]);
				} else if (allFiles[i].isFile()) {
					try {// 删除文件
						if (!allFiles[i].delete()) {
							// 删除失败,返回false
							hasDeleted = false;
						}
					} catch (Exception e) {
						// 异常,返回false
						hasDeleted = false;
					}
				}
			} else {
				// 为false,跳出循环
				break;
			}
		}
		if (hasDeleted) {
			// 该文件夹已为空文件夹,删除它
			delFolder.delete();
		}
		return hasDeleted;
	}

	/**
	 * 方法用途: 如果给定目录为空，就删除此目录<br>
	 * 操作步骤: TODO<br>
	 * @param targetDir
	 */
	public static void deleteIfEmptyDir(File targetDir) {

		if (targetDir == null || !targetDir.isDirectory()) {
			return;
		}

		File[] fs = targetDir.listFiles();
		if (fs == null || fs.length == 0) {
			targetDir.delete();
		}
	}
	
	/**
	 * 方法用途: This class del a directory recursively,that means delete all files and directorys<br>
	 * 操作步骤: TODO<br>
	 * @param directory directory the directory that will be deleted
	 * @throws Exception 
	 */
	public static void recursiveRemoveDir(File directory) throws Exception {
		if (!directory.exists())
			throw new IOException(directory.toString() + " do not exist!");

		String[] filelist = directory.list();
		File tmpFile = null;
		for (int i = 0; i < filelist.length; i++) {
			tmpFile = new File(directory.getAbsolutePath(), filelist[i]);
			if (tmpFile.isDirectory()) {
				recursiveRemoveDir(tmpFile);
			} else if (tmpFile.isFile()) {
				try {
					tmpFile.delete();
				} catch (Exception ex) {
					throw new Exception(tmpFile.toString() + " can not be deleted " + ex.getMessage());
				}
			}
		}
		try {
			directory.delete();
		} catch (Exception ex) {
			throw new Exception(directory.toString() + " can not be deleted " + ex.getMessage());
		} finally {
			filelist = null;
		}
	}

	/**
	 * 方法用途: 给定一个文件路径，提取其中的文件名和扩展名，要求必须给定扩展名<br>
	 * 操作步骤: 
	 * 例如，给定/a/b.c，返回b.c
	 * 如果给定/a/b.c.d，返回b.c.d
	 * 如果给定/a/b/c，返回null<br>
	 * @param path
	 * @return
	 */
	public static String getFileNameAndExt(String path) {

		String DOT = ".";
		String S1 = "/";
		String S2 = "\\";

		if (path == null || path.trim().equals("")) {
			return null;
		}

		int slash = path.lastIndexOf(S1);
		if (slash == -1) {
			slash = path.lastIndexOf(S2);
		}

		String nameAndExt = path.substring(slash + 1);
		if (nameAndExt.contains(DOT)) {
			return nameAndExt;
		}

		return null;
	}

	/**
	 * 方法用途: 给定一个文件路径，提取其中的文件名和扩展名，要求必须给定扩展名<br>
	 * 操作步骤: 
	 * 例如，给定/a/b.c，返回[b, c]
	 * 
	 * 如果给定/a/b.c.d，返回[b.c, d]
	 * 
	 * 如果给定/a/b/c，返回null<br>
	 * @param path
	 * @return
	 */
	public static String[] getFileNameAndExt2(String path) {

		String DOT = ".";

		String resultString = getFileNameAndExt(path);

		if (resultString == null) {
			return null;
		}

		int dot = resultString.lastIndexOf(DOT);

		String name = resultString.substring(0, dot);
		String ext = resultString.substring(dot + 1);

		return new String[] { name, ext };
	}

	/**
	 * read the content from a file;
	 * 
	 * @param output
	 * @param content
	 * @throws Exception
	 */
	public static String readFile(String input) throws Exception {
		char[] buffer = new char[4096];
		int len = 0;
		StringBuffer content = new StringBuffer(4096);

		InputStreamReader fr = null;
		BufferedReader br = null;
		try {
			fr = new InputStreamReader(new FileInputStream(input), EncodingEnum.UTF_8.toString());
			br = new BufferedReader(fr);
			while ((len = br.read(buffer)) > -1) {
				content.append(buffer, 0, len);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (br != null)
				br.close();
			if (fr != null)
				fr.close();
		}
		return content.toString();
	}

	public static byte[] readFile2Bytes(String fileName) throws IOException {
		FileInputStream in = new FileInputStream(fileName);
		int size = in.available();
		byte[] bytes = new byte[size];
		in.read(bytes);
		in.close();
		return bytes;
	}

	public static List<String> readFileConstant(String fileName) {
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader d = new BufferedReader(new FileReader(fileName));
			String s = d.readLine();
			while (s != null) {
				s = s.trim();
				if (s.equals("")) {
					s = d.readLine();
					continue;
				}
				result.add(s);
				s = d.readLine();
			}
			d.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String readFromFile(String filename, String encode) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			byte[] buf = new byte[fis.available()];
			fis.read(buf);
			fis.close();
			return new String(buf, encode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 得到文件内容
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 文件内容
	 * @throws Exception
	 */
	public static String read(String fileName) throws Exception {
		return read(new File(fileName));
	}

	/**
	 * 得到文件内容
	 * 
	 * @param file
	 *            文件
	 * @return 文件内容
	 * @throws Exception
	 */
	public static String read(File file) throws Exception {
		String fileContent = "";
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			fileContent = read(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return fileContent;
	}

	/**
	 * 得到输入流的内容
	 * 
	 * @param is
	 *            输入流
	 * @return 字符串
	 * @throws Exception
	 */
	public static String read(InputStream is) throws Exception {
		byte[] result = readBytes(is);
		return new String(result);
	}

	/**
	 * 以byte数组方式得到输入流的内容
	 * 
	 * @param fileName
	 *            文件名称
	 * @return byte数组
	 * @throws Exception
	 */
	public static byte[] readBytes(String fileName) throws Exception {
		return readBytes(new FileInputStream(fileName));
	}

	/**
	 * 以byte数组方式得到输入流的内容
	 * 
	 * @param file
	 *            文件
	 * @return byte数组
	 * @throws Exception
	 */
	public static byte[] readBytes(File file) throws Exception {
		return readBytes(new FileInputStream(file));
	}

	/**
	 * 以byte数组方式得到输入流的内容
	 * 
	 * @param is
	 *            输入流
	 * @return byte数组
	 * @throws Exception
	 */
	public static byte[] readBytes(InputStream is) throws Exception {
		if (is == null || is.available() < 1) {
			return new byte[0];
		}
		byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
		byte[] result = new byte[is.available()];
		int nch;
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(is);
			int pos = 0;
			while ((nch = in.read(buff, 0, buff.length)) != -1) {
				System.arraycopy(buff, 0, result, pos, nch);
				pos += nch;
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return result;
	}
	
//	-----------------------------------------------
	

	/**
	 * 在已有文件名后增加一个以精确到毫秒的当前时间戳生成随机文件名。
	 * 
	 * @param fileName
	 *            已有文件名
	 * @return 返回生成的随机文件名。
	 */
	public static String getRandomFileName(String fileName) {
		return getFileName(fileName) + "-"
				+ DateUtil.format(new Date(), DateFormatEnum.YEAR_TO_MILLISECOND_N)
				+ getFileType(fileName);
	}

	/**
	 * 从带有类型后缀的文件名中获取不带类型后缀的文件名。
	 * 
	 * @param fileName
	 *            带有类型后缀的文件名
	 * @return 返回不带类型后缀的文件名。
	 */
	public static String getFileName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * 从带有类型后缀的文件名中获取文件名的类型后缀。
	 * 
	 * @param fileName
	 *            带有类型后缀的文件名
	 * @return 返回文件名的类型后缀。
	 */
	public static String getFileType(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 从文件完整路径中截取完整的文件名。
	 * 
	 * @param filePath
	 *            文件完整路径
	 * @return 返回从文件完整路径中截取完整的文件名。
	 */
	public static String getFullFileName(String filePath) {
		int lastSlashIndex = filePath.lastIndexOf(SpecialCharacterEnum.DOUBLE_SLASH.toString());
		int lastBackSlashIndex = filePath.lastIndexOf(SpecialCharacterEnum.BACKSLASH.toString());
		// 如果没找到斜杠和反斜杠则返回原文件路径
		// 如果斜杠位置在反斜杠位置之后则从斜杠位置截取文件名
		// 否则从反斜杠位置截取文件名
		if (lastSlashIndex == -1 && lastBackSlashIndex == -1) {
			return filePath;
		} else if (lastSlashIndex > lastBackSlashIndex) {
			return StringUtil.substringAfterLast(filePath, SpecialCharacterEnum.DOUBLE_SLASH.toString());
		} else {
			return StringUtil.substringAfterLast(filePath,
					SpecialCharacterEnum.BACKSLASH.toString());
		}
	}

	/**
	 * 将输入流转换为字节数组。
	 * 
	 * @param in
	 *            输入流
	 * @return 返回字节数组。
	 */
	public static byte[] toByteArray(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			copyInToOut(in, out);
			byte[] bytes = out.toByteArray();
			in.close();
			out.close();
			return bytes;
		} catch (Exception e) {
			throw new UncheckedException("将文件转换成字节数组时发生异常", e);
		}
	}

	/**
	 * 将一个文件转化为字节数组
	 * 
	 * @param file
	 *            文件
	 * @return 返回字节数组。
	 */
	public static byte[] toByteArray(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			return toByteArray(in);
		} catch (FileNotFoundException e) {
			throw new UncheckedException("将文件转换成字节数组时发生异常", e);
		}
	}

	//------------------------------------
	
	private static final File POOL_FILE = ClassPathUtil.getUniqueFile(FileUtil.class, ".deletefiles");

	private static ArrayList<File> deleteFilesPool;
	static {
		try {
			initPool();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读出以前未删除的文件列表
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void initPool() {
		if (POOL_FILE.exists() && POOL_FILE.canRead()) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(POOL_FILE));
				deleteFilesPool = (ArrayList<File>) in.readObject();

			} catch (Exception e) {
				deleteFilesPool = new ArrayList<File>();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {// do nothing

					}
				}
			}
		} else {
			deleteFilesPool = new ArrayList<File>();
		}

	}

	/**
	 * 得到短文件名. <br>
	 * <br>
	 * <b>示例: </b> <br>
	 * FileUtils.getShortFileName(&quot;/home/app/config.xml&quot;) 返回
	 * &quot;config.xml&quot;
	 * FileUtils.getShortFileName(&quot;C:\\test\\config.xml&quot;) 返回
	 * &quot;config.xml&quot;</br>
	 * 
	 * @param fileName
	 *            文件名
	 * @return 短文件名
	 */
	public static String getShortFileName(String fileName) {
		String shortFileName = "";
		int pos = fileName.lastIndexOf('\\');
		if (pos == -1) {
			pos = fileName.lastIndexOf('/');
		}
		if (pos > -1) {
			shortFileName = fileName.substring(pos + 1);
		} else {
			shortFileName = fileName;
		}
		return shortFileName;
	}

	/**
	 * 得到不带扩展名的短文件名. <br>
	 * <br>
	 * <b>示例: </b> <br>
	 * FileUtils.getShortFileNameWithoutExt(&quot;/home/app/config.xml&quot;) 返回
	 * &quot;config&quot;<br>
	 * FileUtils.getShortFileNameWithoutExt(&quot;C:\\test\\config.xml&quot;) 返回
	 * &quot;config&quot;</br>
	 * 
	 * @param fileName
	 *            文件名
	 * @return 短文件名
	 */
	public static String getShortFileNameWithoutExt(String fileName) {
		String shortFileName = getShortFileName(fileName);
		shortFileName = getFileNameWithoutExt(shortFileName);
		return shortFileName;
	}

	/**
	 * 返回不带扩展名的文件名
	 * 
	 * @param fileName
	 *            原始文件名
	 * @return 不带扩展名的文件名
	 */
	public static String getFileNameWithoutExt(String fileName) {
		String shortFileName = fileName;
		if (fileName.indexOf('.') > -1) {
			shortFileName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return shortFileName;
	}

	/**
	 * 返回文件扩展名,带“.”
	 * 
	 * @param fileName
	 *            原始文件名
	 * @return 文件扩展名
	 */
	public static String getFileNameExt(String fileName) {
		String fileExt = "";
		if (fileName.indexOf('.') > -1) {
			fileExt = fileName.substring(fileName.lastIndexOf('.'));
		}
		return fileExt;
	}

	/**
	 * 得到唯一文件
	 * 
	 * @param fileName
	 *            原始文件名
	 * @return File
	 */
	public synchronized static File getUniqueFile(File repository, String fileName) {
		String shortFileName = getShortFileName(fileName);
		String tempFileName = getFileNameWithoutExt(shortFileName);
		File file = new File(repository, shortFileName);
		String fileExt = getFileNameExt(shortFileName);
		while (file.exists()) {
			file = new File(repository, tempFileName + "-" + Math.abs(Math.random() * 1000000) + fileExt);
		}
		return file;
	}
		
	/**
	 * 获得 机器上 所有磁盘
	 * 
	 * @return
	 */
	public static List<String> getDiskPath() {
		List<String> disks = new ArrayList<String>();
		File[] fs = File.listRoots();
		for (int i = 0; i < fs.length; i++) {
			disks.add(fs[i].getPath());
		}
		return disks;
	}

	/**
	 * 递归读取文件下面的所有文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<File> listFiles(String filePath) {
		return listFiles(filePath, null);
	}

	/**
	 * 递归读取文件下面的满足过滤器要求的文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<File> listFiles(String filePath, FileFilter filter) {
		List<File> fileList = new ArrayList<File>();
		try {
			File file = new File(filePath);
			File[] files = null;
			if (null != filter) {
				files = file.listFiles(filter);
			} else {
				files = file.listFiles();
			}
			for (int i = 0; i < files.length; i++) {
				File tempFile = files[i];
				if (tempFile.isDirectory()) {
					String subDirName = tempFile.getPath();
					List<File> list = null;
					list = listFiles(subDirName, filter);
					for (int j = 0; j < list.size(); j++) {
						fileList.add(list.get(j));
					}
				} else {
					if (!tempFile.isFile()) {
						continue;
					}
					fileList.add(tempFile);
				}
				if (i == (files.length - 1)) {
					return fileList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fileList;
	}

	/**
	 * 用zip格式压缩文件
	 * @param zipFileName
	 *            压缩后的文件名 包含路径 如："c:\\test.zip"
	 * @param inputFile
	 *            要压缩的文件 可以是文件或文件夹 如："c:\\test" 或 "c:\\test.doc"
	 * @throws Exception
	 *             ant下的zip工具默认压缩编码为UTF-8编码，
	 *             而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
	 *             用ant压缩后放到windows上面会出现中文文件名乱码，用winRAR压缩的文件，用ant解压也会出现乱码，
	 *             所以，在用ant处理winRAR压缩的文件时，要设置压缩编码
	 */
	public static boolean zip(String sourceFilePath, String targetFileName) {
		boolean flag = false;
		try {
			File f = new File(sourceFilePath);
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFileName));
			// 设置压缩编码
			// out.setEncoding("GBK");//设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
			flag = zip(out, f, "");// 递归压缩方法
			out.close();
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean zip(ZipOutputStream out, File f, String base) {
		try {
			if (f.isDirectory()) { // 如果是文件夹，则获取下面的所有文件
				File[] fl = f.listFiles();
				out.putNextEntry(new ZipEntry(base + "/"));// 此处要将文件写到文件夹中只用在文件名前加"/"再加文件夹名
				base = base.length() == 0 ? "" : base + "/";
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], base + fl[i].getName());
				}
			} else { // 如果是文件，则压缩
				out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点
				FileInputStream in = new FileInputStream(f); // 读取文件内容
				int len;
				byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
				while ((len = in.read(buf, 0, DEFAULT_BUFFER_SIZE)) != -1) {
					out.write(buf, 0, len); // 写入到压缩包
				}
				in.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * 解压缩zip文件，支持中文
	 * 
	 * @param zipFilePath
	 *            要解压的zip文件对象
	 * @param outputFileName
	 *            要解压到某个指定的目录下
	 * @throws IOException
	 * 
	 */
	public static boolean unZip(String zipFilePath, String outputFileName) {
		return unZip(zipFilePath, outputFileName, false);
	}

	/**
	 * 
	 * 解压缩zip文件，支持中文
	 * 
	 * @param zipFilePath
	 *            要解压的zip文件对象
	 * @param outputFileName
	 *            要解压到某个指定的目录下
	 * @param deleteFile
	 *            解压完毕后,是否同时删除压缩文件
	 * @throws IOException
	 * 
	 */
	public static boolean unZip(String zipFilePath, String outputFileName, boolean deleteFile) {
		try {
			File file = new File(zipFilePath);
			if (!file.exists()) {
				return false;
			}
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zipFile.entries();
			while (e.hasMoreElements()) {
				ZipEntry zipEntry = e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputFileName + name);
					f.mkdirs();
				} else {
					File f = new File(outputFileName + zipEntry.getName());
					f.getParentFile().mkdirs();
					f.createNewFile();
					InputStream is = zipFile.getInputStream(zipEntry);
					FileOutputStream fos = new FileOutputStream(f);
					int length = 0;
					byte[] b = new byte[DEFAULT_BUFFER_SIZE];
					while ((length = is.read(b, 0, DEFAULT_BUFFER_SIZE)) != -1) {
						fos.write(b, 0, length);
					}
					is.close();
					fos.close();
				}
			}

			if (zipFile != null) {
				zipFile.close();
			}

			if (deleteFile) {
				file.deleteOnExit();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取文件中内容
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String path, String destCoding) throws IOException {
		String resultStr = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			byte[] inBuf = new byte[DEFAULT_BUFFER_SIZE];
			int len = inBuf.length;
			int off = 0;
			int ret = 0;
			while ((ret = fis.read(inBuf, off, len)) > 0) {
				off += ret;
				len -= ret;
			}
			resultStr = new String(new String(inBuf, 0, off, destCoding).getBytes());
		} finally {
			if (fis != null)
				fis.close();
		}
		return resultStr;
	}

	/**
	 * 文件转成字节数组
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileToBytes(String path) throws IOException {
		byte[] b = null;
		InputStream is = null;
		File f = new File(path);
		try {
			is = new FileInputStream(f);
			b = new byte[(int) f.length()];
			is.read(b);
		} finally {
			if (is != null)
				is.close();
		}
		return b;
	}

	/**
	 * 文件续传
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 */
	public static boolean runUnFinished(String srcFilePath, String destFilePath) {
		boolean flag = false;
		File destFile = new File(destFilePath);
		if (destFile.exists()) {
			FileChannel fin = null;
			FileChannel fout = null;
			RandomAccessFile randomSrcFile = null;
			RandomAccessFile randomDestFile = null;
			try {
				randomSrcFile = new RandomAccessFile(srcFilePath, "rw");
				long destFileSize = destFile.length();
				randomDestFile = new RandomAccessFile(destFile, "rw");
				randomSrcFile.seek(destFileSize - 1024 * 2);
				randomDestFile.seek(destFileSize - 1024 * 2);
				fin = randomSrcFile.getChannel();
				fout = randomDestFile.getChannel();
				while (true) {
					int length = 1024 * 1024 * 2;

					if (fin.position() == fin.size()) {
						fin.close();
						fout.close();
						break;
					}
					if ((fin.size() - fin.position()) < 1024 * 1024 * 2 * 10) {
						length = (int) (fin.size() - fin.position());
					} else {
						length = 1024 * 1024 * 2 * 10;
					}
					fin.transferTo(fin.position(), length, fout);
					fin.position(fin.position() + length);
				}
				flag = true;
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			} finally {
				try {
					if(randomSrcFile != null) {
						randomSrcFile.close();
					}
					if(randomDestFile != null) {
						randomDestFile.close();
					}
					if (null != fin) {
						fin.close();
					}
					if (null != fout) {
						fout.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
		
	public static String getFileExtName(File file) {
		String fileName = file.getName();
		String extName = fileName.substring(fileName.lastIndexOf('.') + 1);
		return extName;
	}
}