/*
 * @用法
 * 1、定义DxKey对象
 * 2、调用setSrc，设置需要的字符
 * 3、调用setEncryptMethod，设置加密算法，这里设置
 * 4、调用genKey后使用toHex函数得到加密结果
 * 5、测试代码见文件末尾main函数，打开注释即可测
 */
package com.meizu.simplify.encrypt.des;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DxKey
{
	private String m_strSrc;
    private int m_nEncryptMethod;
    private MessageDigest m_md;
    private String m_strKey;
    private byte m_byteDig[];
    
    public DxKey()
    {
    }

    public String getSrc()
    {
        return m_strSrc;
    }

    public void setSrc(String strSrc)
    {
        m_strSrc = strSrc;
    }

    public int getEncryptMethod()
    {
        return m_nEncryptMethod;
    }

    public void setEncryptMethod(int nEncryptMethod)
    {
        m_nEncryptMethod = nEncryptMethod;
    }

    public String getKey()
    {
        return m_strKey;
    }

    public void setKey(String strKey)
    {
        m_strKey = strKey;
    }

    public String Byte2Hex(byte b[])
    {
        String hs = "";
        String strtmp = "";
        for(int n = 0; n < b.length; n++)
        {
            strtmp = Integer.toHexString(b[n] & 0xff);
            if(strtmp.length() == 1)
                hs = hs + "0" + strtmp;
            else
                hs = hs + strtmp;
        }

        return hs.toUpperCase();
    }

    public String toHex()
    {
        return Byte2Hex(getByteDig());
    }

    public byte[] getByteDig()
    {
        return m_byteDig;
    }

    public void setByteDig(byte b[])
    {
        m_byteDig = b;
    }

    public long genKEY()
    {
        int nEncryptMethod = getEncryptMethod();
        switch(nEncryptMethod)
        {
        default:
            break;

        case 1: // '\001'
            try
            {
                m_md = MessageDigest.getInstance("MD5");
            }
            catch(NoSuchAlgorithmException e)
            {
                return 0L;
            }
            break;

        case 2: // '\002'
            try
            {
                m_md = MessageDigest.getInstance("SHA-1");
            }
            catch(NoSuchAlgorithmException e)
            {
                return 0L;
            }
            break;
        }
        m_md.update(getSrc().getBytes());
        setByteDig(m_md.digest());
        setKey(getByteDig().toString());
        return 1L;
    }

    public static void main(String args[])
    {
        String sSSNumber = "10971736";
        String sPage = "1";
        String sKey = "catalog";
        DxKey key = new DxKey();
        String str = new String(sSSNumber + sPage + sKey + "ssConf");
        key.setSrc(str);
        key.setEncryptMethod(1);
        key.genKEY();
        System.out.println(key.toHex());
    }
}