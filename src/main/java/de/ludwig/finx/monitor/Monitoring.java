/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.monitor;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.ludwig.finx.scanner.I18nKey;
import de.ludwig.finx.scanner.I18nScanner;
import de.ludwig.finx.settings.AppSettingNames;
import de.ludwig.finx.settings.AppSettings;
import de.ludwig.finx.settings.ListSetting;
import de.ludwig.finx.settings.SettingsDaoImpl;

/**
 * This class provides basic methods to handle all monitoring relevant programm
 * logic
 * 
 * @author Daniel
 */
public class Monitoring
{

	private static Monitoring INSTANCE;

	private FileMonitor fm;

	private Set<FileI18nChangesListener> changeListeners;

	private static final Logger log = Logger.getLogger(Monitoring.class);

	private Monitoring()
	{
		fm = new FileMonitor(100);
		changeListeners = new HashSet<FileI18nChangesListener>();
	}
	
	public static synchronized Monitoring instance() {
		if(INSTANCE == null) {
			INSTANCE = new Monitoring();
		}
		
		return INSTANCE;
	}

	public void addScanner(final I18nScanner scannerToAdd)
	{
		FileI18nChangesListener i18nChangeListener = new FileI18nChangesListener(scannerToAdd);
		fm.addListener(i18nChangeListener);
		changeListeners.add(i18nChangeListener);
	}

	public void addSrcDirectoriesToMonitor(final List<File> dirsToMonitor)
	{
		for (File dir : dirsToMonitor)
		{
			if (dir.isDirectory() == false)
			{
				log.warn(String.format("File-Object %s is not a directory, skipping", dir.getAbsolutePath()));
				continue;
			}

			log.info(String.format("Scanning Directory %s for Files", dir.getAbsolutePath()));
			
			final File[] filesToMonitor = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name)
				{
					final ListSetting extensions = AppSettings.fileExtensions.setting();
					for(String ext : extensions.setting()) {
						if(name.endsWith(ext)) { 
							return true;
						}
					}
					return false;
				}
			});

			if(filesToMonitor == null || filesToMonitor.length == 0) {
				log.warn(String.format("No files found for monitoring in directory %s", dir.getAbsolutePath()));
			}
			
			for (File file : filesToMonitor)
			{
				log.info(String.format("File %s will be monitored", file.getName()));
				fm.addFile(file);
			}
		}
	}

	public void startMonitoring()
	{
		fm.start();
	}

	/**
	 * TODO Ist diese Methode notwendig? Problem ist auch dass einmal gestoppte Scheduler
	 * nicht so ohne weiteres wieder gestartet werden können.
	 */
	public void stopMonitoring()
	{
		fm.stop();
	}

	/**
	 * TODO is this method really necessary?
	 * @return
	 */
	public List<I18nKey> latestChanges()
	{
		return null;
	}
}
