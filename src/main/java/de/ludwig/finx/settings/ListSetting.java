package de.ludwig.finx.settings;

import java.util.List;

import org.apache.commons.lang3.text.StrTokenizer;

/**
 * @author Daniel
 *
 */
@SettingType(ListSetting.class)
public class ListSetting extends AbstractSetting<List<String>>
{

	private String delimiter = ",";
	
	private List<String> listSetting;
	
	/**
	 * 
	 */
	public ListSetting()
	{
		super();
	}

	/**
	 * @param rawValue
	 */
	public ListSetting(String rawValue)
	{
		super(rawValue);
		initialize(rawValue, delimiter);
	}

	public ListSetting(String rawValue, final String delimiter) {
		super(rawValue);
		this.delimiter = delimiter;
		initialize(rawValue, delimiter);
	}
	
	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#setting()
	 */
	@Override
	public List<String> setting()
	{
		return listSetting;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.settings.Setting#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String rawValue)
	{
		// NOOP we don't use this method because during runtime delimiter is not set when this method is called
	}

	private void initialize(String rawValue, String delimiter) {
		StrTokenizer tokenizer = StrTokenizer.getCSVInstance(rawValue).setDelimiterString(delimiter);
		listSetting = tokenizer.getTokenList();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for(String tok : listSetting) {
			sb.append(tok).append(" ");
		}
		return sb.toString();
	}

}
