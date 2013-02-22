/**
 * 
 */
package de.ludwig.finx.settings;

/**
 * A setting is the programmatic representation of a string value stored in a properties file In
 * most simple case a setting is not more than a String.
 * 
 * Implementations of this class are responsible to convert the stored string value. For example
 * convert a String that represents a Filesystem-Path to a file Object.
 * 
 * @author Daniel
 * 
 */
public abstract class AbstractSetting<SETTINGVALUETYPE> implements UpdatableSetting<SETTINGVALUETYPE>, Modifieable
{

	/**
	 * only dirty setting (settings with changes will be saved)
	 */
	private boolean dirty = false;

	// protected String rawValue;

	public AbstractSetting()
	{

	}

	public AbstractSetting(final String rawValue)
	{
		// this.rawValue = rawValue;
		initializeInternal(rawValue);
	}

	private final void initializeInternal(final String rawValue)
	{
		dirty = true;
		initialize(rawValue);
	}

	public final void change(String newValue)
	{
		initialize(newValue);
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty()
	{
		return dirty;
	}

	AbstractSetting<SETTINGVALUETYPE> setDirty(boolean dirty)
	{
		this.dirty = dirty;
		return this;
	}
}
