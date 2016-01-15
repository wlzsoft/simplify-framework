package com.meizu.encrypt;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:24</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class DecryptStream extends FilterInputStream
{
    private byte[] conkeys = null;
    private byte[] rc4Keys = null;
    private int page = 0;
    
    private byte[] buf = new byte[512];
    private ByteArrayInputStream bin = new ByteArrayInputStream(buf);
    private boolean off = false;
    private int lastLenth = 0;
    
    public DecryptStream(InputStream in, byte[] key) throws IOException
    {
        super(in);
        this.conkeys = Keys.calcConKey("ff", key);
        this.rc4Keys = Keys.calcRc4Key("ff", key);
        fill();
    }
    
    protected DecryptStream(InputStream in) throws IOException {
        super(in);
        fill();
    }
    
    @Override
    public int read() throws IOException
    {
        int read = bin.read();
        if(read == -1)
        {
            return read;
        }
        if(!this.off && bin.available()==0)
        {
            fill();
        }
        return read;
    }
    
    @Override
    public int read(byte b[], int off, int len) throws IOException
    {
        if(len>512)
        {
            throw new IOException("最大读取段长度不可超过512");
        }
        return readFromMemory(b, off, len);
    }
    
    private int readFromMemory(byte b[], int off, int len) throws IOException
    {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int readed = 0;
        int read = 0;
        int temLen = len;
        while(true)
        {
            temLen = len - readed;
            if(this.off && temLen > this.lastLenth)
            {
            	if(readed==0 && this.lastLenth==0)
            	{
            		return -1;
            	}
            	else if(this.lastLenth==0)
            	{
            		return readed;
            	}
                temLen = this.lastLenth;
            }
            read = bin.read(b, off + readed, temLen);
            if(this.off)
            {
            	this.lastLenth -= read;
            }
            readed += read;
            if(readed == -1)
            {
                return readed;
            }
            if(!this.off && bin.available()==0)
            {
                int readLenth = fill();
                if(readLenth == -1)
                {
                    return readed;
                }
            }
            if(readed == len)
            {
                return readed;
            }
        }
    }
    
    private int fill() throws IOException
    {
        int readLenth = readFromFile(buf, 0, 512);
        if(readLenth == -1)
        {
            return -1;
        }
        else if(readLenth < 512)
        {
            this.lastLenth = readLenth;
            this.off = true;
        }
        bin.reset();
        return readLenth;
    }

    private int readFromFile(byte b[], int off, int len) throws IOException
    {
        int bytesRead;
        if((bytesRead = in.read(b, off, len)) != -1)
        {
            if(len == bytesRead)
            {
                if(page==0)
                {
                    Decrypt.rc4crypt(b, rc4Keys);
                }
                if(page % 3 == 0)
                {
                    Decrypt.deConfusion(b, conkeys);
                }
            }
            else
            {
                byte[] data = new byte[bytesRead];
                System.arraycopy(b, off, data, 0, bytesRead);
                Decrypt.rc4crypt(data, rc4Keys);
                Decrypt.deConfusion(data, conkeys);
                System.arraycopy(data, 0, b, off, bytesRead);
            }
        }
        ++page;
        return bytesRead;
    }
}
