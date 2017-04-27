package vip.simplify.dao.enums;

import java.sql.Connection;

/**
 * <p><b>Title:</b><i>事务隔离级别(isolation)枚举</i></p>
 * <p>Desc: oracle只支持TRANSACTION_READ_COMMITTED和TRANSACTION_REPEATABLE_READ，mysql的4种都支持
 *          注意，这里的隔离级别的设置是连接级别，只针对连接的甚至，并不是全局</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午6:08:05</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午6:08:05</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public enum ISOEnum {
	
	/**
	 * 0不提供事务支持
	 */
	TRANSACTION_NONE(Connection.TRANSACTION_NONE),
	/**
	 * 级别[1]未提交读事务--导致[脏读(dirty read)]，可读到事务未提交的脏数据，还导致[不可重复读]。某些业务场景下可用到，例：非高并发下的抽奖业务,可用于避免重复抽奖
	 * 问题：会发生脏读,不可提交读和幻读
	 * 建议使用：比如非高性能下的抽奖可使用，这并发抽奖的完美解决方案，只是最快方案而已
	 */
	TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
	/**
	 * 级别[2]可提交读事务--一般应用推荐使用这个级别的事务,可避免[脏读]就够了,会导致发生[不可重复读]的问题，也就是两次查询会读到另外一个事务提交的数据，导致两次查询结果不一致,性能上也会优于TRANSACTION_REPEATABLE_READ隔离级别
	 * 例1：做列表查询功能能，第一次刷新是5条记录，第二次刷新变成6条记录，但是这个[不可重复读]大多情况下，业务处理上也是允许的。
	 * 例2：做数据更新的时候，第一次查询某个字段的数值1000，这时事务未提交，另外一个事务已经更新这个字段的值为900，这时候再次查询刚刚1000会变成900，如果这时候直接覆盖过去。比如当前事务也是减掉的100，那么会被更新为900，其实这时候已经数据异常了。
	 *      如果是这种情况，事务是需要锁住的，不能修改。不应该出现 [不可重复读]问题,如果和更新相关的，那么使用TRANSACTION_REPEATABLE_READ级别
	 * 问题：会发生[不可提交读-是针对某一行记录的修改导致的一致性问题]和[幻读-是针对多条记录导致的一致性问题]
	 * 建议使用：紧紧是查询操作和添加删除等操作，建议使用这个级别，以获得更好的性能，不需要等待大量事务的提交完成.
	 *           如果是更新操作，不建议使用这个级别，需要用TRANSACTION_REPEATABLE_READ代替
	 */
	TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
	/**
	 * 级别[4]可重复读事务--mysql的默认事务隔离级别，严格次于Connection.TRANSACTION_SERIALIZABLE,可避免[脏读]和[不可重复读]的发生
	 * 问题：会发生幻读,因为是行级锁，只要update的记录刚好被其他事务持有（被加锁），那么就会等待其他事务完成后，才能进行更新操作,delete操作同一记录也受影响而排队。有时为了数据一致性，这个现象可以接受。
	 * 建议使用：如果是记录更新操作，建议使用这个级别
	 */
	TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
	/**
	 * 级别[8]串行化(序列化)事务--最严格事务隔离级别,严格串行排队，可避免[脏读]和[不可重复读]和[幻读(phantom read)]的发生，尽管数据库引擎支持行级锁(INNODB引擎)，也会以锁表的形式来保证数据一致，性能最差。
	 * 锁表原因：因为这个事务隔离级别是为保证数据完整一致。例：对数据表的数据库进行批量修改，同时另外的时候又添加新的数据或删除数据记录。
	 * 这样的话会导致事务不一致的后果是：新添加的数据的没在批量更新中被更改过来，但是觉得很奇怪，明明刚刚我已经更新了，为什么还是老的数据。这个也可能导致count和sum等函数结果不一致。特别是抽奖，有中奖次数限制，这种影响最明显.
	 * 问题：一致性最强但性能最差，因为是表级锁，只要有任何insert或delete，或update操作，就会加锁，其他事务都只能等待，排队严重。
	 * 建议使用： 一般不建议使用
	 */
	TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);
	
	private Integer value;
	private ISOEnum(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}