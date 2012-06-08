package com.example.client.components;

import java.util.ArrayList;
import java.util.List;

import com.example.client.Messages;
import com.example.client.events.SaveEvent;
import com.example.client.events.SaveEvent.SaveHandler;
import com.example.client.images.AppImages;
import com.example.shared.proxy.ColaboradorProxy;
import com.example.shared.service.AppRequestFactory;
import com.example.shared.service.ColaboradorService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class ColaboradorPanel implements IsWidget {

	interface ColaboradorProxyProperties extends PropertyAccess<ColaboradorProxy> {
	    ModelKeyProvider<ColaboradorProxy> id();
	    ValueProvider<ColaboradorProxy, String> nombres();
	    ValueProvider<ColaboradorProxy, String> apellidos();
	    ValueProvider<ColaboradorProxy, Integer> edad();
	}
	  
	public interface Binder extends UiBinder<Widget, ColaboradorPanel> {
	}
	
	public interface Driver extends RequestFactoryEditorDriver<ColaboradorProxy, ColaboradorEditor>{  
	}
	
	private final SaveHandler saveHandler = new SaveHandler() {
		
		public void onSave(SaveEvent event) {
		  RequestContext context = driver.flush();
		  if(!driver.hasErrors()){
		   editor.hide();
		   context.fire(new Receiver<Void>() {
		     
		    @Override
		    public void onSuccess(Void response) {
		     Info.display("ExampleRF", "Se guardo correctamente");      
		     loader.load();
		    }
		   });		   
		  }		  
		}
	};
	
	@Inject AppImages images;
	@Inject Messages messages;
	
	private ColaboradorProxy colaborador;
	private ListStore<ColaboradorProxy> store;
	private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<ColaboradorProxy>> loader;
	private final Driver driver;
	private final ColaboradorEditor editor;
	private final AppRequestFactory factory;
	private final Binder uiBinder;
	
	@UiField(provided = true)
	Grid<ColaboradorProxy> grid;
	
	@UiField(provided = true)
	PagingToolBar toolBar;
	 
	@UiField
	TextButton edit;
	 
	@UiField
	TextButton delete;	 
	   
	 

	@Inject
	public ColaboradorPanel(final Binder uiBinder, EventBus eventBus, Provider<AppRequestFactory> provider, Driver driver, ColaboradorEditor editor){
		this.uiBinder = uiBinder;
		this.editor = editor;
		this.driver = driver;
		this.factory = provider.get();		

		driver.initialize(factory, editor);
		
		eventBus.addHandler(SaveEvent.getType(), saveHandler);		

	}

	public Widget asWidget() {
		RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<ColaboradorProxy>> proxy = new RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<ColaboradorProxy>>() {
		    
			 @Override
			 public void load(FilterPagingLoadConfig loadConfig,
			   Receiver<? super PagingLoadResult<ColaboradorProxy>> receiver) {
			   ColaboradorService cs = factory.colaboradorService();			   
			   List<SortInfo> sortInfo = createRequestSortInfo(cs, loadConfig.getSortInfo());
		       
			   List<FilterConfig> filterConfig = createRequestFilterConfig(cs, loadConfig.getFilters());
			   
			   cs.list(loadConfig.getOffset(), loadConfig.getLimit(), sortInfo, filterConfig).to(receiver);
			   
			   cs.fire();    
			 }
			 
			};
			 
			loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<ColaboradorProxy>>(proxy){
				@Override
			    protected FilterPagingLoadConfig newLoadConfig() {
			      return new FilterPagingLoadConfigBean();
			    }
			};
			loader.setRemoteSort(true);
			ColaboradorProxyProperties props = GWT.create(ColaboradorProxyProperties.class);
			
			store = new ListStore<ColaboradorProxy>(props.id());
			loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, ColaboradorProxy, PagingLoadResult<ColaboradorProxy>>(store));
			   
			toolBar = new PagingToolBar(2);
			toolBar.bind(loader);
			toolBar.getElement().getStyle().setProperty("borderBottom", "none");
			    
			ColumnConfig<ColaboradorProxy, String> nombresColumn = new ColumnConfig<ColaboradorProxy, String>(props.nombres(), 150, "Nombres");
			ColumnConfig<ColaboradorProxy, String> apellidosColumn = new ColumnConfig<ColaboradorProxy, String>(props.apellidos(), 150, "Apellidos");
			ColumnConfig<ColaboradorProxy, Integer> edadColumn = new ColumnConfig<ColaboradorProxy, Integer>(props.edad(), 80, "Edad");
			 
			List<ColumnConfig<ColaboradorProxy, ?>> l = new ArrayList<ColumnConfig<ColaboradorProxy, ?>>();
			l.add(nombresColumn);
			l.add(apellidosColumn);
			l.add(edadColumn);
			 
			ColumnModel<ColaboradorProxy> cm = new ColumnModel<ColaboradorProxy>(l);
			 
			grid = new Grid<ColaboradorProxy>(store, cm) {
			    @Override
			    protected void onAfterFirstAttach() {
			      super.onAfterFirstAttach();
			      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			        public void execute() {
			          loader.load();
			        }
			      });
			    }
			};
			 
			grid.setLoader(loader);
			grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			GridFilters<ColaboradorProxy> filters = new GridFilters<ColaboradorProxy>(loader);
		    filters.initPlugin(grid);
		    filters.setLocal(false);
		    filters.addFilter(new StringFilter<ColaboradorProxy>(props.nombres()));
		    filters.addFilter(new StringFilter<ColaboradorProxy>(props.apellidos()));
		    filters.addFilter(new NumericFilter<ColaboradorProxy, Integer>(props.edad(), new NumberPropertyEditor.IntegerPropertyEditor()));
			 
			grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ColaboradorProxy>() {
			
			  public void onSelectionChanged(
			    SelectionChangedEvent<ColaboradorProxy> event) {
			   int size = event.getSelection().size();
			   if(size == 0){     
			     edit.setEnabled(false);
			     delete.setEnabled(false);
			   }else if(size == 1){      
			     edit.setEnabled(true);
			     delete.setEnabled(true);
			   }else if(size > 1){
			     edit.setEnabled(false);
			     delete.setEnabled(true);
			   }       
			    
			  }
			 });
				 
			return uiBinder.createAndBindUi(this);
	}
	
	@UiHandler("add")
	public void onAdd(SelectEvent event){
	  ColaboradorService cs = factory.colaboradorService();
	  colaborador = cs.create(ColaboradorProxy.class);
	  cs.persist(colaborador);
	  driver.edit(colaborador, cs);
	  editor.clearFields();
	  editor.show("Nuevo Colaborador");
	}
	 
	@UiHandler("edit")
	public void onEdit(SelectEvent event){
	  ColaboradorService cs = factory.colaboradorService();
	  colaborador = grid.getSelectionModel().getSelectedItem();
	  cs.persist(colaborador);
	  driver.edit(colaborador, cs);
	  editor.clearFields();
	  editor.show("Editar Colaborador");
	}
	 
	@UiHandler("delete")
	public void onDelete(SelectEvent event){
	 ConfirmMessageBox box = new ConfirmMessageBox("ExampleRF", "Esta seguro que desea eliminar?");
	 box.addHideHandler(new HideHandler() {
	  
	  public void onHide(HideEvent event) {
	   Dialog btn = (Dialog) event.getSource();
	   if(!"No".equals(btn.getHideButton().getText())){
		ColaboradorService cs = factory.colaboradorService();
	    colaborador = grid.getSelectionModel().getSelectedItem();
	    cs.remove(colaborador).fire(new Receiver<Void>() {
	      
	     @Override
	     public void onSuccess(Void response) {
	      Info.display("ExampleRF", "Se elimino correctamente");
	      loader.load();
	     }
	    });
	   }
	  }
	 });
	 box.show();  
	}	
}
