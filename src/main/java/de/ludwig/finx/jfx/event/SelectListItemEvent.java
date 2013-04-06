package de.ludwig.finx.jfx.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author Daniel
 * 
 */
public class SelectListItemEvent<T> extends Event
{
	public static final EventType<SelectListItemEvent<?>> SELECT = new EventType<>("select");

	private static final long serialVersionUID = 775919833034799366L;

	private T selectedItem;

	/**
	 * @param eventType
	 */
	public SelectListItemEvent(T selectedItem)
	{
		super(SELECT);
		this.selectedItem = selectedItem;
	}

	public T getSelectedItem()
	{
		return selectedItem;
	}
}
