package de.ludwig.finx.gui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

/**
 * @author Daniel
 * 
 */
public class ProjectBackingBean
{
	private SimpleStringProperty projectTitledPane = new SimpleStringProperty();

	private SimpleListProperty<WorkingSetBackingBean> workingSetsContainer = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<WorkingSetBackingBean>()));

	/**
	 * @return the name
	 */
	public String getProjectTitledPane()
	{
		return projectTitledPane.get();
	}

	/**
	 * @param projectName
	 *            the name to set
	 */
	public void setProjectTitledPane(String projectTitledPane)
	{
		this.projectTitledPane.set(projectTitledPane);
	}

	public SimpleStringProperty projectTitledPaneProperty()
	{
		return projectTitledPane;
	}

	/**
	 * @return the workingSetsContainer
	 */
	public List<WorkingSetBackingBean> getWorkingSetsContainer()
	{
		return workingSetsContainer.get();
	}

	/**
	 * @param workingSetsContainer
	 *            the workingSetsContainer to set
	 */
	public void setWorkingSetsContainer(List<WorkingSetBackingBean> workingSetsContainer)
	{
		this.workingSetsContainer.addAll(workingSetsContainer);
	}

	public SimpleListProperty<WorkingSetBackingBean> workingSetsContainerProperty()
	{
		return workingSetsContainer;
	}
}
