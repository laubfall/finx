package de.ludwig.finx.gui.wizard;

import java.io.IOException;
import java.net.URL;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import de.ludwig.finx.ApplicationCodingException;

/**
 * @author Daniel
 * 
 */
public abstract class WizardStep extends Pane
{
	protected SimpleObjectProperty<ValidationContext> validationCtx = new SimpleObjectProperty<>();

	public WizardStep()
	{
		super();
	}

	public WizardStep(URL fxml)
	{
		super();
		FXMLLoader loader = new FXMLLoader(fxml);
		loader.setController(this);

		final Parent content;
		try {
			content = (Parent) loader.load();
		} catch (IOException e) {
			throw new ApplicationCodingException("unable to load content for wizard-step");
		}

		getChildren().add(content);
	}

	// Controll methods to allow implementing Pages to decide wether or not corresponding buttons
	// are selectable.
	// Visibility is controlled by the wizard itself, because that is dependent of the information
	// how many pages the current wizard owns.
	public abstract boolean next();

	public abstract boolean finish();

	public abstract boolean previous();

	// <--

	// Methods that are called by the wizard if the corresponding buttons are clicked.
	public abstract void onNext();

	public abstract void onFinish();

	public abstract void onPrevious();

	public abstract void onCancel();

	// <--

	public abstract void validate() throws StepValidationException;

	public abstract String wizardStepDescription();
}
