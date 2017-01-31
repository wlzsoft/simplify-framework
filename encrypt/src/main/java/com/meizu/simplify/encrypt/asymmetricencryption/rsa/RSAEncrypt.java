package com.meizu.simplify.encrypt.asymmetricencryption.rsa;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.meizu.simplify.encrypt.base64.Base64Encrypt;

/**
 * <p><b>Title:</b><i>RSA非对称加密</i></p>
 * <p>Desc: 更安全的常见使用方式是：双方用rsa协商出一个密钥后通过aes/3des给数据加密，这样更安全，但是速度更慢了一些，多了一层加密，需要在设计上想办法去提供加密速度，比如缓存密钥加密结果等</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 上午10:08:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月9日 上午10:08:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RSAEncrypt {

	/**
	 * 
	 * 方法用途: 生成随机字符串<br>
	 * 操作步骤: TODO<br>
	 * @param src 源字符串（随机字符从源字符串中取）
	 * @param length 生成字符串长度
	 * @return 返回生成的随机字符串
	 */
	public static String genRandomCode(String src, Integer length) {
		char[] chars = src.toCharArray();
		StringBuffer challengeString = new StringBuffer();
		for (int i = 0; i < length; i++) {
			double randomValue = Math.random();
			int randomIndex = (int) Math
					.round(randomValue * (chars.length - 1));
			char characterToShow = chars[randomIndex];
			challengeString.append(characterToShow);
		}
		return challengeString.toString();
	}

	/**
	 * 
	 * 方法用途: 生成密钥对<br>
	 * 操作步骤: TODO<br>
	 * @return 返回生成的密钥对
	 */
	public static KeyPair genKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			return keyGen.genKeyPair();
		} catch (Exception e) {
			throw new RuntimeException("生成密钥对时发生异常", e);
		}
	}

	/**
	 * 
	 * 方法用途: 从密钥对中获取Base64编码的公钥字符串<br>
	 * 操作步骤: TODO<br>
	 * @param keyPair 密钥对
	 * @return 返回Base64编码后的公钥字符串
	 */
	public static String getPublicKey(KeyPair keyPair) {
		return new String(Base64Encrypt.encode(keyPair.getPublic().getEncoded()));
	}

	/**
	 * 
	 * 方法用途: 从密钥对中获取Base64编码的私钥字符串<br>
	 * 操作步骤: TODO<br>
	 * @param keyPair 密钥对
	 * @return 返回Base64编码的私钥字符串
	 */
	public static String getPrivateKey(KeyPair keyPair) {
		return new String(Base64Encrypt.encode(keyPair.getPrivate().getEncoded()));
	}

	/**
	 * 
	 * 方法用途: 从Base64编码的公钥字符串中获取公钥<br>
	 * 操作步骤: TODO<br>
	 * @param publicKey Base64编码的公钥字符串
	 * @return 返回公钥
	 */
	public static PublicKey getPublicKey(String publicKey) {
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec spec = new X509EncodedKeySpec(
					Base64Encrypt.decode(publicKey.getBytes()));
			return factory.generatePublic(spec);
		} catch (Exception e) {
			throw new RuntimeException("将字符串转换为公钥时发生异常", e);
		}
	}
	/**
	 * 
	 * 方法用途: 从Base64编码的私钥字符串中获取私钥<br>
	 * 操作步骤: TODO<br>
	 * @param privateKey Base64编码的私钥字符串
	 * @return 返回私钥
	 */
	public static PrivateKey getPrivateKey(String privateKey) {
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(
					Base64Encrypt.decode(privateKey.getBytes()));
			return factory.generatePrivate(spec);
		} catch (Exception e) {
			throw new RuntimeException("将字符串转换为私钥时发生异常", e);
		}
	}

	/**
	 * 
	 * 方法用途: 对字符串进行签名<br>
	 * 操作步骤: TODO<br>
	 * @param srcString 待签名的字符串
	 * @param privateKey 私钥
	 * @return 返回Base64编码格式的签名
	 */
	public static String sign(String srcString, PrivateKey privateKey) {
		try {
			Signature rsa = Signature.getInstance("MD5withRSA");
			rsa.initSign(privateKey);
			rsa.update(srcString.getBytes());
			byte[] sig = rsa.sign();
			return new String(Base64Encrypt.encode(sig));
		} catch (Exception e) {
			throw new RuntimeException("对字符串进行签名时发生异常", e);
		}
	}

	/**
	 * 
	 * 方法用途: 验证签名<br>
	 * 操作步骤: TODO<br>
	 * @param srcString 原文字符串
	 * @param publicKey 公钥
	 * @param signature 签名
	 * @return 验证签名成功返回true，否则返回false
	 */
	public static Boolean verify(String srcString, PublicKey publicKey,
			String signature) {
		try {
			Signature rsa = Signature.getInstance("MD5withRSA");
			rsa.initVerify(publicKey);
			rsa.update(srcString.getBytes());
			return rsa.verify(Base64Encrypt.decode(signature.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException("验证签名时发生异常", e);
		}
	}
}