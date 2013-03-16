package de.ludwig.finx.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.Language;
import de.ludwig.finx.command.Command;
import de.ludwig.finx.command.CommandException;
import de.ludwig.finx.command.Stackable;
import de.ludwig.finx.io.PropertiesWriter;
import de.ludwig.finx.io.PropertyFileHandling;
import de.ludwig.finx.io.RootNode;

/**
 * @author Daniel
 * 
 */
public class PrintNodeStructure implements Command<Void>, Stackable
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
		for (Language language : root.getExistingLanguages()) {
			log.info(language.language());
		}

		if (StringUtils.isBlank(payload)) {
			return null;
		}

		if (root.getExistingLanguages().contains(payload) == false) {
			throw new CommandException(String.format("no i18n-information for language %s", payload));
		}

		final StringBuilder sb = new StringBuilder();
		PropertiesWriter.dumpI18nTreeStructure(root.getRootNodes(), sb, new Language(payload));
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
		return "This Command show the complete structure of the current node-tree for the wanted language. usage: "
				+ name() + " [languageIso2].";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.command.Stackable#runTheseCmdsBefore()
	 */
	@Override
	public List<Command<?>> runTheseCmdsBefore()
	{
		final List<Command<?>> coms = new ArrayList<>();
		// coms.
		return coms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.command.Stackable#beforeExecute(java.util.List)
	 */
	@Override
	public void beforeExecute(List<Object> payload)
	{
		// TODO Auto-generated method stub

	}

}
