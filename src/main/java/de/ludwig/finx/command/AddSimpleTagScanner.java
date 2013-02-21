/**
 * 
 */
package de.ludwig.finx.command;

import org.apache.log4j.Logger;

import de.ludwig.finx.monitor.Monitoring;
import de.ludwig.finx.scanner.SimpleTagScanner;

/**
 * Command to add a specific Scanner to the actual Monitor
 * 
 * @author Daniel
 *
 */
public class AddSimpleTagScanner implements Command<Void> {
	private Logger log = Logger.getLogger(AddSimpleTagScanner.class);
	
	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) {
		String[] tokens = payload.split(" ");
		
		if(tokens == null || tokens.length != 4) {
			throw new CommandException("Unable to execute AddSimpleTagScanner missing arguments");
		}
		
		final SimpleTagScanner simpleTagScanner = new SimpleTagScanner(tokens[1], tokens[3]);
		Monitoring.instance().addScanner(simpleTagScanner);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name() {
		return "addsimplescanner";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help() {
		return "usage: starttag nameofyourstarttag endtag nameofyourendtag";
	}

}
