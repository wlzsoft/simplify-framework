package com.meizu.encrypt;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:17</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Codec
{
    final static byte[] DIGITS = new byte[64];
    final static byte[] base64Alphabet = new byte[255];
    final static Charset CHARSET = Charset.forName("UTF-8");
    
    static 
    {
        for(int i=0; i<10; i++)
        {
            DIGITS[i]=(byte)('0' + i);
        }
        for(int i=10; i<36; i++)
        {
            DIGITS[i]=(byte)('a' + i-10);
        }
        for(int i=36; i<62; i++)
        {
            DIGITS[i]=(byte)('A' + i-36);
        }
        DIGITS[62]='_';
        DIGITS[63]='-';
        
        for(int i=0; i< 255; ++i)
        {
            base64Alphabet[i] = (byte) -1;
        }
        for(int i = '0'; i <= '9'; ++i)
        {
            base64Alphabet[i] = (byte) (i - '0');
        }
        for(int i = 'a'; i <= 'z'; ++i)
        {
            base64Alphabet[i] = (byte) (i - 'a' + 10);
        }
        for(int i = 'A'; i <= 'Z'; ++i)
        {
            base64Alphabet[i] = (byte) (i - 'A' + 36);
        }
        base64Alphabet['_'] = 62;
        base64Alphabet['-'] = 63;
    }
    
    static String encode64String(byte[] binaryData)
    {
    	if(binaryData == null || binaryData.length == 0)
    		return "";
        byte[] encoded = encode64(binaryData);
        return  new String(encoded, CHARSET);
    }
    
    private static byte[] encode64(byte[] binaryData)
    {
        long binaryDataLength = binaryData.length;
        long lengthDataBits = binaryDataLength * 8;
        long fewerThan24bits = lengthDataBits % 24;
        long tripletCount = lengthDataBits / 24;
        long encodedDataLengthLong;

        if (fewerThan24bits != 0) {
            // data not divisible by 24 bit
            encodedDataLengthLong = (tripletCount + 1) * 4;
        } else {
            // 16 or 8 bit
            encodedDataLengthLong = tripletCount * 4;
        }

        if (encodedDataLengthLong > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Input array too big, output array would be bigger than Integer.MAX_VALUE=" + Integer.MAX_VALUE);
        }
        int encodedDataLength = (int) encodedDataLengthLong;
        byte encodedData[] = new byte[encodedDataLength];

        byte k, l, b1, b2, b3;

        int encodedIndex = 0;
        int dataIndex;
        int i;

        // log.debug("number of triplets = " + numberTriplets);
        for (i = 0; i < tripletCount; i++) {
            dataIndex = i * 3;
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            b3 = binaryData[dataIndex + 2];

            // log.debug("b1= " + b1 +", b2= " + b2 + ", b3= " + b3);

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & 0xffffff80) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & 0xffffff80) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & 0xffffff80) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex] = DIGITS[val1];
            // log.debug( "val2 = " + val2 );
            // log.debug( "k4 = " + (k<<4) );
            // log.debug( "vak = " + (val2 | (k<<4)) );
            encodedData[encodedIndex + 1] = DIGITS[val2 | (k << 4)];
            encodedData[encodedIndex + 2] = DIGITS[(l << 2) | val3];
            encodedData[encodedIndex + 3] = DIGITS[b3 & 0x3f];

            encodedIndex += 4;
        }

        // form integral number of 6-bit groups
        dataIndex = i * 3;

        if (fewerThan24bits == 8) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            // log.debug("b1=" + b1);
            // log.debug("b1<<2 = " + (b1>>2) );
            byte val1 = ((b1 & 0xffffff80) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex] = DIGITS[val1];
            encodedData[encodedIndex + 1] = DIGITS[k << 4];
            encodedData[encodedIndex + 2] = '.';
            encodedData[encodedIndex + 3] = '.';
        } else if (fewerThan24bits == 0x10) {

            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & 0xffffff80) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & 0xffffff80) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex] = DIGITS[val1];
            encodedData[encodedIndex + 1] = DIGITS[val2 | (k << 4)];
            encodedData[encodedIndex + 2] = DIGITS[l << 2];
            encodedData[encodedIndex + 3] = '.';
        }
        return encodedData;
    }
    
    static byte[] decode64(String base64)
    {
    	if(base64 == null || base64.isEmpty())
    		return new byte[0];
        byte[] encodeBytes = base64.getBytes(CHARSET);
        return decode64(encodeBytes);
    }
    
    private static byte[] decode64(byte[] base64Data)
    {
        // RFC 2045 requires that we discard ALL non-Base64 characters
        //base64Data = discardNonBase64(base64Data);

        // handle the edge case, so we don't have to worry about it later
        if (base64Data== null || base64Data.length == 0) {
            return new byte[0];
        }

        int numberQuadruple = base64Data.length / 4;
        byte decodedData[];
        byte b1, b2, b3, b4, marker0, marker1;

        // Throw away anything not in base64Data

        int encodedIndex = 0;
        int dataIndex;
        {
            // this sizes the output array properly - rlw
            int lastData = base64Data.length;
            // ignore the '=' padding
            while (base64Data[lastData - 1] == '.') {
                if (--lastData == 0) {
                    return new byte[0];
                }
            }
            decodedData = new byte[lastData - numberQuadruple];
        }

        for (int i = 0; i < numberQuadruple; i++) {
            dataIndex = i * 4;
            marker0 = base64Data[dataIndex + 2];
            marker1 = base64Data[dataIndex + 3];

            b1 = base64Alphabet[base64Data[dataIndex]];
            b2 = base64Alphabet[base64Data[dataIndex + 1]];

            if (marker0 != '.' && marker1 != '.') {
                // No PAD e.g 3cQl
                b3 = base64Alphabet[marker0];
                b4 = base64Alphabet[marker1];

                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
            } else if (marker0 == '.') {
                // Two PAD e.g. 3c[Pad][Pad]
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
            } else {
                // One PAD e.g. 3cQ[Pad]
                b3 = base64Alphabet[marker0];
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            }
            encodedIndex += 3;
        }
        return decodedData;
    }
    
    static String encode16ToString(byte[] bytes)
    {
    	if(bytes == null || bytes.length == 0)
    		return null;
        char[] encodedChars = encode16(bytes);
        return new String(encodedChars);
    }
    
    private static char[] encode16(byte[] data) {
    	if(data==null || data.length == 0)
    		return new char[0];
        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = (char) DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = (char) DIGITS[0x0F & data[i]];
        }
        return out;
    }
    
    static byte[] decode16(String hex)
    {
    	if(hex==null || hex.isEmpty())
    		return new byte[0];
        char[] data = hex.toCharArray();
        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = Character.digit(data[j], 16) << 4;
            j++;
            f = f | Character.digit(data[j], 16);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }
    
    static String hashMd5(String plaintext)
    {
    	if(plaintext == null || plaintext.isEmpty())
    		return "";
        byte[] data = plaintext.getBytes(CHARSET);
        byte[] hash = hashMd5(data);
        return encode16ToString(hash);
    }
    
    private static byte[] hashMd5(byte[] bytes)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(bytes);
            return digest.digest();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
