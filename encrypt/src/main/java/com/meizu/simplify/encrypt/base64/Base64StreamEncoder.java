
package com.meizu.simplify.encrypt.base64;

import java.io.*;

/**
 * <p><b>Title:</b><i>base64编码算法-正统算法</i></p>
 * <p>Desc: 可用于编码字符串和字节流数据。base64算法的详细说明可以查看 RFC 1521 规范 的5.2小节  
 * <p>
 * 1.编码字符串(string)的用法:
 * <blockquote><pre>
 * String unencoded = "3lisoiwseis";
 * String encoded = Base64Encoder.encode(unencoded);
 * </pre></blockquote>
 * 2.编码字节流(streams)的用法:
 * <blockquote><pre>
 * OutputStream out = new Base64Encoder(System.out);
 * </pre></blockquote></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月10日 上午11:29:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月10日 上午11:29:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version 1.2, 2002/11/01, added encode(byte[]) method to better handle
 *                           binary data (thanks to Sean Graham)
 * @version 1.1, 2000/11/17, fixed bug with sign bit for char values
 * @version 1.0, 2000/06/11
 * @version 0.1
 *
 */
public class Base64StreamEncoder extends FilterOutputStream {


 

  public Base64StreamEncoder(OutputStream out) {
    super(out);
  }


  /*public void write(byte[] buf, int off, int len) throws IOException {
	  byte[] encodingTable = Base64Encrypt.encodingTable;
	  int charCount = 0;
	  int carryOver = 0;
	  //*Writes the given byte array to the output stream in an 
      //* encoded form.
      //* @param b the data to be written
      //* @param off the start offset of the data
      //* @param len the length of the data
    // This could of course be optimized
    for (int i = 0; i < len; i++) {
      int b = buf[off + i];

//Writes the given byte to the output stream in an encoded form
      // Take 24-bits from three octets, translate into four encoded chars
      // Break lines at 76 chars
      // If necessary, pad with 0 bits on the right at the end
      // Use = signs as padding at the end to ensure encodedLength % 4 == 0

      // Remove the sign bit,
      // thanks to Christian Schweingruber <chrigu@lorraine.ch>
      if (b < 0) {
        b += 256;
      }

      // First byte use first six bits, save last two bits
      if (charCount % 3 == 0) {
        int lookup = b >> 2;
        carryOver = b & 3;        // last two bits
        out.write(encodingTable[lookup]);
      }
      // Second byte use previous two bits and first four new bits,
      // save last four bits
      else if (charCount % 3 == 1) {
        int lookup = ((carryOver << 4) + (b >> 4)) & 63;
        carryOver = b & 15;       // last four bits
        out.write(encodingTable[lookup]);
      }
      // Third byte use previous four bits and first two new bits,
      // then use last six new bits
      else if (charCount % 3 == 2) {
        int lookup = ((carryOver << 2) + (b >> 6)) & 63;
        out.write(encodingTable[lookup]);
        lookup = b & 63;          // last six bits
        out.write(encodingTable[lookup]);
        carryOver = 0;
      }
      charCount++;

      // Add newline every 76 output chars (that's 57 input chars)
      if (charCount % 57 == 0) {
        out.write('\n');
      }
    
    }
    
   //Closes the stream, this MUST be called to ensure proper padding is
   //written to the end of the output stream.
   // Handle leftover bytes
    if (charCount % 3 == 1) {  // one leftover
      int lookup = (carryOver << 4) & 63;
      out.write(encodingTable[lookup]);
      out.write('=');
      out.write('=');
    }
    else if (charCount % 3 == 2) {  // two leftovers
      int lookup = (carryOver << 2) & 63;
      out.write(encodingTable[lookup]);
      out.write('=');
    }
  }*/
  
  public void write(byte[] buf) throws IOException {
		byte[] bytes = Base64Encrypt.encodeTwo(buf, 0);
		out.write(bytes);
		try (OutputStream ostream = out) {
			out.flush();
		}
	}


  

  
}

