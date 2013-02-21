package de.ludwig.finx;

/**
 * Like an {@link ApplicationCodingException} but indicates that another checked or unchecked
 * Exception occured at some point where the application can not decide what to do next or is unable
 * to proceed as expected.
 * 
 * This exception has no Constructor that expects some kind of throwable type. Thats because it
 * makes more sense to handle the original exception at that point where it occured.
 * 
 * @author Daniel
 * 
 */
public class ApplicationException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1387775955067105420L;

	/**
	 * 
	 */
	public ApplicationException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ApplicationException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public ApplicationException(String arg0)
	{
		super(arg0);
	}
}
