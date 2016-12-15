#!/bin/sh
#startup.sh最终和startup2.sh合并起来
#dirname $0，取得当前执行的脚本文件的父目录
#cd `dirname $0`，进入这个目录(切换当前工作目录)
#pwd，显示当前工作目录(cd执行后的)
APP_HOME=$(cd `dirname $0`; pwd)/..
#APP_HOME=${PWD}/..
MAIN_CLASS_NAME="com.meizu.simplify.config.server.bootstrap.Server"
#PORT=`sed "s/.*=//g" $APP_HOME/properties/rmi.properties`
PORT=$3
HOSTNAME=$2
#HOSTNAME=`sed "s/!.*=//g" $APP_HOME/properties/rmi.properties`

JVM_ARGS="-DlogDir=$APP_HOME/logs"
if [ -r app.vmoptions ];then
JVM_ARGS="$JVM_ARGS `tr '\n' ' ' < app.vmoptions`"
fi

process_Id=`/usr/sbin/lsof -i tcp:$PORT|awk '{print $2}'|sed '/PID/d'`


PATH="./"
CLASSPATH=$APP_HOME/$PATH
for i in $APP_HOME/lib/*.jar;do
CLASSPATH="$i:$CLASSPATH"
done

export CLASSPATH


#echo "java_home is $JAVA_HOME"
#echo "APP_HOME is $APP_HOME \n"
#echo "CLASSPATH is $CLASSPATH \n"
#echo "JVM_ARGS is $JVM_ARGS "
#echo "MAIN_CLASS_NAME is $MAIN_CLASS_NAME"
#echo "process_Id is $process_Id \n"
#echo "$PORT \n"
#echo "$JAVA_HOME/bin/java $JVM_ARGS -classpath $CLASSPATH $MAIN_CLASS_NAME $HOSTNAME $PORT &"


start(){
    printf 'ReportServer is starting...\n'
    if [ $process_Id ];then
       kill -9 $process_Id
       sleep 1
    fi 

   echo "JVM_ARGS="$JVM_ARGS 
   echo "CLASSPATH="$CLASSPATH
   echo "MAIN_CLASS_NAME="$MAIN_CLASS_NAME
   echo "PORT="$PORT
   echo "APP_HOME="$APP_HOME
   echo $JAVA_HOME/bin/java $JVM_ARGS -classpath $CLASSPATH $MAIN_CLASS_NAME $HOSTNAME $PORT &
   /usr/local/java/jdk1.8.0_25/bin/java $JVM_ARGS -classpath $CLASSPATH $MAIN_CLASS_NAME $HOSTNAME $PORT &
}

restart(){
    printf 'ReportServer is restart...\n'
    if [ $process_Id ];then
       kill -9 $process_Id
       sleep 1
    fi 

   $JAVA_HOME/bin/java $JVM_ARGS -classpath $CLASSPATH $MAIN_CLASS_NAME $HOSTNAME $PORT &
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
