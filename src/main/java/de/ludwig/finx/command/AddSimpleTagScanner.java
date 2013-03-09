/**
 * 
 */
package de.ludwig.finx.command;

import java.util.List;

import org.apache.log4j.Logger;

import de.ludwig.finx.monitor.Monitoring;
import de.ludwig.finx.scanner.SimpleTagScanner;

/**
 * Command to add a specific Scanner to the actual Monitor
 * 
 * @author Daniel
 * 
 */
public class AddSimpleTagScanner implements Command<Void>, Stackable
{
	private Logger log = Logger.getLogger(AddSimpleTagScanner.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload)
	{
		String[] tokens = payload.split(" ");

		if (tokens == null || tokens.length != 4) {
			throw new CommandException("Unable to execute AddSimpleTagScanner missing arguments");
		}

		final SimpleTagScanner simpleTagScanner = new SimpleTagScanner(tokens[1], tokens[3]);

		Monitoring.instance().addScanner(simpleTagScanner, null);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "addsimplescanner";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return "usage: starttag nameofyourstarttag endtag nameofyourendtag";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.command.Stackable#runTheseCmdsBefore()
	 */
	@Override
	public List<Command<?>> runTheseCmdsBefore()
	{
		// TODO Auto-generated method stub
		return null;
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
