package de.ludwig.finx.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public PropertyKeyOrderSetting setting()
	{
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	enum PropertyKeyOrder
	{
		ASC, DESC, NONE, ;
	}

	public static void sort(final List<I18nNode> nodesToSort, final PropertyKeyOrder keyOrder)
	{
		if (keyOrder.equals(PropertyKeyOrder.NONE))
			return;

		Collections.sort(nodesToSort, new NodeComparator(keyOrder));
	}

	static class NodeComparator implements Comparator<I18nNode>
	{
		private int sortModifier;

		public NodeComparator(final PropertyKeyOrder keyOrder)
		{
			switch (keyOrder)
			{
			case ASC:
				sortModifier = -1;
				break;
			case DESC:
				sortModifier = 1;
				break;
			case NONE:
				sortModifier = 0;
				break;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(I18nNode o1, I18nNode o2)
		{
			final String key1 = o1.key();
			final String key2 = o2.key();
			return sortModifier * key1.compareTo(key2);
		}

	}
}
