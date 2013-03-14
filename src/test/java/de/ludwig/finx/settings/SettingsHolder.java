package de.ludwig.finx.settings;

import java.io.File;

/**
 * Class only used by tests in this package
 * 
 * @author Daniel
 * 
 */
class SettingsHolder
{
	public static UpdatableSetting<File> i18nPropertiesLocation;

	static {
		SettingsDaoImpl.instance().init(SettingsHolder.class);
	}

}
