Simplfiy框架快速入门
====================

框架简述
---------------------
简述
:   框架主要用于简化和加快服务端应用开发：适用 “单web应用架构”，“分布式服务架构”，“微服务架构” 等。
:   从字面意义可以看出，目的就要精简：不过度设计，易于使用，不失功能的情况下，权衡性能和易用性。
:   框架整体基于组件化，插件化设计，组件和插件粒度尽量的细，并提供灵活的扩展机制。
:   为了更轻量级，框架在不影响性能的情况下，极度削减第三方库依赖，减少框架体积。

### 开发环境

#####依赖的软件及版本
* jdk版本：1.8.0以上  
* maven版本：3.3.3以上
* gradle版本 2.14以上(可选)

| WEB容器 | 版本 (low) | 是否必选 |  
| ------ | --------- | --------|  
| tomcat |      8.0+ |      否   |  
| jetty  |      9.2+ |      否   |  
     
######开发工具
> eclipse 或 intellij idea 常规开发建议使用后者

######开发环境搭建
> 安装jdk1.8 和 选择的IDE；IDE修改默认的jdk配置指向。
> 配置maven的全局settings.xml文件（并且eclipse等ide工具中，设置settings.xml文件的地址，这个步骤可选，不设置指定到默认地址）。
> setttings.xml文件可从框架源码中的src/doc/maven-common-config中获取，并修改相应私服地址，拷贝到maven仓库默认位置即可。
     
### 单web应用开发

