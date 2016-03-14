待完成功能：
1.连接池的连接的控制，目前是否出现连接泄露，需要监控起来==>>已测试，已通过，案例[druid自带管理后台+相关druid配置调整和连接回收到连接池处理] 2016/2/25
2.分页查询的封装==>>已测试，已通过，案例[com.meizu.simplify.dao.DaoTest] 2016/2/25
3.测试一下多表负责查询是否有bug，测试相对复杂点的sql语句==》已测试，已通过，案例[TestController.adoTestSelect2方法] 2016/2/24
4.map方式的查询的处理和实现==》已测试，已通过，案例[TestController.adoTestSelect3方法] 2016/2/24
5.多表复杂查询返回实体的方式需要优化：目前依赖了jpa标准，无法灵活注入，对于查询，其实可以忽略jpa标准的==》已测试，已通过，案例[新增SearchByMapDao,并集成到BaseDao中] 2016/2/24
6.public Page<T> findPage(Integer currentPage,Integer pageSize,String sort, Boolean isDesc,String sql,Object... params)
     ==>> 已测试，已通过，案例[DaoTest.s2_findPageMutilSql3Test] 2016/2/26
7.提供可选字段的update操作==>>已测试，已通过，可再优化(更细致的字段控制)，案例[新增update(T t,Boolean isAllField)] 2016/2/26 
8.IdEntity 的id改成了fid，业务需要，后续可以考虑更优雅的处理方式==》已测试，已通过  2016/2/25
9.BaseEntity 的delFlag改成deleteflag业务需要==》已测试，已通过  2016/2/25
10.缓存连接池及连接的问题，采用回调方法和内部类重构redis缓存，统一管理连接的回收到连接池的问题==>>已测试重要部分，已通过 2016/2/26
11.缓存操作，基于注解的读取缓存功能，好像失效，需要查看下==》已测试，已通过 2016/2/26
12.改造拦截器，可支持拦截返回，而不只是拦截处理,并修复缓存读取无法返回的问题==>>已测试，已通过2016/2/29
13.事务做更精细化控制，不需要在代码里面硬编码==>>已测试，已通过 2016/2/29 待解决问题：事务重叠问题
16.mvc的json方式的ajax方法的处理和优化==>> 已测试，已通过 2016/3/1
14.压力测试业务请求的性能表现:性能指标，模拟mvc的tomcat，jedis，druid的性能测试，着重是mysql的压力导致tomcat挂掉，而不是mysql挂掉，检查下，是否应该mysql客户端连接频繁重试导致的tomcat挂掉，这时候用的线程资源和连接都是tomcat消耗，需要确认是否是这个问题
15.修复部分数据库相关没释放的问题，比如result和statement的关闭(druid连接池，是否有管理resultset和statement的关闭和创建)，需要测试，配合14点的压力测试，查看效果
17.查看aop模块的readme.md文档，处理aop相关的问题==>>暂缓
18.配置文件热加载实现==>>优先实现
19.极速启动实现==》暂缓
20.jdbc多数据源及数据分片，主从分离等功能实现，建议以mycat中间件来做这个事情，但是也可以内置实现，部分需求可以没必要使用mycat==》可不实现
21.webserver 精简初版==>>已测试，已通过 2016/3/2
22.模板引擎，velocityForward 不要依赖 servlet相关api，需调整优化==>>暂不处理
23.提供识别get和post请求的处理，甚至于put和delete等基于rest风格相关的请求处理
24.websocket的实现，可用于集成webserver和消息服务中间件
25.webcache的实现，从旧版框架中移植之前的代码，并优化和正式启用==>>已测试，已通过 2016/3/3
26.mvcinit类 需要重构下，以便和webcache等模块分离==>>已测试，已通过 2016/3/3
27.PropertieUtil类需要重构下，目前有多个实例的冗余，[1.可以根据配置文件的不同，创建不同实例，2.也可以通过配置文件自动合并，只生成一个实例。推荐第二种方式]
28.webcache的removespace的功能的实现：目前这个压缩过程，有bug，会丢失有效的文本信息==>>已测试，已通过 2016/3/3
29.aop日志打印太过详细的问题==>>已测试，已通过[由于jetty本身的问题，jetty底层依赖slf4j，如果有相关的slf4j，那么就会启用slf4j的日志，导致输出日志很详细，可以在javaagent中删掉slf4j，那么就会用回jdk的log模块，日志就很少]2016/3/4
30.对非正则表达式的url的映射解析的404问题的解决==>>已测试，已通过 2016/3/4
31.对参数@RequestParam的解析的测试==>>已测试，已通过2016/3/4
32.基于jetty使用框架时，第一次请求性能耗费了800ms左右的时间==>>已测试，已通过，jetty本身自己的问题，第一次访问会很慢（估计jetty第一次访问会初始化一些数据） 2016/3/4
33.webcache的ClearCommentUtil.clear中要清除的html页面中，全部用html5的头声明(<!doctype html>)，否则声明头会被清除掉一部分
34.mvc性能优化，修复请求过程中使用反射的问题
35.自动创建表的实现
36.连接超时，自动重连功能	      
37.mvc的页面异常处理 
38.考虑使用Collection中的Stream(注意这个方式，只能遍历一次)来提高大数据json并行解析的速度
39.使用invokedynamic(1.函数式编程的性能基础,2.对其他语言，比如js的支持，NashornJavaScript，java版nodejs的实现)或是动态代理(cglib)，或是java8的函数指针，或是启动时修改class内容的方式来提高mvc请求时解析的性能
40.使用lambda表达式简化编程，优先改造内部类的使用方式
41.使用 java.time代替部分date的操作
相关信息：
1.druid配置相关优化：https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
2.druid统计配置：https://github.com/alibaba/druid/wiki/%E6%80%8E%E4%B9%88%E4%BF%9D%E5%AD%98Druid%E7%9A%84%E7%9B%91%E6%8E%A7%E8%AE%B0%E5%BD%95
3.druid日志配置：https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter