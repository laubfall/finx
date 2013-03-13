package de.ludwig.finx.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.settings.AppSettings;

/**
 * 
 * @author Daniel
 */
public class PropertiesReaderTest
{
	public static final String TEST_PROPERTIES_FILE_NAME = "P1.0.0.I18n_de_DE.properties";

	@Test
	public void testCreateNodeView() throws URISyntaxException
	{
		URL resource = getClass().getResource("/PropertiesReaderTest/");
		File file = new File(resource.toURI());

		PropertiesReader pr = new PropertiesReader(file, AppSettings.i18nPropFilePostFix.setting(),
				AppSettings.i18nPropFilePreFix.setting());
		RootNode root = pr.createNodeView();

		Assert.assertNotNull(root);
		final List<I18nNode> rootNodes = root.getRootNodes();
		Assert.assertNotNull(rootNodes);
		Assert.assertFalse(rootNodes.isEmpty());
		Assert.assertEquals(1, rootNodes.size());
	}
}
