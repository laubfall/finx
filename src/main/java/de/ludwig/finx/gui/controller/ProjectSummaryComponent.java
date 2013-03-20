package de.ludwig.finx.gui.controller;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import de.ludwig.finx.ApplicationCodingException;

/**
 * @author Daniel
 * 
 */
public class ProjectSummaryComponent extends VBox
{
	@FXML
	private VBox projectSummary;

	@FXML
	private VBox projectsContainer;

	/**
	 * 
	 */
	public ProjectSummaryComponent()
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
	}

	@FXML
	private void newWorkingSet(Event e)
	{
		projectsContainer.getChildren().add(new WorkingSetComponent());
	}
}
