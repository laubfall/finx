package de.ludwig.finx.gui.wizard;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Daniel
 * 
 */
public class Wizard extends Stage
{
	private final Content content;

	/**
	 * 
	 */
	public Wizard()
	{
		super();
		initModality(Modality.APPLICATION_MODAL);

		final Group root = new Group();
		content = new Content();

		content.getCancel().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event)
			{
				Wizard.this.onCancel();
			}
		});

		content.getFinish().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event)
			{
				Wizard.this.onFinish();
			}
		});

		root.getChildren().add(content.getContent());
		final Scene scene = new Scene(root);
		this.setScene(scene);
	}

	/**
	 * @return the content
	 */
	public Content getContent()
	{
		return content;
	}

	public void onCancel()
	{
		close();
	}

	public void onFinish()
	{

	}
}
