package de.ludwig.finx.gui.controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import de.ludwig.finx.ApplicationCodingException;

/**
 * @author Daniel
 * 
 */
public class AppLayout
{
	private final Node content;

	@FXML
	public MenuItem openWorkspace;

	@FXML
	public MenuItem menuItemClose;

	public AppLayout()
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"/de/ludwig/finx/gui/fxml/AppLayoutAnchorPane.fxml"));
			loader.setController(this);
			content = (Node) loader.load();
		} catch (IOException e) {
			throw new ApplicationCodingException("Unable to load FXML", e);
		}

		openWorkspace.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event)
			{
				AppLayout.this.openWorkspace(event);
			}
		});
	}

	public void openWorkspace(Event e)
	{
		FileChooser fc = new FileChooser();
		fc.setTitle("select workspace");
		File workspace = fc.showOpenDialog(null);

	}

	/**
	 * @return the content
	 */
	public AnchorPane getContent()
	{
		return (AnchorPane) content;
	}

	public Group grouped()
	{
		return new Group(getContent());
	}
}
