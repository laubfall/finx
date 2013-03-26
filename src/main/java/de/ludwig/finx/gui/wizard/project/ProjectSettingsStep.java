package de.ludwig.finx.gui.wizard.project;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.apache.commons.lang3.StringUtils;

import de.ludwig.finx.gui.controller.ProjectModel;
import de.ludwig.finx.gui.wizard.StepValidationException;
import de.ludwig.finx.gui.wizard.WizardStep;

/**
 * 
 * @author Daniel
 * 
 */
class ProjectSettingsStep extends WizardStep<ProjectModel>
{
	@FXML
	protected TextField projectName;

	/**
	 * @param model
	 *            TODO
	 * @param fxml
	 */
	public ProjectSettingsStep(ProjectModel model)
	{
		super(ProjectSettingsStep.class.getResource("/de/ludwig/finx/gui/fxml/ProjectWizard$ProjectSettingsPage.fxml"));
		Bindings.bindBidirectional(projectName.textProperty(), model.nameProperty());
		this.model = model;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void validate() throws StepValidationException
	{
		if (StringUtils.isBlank(model.getName())) {
			throw new StepValidationException("Project name needs to be set");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.gui.wizard.WizardStep#wizardStepDescription()
	 */
	@Override
	public String wizardStepDescription()
	{
		return "Define project settings";
	}
}