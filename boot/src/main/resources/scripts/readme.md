警告：app.vmoptions文件的java虚拟机启动选项要谨慎配置，这些配置会严重影响到应用的性能，如果不知道具体选项的作用，请不要做任何设置，全部默认为空，把所有选项删除，因为在不同的虚拟机版本，包含之前和未来版本，选项的可能会做更改，对性能有好处的项目会被默认开启，有问题的选项会被默认关闭，特别是-XX的选项，改动更大,不通厂商的虚拟机实现，这些选项也各不相同

*和jenkins集成方式： 1.创建一个shell类型的jenkins任务，加入以下配置(其中xxx为项目名):
ssh 10.2.67.41 "bash  /usr/local/tomcat/xxx/bin/restart.sh   /usr/local/tomcat xxx  root@10.2.67.40:/home/jenkins ${BUILD_ID}"
                    2.要提前复制restart.sh到tomcat目录中
                    3.后续配合docker，做到更好的集成，避免手写地址和项目名