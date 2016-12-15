#!/bin/bash
echo "----------server start---------------------"
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
JAVA_HOME="/usr/local/jdk1.8.0_25"
echo "JAVA_HOME path: $JAVA_HOME"
DEPLOY_DIR=`pwd`
echo "current path：$DEPLOY_DIR"
CONF_DIR=$DEPLOY_DIR/conf

echo "conf path: $CONF_DIR"

PIDS=`ps -ef| grep java | grep config-server |awk '{print $2}'`

if [ -n "$PIDS" ]; then
    echo "ERROR: The already started! PID: $PIDS"
    exit 1
fi

STDOUT_FILE=$DEPLOY_DIR/logs/stdout.log
LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -javaagent:$DEPLOY_DIR/aop/weaving.jar"

JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi

JAVA_MEM_OPTS=""
BITS=`$JAVA_HOME/bin/java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx2g -Xms2g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

echo -e "Starting the $SERVER_NAME ...\c"
nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -classpath $CONF_DIR:$LIB_JARS com.meizu.simplify.config.server.bootstrap.Server > $STDOUT_FILE 2>&1 &
echo "service start OK!"
PIDS=`ps -ef| grep java | grep config-server |awk '{print $2}'`
echo "start PID: $PIDS"
