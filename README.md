#框架功能列表及bug修复情况：
###1.连接池的连接的控制，目前是否出现连接泄露，需要监控起来==>>已测试，已通过，案例[druid自带管理后台+相关druid配置调整和连接回收到连接池处理] 2016/2/25
###2.分页查询的封装==>>已测试，已通过，案例[DaoTest] 2016/2/25
###3.测试一下多表负责查询是否有bug，测试相对复杂点的sql语句==》已测试，已通过，案例[TestController.adoTestSelect2方法] 2016/2/24
###4.map方式的查询的处理和实现==》已测试，已通过，案例[TestController.adoTestSelect3方法] 2016/2/24
###5.多表复杂查询返回实体的方式需要优化：目前依赖了jpa标准，无法灵活注入，对于查询，其实可以忽略jpa标准的==》已测试，已通过，案例[新增SearchByMapDao,并集成到BaseDao中] 2016/2/24
###6.```
public Page<T> findPage(Integer currentPage,Integer pageSize,String sort, Boolean isDesc,String sql,Object... params)
```==>> 已测试，已通过，案例[DaoTest.s2_findPageMutilSql3Test] 2016/2/26
###7.提供可选字段的update操作==>>已测试，已通过，可再优化(更细致的字段控制)，案例[新增update(T t,Boolean isAllField)] 2016/2/26 
###8.IdEntity 的id改成了fid，业务需要，后续可以考虑更优雅的处理方式==》已测试，已通过  2016/2/25
###9.BaseEntity 的delFlag改成deleteflag业务需要==》已测试，已通过  2016/2/25
###10.缓存连接池及连接的问题，采用回调方法和内部类重构redis缓存，统一管理连接的回收到连接池的问题==>>已测试重要部分，已通过 2016/2/26
###11.缓存操作，基于注解的读取缓存功能，好像失效，需要查看下==》已测试，已通过 2016/2/26
###12.改造拦截器，可支持拦截返回，而不只是拦截处理,并修复缓存读取无法返回的问题==>>已测试，已通过2016/2/29
###16.mvc的json方式的ajax方法的处理和优化==>> 已测试，已通过 2016/3/1
###21.webserver 精简初版==>>已测试，已通过 2016/3/2
###25.webcache的实现，从旧版框架中移植之前的代码，并优化和正式启用==>>已测试，已通过 2016/3/3
###26.mvcinit类 需要重构下，以便和webcache等模块分离==>>已测试，已通过 2016/3/3
###28.webcache的removespace的功能的实现：目前这个压缩过程，有bug，会丢失有效的文本信息==>>已测试，已通过 2016/3/3
###29.aop日志打印太过详细的问题==>>已测试，已通过[由于jetty本身的问题，jetty底层依赖slf4j，如果有相关的slf4j，那么就会启用slf4j的日志，导致输出日志很详细，可以在javaagent中删掉slf4j，那么就会用回jdk的log模块，日志就很少]2016/3/4
###30.对非正则表达式的url的映射解析的404问题的解决==>>已测试，已通过 2016/3/4
###31.对参数@RequestParam的解析的测试==>>已测试，已通过2016/3/4
###32.基于jetty使用框架时，第一次请求性能耗费了800ms左右的时间==>>已测试，已通过，jetty本身自己的问题，第一次访问会很慢（估计jetty第一次访问会初始化一些数据） 2016/3/4
###35.自动创建表的实现==>> 已测试，已通过 2016/3/16
###37.mvc的页面异常处理  ==>> 已测试，已通过 2016/3/16
###39.使用invokedynamic(1.函数式编程的性能基础,2.对其他语言，比如js的支持，NashornJavaScript，java版nodejs的实现)或是动态代理(cglib)，或是java8的函数指针，或是启动时修改class内容的方式来提高mvc请求时解析的性能 ==>> 已测试，invokedynamic未通过，NashornJavaScript已通过(性能不好),cglib的动态代理未测试 2016/3/16
###40.使用lambda表达式简化编程，优先改造内部类的使用方式==>>已测试，已通过 2016/3/16
###41.使用 java.time代替部分date的操作==>>已测试，已通过 2016/3/16
###42.对脚本语言的支持,直接支持或间接支持,扩展java没有的高级特性的支持：Scala(待解决),ruby(延迟), groovy(待解决), javascript(已解决，普通js处理性能90ms，pc机)
###43.对rest请求方式的多视图的支持，目前支持通过.json 和非.json后缀的请求的视图，分别是json视图，html页面视图(支持velocity和jsp方式)==>> 已测试，已通过 2016/3/18
###44.对rest请求方式多视图支持的异常处理 ==>>已测试，已通过 2016/3/18
###47-1. httl现代模板引擎已经集成-http://httl.github.io/zh/syntax.html ==>> 已测试，已通过 2016/3/21
###47-2. velocity传统模板引擎已经集成 ==>> 已测试，已通过 2015/11/25
###47-3. beetl现代模板引擎已经集成 ==>> 已测试 ,已通过 2016/3/21
###50.mvc请求地址注解配置方式：直接类级别的一级地址前缀配置 ==>> 已测试，已通过 2016/3/21
###27.PropertieUtil类需要重构下，目前有多个实例的冗余，[1.可以根据配置文件的不同，创建不同实例，2.也可以通过配置文件自动合并，只生成一个实例。推荐第二种方式]==>> 已测试，已通过 [目前这两种方式都支持] 2016/3/28 
###53.调整配置信息读取方式，合并多余的properties实例==>>已测试，已通过 2016/3/23
###54.提供配置信息单个配置注入功能==>>已测试，已通过 2016/3/28
###55.支持配置信息扩展功能，通过ReloadableResource注解，就可以实现配置文件和实体的映射,并生成单例==>>已测试，已通过 2016/3/28
###56.RequestParam调整了实现方式，需要测试==>>已测试，已通过 2016/3/25
###57.页面缓存做了调整，防止并发导致的问题,需要测试调整后的可用性==>>已测试，已通过 2016/3/25
###66.IdentityHashMap实现了HashMap的功能，但能避免HashMap并发时的死循环,高性能循环使用ConcurrentSkipListMap，
ConcurrentHashMap代替IdentityHashMap和HashMap。IdentityHashMap有其特殊用途，比如序列化或者深度复制。或者记录对象代理。
举个例子，jvm中的所有对象都是独一无二的，哪怕两个对象是同一个class的对象，而且两个对象的数据完全相同，对于jvm来说，他们也是完全不同的，如果要用一个map来记录这样jvm中的对象，你就需要用IdentityHashMap，而不能使用其他Map实现。==>>已测试，已通过 2016/3/29
###68.正则表达式和非正则表达式的urlcache的map容器要区分开来，正则表达式的容器改成ConcurrentSkipListMap，非正则表达式如果结果集大，可以分片。==>>已测试，已通过 2016/3/29
###69.提前分析好rest的几种后缀，存起来，空间换时间==>>已测试，已通过 2016/3/29
###70.解析指定不同视图的处理器，需要和68及69一同配合解决 ==>>已测试，已通过 2016/3/29
###51.提供更灵活的根据url后缀更换视图的方式，并使用单例的视图对象==>>已测试，已通过 2016/3/30
###52.提供更高性能的一个controller只对应一个地址，这样的controller必须实现特定接口，而这个接口的方法，就是controller的请求映射的方法(没有这个必要，使用代码生成代替反射就可以了)==>>已测试，已通过 2016/3/30
###65.视图层优化方案：*相关非模版的实现类的方法都使用静态方法，其他模版的类使用单例，减少对象的创建
                               *使用过程中，除了特殊的String类型返回值（[url,redirect,forward 等特殊类型，例子：url:userList,redirect:www.baidu.com]，这是可选方案，也可使用强类型标记-ITemlate，只是用于标记处理结果的类型-比如url，redirct或是forward），其他的类型包含普通的string类型，都当成输出数据处理
                               *考虑视图的指定方式，有rest风格后缀的方式，视图全局配置。如果需要同时使用多个html模版视图，那么可以使用ITemplate.
                               *考虑如果同个地址，不通视图，然后有不通的业务逻辑，那么这时可以提供一种特殊后缀，专门用于处理特殊逻辑（针对页面的）==>>已测试，已通过 2016/3/30
###60.优化配置的功能配置信息的读取，使用注解的方式来标注一个pojo，作为配置文件的一个映射，并且支持热加载，还要支持单个属性注入的方式，通过@Config注解 ==>>已测试，已通过 2016/3/29
###71.ErrorView和ioc的Message整合成一个异常处理业务，用于包装throw new 的显示错误信息==>> 已测试，已通过 2016/3/31
###72.Template相关的类都使用bean标注，都是单例==>>已测试，已通过 2016/3/30
###73.通过视图配置文件，来觉得默认视图，通过controller返回的url来判断是否是字符串，是就返回到页面，不是，就解析url，觉得使用配置文件中的默认视频，或是显示指定视图方案，通过url头标识，配置文件中提供默认视图，和其他目前有的，需要支持的视图的指定==>>已测试，已通过 2016/3/30
###74.redirect和json和message会使用默认的代码中硬编码的方式匹配==>>已测试，已通过 2016/3/31
###75.模版引擎只做渲染的事情，不要收到request和reponse等servlet容器相关的api的侵入==>>已测试，已通过 2016/3/31
###78.包装异常信息，封装到Message类中，包含各种类型http状态码的提示,208,300,和默认的500,在最外层异常处理时，需要默认解析，并可覆盖状态码==>>已测试，已通过 2016/3/31
###80.Message提示信息，不要在控制台打印异常信息，因为他们只是提示信息不用使用BaseException,另外定义一个Excpetion，叫MessageException ==>> 已测试，已通过 2016/3/31
###77.@InitBean注解的实现==>>已测试，已通过 2016/3/31
###79.dao模块，要考虑实体为非数据库表的对应实体的情况==>>已测试，已通过 2016/4/1
###81.支持服务端多视图响应式设计方案：响应视图，支持设备类型和屏幕尺寸做响应，若要更好体验，需要通过前端工程师和设计师配合==>>已测试，已通过 2016/4/6


###47.模板引擎的性能问题，改用现代模板引擎 (1.模板引擎将模板文件编译成class运行
  			   2.模板中的静态部分采用二进制输出，不需要CPU运行的时候再转码
               3.合并模板中的静态部分一起输出，而不是每一行每一行输出 
               目前的现代模板引擎有 smarty4j(没有专业团队维护，移植于php的smarty)或是beetl(推荐)，或是httl
###48.用velocity来合成json串，比直接用那个JSONArray那个快很多，但是瓶颈还在这块，因为我们数据都读到内存里。如果直接拼StringBuffer会快1/3. 更换模板引擎可以提高一下。因为你停留在一个页面，行情列表是需要不停动态刷新的。velocity性能不够理想
###49.SPI机制实现-用以替换和优化java的实现类发现机制，而不是通过扫描某个路径下的class文件的集合的方式,这是一种hack方式，直接用java自己的spi机制，通过ServiceLoader类实现,使用spi而不是api，可用于框架实现插件机制，接口位于调用方一端,考虑视图层使用spi插件机制
66.fastjson和jackson是否有提供java8 的 stream api 的json生成方式，毕竟数据库集大的时候，串行处理会有严重性能问题
   已经确认在fastjson-1.1.32版本中开始提供Stream API
http://www.csdn.net/article/2014-09-25/2821866
###66.json处理框架fastjson ，所谓序列化是很好的序列化成json字符串的性能。
   框架中是否缓存序列化后的二进制数据到redis中，是否可以先把数据转json再序列化或是存储，是否会节省redis空间
###64.考虑是否把javasist修改字节码的方式去调用，直接用jdk自带的功能，启动时生成特定源码并编译再启动 http://www.importnew.com/12548.html
###18.配置文件热加载实现==>>优先实现
###58.为了提高性能，尽量减少，请求过程中对象的创建，特别是，没必要的无意义的对象的创建。
###38.考虑使用Collection中的Stream的并行接口操作集合(注意这个方式，只能遍历一次)来提高大数据json并行解析的速度，特别是数据集大的时候。
###61. 需要实现网络通讯模块，类似netty和mina，可用于物联网，互联网，服务化，远程方法调用的使用，用于http的实现，websocket的实现，自定义协议的实现，rest服务端实现，web容器的实现
###14.压力测试业务请求的性能表现:性能指标，模拟mvc的tomcat，jedis，druid的性能测试，着重是mysql的压力导致tomcat挂掉，而不是mysql挂掉，检查下，是否应该mysql客户端连接频繁重试导致的tomcat挂掉，这时候用的线程资源和连接都是tomcat消耗，需要确认是否是这个问题
###13.事务做更精细化控制，不需要在代码里面硬编码==>>已测试，已通过 2016/2/29 待解决问题：事务重叠问题
###15.修复部分数据库相关没释放的问题，比如result和statement的关闭(druid连接池，是否有管理resultset和statement的关闭和创建)，需要测试，配合14点的压力测试，查看效果
###17.拦截器模块修改成可配置的方式，通过配置文件的方式，或是注解的方式
###19.极速启动实现==》暂缓
###20.jdbc多数据源及数据分片，主从分离等功能实现，建议以mycat中间件来做这个事情，但是也可以内置实现，部分需求可以没必要使用mycat==》可不实现
###24.websocket的实现，可用于集成webserver和消息服务中间件
###33.webcache的ClearCommentUtil.clear中要清除的html页面中，全部用html5的头声明(<!doctype html>)，否则声明头会被清除掉一部分
###34.mvc性能优化，修复请求过程中使用反射的问题,可以使用动态代理，或是修改字节码的方式
###36.连接超时，自动重连功能	
###45.整合proguard的特有的东西，还有fha的支持的微信等等的功能,smarty4j使用maven的本地库模式
###46.减少mvc请求时，对象的频繁创建，控制jvm的对象数量，特别是频繁的对象创建和销毁
###80.评估下properties/message.properties 等配置文件，如果不存在，是否会导致致命错误，或是配置为空就行




###76.sql解析需要加上缓存，可以启动测试模式，不需要缓存，sql可以使用template引擎来渲染
80.重构模版引擎：使模版引擎不仅仅使用于html页面，不要依赖servlet的api，可用于解析其他模版，比如sql模版
67.提供识别get和post请求的处理，基于rest风格相关的请求处理提供rest风格的操作，比如支持option 和 delete 等操作update和put等操作，补充get和post的不足，考虑简单的controller都可以不写，在basecontroller中提供默认的通用模块的操作，类似basedao的功能。
使用api：```import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
Files.write( new File( "src/impls/ObjectWithCommands.java" ).toPath(), generateObjectWithCommands( commands ).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );```生成代码
62. 使用代码生成代替反射机制，可用javasist等类库，需要优化mvc和dao中使用反射的地方，特别优先优化baseController
   优先使用代码生成的方式实现（生成的代码要注意，不要使用ifelse语句来判断地址并定位方法，使用直接方法调用，而是使用switch语句，性能更好【http://blog.csdn.net/kehui123/article/details/5298337】）
63.测试下switch的性能是否比ifelse高许多测试下是否（的确switch的性能更优），equalsIgnoreCase的性能比equals性能高许多，参考http://www.deepinmind.com/%E6%80%A7%E8%83%BD/2014/06/26/string-switch-performance.html
http://fishermen.iteye.com/blog/430286
附件在微云网盘中的stringSwitch
83.maven-site插件使用(命令:clean site site:stage)，及附属插件配置 1.javadoc,2.skin(皮肤),3.template(velocity模版文件),4.site.xml配置
84.mavent-site插件支持：生成word文档，生成pdf文档，生成ppt文档，生成html文档
85.TODO 注意使用下面的方式，可能设置头信息(addHead) ,会丢失，无法发送到浏览器的reponse头中
   response.getWriter().print(message);
86.if(!AjaxUtils.isAjaxRequest(request)) {//TODO,不是判断ajax的唯一方式，有可能是ajax请求，但是不带这个头信息
87.重构请求方法反射调用部分代码，便于代码生成集成
88.代码生成功能实现，用于优化反射代理的代码,这块代码在生成环境启用
89.网页加载的进度条：在body的最上方出现
90.使用异常对象池的概念来解决频繁创建异常对象的开销
91.异常对象池配合使用ThreadLocal来解决业务信息的传递
92.由于正则表达式解析性能很差，解决方案A.使用普通方式匹配参数路径。解决方案B.缓存正则表达式的编译过程，减少编译的成本。解决方案C.避免使用。解决方案D.结合实际使用，可以第一次匹配到的路径做缓存一段时间。在这个时间内，这个路径会在指定的map中命中
93.dao模块，中文插入乱码的问题，排查及解决==>> 已测试，已通过 2016/05/04 [由于jdbc连接字符串适用了&amp;转码导致的问题，在spring中需要转码，但是config.properties中不需要]
94.日期SimpleDateFormat需要安装日期格式缓存起来，不要重复创建==>> 已测试，已通过 2016/05/04
95.分页问题：目前只考虑到pc端分页，没有考虑移动端分页。所以要考虑多种分页变种的解决方案
96.jsp的模版处理有问题，全部无法处理，需要检查并解决掉
97.目前需要对aop.properties手动进行拦截方法的配置，很是繁琐，解决方案是：任何模块（比如Cache）实现了IInterceptor接口的实现都需要提供对aop.properties的配置生成功能,aop.properties可以使用模版引擎来处理，后续aop.properties对开发人员透明
98.确认eclipse是否有类似sublime的多行选择功能
99.redis-集群优化：基于分布式集群的测试，减少redis单cpu线程的阻塞影响，可用codis或是官方的redis cluster来测试，并选择可靠方案
100.redis-程序优化:不要所有的缓存都使用通用的二进制数据存储，比如即时排序，就不行。部分情况为了优化性能：可以采用双层hash来优化内存
或是通过多个hash结构来分摊存储所有记录,有性能要求的地方，要按需求来优化处理，选址合适的数据结构
101.使用了反射待优化的类：AnalysisRequestControllerModel
102.主机对运行时使用instanceof的地方做调整优化，使用多态来代替，对于无法使用多态的场景，那么再考虑其他方式来处理
103.抽取出listener模版，可以简化web项目的集成
104.dao模版的sql编写还是比较繁琐,需要简化
105.封装代码生成模版，并抽取到公用的模块中 ==>>已测试，已通过 2016/5/12
106.修复ioc框架的spi识别机制:必须有淘汰错误对象，错误接口的能力，防止数据乱入
107.考虑 java的反序列化攻击:http://itindex.net/detail/54975-java-%E5%BA%8F%E5%88%97%E5%8C%96
108.提供Fst , kryo , hessian , Protostuff , java的序列化性能比较，特别测试hessian序列化
109.序列化是否必须带无参的构造函数
110.指定同类型多例bean创建的确切类型==>> 已测试，已通过  2016/5/16
111.提供Init的注解的实例支持bean托管==>> 已测试，已通过 2016/5/16
112.整合freemarket ==>> 已测试，已通过 2016/5/16
113.提供hessian2序列化单元测试压力测试，性能对比==>>已测试，已通过 2016/5/16
114.提供对bean注解的resource不仅仅支持接口注入，还要支持抽象类注入
115.重构：使得各个模块都相对独立，不要互相依赖==>>已测试，已通过 2016/5/18
116.重构：使得能够满足模块化的需要
117.单元测试提供：ReflectionGenericUtil.getSuperClassGenricType需要提供，严格测试
118.调整配置文件及读取方式，做到0配置，也就是提供默认配置,并且可以支持在PropertiesConfig(标注了@ReloadableResource的)等配置实体中指定默认值，并且@Config的注入的值也可以使用PropertiesConfig实体的默认值，在@Config的属性没有默认值时，以PropertiesConfig的为准
119.system.controllerClasspath等path有关的配置，要支持多路径的。多个路径用逗号隔开，可以支持默认vip.simplify的大路径扫描，如果对性能要求求，才指定具体路径，并指定多路径，主要为启动加速
120.减少代码的硬编码的字符串:目前比较多的是vip.simplify这样的字符串==>>已测试，已通过 2016/5/27 
121.fastjson格式化的日期等格式的处理，通过注解的方式来处理
122.如何做到框架的可测试，是否单元测试就够了，谁来确保，框架是被测试通过的
123.检查为什么StartupError异常后，服务没有停止掉
124.weaving模版重构，使其更智能，提供配置文件和注解方式，来扫描需要进入织入的方法,并减少类的扫描，通过javaagent自己的机制获取到，直接获取当前class字节码 ==>>已测试，已通过 2016/5/27 
125.调整精简优化配置文件的个数和属性值，减少配置文件依赖 ==>>已测试，已通过 2016/5/27 
126.injectionTargetClassPaths属性的通用性，减少人工干预 ==>>已测试，已通过 2016/5/27 
127.优化mvc模块，处理表单数据映射的问题
128.枚举类型的处理TimeEnum和MeasureEnum的处理 ==>>已测试，已通过 2016/5/27 
129.修复所有警告的代码 ==>>已测试，已通过 2016/5/27
130.优化mvc模块，处理rest参数映射问题，单个参数映射问题
140.优化mvc模块，处理参数注入必须使用RequestParam的问题，可以不用指定注解
131.优化mvc模块，处理必须带上HttpServletRequest request, HttpServletResponse response, TestModel model三个参数的问题，做到可选，并且目前没有这三个，会报不明确的错误
132.优化mvc模块，处理没有继承BaseControll的时候，没有明确的错误提示，并且错误信息靠后，如果没有继承，机制抛异常(确定抛异常时机),
133.优化mvc模块, 处理可以确保方法不会受到BaseControll知道的model泛型的全局影响 
134.优化mvc模块， 提供非model实体的注入,并且注入时，如果没有属性，那么model对象和注入的对象有空指针，避免浪费对象
135.优化mvc模块, 支持递归model复杂属性的注入
136.优化135的实现：由于递归和反射影响mdel注入的性能的，所以需要优化，目前的方案是通过类型推导方式，来做代码的生成
137.优化ioc模块：spi机制的扩展，ioc模块和其他模块的解耦，保证其他模块不用依赖ioc，不需要在其他模块中硬编码ioc模块的代码，可以通过配置生成Init注解对应的初始化模块，也就是plugin的入口。(模块解耦)
138.优化ioc模块：在Init注解对应的类中，或是其他初始化的的数据处理中，会出现许多map，具体后续要用到哪些map，还需要找到各自的Init注解标注的类，所以后续的改进是：要做到数据和功能操作分离，
                 要统一对map进行管理，一同一个简单的缓存容器,能够对容器做缓存的增删改。（数据和操作解耦）
143.优化ioc模块：减少重复扫描class文件的过程
139.优化ioc模块： InitTypeEnum类会影响ioc的独立性，会导入一些特定的组件业务，要保证后续增加组件模块，不会去修改到这个类
140.反射待优化(class文件)：SQLBuilder,SearchByPojoDao的find方法优化==>>已测试，已通过2016/5/30
141.反射待优化(class文件)：AnalysisRequestControllerModel(优化递归) 优化
142.反射待优化(class文件)：JsonAfterFilter
143.合并重构代码生成的代码，目前代码有些冗余
144.cas和oauth2.0源码深入分析
145.基于cas的gradle的配置改造
146.替换成git托管
147.基于docker的微服务
148.web防火墙实现
149.proguard压缩、优化和混淆Java字节码文件的免费的工具，可用于android和java项目,用于优化方法内联，（可以不需要手动给set和get方法指定final，就可以达到内联优化,当接口只有一个实现类的时候，就取代它）
150.nexus的bug，导致无法同时发布字节码包和源码包，配置nexus的release仓库config为redeploy
151.所有maven插件都要指定版本号，否则版本更新时，回去下载最新版本，会出现莫名其妙的问题，并且多了一次网络传输过程
152.(jetty使用过程中存在的问题，由于m2e插件导致的)在eclipse工具中，由于m2e插件的bug，default里面没有对应的maven二进制包及源码包路径，如果需要使用jetty等插件debug的时候不可以跟踪到lib库的源码包，需要安装http://ifedorenko.github.io/m2e-extras/这个扩展插件,Inteijj idea是否存在这个问题
153.由于javasist3.20.0-GA 不支持tomcat7,需要tomcat8以上才支持，所以使用weaving模块的时候，至少需要tomcat8的支持
154.对tomcat8.jetty9.webserver做兼容性测试
155.配置中心的集成
156.测试不同方式的项目立项搭建：1.同属vip.simplify，2.不属于vip.simplify 3.引用不同子模块
157.和压力测试人员衡量maxThreads，cceptCount 这个两个属性的值的设置,还有具体操作系统的限制，比如linux的openfile的限制
158.优化权限拦截处理,减少model对请求的影响，model和请求分离
159.日期处理全部替换成java.time的最新的jdk1.8的实现,实体(entity)中不需要time的地方直接用localDate，而不是date,因为现在部分地方有公用的SimpleDateFormat有线程安全问题,必须解决,务必先优化DateUtil类
###160.优化resin的access-log.log否则会出现死锁,优化参数：
```<session-max>4096</session-max>
   <session-timeout>30</session-timeout> <!--可以废弃掉，使用自己实现的session-->
   <enable-cookies>true</enable-cookies>
   <enable-url-rewriting>true</enable-url-rewriting> <!--url重写，可不考虑-->
   <dependency-check-interval>2s</dependency-check-interval>  <!--禁止热部署检测，优化性能-->
