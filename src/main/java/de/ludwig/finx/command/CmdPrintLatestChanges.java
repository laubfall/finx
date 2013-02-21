/**
 * 
 */
package de.ludwig.finx.command;

import java.util.List;

import de.ludwig.finx.io.PropertyFileHandling;
import de.ludwig.finx.scanner.I18nKey;

public class CmdPrintLatestChanges implements Command
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#execute()
	 */
	public Object execute(String payload)
	{
		cmdPrintLatestChanges();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "printchanges";
	}

	private void cmdPrintLatestChanges()
	{
//		PropertyFileHandling.instance().nodeStructureFromFiles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return name() + ": shows all changes of I18n References in Source-Files";
	}
}