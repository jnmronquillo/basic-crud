package com.example.server.dao;

import java.util.List;

public interface GenericDao <T> {

	T persist(T entity);
	void remove(T entity);	
	List <T> findAll();
}
