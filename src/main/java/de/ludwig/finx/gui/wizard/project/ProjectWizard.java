package de.ludwig.finx.gui.wizard.project;

import java.io.File;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Modality;
import de.ludwig.finx.gui.controller.ProjectModel;
import de.ludwig.finx.gui.controller.WorkingSetModel;
import de.ludwig.finx.gui.wizard.Wizard;
import de.ludwig.finx.workspace.Project;

/**
 * @author Daniel
 * 
 */
public class ProjectWizard extends Wizard
{
	private ProjectSettingsStep projectSettingsStep;

	private WorkingSetSettingsStep workingSetSettingsStep;

	private Project project = null;

	private SimpleBooleanProperty finished = new SimpleBooleanProperty(false);

	public ProjectWizard()
	{
		projectSettingsStep = new ProjectSettingsStep(new ProjectModel());
		workingSetSettingsStep = new WorkingSetSettingsStep();
		initModality(Modality.APPLICATION_MODAL);
		getContent().addPage(projectSettingsStep);
		getContent().addPage(workingSetSettingsStep);
		getContent().start();
	}

	@Override
	public void onFinish()
	{
		final ProjectModel p = projectSettingsStep.modelObject();
		final List<WorkingSetModel> ws = workingSetSettingsStep.modelObject();

		project = new Project(p.getName());
		for (WorkingSetModel wsm : ws) {
			// WorkingSet fromModel = wsm.getWs();
			project.addWorkingSet(wsm.getPropDir(), wsm.getSourceDirs().toArray(new File[wsm.getSourceDirs().size()]))
					.changePropertiesReader(wsm.getPostfix(), wsm.getPrefix());
		}

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

	/**
	 * @return the project
	 */
	public Project getProject()
	{
		return project;
	}
}
