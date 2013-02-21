package de.ludwig.finx.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.io.I18nNode;
import de.ludwig.finx.io.PropertiesReader;
import de.ludwig.finx.io.RootNode;
import de.ludwig.finx.settings.AppSettings;
import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 *
 * @author Daniel
 */
public class PropertiesReaderTest {
	public static final String TEST_PROPERTIES_FILE_NAME = "P1.0.0.I18n_de_DE.properties";
	
    @Test
    public void testCreateNodeView() throws URISyntaxException{
    	File i18nPropertiesLocation = AppSettings.i18nPropertiesLocation.setting();
    	URL resource = getClass().getResource("/PropertiesReaderTest/");
    	File file = new File(resource.toURI());
    	SettingsDaoImpl.instance().changeSetting("i18nPropertiesLocation", file.getAbsolutePath());
    	
        i18nPropertiesLocation = AppSettings.i18nPropertiesLocation.setting();
        Assert.assertNotNull(i18nPropertiesLocation);
        Assert.assertTrue(i18nPropertiesLocation.exists());
        
        PropertiesReader pr = new PropertiesReader(i18nPropertiesLocation);
        RootNode root = pr.createNodeView();
        
        Assert.assertNotNull(root);
        final List<I18nNode> rootNodes = root.getRootNodes();
        Assert.assertNotNull(rootNodes);
        Assert.assertFalse(rootNodes.isEmpty());
        Assert.assertEquals(1, rootNodes.size());
    }
}
