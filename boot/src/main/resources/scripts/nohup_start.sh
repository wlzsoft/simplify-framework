rm -f nohup.out
#以下ip在多网卡的情况下需要指定具体网卡的ip地址，否则可以不写。
STDOUT_FILE=$DEPLOY_DIR/logs/stdout.log
nohup ./startup.sh start 10.2.67.28 > $STDOUT_FILE 2>&1 &
