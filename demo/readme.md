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
10.缓存连接池及连接的问题，采用回调方法和内部类重构redis缓存，统一管理连接的回收到连接池的问题
11.缓存操作，基于注解的读取缓存功能，好像失效，需要查看下。
12.事务做更精细化控制，不需要在代码里面硬编码
13.压力测试业务请求的性能表现

	       
	       
相关信息：
1.druid配置相关优化：https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
2.druid统计配置：https://github.com/alibaba/druid/wiki/%E6%80%8E%E4%B9%88%E4%BF%9D%E5%AD%98Druid%E7%9A%84%E7%9B%91%E6%8E%A7%E8%AE%B0%E5%BD%95
3.druid日志配置：https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter