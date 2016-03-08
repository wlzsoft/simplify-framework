package com.meizu.simplify.encrypt.des;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.meizu.simplify.encrypt.base64.Base64Encoder;
import com.meizu.simplify.encrypt.base64.ByteHexUtil;

/**
 * 
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:41:13</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:41:13</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class DESMessageEncrypt {
	public byte[] encode(byte[] input) {
		try {
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.ENCRYPT_MODE, getSecretKey());
			byte[] codes =  c1.doFinal(input);
//			return  StringUtil.bytes2Hex(codes).getBytes();
			return  ByteHexUtil.bytes2Hex(codes).getBytes();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	 
	
	
	
	
	public byte[] decode(byte[] input) {
		try {			
			Cipher c1 = Cipher.getInstance("DES");
			c1.init(Cipher.DECRYPT_MODE,getSecretKey());
			return c1.doFinal(ByteHexUtil.hex2Bytes(new String(input)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected SecretKey secretKey;
	
	protected SecretKey getSecretKey() {
		if(secretKey==null){
			SecretKeyFactory keygen=null;
			try {
				keygen = SecretKeyFactory.getInstance("DES");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			DESKeySpec keySpec=null;
			try {
				//keySpec = new DESKeySpec(LuceneLabContext.getString("s_a").getBytes());
				keySpec = new DESKeySpec("meizuall".getBytes());				
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}			 
			try {
				secretKey= keygen.generateSecret(keySpec);
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
		return secretKey;
	}
	
	

	public static void main(String[] args) {
//		System.out.println(new DESMessageEncrypt()
//		.encode("123456789012"));
//
//		System.out.println(new DESMessageEncrypt()
//		.encode("�ز�"));
		
//		System.out.println(new DESMessageEncrypt()
//		.decode("e0e9f8daabe4f77db4298411b075e38e742dcc96c2085da6e0130bf0b0f1dd7c23aa70c3d1bd4438b941790bbc0fcf7406587467eade1be9b4df0afa22f5ffb9".toLowerCase()));
		
		String token=DES.encrypt("meizu&123456","meizuall");
		String token64 = DES.encrypt64("meizu&123456","meizuall");
		System.out.println(new DESMessageEncrypt().encode("meizu&123456".getBytes())+"kkkk");
		//String token64 = DESMessageEncrypt.encrypt64("meizuall","meizu&123456");
//		 BASE64Encoder base64Encoder = new BASE64Encoder();
//         strMi = base64Encoder.encode(byteMi);
		//a2ee5e1d00de3fc5320a95beaf15b692
		
		System.out.println(token);//a2ee5e1d00de3fc5320a95beaf15b692
		System.out.println(Base64Encoder.encode(token));//YTJlZTVlMWQwMGRlM2ZjNTMyMGE5NWJlYWYxNWI2OTI=
		System.out.println(token64);//ou5eHQDeP8UyCpW+rxW2kg==
	}
}
