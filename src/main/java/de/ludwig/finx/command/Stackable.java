package de.ludwig.finx.command;

import java.util.List;

/**
 * Steht noch nicht fest ob das bleibt.
 * 
 * Ist dazu gedacht, spezielle Commands vor dem Command laufen zu lassen welches dieses interface
 * implementiert.
 * 
 * @author Daniel
 * 
 */
public interface Stackable
{
	/**
	 * Commands that should run before the implementing command is executed
	 * 
	 * @return
	 */
	List<Command<?>> runTheseCmdsBefore();

	/**
	 * called before the implementing command is executed.
	 * 
	 * @param payload
	 *            the payloads of all commands that were returned by {@link #runTheseCmdsBefore()}
	 */
	void beforeExecute(final List<Object> payload);
}
