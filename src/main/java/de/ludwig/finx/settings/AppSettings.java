/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.settings;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The programmatic view on settings.i18n Properties File
 * 
 * @author Daniel
 */
public class AppSettings
{
	/**
	 * Um erkennen zu können um welche Sprache es sich bei der
	 * I18n-Properties-Datei handelt müssen wir wissen wie der Bereich vor und
	 * nach der Locale-Information im Dateinamen ausschaut. Beispiel
	 * MyI18n_de_DE.properties. Dann wäre MyI18n_ das Postfix
	 */
	public static UpdatableSetting<String> i18nPropFilePostFix;
	
	/**
	 * Um erkennen zu können um welche Sprache es sich bei der
	 * I18n-Properties-Datei handelt müssen wir wissen wie der Bereich vor und
	 * nach der Locale-Information im Dateinamen ausschaut. Beispiel
	 * MyI18n_de_DE.properties. Dann wäre .properties das Prefix.
	 */
	public static UpdatableSetting<String> i18nPropFilePreFix;
	
	/**
	 * Verzeichnis in dem die Internationalisierungs-Dateien liegen.
	 */
	public static UpdatableSetting<File> i18nPropertiesLocation;
	
	public static UpdatableSetting<I18nDefaultValueSetting> i18nDefaultValue;
	
	/**
	 * Comma seperate List of File-Extensions. Used to define which Files need
	 * to observed. Files that have an extension that is not defined in this
	 * list are ignored.
	 */
	public static UpdatableSetting<ListSetting> fileExtensions;
	
	static {
		SettingsDaoImpl.instance().init(AppSettings.class);
	}
	
	/**
	 * key: name of setting value: well, guess what ;-)
	 */
	private Map<AppSettingNames, AbstractSetting<?>> settings = new HashMap<AppSettingNames, AbstractSetting<?>>();

	/**
	 * package protected to ensure that no other programm part then the setting
	 * components add new settings. editing of settings can be done by another
	 * method, so we are able to monitor any changes.
	 * 
	 * @param key
	 * @param setting
	 */
	void addNewSetting(AppSettingNames key, AbstractSetting<?> setting)
	{
		setting.setDirty(false);
		settings.put(key, setting);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public <SETTINGTYPE extends AbstractSetting<?>> SETTINGTYPE setting(final AppSettingNames settingKey)
	{
		return (SETTINGTYPE) settings.get(settingKey);
	}
}
