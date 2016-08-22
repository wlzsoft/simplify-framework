# 框架待完善功能
### 1. 准备集成gradle
   1. 安装gradle的eclipse官方插件：https://github.com/eclipse/buildship/
   2. 这里是列表文本2.安装gradle工具，配置环境
   3. maven转gradle
   4. 初步调试基于gradle的配置
   
### 2. 集成docker
   使dubbo服务变成一个一个的docker容器，mycat服务，mysql服务，tomcat，resin等，集成版的jetty，和webserver等等。
    *参考文档：
              http://dockone.io/article/442，http://blog.sina.com.cn/s/blog_72ef7bea0102vwhr.html
              http://www.open-open.com/lib/view/open1439793616442.html
              http://developer.51cto.com/art/201404/434879.htm
              http://www.csdn.net/article/2015-07-21/2825266
              file:///C:/gradle-2.14/docs/userguide/userguide_single.html
              
### 3. 分析tomcat无法解析jstl的问题，并编写文档，避免重复问题出现
### 4. 扩展框架controller的处理机制，支持labda表达式
### 5. 修复install和执行单个test-compile命令的时候，只会编译java文件，不会编译其他配置文件，比如txt文件等。
### 6. markdown 语法学习，重新编写readme文档
### 7. github的个人github pages的站点创建
### 8. 类似github的git服务端搭建
### 9. 提供stream视图处理方式，可以处理小数据流
### 10. 为stream视图提供新的处理流的方式，可以定制buffer的大小，处理大文件，可以分块传输
### 11. junit5 基于java8，目前开发到里程碑1.0版本，可以考虑testng，spock
### 12. 可以提供以方法和类名为准，只需要指定@RequestMap就可以，不需要指定@RequestMap(path="xxxx")的映射方式，减少用户的重复编码
### 13. 提供RequestMap的默认设置，path值可以不设置，那么path最终的值以所在的类和方法的名称为准
### 14. RequestMap增加isStatic属性，用来处理静态请求，加速请求
### 15. dml语句 创建表的语句提供  主键检测设置，自增检测设置，表引擎设置，表字符集编码设置
### 16. update功能提供根据其他字段进行更新数据，而不仅仅是主键才可以
### 17. update要提供where自定义功能，不紧紧是等于的操作符,至少支持in 大于和小于等操作，目前只有等于
### 18. 用clone代替new，提高性能,所有使用new的地方都是用clone， 同时提供clone工具类，用于代替普通业务系统中的new操作
### 19. 提供clone工具类：不仅仅用于克隆自己的对象，也可以用于克隆子类对象（其实严格意义来说包含父类对象的所有属性的值的子类）
### 19. RequestMap的地址不能重复，如果有重复地址，需要在启动时报错，并提醒url重复
### 20. 分页bug修复
### 21. dao针对单表：提供 多条件update，多条件delete，多条件查询 ，注（多条件）：支持等于，不等于 ，大于小于等等操作符
### 22. dao针对单表：提供 更灵活的结果集，可以选择查询只查特定字段，提供filter方法，指定查询过滤的字段及属性，属性的接收，默认支持单个属性，比     如List<String>列表中一个属性的集合，List<Map<String,Object> 多个属性的集合，List<T> 多个属性的集合，T可以是对象，dto等等。

### 1.2.0版本规划
1. 支持docker微服务部署，配合webserver，可以更灵活的监控服务的运行，更灵活的持续集成。
2. dao支持更灵活的单表操作，考虑更广泛的使用场景。
3. ioc支持jmx，提供更可控的bean实例管理，可通过jconsole或是visualvm等工具监控查看。
4. 支持默认代理模式，减少重复代码
5. 支持插件模式，首先剥离 模版引擎的特有第三方依赖，以插件方式做到可选，定制插件顶级类
    剥离连接池，使框架不依赖于druid这个特定连接池
6. 重构异常处理模块和视图路由模块，使用双重分配机制来实现，同时引入访问者模式。用以减少instanceof关键字的使用。提供框架的可扩展性，减少耦合，更符合开闭原则。
7. 需要做基准测试（对比测试），以确保引入新特性对性能的影响。test目录可以独立出bearchmark文件夹。
8. 提供冒烟测试(bvt),用以确保功能总体可用。避免长时间运行完整的单元测试。