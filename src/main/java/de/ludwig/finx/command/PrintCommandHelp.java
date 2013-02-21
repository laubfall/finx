/**
 * 
 */
package de.ludwig.finx.command;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

/**
 * @author Daniel
 * 
 */
public class PrintCommandHelp implements Command<Void> {
	private static Logger log = Logger.getLogger(PrintCommandHelp.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(final String payload) {
		Command<?> find = (Command<?>) CollectionUtils.find(
				CommandHub.getKnownCommands(), new Predicate() {
					public boolean evaluate(Object object) {
						final Command<?> c = (Command<?>) object;
						return c.name().equals(payload);
					}
				});

		if (find == null) {
			log.info("No Command found with name: " + payload);
			return null;
		}

		log.info("Help: " + find.help());
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name() {
		return "printcommandhelp";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help() {
		return "";
	}
}
