package de.ludwig.finx.io;

import java.io.File;

import org.junit.After;

/**
 * @author Daniel
 * 
 */
public abstract class BasePropertyFileTest
{
	/**
	 * subclasses can use this file object to create a properties-file for test purposes, without to
	 * delete this created file by themselves.
	 * 
	 * Avoid to assign a new file object more than once during one test-method, because only the
	 * last created file-object can be deleted.
	 */
	File propertyFileHandle = null;

	@After
	public void deleteFile()
	{
		if (propertyFileHandle == null)
			return;

		boolean delete = propertyFileHandle.delete();
		if (delete == false)
			throw new RuntimeException("unable to delete tmp-testfile");
	}
}
