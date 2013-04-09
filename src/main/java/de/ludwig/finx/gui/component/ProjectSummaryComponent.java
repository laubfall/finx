package de.ludwig.finx.gui.component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.gui.wizard.project.WorkingSetWizard;
import de.ludwig.finx.gui.wizard.project.WorkingSetWizardBackingBean;
import de.ludwig.finx.jfx.event.SelectWorkingSetEvent;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombinedAware;

/**
 * @author Daniel
 * 
 */
public class ProjectSummaryComponent extends VBox implements SupportCombinedAware, Initializable
{
	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	private TitledPane projectTitledPane;

	@BindToBeanProperty(bindPropertyName = "items")
	@FXML
	private WorkingSetOverviewComponent workingSetsContainer;

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

		if (modelObject != null) {
			model.setModelObject(modelObject);
		}

		model.bind();

		workingSetsContainer.selectionModelProperty().get().selectedItemProperty()
				.addListener(new ChangeListener<WorkingSetBackingBean>() {

					@Override
					public void changed(ObservableValue<? extends WorkingSetBackingBean> observable,
							WorkingSetBackingBean oldValue, WorkingSetBackingBean newValue)
					{
						Event.fireEvent(ProjectSummaryComponent.this, new SelectWorkingSetEvent(newValue));
					}
				});
	}

	@FXML
	private void newWorkingSet(Event e)
	{
		final WorkingSetWizardBackingBean modelObject = new WorkingSetWizardBackingBean();
		final WorkingSetWizard wsw = new WorkingSetWizard(modelObject);
		wsw.showAndWait();
		final ObservableList<WorkingSetBackingBean> workingSets = modelObject.getWorkingSetSettingsStep()
				.getWorkingSetsComponent();
		model.getModelObject().getWorkingSetsContainer().addAll(workingSets);
	}

	@Override
	public Model<?> getModel()
	{
		return model;
	}

	@Override
	public void afterCombinedBinding()
	{
		// bindSelectionModel();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// TODO Results in NPE if you click in a workingSetComponent
		// bindSelectionModel();
	}
}
