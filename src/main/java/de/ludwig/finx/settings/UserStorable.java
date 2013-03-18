package de.ludwig.finx.settings;

/**
 * Should only be implemented by classes that implements {@link Setting} in any way and wanted to
 * show that their setting {@link Setting#setting()} is storable in a user specific settings-file.
 * 
 * @author Daniel
 * 
 */
public interface UserStorable
{
	/**
	 * Method that serializes the Settings-Value into a form where it can be written to a
	 * settings-file vice versa.
	 * 
	 * @return the serialized Settings-Value.
	 */
	public String storeValue();
}
