package de.ludwig.finx.gui.controller;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import de.ludwig.finx.ApplicationCodingException;

/**
 * @author Daniel
 * 
 */
public abstract class BaseController
{
	private final Node content;

	public BaseController(URL controllerFxml)
	{
		FXMLLoader loader = new FXMLLoader(controllerFxml);
		loader.setController(this);
		try {
			content = (Node) loader.load();
		} catch (IOException e) {
			throw new ApplicationCodingException("unable to load fxml");
		}
	}

	/**
	 * @return the content
	 */
	public Node getContent()
	{
		return content;
	}
}
