package com.meizu.demo.system;

import java.io.Serializable;
import java.util.List;

public interface IAutoBusinessService {
	List<?> get(Serializable[] id);

	<T> boolean save(T t,Class<T> clazz);

	int del(Serializable[] id);

	<T> int update(T t,Class<T> clazz);
}
