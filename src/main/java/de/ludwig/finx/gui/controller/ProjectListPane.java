package de.ludwig.finx.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import de.ludwig.finx.gui.component.ProjectBackingBean;
import de.ludwig.finx.gui.component.ProjectSummaryComponent;
import de.ludwig.finx.gui.component.accordion.AccordionTitledPaneBackingBean;
import de.ludwig.finx.gui.component.accordion.ModelBindedAccordion;
import de.ludwig.finx.gui.wizard.project.ProjectWizard;
import de.ludwig.finx.gui.wizard.project.ProjectWizardBackingBean;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombinedAware;

/**
 * @author Daniel
 * 
 */
public class ProjectListPane implements Initializable, SupportCombinedAware
{

	@FXML
	private VBox noProjectVBox;

	@FXML
	private StackPane projectListContent;

	@BindToBeanProperty
	private ModelBindedAccordion<AccordionTitledPaneBackingBean<ProjectBackingBean>, ProjectBackingBean> projectsView2 = new ModelBindedAccordion<>(
			new ModelBindedAccordion.TitledPaneFactory<ProjectBackingBean>() {

				@Override
				public TitledPane content(ProjectBackingBean modelObject)
				{
					return new ProjectSummaryComponent(modelObject);
				}
			});

	private Model<ProjectListPaneBackingBean> model = new Model<>(this, new ProjectListPaneBackingBean());

	private BooleanBinding emptyBinding;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		noProjects();

		projectListContent.getChildren().add(projectsView2);

		model.bind();
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
					// projectsView.getItems().add(modelObject.convert());
				}
			}
		});
		pw.show();
	}

	private void noProjects()
	{
		noProjectVBox.setVisible(true);
		projectsView2.setVisible(false);
	}

	private void projects()
	{
		noProjectVBox.setVisible(false);
		projectsView2.setVisible(true);
	}

	@Override
	public Model<?> getModel()
	{
		return model;
	}

	class ProjectCell extends ListCell<ProjectBackingBean>
	{
		@Override
		protected void updateItem(ProjectBackingBean arg0, boolean empty)
		{
			super.updateItem(arg0, empty);
			if (empty == false) {
				final ProjectSummaryComponent psc = new ProjectSummaryComponent(arg0);
				setGraphic(psc);
				System.out.println("ProjectListPane$ProjectCell: project count "
						+ getListView().itemsProperty().get().size());
			}
		}

		@Override
		public void updateSelected(boolean arg0)
		{
			// NOOP not selectable
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.jfxmodel.SupportCombinedAware#afterCombinedBinding()
	 */
	@Override
	public void afterCombinedBinding()
	{
		emptyBinding = Bindings.isEmpty(model.getModelObject().projectsView2Property().get().itemsProperty());

		emptyBinding.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue) {
					noProjects();
				} else {
					projects();
				}
			}
		});
	}
}
