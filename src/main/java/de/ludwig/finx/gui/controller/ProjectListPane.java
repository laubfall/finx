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
import de.ludwig.jfxmodel.Model;

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
	 * TODO dl i think we have to change this to a listview in order to bind a model-object TODO
	 * binding to model
	 */
	@FXML
	private VBox projectsBox;

	private Model<ProjectListPaneBackingBean> model = new Model<>(this);

	/**
	 * 
	 * @param modelObject
	 *            TODO bad idea. Thats a component and so can be initiated by fxml...
	 */
	public ProjectListPane(ProjectListPaneBackingBean modelObject)
	{

		model.setModelObject(modelObject);
		model.bind();
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
