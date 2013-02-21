package de.ludwig.finx.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.settings.SettingsDaoImpl;
import de.ludwig.finx.settings.UpdatableSetting;

/**
 * 
 * @author Daniel
 * 
 */
public class PropertiesWriter
{
	private File source;

	private List<String> lines;
	
	private Locale localeToWriteFor;

	/**
	 * The original I18n-Property-Files stays untouched, their formatting is not
	 * modified.
	 * 
	 * Possible values: STRICT, NONSTRICT, NONE
	 * 
	 * nonstrict: The application adds new keys to the point where their natural
	 * ordering matches most (for example existing key: de.ludwig.test new Key
	 * de.ludwig.tesz, in this case the new key is added after the existing
	 * one). Other Pretty-Printing Settings have no effect.
	 * 
	 * strict: new keys are added to the end of the file. Other Pretty-Printing
	 * Settings have no effect.
	 * 
	 * none: if the application writes the I18n-Property-Files Keys are sorted
	 * as specified by the given Pretty-Print-Settings
	 */
	public static UpdatableSetting<PropertyPreserveMode> preservePropertyLayout;

	/**
	 * Defines the level of grouping of I18nNodes in the Property-File. 0 means
	 * no grouping, all I18nNodes are printed after each other. 1 means all
	 * I18nNodes with equal keyparts are grouped (that means followed by an
	 * empty line) and so on
	 */
	public static UpdatableSetting<Integer> keyGrouping;

	/**
	 * Number of empty lines between Groups of I18nNodes. No effect if
	 * {@link #PRETTY_PRINTING_GROUP} is 0
	 */
	public static UpdatableSetting<Integer> keyGroupSpace;

	/**
	 * defines how keys are sorted. If value of this setting is none keys are
	 * added to the file as added to the I18n-Structure.
	 * 
	 * possible values: ASC, DESC, NONE
	 * 
	 */
	public static UpdatableSetting<PropertyKeyOrderSetting> keyOrder;
	
	/**
	 * sometimes key-value-pairs in property-files are commented, we can say a comment is attached to a key-value pair.
	 * To avoid loseing this relationship finx has to know when a comment is ment to be related (attached) to a key-value-pair
	 * and when it is not. This value is the maximum count of empty lines between a comment and a key-value-pair finx interpret
	 * this as a relationship. Such relationships are not gonna be destroyed if pretty-printing is preformed.
	 */
	public static UpdatableSetting<Integer> attachCommentsWithEmptyLineCount;

	static
	{
		SettingsDaoImpl.instance().init(PropertiesWriter.class);
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
		for (I18nNode c : childs)
		{
			for (I18nNode n : c.flatten())
			{
				sb.append(n.key() + "=" + n.value(language.getLanguage())).append("\n");
			}
		}

		return sb;
	}

	/**
	 * @param source
	 *            the I18n-Property-Files the application reads from and writes
	 *            to.
	 * @param sourceLocale
	 *            controls which values the application pulls from the given
	 *            {@link I18nNode}s
	 */
	public PropertiesWriter(File source, Locale sourceLocale) throws ApplicationCodingException
	{
		this.source = source;
		localeToWriteFor = sourceLocale;
		
		try
		{
			lines = IOUtils.readLines(new FileInputStream(source));
		} catch (FileNotFoundException e)
		{
			throw new ApplicationCodingException(e);
		} catch (IOException e)
		{
			throw new ApplicationCodingException(e);
		}
	}

	/**
	 * Transforms the root-node into Strings with respect to the existing
	 * I18n-Property-File and existing Pretty-Print-Settings. If an
	 * I18n-Key-Value-Pair exists in file but not in the root-node the
	 * I18n-Key-Value-Pair is removed from the file.
	 * 
	 * After processing the property-file will written.
	 */
	void process(final RootNode root)
	{
		try
		{
			insert(root);
			removeDeleted(root);
			rework();
			store();
		} catch (FileNotFoundException e)
		{
			throw new ApplicationCodingException(e);
		} catch (IOException e)
		{
			throw new ApplicationCodingException(e);
		}
	}

