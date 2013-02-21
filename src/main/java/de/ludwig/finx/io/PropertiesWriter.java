package de.ludwig.finx.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.ApplicationException;
import de.ludwig.finx.settings.SettingsDaoImpl;
import de.ludwig.finx.settings.UpdatableSetting;

/**
 * 
 * @author Daniel
 * 
 */
public class PropertiesWriter
{
	private File target;

	/**
	 * The original I18n-Property-Files stays untouched, their formatting is not modified.
	 * 
	 * Possible values: STRICT, NONSTRICT, NONE
	 * 
	 * nonstrict: The application adds new keys to the point where their natural ordering matches
	 * most (for example existing key: de.ludwig.test new Key de.ludwig.tesz, in this case the new
	 * key is added after the existing one). Other Pretty-Printing Settings have no effect.
	 * 
	 * strict: new keys are added to the end of the file. Other Pretty-Printing Settings have no
	 * effect.
	 * 
	 * none: if the application writes the I18n-Property-Files Keys are sorted as specified by the
	 * given Pretty-Print-Settings
	 */
	public static UpdatableSetting<PropertyPreserveMode> preservePropertyLayout;

	/**
	 * Defines the level of grouping of I18nNodes in the Property-File. 0 means no grouping, all
	 * I18nNodes are printed after each other. 1 means all I18nNodes with equal keyparts are grouped
	 * (that means followed by an empty line) and so on
	 */
	public static UpdatableSetting<Integer> keyGrouping;

	/**
	 * Number of empty lines between Groups of I18nNodes. No effect if
	 * {@link #PRETTY_PRINTING_GROUP} is 0
	 */
	public static UpdatableSetting<Integer> keyGroupSpace;

	/**
	 * defines how keys are sorted. If value of this setting is none keys are added to the file as
	 * added to the I18n-Structure.
	 * 
	 * possible values: ASC, DESC, NONE
	 * 
	 */
	public static UpdatableSetting<PropertyKeyOrderSetting> keyOrder;

	/**
	 * sometimes key-value-pairs in property-files are commented, we can say a comment is attached
	 * to a key-value pair. To avoid loseing this relationship finx has to know when a comment is
	 * ment to be related (attached) to a key-value-pair and when it is not. This value is the
	 * maximum count of empty lines between a comment and a key-value-pair finx interpret this as a
	 * relationship. Such relationships are not gonna be destroyed if pretty-printing is preformed.
	 */
	public static UpdatableSetting<Integer> attachCommentsWithEmptyLineCount;

	/**
	 * The line-ending-sequence to use at the end of a line
	 */
	public static UpdatableSetting<String> lineEnding;

	private static final Logger LOG = Logger.getLogger(PropertiesWriter.class);

	static {
		SettingsDaoImpl.instance().init(PropertiesWriter.class);
	}

	/**
	 * @param target
	 *            the I18n-Property-Files the application reads from and writes to.
	 */
	public PropertiesWriter(File target) throws ApplicationCodingException
	{
		Validate.notNull(target);
		this.target = target;
	}

	public void write(PropertyFile pf)
	{
		LOG.info("write PropertyFile to File " + target.getName());
		Iterator<Block> blockIterator = pf.iterator();
		final File f = new File(target.getParent(), target.getName() + ".tmp");
		while (blockIterator.hasNext()) {
			final Block block = blockIterator.next();
			List<Line> lines = block.getLines();
			LOG.debug("write block of type " + block.getType().name());
			for (Line l : lines) {
				try {
					FileUtils.writeStringToFile(f, l.getLine() + lineEnding, "ISO-8859-1", true);
					LOG.debug(String.format("wrote line %s to file %s", l.getLine(), f.getName()));
				} catch (IOException e) {
					LOG.error("propertyFile could not be written to file " + f.getName(), e);
					throw new ApplicationException("unable to write the propertyFile Object to a file");
				}
			}
		}

		try {
			FileUtils.copyFile(f, target);
			LOG.info("moved tmp-File to original File " + target.getName());
		} catch (IOException e) {
			LOG.error("moving tmp-File to original failed: " + e.getMessage(), e);
			throw new ApplicationException("it was not possible to update the properties-file " + target.getName());
		}
	}

	/**
	 * This method dumps a complete node-structure to a String(Builder)
	 * 
	 * @param childs
	 * @param sb
	 * @param language
	 *            TODO
	 * @return
	 */
	public static StringBuilder dumpI18nTreeStructure(final List<I18nNode> childs, StringBuilder sb, Locale language)
	{
		for (I18nNode c : childs) {
			for (I18nNode n : c.flatten()) {
				sb.append(n.key() + "=" + n.value(language.getLanguage())).append("\n");
			}
		}

		return sb;
	}

}