*注*：开发过程建议基于IDE工具完成

 - 1.创建基于maven的web项目
    > 选择官方的`Archetype`为：`org.apache.maven.archetype:maven-archetype-webapp` 这个类型。
    > 如果`Archetype`选择本地自定义的，那么只需 按 第4步的补充代码步骤，写完代码，就可以jetty:run启动了，其他步骤掠过。
    > 自定义的archetype地址：http://10.2.81.208:8081/nexus/content/groups/public/archetype-catalog.xml，这是私服的地址，里面包含自己自定义的，还有大量非maven官方的archetype模版，如果是SNAPSHOT的，注意勾选包含SNAPSHOT才能看到自己定义的。
    > 本地没有.m2目录中没有archetype-catalog.xml的时候可以私服上手动下载，或其他地方下载过来的。也可以通过archetype:update-local-catalog 命令生成这个文件。无需私服上有这个archetype信息,也无需install。
	> 如果需要调试框架，或是分析框架和扩展框架功能，需要检出或下载框架源码并通过maven导入, [git地址](http://172.16.180.92/dev-team/simplify-framework.git)。
	
 - 2.在pom.xml中增加以下配置：
    - 设置jdk编译版本
    > 添加到节点 `<build> --> <plugins>` 里面,节点不存在就创建
    
    ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.5.1</version>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
        </configuration>
    </plugin>
    ```
    
    * 添加框架监听依赖
    > 删除已经存在的 `junit` 依赖，自带的版本过低，避免冲突。
    > 开启监听后，容器启动时会初始化框架
			
    ```xml
     <dependency>
        <groupId>vip.simplify</groupId>
        <artifactId>listener</artifactId>
        <version>1.2.5-SNAPSHOT</version>
     </dependency>
    ```
    
    * web项目需添加Servlet 3.1.0依赖
    
    ```xml
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>
    ```
		
    * 添加框架mvc组件依赖
    > 启用mvc组件后，使用相关api，可接收和响应用户请求
			
    ```xml
    <dependency>
    	<groupId>vip.simplify</groupId>
    	<artifactId>mvc</artifactId>
    	<version>1.2.5-SNAPSHOT</version>
    </dependency>
    ```

 - 3.配置日志
    
    - 添加日志依赖
    > 日志基于SLF4J,这里选择Logback作为日志实现
    
	```xml
	<dependency> 
		<groupId>ch.qos.logback</groupId> 
		<artifactId>logback-classic</artifactId> 
		<version>1.1.3</version> 
	</dependency> 
	``` 
    
 - 4.开始编写示例代码

    - 按照maven标准目录结构，在 src 目录下 创建 java目录
    - 创建一个Controller类名为 `TestController`，包名为 `com.example.demo`
    - `src\main\resources` 下增加 `properties`目录，并添加`config.properties`文件,内容如下：
    ```
    system.classpaths=com.example.demo
    ```
    - 补充完基础代码，如下：
    
    ```java
       package com.example.demo;
       import vip.simplify.mvc.model.Model;
       import vip.simplify.ioc.annotation.Bean;
       import javax.servlet.http.HttpServletRequest;
       import javax.servlet.http.HttpServletResponse;
       import vip.simplify.mvc.annotation.RequestMap;
       import vip.simplify.dto.Result;
       import vip.simplify.dto.ResultFactory;
       @Bean
       public class TestController extends BaseController<Model>  {
           @RequestMap
           public Result test(HttpServletRequest request, HttpServletResponse response, Model model) {
                return ResultFactory.success("测试成功!");
           }
       }
    ```

 - 5.启动系统

    - 使用tomcat启动
    > 建议下载tomcat的zip格式二进制包，解压直接使用
    > tomcat版本必须8.0及以上
    > 项目部署到tomcat
    > 启动tomcat ，访问 http://127.0.0.1:8080
       
    - 使用jetty启动
    > 可以使用和tomcat一样的部署和启动方式
    > jetty版本必须9.2及以上

    - 开发环境maven的jetty插件的使用
    > 为了快速开发调试，统一环境
    > 注意： jetty 8 ~ 9.1.6.v20160112 这些版本支持Servlet3.0的注解解析,但是框架需要jetty 9.2及以上版本,所以需要手动指定Fitler和Listener，配置中，多了一个defaultsDescriptor配置，就是做这个用的
    >  webdefault.xml 文件可以从框架源码中的demo模块中获取到
 
    > 增加如下jetty插件配置如下：
          
    ```xml
       <plugin>
          <groupId>org.eclipse.jetty</groupId>
          <artifactId>jetty-maven-plugin</artifactId>
          <version>9.3.18.v20170406</version>
            <configuration>
                <webApp>
                  <defaultsDescriptor>${project.basedir}/src/test/resources/jetty/webdefault.xml</defaultsDescriptor>
                  <contextPath>/</contextPath>
                </webApp>
            </configuration>
        </plugin>
    ```
          
    > 启动命令 `jetty:run`

### 分布式web应用开发

>依赖dubbo服务，预先安装zookeeper用于注册 和 dubbo-admin用于监控管理，dubbo-monitor可选安装

#### 单web应用改造开发

-原来的demo-web项目拆分如下：创建类型为pom的maven父级项目，并创建三个模块，项目分别是demo demo-api demo-server demo-web

#### rpc客户端api库开发

#### rpc服务端应用开发
 -1.创建类型为jar的maven项目，或是基于指定的archetype来创建，那么后续的步骤可以忽略
 -2.创建config.properties文件，增加类路径扫描设置，配置如下
 -3.创建dubbo.properties文件，具体配置如下
 -4.pom文件增加相关依赖配置项
 -5.开始编写业务代码
 -6.开始编写单元测试
 -7.接下来需要测试启动服务是否正常，并配置发布环境，最终打包给生产和测试环境用
     a.增加logback.xml的配置
     b.增加META-INF来描述打包的信息
     c.pom文件增加打包和启动测试的插件信息
     d.使用exec:java启动服务，并通过客户端测试
     e.使用install deploy打包发布，生成发布包


### 框架常见问题

#### 打包问题

   - 1.没有发现类定义错误 : (java.lang.NoClassDefFoundError：org/apache/commons/pool2/impl/EvictionConfig)，比如:20-Jun-2017 11:46:34.176 INFO [commons-pool-EvictionTimer]  Illegal access: this web application instance has been stopped already. Could not load [org.apache.commons.pool2.impl.EvictionConfig]
Exception in thread "commons-pool-EvictionTimer" java.lang.NoClassDefFoundError: org/apache/commons/pool2/impl/EvictionConfig

     > 问题现象分析：1.初步判断是因为类没被加载导致，导致这个问题的原因，a.可能是由于jar包丢失，所以被没加载，那么检测服务器的对应报错类的jar包是否存储，发现不存在，判断是jar包冲突，这是需要检查是否有不同版本的jar被引入，先检查开发环境，发现正常，没有重复jar包，或是不同版本的重复jar，开发环境也没上面错误，那么检查是否服务器环境冲突，看看给服务器的war包是否有问题，检查发现war包中包含了不同版本的jar包，导致服务器上问题发生。
     > 原因：这是由于打包时，没使用clean install，而是直接install，导致旧的包和新的包一起被打包到war中，导致包冲突。
   - 2.RPC服务端打包注意实现：
     > 在修改服务端软件版本的同时，记得修改  src/main/resources/META-INF/MANIFEST.MF 文件,主要用于jps，java -jar 用于识别元数据信息。
     > 另外一个重要的问题，用于dubbo识别软件版本，如果服务端的Implementation-Version 的版本（需和dubbo的revision匹配，待确认）对不上，会导致dubbo客户端注册失败，无法成功连接到dubbo服务端（待确认是否dubbo的问题，就算版本对不上，应该可以注册成功，只是多了一个版本信息，不匹配而已，现在是直接看不到客户端的注册信息，而且后台也注册成功，没报错，导致问题排查麻烦）
   
### 中文乱码问题

   - 1.中文表单post提交后，业务请求端请求数据异常，但是后台出现乱码
     > 问题现象分析：中文到后台通过request.getParameter("userName")获取后得到的数据是这样" é<99><88>å<95><9f>æ<9b> " 的特殊字符，无法获取正常中文值,变成iso8859-1编码的值。这个问题可能会引发其他一些问题，比如导致JsonUtil做Json转换，由于特殊字符，JsonUtil会把这个表单乱码的数据转换成unicode编码"é\u0099\u0088å\u0095\u009Fæ\u009B´"，导致问题被隐藏，误以为是JsonUtil转换导致的乱码
     > 原因：由于tomcat容器导致的，tomcat在linux会有如上错误，window没测试，window使用jetty测试，jetty在window没有这个问题
     > 解决方法：通过request.setCharacterEncoding("UTF-8")来解决，但是不起作用。那么需要只能通过执行
                 new String(userName.getBytes("ISO-8859-1"),"UTF-8") 来解决，为了完全解决这个问题需要在框架中全局设置
     > 注意考虑的点：会不会影响到其他容器的编码，比如改好了tomcat的，影响了jetty和其他容器的编码，因为编码最好只转一次，转多了容易出乱码
     
     
 ## RPC 问题
 
   - 1.dubbo服务无法注册到zookeeper中的问题，有可能是客户端，也可能是服务端，偶尔又注册成功，偶尔不行，注册成功，调用又失败 
      > 现象一：在框架版本是1.2.4-SNAPSHOT的时候，dubbo服务在 测试环境，本地开发环境，都可以正常使用，但是当集成spring后，就会导致无法注册到zookeeper中（1，有可能是和spring冲突。2，group导致的问题，在zookeeper有两个节点，一个dubbo，一个dev，之前配置文件的group值为dev，注册的时候会注册到group为dubbo的节点，查询是group为dev的节点，有可能是框架问题，待测试）
      > 现象二：框架版本从 1.2.4-SNAPSHOT升级到1.2.5-SNAPSHOT后，导致dubbo服务无法注册到zookeeper中，后面来回调整dubbo配置文件，
      > 启动日志中，没有任何报错信息，只能看到empty://这样的空协议，并且还比较隐蔽，另外一个现象就是 注册的zookeeper节点地址 有些是 /dev/com.xxxx,有些是/dubbo/com.xxxx
      > dubbo admin中，有时能看到注册信息，有时看不到，特别不同的机器，不通框架版本，不同的环境
      > 当dubbo admin 两边都能看到提供者和消费者的注册信息时，访问也会com.alibaba.dubbo.rpc.RpcException: Forbid consumer 10.0.53.69 access service com.xxxxService from registry 10.0.50.150:2181 use dubbo version 2.5.3, Please check registry access list (whitelist/blacklist).
      > dubbo代码中是RegistryDirectory类的 public List<Invoker<T>> doList(Invocation invocation) 这个方法报错
      > 解决的办法是：1.由于dubbo admin看不到注册信息的详细情况，所有打算从数据源查起，也就是zookeeper数据，但是线上的zookeeper连不上，并且注册信息过多，容易隐藏错误，增加排查效率
      > 2.所以本地通过telnet连上线上zookeeper，分析zookeeper版本，在telnet使用zookeeper提供的 ruok，dump,conf,kill,cons,envi reqs,wchs,wchp等命令查看到zookeeper的版本等信息 
      > 3.下载zookeeper官方相关的版本2.4.6
      > 4.本地的dubbo配置全改成本地的zookeeper
      > 5.通过zookeeper的zkCli.cmd 的相关命令 "ls /" 和"ls2 /" 来分析节点情况，通过 get和set来查看具体节点信息
      > 结果是：分析本地zookeeper时，发现有dubbo和zookeeper，dev等三个根节点，而且dubbo这个节点后面才出来的，一开始没有，这个就是问题所在了
      > dubbo注册的时候，应该只会写到dev这个组里，怎么会产生dubbo这个能，所以把group的配置也改成dubbo，问题解决
      > 问题的本质是这样的：从1.2.4-SNAPSHOT切换到1.2.5-SNAPSHOT的会出现问题的原因在于三个方法：
      > 第一个方面是：由于新版的1.2.5-SNAPSHOT版本，把dubbo.properties配置文件移到外层的classpath的根目录下面,
      >  而dubbo的jar包默认会读取classpath下面名字为dubbo.properties的文件，不管集成的时候，RPC模块是否有设置group的值，dubbo框架都是自动设置，但是RPC模块这是如果也设置group的值，就不会导致这个问题了，刚好RPC模块没有设置，
      >  所以导致了部分设置了group为dev，而部分没有，才用group为dubbo的值，所以不紧紧是group这个值会受到影响，dubbo.properties的其他属性也要收到影响，所以既然dubbo.properties已经配置了属性，那么RPC模块也一定要设置
      >  dubbo框架中自动读取dubbo.properties的源码位置[com.alibaba.dubbo.common.utils.ConfigUtils] 读取dubbo.properties证据所在
      > 第二个方面是：是由于RPC模块没有设置group的值，但是dubbo.properties中又提供了
      > 第三个方面是：集成spring后，会出现连接不上zookeeper的问题，待确认
      > 最终解决的方法是:  把dubbo.properties移动到底层目录，或是RPC模块对dubbo。properties中的属性都做相应设置，没有的选项，就注释掉
      
 ## IOC注入问题
   - 1.由于部分属性注入失败，导致BaseController中的属性也注入失败，出现空指针问题，
     > 比如这个注入失败 @Inject private ICacheDao<String, Object> cachedDao = CacheProxyDao.getCache();,ICacheDao有多个实现类，所以报错；另外这里不应该注入，因为已经有赋值了
     > 会有以下错误BaseController类中的 @Inject private DelegateController<T> delegateController;属性也注入失败，为空指针
     
 ## 缓存问题
   - 1.ICacheDao处理复杂带泛型的对象，会导致无法存储的问题，并框架没报错，如果发现这个问题，可以使用IJsonCacheDao先代替，这个复制对象处理的功能后续解决
   - redis模块增加  redis cluster的处理  ，具体开启，需要在 redis-pool.properties中设置officialCluster=true，并且需要指定cluster模式特有的几个配置项
   connectionTimeout=1000
   soTimeout=1000
   maxAttempts=100