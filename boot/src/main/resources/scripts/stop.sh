app_name=$1
app_version=$2
if
ps fx|grep $app_name |grep -v grep |awk '{print $1}'|xargs kill -9
then
echo "success"
else
echo "error"
fi
#./service.sh stop $app_name $app_version
#用法：./stop.sh config-server 1.2.1-SNAPSHOT