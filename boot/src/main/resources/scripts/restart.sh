#!/bin/bash
echo "---------- server 发布 start---------------------"
#目前这个脚本需要单独放到linux服务器上去执行，通过jenkins调用，或是直接在jenkins中调用，需要修改
cd `dirname $0`
bash /usr/local/service/config-server/bin/stop.sh
rm -rf /usr/local/service/config-server*
mkdir -vp /usr/local/service/config-server
echo ' 远程服务器拷贝  '
scp   root@10.2.67.41:/home/jenkins/jobs/config-server/workspace/config-server/target/config-server-bin.zip /usr/local/service/config-server/config-server.zip
echo '服务拷贝成功,开始启动服务'
tar -zxvf  /usr/local/service/config-server/config-server.tar.gz -C /usr/local/service/
#unzip -u /usr/local/service/config-server/config-server.zip -d /usr/local/service/
mkdir -vp /usr/local/service/config-server/log
chmod 700  /usr/local/service/config-server/bin/*
setsid bash /usr/local/service/config-server/bin/start.sh start 10.2.67.31  1>/usr/local/service/config-server/log/log.out 2>/usr/local/service/config-server/log/logerr.out &
echo '启动服务成功！'
#第二步配合jenkins： 需要在jenkins执行脚本以出发实际执行shell脚本
#ssh  10.2.67.41    'bash  /usr/local/service/config-server/bin/restart.sh