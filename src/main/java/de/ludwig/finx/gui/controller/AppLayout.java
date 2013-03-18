package de.ludwig.finx.gui.controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import de.ludwig.finx.gui.popup.AppSettingsPopup;

/**
 * @author Daniel
 * 
 */
public class AppLayout extends BaseController
{
	@FXML
	private MenuItem openWorkspace;

	@FXML
	private MenuItem menuItemClose;

	public AppLayout()
	{
		super(AppLayout.class.getResource("/de/ludwig/finx/gui/fxml/AppLayoutAnchorPane.fxml"));
		openWorkspace.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event)
			{
				AppLayout.this.openWorkspace(event);
			}
		});
	}

	private void openWorkspace(Event e)
	{
		FileChooser fc = new FileChooser();
		fc.setTitle("select workspace");
		File workspace = fc.showOpenDialog(null);
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
