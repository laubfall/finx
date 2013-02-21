/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.scanner;

import java.io.File;

/**
 * programmatic representation of an i18n-key that resides in a textfile
 * 
 * @author Daniel
 */
public class I18nKey
{
	private String i18nKey;

	/**
	 * Die Datei in der der I18n-Key gefunden wurde.
	 * 
	 * Nie null.
	 */
	private File source;

	private I18nKeyControll keyControll;

	public I18nKey(String i18nKey, File source)
	{
		this.i18nKey = i18nKey;
		this.source = source;
	}

	public I18nKey(String i18nKey, File source, I18nKeyControll keyControll)
	{
		this(i18nKey, source);
		this.keyControll = keyControll;
	}

	public String getI18nKey()
	{
		return i18nKey;
	}

	public I18nKeyControll getKeyControll()
	{
		return keyControll;
	}

	public File getSource()
	{
		return source;
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
		result = prime * result + ((i18nKey == null) ? 0 : i18nKey.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		I18nKey other = (I18nKey) obj;
		if (i18nKey == null)
		{
			if (other.i18nKey != null)
				return false;
		} else if (!i18nKey.equals(other.i18nKey))
			return false;
		if (source == null)
		{
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("I18nKey [i18nKey=").append(i18nKey).append(", source=").append(source).append("]");
		return builder.toString();
	}
}
