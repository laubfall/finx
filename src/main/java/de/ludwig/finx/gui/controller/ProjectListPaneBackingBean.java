package de.ludwig.finx.gui.controller;

import java.util.List;

import javafx.beans.property.SimpleListProperty;

/**
 * @author Daniel
 * 
 */
public class ProjectListPaneBackingBean
{
	private SimpleListProperty<ProjectBackingBean> projectsBox = new SimpleListProperty<>();

	/**
	 * @return the projectsBox
	 */
	public List<ProjectBackingBean> getProjectsBox()
	{
		return projectsBox.get();
	}

	/**
	 * @param projectsBox
	 *            the projectsBox to set
	 */
	public void setProjectsBox(List<ProjectBackingBean> projectsBox)
	{
		this.projectsBox.addAll(projectsBox);
	}

	public SimpleListProperty<ProjectBackingBean> projectsBoxProperty()
	{
		return projectsBox;
	}
}
