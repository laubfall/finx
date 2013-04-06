package de.ludwig.finx.gui.component;

import java.io.File;
import java.io.IOException;

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
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;

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

	@BindToBeanProperty(bindPropertyName = "text", converter = FileStringConverter.class)
	@FXML
	private Text propDir;

	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	private Text postfix;

	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	private Text prefix;

	@BindToBeanProperty(bindPropertyName = "items")
	@FXML
	private ListView<File> sourceDirs;

	@FXML
	private Button delete;

	private Model<WorkingSetBackingBean> model = new Model<>(this);

	public WorkingSetComponent(WorkingSetBackingBean backingBean)
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

		this.model.setModelObject(backingBean == null ? new WorkingSetBackingBean() : backingBean);
		this.model.bind();
	}

	@FXML
	private void delete(Event e)
	{
		model.getModelObject().setDeleted(true);
	}
}
