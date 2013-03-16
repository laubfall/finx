package de.ludwig.finx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import de.ludwig.finx.gui.controller.AppLayout;

/**
 * 
 * @author Daniel
 */
public class I18nApp extends Application
{

	public static void main(String[] argv)
	{
		Application.launch(I18nApp.class, argv);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		AppLayout applayout = new AppLayout();
		Scene scene = new Scene(applayout.getContent());
		stage.setScene(scene);

		stage.show();
	}
}
