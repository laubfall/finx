/**
 * 
 */
package de.ludwig.finx.io;

import java.io.File;

import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;

/**
 * This class provide methods to handle all relevant Propertyfile handling operations in a highlevel
 * manner. Such operations can be: - Setup PropertiesReader - Reading Property files - Writing
 * Propertyfiles
 * 
 * @author Daniel
 * 
 */
public class PropertyFileHandling
{
	private Logger log = Logger.getLogger(PropertyFileHandling.class);

	private static PropertyFileHandling INSTANCE;

	private PropertiesReader propReader;

	/**
	 * cache of the {@link RootNode} structure. Actually this is not a very nice solution. If the
	 * property files are changed by another application the nodes will differ from the content of
	 * the property files -> TODO monitoring of the property files maybe!?
	 */
	private RootNode ramNodes;

	public PropertyFileHandling()
	{

	}

	@Deprecated
	public static synchronized PropertyFileHandling instance()
	{
		if (INSTANCE == null) {
			INSTANCE = new PropertyFileHandling();
		}

		return INSTANCE;
	}

	public void setupPropertiesReader(File propertyFileDir, String postFix, String preFix)
	{
		propReader = new PropertiesReader(propertyFileDir, postFix, preFix);
	}

	public RootNode nodeStructureFromFiles()
	{
		if (propReader == null) {
			throw new ApplicationCodingException("The propertyfile reader is not initialized");
		}

		if (ramNodes != null)
			return ramNodes;

		log.debug("loading I18N-Nodestructure from file to ram");
		ramNodes = propReader.createNodeView();
		return ramNodes;
	}

	public RootNode reloadNodeStructureFromFiles()
	{
		ramNodes = null;
		return nodeStructureFromFiles();
	}

	/**
	 * 
	 * @return a root node based on the Property Files (no caching)
	 */
	public RootNode actualFileStructure()
	{
		if (propReader == null) {
			throw new ApplicationCodingException("The propertyfile reader is not initialized");
		}

		return propReader.createNodeView();
	}

	/**
	 * "Serialize" the hole node structure to the property files
	 * 
	 */
	public void nodeStructureToFiles()
	{

	}
}
