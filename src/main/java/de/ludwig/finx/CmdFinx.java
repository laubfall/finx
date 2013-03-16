package de.ludwig.finx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.ludwig.finx.command.CommandException;
import de.ludwig.finx.command.CommandHub;
import de.ludwig.finx.command.impl.AddI18nPropertyPath;
import de.ludwig.finx.command.impl.AddSimpleTagScanner;
import de.ludwig.finx.command.impl.AddSrcPath;
import de.ludwig.finx.command.impl.ChangeSettingCommand;
import de.ludwig.finx.command.impl.CmdPrintLatestChanges;
import de.ludwig.finx.command.impl.CmdShutdown;
import de.ludwig.finx.command.impl.PrintNodeStructure;
import de.ludwig.finx.command.impl.ReloadNodeStructure;
import de.ludwig.finx.command.impl.ShowAppSettings;
import de.ludwig.finx.command.impl.ShowKnownCommands;
import de.ludwig.finx.command.impl.StartStopMonitoringCommand;

/**
 * Commandline-Anwendung für Finx
 * 
 * @author Daniel
 */
public class CmdFinx
{

	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CmdFinx.class);

	public static final String OPT_I18NPATH = "i18nPath";

	public CmdFinx(CommandLine cmd)
	{
		CommandHub.addCommand(new CmdPrintLatestChanges());
		CommandHub.addCommand(new CmdShutdown());
		CommandHub.addCommand(new ShowKnownCommands());
		CommandHub.addCommand(new AddSrcPath());
		CommandHub.addCommand(new AddSimpleTagScanner());
		CommandHub.addCommand(new StartStopMonitoringCommand());
		CommandHub.addCommand(new AddI18nPropertyPath());
		CommandHub.addCommand(new ShowAppSettings());
		CommandHub.addCommand(new ChangeSettingCommand());
		CommandHub.addCommand(new PrintNodeStructure());
		CommandHub.addCommand(new ReloadNodeStructure());
	}

	public static void main(String[] arg)
	{
		log.info("Starting FINX Commandline Client");
		log.info("Type " + new ShowKnownCommands().name() + " to show all available commands");
		log.info("type [command] -? to get help information about the choosen command");

		Options opts = new Options();
		opts.addOption(OPT_I18NPATH, true, "The absolute path to the directory where your I18n-Propertie-Files are");

		CommandLineParser parser = new GnuParser();
		try {
			CommandLine result = parser.parse(opts, arg);
			CmdFinx finx = new CmdFinx(result);
			finx.run();
		} catch (ParseException ex) {
			log.error("Unable to parse Commandline Argument", ex);
		}
	}

	public void run()
	{
		while (true) {
			try {
				final BufferedReader cmdInput = new BufferedReader(new InputStreamReader(System.in));
				String input = cmdInput.readLine();
				commandDispatch(input);
			} catch (IOException e) {
				log.error("IOException during Command Dispatch", e);
			} catch (CommandException e) {
				log.warn(e.getMessage());
			} catch (ApplicationCodingException e) {
				log.error("an application error occured. please try again with different settings", e);
			}
		}
	}

	private void commandDispatch(final String cmd)
	{
		log.debug("Dispatching command " + cmd);
		CommandHub.execute(cmd);
	}
}
