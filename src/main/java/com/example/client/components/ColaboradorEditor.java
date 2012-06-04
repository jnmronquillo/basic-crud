package com.example.client.components;

import com.example.client.Messages;
import com.example.client.events.SaveEvent;
import com.example.client.images.AppImages;
import com.example.shared.proxy.ColaboradorProxy;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ColaboradorEditor implements Editor<ColaboradorProxy> {

	public interface Binder extends UiBinder<Widget, ColaboradorEditor> {
	}
	
	@Inject AppImages images;
	@Inject Messages messages;
	
	@UiField
	FramedPanel form;
	 
	@UiField
	TextField nombres;
	 
	@UiField
	TextField apellidos;
	 
	@UiField(provided = true)
	NumberField<Integer> edad;
	  
	Window panel;
	private final EventBus eventBus;
	 
	@Inject
	public ColaboradorEditor(final Binder uiBinder, EventBus eventBus){
		this.eventBus = eventBus;
		
		edad = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
			
		panel = (Window) uiBinder.createAndBindUi(this);
	}
	public void show(String title){
		panel.setHeadingText(title);
		panel.show();
	}  
	 
	@UiHandler("save")
	public void onSave(SelectEvent event){
		eventBus.fireEvent(new SaveEvent());
	}
	 
	@UiHandler("cancel")
	public void onCancel(SelectEvent event){
		hide();
	}
	public void clearFields() {
		nombres.clearInvalid();
		apellidos.clearInvalid();  
	}
	 
	public void hide() {
		panel.hide();
	}	
}
