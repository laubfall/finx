package de.ludwig.finx.monitor;

import java.io.File;
import java.util.Set;

import org.apache.log4j.Logger;

import de.ludwig.finx.io.I18nNode;
import de.ludwig.finx.io.PropertyFileHandling;
import de.ludwig.finx.io.RootNode;
import de.ludwig.finx.scanner.I18nKey;
import de.ludwig.finx.scanner.I18nScanner;

/**
 * Detects changes to i18n-keys. Changes mean that keys are new (not in any provided propertie file)
 * or there value has changed.
 * 
 * @author Daniel
 */
public class FileI18nChangesListener implements FileListener
{
	private static final Logger LOG = Logger.getLogger(FileI18nChangesListener.class);

	private I18nScanner detector;

	private PropertyFileHandling pfh;

	public FileI18nChangesListener(final I18nScanner detector, PropertyFileHandling pfh)
	{
		this.detector = detector;
		this.pfh = pfh;
	}

	/**
	 * a file change occured now trigger the detection of any I18n-Key
	 * 
	 * normally the hole process should be: - load existing i18n-properties-files (done by app) (/)
	 * - scan source files for i18n-keys (done by scanner triggerd here) (/) - check if any key does
	 * not exist in the i18n-property-files (done here) (/) - if there is a key that does not exist
	 * update the i18n-property-files (done here) (/) - if the value of the key has changed
	 * (optional) update the i18n-propertie-files (done here)
	 */
	public void fileChanged(File file)
	{
		LOG.debug("Detected file change in file " + file.getAbsolutePath());

		final Set<I18nKey> keys = detector.keys(file);
		LOG.debug(String.format("%d i18n-keys found in changed file %s", keys.size(), file.getName()));
		final RootNode nodes = PropertyFileHandling.instance().nodeStructureFromFiles();
		for (final I18nKey k : keys) {
			final I18nNode existingNode = nodes.findNode(k.getI18nKey());
			if (existingNode == null) {
				nodes.addAll(k.getI18nKey());
			} else {
				// TODO without the meta-information I18nKeyControll it is not possible to react in
				// a good way
			}
		}
	}

}
