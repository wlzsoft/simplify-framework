#!/bin/bash
#全局脚本：整个jenkins关于tomcat的部署，只需要这个脚本
#第一步：编写tomcat远程部署shell脚本:/usr/local/tomcat/face-to-face-pay/bin/restart-common.sh
echo 'Usage: bash [app-path]/[app-id]/bin/restart-common.sh [app-path] [app-id] [jenkins copy package path] [jenkins BUILD_ID]'
echo 'app-path='$1', app-id='$2', jenkins copy package path='$3', jenkins BUILD_ID='$4
echo '启动服务开始BUILD_ID='$4
bash $1/$2/bin/shutdown.sh
#使用trap检测异常信号，目前使用if来判断是否异常
if
#ps -ef |grep java|grep $2 |grep -v grep |awk '{print $2}'|xargs kill -9 
ps fx |grep java|grep $2 |grep -v grep |awk '{print $1}'|xargs kill -9 
then
echo "success"
else
echo "error"
fi
rm -rf $1/$2/webapps/ROOT*
#BUILD_ID是用来在jenkins中全局标识一次build的，如果需要预留每次build的历史数据，可以使用这个值来生成目录名，目前这里可以不使用(本身build已经每次都有生成)，后续可以考虑使用（后续远程生成两份备份,通过shell脚本指定）
# mkdir $1/$2/$4
echo '远程服务器拷贝'
scp $3/jobs/$2/workspace/target/$2.war $1/$2/webapps/ROOT.war
bash $1/$2/bin/startup.sh
echo '启动'$1'目录下的'$2'服务成功！'
#第二步配合jenkins： 需要在jenkins执行脚本以出发实际执行shell脚本
#ssh  10.2.67.41    'bash  /usr/local/tomcat/face-to-face-pay/bin/restart-common.sh   /usr/local/tomcat face-to-face-pay  root@10.2.67.40:/home/jenkins ${BUILD_ID}'