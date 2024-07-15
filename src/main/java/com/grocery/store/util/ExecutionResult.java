package com.grocery.store.util;

import org.hibernate.Session;

public interface ExecutionResult<T> {
	T getResult(Session session);
}