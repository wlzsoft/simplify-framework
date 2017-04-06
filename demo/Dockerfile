FROM 172.16.85.146:8884/com.meizu.simplify/simplify-base:1.2.1-SNAPSHOT
COPY target/demo /usr/local/tomcat/webapps/ROOT/
CMD ["catalina.sh", "run"]
#暴露容器8080端口给宿主机，如果是父级镜像，如tomcat，已经暴露过端口，那么无需下面设置，如果是基于simplify框架的webserver，那么需要以下设置
EXPOSE 8080