package de.ludwig.finx.settings;

/**
 * Settings of this type can be changed during runtime.
 * 
 * @author Daniel
 * 
 */
public interface UpdatableSetting<R> extends Setting<R>
{
	/**
	 * Parses the given parameter and updates the property;
	 * 
	 * @param newValue
	 */
	public void change(String newValue);
}
