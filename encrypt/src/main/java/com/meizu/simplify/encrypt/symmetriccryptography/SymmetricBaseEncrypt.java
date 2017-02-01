package com.meizu.simplify.encrypt.symmetriccryptography;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.meizu.simplify.encrypt.ByteHexUtil;
import com.meizu.simplify.encrypt.base64.Base64Encrypt;
/**
 * <p><b>Title:</b><i>对称加密算法</i></p>
* <p>Desc: 对称加密算法相关工具类
* 对称加密算法性能：AES>Blowfish>DES>3DES 非对称加密算法： RSA
* 而对称加密算法性能优于对非对称加密(待考量)
* <h1>Blowfish,DES,AES和3DES的比较：</h1>
                       算法类型(数据加密)                            密钥大小(位)        加解密速度         解密时间(若机器每秒尝试255个密钥)        资源消耗       NIST标准编码      是否被cpu支持
AES      对称block密码                                                                                       128,192,256         最快                            1490000亿年                                                                       低                     无                                是
Blowfish -----------------                            --                  比AES稍慢           ----很久---                            ---       ---           否
3DES     对称Feistet密码(Feistet结构的分组密码算法)     112,168             比DES慢               46亿年                                                                                         高                     FIPS 46-3     否
DES      对称Feistet密码(Feistet结构的分组密码算法)     64                  比AES慢               现代机器24小时可被破解                                                 偏高                ----          否
* </p>
* <p>source folder:{@docRoot}</p>
* <p>Copyright:Copyright(c)2014</p>
* <p>Company:meizu</p>
* <p>Create Date:2016年3月8日 下午4:04:27</p>
* <p>Modified By:luchuangye-</p>
* <p>Modified Date:2016年3月8日 下午4:04:27</p>
* @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
* @version Version 0.1
*
*/
public class SymmetricBaseEncrypt {
	
