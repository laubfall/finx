package de.ludwig.finx;

/**
 * This Exception is used if some unexpected application behaviour occured. For
 * example: if a required member is not initialized or more general required
 * application resources are not ready in a way the application can proceed in a
 * normal way.
 * 
 * At a central point of the application there should be a catch-block for this
 * type of exception so the applicaton can decide what to do next (maybe some
 * error-message for the user etc.)
 * 
 * @author Daniel
 * 
 */
public class ApplicationCodingException extends RuntimeException
{
	private static final long serialVersionUID = -5261098003765399509L;

	/**
	 * 
	 */
	public ApplicationCodingException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ApplicationCodingException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public ApplicationCodingException(String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ApplicationCodingException(Throwable arg0)
	{
		super(arg0);
	}

}
