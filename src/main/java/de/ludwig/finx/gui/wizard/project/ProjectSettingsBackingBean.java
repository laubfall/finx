package de.ludwig.finx.gui.wizard.project;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Daniel
 * 
 */
public class ProjectSettingsBackingBean
{

	private SimpleStringProperty projectName = new SimpleStringProperty();

	/**
	 * @return the name
	 */
	public String getProjectName()
	{
		return projectName.get();
	}

	/**
	 * @param projectName
	 *            the name to set
	 */
	public void setProjectName(String projectName)
	{
		this.projectName.set(projectName);
	}

	public SimpleStringProperty projectNameProperty()
	{
		return projectName;
	}

}
