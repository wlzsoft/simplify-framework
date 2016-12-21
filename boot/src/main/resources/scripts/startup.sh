#!/bin/bash
echo "----------server start---------------------"
echo 'pwd='`pwd`
echo 'ls='`ls`
JAVA_HOME="/usr/local/jdk1.8.0_25"
echo "JAVA_HOME path: $JAVA_HOME"
#dirname $0，取得当前执行的脚本文件的父目录
#进入这个目录(切换当前工作目录)
echo '当前执行命令,不包含参数==>>'$0
cd `dirname $0`
#回到上层目录==>>APP_HOME
cd ..
#显示当前工作目录==>>取得APP_HOME路径
APP_HOME=`pwd`
echo 'APP_HOME='$APP_HOME
BIN_DIR=$APP_HOME/bin
echo 'BIN_DIR='$BIN_DIR
CONF_DIR=$APP_HOME/conf
echo "CONF_DIR="$CONF_DIR

#虚拟机启动的其他选项设置，不包含内存和堆栈设置，可以设置一些调优参数
JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"

#虚拟机本身日志输出设置
D_Log_Dir="-DlogDir=$APP_HOME/logs"

#虚拟机的启动内存选项设置
#参数-r来判断app.vmoptions文件是否可读,这里判断一直是false，需要检查下原因
if [ -r app.vmoptions ];then
JAVA_MEM_OPTS="$JAVA_MEM_OPTS `tr '\n' ' ' < app.vmoptions`"
fi
echo "JAVA_MEM_OPTS="$JAVA_MEM_OPTS 

#javaAgent设置
JAVA_AGENT=" -javaagent:$APP_HOME/aop/weaving.jar"

#主程序CLASSPATH-类搜索路径
echo 'GOBAL CLASSPATH is '$CLASSPATH
#CLASSPATH库路径
LIB_DIR=$APP_HOME/lib
echo 'LIB_DIR='$LIB_DIR
for i in $LIB_DIR/*.jar;do
LIB_JARS=$i':'$LIB_JARS
done
LIB_JARS=$LIB_JARS':'$CLASSPATH
echo 'LIB_JARS='$LIB_JARS	

#主程序入库类,这里在jar包指定，这个不起作用
MAIN_CLASS_NAME="com.meizu.simplify.bootstrap.Server"
echo "MAIN_CLASS_NAME="$MAIN_CLASS_NAME

process_Id=`ps fx | grep java | grep config-server |awk '{print $1}'` 
echo "process_Id=$process_Id"

start(){
    printf '$SERVER_NAME is starting...\n'

	if [ -n "$process_Id" ]; then
    	echo "ERROR: The already started! PID: $PIDS"
    	exit 1
	fi 

	JAVA_DEBUG_OPTS=""
	if [ "$1" = "debug" ]; then
		#jpda参数说明：
		#-XDebug 启用调试
		#-Xrunjdwp 加载JDWP的JPDA参考执行实例。
		#transport  用于在调试程序和 VM 使用的进程之间通讯。
		#dt_socket 套接字传输。
		#server=y/n VM是否需要作为调试服务器执行。
		#address=2345调试服务器监听的端口号。
		#suspend=y/n 是否在调试客户端建立连接之后启动 VM ;suspend=y 挂起=只有调试端客户端连接上才会开始执行main方法，避免需要调试启动时代码一闪而过，无法调试到设置断点的代码
	    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 "
	fi
	
	BITS=`$JAVA_HOME/bin/java -version 2>&1 | grep -i 64-bit`
	echo 'BITS='$BITS
	if [ -n "$BITS" ]; then
	    #64位服务器版本-会使用server编译器，jdk1.8会开启分层编译,参数信息从app.vmoptions中读取
	    #JAVA_MEM_OPTS=$JAVA_MEM_OPTS
	    JAVA_MEM_OPTS=-server -Xmx1024m -Xms640m -Xmn512m -Xss256k -XX:PermSize=128m -XX:+DisableExplicitGC -XX:MaxPermSize=128m -XX:ReservedCodeCacheSize=64m -XX:-UseConcMarkSweepGC -XX:+UseParNewGC -XX:+HandlePromotionFailure -XX:+HeapDumpOnOutOfMemoryError -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70
	else
	    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
	fi
	
    #$JAVA_HOME/bin/java $JAVA_OPTS $D_Log_Dir $JAVA_MEM_OPTS JAVA_AGENT $JAVA_DEBUG_OPTS -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS_NAME  &
echo $JAVA_HOME/bin/java $JAVA_OPTS $D_Log_Dir $JAVA_MEM_OPTS JAVA_AGENT $JAVA_DEBUG_OPTS &
     $JAVA_HOME/bin/java $JAVA_OPTS $D_Log_Dir $JAVA_MEM_OPTS JAVA_AGENT $JAVA_DEBUG_OPTS &
	echo "service start OK!"
	process_Id=`ps fx | grep java | grep config-server |awk '{print $1}'`
	echo "start PID: $process_Id"
}

restart(){
   printf '$SERVER_NAME is restart...\n'
    stop    
    start
}

stop (){
   printf '$SERVER_NAME is stoping...\n'
   if
     ps fx|grep config-server|grep -v grep |awk '{print $1}'|xargs kill -9
   then
     echo "success"
   else
     echo "error"
   fi
   #if [ $process_Id ];then
   #  kill -9 $process_Id 
       sleep 1
   #fi 
}

case "$1" in
	start)
		start
	;;
	restart)
		restart
	;;
	stop)
		stop
	;;
	*)
		printf 'Usage:%s {start|restart|stop}\n'
		printf 'app.vmoptions is configuration for  JVM \n '
		exit 1
	;;
esac
