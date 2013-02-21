package de.ludwig.finx.io;

import de.ludwig.finx.settings.AbstractSetting;
import de.ludwig.finx.settings.SettingType;

/**
 * @author Daniel
 *
 */
@SettingType(PropertyKeyOrderSetting.class)
public class PropertyKeyOrderSetting extends AbstractSetting<PropertyKeyOrderSetting>
{
	private PropertyKeyOrder keyOrder;

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public PropertyKeyOrderSetting setting()
	{
		return this;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Modifieable#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String rawValue)
	{
		keyOrder = PropertyKeyOrder.valueOf(rawValue);
	}

	/**
	 * @return the keyOrder
	 */
	public PropertyKeyOrder getKeyOrder()
	{
		return keyOrder;
	}

	enum PropertyKeyOrder {
		ASC,
		DESC,
		NONE,
		;
	}
}
