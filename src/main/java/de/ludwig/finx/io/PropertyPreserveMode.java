package de.ludwig.finx.io;

import de.ludwig.finx.settings.SettingType;

/**
 * The original I18n-Property-Files stays untouched, their formatting is not modified.
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
 * Pretty-Print-Settings
 * 
 * @author Daniel
 */
@SettingType(PropertyPreserveModeSetting.class)
public enum PropertyPreserveMode
{
	STRICT, NONSTRICT, NONE;
}
