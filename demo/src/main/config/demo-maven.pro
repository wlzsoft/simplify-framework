#android中是固定proguard.cfg这个文件名
#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-keep public class * extends com.meizu.simplify.ioc.resolver.IAnnotationResolver
#-keep public class com.meizu.simplify.ioc.resolver.IAnnotationResolver
#-keepclasseswithmembers class * {
#    public <init>(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse);
#}
#-keepclasseswithmembers class * {
#    public <init>(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, int);
#}
-renamesourcefileattribute SourceFile

-dontskipnonpubliclibraryclassmembers

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class * {
    public protected *;

-dontskipnonpubliclibraryclassmembers

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class * {
    public protected *;
}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}