package de.ludwig.finx.command;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * Command to change existing Settings, see also {@link SettingsDao}
 * 
 * @author Daniel
 * 
 */
public class ChangeSettingCommand implements Command<Void>
{
	private static final Logger log = Logger.getLogger(ChangeSettingCommand.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#execute(java.lang.String)
	 */
	public Void execute(String payload) throws CommandException
	{
		// mySetting [PAYLOAD]
		final int indexOf = payload.indexOf(' ');
		final String settingName = StringUtils.substring(payload, 0, indexOf).trim();
		final String settingRawValue = StringUtils.substring(payload, indexOf).trim();

		SettingsDaoImpl.instance().changeSetting(settingName, settingRawValue);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help()
	{
		return "Change an Application Setting, usage: appSettingName appSettingValue";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name()
	{
		return "changeAppSetting";
	}

}
