package de.ludwig.finx.gui.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		TableColumn<I18nViewRow, String> keyColumn = new TableColumn<>();
		keyColumn.setCellValueFactory(new PropertyValueFactory<I18nViewRow, String>("key"));

		final Set<TableColumn<I18nViewRow, String>> cols = new HashSet<>();
		cols.add(keyColumn);

		final Set<Language> existingLanguages = root.getExistingLanguages();
		for (final Language lang : existingLanguages) {
			TableColumn<I18nViewRow, String> langColumn = new TableColumn<>();
			langColumn.setCellValueFactory(new LanguageCellValueFactory(lang));
			cols.add(langColumn);
		}

		i18nView.getColumns().addAll(cols);
	}

	/**
	 * Dependent on the actual selected WorkingSet this method returns a List of Rows of Type
	 * {@link I18nViewRow}.
	 * 
	 * @return s. description.
	 */
	public static ObservableList<I18nViewRow> createViewData(final RootNode root)
	{
		final ObservableList<I18nViewRow> data = FXCollections.observableArrayList(new ArrayList<I18nViewRow>());
		final List<I18nNode> all = root.flatten();
		final Set<Language> existingLanguages = root.getExistingLanguages();

		for (final I18nNode node : all) {
			final I18nViewRow row = new I18nViewRow();
			row.setKey(node.key());
			for (Language lang : existingLanguages) {
				row.getTranslations().put(lang, new SimpleStringProperty(node.keyValue(lang)));
			}
		}

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
