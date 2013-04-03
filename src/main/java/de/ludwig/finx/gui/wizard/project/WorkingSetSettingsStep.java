package de.ludwig.finx.gui.wizard.project;

import java.io.File;
import java.util.List;

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

import org.apache.commons.lang3.StringUtils;

import de.ludwig.finx.gui.controller.WorkingSetBackingBean;
import de.ludwig.finx.gui.controller.WorkingSetOverviewComponent;
import de.ludwig.finx.gui.wizard.StepValidationException;
import de.ludwig.finx.gui.wizard.ValidationContext;
import de.ludwig.finx.gui.wizard.WizardStep;
import de.ludwig.finx.jfx.converter.FileStringConverter;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombined;

/**
 * 
 * @author Daniel
 * 
 */
class WorkingSetSettingsStep extends WizardStep implements SupportCombined
{
	@FXML
	protected ListView<File> sourceDirOverview;

	@FXML
	protected Button removeSourceDir;

	@BindToBeanProperty(bindPropertyName = "text", converter = FileStringConverter.class)
	@FXML
	protected TextField propertiesDirPath;

	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	protected TextField prefix;

	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	protected TextField postfix;

	@FXML
	protected VBox configuredWorkingSets;

	@BindToBeanProperty(bindPropertyName = "items")
	private WorkingSetOverviewComponent workingSetsComponent = new WorkingSetOverviewComponent();

	private Model<WorkingSetSettingsBackingBean> model = new Model<WorkingSetSettingsBackingBean>(this,
			new WorkingSetSettingsBackingBean());

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

		configuredWorkingSets.getChildren().add(workingSetsComponent);

		model.bind();
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
		validationCtx.set(null);

		if (validatePropertiesDirPath() == false) {
			return;
		}

		final WorkingSetBackingBean wm = new WorkingSetBackingBean(new File(propertiesDirPath.getText()),
				prefix.getText(), postfix.getText(), sourceDirOverview.getItems());

		if (validateCreateWorkingSet(wm) == false) {
			return;
		}

		final WorkingSetSettingsBackingBean bean = (WorkingSetSettingsBackingBean) getModel().getModelObject();
		bean.workingSetsComponentProperty().add(wm);
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
		// NOOP
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
	public void validate() throws StepValidationException
	{
		if (model.getModelObject().getWorkingSetsComponent().isEmpty()) {
			throw new StepValidationException("you have to define at least one workingset");
		}
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

	private boolean validatePropertiesDirPath()
	{
		final ValidationContext vc = new ValidationContext();
		if (StringUtils.isBlank(propertiesDirPath.getText())) {
			vc.addValidationMessage("no Properties-Directory is set");
		}
		validationCtx.set(vc);
		return vc.isValid();
	}

	private boolean validateCreateWorkingSet(final WorkingSetBackingBean model)
	{
		final ValidationContext vc = new ValidationContext();
		if (StringUtils.isBlank(model.getPostfix())) {
			vc.addValidationMessage("postfix has not to be empty");
		}

		if (StringUtils.isBlank(model.getPrefix())) {
			vc.addValidationMessage("prefix has not to be empty");
		}

		final List<File> sourceDirs = model.getSourceDirs();
		if (sourceDirs == null || sourceDirs.isEmpty()) {
			vc.addValidationMessage("at least you have to define minimum one source directory");
		}

		validationCtx.set(vc);
		return vc.isValid();
	}

	@Override
	public Model<?> getModel()
	{
		return model;
	}

}