```
thread-min，thread-max，thread-keepalive分别写为150，400，300 线程相关微调
accept-buffer-size值设置的较大，10000以上,后续微调
*resin3配置
```<jvm-arg>-Xmx2048m</jvm-arg>
   <jvm-arg>-Xms1024m</jvm-arg>
   <jvm-arg>-Xss1m</jvm-arg>
   <jvm-arg>-Xdebug</jvm-arg>
   <jvm-arg>-Dcom.sun.management.jmxremote</jvm-arg>
```
*resin4配置
```<server-default>
    <jvm-arg>-Xms1024m</jvm-arg>
    <jvm-arg>-Xmx1024m</jvm-arg>
    <jvm-arg>-Xmn256m</jvm-arg>
    <jvm-arg>-XX:PermSize=128m</jvm-arg>
	<jvm-arg>-XX:MaxMetaspaceSize=256m</jvm-arg>
    <thread-max>1024</thread-max>
    <socket-timeout>30s</socket-timeout>
    <keepalive-max>512</keepalive-max>
    <keepalive-timeout>60s</keepalive-timeout>
</server-default>
```
###161.序列化的性能，yaml和json比较，还有解析速度，占用空间
162.进程控制：（1）使用Runtime的exec()方法 
           （2）使用ProcessBuilder的start()方法，参考地址：http://www.jb51.net/article/74430.htm 
163.优化避免新环境构建的时候，由于加载大量的jar，特别是一些maven的系统级jar包，导致构建一个新项目要花很多时间。即使使用nexus私服也会存在这样的问题   
*相关信息：
1.druid配置相关优化：https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
2.druid统计配置：https://github.com/alibaba/druid/wiki/%E6%80%8E%E4%B9%88%E4%BF%9D%E5%AD%98Druid%E7%9A%84%E7%9B%91%E6%8E%A7%E8%AE%B0%E5%BD%95
3.druid日志配置：https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter
4.https://www.leangoo.com/board_list
5.淘宝开源的系统监控工具 OrzDBA
6.jdk1.8的jvm参数设置：
  1.PermGen空间被移除了，取而代之的是Metaspace
需要做的调整为-XX:PermSize=64m -XX:MaxPermSize=128m 变成 -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m 否则起不来
  2.CompressedClassSpaceSize = 1073741824 (1024.0MB) 多出了这块，
CompressedClassSpaceSize的调优只有当-XX:+UseCompressedClassPointers开启了才有效-XX:CompressedClassSpaceSize=1G
由于这个大小在启动的时候就固定了的，因此最好设置得大点。没有使用到的话不要进行设置JVM后续可能会让这个区可以动态的增长。不需要是连续的区域，只要从基地址可达就行；可能会将更多的类元信息放回到元空间中；未来会基于PredictedLoadedClassCount的值来自动的设置该空间的大小
根据
*参考资料
http://www.examw.com/java/jichu/204271/index-3.html
http://blog.csdn.net/renfufei/article/details/24600507
http://jobar.iteye.com/blog/2023477
http://www.importnew.com/11908.html
http://book.51cto.com/art/201205/339217.htm
http://tech.it168.com/a2011/0609/1202/000001202458_2.shtml
http://blog.csdn.net/maosijunzi/article/details/38616357
http://www.tuicool.com/articles/iUfMBr
http://www.csdn.net/article/2013-08-02/2816449-Love-and-hate-for-Java8
http://www.tuicool.com/articles/uEr2i2
http://blog.csdn.net/maosijunzi/article/details/21706213
http://www.cnblogs.com/haiq/archive/2011/03/31/2001492.html
http://www.cnblogs.com/fish-li/archive/2013/02/18/2916253.html
http://my.oschina.net/glarystar/blog/483657
http://www.cnblogs.com/zc22/archive/2010/06/01/1749459.html
http://wenku.baidu.com/link?url=W5O4AjubMdBHaWiFeDxBVBUNRlHXP0WGEQSonPfosooA2Be6wMLkIRrMSnFwPoFEqqRhlsGdvrOsdm9tW1xaxIJDPYh-6AoIzUSzvKjS7Vy
http://www.tuicool.com/articles/Ez2i2am
http://www.tuicool.com/articles/eiu6j22
http://www.w3cplus.com/css3/css-svg-clipping.html
http://mp.weixin.qq.com/s?__biz=MzA4Nzc4MjI4MQ==&mid=403119339&idx=1&sn=63a40fd29879efc9b537cc3b9457e224&scene=23&srcid=0306snA2Hx2VeHGJpNMfNWrC#rd
https://www.leangoo.com/board_list
https://github.com/oblac/jodd/