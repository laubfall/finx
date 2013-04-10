package de.ludwig.finx.gui.controller;

import javafx.beans.property.SimpleObjectProperty;
import de.ludwig.finx.gui.component.ProjectBackingBean;
import de.ludwig.finx.gui.component.accordion.AccordionBackingBean;
import de.ludwig.finx.gui.component.accordion.AccordionTitledPaneBackingBean;

/**
 * @author Daniel
 * 
 */
public class ProjectListPaneBackingBean
{
	private SimpleObjectProperty<AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>>> projectsView2 = new SimpleObjectProperty<>(
			new AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>>());

	/**
	 * @return the projectsView2
	 */
	public AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>> getProjectsView2()
	{
		return projectsView2.get();
	}

	/**
	 * @param projectsView2
	 *            the projectsView2 to set
	 */
	public void setProjectsView2(AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>> projectsView2)
	{
		this.projectsView2.set(projectsView2);
	}

	public SimpleObjectProperty<AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>>> projectsView2Property()
	{
		return projectsView2;
	}
}