	/**
	 * 
	 * @param node
	 *            insert this node into the file structure with respect to
	 *            existing Pretty-Print-Settings. Does nothing if the
	 *            Key-Value-Pair already exists in the file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void insert(final RootNode root) throws FileNotFoundException, IOException
	{
		for (final I18nNode processThis : root.flatten())
		{
			int foundAt = -1;
			final String keyToFind = processThis.key() + "=";
			for (int i = 0; i < lines.size(); i++)
			{
				if (lines.get(i).startsWith(keyToFind) == false)
				{
					continue;
				}
	
				foundAt = i;
				break;
			}
	
			// insert
			if (foundAt == -1)
			{
				// because we insert new lines it is necessary to calculate the comment positions again
				// if we want to insert a new line.
				final List<Integer> commentBlocks = PropertyFileUtils.commentBlocks(lines);
				
				switch (preservePropertyLayout.setting())
				{
				case NONSTRICT:
					int posToInsert = 0;
					final String keyValue = processThis.keyValue(localeToWriteFor);
					for(;posToInsert < lines.size(); posToInsert++) {
						// this line is part of a comment
						if(commentBlocks.contains(Integer.valueOf(posToInsert))) {
							// that means that is no place for a key-value-pair
							continue;
						}
						
						final String actualLine = lines.get(posToInsert);
						if(actualLine.compareTo(keyValue) > 1) {
							break;
						}
					}
					
					lines.add(posToInsert+1, keyValue);
					break;
				case NONE:
					// TODO Sort und "Druck" nach Settings
					break;
				case STRICT:
					lines.add(keyToFind + processThis.value(localeToWriteFor.getLanguage()));
					break;
				default:
					break;
				}
			} else
			{ // update
				// TODO
			}
		}
	}

	/**
	 * Removes I18n-Key-Value-Pairs from fail that are not any longer in the
	 * Root-Node-Structure
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void removeDeleted(final RootNode root) throws FileNotFoundException, IOException
	{
		Properties p = new Properties();
		p.load(new FileInputStream(source));

		for (I18nNode processThis : root.flatten())
		{
			// TODO
		}
	}

	/**
	 * Sorts the lines of a given I18n-Properties-File with respect to the actual Pretty-Print-Settings.
	 */
	private void rework() {
		
	}
	
	private void store() throws FileNotFoundException, IOException
	{
		IOUtils.writeLines(lines, "\n", new FileOutputStream(source), "ISO-8859-1");
	}

	/**
	 * Inserts a string after a textline that starts with the given String.
	 * 
	 * @param afterThis
	 *            Normally this holds the key of an I18n-Node and the
	 *            equals-Symbol.
	 * @throws IOException
	 */
	private void insertAfterStartsWith(final String afterThis, final String insertThis, final File insertHere)
			throws IOException
	{
		long sizeOfFile = FileUtils.sizeOf(insertHere);
		RandomAccessFile raf = new RandomAccessFile(insertHere, "rw");

		try
		{
			String line = null;
			while ((line = raf.readLine()) != null)
			{
				if (line.startsWith(afterThis))
				{
					String filePart = filePart(insertHere, raf.getFilePointer());
					sizeOfFile += insertThis.length();
					raf.setLength(sizeOfFile);
					raf.writeChars(insertThis);
					raf.writeChars(filePart);
				}
			}
		} finally
		{
			raf.close();
		}
	}

	/**
	 * Returns a part of a given textfile starting from a given offset.
	 * 
	 * @param f
	 *            a textfile
	 * @param offset
	 *            the offset, from that point on method will return text from
	 *            the given file
	 * @return s. description
	 * @throws IOException
	 */
	private String filePart(File f, long offset) throws IOException
	{
		final RandomAccessFile rafR = new RandomAccessFile(f, "r");
		try
		{
			rafR.seek(offset);
			String result = null;
			StringBuilder sb = new StringBuilder();
			while ((result = rafR.readLine()) != null)
			{
				sb.append(result);
			}
			return sb.toString();
		} finally
		{
			rafR.close();
		}
	}
}
