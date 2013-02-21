package de.ludwig.finx.settings;

/**
 * @author Daniel
 *
 */
public interface UpdatableSetting<R> extends Setting<R>
{
	public void change(String newValue);
}
