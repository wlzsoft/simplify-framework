rm -f nohup.out
#以下ip在多网卡的情况下需要指定具体网卡的ip地址，否则可以不写。
nohup ./startup.sh start 10.2.67.28&
