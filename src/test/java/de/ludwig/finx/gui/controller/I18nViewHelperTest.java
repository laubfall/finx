package de.ludwig.finx.gui.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.Language;
import de.ludwig.finx.gui.component.I18nViewRow;
import de.ludwig.finx.io.PropertiesReader;
import de.ludwig.finx.io.RootNode;
import de.ludwig.finx.settings.AppSettings;

/**
 * @author Daniel
 * 
 */
public class I18nViewHelperTest
{
	@Test
	public void createViewData() throws URISyntaxException
	{
		URL resource = getClass().getResource("/I18nViewHelperTest/");
		File file = new File(resource.toURI());

		PropertiesReader pr = new PropertiesReader(file, AppSettings.i18nPropFilePostFix.setting(),
				AppSettings.i18nPropFilePreFix.setting());

		RootNode root = pr.createNodeView();
		ObservableList<I18nViewRow> createViewData = I18nViewHelper.createViewData(root);
		Assert.assertNotNull(createViewData);
		Assert.assertFalse(createViewData.isEmpty());
		Assert.assertEquals(4, createViewData.size());

		for (int i = 0; i < 4; i++) {
			I18nViewRow i18nViewRow = createViewData.get(0);
			Assert.assertNotNull(i18nViewRow);
			Map<Language, StringProperty> translations = i18nViewRow.getTranslations();
			Assert.assertNotNull(translations);
			Assert.assertFalse(translations.isEmpty());
			Assert.assertEquals(2, translations.keySet().size());
		}
	}
}
