/**
 * 
 */
package de.ludwig.finx.command;

import java.util.List;

/**
 * @author Daniel
 *
 */
public class ShowKnownCommands implements Command<Void> {

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute()
	 */
	public Void execute(String payload) {
		List<Command<?>> knownCommands = CommandHub.getKnownCommands();
		for(Command<?> c : knownCommands) {
			System.out.println(c.name());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name() {
		return "availablecommands";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help() {
		return name() + " print all available commands";
	}

}
