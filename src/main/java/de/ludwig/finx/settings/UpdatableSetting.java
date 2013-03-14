package de.ludwig.finx.settings;

/**
 * Settings of this type can be changed during runtime.
 * 
 * Use this as type for fields in your class that should store Settings! That is necessary, because
 * for every setting a proxy is created that implements this interface and wraps every call of the
 * implementing setting. After this is done, that proxy-object is assigned to the field in your
 * class.
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
