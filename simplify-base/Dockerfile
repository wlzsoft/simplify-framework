#由于java有授权问题，又没有官方的docker镜像，只有openjdk和openjava相关的官方镜像，所以要使用oracle的jdk，那么只能选择最小的ubuntu镜像(centos比ubuntu大)，加上oracle官方下载的jdk软件的定制镜像了
FROM 172.16.85.146:8800/ubuntu:16.04
#FROM ubuntu:16.04
#ADD server-jre-8u121-linux-x64.rpm /server-jre-8u121-linux-x64.rpm
#RUN rpm -ivh /server-jre-8u121-linux-x64.rpm
#ADD server-jre-8u121-linux-x64.tar.gz /server-jre-8u121-linux-x64.tar.gz
#RUN tar -zxvf /opt/ /server-jre-8u121-linux-x64.tar.gz 
#ENV JAVA_HOME /jdk1.8.0_121
#ENV PATH $PATH:$JAVA_HOME/bin
#ADD start.sh /data/
#RUN chmod +x /data/start.sh
#CMD ["start.sh"]