package de.ludwig.finx.io;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.settings.AbstractSetting;

/**
 * @author Daniel
 *
 */
public class PropertyPreserveModeSetting extends AbstractSetting<PropertyPreserveMode>
{
	private PropertyPreserveMode mode;
	
	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public PropertyPreserveMode setting()
	{
		if(mode == null) {
			throw new ApplicationCodingException("PropertyPreserveMode Setting is not initialized");
		}
		
		return mode;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String rawValue)
	{
		mode = PropertyPreserveMode.valueOf(rawValue);
	}

}
