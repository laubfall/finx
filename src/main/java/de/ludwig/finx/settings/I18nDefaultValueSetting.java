package de.ludwig.finx.settings;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrTokenizer;

import de.ludwig.finx.Language;
import de.ludwig.finx.io.RootNode;

/**
 * Setting for Default-Value of i18n Keys that are new.
 * 
 * @author Daniel
 * 
 */
@SettingType(I18nDefaultValueSetting.class)
public final class I18nDefaultValueSetting extends AbstractSetting<I18nDefaultValueSetting>
{
	private DefaultValueTypes type;

	private String userDefinedText = null;

	/**
	 * 
	 */
	public I18nDefaultValueSetting()
	{
		super();
	}

	/**
	 * @param rawValue
	 */
	public I18nDefaultValueSetting(String rawValue)
	{
		super(rawValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public I18nDefaultValueSetting setting()
	{
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.i18n.settings.Setting#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String rawValue)
	{
		String type = rawValue;
		if (rawValue.contains(":")) {
			final StrTokenizer tokenizer = StrTokenizer.getCSVInstance(rawValue).setDelimiterChar(':');
			String[] tokenArray = tokenizer.getTokenArray();
			type = tokenArray[0];
			userDefinedText = tokenArray[1];
		}

		this.type = DefaultValueTypes.valueOf(type);
	}

	public final void updateValue(RootNode node, String key)
	{
		switch (type)
		{
		case EMPTY:
			node.updateAll("", key);
			break;
		case ISO2KEY:
			Map<String, String> value2 = value(node, key);
			for (String iso2 : node.getExistingLanguages()) {
				node.update(value2.get(iso2), key, Language.language(iso2));
			}
			break;
		case KEY:
			node.updateAll(key, key);
			break;
		case TEXT:
			node.updateAll(userDefinedText, key);
			break;
		case ISO2TEXT:
			final Map<String, String> value = value(node, key);
			for (String iso2 : node.getExistingLanguages()) {
				node.update(value.get(iso2), key, Language.language(iso2));
			}
			break;
		default:
			break;
		}
	}

	public final Map<String, String> value(RootNode node, String key)
	{
		final Map<String, String> result = new HashMap<String, String>();
		switch (type)
		{
		case EMPTY:
			for (String iso2 : node.getExistingLanguages()) {
				result.put(iso2, "");
			}
			break;
		case ISO2KEY:
			for (String iso2 : node.getExistingLanguages()) {
				result.put(iso2, iso2 + "_" + key);
			}
			break;
		case KEY:
			for (String iso2 : node.getExistingLanguages()) {
				result.put(iso2, key);
			}
			break;
		case TEXT:
			for (String iso2 : node.getExistingLanguages()) {
				result.put(iso2, userDefinedText);
			}
			break;
		case ISO2TEXT:
			for (String iso2 : node.getExistingLanguages()) {
				result.put(iso2, iso2 + "_" + userDefinedText);
			}
			break;
		default:
			break;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		if (type == null)
			return "";
		return type.name();
	}

	enum DefaultValueTypes
	{
		KEY, // use the key of the I18nNode
		ISO2KEY, // use the language code and the key
		EMPTY, // no text empty value
		TEXT, // user defined text, delimiter is : between type and
				// text
		ISO2TEXT, ;
	}
}
