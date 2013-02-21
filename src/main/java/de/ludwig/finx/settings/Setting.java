package de.ludwig.finx.settings;

/**
 * @author Daniel
 *
 */
public interface Setting<R>
{
	/**
	 * 
	 * @return the converted Object
	 */
	public abstract R setting();
	
	public boolean isDirty();
}
