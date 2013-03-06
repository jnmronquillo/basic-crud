package com.example.shared.service;

import java.util.List;

import com.example.server.dao.ColaboradorDao;
import com.example.server.requestfactory.InjectingServiceLocator;
import com.example.shared.proxy.ColaboradorProxy;
import com.example.shared.resultproxy.ColaboradorPagingLoadResultProxy;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;

@Service(value = ColaboradorDao.class, locator = InjectingServiceLocator.class)
public interface ColaboradorService extends RequestContext {
	
    Request<ColaboradorProxy> persist(ColaboradorProxy colaborador);
    Request<Void> remove(List<ColaboradorProxy> colaborador);
    Request<ColaboradorPagingLoadResultProxy> list(int offset, int limit, List<? extends SortInfo> sortInfo, List<? extends FilterConfig> filterConfig);
}
