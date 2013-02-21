/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.settings;

import java.io.File;

import junit.framework.Assert;
import org.junit.Test;

import de.ludwig.finx.io.PropertiesWriter;
import de.ludwig.finx.settings.AppSettings;
import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * 
 * @author Daniel
 */
public class SettingsDaoImplTest
{
	@Test
	public void testLoad()
	{
		Assert.assertNotNull(AppSettings.i18nPropFilePostFix);

		Assert.assertNotNull(AppSettings.i18nPropFilePreFix);
	}

	@Test
	public void testSave()
	{

	}

	/**
	 * Tests if the setting is marked dirty after changing it
	 */
	@Test
	public void inject()
	{
		Assert.assertNotNull(PropertiesWriter.preservePropertyLayout);
		Assert.assertFalse(PropertiesWriter.preservePropertyLayout.isDirty());

		Assert.assertNotNull(PropertiesWriter.preservePropertyLayout.setting());
		Assert.assertFalse(PropertiesWriter.preservePropertyLayout.isDirty());

		PropertiesWriter.preservePropertyLayout.change("NONE");
		Assert.assertTrue(PropertiesWriter.preservePropertyLayout.isDirty());
	}

	@Test
	public void changeSetting()
	{
		String newPath = String.format("%1$snew%1$spath%1$sfor%1$stest", File.separator);

		Assert.assertNotNull(AppSettings.i18nPropertiesLocation);
		File setting = AppSettings.i18nPropertiesLocation.setting();
		Assert.assertNotNull(setting);
		SettingsDaoImpl.instance().changeSetting("i18nPropertiesLocation", newPath);
		File newSetting = AppSettings.i18nPropertiesLocation.setting();

		Assert.assertNotNull(newSetting);
		Assert.assertEquals(newPath, newSetting.getPath());
		Assert.assertFalse(setting.getAbsolutePath().equals(newSetting.getAbsolutePath()));
	}
}
