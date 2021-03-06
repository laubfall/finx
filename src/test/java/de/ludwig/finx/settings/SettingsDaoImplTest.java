package de.ludwig.finx.settings;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.ludwig.finx.io.PropertyFile;

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
	public void testSave() throws IOException
	{
		final SettingsDaoImpl settingsDao = SettingsDaoImpl.instance();
		final String userDir = System.getProperty("user.dir");
		final File userSettings = new File(userDir, SettingsDaoImpl.USER_SETTINGS_FILENAME);
		Assert.assertTrue(userSettings.exists());

		AppSettings.i18nDefaultValue.change(I18nDefaultValueSetting.DefaultValueTypes.EMPTY.name());
		settingsDao.saveSettings();

		final List<String> lines = FileUtils.readLines(userSettings);
		Assert.assertNotNull(lines);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(2, lines.size());
		Assert.assertTrue(StringUtils.startsWith(lines.get(0), "#")); // the store mechanism for
																		// property-files store a
																		// date in the first line
		Assert.assertEquals("i18nDefaultValue=" + I18nDefaultValueSetting.DefaultValueTypes.EMPTY, lines.get(1));

		FileUtils.deleteQuietly(userSettings);
	}

	/**
	 * Tests if the setting is marked dirty after changing it
	 */
	@Test
	public void inject()
	{
		// because other tests can change the settings of PropertiesWriter we need to reset all
		// settings
		SettingsDaoImpl.instance().init(PropertyFile.class);

		Assert.assertNotNull(PropertyFile.preservePropertyLayout);
		Assert.assertFalse(PropertyFile.preservePropertyLayout.isDirty());

		Assert.assertNotNull(PropertyFile.preservePropertyLayout.setting());
		Assert.assertFalse(PropertyFile.preservePropertyLayout.isDirty());

		PropertyFile.preservePropertyLayout.change("NONE");
		Assert.assertTrue(PropertyFile.preservePropertyLayout.isDirty());
	}

	@Test
	public void changeSetting()
	{
		String newPath = String.format("%1$snew%1$spath%1$sfor%1$stest", File.separator);

		Assert.assertNotNull(SettingsHolder.i18nPropertiesLocation);
		File setting = SettingsHolder.i18nPropertiesLocation.setting();
		Assert.assertNotNull(setting);
		SettingsDaoImpl.instance().changeSetting("i18nPropertiesLocation", newPath);
		File newSetting = SettingsHolder.i18nPropertiesLocation.setting();

		Assert.assertNotNull(newSetting);
		Assert.assertEquals(newPath, newSetting.getPath());
		Assert.assertFalse(setting.getAbsolutePath().equals(newSetting.getAbsolutePath()));
	}

	@Test
	public void changeSettingWithListener()
	{
		Assert.assertNotNull(SettingsHolder.i18nPropertiesLocation.setting());

		InterestedInChangesHolder iich = new InterestedInChangesHolder();
		Assert.assertEquals(0, iich.getSettingChangedCnt().intValue());

		SettingsHolder.i18nPropertiesLocation.change("test");
		Assert.assertEquals(1, iich.getSettingChangedCnt().intValue());
		Assert.assertNotNull(iich.getSettingName());
		Assert.assertEquals("i18nPropertiesLocation", iich.getSettingName());
		Assert.assertNotNull(iich.getOldSetting());

		SettingsHolder.i18nPropertiesLocation.change("test2");
		Assert.assertEquals(2, iich.getSettingChangedCnt().intValue());

		SettingsHolder.i18nPropertiesLocation.setting();
		// no change, no call
		Assert.assertEquals(2, iich.getSettingChangedCnt().intValue());
	}
}
