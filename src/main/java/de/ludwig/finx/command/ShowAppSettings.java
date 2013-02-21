package de.ludwig.finx.command;

import org.apache.log4j.Logger;

import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * @author Daniel
 *
 */
public class ShowAppSettings implements Command<Void>
{
	private final static Logger log = Logger.getLogger(ShowAppSettings.class);
	
	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) throws CommandException
	{
//		for(AppSettingNames asn : AppSettingNames.values()) {
//			final AbstractSetting<?> set = SettingsDaoImpl.instance().loadSettings().setting(asn);
//			if(set == null)
//				continue;
//			log.info(String.format("%s : %s", asn.settingName(), set));
//		}
		
		// TODO das funktioniert nicht unbedingt, da die Settings im Static-Initializer gesetzt werden und
		// somit auch erst dann hier rauskommen können.
		for(final String settingName : SettingsDaoImpl.instance().namesOfKnownSettings()) {
			log.info(settingName);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "showAppSettings";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return "Prints all Application Settings that are actually in use";
	}

}
