package de.ludwig.finx.io;

import de.ludwig.finx.settings.SettingType;

/**
 * @author Daniel
 *
 */
@SettingType(PropertyPreserveModeSetting.class)
public enum PropertyPreserveMode
{
	STRICT,
	NONSTRICT,
	NONE
	;
}
