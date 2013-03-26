package de.ludwig.finx.gui.controller;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Daniel
 * 
 */
public class ProjectModel
{
	private SimpleStringProperty name = new SimpleStringProperty();

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name.get();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name.set(name);
	}

	public SimpleStringProperty nameProperty()
	{
		return name;
	}
}
