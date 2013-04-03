package de.ludwig.finx.gui.wizard.project;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Modality;
import de.ludwig.finx.gui.wizard.Wizard;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;

/**
 * @author Daniel
 * 
 */
public class ProjectWizard extends Wizard
{
	@BindToBeanProperty
	private ProjectSettingsStep projectSettingsStep;

	@BindToBeanProperty
	private WorkingSetSettingsStep workingSetSettingsStep;

	private SimpleBooleanProperty finished = new SimpleBooleanProperty(false);

	private Model<ProjectWizardBackingBean> model = new Model<ProjectWizardBackingBean>(this,
			new ProjectWizardBackingBean());

	public ProjectWizard()
	{
		projectSettingsStep = new ProjectSettingsStep();
		workingSetSettingsStep = new WorkingSetSettingsStep();
		initModality(Modality.APPLICATION_MODAL);
		getContent().addPage(projectSettingsStep);
		getContent().addPage(workingSetSettingsStep);
		getContent().start();

		model.bind();
	}

	@Override
	public void onFinish()
	{
		finished.setValue(true);
		close();
	}

	public Boolean getFinished()
	{
		return finished.getValue();
	}

	public SimpleBooleanProperty finishedProperty()
	{
		return finished;
	}

	public ProjectWizardBackingBean modelObject()
	{
		return model.getModelObject();
	}
}