	/**
	 * 
	 * 方法用途: 获取生成原始安全密钥<br>
	 * 操作步骤: 这个方法在对象第一次初始化时生成，避免重复生成，目前是每次调用重新生成 TODO<br>
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param algorithm
	 * @return
	 */
	public static SecretKey getSecretKey(byte[] key,String algorithm) {
		SecretKey secretKey = null;
			if(key != null) {
//				第一种写法:推荐使用，更通用，只需要指定算法(algorithm),可以通用所有的加密算法。对于DES算法(key长度必须是8个字节,也就是64位)
				secretKey = new SecretKeySpec(key, algorithm);
//				第二种写法：需要创建具体的keySpec，使用麻烦,不建议使用。对于DES算法和第一种方式不同的是,key长度至少8个字节以上，也就是64位或以上其实。由于DES算法的key要求8字节，所以key指定到8字节以上等同于8字节，超过的部分被丢弃
				/*try {
					KeySpec keySpec = new DESKeySpec(key);
					SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
					secretKey = keyFactory.generateSecret(keySpec);
				} catch (InvalidKeySpecException | InvalidKeyException | NoSuchAlgorithmException e) {
					e.printStackTrace();
				}*/
//				第二种写法:end
			} else {
//				第三种写法:
				try {
					KeyGenerator kg = KeyGenerator.getInstance(algorithm);
//	 				初始化密钥生成器
//					SecureRandom secureRandom = null;  
//			        if (seed != null) {  
//			            secureRandom = new SecureRandom(base64AndDecrypt(seed));  
//			        } else {  
//			            secureRandom = new SecureRandom();  
//			        }  
//		        	kg.init(secureRandom);//默认指定56
//	 				生成密钥
					secretKey = kg.generateKey();  
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
//				第三种写法:end
			}
			return secretKey;
	}
	
	
	/**
	 * 
	 * 方法用途: 加密并使用Base64编码<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值
	 *           注意：没有缓存Cipher对象，多次调用，效率低 <br>
	 * @param data 
	 * @param key 如果是DES必须是8个字节，如果是ascii编码，那么都是8个字符，如果是中文等其他字符，那么在charset是utf-8，是2到3个汉字("中文st"这个key才合法)，如果是gbk，那么是4汉字("中文测试"这个key才合法)
	 *            如果是AES必须是16个字节
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptAndBase64(String data,String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
			byte[] byteData = encrypt(data.getBytes(charset), byteKey, iv,algorithm,algorithmMode,algorithmPadding);
	        return new String(Base64Encrypt.encode(byteData));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 加密并使用Base64编码<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值
	 *           注意：没有缓存Cipher对象，多次调用，效率低 <br>
	 * @param data 
	 * @param key 如果是DES必须是8个字节，如果是ascii编码，那么都是8个字符，如果是中文等其他字符，那么在charset是utf-8，是2到3个汉字("中文st"这个key才合法)，如果是gbk，那么是4汉字("中文测试"这个key才合法)
	 *            如果是AES必须是16个字节
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptAndBase64(String data,SecretKey secretKey,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] iv = null;
			if(useIV) {
				iv = secretKey.getEncoded();
			}
			byte[] byteData = encrypt(data.getBytes(charset), secretKey, iv,algorithm,algorithmMode,algorithmPadding);
	        return new String(Base64Encrypt.encode(byteData));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: Base64解码并解密<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64AndDecrypt(String data,String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
       try {
			byte[] resultArr = Base64Encrypt.decodeToBytes(data);
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
	        return new String(decrypt(resultArr, byteKey, iv,algorithm,algorithmMode,algorithmPadding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: Base64解码并解密<br>
	 * 操作步骤: 如果使用向量，那么key值作为IV向量值<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64AndDecrypt(String data,SecretKey secretKey,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		byte[] resultArr = Base64Encrypt.decodeToBytes(data);
		byte[] iv = null;
		if(useIV) {
			iv = secretKey.getEncoded();
		}
        return new String(decrypt(resultArr, secretKey, iv,algorithm,algorithmMode,algorithmPadding));
	}
	
	/**
	 * 
	 * 方法用途: 加密<br>
	 * 操作步骤: 针对向量的处理
	 *           注意：没有缓存Cipher对象，多次调用，效率低 <br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] encrypt(String data,String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
			byte[] byteData = encrypt(data.getBytes(charset), byteKey, iv,algorithm,algorithmMode,algorithmPadding);
			return byteData;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: 针对向量的处理<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String decrypt(byte[] data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			byte[] byteKey = key.getBytes(charset);
			byte[] iv = null;
			if(useIV) {
				iv = key.getBytes(charset);
			}
			byte[] cipherByte = decrypt(data,byteKey,iv,algorithm,algorithmMode,algorithmPadding);
			return new String(cipherByte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 加密并转成16进制数据<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String encryptToHex(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		byte[] cipherByte = encrypt(data,key,charset,useIV,algorithm,algorithmMode,algorithmPadding);
		return ByteHexUtil.bytes2Hex(cipherByte);
	}
	
	/**
	 * 
	 * 方法用途: 16进制数据解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param useIV
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String hexToDecrypt(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		byte[] byteData = ByteHexUtil.hex2Bytes(data);
		return decrypt(byteData, key, charset, useIV,algorithm,algorithmMode,algorithmPadding);
	}
	
	/**
	 * 
	 * 方法用途: URLEncoder编码并且加密<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String urlEncodeAndEncrypt(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			if(!(null == charset || charset.length() < 1)){
				data = URLEncoder.encode(data, charset);
			}
			String result = encryptToHex(data, key,charset,useIV,algorithm,algorithmMode,algorithmPadding);
			return result;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 
	 * 方法用途: 解密并URLEncoder解码<br>
	 * 操作步骤: 注意：可解决中文乱码问题(加密算法，unicode转码，URIEncode转码，都可以解决乱码问题)<br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param charset
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static String decryptAndUrldecode(String data, String key,String charset,boolean useIV,String algorithm,String algorithmMode,String algorithmPadding) {
		try {
			if (data == null || data.length() < 1) {
				return "";
			}
			String result = hexToDecrypt(data,key,charset,useIV,algorithm,algorithmMode,algorithmPadding);
			if(!(null == charset || charset.length() < 1)){
				result = URLDecoder.decode(result, charset);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 
	 * 方法用途: 加密<br>
	 * 操作步骤: 注意：没有缓存Cipher对象，多次调用，效率低 <br>
	 * @param data
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param iv 加密向量-向量是为了提高安全性，使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] encrypt(byte[] data,byte[] key, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		
		try {
			Cipher cipher = getEncryptCipher(key,iv,algorithm,algorithmMode,algorithmPadding);
			byte[] cipherByte = cipher.doFinal(data);
			return  cipherByte;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 加密<br>
	 * 操作步骤: 注意：没有缓存Cipher对象，多次调用，效率低 <br>
	 * @param data
	 * @param secretkey 
	 * @param iv 加密向量-向量是为了提高安全性，使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] encrypt(byte[] data,SecretKey secretkey, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		
		try {
			Cipher cipher = getEncryptCipher(secretkey,iv,algorithm,algorithmMode,algorithmPadding);
			byte[] cipherByte = cipher.doFinal(data);
			return  cipherByte;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * 方法用途: 获取加密Cipher<br>
	 * 操作步骤: TODO <br>
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param iv 加密向量-向量是为了提高安全性，使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static Cipher getEncryptCipher(byte[] key, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		return getEncryptCipher(getSecretKey(key,algorithm), iv, algorithm, algorithmMode, algorithmPadding);
	}
	
	/**
	 * 
	 * 方法用途: 获取加密Cipher<br>
	 * 操作步骤: TODO <br>
	 * @param secretKey 
	 * @param iv 加密向量-向量是为了提高安全性，使加密的数据更难被破解(用于避免明文中的重复字符在加密后还是重复的，减少加密后数据的规律)
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static Cipher getEncryptCipher(SecretKey secretKey, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		
		try {
			Cipher cipher;
			if(iv != null) {//CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
				cipher = Cipher.getInstance(algorithm+"/"+algorithmMode+"/"+algorithmPadding);
				IvParameterSpec param = new IvParameterSpec(iv);
				// 用密匙初始化Cipher对象
				cipher.init(Cipher.ENCRYPT_MODE, secretKey,param);
			} else {//ECB模式不支持向量
				cipher = Cipher.getInstance(algorithm+"/"+algorithmMode+"/"+algorithmPadding);
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			}
			//缓存以上操作,主要缓存cipher
			return  cipher;
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("无效算法向量参数异常,使用向量的CBC模式会有这个异常");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println(algorithm+"算法，加密数据出错!");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.err.println(algorithm+"算法，加密数据出错,无效key值");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: TODO<br>
	 * @param data 如果是aes，待解密的密文必须是16的倍数
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param iv 加密向量
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] decrypt(byte[] data,byte[] key, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		Cipher cipher = getDecryptCipher(key,iv,algorithm,algorithmMode,algorithmPadding);
		try {
			return cipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			System.err.println("若是aes解密，密文长度必须是16的倍数");
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: TODO<br>
	 * @param data
	 * @param secretKey 
	 * @param iv 加密向量
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static byte[] decrypt(byte[] data,SecretKey secretKey, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		Cipher cipher = getDecryptCipher(secretKey,iv,algorithm,algorithmMode,algorithmPadding);
		try {
			return cipher.doFinal(data);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: TODO<br>
	 * @param key des长度8个字节，也就是64位; aes长度16个字节
	 * @param iv 加密向量
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static Cipher getDecryptCipher(byte[] key, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
		return getDecryptCipher(getSecretKey(key,algorithm), iv, algorithm, algorithmMode, algorithmPadding);
	}
	
	/**
	 * 
	 * 方法用途: 解密<br>
	 * 操作步骤: TODO<br>
	 * @param secretKey 
	 * @param iv 加密向量
	 * @param algorithm 算法名称
	 * @param algorithmMode 算法模式(加密分割模式)
	 * @param algorithmPadding 算法填充模式
	 * @return
	 */
	public static Cipher getDecryptCipher(SecretKey secretKey, byte[] iv,String algorithm,String algorithmMode,String algorithmPadding) {
//		判断是否缓存secretKey
//		if(secretKey==null){
//			secretKey = getSecretKey(key);
//		}
//		判断是否缓存Cipher,后续可以通过EnumMap来缓存Cipher TODO 
		try {	
			Cipher cipher;
			if(iv != null) {//CBC模式，必须有向量值，否则加密的数据不固定，导致加密的值无法解密(解密异常java.security.InvalidKeyException: Parameters missing)
		        cipher = Cipher.getInstance(algorithm+"/"+algorithmMode+"/"+algorithmPadding);
		        IvParameterSpec param = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE, secretKey, param);
			} else {//ECB模式不支持向量
				cipher = Cipher.getInstance(algorithm+"/"+algorithmMode+"/"+algorithmPadding);
				cipher.init(Cipher.DECRYPT_MODE,secretKey);
			}
			return cipher;
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("无效算法向量参数异常,使用向量的CBC模式会有这个异常");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println(algorithm+"算法，解密出错。");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
