package com.grocery.store.util;

import org.hibernate.Session;

public interface Execution {
	void execute(Session session);
}
