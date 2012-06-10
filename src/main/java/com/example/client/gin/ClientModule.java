package com.example.client.gin;

import com.example.client.Messages;
import com.example.client.images.AppImages;
import com.example.shared.service.AppRequestFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AppImages.class).in(Singleton.class);
		bind(Messages.class).in(Singleton.class);
		
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);		
	}
	
	@Provides
    @Singleton
    public AppRequestFactory createRequestFactory(EventBus eventBus) {
	  AppRequestFactory factory = GWT.create(AppRequestFactory.class);
      factory.initialize(eventBus);
      return factory;
    }
        
}
