package de.ludwig.finx.gui.wizard;

/**
 * Used to show that a step of a wizard could not be finished because of validation errors.
 * 
 * @author Daniel
 * 
 */
public class StepValidationException extends Exception
{
	private static final long serialVersionUID = -5616608879454335756L;

	/**
	 * @param arg0
	 */
	public StepValidationException(String arg0)
	{
		super(arg0);
	}
}
