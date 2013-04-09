package de.ludwig.finx.gui.component.accordion;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * For the lazy ones who don't want to implement a base-backing-bean for the accordian for
 * themselves :-)
 * 
 * @author Daniel
 * 
 * @param <T>
 */
public class AccordionBackingBean<T extends AccordionTitledPaneBackingBean<?>>
{

	private SimpleListProperty<T> items = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<T>()));

	/**
	 * @return the testBackingList
	 */
	public List<T> getItems()
	{
		return items;
	}

	/**
	 * @param items
	 *            the testBackingList to set
	 */
	public void setItems(List<T> items)
	{
		this.items.addAll(items);
	}

	public SimpleListProperty<T> itemsProperty()
	{
		return items;
	}
}
