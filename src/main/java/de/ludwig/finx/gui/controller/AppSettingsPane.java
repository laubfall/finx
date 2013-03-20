package de.ludwig.finx.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import org.apache.log4j.Logger;

import de.ludwig.finx.settings.I18nDefaultValueSetting;
import de.ludwig.finx.settings.I18nDefaultValueSetting.DefaultValueTypes;
import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * @author Daniel
 * 
 */
public class AppSettingsPane extends BaseController
{
	private static final Logger LOG = Logger.getLogger(AppSettingsPane.class);

	private Window owner;

	@FXML
	private ChoiceBox<String> defaultEmpty;

	@FXML
	private TextField emptyText;

	/**
	 * @param owner
	 *            The owner Window gets closed if the saving process of settings is canceled
	 */
	public AppSettingsPane(Window owner)
	{
		super(AppSettingsPane.class.getResource("/de/ludwig/finx/gui/fxml/AppSettingsPane.fxml"));
		this.owner = owner;
		defaultEmpty.getItems().addAll(DefaultValueTypes.EMPTY.name(), DefaultValueTypes.ISO2KEY.name(),
				DefaultValueTypes.ISO2TEXT.name(), DefaultValueTypes.KEY.name(), DefaultValueTypes.TEXT.name());

		defaultEmpty.getSelectionModel().select(
				de.ludwig.finx.settings.AppSettings.i18nDefaultValue.setting().toString());

		defaultEmpty.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2)
			{
				if (arg2.equals("TEXT")) {
					emptyText.setVisible(true);
				} else {
					emptyText.setVisible(false);
				}

			}
		});

		final I18nDefaultValueSetting i18nDefaultValueSetting = de.ludwig.finx.settings.AppSettings.i18nDefaultValue
				.setting();
		emptyText.setVisible(i18nDefaultValueSetting.getType().equals(DefaultValueTypes.TEXT));
		emptyText.setText(i18nDefaultValueSetting.getUserDefinedText());
	}

	@FXML
	private void saveSettings(ActionEvent event)
	{
		String selectedItem = defaultEmpty.getSelectionModel().getSelectedItem();
		String newValue = selectedItem
				+ (selectedItem.equals(DefaultValueTypes.TEXT.name()) ? ":" + emptyText.getText() : "");
		de.ludwig.finx.settings.AppSettings.i18nDefaultValue.change(newValue);
		SettingsDaoImpl.instance().saveSettings();
		LOG.info("saved settings for application");
		owner.hide();
	}

	@FXML
	private void cancelSaveSettings(ActionEvent event)
	{
		LOG.info("canceled save Settings");
		owner.hide();
	}
}
