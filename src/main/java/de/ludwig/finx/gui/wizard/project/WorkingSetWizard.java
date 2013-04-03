package de.ludwig.finx.gui.wizard.project;

import javafx.stage.Modality;
import de.ludwig.finx.gui.wizard.Wizard;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;

/**
 * @author Daniel
 * 
 */
public class WorkingSetWizard extends Wizard
{
	@BindToBeanProperty
	private WorkingSetSettingsStep workingSetSettingsStep;

	private Model<WorkingSetWizardBackingBean> model = new Model<WorkingSetWizardBackingBean>(this);

	public WorkingSetWizard(final WorkingSetWizardBackingBean modelObject)
	{
		workingSetSettingsStep = new WorkingSetSettingsStep();
		initModality(Modality.APPLICATION_MODAL);
		getContent().addPage(workingSetSettingsStep);
		getContent().start();

		model.setModelObject(modelObject);
		model.bind();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.gui.wizard.Wizard#onFinish()
	 */
	@Override
	public void onFinish()
	{
		close();
	}

}
