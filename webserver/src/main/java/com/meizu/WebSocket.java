package com.meizu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.LocalDateTime;

import com.meizu.simplify.encrypt.base64.Base64Encrypt;
import com.meizu.simplify.encrypt.sign.sha1.SHA1Encrypt;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月4日 下午5:23:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月4日 下午5:23:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class WebSocket {
	private static int i=0;
    public static class Handler {
        private Socket socket;
        private boolean hasHandshake = false;
        Charset charset = Charset.forName("UTF-8");
        InputStream br;
        HttpRequest request;
        public Handler(Socket socket, InputStream br, HttpRequest request) {
            this.socket = socket;
            this.br = br;
            this.request = request;
        }
		
		public void exec2() {
			try {
                System.out.println("client["+(i++)+"] connected: " + socket.getInetAddress() + ":" + socket.getPort());
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                String upgrade = request.getHeader("Upgrade");
                InputStream in = br;//socket.getInputStream();
                if (!hasHandshake && (upgrade!= null&&upgrade.equals("websocket"))) { // 握手
//            			request info:
//            			GET /websocket/notice HTTP/1.1
//            			Host: 127.0.0.1:8080
//            			Connection: Upgrade
//            			Pragma: no-cache
//            			Cache-Control: no-cache
//            			Upgrade: websocket
//            			Origin: http://127.0.0.1:8080
//            			Sec-WebSocket-Version: 13
//            			User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36
//            			Accept-Encoding: gzip, deflate, sdch
//            			Accept-Language: zh-CN,zh;q=0.8
//            			Cookie: cookie_lang=1; JSESSIONID=1dqg8b1r9lt7o7j4mr0uqp4g3; Hm_lvt_82116c626a8d504a5c0675073362ef6f=1457403984; Hm_lpvt_82116c626a8d504a5c0675073362ef6f=1457415472; sessionId=393bbe756f294f2e8c3677164bddefa6
//            			Sec-WebSocket-Key: utj2uKJA660Zw7uwVFQi8Q==
//            			Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits
//            			ContentLength :null
                	
//            		response info //第二次握手确认
                    String key = request.getHeader("Sec-WebSocket-Key")+"258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
                    byte[] sha1Hash = SHA1Encrypt.sign(key.getBytes("utf-8"));
                    key = new String(Base64Encrypt.encode(sha1Hash));
                    pw.println("HTTP/1.1 101 Switching Protocols");
//                  response.setStatusCode("101");
//                  response.setReason("Switching Protocols");
//        			response.setHeader("Connection", "Upgrade");
                    pw.println("Connection: Upgrade");
                    pw.println("Server:meizu websocket server 1.0");
                    pw.println("Upgrade: WebSocket");
                    pw.println("Date: "+LocalDateTime.now());//Date:Mon, 26 Nov 2016 23:59:59 GMT
        			pw.println("Access-Control-Allow-Credentials: true");
        			pw.println("Access-Control-Allow-Headers: content-type");
                    pw.println("Sec-WebSocket-Accept: " + key);
                    pw.println();
                    pw.flush();
                    hasHandshake = true;
                    // 接收数据
                    byte[] first = new byte[1];
                    int read = in.read(first, 0, 1); // 这里会阻塞
                    while (read > 0) {
                        int b = first[0] & 0xFF;
                        // 1为字符数据，8为关闭socket
                        byte opCode = (byte) (b & 0x0F);
//                        byte firstBit = (byte) (b & 0x80);//如果考虑分帧处理，那么需要判断这个值，是否是结束帧，目前不分帧处理
                        if (opCode == 8) {
                            socket.getOutputStream().close();
                            break;
                        }
                        b = in.read();
                        int payloadLength = b & 0x7F;
                        if (payloadLength == 126) {
                            byte[] extended = new byte[2];
                            in.read(extended, 0, 2);
                            int shift = 0;
                            payloadLength = 0;
                            for (int i = extended.length - 1; i >= 0; i--) {
                                payloadLength = payloadLength + ((extended[i] & 0xFF) << shift);
                                shift += 8;
                            }
                        } else if (payloadLength == 127) {
                            byte[] extended = new byte[8];
                            in.read(extended, 0, 8);
                            int shift = 0;
                            payloadLength = 0;
                            for (int i = extended.length - 1; i >= 0; i--) {
                                payloadLength = payloadLength + ((extended[i] & 0xFF) << shift);
                                shift += 8;
                            }
                        }
                        // 掩码
                        byte[] mask = new byte[4];
                        in.read(mask, 0, 4);
                        int readThisFragment = 1;
                        ByteBuffer byteBuf = ByteBuffer.allocate(payloadLength);
                        while (payloadLength > 0) {
                            int masked = in.read();
                            masked = masked ^ (mask[(int) ((readThisFragment - 1) % 4)] & 0xFF);
                            byteBuf.put((byte) masked);
                            payloadLength--;
                            readThisFragment++;
                        }
                        byteBuf.flip();
                        responseClient(byteBuf, true);
                        System.out.println(new String(byteBuf.array(),charset));
                        in.read(first, 0, 1);
                    }
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null){
                    	System.out.println("client["+(i--)+"] disconnected: "+ socket.getInetAddress() + ":" + socket.getPort());
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
 
        private void responseClient(ByteBuffer byteBuf, boolean finalFragment) throws IOException {
            OutputStream out = socket.getOutputStream();
            byte first = 0x00;
            if (finalFragment) {// 是否是输出最后的WebSocket响应片段
                first = (byte) (0x80 | 0x01);
            }
            out.write(first);
 
            if (byteBuf.limit() < 126) {
                out.write(byteBuf.limit());
            } else if (byteBuf.limit() < 65536) {
                out.write(126);
                out.write(byteBuf.limit() >>> 8);
                out.write(byteBuf.limit() & 0xFF);
            } else {
                // Will never be more than 2^31-1
                out.write(127);
                out.write(0);
                out.write(0);
                out.write(0);
                out.write(0);
                out.write(byteBuf.limit() >>> 24);
                out.write(byteBuf.limit() >>> 16);
                out.write(byteBuf.limit() >>> 8);
                out.write(byteBuf.limit() & 0xFF);
            }
            // Write the content
            out.write(byteBuf.array(), 0, byteBuf.limit());
            out.flush();
        }
    }
}
