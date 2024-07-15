package com.grocery.store.util;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class ManagedSessionFactory {

	private final SessionFactory sessionFactory;

	public <R> R fromTransaction(Function<Session,R> action) {
		return sessionFactory.fromTransaction(session -> {
			try {
				ManagedSessionContext.bind(session);
				return action.apply(session);
			} finally {
				ManagedSessionContext.unbind(sessionFactory);
			}
		});
	}

	public void inTransaction(Consumer<Session> consumer) {
		sessionFactory.inTransaction(session -> {
			try {
				ManagedSessionContext.bind(session);
				consumer.accept(session);
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
