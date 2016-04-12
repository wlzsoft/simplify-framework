package com.meizu.simplify.utils;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.junit.Test;

import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

/**
  * <p><b>Title:</b><i>jdk1.7动态调用测试用例</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月14日 下午5:00:58</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月14日 下午5:00:58</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class InvokeDynamicTest {
	
	@Test
	public void useConstantCallSite() throws Throwable {  
	    MethodHandles.Lookup lookup = MethodHandles.lookup();  
	    MethodType type = MethodType.methodType(String.class, int.class, int.class);  
	    MethodHandle mh = lookup.findVirtual(String.class, "substring", type);  
	    ConstantCallSite callSite = new ConstantCallSite(mh);  
	    MethodHandle invoker = callSite.dynamicInvoker();  
	    String result = (String) invoker.invoke("Hello", 2, 3);  
	    System.out.println(result);
	} 
	@Test
	public void useMutableCallSite() throws Throwable {  
		MethodHandles.Lookup lookup = MethodHandles.lookup();  
	    MethodType type = MethodType.methodType(int.class, int.class, int.class);  
	    MutableCallSite callSite = new MutableCallSite(type);  
	    MethodHandle invoker = callSite.dynamicInvoker();  
	    MethodHandle mhMax = lookup.findStatic(Math.class, "max", type);  
	    MethodHandle mhMin = lookup.findStatic(Math.class, "min", type);  
	    callSite.setTarget(mhMax);  
	    int result = (int) invoker.invoke(3, 5); //值为5  
	    System.out.println(result);
	    callSite.setTarget(mhMin);  
	    result = (int) invoker.invoke(3, 5); //值为3  
	    System.out.println(result);
	} 
	
	
}
/*class ToUpperCaseGenerator {   
    private static final MethodHandle BSM =  
            new MethodHandle(MH_INVOKESTATIC,  
            ToUpperCase.class.getName().replace('.', '/'),  
            "bootstrap",  
            MethodType.methodType(  
            CallSite.class, Lookup.class, String.class, MethodType.class, String.class).toMethodDescriptorString());  
 
    public static void main(String[] args) throws IOException {  
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
        cw.visit(V1_7, ACC_PUBLIC | ACC_SUPER, "ToUpperCaseMain", null, "java/lang/Object", null);  
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);  
        mv.visitCode();  
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");  
        mv.visitInvokeDynamicInsn("toUpperCase", "()Ljava/lang/String;", BSM, "Hello");  
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");  
        mv.visitInsn(RETURN);  
        mv.visitMaxs(0, 0);  
        mv.visitEnd();  
        cw.visitEnd();  
 
        Files.write(Paths.get("ToUpperCaseMain.class"), cw.toByteArray());  
    }  
} */
class ToUpperCase {  
    public static CallSite bootstrap(Lookup lookup, String name, MethodType type, String value) throws Exception {  
        MethodHandle mh = lookup.findVirtual(String.class, "toUpperCase", MethodType.methodType(String.class)).bindTo(value);  
        return new ConstantCallSite(mh);  
    }  
} 
