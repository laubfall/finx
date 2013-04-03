package de.ludwig.finx.gui.controller;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;

/**
 * @author Daniel
 * 
 */
public class ProjectSummaryComponent extends VBox
{
	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	private TitledPane projectTitledPane;

	@BindToBeanProperty(bindPropertyName = "items")
	@FXML
	private ListView<WorkingSetBackingBean> workingSetsContainer;

	private Model<ProjectBackingBean> model = new Model<ProjectBackingBean>(this, new ProjectBackingBean());

	public ProjectSummaryComponent(final ProjectBackingBean modelObject)
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

		workingSetsContainer
				.setCellFactory(new Callback<ListView<WorkingSetBackingBean>, ListCell<WorkingSetBackingBean>>() {

					@Override
					public ListCell<WorkingSetBackingBean> call(ListView<WorkingSetBackingBean> arg0)
					{
						return new WorkingSetCell();
					}
				});

		if (modelObject != null) {
			model.setModelObject(modelObject);
		}
		model.bind();
	}

	@FXML
	private void newWorkingSet(Event e)
	{
		// TODO call WizardStep to create a Working-Set. Then put this into the model
		// --> model.getModelObject().workingSetsContainer.add(...)
	}
}
