package de.ludwig.finx;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * A clear interface / definition of what it does mean beeing a language.
 * 
 * @author Daniel
 */
public class Language
{
	private Locale base;

	private static final Map<String, Language> CACHE = new HashMap<>();

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

	/**
	 * Shortcut Constructor.
	 * 
	 * @param iso2
	 *            Not optional. normally you provide an iso2 code here, but in theory every value is
	 *            possible
	 */
	public Language(String iso2)
	{
		// we dont need any toLowerCaseOperation at this point. If you call getLanguage of Locale
		// you get a lower-case value
		this(new Locale(iso2));
	}

	public static final Language language(final String languageIso2)
	{
		final String key = languageIso2.toLowerCase();

		if (CACHE.containsKey(key)) {
			return CACHE.get(key);
		}
		Language language = new Language(languageIso2);
		CACHE.put(key, language);
		return language;
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
