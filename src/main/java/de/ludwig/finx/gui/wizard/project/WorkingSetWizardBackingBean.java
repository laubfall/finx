package de.ludwig.finx.gui.wizard.project;

/**
 * @author Daniel
 * 
 */
public class WorkingSetWizardBackingBean
{
	private WorkingSetSettingsBackingBean workingSetSettingsStep = new WorkingSetSettingsBackingBean();

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
}
