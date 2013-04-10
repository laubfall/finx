package de.ludwig.finx.gui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import de.ludwig.finx.Language;
import de.ludwig.finx.gui.component.I18nViewRow;
import de.ludwig.finx.io.I18nNode;
import de.ludwig.finx.io.RootNode;

/**
 * 
 * 
 * @author Daniel
 * 
 */
public class I18nViewHelper
{

	/**
	 * Adds all necessary columns to the given Table-View. The key-Column is the only static Column.
	 * The other columns holds the data for the different languages. And the available languages are
	 * defined by the corresponding workingset.
	 * 
	 * @param i18nView
	 * @param root
	 *            needed to calculate the needed Language columns
	 */
	public static void addColumns(final TableView<I18nViewRow> i18nView, final RootNode root)
	{
		TableColumn<I18nViewRow, String> keyColumn = new TableColumn<>("key");
		keyColumn.setCellValueFactory(new PropertyValueFactory<I18nViewRow, String>("key"));

		final Set<TableColumn<I18nViewRow, String>> cols = new HashSet<>();
		cols.add(keyColumn);

		final Set<Language> existingLanguages = root.getExistingLanguages();
		for (final Language lang : existingLanguages) {
			TableColumn<I18nViewRow, String> langColumn = new TableColumn<>(lang.language());
			langColumn.setCellValueFactory(new LanguageCellValueFactory(lang));
			cols.add(langColumn);
		}

		i18nView.getColumns().setAll(cols);
	}

	/**
	 * Dependent on the actual selected WorkingSet this method returns a List of Rows of Type
	 * {@link I18nViewRow}.
	 * 
	 * @return s. description.
	 */
	public static ObservableList<I18nViewRow> createViewData(final RootNode root)
	{
		final List<I18nNode> rootNodes = root.getRootNodes();

		final Set<Language> existingLanguages = root.getExistingLanguages();
		final Map<String, I18nViewRow> raw = new HashMap<>();
		for (I18nNode rn : rootNodes) {

			for (Language lang : existingLanguages) {
				final List<I18nNode> contentNodes = rn.flattenToNonEmpty(lang);
				for (I18nNode cn : contentNodes) {
					String key = cn.key();
					if (raw.containsKey(key) == false) {
						final I18nViewRow row = new I18nViewRow();
						row.setKey(cn.key());
						raw.put(key, row);
					}
					final I18nViewRow row = raw.get(key);
					row.getTranslations().put(lang, new SimpleStringProperty(cn.value(lang)));
				}
			}
		}
		final ObservableList<I18nViewRow> data = FXCollections.observableArrayList(new ArrayList<I18nViewRow>());
		data.addAll(raw.values());
		return data;
	}

	static class LanguageCellValueFactory implements
			Callback<TableColumn.CellDataFeatures<I18nViewRow, String>, ObservableValue<String>>
	{
		private Language lang;

		/**
		 * @param lang
		 */
		public LanguageCellValueFactory(Language lang)
		{
			super();
			this.lang = lang;
		}

		@Override
		public ObservableValue<String> call(CellDataFeatures<I18nViewRow, String> param)
		{
			final StringProperty val = param.getValue().getTranslations().get(lang);
			return val;
		}

	}
}
