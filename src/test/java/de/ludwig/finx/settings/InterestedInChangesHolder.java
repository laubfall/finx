package de.ludwig.finx.settings;

/**
 * This class is only interessting for JUnit-Tests
 * 
 * @author Daniel
 * 
 */
class InterestedInChangesHolder implements SettingsChangedListener
{
	private Integer settingChangedCnt = 0;

	private String settingName;

	private Setting<?> oldSetting;

	/**
	 * 
	 */
	public InterestedInChangesHolder()
	{
		SettingsDaoImpl.instance().addListener(SettingsHolder.class, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.settings.SettingsChangedListener#settingChanged(java.lang.String,
	 * de.ludwig.finx.settings.UpdatableSetting)
	 */
	@Override
	public void settingChanged(String settingName, Setting<?> oldSetting)
	{
		settingChangedCnt++;
		this.settingName = settingName;
		this.oldSetting = oldSetting;
	}

	/**
	 * @return the settingChangedCnt
	 */
	public Integer getSettingChangedCnt()
	{
		return settingChangedCnt;
	}

	/**
	 * @return the settingName
	 */
	public String getSettingName()
	{
		return settingName;
	}

	/**
	 * @return the oldSetting
	 */
	public Setting<?> getOldSetting()
	{
		return oldSetting;
	}
}
