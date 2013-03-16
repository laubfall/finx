package de.ludwig.finx.io;

import de.ludwig.finx.ApplicationException;
import de.ludwig.finx.settings.AbstractSetting;
import de.ludwig.finx.settings.SettingType;

/**
 * @author Daniel
 * 
 */
public class BlockOrderSetting extends AbstractSetting<BlockOrder>
{
	private BlockOrder bo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.settings.Setting#setting()
	 */
	@Override
	public BlockOrder setting()
	{
		return bo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.settings.Modifieable#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String rawValue)
	{
		try {
			bo = BlockOrder.valueOf(rawValue);
		} catch (Exception e) {
			throw new ApplicationException("Unable to create BlockOrderSetting from rawvalue: " + rawValue);
		}
	}

}

@SettingType(BlockOrderSetting.class)
enum BlockOrder
{
	GROUPING_KEY_LENGTH_ASC, //
	GROUPING_KEY_LENGTH_DESC, //
	GROUPING_KEY_AND_LENGTH_ASC, //
	GROUPING_KEY_AND_LENGTH_DESC, //
	NONE, //
	;
}
