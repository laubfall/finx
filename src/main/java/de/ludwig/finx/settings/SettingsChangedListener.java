package de.ludwig.finx.settings;

/**
 * Classes / Instances that want to be informed on setting-changes have to implement this interface
 * and register themselves with {@link SettingsDaoImpl#addListener(Class, SettingsChangedListener)}
 * 
 * @author Daniel
 * 
 */
public interface SettingsChangedListener
{
	/**
	 * Called whenever a setting has changed.
	 * 
	 * @param settingName
	 *            the name of the setting-field that has changed
	 * @param oldSetting
	 *            the old setting.
	 */
	void settingChanged(String settingName, Setting<?> oldSetting);
}
