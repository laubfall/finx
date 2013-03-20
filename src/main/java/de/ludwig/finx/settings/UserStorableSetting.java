package de.ludwig.finx.settings;

/**
 * @author Daniel
 * 
 */
public interface UserStorableSetting<R> extends UpdatableSetting<R>
{
	public String storeValue();
}
