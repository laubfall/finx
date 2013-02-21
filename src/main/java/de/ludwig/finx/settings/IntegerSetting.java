package de.ludwig.finx.settings;

/**
 * @author Daniel
 *
 */
public class IntegerSetting extends AbstractSetting<Integer>
{
	private Integer value;

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public Integer setting()
	{
		return value;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Modifieable#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String rawValue)
	{
		value = Integer.decode(rawValue);
	}

}
