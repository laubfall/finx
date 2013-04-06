package de.ludwig.finx.gui.component;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import de.ludwig.finx.Language;

/**
 * Typical Bean that represents the data of one row inside the I18nView.
 * 
 * @author Daniel
 * 
 */
public class I18nViewRow
{
	private StringProperty key = new SimpleStringProperty();

	private ObservableMap<Language, StringProperty> translations = new SimpleMapProperty<>(
			FXCollections.observableMap(new HashMap<Language, StringProperty>()));

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key.get();
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key)
	{
		this.key.set(key);
	}

	public StringProperty keyProperty()
	{
		return key;
	}

	/**
	 * @return the translations
	 */
	public Map<Language, StringProperty> getTranslations()
	{
		return translations;
	}

	/**
	 * @param translations
	 *            the translations to set
	 */
	public void setTranslations(ObservableMap<Language, StringProperty> translations)
	{
		this.translations = translations;
	}

	public ObservableMap<Language, StringProperty> translationsProperty()
	{
		return translations;
	}
}
