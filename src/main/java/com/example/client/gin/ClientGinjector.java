package com.example.client.gin;

import com.example.client.components.ColaboradorPanel;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ClientModule.class)
public interface ClientGinjector extends Ginjector {
	ColaboradorPanel getColaboradorPanel();
}
