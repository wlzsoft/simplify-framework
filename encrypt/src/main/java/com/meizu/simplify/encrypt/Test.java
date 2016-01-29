package com.meizu.simplify.encrypt;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年9月17日 下午9:18:56</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年9月17日 下午9:18:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Test
{
    public static void main(String[] args)
    {
    	System.out.println(Encrypt.passwordEncrypt("scts808"));
//    	String ssssss = "123456789";
//        ssssss = Encrypt.numEncrypt(ssssss);
//        print("ssssss=" + ssssss);
//        ssssss = Decrypt.numDecrypt(ssssss);
//        print("ssssss=" + ssssss);
//        ssssss = Encrypt.numEncrypt(ssssss);
//        print("ssssss=" + ssssss);
//        
//        String cinemaNo = "13014101";
//        String key = "loongcinema";
//        print("设置影院编码=" + cinemaNo);
//        print("设置秘钥=" + key);
//        Keys.setCinemaNo(cinemaNo);
//        Keys.setKey(key);
//        
//        String username = "admin";
//        String password = "123456";
//        String name = "雪狼";
//        String adderss = "深圳市福田区福强路111栋4楼 深圳市福田区福强路111栋4楼 深圳市福田区福强路111栋4楼 深圳市福田区福强路111栋4楼 深圳市福田区福强路111栋4楼";
//        
//        print("用户名：" + username);
//        print("密码：" + password);
//        print("姓名：" + name);
//        print("地址：" + adderss);
//        print("=================================");
//        
//        print("===========加密用户名============");
//        String usernameEncrtyed = Encrypt.fieldEncrypt("username", username);
//        print("用户名加密结果：" + usernameEncrtyed);
//        print("===========解密用户名============");
//        String usernameDecrtyed = Decrypt.fieldDecrypt(usernameEncrtyed);
//        print("用户名解密结果：" + usernameDecrtyed);
//        
//        print("=============加密密码===========");
//        String passwordEncrtyed = Encrypt.passwordEncrypt(password);
//        print("密码加密结果：" + passwordEncrtyed);
//        print("=============验证密码============");
//        boolean passwordVerify = Decrypt.passwordVerify(password, passwordEncrtyed);
//        print("密码验证结果：" + passwordVerify);
//        
//        print("=============加密姓名============");
//        String nameEncrtyed = Encrypt.fieldEncrypt("name", name);
//        print("姓名加密结果：" + nameEncrtyed);
//        print("=============解密姓名============");
//        String nameDecrtyed = Decrypt.fieldDecrypt(nameEncrtyed);
//        print("姓名解密结果：" + nameDecrtyed);
//        
//        print("=============加密地址============");
//        String adderssEncrtyed = Encrypt.fieldEncrypt("adderss", adderss);
//        print("地址加密结果：" + adderssEncrtyed);
//        print("=============解密地址============");
//        String adderssDecrtyed = Decrypt.fieldDecrypt(adderssEncrtyed);
//        print("地址解密结果：" + adderssDecrtyed);
//        
//        print("=================================");
//        print("=============文件加解密===========");
//        String file1 = "C:\\Users\\Administrator\\Desktop\\热部署.txt";
//        String file2 = "C:\\Users\\Administrator\\Desktop\\热部署.enc";
//        String file3 = "C:\\Users\\Administrator\\Desktop\\热部署.enc.zip";
//        print("待加密文件：" + file1);
//        print("加密后文件：" + file2);
//        print("解密后文件：" + file3);
//        byte[] keybyte = Keys.defaultFileKey();
//        byte[] filekey = Keys.calcConKey("ff", keybyte);
//        print("文件加混淆秘钥：" + new String(filekey));
//        byte[] rc4Key = Keys.calcRc4Key("ff", keybyte);
//        print("文件加密秘钥：" + new String(rc4Key));
//        print("===========开始加密文件===========");
//        FileInputStream in = null;
//        FileOutputStream out = null;
//
//        try
//        {
//            in = new FileInputStream(file1);
//            out = new FileOutputStream(file2);
//            FileEncrypt.StreamEncrypt(in, out, keybyte);
//        }
//        catch (FileNotFoundException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (IOException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        finally
//        {
//            if(in!=null)
//            {
//                try
//                {
//                    in.close();
//                } catch (IOException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if(out!=null)
//            {
//                try
//                {
//                    out.close();
//                } catch (IOException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        print("===========开始解密文件===========");
//        try
//        {
//            in = new FileInputStream(file2);
//            out = new FileOutputStream(file3);
//            FileDecrypt.StreamDecrypt(in, out, keybyte);
//        } catch (FileNotFoundException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        catch (IOException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        finally
//        {
//            if(in!=null)
//            {
//                try
//                {
//                    in.close();
//                } catch (IOException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if(out!=null)
//            {
//                try
//                {
//                    out.close();
//                } catch (IOException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        print("文件加解密完成");
    }
    
//    private static void print(String s)
//    {
//        System.out.printf("%1$tF%1$tT: %2$s\r\n", new java.util.Date(), s);
//    }
}
