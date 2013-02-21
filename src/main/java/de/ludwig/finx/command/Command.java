/**
 * 
 */
package de.ludwig.finx.command;

/**
 * @author Daniel
 *
 */
public interface Command<T extends Object> {
	/**
	 * 
	 * @param payload Normaly you provide the name of the command. In some cases it becomes
	 * necessary to also provide some additional Information for the Command that shall be executed.
	 * Payload is delimited by an empty space from the command name.
	 * @return Anything that the Command wants to return.
	 * 
	 * @throws CommandException If execution of a command fails unexpectedly
	 */
	public T execute(String payload) throws CommandException;
	
	/**
	 * 
	 * @return The name of the command. Type this name to the command line and the command
	 * with that name will executed. Don't user empty spaces for the name!
	 */
	public String name();
	
	/**
	 * 
	 * @return Some help for the specific command. Not null, but may be empty.
	 */
	public String help();
}
