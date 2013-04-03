package de.ludwig.finx.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import de.ludwig.finx.gui.wizard.project.ProjectWizard;
import de.ludwig.finx.gui.wizard.project.ProjectWizardBackingBean;

/**
 * @author Daniel
 * 
 */
public class ProjectListPane implements Initializable
{

	@FXML
	private VBox noProjectVBox;

	@FXML
	private ScrollPane projects;

	/**
	 * TODO dl i think we have to change this to a listview in order to bind a model-object
	 */
	@FXML
	private VBox projectsBox;

	public ProjectListPane()
	{

	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		noProjects();
		projects.setContent(projectsBox);
	}

	@FXML
	private void addProject()
	{
		final ProjectWizard pw = new ProjectWizard();
		pw.finishedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue) {
					final ProjectWizardBackingBean modelObject = pw.modelObject();
					final ProjectSummaryComponent psc = new ProjectSummaryComponent(modelObject.convert());
					projectsBox.getChildren().add(psc);
					ProjectListPane.this.projects();
				}
			}
		});
		pw.show();
	}

	private void noProjects()
	{
		noProjectVBox.setVisible(true);
		projects.setVisible(false);
	}

	private void projects()
	{
		noProjectVBox.setVisible(false);
		projects.setVisible(true);
	}
}
