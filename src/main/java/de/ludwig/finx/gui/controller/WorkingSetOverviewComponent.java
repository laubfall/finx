package de.ludwig.finx.gui.controller;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * @author Daniel
 * 
 */
public class WorkingSetOverviewComponent extends ListView<WorkingSetBackingBean>
{

	/**
	 * 
	 */
	public WorkingSetOverviewComponent()
	{
		super();
		setCellFactory(new Callback<ListView<WorkingSetBackingBean>, ListCell<WorkingSetBackingBean>>() {

			@Override
			public ListCell<WorkingSetBackingBean> call(ListView<WorkingSetBackingBean> arg0)
			{
				return new WorkingSetCell();
			}
		});
	}
}
