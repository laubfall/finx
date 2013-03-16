package de.ludwig.finx.command.impl;

import de.ludwig.finx.command.Command;
import de.ludwig.finx.command.CommandException;
import de.ludwig.finx.io.PropertyFileHandling;

/**
 * @author Daniel
 *
 */
public class ReloadNodeStructure implements Command<Void>
{

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) throws CommandException
	{
		PropertyFileHandling.instance().reloadNodeStructureFromFiles();
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "loadI18nFiles";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return "Resets the Cache of the actual Node-Structure means: dismiss any changes to the I18n-Node-Structure (new keys etc.)";
	}

}
