#!/bin/bash
#局部项目级脚本：整个jenkins的每一个tomcat的部署的job，都有一个脚本，脚步跟着项目走，属于框架级别，无需根据项目变动脚本内容
#第一步：编写tomcat远程部署shell脚本:/usr/local/tomcat/face-to-face-pay/bin/restart-common.sh
echo 'Usage: bash [app-path]/[app-id]/bin/restart-common.sh  [jenkins copy package path] [jenkins BUILD_ID]'
# $(cd `dirname $0`; pwd)
echo 'app-path='$app-path', app-id='$app-id', jenkins copy package path='$1', jenkins BUILD_ID='$2
echo '启动服务开始BUILD_ID='$2
bash $app-path/$app-id/bin/shutdown.sh
#使用trap检测异常信号，目前使用if来判断是否异常
if
#ps -ef |grep java|grep $app-id |grep -v grep |awk '{print $2}'|xargs kill -9 
ps fx |grep java|grep $app-id |grep -v grep |awk '{print $1}'|xargs kill -9 
then
echo "success"
else
echo "error"
fi
rm -rf $app-path/$app-id/webapps/ROOT*
#BUILD_ID是用来在jenkins中全局标识一次build的，如果需要预留每次build的历史数据，可以使用这个值来生成目录名，目前这里可以不使用(本身build已经每次都有生成)，后续可以考虑使用（后续远程生成两份备份,通过shell脚本指定）
# mkdir $app-path/$app-id/$2
echo '远程服务器拷贝'
scp $1/jobs/$app-id/workspace/target/$app-id.war $app-path/$app-id/webapps/ROOT.war
bash $app-path/$app-id/bin/startup.sh
echo '启动'$app-path'目录下的'$app-id'服务成功！'
#第二步配合jenkins： 需要在jenkins执行脚本以出发实际执行shell脚本
#ssh  10.2.67.41    'bash  /usr/local/tomcat/face-to-face-pay/bin/restart-common.sh   /usr/local/tomcat face-to-face-pay  root@10.2.67.40:/home/jenkins ${BUILD_ID}'