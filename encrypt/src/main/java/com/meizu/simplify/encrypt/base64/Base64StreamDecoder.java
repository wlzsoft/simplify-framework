package com.meizu.simplify.encrypt.base64;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p><b>Title:</b><i>base64编码算法-正统算法</i></p>
 * <p>Desc: 可用于解码字符串和字节流数据。base64算法的详细说明可以查看 RFC 1521 规范 的5.2小节  
 * <p>
 * 1.解码字符串(string)的用法:
 * <blockquote><pre>
 * String encoded = "d2VibWFzdGVyOnRyeTJndWVTUw";
 * String decoded = Base64Decoder.decode(encoded);
 * </pre></blockquote>
 * 2.解码字节流(streams)的用法:
 * <blockquote><pre>
 * InputStream in = new Base64Decoder(System.in);
 * </pre></blockquote></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月10日 上午11:29:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月10日 上午11:29:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version 1.1, 2002/11/01, added decodeToBytes() to better handle binary
 *                           data (thanks to Sean Graham)
 * @version 1.0, 2000/06/11
 * @version 0.1
 *
 */
public class Base64StreamDecoder extends FilterInputStream {

  private static final char[] decodingTable = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
    'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', '+', '/'
  };

  // A mapping between char values and six-bit integers
  private static final int[] ints = new int[128];
  static {
    for (int i = 0; i < 64; i++) {
      ints[decodingTable[i]] = i;
    }
  }

  private int charCount;
  private int carryOver;

  /**
   * Constructs a new Base64 decoder that reads input from the given
   * InputStream.
   *
   * @param in the input stream
   */
  public Base64StreamDecoder(InputStream in) {
    super(in);
  }

  /**
   * Returns the next decoded character from the stream, or -1 if
   * end of stream was reached.
   *
   * @return  the decoded character, or -1 if the end of the
   *      input stream is reached
   * @exception IOException if an I/O error occurs
   */
  public int read() throws IOException {
    // Read the next non-whitespace character
    int x;
    do {
      x = in.read();
      if (x == -1) {
        return -1;
      }
    } while (Character.isWhitespace((char)x));
    charCount++;

    // The '=' sign is just padding
    if (x == '=') {
      return -1;  // effective end of stream
    }

    // Convert from raw form to 6-bit form
    x = ints[x];

    // Calculate which character we're decoding now
    int mode = (charCount - 1) % 4;

    // First char save all six bits, go for another
    if (mode == 0) {
      carryOver = x & 63;
      return read();
    }
    // Second char use previous six bits and first two new bits,
    // save last four bits
    else if (mode == 1) {
      int decoded = ((carryOver << 2) + (x >> 4)) & 255;
      carryOver = x & 15;
      return decoded;
    }
    // Third char use previous four bits and first four new bits,
    // save last two bits
    else if (mode == 2) {
      int decoded = ((carryOver << 4) + (x >> 2)) & 255;
      carryOver = x & 3;
      return decoded;
    }
    // Fourth char use previous two bits and all six new bits
    else if (mode == 3) {
      int decoded = ((carryOver << 6) + x) & 255;
      return decoded;
    }
    return -1;  // can't actually reach this line
  }

  /**
   * Reads decoded data into an array of bytes and returns the actual 
   * number of bytes read, or -1 if end of stream was reached.
   *
   * @param buf the buffer into which the data is read
   * @param off the start offset of the data
   * @param len the maximum number of bytes to read
   * @return  the actual number of bytes read, or -1 if the end of the
   *      input stream is reached
   * @exception IOException if an I/O error occurs
   */
  public int read(byte[] buf, int off, int len) throws IOException {
    if (buf.length < (len + off - 1)) {
      throw new IOException("The input buffer is too small: " + len + 
       " bytes requested starting at offset " + off + " while the buffer " +
       " is only " + buf.length + " bytes long.");
    }

    // This could of course be optimized
    int i;
    for (i = 0; i < len; i++) {
      int x = read();
      if (x == -1 && i == 0) {  // an immediate -1 returns -1
        return -1;
      }
      else if (x == -1) {       // a later -1 returns the chars read so far
        break;
      }
      buf[off + i] = (byte) x;
    }
    return i;
  }

  

  
}
