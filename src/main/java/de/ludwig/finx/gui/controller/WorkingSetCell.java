package de.ludwig.finx.gui.controller;

import javafx.scene.control.ListCell;

/**
 * @author Daniel
 * 
 */
public class WorkingSetCell extends ListCell<WorkingSetBackingBean>
{
	/**
	 * 
	 */
	public WorkingSetCell()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
	 */
	@Override
	protected void updateItem(WorkingSetBackingBean item, boolean empty)
	{
		super.updateItem(item, empty);
		if (empty == false)
			setGraphic(new WorkingSetComponent(item));
	}

}
