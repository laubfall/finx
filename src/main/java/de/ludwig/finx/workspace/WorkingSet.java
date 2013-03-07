package de.ludwig.finx.workspace;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.io.PropertiesReader;
import de.ludwig.finx.settings.AppSettings;

/**
 * ISSUE: GIT-3
 * 
 * This class represents the relation between File that contains I18n-Keys, and the
 * Properties-Files. It defines a set of files that servers I18n-Keys. In other words these are file
 * which shall be monitored.
 * 
 * @author Daniel
 * 
 */
public class WorkingSet implements Serializable
{
	private static final long serialVersionUID = -7931008810035637617L;

	private File propertiesDir;

	/**
	 * the propertiesReader that knows about the related I18n-Properties-Files
	 */
	private transient PropertiesReader pr;

	/**
	 * A workingSet can have different Settings regarding Property-Files. The Pre- and Postfix for
	 * example. Keep in mind, that actually these values are defined during construction time. If
	 * the AppSettings changes these values will stay untouched!
	 */
	private String i18nPropertiesFilePrefix;

	/**
	 * see {@link #i18nPropertiesFilePrefix}
	 */
	private String i18nPropertiesFilePostfix;

	WorkingSet(final File propertiesDir)
	{
		{
			this.propertiesDir = propertiesDir; // needs to be called first
			changePropertiesReader(AppSettings.i18nPropFilePostFix.setting(), AppSettings.i18nPropFilePreFix.setting());
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException
	{
		try {
			ois.defaultReadObject();
			pr = new PropertiesReader(propertiesDir, i18nPropertiesFilePostfix, i18nPropertiesFilePrefix);
		} catch (ClassNotFoundException e) {
			throw new ApplicationCodingException("unable to read workingSet Object from inputstream");
		}
	}

	public void changePropertiesReader(final String i18nPropertiesFilePostFix, final String i18nPropertiesFilePreFix)
	{
		this.i18nPropertiesFilePrefix = i18nPropertiesFilePreFix;
		this.i18nPropertiesFilePostfix = i18nPropertiesFilePostFix;
		pr = new PropertiesReader(propertiesDir, i18nPropertiesFilePostfix, i18nPropertiesFilePrefix);
	}

	/**
	 * @return the pr
	 */
	@Transient
	public PropertiesReader getPr()
	{
		return pr;
	}

	/**
	 * @return the propertiesDir
	 */
	public File getPropertiesDir()
	{
		return propertiesDir;
	}

}
