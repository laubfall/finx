package de.ludwig.finx.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;

/**
 * Thats a specialized ListCell that renders a WorkingSetComponent into every cell.
 * 
 * @author Daniel
 * 
 */
class WorkingSetCell extends ListCell<WorkingSetBackingBean>
{
	public WorkingSetCell()
	{
		super();
	}

	@Override
	protected void updateItem(final WorkingSetBackingBean item, boolean empty)
	{
		super.updateItem(item, empty);

		if (empty == false) {
			final WorkingSetComponent workingSetComponent = new WorkingSetComponent(item);
			setGraphic(workingSetComponent);

			item.deletedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (newValue) {
						onDeletion(item);
					}
				}
			});
		}

	}

	@Override
	public void updateSelected(boolean arg0)
	{
		// NOOP Prevent this cell from beeing selectable
	}

	public void onDeletion(final WorkingSetBackingBean wsc)
	{

	}
}
