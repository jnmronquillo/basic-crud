package com.example.server;

import com.example.server.requestfactory.InjectedRequestFactoryModule;
import com.example.server.requestfactory.InjectedRequestFactoryServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class MyGuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector( 
		  new ServletModule(){
			@Override
			protected void configureServlets() {				
				
				install(new JpaPersistModule("myFirstJpaUnit"));  // like we saw earlier.

			    filter("/*").through(PersistFilter.class);
				
			 // RequestFactory servlet
//				Map<String, String> params = new HashMap<String, String>();
//                params.put("symbolMapsDirectory", "WEB-INF/classes/symbolMaps/");
//                
//				bind(RequestFactoryServlet.class).in(Singleton.class);
//				serve("/gwtRequest").with(RequestFactoryServlet.class, params);
			    
			    install(new InjectedRequestFactoryModule());
			    serve("/gwtRequest").with(InjectedRequestFactoryServlet.class);
				
				//filter("/*").through(MyFilter.class);
				//filter("*.css").through(MyCssFilter.class);
				// etc..
	
				//serve("*.html").with(MyServlet.class);
				//serve("/my/*").with(MyServlet.class);
				// etc..
			}
		});
	}

}
