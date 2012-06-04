package com.example.server.dao;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.server.domain.Colaborador;
import com.example.server.resultbean.ColaboradorPagingLoadResultBean;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;

public class ColaboradorDao implements GenericDao<Colaborador> {

	@Inject Provider<EntityManager> emProvider;
	
	@Transactional
	public Colaborador persist(Colaborador entity) {
		emProvider.get().persist(entity);
		return entity;
	}

	@Transactional
	public void remove(Colaborador entity) {
		emProvider.get().remove(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Colaborador> findAll() {		
		return  emProvider.get().createQuery("from Colaborador").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public ColaboradorPagingLoadResultBean list(int offset, int limit, List<SortInfoBean> sortInfo, List<FilterConfigBean> filterConfig) {
		Query q = emProvider.get().createQuery("from Colaborador order by "+getOrder(sortInfo));
		  //q.setParameter("field", sortInfo.get(0).getSortField());
		  //q.setParameter("dir", sortInfo.get(0).getSortDir());
		
			
		q.setFirstResult(offset);
		q.setMaxResults(limit);
		
		List<Colaborador> list = q.getResultList();
		
		Query q2 = emProvider.get().createQuery("select count(c) from Colaborador c");		 
		Long count = (Long) q2.getSingleResult();
		
//		System.out.println(filterConfig.size());
//		for(FilterConfigBean s : filterConfig){
//			System.out.println(s.getComparison());
//			System.out.println(s.getField());
//			System.out.println(s.getType());
//			System.out.println(s.getValue());
//		}
		return new ColaboradorPagingLoadResultBean(list, count.intValue(), offset);
	}
	
	private String getOrder(List<SortInfoBean> sortInfo){
		String order = " id DESC ";
		if(sortInfo.size() == 1)
		{
			try {
				Field field = Colaborador.class.getDeclaredField(sortInfo.get(0).getSortField());
				field.setAccessible(true);
				
				if(sortInfo.get(0).getSortDir() == SortDir.ASC || sortInfo.get(0).getSortDir() == SortDir.DESC)
					return " "+field.getName()+" "+ sortInfo.get(0).getSortDir()+" ";
				
			} catch (NoSuchFieldException e) {				
				e.printStackTrace();
				return order;
			} catch (SecurityException e) {
				e.printStackTrace();
				return order;
			}
			
		}
		return order;
	}

}
