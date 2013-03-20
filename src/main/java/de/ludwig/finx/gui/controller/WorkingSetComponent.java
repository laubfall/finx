package de.ludwig.finx.gui.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * @author Daniel
 * 
 */
public class WorkingSetComponent extends VBox
{

	// private boolean oddOrEven = false;

	@FXML
	private GridPane contentGridPane;

	public WorkingSetComponent()
	{
		FXMLLoader fxmlLoader = new FXMLLoader(
				WorkingSetComponent.class.getResource("/de/ludwig/finx/gui/fxml/WorkingSetComponent.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// contentGridPane.
	}

}
