package de.ludwig.finx.settings;

import java.io.File;

/**
 * The programmatic view on settings.i18n Properties File
 * 
 * @author Daniel
 */
public abstract class AppSettings
{
	/**
	 * Um erkennen zu können um welche Sprache es sich bei der I18n-Properties-Datei handelt müssen
	 * wir wissen wie der Bereich vor und nach der Locale-Information im Dateinamen ausschaut.
	 * Beispiel MyI18n_de_DE.properties. Dann wäre MyI18n_ das Postfix
	 */
	public static UpdatableSetting<String> i18nPropFilePostFix;

	/**
	 * Um erkennen zu können um welche Sprache es sich bei der I18n-Properties-Datei handelt müssen
	 * wir wissen wie der Bereich vor und nach der Locale-Information im Dateinamen ausschaut.
	 * Beispiel MyI18n_de_DE.properties. Dann wäre .properties das Prefix.
	 */
	public static UpdatableSetting<String> i18nPropFilePreFix;

	/**
	 * 
	 * Verzeichnis in dem die Internationalisierungs-Dateien liegen.
	 */
	@Deprecated
	// in future this setting is stored in a workingSet
	public static UpdatableSetting<File> i18nPropertiesLocation;

	public static UpdatableSetting<I18nDefaultValueSetting> i18nDefaultValue;

	/**
	 * Comma seperate List of File-Extensions. Used to define which Files need to observed. Files
	 * that have an extension that is not defined in this list are ignored.
	 */
	public static UpdatableSetting<ListSetting> fileExtensions;

	public static UpdatableSetting<File> projectSaveDir;

	static {
		SettingsDaoImpl.instance().init(AppSettings.class);
	}
}
