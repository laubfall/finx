package de.ludwig.finx.jfx.event;

import de.ludwig.finx.gui.component.WorkingSetBackingBean;

/**
 * @author Daniel
 * 
 */
public class SelectWorkingSetEvent extends SelectListItemEvent<WorkingSetBackingBean>
{

	private static final long serialVersionUID = 3428611182402766893L;

	/**
	 * @param selectedItem
	 */
	public SelectWorkingSetEvent(WorkingSetBackingBean selectedItem)
	{
		super(selectedItem);
	}

}
