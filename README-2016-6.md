# 框架待完善功能
###1.准备集成gradle
   1. 安装gradle的eclipse官方插件：https://github.com/eclipse/buildship/
   2. 这里是列表文本2.安装gradle工具，配置环境
   3. maven转gradle
   4. 初步调试基于gradle的配置
   
###2.集成docker
   使dubbo服务变成一个一个的docker容器，mycat服务，mysql服务，tomcat，resin等，集成版的jetty，和webserver等等。
    *参考文档：
              http://dockone.io/article/442，http://blog.sina.com.cn/s/blog_72ef7bea0102vwhr.html
              http://www.open-open.com/lib/view/open1439793616442.html
              http://developer.51cto.com/art/201404/434879.htm
              http://www.csdn.net/article/2015-07-21/2825266
              file:///C:/gradle-2.14/docs/userguide/userguide_single.html
###3.分析tomcat无法解析jstl的问题，并编写文档，避免重复问题出现
###4.扩展框架controller的处理机制，支持labda表达式
###5.修复install和执行单个test-compile命令的时候，只会编译java文件，不会编译其他配置文件，比如txt文件等。
###6.markdown 语法学习，重新编写readme文档
###7.github的个人github pages的站点创建
###8.类似github的git服务端搭建
###9.提供stream视图处理方式，可以处理小数据流
###10.为stream视图提供新的处理流的方式，可以定制buffer的大小，处理大文件，可以分块传输
###11.junit5 基于java8，目前开发到里程碑1.0版本，可以考虑testng，spock