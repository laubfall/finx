/**
 * 
 */
package de.ludwig.finx.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Daniel
 * 
 */
public class CommandHub {
	
	/**
	 * put to the end of a command name (seperated by whitespace) execution will trigger
	 * displaying of {@link Command#help()}
	 */
	public static final String SHOW_COMMAND_HELP_MARKER = "-?";
	
	/**
	 * holds all known Commands, that means Commands that are available for the user.
	 */
	private static List<Command> knownCommands = new ArrayList<Command>();

	/**
	 * Naturally the user decides what command is to be executed next. In the
	 * process of command-dispatching the application checks first if there are
	 * any workflow-commands. If thats true the first command of this list is
	 * executed next and removed if executed sucessfull.
	 * 
	 * TODO nur eine Liste wird nicht reichen. Commands die hier Commands einstellen
	 * werden wahrscheinlich auch ein Interesse am Ergebnis haben. 
	 * 
	 * Anwendungsfall: Command Shutdown und es sind noch nicht gespeicherte Settings vorhanden.
	 */
	private List<Command> workflowCommands = new ArrayList<Command>();
	
	private static Logger log = Logger.getLogger(CommandHub.class);
	
	public static void addCommand(final Command cmd) {
		knownCommands.add(cmd);
	}

	/**
	 * Calls the wanted Command and triggers execution of it.
	 * 
	 * @param fullCommandLineArgument
	 *            Name of the command and some payload (optional) e.g.:
	 *            mycommand somepayload For additional help type: mycommand -?
	 *            this triggers
	 * @return Everything that the Command wants to return. Null if there is
	 *         nothing to return.
	 */
	public static Object execute(final String fullCommandLineArgument) throws CommandException {
		
		if(showHelp(fullCommandLineArgument)) {
			return null;
		}
		
		final int firstEmptySpace = fullCommandLineArgument.indexOf(" ");
		String payload = null;
		String cmdStripped = fullCommandLineArgument;
		if (firstEmptySpace >= 0) {
			payload = fullCommandLineArgument.substring(firstEmptySpace);
			payload = payload.trim();
			cmdStripped = fullCommandLineArgument.substring(0, firstEmptySpace);
		}
		
		final String fCmdOnly = cmdStripped;
		Command cmd = (Command) CollectionUtils.find(knownCommands,
				new Predicate() {
					public boolean evaluate(Object object) {
						Command c = (Command) object;
						return c.name().equals(fCmdOnly);
					}
				});

		if (cmd == null)
			throw new CommandException("Command not found: " + fullCommandLineArgument);

		
		return cmd.execute(payload);
	}

	private static boolean showHelp(final String cmdName) {
		if (cmdName.endsWith(SHOW_COMMAND_HELP_MARKER) == false) {
			return false;
		}

		final String cmdNameOnly = StringUtils.removeEnd(cmdName, SHOW_COMMAND_HELP_MARKER);
		final PrintCommandHelp pch = new PrintCommandHelp();
		try {
			knownCommands.add(pch);
			execute(pch.name() + " " + cmdNameOnly);
		} finally {
			knownCommands.remove(pch);
		}
		
		return true;
	}

	public static List<Command> getKnownCommands() {
		return knownCommands;
	}
}
