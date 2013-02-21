/**
 * 
 */
package de.ludwig.finx.settings;

/**
 * @author Daniel
 *
 */
public class StringSetting extends AbstractSetting<String> {
	private String value;
	
	public StringSetting() {
		
	}
	
	/**
	 * @param rawValue
	 */
	public StringSetting(String rawValue) {
		super(rawValue);
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public String setting() {
		return value;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#initialize()
	 */
	@Override
	public void initialize(String rawValue) {
		value = rawValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return value;
	}
}
