package de.ludwig.finx.command;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import de.ludwig.finx.io.PropertyFileHandling;

/**
 * Command to add the path to the directory where all i18n-propertie-files are
 * saved.
 * 
 * This information is necessary to update the propertie-files if there are any
 * changes of i18n-keys.
 * 
 * @author Daniel
 * 
 */
public class AddI18nPropertyPath implements Command<Void>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) throws CommandException
	{
		if(StringUtils.isBlank(payload)) {
			throw new CommandException("you have to provide the path of the property file directory");
		}
		
		final File propertyFileDir = new File(payload);
		PropertyFileHandling.instance().setupPropertiesReader(propertyFileDir);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "i18nPropertyFileDirectory";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return "usage: " + name() + " /path/to/directory/with/i18nfiles";
	}

}
