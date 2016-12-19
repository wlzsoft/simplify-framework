if
ps fx|grep config-server|grep -v grep |awk '{print $1}'|xargs kill -9
then
echo "success"
else
echo "error"
fi