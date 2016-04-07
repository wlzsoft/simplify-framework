package com.meizu.simplify.utils;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月3日 下午1:15:36</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月3日 下午1:15:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */

import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Loader
{
 /* ------------------------------------------------------------ */
 public static URL getResource(Class<?> loadClass,String name, boolean checkParents)
     throws ClassNotFoundException
 {
     URL url =null;
     ClassLoader loader=Thread.currentThread().getContextClassLoader();
     while (url==null && loader!=null )
     {
         url=loader.getResource(name); 
         loader=(url==null&&checkParents)?loader.getParent():null;
     }      
     
     loader=loadClass==null?null:loadClass.getClassLoader();
     while (url==null && loader!=null )
     {
         url=loader.getResource(name); 
         loader=(url==null&&checkParents)?loader.getParent():null;
     }       

     if (url==null)
     {
         url=ClassLoader.getSystemResource(name);
     }   

     return url;
 }

 /* ------------------------------------------------------------ */
 @SuppressWarnings("rawtypes")
 public static Class loadClass(Class loadClass,String name)
     throws ClassNotFoundException
 {
     return loadClass(loadClass,name,false);
 }
 
 /* ------------------------------------------------------------ */
 /** Load a class.
  * 
  * @param loadClass
  * @param name
  * @param checkParents If true, try loading directly from parent classloaders.
  * @return Class
  * @throws ClassNotFoundException
  */
 @SuppressWarnings("rawtypes")
 public static Class loadClass(Class loadClass,String name,boolean checkParents)
     throws ClassNotFoundException
 {
     ClassNotFoundException ex=null;
     Class<?> c =null;
     ClassLoader loader=Thread.currentThread().getContextClassLoader();
     while (c==null && loader!=null )
     {
         try { c=loader.loadClass(name); }
         catch (ClassNotFoundException e) {if(ex==null)ex=e;}
         loader=(c==null&&checkParents)?loader.getParent():null;
     }      
     
     loader=loadClass==null?null:loadClass.getClassLoader();
     while (c==null && loader!=null )
     {
         try { c=loader.loadClass(name); }
         catch (ClassNotFoundException e) {if(ex==null)ex=e;}
         loader=(c==null&&checkParents)?loader.getParent():null;
     }       

     if (c==null)
     {
         try { c=Class.forName(name); }
         catch (ClassNotFoundException e) {if(ex==null)ex=e;}
     }   

     if (c!=null)
         return c;
     throw ex;
 }

 public static ResourceBundle getResourceBundle(Class<?> loadClass,String name,boolean checkParents, Locale locale)
     throws MissingResourceException
 {
     MissingResourceException ex=null;
     ResourceBundle bundle =null;
     ClassLoader loader=Thread.currentThread().getContextClassLoader();
     while (bundle==null && loader!=null )
     {
         try { bundle=ResourceBundle.getBundle(name, locale, loader); }
         catch (MissingResourceException e) {if(ex==null)ex=e;}
         loader=(bundle==null&&checkParents)?loader.getParent():null;
     }      
     
     loader=loadClass==null?null:loadClass.getClassLoader();
     while (bundle==null && loader!=null )
     {
         try { bundle=ResourceBundle.getBundle(name, locale, loader); }
         catch (MissingResourceException e) {if(ex==null)ex=e;}
         loader=(bundle==null&&checkParents)?loader.getParent():null;
     }       

     if (bundle==null)
     {
         try { bundle=ResourceBundle.getBundle(name, locale); }
         catch (MissingResourceException e) {if(ex==null)ex=e;}
     }   

     if (bundle!=null)
         return bundle;
     throw ex;
 }
 

}


