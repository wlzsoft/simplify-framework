package com.meizu.simplify.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.meizu.simplify.utils.collection.IEqualCallBack;


/**
 * <p><b>Title:</b><i>集合工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月24日 下午4:50:33</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月24日 下午4:50:33</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class CollectionUtil {
	
	/**
	 * 方法用途: 判断指定的集合是否为空<br>
	 * 操作步骤: TODO<br>
	 * @param collection 待判断的集合
	 * @return 返回指定的集合是否为空
	 */
	public static Boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * 方法用途: 判断指定的集合是否不为空<br>
	 * 操作步骤: TODO<br>
	 * @param collection 待判断的集合
	 * @return 返回指定的集合是否不为空
	 */
	public static Boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * 方法用途: 判断指定的数组是否为空<br>
	 * 操作步骤: TODO<br>
	 * @param array  待判断的数组
	 * @return 返回指定的数组是否为空
	 */
	public static Boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * 方法用途: 判断指定的数组是否不为空<br>
	 * 操作步骤: TODO<br>
	 * @param array  待判断的数组
	 * @return 返回指定的数组是否不为空
	 */
	public static Boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * 方法用途: 判断指定的Map是否为空<br>
	 * 操作步骤: TODO<br>
	 * @param map  待判断的Map
	 * @return 返回指定的Map是否为空
	 */
	public static Boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 方法用途: 判断指定的Map是否不为空<br>
	 * 操作步骤: TODO<br>
	 * @param map 待判断的Map
	 * @return 返回指定的Map是否不为空
	 */
	public static Boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
	
	/**
	 * 方法用途: 判断数组中是否包含指定元素<br>
	 * 操作步骤: TODO<br>
	 * @param <T> 数组类型
	 * @param elements 数组
	 * @param elementByFind 待查找的元素
	 * @return 如果数组中包含指定元素返回true，否则返回false
	 */
	public static <T> Boolean contains(T[] elements, T elementByFind) {
		List<T> elementsList = Arrays.asList(elements);
		return elementsList.contains(elementByFind);
	}
	
	/**
	 * 方法用途: 判断数组中,是否包含满足条件的元素<br>
	 * 操作步骤: TODO<br>
	 * @param <T>  数组类型
	 * @param elements 数组
	 * @param elementByFind 待查找的元素
	 * @param call 回调处理确认满足条件的对象
	 * @return 如果数组中包含指定元素返回true，否则返回false
	 */
	public static <T,W> Boolean contains(T[] elements, W elementByFind,IEqualCallBack<T,W> call) {
		List<T> elementsList = Arrays.asList(elements);
		return contains(elementsList,elementByFind,call);
	}
	
	/**
	 * 方法用途: 集合对象数组中，是否包含满足条件的对象<br>
	 * 操作步骤: TODO<br>
	 * @param sourceList 待提取的数组对象
	 * @param elementByFind 提取的条件
	 * @param call 回调处理确认满足条件的对象
	 * @return
	 */
	public static <T,W> Boolean contains(List<T> sourceList,W elementByFind, IEqualCallBack<T,W> call) {
		for (int i=0; i < sourceList.size(); i++) {
			T t = sourceList.get(i);
			boolean res = call.equal(t,elementByFind);
			if(res) {
				return res;
			}
		}
		return false;
	}
	
	/**
	 * 方法用途: 复制集合<br>
	 * 操作步骤: TODO<br>
	 * @param <T> 集合元素类型
	 * @param source 源集合
	 * @param target 目标集合
	 */
	public static <T> void copy(Collection<T> source, Collection<T> target) {
		AssertUtil.isTrue(source != null, "源集合不能为空。");
		AssertUtil.isTrue(source != null, "目标集合不能为空。");
		target.clear();
		if (!source.isEmpty()) {
			for (T o : source) {//TODO 是否使用迭代器，这里比较特殊，有可能是set，也可能是list，慎重考虑
				target.add(o);
			}
		}
	}
	
	/**
	 * 方法用途: 根据值对Map进行排序<br>
	 * 操作步骤: TODO<br>
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map 
	 * @param asc 是否升序
	 * @return 返回排序后的Map
	 */
	public static <K, V> Map<K, V> sortMapByValue(Map<K, V> map,
			final Boolean asc) {
		List<Entry<K, V>> entries = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(entries, new Comparator<Entry<K, V>>() {
			@SuppressWarnings("unchecked")
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				if (asc) {
					return ((Comparable<V>) o1.getValue()).compareTo(o2
							.getValue());
				} else {
					return -((Comparable<V>) o1.getValue()).compareTo(o2
							.getValue());
				}
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (int i=0; i < entries.size(); i++) {
			Entry<K, V> entry = entries.get(i);
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	/**
	 * 方法用途: 根据键对Map进行排序<br>
	 * 操作步骤: TODO<br>
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map
	 * @param asc 是否升序
	 * @return 返回排序后的Map
	 */
	public static <K, V> Map<K, V> sortMapByKey(Map<K, V> map, final Boolean asc) {
		List<Entry<K, V>> entries = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(entries, new Comparator<Entry<K, V>>() {
			@SuppressWarnings("unchecked")
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				if (asc) {
					return ((Comparable<K>) o1.getKey()).compareTo(o2.getKey());
				} else {
					return -((Comparable<K>) o1.getKey()).compareTo(o2.getKey());
				}
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (int i=0; i < entries.size(); i++) {
			Entry<K, V> entry = entries.get(i);
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	/**
	 * 方法用途: 集合对象数组中，提取满足条件的对象<br>
	 * 操作步骤: TODO<br>
	 * @param sourceList 待提取的数组对象
	 * @param w 提取的条件
	 * @param call 回调处理确认满足条件的对象
	 * @return
	 */
	public static <T,W> T getItem(T[] sourceList,W w, IEqualCallBack<T,W> call) {
		for (int i=0; i < sourceList.length; i++) {
			T t = sourceList[i];
			boolean res = call.equal(t,w);
			if(res) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * 方法用途: 过滤list中对象的属性，并通过splitChar字符拼接成字符串返回<br>
	 * 操作步骤: TODO<br>
	 * @param sourceList 源list集合
	 * @param field 抽取字段属性
	 * @param splitChar 拼接所用字符
	 * @return 拼接后结果
	 */
	public static <T> String filterAndFormat(List<T> sourceList, String field, String splitChar) {
		StringBuffer dataIds = new StringBuffer();
		sourceList.forEach(data -> dataIds.append(",'")
				.append(String.valueOf(ReflectionUtil.obtainFieldValue(data, field))).append("'"));
		return dataIds.toString().substring(1);
	}

	/**
	 * 方法用途: 过滤list中对象的属性，并通过splitChar字符拼接成字符串返回<br>
	 * 操作步骤: TODO<br>
	 * @param sourceList 源list集合
	 * @param field 抽取字段属性
	 * @param splitChar 拼接所用字符
	 * @return 拼接后结果
	 */
	public static <T> List<Serializable> filterAndFormatByList(List<T> sourceList, String field, String splitChar) {
		List<Serializable> list = new ArrayList<>();
		sourceList.forEach(data -> list.add(ReflectionUtil.obtainFieldValue(data, field, Serializable.class)));
		return list;
	}

	/**
	 * 方法用途: 过滤list中对象的属性，并通过splitChar字符拼接成字符串返回<br>
	 * 操作步骤: TODO<br>
	 * @param sourceList 源list集合
	 * @param field 抽取字段属性
	 * @param splitChar 拼接所用字符
	 * @return 拼接后结果
	 */
	public static <T> String filterAndFormatByString(List<T> sourceList, String field, String splitChar) {
		StringBuffer result = new StringBuffer();
		sourceList.forEach(data -> result.append(",'")
				.append(ReflectionUtil.obtainFieldValue(data, field, Serializable.class)).append("'"));
		String resultStr = result.toString();
		if (StringUtil.isEmpty(resultStr)) {
			return "";
		}
		return resultStr.substring(1);
	}

	/**
	 * 方法用途: 过滤list中对象的属性，并通过splitChar字符拼接成字符串返回<br>
	 * 操作步骤: TODO<br>
	 * @param sourceList 源list集合
	 * @param field 抽取字段属性
	 * @param splitChar 拼接所用字符
	 * @return 拼接后结果
	 */
	public static <T> Serializable[] filterAndFormatByArr(List<T> sourceList, String field, String splitChar) {
		List<Serializable> list = filterAndFormatByList(sourceList, field, splitChar);
		Serializable[] sArr = new Serializable[list.size()];
		list.toArray(sArr);
		return sArr;
	}

	/**
	 * 方法用途: 字符串集合转字符串<br>
	 * 操作步骤: 为给定字符串集合的值的两遍添加上指定字符，并转化成字符串，以指定字符分隔<br>
	 * @param types
	 * @param appendLeft 在每个值的左边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @param appendRight 在每个值的右边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @param splitStr 指定分隔的字符
	 * @return
	 */
	public static String listToStringBySplit(String[] types, String appendLeft, String appendRight,
			String splitStr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < types.length; i++) {
			if (i > 0 && i < types.length) {
				sb.append(splitStr);
			}
			sb.append(appendLeft + types[i] + appendRight);
		}
		return sb.toString();
	}

	/**
	 * 方法用途: 字符串集合转字符串<br>
	 * 操作步骤: 为给定字符串集合的值的两遍添加上指定字符，并转化成字符串，以指定字符分隔<br>
	 * @param types
	 * @param appendLeft 在每个值的左边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @param appendRight 在每个值的右边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @param splitStr 指定分隔的字符
	 * @return
	 */
	public static String listToStringBySplit(List<String> types, String appendLeft, String appendRight,String splitStr) {
		String[] typeArr = new String[types.size()];
		return listToStringBySplit(typeArr, appendLeft, appendRight, splitStr);
	}
	
	/**
	 * 方法用途: 字符串集合转字符串<br>
	 * 操作步骤: 为给定字符串集合的值的两遍添加上指定字符，并转化成字符串，以逗号分隔<br>
	 * @param types
	 * @param appendLeft 在每个值的左边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @param appendRight 在每个值的右边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @return
	 */
	public static String listToStringBySplit(List<String> types, String appendLeft, String appendRight) {
		return listToStringBySplit(types, appendLeft, appendRight, ",");
	}
	/**
	 * 
	 * 方法用途: 实现将一个List转化为Map<br>
	 * 操作步骤: TODO<br>
	 * @param keyName 转换对象属性名
	 * @param list 需要转换的源list
	 * @author wanghaibin
	 * @return
	 */
	public static <T> Map<String, T> listToMap(String keyName, List<T> list) {

		Map<String, T> map = new ConcurrentHashMap<String, T>();

		if (list != null && list.size() != 0) {
			for (T t : list) {
				Map<String, Object> mapField;
				try {
					mapField = ReflectionUtil.bean2Map(t);
					Object fieldValue = mapField.get(keyName);
					String valueForString = "";
					try {
						valueForString = String.valueOf(fieldValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (valueForString.trim().equals("")) {
						continue;
					}
					map.put(valueForString, t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	/**
	 * 方法用途: 删除数组重复元素-针对基本数据类型,如果是普通复杂对象，那么需要重写equals方法<br>
	 * 操作步骤: TODO<br>
	 * @param strArr
	 * @return 返回list
	 */
	public static  <T extends Serializable>  List<T> duplicate(T[] strArr) {
		List<T> tList = new ArrayList<>();
		for (T t : strArr) {
			boolean isContains = contains(tList, t,new IEqualCallBack<T, T>() {
				@Override
				public boolean equal(T o, T w) {
					if(o.equals(w)) {
						return true;
					}
					return false;
				}
			});
			if(!isContains) {
				tList.add(t);
			}
		}
		return tList;
	}
}
