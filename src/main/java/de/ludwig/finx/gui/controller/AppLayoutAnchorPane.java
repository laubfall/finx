package de.ludwig.finx.gui.controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import de.ludwig.finx.gui.popup.AppSettingsPopup;
import de.ludwig.finx.workspace.WorkspacePersistencyDao;

/**
 * @author Daniel
 * 
 */
public class AppLayoutAnchorPane extends BaseController
{
	@FXML
	private MenuItem menuItemClose;

	// beyond this point: nested controllers:

	/**
	 * the include has the id projectOverviewPane. If you name this field like the id, you will run
	 * into a ClassCastException or something similar because javafx tries to convert the
	 * Root-Element of the included fxml into this field. But the field is a controller. To get the
	 * controller of the included fxml simply attach "Controller" to the end of the fields name!
	 */
	@FXML
	private ProjectListPane projectOverviewPaneController;

	public AppLayoutAnchorPane()
	{
		super(AppLayoutAnchorPane.class.getResource("/de/ludwig/finx/gui/fxml/AppLayoutAnchorPane.fxml"));
	}

	@FXML
	private void openWorkspace(Event e)
	{
		FileChooser fc = new FileChooser();
		fc.setTitle("select workspace");
		File workspace = fc.showOpenDialog(null);
	}

	@FXML
	private void saveWorkspaceAs(Event e)
	{
		final WorkspacePersistencyDao pers = new WorkspacePersistencyDao();

		// pers.
		// projectOverviewPaneController.
		// pers.saveProject(project)
	}

	@FXML
	private void editPreferences(ActionEvent e)
	{
		final AppSettingsPopup asp = new AppSettingsPopup();
		asp.show();
	}

	/**
	 * @return the content
	 */
	public AnchorPane getContent()
	{
		return (AnchorPane) super.getContent();
	}

	public Group grouped()
	{
		return new Group(getContent());
	}
}
