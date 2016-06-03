package com.meizu.demo.system;

import java.io.Serializable;

import com.meizu.simplify.entity.IdEntity;

public interface IAutoBusinessService {
	IdEntity<Serializable, Integer> get(Serializable id);

	<T> boolean save(T t,Class<T> clazz);
}
