package vip.simplify.weaving;

import java.lang.instrument.Instrumentation;

/**
 * <p><b>Title:</b><i>字节码编辑植入处理类</i></p>
 * <p>Desc: java命令启用的参数： -javaagent:E:/workspace-new/simplify-framework/weaving/target/weaving-{version}.jar -Daop.properties=E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:47</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AopAgentMain {

    /**
     * 
     * 方法用途: 在main方法执行后，执行本方法<br>
     * 操作步骤: TODO:测试未通过，需要通过VirtualMachine 加装jar包的方式，暂不实现 <br>
     * @param agentArgs
     * @param ins
     */
    public static void agentmain (String agentArgs, Instrumentation ins) {
    	System.out.println("系统已经启动了");
    	AopClassFileTransformer.printAopMappingInfo();
    }

}
