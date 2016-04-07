ODBC：基于C语言的数据库访问接口。
JDBC：是Java版的ODBC。JDBC 特性：高度的一致性、简单性(常用的接口只有4、5个).
驱动程序按照工作方式分为四类：
    1、JDBC-ODBC bridge + ODBC 驱动
       JDBC-ODBC bridge桥驱动将JDBC调用翻译成ODBC调用，再由ODBC驱动翻译成访问数据库命令。
       优点：可以利用现存的ODBC数据源来访问数据库。
       缺点：从效率和安全性的角度来说的比较差。不适合用于实际项目。
    2、基于本地API的部分Java驱动
       我们应用程序通过本地协议跟数据库打交道。然后将数据库执行的结果通过驱动程序中的Java部分返回给客户端程序。
       优点：效率较高。
       缺点：安全性较差。
    3、纯Java的网络驱动
       (中间协议)            (本地协议)
       app    JDBC     纯Java                 中间服务器               DB
       缺点：两段通信，效率比较差
       优点：安全信较好
    4、纯Java本地协议：通过本地协议用纯Java直接访问数据库。
       特点：效率高，安全性好。


JDBC的问题：
1.数据类型的问题（日期的问题，大文本数据的问题==不能用varchar
2.jdbc事务处理原则
3.JDBC中调用存储过程：不提供，不建议使用
4.JDBC中调用函数：可以支持，自定义函数，目前可用于分布式主键的自增问题上，其他都不建议用
5.JDBC批处理，比如批量插入：有两种方式 A.通过mysql特有语法insert into table(id,name) values (?,?),(?,?),(?,?),...;
  B.jdbc通用的语法PreparedStatement.addBatch方法来实现，使用语法是insert into table(id,name) values(?,?);
  注意：建立一个连接和执行一个sql语句的是很耗时间，特别是创建连接，所以才需要有连接池，那么执行sql语句的优化方法，就是批处理  
        另外，批处理的数据不是越大越好，因为可能会内存溢出，同时网络传输的过程中也是会进行拆包传输的，由于网络环境这个包的大小是不一定的，有时候打包的效率不一定就会高，这个和数据库的类型，版本都有关系的，所以我们在实践的过程中需要检验的。
6.JDBC中的滚动结果集和分页技术：需要数据库支持 ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY，这样可以提高分页查询的速度。api：ResultSet.isFirst,ResultSet.absolute,ResultSet.isBeforeFirst

