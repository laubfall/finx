/**
 * 
 */
package de.ludwig.finx.settings;

import java.io.File;

/**
 * @author Daniel
 *
 */
public class FileSetting extends AbstractSetting<File> {
	private File fileObject;
	
	/**
	 * 
	 */
	public FileSetting()
	{
		super();
	}

	/**
	 * @param rawValue
	 */
	public FileSetting(String rawValue) {
		super(rawValue);
	}
	
	public FileSetting(File wrapThis) {
		super(wrapThis.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public File setting() {
		return fileObject;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#initialize()
	 */
	@Override
	public void initialize(String rawValue) {
		fileObject = new File(rawValue);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if(fileObject == null)
			return "";
		
		return fileObject.getAbsolutePath();
	}

}
