package de.ludwig.finx.gui.wizard.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import de.ludwig.finx.gui.controller.WorkingSetComponent;
import de.ludwig.finx.gui.controller.WorkingSetModel;
import de.ludwig.finx.gui.wizard.StepValidationException;
import de.ludwig.finx.gui.wizard.WizardStep;

/**
 * 
 * @author Daniel
 * 
 */
class WorkingSetSettingsStep extends WizardStep<List<WorkingSetModel>>
{
	@FXML
	protected ListView<File> sourceDirOverview;

	@FXML
	protected Button removeSourceDir;

	@FXML
	protected VBox configuredWorkingSets;

	@FXML
	protected TextField propertiesDirPath;

	@FXML
	protected TextField prefix;

	@FXML
	protected TextField postfix;

	private List<WorkingSetModel> workingSetModels = new ArrayList<>();

	public WorkingSetSettingsStep()
	{
		super(WorkingSetSettingsStep.class
				.getResource("/de/ludwig/finx/gui/fxml/ProjectWizard$WorkingSetSettingsPage.fxml"));

		removeSourceDir.setDisable(true);

		sourceDirOverview.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<File>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends File> c)
			{
				removeSourceDir.setDisable(c.getList().isEmpty());
			}
		});

		sourceDirOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	@FXML
	protected void addSourceDir(final Event e)
	{
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("select Source Directory");
		File source = dc.showDialog(null);
		if (source == null) {
			return;
		}
		sourceDirOverview.getItems().add(source);
	}

	@FXML
	protected void removeSourceDir(final Event e)
	{
		final ObservableList<File> selectedItems = sourceDirOverview.getSelectionModel().getSelectedItems();
		for (File sel : selectedItems) {
			sourceDirOverview.getItems().remove(sel);
		}
		sourceDirOverview.getSelectionModel().clearSelection();
	}

	@FXML
	protected void createWorkingSet(final Event e)
	{
		final WorkingSetModel wm = new WorkingSetModel(new File(propertiesDirPath.getText()), prefix.getText(),
				postfix.getText(), sourceDirOverview.getItems());
		workingSetModels.add(wm);
		final WorkingSetComponent wsc = new WorkingSetComponent(wm);

		wsc.deletedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue) {
					configuredWorkingSets.getChildren().remove(wsc);
					workingSetModels.remove(wm);
				}
			}
		});

		configuredWorkingSets.getChildren().add(wsc);
	}

	@FXML
	protected void propertiesDirSelect(final Event e)
	{
		final DirectoryChooser dc = new DirectoryChooser();
		final File showDialog = dc.showDialog(null);
		if (showDialog == null)
			return;

		propertiesDirPath.setText(showDialog.getAbsolutePath());
	}

	@Override
	public boolean next()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean finish()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean previous()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onNext()
	{
		// NOOP
	}

	@Override
	public void onFinish()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrevious()
	{
		// NOOP
	}

	@Override
	public void onCancel()
	{
		// NOOP
	}

	@Override
	public List<WorkingSetModel> modelObject()
	{
		return workingSetModels;
	}

	@Override
	public void validate() throws StepValidationException
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.gui.wizard.WizardStep#wizardStepDescription()
	 */
	@Override
	public String wizardStepDescription()
	{
		return "Create workingsets";
	}

}