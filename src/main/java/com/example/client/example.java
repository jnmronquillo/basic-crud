package com.example.client;

import com.example.client.gin.ClientGinjector;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class example implements EntryPoint {
	
  private final ClientGinjector injector = GWT.create(ClientGinjector.class);
  
  public void onModuleLoad() {
	  RootPanel.get().add(injector.getColaboradorPanel());	  
  }

}
