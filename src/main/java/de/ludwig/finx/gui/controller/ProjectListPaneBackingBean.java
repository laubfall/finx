package de.ludwig.finx.gui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * @author Daniel
 * 
 */
public class ProjectListPaneBackingBean
{
	private SimpleListProperty<ProjectBackingBean> projectsView = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<ProjectBackingBean>()));

	/**
	 * @return the projectsBox
	 */
	public List<ProjectBackingBean> getProjectsView()
	{
		return projectsView.get();
	}

	/**
	 * @param projectsBox
	 *            the projectsBox to set
	 */
	public void setProjectsView(List<ProjectBackingBean> projectsView)
	{
		this.projectsView.addAll(projectsView);
	}

	public SimpleListProperty<ProjectBackingBean> projectsViewProperty()
	{
		return projectsView;
	}
}
