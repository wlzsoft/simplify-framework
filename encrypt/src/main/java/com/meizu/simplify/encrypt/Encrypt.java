package com.meizu.simplify.encrypt;

/**
 * <p><b>Title:</b><i>加密</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:17:57</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:17:57</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public final class Encrypt
{
//    private static void print(String s)
//    {
//        System.out.printf("%1$tF%1$tT: %2$s\r\n", new java.util.Date(), s);
//    }
    
    /**
     * 数据表字段加密
     * @return
     */
    public static String fieldEncrypt(String name, String value)
    {
    	if(name == null || value == null)
    		return null;
    	
        byte[] bytes = value.getBytes();
        
        String prefix = Keys.calcPrefix(name);
        //print("前缀：" + prefix);
        byte[] conKey = Keys.calcConKey(prefix);
        //print("混淆秘钥：" + new String(conKey));
//        byte[] aesKey = NgKeys.calcAesKey(prefix);
//        print("加密秘钥：" + new String(aesKey));
        byte[] rc4Key = Keys.calcRc4Key(prefix);
        //print("ARC秘钥：" + new String(rc4Key));
        
        //print("明文：" + value);
        enConfusion(bytes, conKey);
        //print("混淆密文：" + new String(bytes));
        rc4crypt(bytes, rc4Key);
        //print("rc4密文：" + new String(bytes));
        return prefix + Codec.encode64String(bytes);
    }
    
    /**
     * 数据表密码加密
     * @return
     */
    public static String passwordEncrypt(String plaintext)
    {
    	if(plaintext == null)
    		return plaintext;
        String md5 = Codec.hashMd5(plaintext);
        return fieldEncrypt("password", md5);
    }
    
    static void rc4crypt(byte[] src, byte[] keys)
    {
        int x = 0;
        int y = 0;
        byte key[] = new byte[keys.length];
        System.arraycopy(keys, 0, key, 0, keys.length);
        int xorIndex;
        for (int i = 0; i < src.length; ++i) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            src[i] = (byte) (src[i] ^ key[xorIndex]);
        }
    }
    
    /**
     * 分组置换加密
     * @param src
     * @param keys
     */
    static void enConfusion(byte[] src, byte[] keys)
    {
        int keyLength = keys.length;
        int n = 0;
        for(n=0; n<src.length; n+=keyLength)
        {
            for(int i=0; i<keyLength; ++i)
            {
                if((n+i) < src.length)
                {
                    src[n+i] = (byte) (src[n+i] ^ keys[i]);
//                    if(src[n+i]==0)
//                    {
//                        src[n+i] = keys[i];
//                    }
                    src[n+i] = (byte) (src[n+i] + keyLength);
                }
                else
                {
                    break;
                }
            }
        }
    }
    
    static int[] xorInt = Keys.numKey();
    /**
     * 数字加密
     * @param num
     * @return
     */
    public static String numEncrypt(String num)
    {
    	if(num == null || num.length() < 4)
    	{
    		return num;
    	}

        //校验码的十位数和各位数
        int first = num.charAt(num.length()-2);
        int second = num.charAt(num.length()-1);
        num = num.substring(0, num.length() - 2);
        String midValue = "";
        if ((num.length() % 2) == 1)
        {
            midValue += num.charAt(num.length() / 2);
        }

        int[] temp = new int[num.length() / 2];

        //字符组合规则，i -- len-i 组成一个数字
        for (int i = 0; i < num.length() / 2; i++)
        {
            temp[i] = Integer.valueOf("" + num.charAt(i)) * 10 + Integer.valueOf("" + num.charAt(num.length() - i - 1));
        }

        //取各位数和十位数的绝对值+分组号%4,获取异或表中的对应的异或值。
        //把分组值跟异或表的值进行异或。
    	StringBuilder sb = new StringBuilder();
        sb.append((char)first);
        sb.append((char)second);
    	int differ = first > second ? (first - second) : (second - first);
        for (int i = 0; i < temp.length; i++)
        {
        	temp[i] = temp[i] ^ xorInt[((differ) + i) % 4];
            sb.append(String.format("%02d", temp[i]));
        }
        //组装最后的值
    	sb.append(midValue);
        return sb.toString();
    }

}
