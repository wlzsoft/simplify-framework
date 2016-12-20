#!/bin/bash
echo "----------server start---------------------"
#dirname $0，取得当前执行的脚本文件的父目录
#进入这个目录(切换当前工作目录)
echo '当前执行命令,不包含参数'$0
echo 'pwd'`pwd`
echo 'ls'`ls`
cd `dirname $0`
#显示当前工作目录(cd执行后的)
BIN_DIR=`pwd`
echo 'BIN_DIR='$BIN_DIR
JAVA_HOME="/usr/local/jdk1.8.0_25"

cd ..
echo "JAVA_HOME path: $JAVA_HOME"
DEPLOY_DIR=`pwd`
echo "current path：$DEPLOY_DIR"
CONF_DIR=$DEPLOY_DIR/conf
echo "conf path: $CONF_DIR"

APP_HOME=$(cd `dirname $0`; pwd)/..
MAIN_CLASS_NAME="com.meizu.simplify.bootstrap.Server"

JAVA_MEM_OPTS="-DlogDir=$APP_HOME/logs"
if [ -r app.vmoptions ];then
JAVA_MEM_OPTS="$JAVA_MEM_OPTS `tr '\n' ' ' < app.vmoptions`"
fi

process_Id=`ps -ef| grep java | grep config-server |awk '{print $2}'`
#库路径
LIB_DIR=$DEPLOY_DIR/lib
for i in $APP_HOME/lib/*.jar;do
LIB_JARS="$i"
done
LIB_JARS="LIB_JARS':'$CLASSPATH

echo 'DEPLOY_DIR='$DEPLOY_DIR
echo 'LIB_DIR='$LIB_DIR
echo 'LIB_JARS='$LIB_JARS	
echo "JAVA_HOME path: $JAVA_HOME"
echo "JAVA_MEM_OPTS="$JAVA_MEM_OPTS 
echo "MAIN_CLASS_NAME="$MAIN_CLASS_NAME
echo "APP_HOME=$APP_HOME"
echo "process_Id=$process_Id"

start(){
    printf '$SERVER_NAME is starting...\n'

	if [ -n "$process_Id" ]; then
    	echo "ERROR: The already started! PID: $PIDS"
    	exit 1
	fi 

	JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -javaagent:$DEPLOY_DIR/aop/weaving.jar"
	
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
	if [ -n "$BITS" ]; then
	    JAVA_MEM_OPTS=" -server -Xmx2g -Xms2g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
	else
	    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
	fi
	
echo $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS_NAME  &
     $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS_NAME  &
	echo "service start OK!"
	PIDS=`ps -ef| grep java | grep config-server |awk '{print $2}'`
	echo "start PID: $process_Id"
}

restart(){
    printf 'ReportServer is restart...\n'
    if [ $process_Id ];then
       kill -9 $process_Id
       sleep 1
    fi 

   $JAVA_HOME/bin/java $JAVA_MEM_OPTS -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS_NAME &
}

stop (){
   printf 'ReportServer is stoping...\n'
   if
     ps fx|grep config-server|grep -v grep |awk '{print $1}'|xargs kill -9
   then
     echo "success"
   else
     echo "error"
   fi
   #if [ $process_Id ];then
   #  kill -9 $process_Id 
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
