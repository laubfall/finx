package de.ludwig.finx.gui.wizard;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.gui.controller.BaseController;

/**
 * @author Daniel
 * 
 */
public class Content extends BaseController
{

	@FXML
	private Button previous;

	@FXML
	private Button next;

	@FXML
	private Button finish;

	@FXML
	private Button cancel;

	@FXML
	private StackPane pages;

	@FXML
	private VBox validationMessage;

	@FXML
	private Text wizardStepDescription;

	private SimpleObjectProperty<WizardStep> currentPage = new SimpleObjectProperty<>();

	private Wizard owningWizard;

	/**
	 * @param owningWizard
	 *            TODO
	 */
	public Content(Wizard owningWizard)
	{
		super(Content.class.getResource("/de/ludwig/finx/gui/fxml/WizardPane.fxml"));

		this.owningWizard = owningWizard;
		// we set the managed-Property to make sure that visible buttons can invade the space of
		// buttons that are invisible.
		finish.managedProperty().bind(finish.visibleProperty());
		next.managedProperty().bind(next.visibleProperty());
		previous.managedProperty().bind(previous.visibleProperty());

		currentPage.addListener(new ChangeListener<WizardStep>() {
			@Override
			public void changed(ObservableValue<? extends WizardStep> observable, WizardStep oldValue,
					WizardStep newValue)
			{
				if (newValue == null)
					return;

				wizardStepDescription.setText(newValue.wizardStepDescription());
			}
		});
	}

	public final void addPage(final WizardStep page)
	{
		page.setVisible(false);
		pages.getChildren().add(page);
		page.validationCtx.addListener(new ChangeListener<ValidationContext>() {

			@Override
			public void changed(ObservableValue<? extends ValidationContext> observable, ValidationContext oldValue,
					ValidationContext newValue)
			{
				if (newValue == null) {
					validationMessage.setVisible(false);
					clearValidationMessages();
				} else {
					validationMessage.setVisible(true);
					// validationMessage.setText(newValue.getValidationMessage());
					for (String msg : newValue.getValidationMessages()) {
						addNewValidationMessage(msg);
					}
				}
			}
		});
	}

	/**
	 * calls the first page in the list and makes it visible
	 */
	public final void start()
	{
		Node node = pages.getChildren().get(0);
		node.setVisible(true);
		currentPage.set((WizardStep) node);
		handleButtonVisibility();
	}

	private void handleButtonVisibility()
	{
		initCheck();

		WizardStep previousPage = previousPage();
		finish.setVisible(false);
		if (previousPage == null) {
			previous.setVisible(false);
		} else {
			previous.setVisible(true);
		}

		WizardStep nextPage = nextPage();
		if (nextPage == null) {
			next.setVisible(false);
			finish.setVisible(true);
		} else {
			next.setVisible(true);
		}
	}

	// private void handleButtonAccessibility(final WizardStep stepThatControlls) {
	// previous.disableProperty().bind(Bindings.when(stepThatControlls.previous()))
	// }

	/**
	 * checks if the current page has a previous page
	 * 
	 * @return null if there is no previous page otherwise the previous page
	 */
	private WizardStep previousPage()
	{
		int indexOf = pages.getChildren().indexOf(currentPage.get());

		if (indexOf > 0) {
			return (WizardStep) pages.getChildren().get(indexOf - 1);
		}

		return null;
	}

	/**
	 * same as {@link #previousPage()} but only for the next page
	 * 
	 * @return see {@link #previousPage()}
	 */
	private WizardStep nextPage()
	{
		int indexOf = pages.getChildren().indexOf(currentPage.get());
		if (indexOf < pages.getChildren().size() - 1) {
			return (WizardStep) pages.getChildren().get(indexOf + 1);
		}
		return null;
	}

	@FXML
	private void previous(Event e)
	{
		WizardStep cp = currentPage.get();
		cp.onPrevious();

		int indexOf = pages.getChildren().indexOf(currentPage.get());
		cp.setVisible(false);
		currentPage.set((WizardStep) pages.getChildren().get(indexOf - 1));
		currentPage.get().setVisible(true);
		handleButtonVisibility();
	}

	private void initCheck()
	{
		if (currentPage == null) {
			throw new ApplicationCodingException("Wizard not initialized correctly, call start!");
		}
	}

	@FXML
	private void next(Event e)
	{
		WizardStep cp = currentPage.get();
		cp.onNext();
		if (validate(cp) == false) {
			return;
		}
		int indexOf = pages.getChildren().indexOf(currentPage.get());
		cp.setVisible(false);
		currentPage.set((WizardStep) pages.getChildren().get(indexOf + 1));
		currentPage.get().setVisible(true);
		handleButtonVisibility();
	}

	private boolean validate(final WizardStep stepToValidate)
	{
		try {
			validationMessage.setVisible(false);
			clearValidationMessages();
			stepToValidate.validate();
			return true;
		} catch (StepValidationException ex) {
			// TODO maybe highlighting of affected input elements
			addNewValidationMessage(ex.getMessage());
			validationMessage.setVisible(true);
		}
		return false;
	}

	private void addNewValidationMessage(final String msg)
	{
		validationMessage.getChildren().add(new Text(msg));
	}

	private void clearValidationMessages()
	{
		validationMessage.getChildren().clear();
	}

	@FXML
	private void finish(Event e)
	{
		if (validate(currentPage.get()) == false) {
			return;
		}
		currentPage.get().onFinish();
		owningWizard.onFinish();
	}

	@FXML
	private void cancel(Event e)
	{
		currentPage.get().onCancel();
		owningWizard.onCancel();
	}

	/**
	 * @return the previous
	 */
	public Button getPrevious()
	{
		return previous;
	}

	/**
	 * @return the next
	 */
	public Button getNext()
	{
		return next;
	}

	/**
	 * @return the finish
	 */
	public Button getFinish()
	{
		return finish;
	}

	/**
	 * @return the cancel
	 */
	public Button getCancel()
	{
		return cancel;
	}
}
