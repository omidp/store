package com.grocery.store.util;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import java.util.function.Function;

@RequiredArgsConstructor
public class ManagedSessionFactory {

	private final SessionFactory sessionFactory;

	public <T> T fromTransaction(ExecutionResult<T> executionResult) {
		return sessionFactory.fromTransaction(session -> {
			try {
				ManagedSessionContext.bind(session);
				T result = executionResult.getResult(session);
				return result;
			} finally {
				ManagedSessionContext.unbind(sessionFactory);
			}
		});
	}

	public void inTransaction(Execution execution) {
		sessionFactory.inTransaction(session -> {
			try {
				ManagedSessionContext.bind(session);
				execution.execute(session);
			} finally {
				ManagedSessionContext.unbind(sessionFactory);
			}
		});
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public <R> R fromSession(Function<Session,R> action) {
		return sessionFactory.fromSession(action);
	}
}
