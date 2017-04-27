package vip.simplify.encrypt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

import vip.simplify.encrypt.file.FileDecrypt;
import vip.simplify.encrypt.file.FileEncrypt;
import vip.simplify.encrypt.sign.md5.MD5Encrypt;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: AES_256_CBC 256位加密算法-存在的问题和优势</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:56</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class EncryptTest {

	@Test
	public void testPasswordEncrypt() {
		System.out.println(new String(Keys.defaultFileKey()));
		System.out.println(PasswordEncrypt.passwordEncrypt("ros123"));
	}
	@Test
	public void testNum() {
		String num = "123456789";
		num = Encrypt.numEncrypt(num);
		print("加密=" + num);
		num = Decrypt.numDecrypt(num);
		print("解密=" + num);
		num = Encrypt.numEncrypt(num);
		print("加密=" + num);
	}
	
	@Test
	public void testFieldEncryptAndPassword() {

		String password = "123456";
		String adderss = "深圳市海王银河科技大厦 深圳市海王银河科技大厦";

		print("密码字段：" + password);
		print("中文内容字段：" + adderss);
		print("=================================");

		print("=============加密密码===========");
		String passwordEncrtyed = FieldEncrypt.fieldEncrypt("password", MD5Encrypt.hashMd5(password));
		print("密码加密结果1：" + passwordEncrtyed);
		passwordEncrtyed = PasswordEncrypt.passwordEncrypt(password);
		print("密码加密结果2：" + passwordEncrtyed);
		print("=============验证密码============");
		boolean passwordVerify = PasswordEncrypt.passwordVerify(password, passwordEncrtyed);
		print("密码验证结果：" + passwordVerify);

		print("=============加密中文内容============");
		String adderssEncrtyed = FieldEncrypt.fieldEncrypt("message", adderss);
		print("中文内容加密结果：" + adderssEncrtyed);
		print("=============解密中文内容============");
		String adderssDecrtyed = Decrypt.fieldDecrypt(adderssEncrtyed);
		print("中文内容解密结果：" + adderssDecrtyed);
	}
	@Test
	public void testFileEncrypt() {
		String key = "iuiserliselros";
		String no = "13014101";
		print("手机编码=" + no);
		print("秘钥=" + key);
		Keys.setNo(no);
		Keys.setKey(key);
		
		print("=================================");
		print("=============文件加解密===========");
		String file1 = "/EncryptTest.txt";
		InputStream in = this.getClass().getResourceAsStream(file1);
		String file2 = "/EncryptTest.enc";
		String file3 = "/EncryptTest.enc.unpack";//加密后的文件
		print("待加密文件：" + file1);
		print("加密后文件：" + file2);
		print("解密后文件：" + file3);
		byte[] keybyte = Keys.key;
		byte[] re = new byte[14];
		if(Arrays.equals(keybyte, re)) {
			keybyte = Keys.defaultFileKey();
		}
		byte[] filekey = Keys.calcConKey("ff", keybyte);
		print("文件加混淆秘钥：" + new String(filekey));
		byte[] rc4Key = Keys.calcRc4Key("ff", keybyte);
		print("文件加密秘钥：" + new String(rc4Key));
		print("===========开始加密文件===========");
		FileOutputStream out = null;

		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			out = new FileOutputStream(path+file2.substring(1));
			FileEncrypt.streamEncrypt(in, out, keybyte);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		print("===========开始解密文件===========");
		in = this.getClass().getResourceAsStream(file2);
		try {
			out = new FileOutputStream(path+file3.substring(1));
			FileDecrypt.streamDecrypt(in, out, keybyte);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		print("文件加解密完成");
	}

	private static void print(String s) {
		System.out.printf("%1$tF%1$tT: %2$s\r\n", new java.util.Date(), s);
	}
}
