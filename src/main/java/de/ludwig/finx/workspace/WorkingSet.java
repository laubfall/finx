package de.ludwig.finx.workspace;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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
	// private transient PropertiesReader pr;

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

	private File[] sourceDirs;

	public WorkingSet(final File propertiesDir, File... sourceDirs)
	{
		{
			this.sourceDirs = sourceDirs;
			this.propertiesDir = propertiesDir; // needs to be called first
			changePropertiesReader(AppSettings.i18nPropFilePostFix.setting(), AppSettings.i18nPropFilePreFix.setting());
		}
	}

	// private void readObject(ObjectInputStream ois) throws IOException
	// {
	// try {
	// ois.defaultReadObject();
	// pr = new PropertiesReader(propertiesDir, i18nPropertiesFilePostfix,
	// i18nPropertiesFilePrefix);
	// } catch (ClassNotFoundException e) {
	// throw new ApplicationCodingException("unable to read workingSet Object from inputstream");
	// }
	// }

	public void changePropertiesReader(final String i18nPropertiesFilePostFix, final String i18nPropertiesFilePreFix)
	{
		this.i18nPropertiesFilePrefix = i18nPropertiesFilePreFix;
		this.i18nPropertiesFilePostfix = i18nPropertiesFilePostFix;
		// pr = new PropertiesReader(propertiesDir, i18nPropertiesFilePostfix,
		// i18nPropertiesFilePrefix);
	}

	/**
	 * @return the pr
	 */
	// @Transient
	// public PropertiesReader getPr()
	// {
	// return pr;
	// }

	/**
	 * @return the propertiesDir
	 */
	public File getPropertiesDir()
	{
		return propertiesDir;
	}

	/**
	 * @return the i18nPropertiesFilePrefix
	 */
	public String getI18nPropertiesFilePrefix()
	{
		return i18nPropertiesFilePrefix;
	}

	/**
	 * @param i18nPropertiesFilePrefix
	 *            the i18nPropertiesFilePrefix to set
	 */
	public void setI18nPropertiesFilePrefix(String i18nPropertiesFilePrefix)
	{
		this.i18nPropertiesFilePrefix = i18nPropertiesFilePrefix;
	}

	/**
	 * @return the i18nPropertiesFilePostfix
	 */
	public String getI18nPropertiesFilePostfix()
	{
		return i18nPropertiesFilePostfix;
	}

	/**
	 * @param i18nPropertiesFilePostfix
	 *            the i18nPropertiesFilePostfix to set
	 */
	public void setI18nPropertiesFilePostfix(String i18nPropertiesFilePostfix)
	{
		this.i18nPropertiesFilePostfix = i18nPropertiesFilePostfix;
	}

	public List<File> getSourceDirsAsList()
	{
		return Arrays.asList(getSourceDirs());
	}

	/**
	 * @return the sourceDirs
	 */
	public File[] getSourceDirs()
	{
		return sourceDirs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((i18nPropertiesFilePostfix == null) ? 0 : i18nPropertiesFilePostfix.hashCode());
		result = prime * result + ((i18nPropertiesFilePrefix == null) ? 0 : i18nPropertiesFilePrefix.hashCode());
		result = prime * result + ((propertiesDir == null) ? 0 : propertiesDir.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkingSet other = (WorkingSet) obj;
		if (i18nPropertiesFilePostfix == null) {
			if (other.i18nPropertiesFilePostfix != null)
				return false;
		} else if (!i18nPropertiesFilePostfix.equals(other.i18nPropertiesFilePostfix))
			return false;
		if (i18nPropertiesFilePrefix == null) {
			if (other.i18nPropertiesFilePrefix != null)
				return false;
		} else if (!i18nPropertiesFilePrefix.equals(other.i18nPropertiesFilePrefix))
			return false;
		if (propertiesDir == null) {
			if (other.propertiesDir != null)
				return false;
		} else if (!propertiesDir.equals(other.propertiesDir))
			return false;
		return true;
	}

}
