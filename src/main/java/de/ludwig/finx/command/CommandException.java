/**
 * 
 */
package de.ludwig.finx.command;

/**
 * Exception that shows that something went wrong during execution
 * of a Command
 * 
 * @author Daniel
 *
 */
public class CommandException extends RuntimeException {

	/**
	 * @param string
	 */
	public CommandException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 424127846712090207L;

}
