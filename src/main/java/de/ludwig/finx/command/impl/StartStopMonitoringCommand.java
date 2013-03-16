package de.ludwig.finx.command.impl;

import org.apache.commons.lang3.StringUtils;

import de.ludwig.finx.command.Command;
import de.ludwig.finx.command.CommandException;
import de.ludwig.finx.monitor.Monitoring;

/**
 * @author Daniel
 *
 */
public class StartStopMonitoringCommand implements Command<Void> {
	public static final String START = "start";
	
	public static final String STOP = "stop";
	
	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) throws CommandException {
		if(StringUtils.isBlank(payload)) {
			throw new CommandException("no action for monitoring defined");
		}
		
		if(payload.equals(START)) {
			Monitoring.instance().startMonitoring();
			return null;
		} else if(payload.equals(STOP)) {
			Monitoring.instance().stopMonitoring();
			return null;
		}
		
		throw new CommandException("unknown action " + payload + " for monitoring");
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name() {
		return "monitoring";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help() {
		return "usage: " + name() + " start or stop";
	}

}
