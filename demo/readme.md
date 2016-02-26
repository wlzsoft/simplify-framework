待完成功能：1.事务做更精细化控制，不需要在代码里面硬编码
           2.连接池的连接的控制，目前是否出现连接泄露，需要监控起来。
           3.分页查询的封装
           4.测试一下多表负责查询是否有bug，测试相对复杂点的sql语句==》已测试，已通过，案例[TestController.adoTestSelect2方法] 2016/2/24
           4.1.map方式的查询的处理和实现==》已测试，已通过，案例[TestController.adoTestSelect3方法] 2016/2/24
           5.压力测试业务请求的性能表现
           6.多表复杂查询返回实体的方式需要优化：目前依赖了jpa标准，无法灵活注入，对于查询，其实可以忽略jpa标准的
           7.druid配置相关优化：https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
           8.druid统计配置：https://github.com/alibaba/druid/wiki/%E6%80%8E%E4%B9%88%E4%BF%9D%E5%AD%98Druid%E7%9A%84%E7%9B%91%E6%8E%A7%E8%AE%B0%E5%BD%95
           9.druid日志配置：https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter
           
           10.public Page<T> findPage(Integer currentPage,Integer pageSize,String sort, Boolean isDesc,String sql,Object... params)
           还未测试通过，count的replaceAll有问题，处理后，还要测试是否可用，基于这个方法，再次封装，提供更简便的多表分页查询 TODO<br>
	       11.提供可选字段的update操作TODO
	       12.缓存操作，基于注解的读取缓存功能，好像失效，需要查看下。
	       13.IdEntity 的id改成了fid，业务需要，后续可以考虑更优雅的处理方式
	       14.BaseEntity 的delFlag改成deleteflag业务需要