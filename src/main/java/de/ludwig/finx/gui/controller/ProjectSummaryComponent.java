package de.ludwig.finx.gui.controller;

import java.io.IOException;
import java.util.Set;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.workspace.Project;
import de.ludwig.finx.workspace.WorkingSet;

/**
 * @author Daniel
 * 
 */
public class ProjectSummaryComponent extends VBox
{
	@FXML
	private TitledPane projectTitledPane;

	@FXML
	private VBox workingSetsContainer;

	/**
	 * @param project
	 *            TODO
	 * 
	 */
	public ProjectSummaryComponent(Project project)
	{
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(
				WorkingSetComponent.class.getResource("/de/ludwig/finx/gui/fxml/ProjectSummaryComponent.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new ApplicationCodingException("unable to load project summary", exception);
		}

		projectTitledPane.setText(project.getName());
		final Set<WorkingSet> workingSets = project.getWorkingSets();
		for (final WorkingSet ws : workingSets) {
			final WorkingSetModel m = new WorkingSetModel(ws.getPropertiesDir(), ws.getI18nPropertiesFilePrefix(),
					ws.getI18nPropertiesFilePostfix(), ws.getSourceDirsAsList());
			final WorkingSetComponent w = new WorkingSetComponent(m);
			workingSetsContainer.getChildren().add(w);
		}
	}

	@FXML
	private void newWorkingSet(Event e)
	{
		workingSetsContainer.getChildren().add(new WorkingSetComponent(null));
	}
}
