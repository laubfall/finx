package de.ludwig.finx.gui.wizard.project;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import de.ludwig.finx.gui.wizard.StepValidationException;
import de.ludwig.finx.gui.wizard.WizardStep;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombined;

/**
 * 
 * @author Daniel
 * 
 */
class ProjectSettingsStep extends WizardStep<ProjectSettingsBackingBean> implements SupportCombined
{
	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	protected TextField projectName;

	private Model<ProjectSettingsBackingBean> model = new Model<ProjectSettingsBackingBean>(this,
			new ProjectSettingsBackingBean());

	/**
	 * @param fxml
	 */
	public ProjectSettingsStep()
	{
		super(ProjectSettingsStep.class.getResource("/de/ludwig/finx/gui/fxml/ProjectWizard$ProjectSettingsPage.fxml"));
	}

	@Override
	public boolean next()
	{
		return true;
	}

	@Override
	public boolean finish()
	{
		return false;
	}

	@Override
	public boolean previous()
	{
		return false;
	}

	@Override
	public void onNext()
	{

	}

	@Override
	public void onFinish()
	{
		// NOOP
	}

	@Override
	public void onPrevious()
	{
		// NOOP
	}

	@Override
	public void onCancel()
	{
		// NOOP
	}

	@Override
	public void validate() throws StepValidationException
	{
		// if (StringUtils.isBlank(model.getName())) {
		// throw new StepValidationException("Project name needs to be set");
		// }
	}

	@Override
	public String wizardStepDescription()
	{
		return "Define project settings";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.jfxmodel.SupportCombined#getModel()
	 */
	@Override
	public Model<?> getModel()
	{
		return model;
	}
}