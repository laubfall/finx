package de.ludwig.finx.io;

import de.ludwig.finx.settings.SettingType;

/**
 * Defines how finx interact with existing Property-Files and how much finx modifies these files in
 * a way defined by the various pretty-print-settings.
 * 
 * Possible values: STRICT, NONSTRICT, NONE
 * 
 * nonstrict: The application adds new keys to the point where their natural ordering matches most
 * (for example existing key: de.ludwig.test new Key de.ludwig.tesz, in this case the new key is
 * added after the existing one). Other Pretty-Printing Settings have no effect.
 * 
 * strict: new keys are added to the end of the file. Other Pretty-Printing Settings have no effect.
 * 
 * none: if the application writes the I18n-Property-Files Keys are sorted as specified by the given
 * Pretty-Print-Settings, that means Finx fully manages the layouting of the properties file.
 * 
 * @author Daniel
 */
@SettingType(PropertyPreserveModeSetting.class)
public enum PropertyPreserveMode
{
	STRICT, NONSTRICT, NONE;
}
