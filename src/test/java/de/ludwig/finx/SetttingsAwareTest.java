package de.ludwig.finx;

import org.junit.After;

import de.ludwig.finx.io.PropertiesWriter;
import de.ludwig.finx.io.PropertyFile;
import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * Extend this class if you want to make sure that Settings are gonna be resetted to default values.
 * 
 * @author Daniel
 * 
 */
public abstract class SetttingsAwareTest
{
	@After
	public void resetSettings()
	{
		SettingsDaoImpl.instance().init(PropertiesWriter.class);
		SettingsDaoImpl.instance().init(PropertyFile.class);
	}
}
