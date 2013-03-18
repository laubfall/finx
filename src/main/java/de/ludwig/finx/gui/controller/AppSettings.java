package de.ludwig.finx.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Window;

import org.apache.log4j.Logger;

import de.ludwig.finx.settings.I18nDefaultValueSetting.DefaultValueTypes;
import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * @author Daniel
 * 
 */
public class AppSettings extends BaseController
{
	private static final Logger LOG = Logger.getLogger(AppSettings.class);

	private Window owner;

	@FXML
	private ChoiceBox<String> defaultEmpty;

	/**
	 * @param owner
	 *            The owner Window gets closed if the saving process of settings is canceled
	 */
	public AppSettings(Window owner)
	{
		super(AppSettings.class.getResource("/de/ludwig/finx/gui/fxml/AppSettingsPane.fxml"));
		this.owner = owner;
		defaultEmpty.getItems().addAll(DefaultValueTypes.EMPTY.name(), DefaultValueTypes.ISO2KEY.name(),
				DefaultValueTypes.ISO2TEXT.name(), DefaultValueTypes.KEY.name(), DefaultValueTypes.TEXT.name());

		defaultEmpty.getSelectionModel().select(
				de.ludwig.finx.settings.AppSettings.i18nDefaultValue.setting().toString());

	}

	@FXML
	private void saveSettings(ActionEvent event)
	{
		de.ludwig.finx.settings.AppSettings.i18nDefaultValue.change(defaultEmpty.getSelectionModel().getSelectedItem());
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
