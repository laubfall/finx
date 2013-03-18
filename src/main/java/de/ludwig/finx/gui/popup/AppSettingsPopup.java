package de.ludwig.finx.gui.popup;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import de.ludwig.finx.gui.controller.AppSettings;

/**
 * @author Daniel
 * 
 */
public class AppSettingsPopup extends Stage
{
	private AppSettings as = new AppSettings(this);

	public AppSettingsPopup()
	{
		initModality(Modality.APPLICATION_MODAL);
		final Group g = new Group(as.getContent());
		Scene scene = new Scene(g);
		setScene(scene);
		setResizable(false);
	}
}
