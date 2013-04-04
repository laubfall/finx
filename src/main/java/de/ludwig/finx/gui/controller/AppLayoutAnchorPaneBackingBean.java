package de.ludwig.finx.gui.controller;

/**
 * @author Daniel
 * 
 */
public class AppLayoutAnchorPaneBackingBean
{
	private ProjectListPaneBackingBean projectOverviewPaneController = new ProjectListPaneBackingBean();

	/**
	 * @return the projectOverviewPaneController
	 */
	public ProjectListPaneBackingBean getProjectOverviewPaneController()
	{
		return projectOverviewPaneController;
	}

	/**
	 * @param projectOverviewPaneController
	 *            the projectOverviewPaneController to set
	 */
	public void setProjectOverviewPaneController(ProjectListPaneBackingBean projectOverviewPaneController)
	{
		this.projectOverviewPaneController = projectOverviewPaneController;
	}
}
