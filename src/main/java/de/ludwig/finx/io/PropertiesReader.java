package de.ludwig.finx.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.settings.AppSettings;

/**
 * Low-Level read-only Access to Property-Files
 * 
 * @author Daniel
 */
public class PropertiesReader
{
	private File[] propertiesfiles;
	private static final Logger log = Logger.getLogger(PropertiesReader.class);

	PropertiesReader(final File propertieFilesDir)
	{
		if (propertieFilesDir.isDirectory() == false)
		{
			throw new ApplicationCodingException("This is not a directory: " + propertieFilesDir.getAbsolutePath());
		}

		log.debug("Initializing PropertiesReader with Properties-Dir: " + propertieFilesDir.getAbsolutePath());

		propertiesfiles = propertieFilesDir.listFiles(new FileFilter() {

			public boolean accept(File pathname)
			{
				if (pathname.isDirectory())
				{
					return false;
				}

				final String filename = pathname.getName();
				String localInfo = StringUtils.substringBetween(filename, AppSettings.i18nPropFilePostFix.setting(),
						AppSettings.i18nPropFilePreFix.setting());
				if (StringUtils.isBlank(localInfo))
				{
					log.debug("Unable to locate Locale-Info in Filename " + filename + " . Ignoring this file!");
					return false;
				}

				return true;
			}
		});

		log.debug(String.format("PropertiesReader initialized, %1$d Propertie-Files found", propertiesfiles.length));
	}

	/**
	 * 
	 * @return
	 */
	RootNode createNodeView()
	{
		final Map<Locale, Properties> langProperties = new HashMap<Locale, Properties>();

		for (File f : propertiesfiles)
		{
			try
			{
				Locale localeFromFileName = localeFromFileName(f.getName());
				Properties p = new Properties();
				p.load(new FileInputStream(f));
				langProperties.put(localeFromFileName, p);
			} catch (IOException ex)
			{
				log.error("io-error while loading Properties-file", ex);
			}
		}

		final Set<Locale> locales = langProperties.keySet();
		// die Sprachen für die wir Internationalisierungsdateien gefunden haben
		final RootNode root = new RootNode();
		for (Locale l : locales)
		{
			log.debug("creating nodes for language: " + l.getLanguage());
			final Properties i18ns = langProperties.get(l);
			final Set<Object> keySet = i18ns.keySet();

			// de.ludwig.blah=
			// com.ludwig.blub=
			for (Object key : keySet)
			{
				root.addNode((String) key, l.getLanguage(), i18ns.getProperty((String) key));
			}
		}
		return root;
	}
	
	private Locale localeFromFileName(String filename)
	{
		String localInfo = StringUtils.substringBetween(filename, AppSettings.i18nPropFilePostFix.setting(),
				AppSettings.i18nPropFilePreFix.setting());
		if (localInfo.contains("_") == false)
		{
			return new Locale(localInfo);
		}

		String lang = StringUtils.substringBefore(localInfo, "_");
		String country = StringUtils.substringAfter(localInfo, "_");
		Locale l = new Locale(lang, country);
		return l;
	}
}
