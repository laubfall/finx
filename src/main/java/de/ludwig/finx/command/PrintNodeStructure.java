package de.ludwig.finx.command;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.io.PropertiesWriter;
import de.ludwig.finx.io.PropertyFileHandling;
import de.ludwig.finx.io.RootNode;

/**
 * @author Daniel
 * 
 */
public class PrintNodeStructure implements Command<Void>
{
	private Logger log = Logger.getLogger(PrintNodeStructure.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) throws CommandException
	{
		final RootNode root = PropertyFileHandling.instance().nodeStructureFromFiles();
		
		log.info("Existing Languages: ");
		for(String language : root.getExistingLanguages()) {
			log.info(language);
		}
		
		if(StringUtils.isBlank(payload)) {
			return null;
		}
		
		if(root.getExistingLanguages().contains(payload) == false) {
			throw new CommandException(String.format("no i18n-information for language %s", payload));
		}
		
		final StringBuilder sb = new StringBuilder();
		PropertiesWriter.dumpI18nTreeStructure(root.getRootNodes(), sb, new Locale(payload));
		log.info("All I18n-Node for language: " + payload);
		log.info(sb.toString());
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "printNodeStructure";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return "This Command show the complete structure of the current node-tree for the wanted language. usage: " + name() + " [languageIso2].";
	}

}
