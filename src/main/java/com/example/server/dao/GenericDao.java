package com.example.server.dao;


public interface GenericDao <T> {

	T persist(T entity);
	void remove(T entity);
}
