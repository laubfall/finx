package de.ludwig.finx.gui.wizard.project;

import de.ludwig.finx.gui.controller.ProjectBackingBean;

/**
 * @author Daniel
 * 
 */
public class ProjectWizardBackingBean
{
	private ProjectSettingsBackingBean projectSettingsStep = new ProjectSettingsBackingBean();

	private WorkingSetSettingsBackingBean workingSetSettingsStep = new WorkingSetSettingsBackingBean();

	/**
	 * @return the projectSettingsStep
	 */
	public ProjectSettingsBackingBean getProjectSettingsStep()
	{
		return projectSettingsStep;
	}

	/**
	 * @param projectSettingsStep
	 *            the projectSettingsStep to set
	 */
	public void setProjectSettingsStep(ProjectSettingsBackingBean projectSettingsStep)
	{
		this.projectSettingsStep = projectSettingsStep;
	}

	/**
	 * @return the workingSetSettingsStep
	 */
	public WorkingSetSettingsBackingBean getWorkingSetSettingsStep()
	{
		return workingSetSettingsStep;
	}

	/**
	 * @param workingSetSettingsStep
	 *            the workingSetSettingsStep to set
	 */
	public void setWorkingSetSettingsStep(WorkingSetSettingsBackingBean workingSetSettingsStep)
	{
		this.workingSetSettingsStep = workingSetSettingsStep;
	}

	public ProjectBackingBean convert()
	{
		final ProjectBackingBean pbb = new ProjectBackingBean();
		pbb.setProjectTitledPane(projectSettingsStep.getProjectName());
		pbb.setWorkingSetsContainer(workingSetSettingsStep.getWorkingSetsComponent());
		// for (final WorkingSetBackingBean wsbb : workingSetSettingsStep.getWorkingSetsComponent())
		// {
		// final WorkingSetBackingBean nWsbb = new WorkingSetBackingBean(wsbb.getPropDir(),
		// wsbb.getPrefix(),
		// wsbb.getPostfix(), wsbb.getSourceDirs());
		// pbb.getWorkingSetsContainer().add(nWsbb);
		// }
		return pbb;
	}
}
