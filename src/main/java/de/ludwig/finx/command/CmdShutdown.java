/**
 * 
 */
package de.ludwig.finx.command;

/**
 * @author Daniel
 *
 */
public class CmdShutdown implements Command {

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute()
	 */
	public Object execute(String payload) {
		System.exit(0);
		
		// if(unsavedSettings)
		// {
		//    save?
		// }

		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name() {
		return "cmdShutdown";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help() {
		return name() + " terminate the application";
	}
	
}
