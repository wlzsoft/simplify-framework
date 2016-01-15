package com.meizu.aop;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * �ֽ���ת����
 * 
 */
public class AopClassFileTransformer implements ClassFileTransformer {

    /**
     * �ֽ�����ص������ǰ������������
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        System.out.println(className);
        //�������Business�������
        if (!"model/Business".equals(className)) {
            return null;
        }

        //javassist�İ������õ�ָ�ģ���Ҫת����
        if (className != null && className.indexOf("/") != -1) {
            className = className.replaceAll("/", ".");
        }
        try {
            //ͨ��������ȡ���ļ�
            CtClass cc = ClassPool.getDefault().get(className);
            //���ָ���������ķ���
            CtMethod m = cc.getDeclaredMethod("doSomeThing");
            //�ڷ���ִ��ǰ�������
            m.insertBefore("{ System.out.println(\"��¼��־\"); }");
            return cc.toBytecode();
        } catch (NotFoundException e) {
        } catch (CannotCompileException e) {
        } catch (IOException e) {
            //�����쳣����
        }
        return null;
    }

    /**
     * ��main����ִ��ǰ��ִ�еĺ���
     * 
     * @param options
     * @param ins
     */
    public static void premain(String options, Instrumentation ins) {
        //ע�����Լ����ֽ���ת����
        ins.addTransformer(new AopClassFileTransformer());
    }

}
