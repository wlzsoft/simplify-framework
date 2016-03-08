package com.meizu.simplify.encrypt.des;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.meizu.simplify.encrypt.base64.ByteHexUtil;


 

/**
 * <p><b>Title:</b><i>自定义随机加解密算法</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月8日 下午5:38:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月8日 下午5:38:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class CoverMessageEncrypt {

	private static long time;
	static {
		
		Thread t = new Thread(new Runnable() {			
			public void run() {
				while(true){
					time = System.currentTimeMillis()^0x863FA34;
					try {
						Thread.sleep(24*3600);//wait one day
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.setDaemon(true);
		
		t.start();
		
	}

	public String decode(String input){
		int nConLen = input.length();
		byte []byKey = ByteHexUtil.hex2Bytes(input.substring(nConLen - 16, nConLen));
		byte[] byteResult = ByteHexUtil.hex2Bytes(input.substring(0, nConLen - 16));

		for (int n = 0; n < byteResult.length; n++) {
			byteResult[n] -= byKey[byKey.length - 1];
		}
		try {
			return new String(byteResult,"GBK");
		} catch (UnsupportedEncodingException e) {	
			return new String(byteResult);
		}
	}

	public byte[] decode(byte[] input) {				
		return decode(new String(input)).getBytes();
	}

	public byte[] encode(byte[] input) {
		return ByteHexUtil.bytes2Hex(RandEncode(input)).getBytes();
	}

	private byte[] getRandBytes(){
		Random rand = new Random(time);
		byte []m_byteRand = new byte[8];
		int n = 0;
		while (n % 10 == 0) {
			n = rand.nextInt();
		}
		n=n>0?n:-n;	    

		byte[] bytesRand = String.valueOf(n).getBytes();

		for (int i = 7,j=bytesRand.length-1; i >=0&& j>=0 ; i--,j--) {
			m_byteRand[i] = bytesRand[j];
		}
		return m_byteRand;
	}




	/**
	 *   bySrc[n]  =  
	 * @param bySrc
	 * @return
	 */
	private byte[] RandEncode(byte []bySrc) {
		byte[] m_byteRand = getRandBytes();				
		byte[] result = new byte[bySrc.length+m_byteRand.length];
		System.arraycopy(bySrc, 0, result, 0, bySrc.length);
		System.arraycopy(m_byteRand, 0, result, bySrc.length, m_byteRand.length);
		for (int n = 0; n < bySrc.length; n++) {
			result[n] += m_byteRand[m_byteRand.length-1];
		}

		return result;
	}
		 
		 public static void main(String[] args) {
			 CoverMessageEncrypt encrypt = new CoverMessageEncrypt();
			 
			 String str = new String(encrypt.encode("2212".getBytes()));
			 System.out.println(str);
			 System.out.println(encrypt.decode(str));
		}

}
