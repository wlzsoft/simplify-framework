package vip.simplify.encrypt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;

import vip.simplify.encrypt.symmetriccryptography.SymmetricBaseEncrypt;

/**
 * <p><b>Title:</b><i>DES,AES,BlowFish等对称加密算法的微基准测试(性能测试)</i></p>
* <p>Desc: TODO</p>
* <p>source folder:{@docRoot}</p>
* <p>Copyright:Copyright(c)2014</p>
* <p>Company:meizu</p>
* <p>Create Date:2016年3月9日 下午5:00:54</p>
* <p>Modified By:luchuangye-</p>
* <p>Modified Date:2016年3月9日 下午5:00:54</p>
* @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
* @version Version 0.1
*
*/
public class SymmetricEncryptPerformanceTest {

	@Test
	public void init() throws IOException {
		int size = 1;
		FileWriter fw = null;
		PrintWriter pw = null;
		for (int i = 0; i < 5; i++) {
			fw = new FileWriter(new File(SymmetricEncryptPerformanceTest.class.getResource("/").getPath()+"/"+size + "K.txt"), true);
			pw = new PrintWriter(new BufferedWriter(fw));
			for (int j = 0; j < size * 1024; j++) {
				pw.print(" ");
			}
			pw.close();
			fw.close();
			System.out.println(size + "K is done");
			size *= 10;
		}
	}

	@Test
	public void testDes() throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
		
        int size = 1;
        for (int i = 0; i < 5; i++) {
           String dataFileName = SymmetricEncryptPerformanceTest.class.getResource("/").getPath()+"/"+size + "K.txt";
            byte[] b = new byte[size * 1024];
            FileInputStream dataFIS = new FileInputStream(dataFileName);
            dataFIS.read(b);

            Cipher cipher = SymmetricBaseEncrypt.getEncryptCipher("sdferest".getBytes(), null, "DES", "ECB", "PKCS5Padding");
            long t1 = System.currentTimeMillis();
    		//System.out.println(ByteHexUtil.bytes2Hex(cipher.doFinal(b)));
            cipher.doFinal(b);
            System.out.println(System.currentTimeMillis() - t1);

            size *= 10;
            dataFIS.close();
        } 
	}
	
	@Test
	public void testAes() throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
		
        int size = 1;
        for (int i = 0; i < 5; i++) {
           String dataFileName = SymmetricEncryptPerformanceTest.class.getResource("/").getPath()+"/"+size + "K.txt";
            byte[] b = new byte[size * 1024];
            FileInputStream dataFIS = new FileInputStream(dataFileName);
            dataFIS.read(b);

            Cipher cipher = SymmetricBaseEncrypt.getEncryptCipher("sdferestsdferest".getBytes(), null, "AES", "ECB", "PKCS5Padding");
            long t1 = System.currentTimeMillis();
    		cipher.doFinal(b);
            System.out.println(System.currentTimeMillis() - t1);

            size *= 10;
            dataFIS.close();
        } 
	}
	
	@Test
	public void testBlowfish() throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
		
        int size = 1;
        for (int i = 0; i < 5; i++) {
           String dataFileName = SymmetricEncryptPerformanceTest.class.getResource("/").getPath()+"/"+size + "K.txt";
            byte[] b = new byte[size * 1024];
            FileInputStream dataFIS = new FileInputStream(dataFileName);
            dataFIS.read(b);
            
            Cipher cipher = SymmetricBaseEncrypt.getEncryptCipher("sdferestsdferest".getBytes(), null, "Blowfish", "ECB", "PKCS5Padding");
            long t1 = System.currentTimeMillis();
            cipher.doFinal(b);
            System.out.println(System.currentTimeMillis() - t1);

            size *= 10;
            dataFIS.close();
        } 
	}
}
