package de.ludwig.finx;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * A clear interface / definition of what it does mean beeing a language.
 * 
 * @author Daniel
 */
public class Language
{
	private Locale base;

	/**
	 * @param base
	 */
	public Language(Locale base)
	{
		super();
		if (base == null) {
			throw new ApplicationCodingException("locale is null, unable to create language");
		}

		if (StringUtils.isBlank(base.getLanguage())) {
			throw new ApplicationCodingException("language is not set");
		}
		this.base = base;
	}

	public String language()
	{
		return base.getLanguage();
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
		result = prime * result + ((language() == null) ? 0 : language().hashCode());
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
		Language other = (Language) obj;
		if (!language().equals(other.language()))
			return false;
		return true;
	}

}
