package de.ludwig.finx.gui.controller;

import java.io.File;
import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import de.ludwig.finx.jfx.converter.FileStringConverter;
import de.ludwig.finx.workspace.WorkingSet;

/**
 * Shows all relevant information of {@link WorkingSet}s
 * 
 * @author Daniel
 * 
 */
public class WorkingSetComponent extends VBox
{

	@FXML
	private GridPane contentGridPane;

	@FXML
	private Text propDir;

	@FXML
	private Text postfix;

	@FXML
	private Text prefix;

	@FXML
	private ListView<File> sourceDirs;

	@FXML
	private Button delete;

	/**
	 * Marks that component as deleted. Container can hold a listener on this property to decide if
	 * they show this component or not.
	 */
	private BooleanProperty deleted = new SimpleBooleanProperty(false);

	public WorkingSetComponent(WorkingSetModel model)
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

		Bindings.bindContentBidirectional(sourceDirs.itemsProperty().get(), model.sourceDirsProperty());
		Bindings.bindBidirectional(propDir.textProperty(), model.propDirProperty(), new FileStringConverter());
		Bindings.bindBidirectional(postfix.textProperty(), model.postfixProperty());
		Bindings.bindBidirectional(prefix.textProperty(), model.prefixProperty());
	}

	public boolean isDeleted()
	{
		return deleted.get();
	}

	public void setDeleted(final boolean deleted)
	{
		this.deleted.set(deleted);
	}

	public BooleanProperty deletedProperty()
	{
		return deleted;
	}

	@FXML
	private void delete(Event e)
	{
		deleted.set(true);
	}
}
