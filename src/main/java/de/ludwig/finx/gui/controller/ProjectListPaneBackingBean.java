package de.ludwig.finx.gui.controller;

import de.ludwig.finx.gui.component.ProjectBackingBean;
import de.ludwig.finx.gui.component.accordion.AccordionBackingBean;
import de.ludwig.finx.gui.component.accordion.AccordionTitledPaneBackingBean;

/**
 * @author Daniel
 * 
 */
public class ProjectListPaneBackingBean
{
	private AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>> projectsView2 = new AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>>();

	/**
	 * @return the projectsView2
	 */
	public AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>> getProjectsView2()
	{
		return projectsView2;
	}

	/**
	 * @param projectsView2
	 *            the projectsView2 to set
	 */
	public void setProjectsView2(AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>> projectsView2)
	{
		this.projectsView2 = projectsView2;
	}

	// public
	// SimpleObjectProperty<AccordionBackingBean<AccordionTitledPaneBackingBean<ProjectBackingBean>>>
	// projectsView2Property()
	// {
	// return projectsView2;
	// }
}
