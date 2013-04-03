package de.ludwig.finx.gui.wizard;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that allows components to anticpate at the validation progress of the wizard. The wizard
 * has the possibilty to display validation messages if it comes to validation-errors while
 * switching to the next step. If you want to validate and display possible message that results of
 * an internal process of the step you can add all messages to this class an set it into the
 * {@link WizardStep#validationCtx} Property.
 * 
 * @author Daniel
 * 
 */
public class ValidationContext
{

	private Set<String> validationMessages = new HashSet<>();

	private boolean preventsNextPage;

	public void addValidationMessage(final String msg)
	{
		validationMessages.add(msg);
	}

	public boolean isValid()
	{
		return validationMessages.isEmpty();
	}

	/**
	 * @return the validationMessages
	 */
	public Set<String> getValidationMessages()
	{
		return validationMessages;
	}

	/**
	 * @return the preventsNextPage
	 */
	public boolean isPreventsNextPage()
	{
		return preventsNextPage;
	}

	/**
	 * @param preventsNextPage
	 *            the preventsNextPage to set
	 */
	public void setPreventsNextPage(boolean preventsNextPage)
	{
		this.preventsNextPage = preventsNextPage;
	}

}
