package de.ludwig.finx.command;

/**
 * TODO prüfen ob diese Klasse noch benötigt wird
 * 
 * @author Daniel
 *
 */
public class YesNoChoice implements Command<Boolean>
{

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Boolean execute(String payload) throws CommandException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "yesNoChoice";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#userAccessible()
	 */
	public boolean userAccessible()
	{
		return false;
	}

}
