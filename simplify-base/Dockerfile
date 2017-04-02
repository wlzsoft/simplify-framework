#由于java有授权问题，又没有官方的docker镜像，只有openjdk和openjava相关的官方镜像，所以要使用oracle的jdk，那么只能选择最小的ubuntu镜像(centos比ubuntu大)，加上oracle官方下载的jdk软件的定制镜像了
FROM 172.16.85.146:8800/ubuntu
#FROM ubuntu:14
#COPY target/demo /usr/local/tomcat/webapps/ROOT/
#ADD target/lib /data/lib
#ADD start.sh /data/
#ADD lib/jdk-1.8.0_77-2.el6.x86_64.rpm /jdk-1.8.0_77-2.el6.x86_64.rpm
#RUN rpm -ivh /jdk-1.8.0_77-2.el6.x86_64.rpm
#ENV JAVA_HOME /usr/local/jdk1.8.0_77
#ENV PATH $PATH:$JAVA_HOME/bin
#RUN chmod +x /data/start.sh
#CMD ["start.sh"]