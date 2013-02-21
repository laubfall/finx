package de.ludwig.finx.settings;

/**
 * @author Daniel
 * 
 */
@Deprecated
public enum AppSettingNames
{
	/**
	 * Um erkennen zu können um welche Sprache es sich bei der
	 * I18n-Properties-Datei handelt müssen wir wissen wie der Bereich vor und
	 * nach der Locale-Information im Dateinamen ausschaut. Beispiel
	 * MyI18n_de_DE.properties. Dann wäre MyI18n_ das Postfix
	 */
	APP_SETTING_I18N_FILE_POSTFIXS("i18nPropFilePostFix"),
	/**
	 * Um erkennen zu können um welche Sprache es sich bei der
	 * I18n-Properties-Datei handelt müssen wir wissen wie der Bereich vor und
	 * nach der Locale-Information im Dateinamen ausschaut. Beispiel
	 * MyI18n_de_DE.properties. Dann wäre .properties das Prefix.
	 */
	APP_SETTING_I18N_FILE_PREFIXS("i18nPropFilePreFix"),
	/**
	 * Verzeichnis in dem die Internationalisierungs-Dateien liegen.
	 */
	APP_SETTING_I18N_PROPERTIES_LOCATIONS("i18nPropertiesLocation"),
	/**
	 * Absolute path to the properties file we are actually referencing.
	 */
	APP_SETTING_SETTINGS_STORED_LOCATION("settingsStoredAt"),

	APP_SETTING_OWNSETTINGS_LOCATION("ownsettings"),

	APP_SETTING_I18N_DEFAULT_VALUE("i18nDefaultValue"),
	/**
	 * Comma seperate List of File-Extensions. Used to define which Files need
	 * to observed. Files that have an extension that is not defined in this
	 * list are ignored.
	 */
	APP_SETTING_RELEVANT_FILE_EXTENSIONS("fileExtensions"),

	// after this point the settings for prettyprinting I18n-Property-Files
	/**
	 * Defines the level of grouping of I18nNodes in the Property-File. 0 means
	 * no grouping, all I18nNodes are printed after each other. 1 means all
	 * I18nNodes with equal keyparts are grouped (that means followed by an
	 * empty line) and so on
	 */
	PRETTY_PRINTING_GROUP("keyGrouping"),

	/**
	 * Number of empty lines between Groups of I18nNodes. No effect if
	 * {@link #PRETTY_PRINTING_GROUP} is 0
	 */
	PRETTY_PRINTING_GROUP_SPACE("keyGroupSpace"),

	/**
	 * The original I18n-Property-Files stays untouched, their formatting is not
	 * modified.
	 * 
	 * Possible values: STRICT, NONSTRICT, NONE
	 * 
	 * nonstrict: The application adds new keys to the point where their natural
	 * ordering matches most (for example existing key: de.ludwig.test new Key
	 * de.ludwig.tesz, in this case the new key is added after the existing
	 * one). Other Pretty-Printing Settings have no effect.
	 * 
	 * strict: new keys are added to the end of the file. Other Pretty-Printing
	 * Settings have no effect.
	 * 
	 * none: if the application writes the I18n-Property-Files Keys are sorted
	 * as specified by the given Pretty-Print-Settings
	 */
	PRETTY_PRINTING_PRESERVE("preservePropertyLayout"),

	/**
	 * defines how keys are sorted. If value of this setting is none keys are
	 * added to the file as added to the I18n-Structure.
	 * 
	 * possible values: ASC, DESC, NONE
	 * 
	 */
	PRETTY_PRINTING_ORDER("keyOrder"), ;

	private String settingName;

	private Class<? extends AbstractSetting<?>> settingType;

	private AppSettingNames(String settingName)
	{
		this.settingName = settingName;
		settingType = StringSetting.class;
	}

	private AppSettingNames(String settingName, Class<? extends AbstractSetting<?>> settingType)
	{
		this(settingName);
		this.settingType = settingType;
	}

	public static AppSettingNames bySettingName(final String settingName)
	{
		for (AppSettingNames asn : values())
		{
			if (asn.settingName.equals(settingName))
				return asn;
		}
		return null;
	}

	public String settingName()
	{
		return settingName;
	}

	public Class<? extends AbstractSetting<?>> settingType()
	{
		return settingType;
	}
}
