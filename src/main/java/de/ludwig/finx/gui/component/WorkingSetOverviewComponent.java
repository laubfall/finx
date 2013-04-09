package de.ludwig.finx.gui.component;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

/**
 * Extends ListView to place an own CellFactory. The used CellFactory uses {@link WorkingSetCell} to
 * render Cells.
 * 
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
		getStylesheets().add("/de/ludwig/finx/gui/css/workingSetOverviewComponent.css");

		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		setCellFactory(new Callback<ListView<WorkingSetBackingBean>, ListCell<WorkingSetBackingBean>>() {

			@Override
			public ListCell<WorkingSetBackingBean> call(ListView<WorkingSetBackingBean> arg0)
			{
				return new WorkingSetCell() {

					@Override
					public void onDeletion(final WorkingSetBackingBean wsc)
					{
						WorkingSetOverviewComponent.this.getItems().remove(wsc);
					}
				};
			}
		});

	}
}
