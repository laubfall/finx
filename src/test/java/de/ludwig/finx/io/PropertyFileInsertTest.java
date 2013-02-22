package de.ludwig.finx.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.Language;

/**
 * The insertion of new keys is quite complex. So we have a test-case only for this part.
 * 
 * @author Daniel
 * 
 */
public class PropertyFileInsertTest extends BasePropertyFileTest
{
	@Test
	public void insertIntoEmpty() throws FileNotFoundException, IOException
	{
		PropertiesWriter.keyOrder.change("DESC");

		propertyFileHandle = I18nFileCreator.start().end();
		Language de = new Language(Locale.GERMANY);
		final PropertyFile pf = new PropertyFile(propertyFileHandle, de);

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", de, "unter-knoten");
		insertThis.update(new Language("EN"), "sub-node");
		pf.insert(insertThis);
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(3, lines.size());

		// we expect the value in german language because the PropertyFile was constructed with a
		// german locale
		Assert.assertEquals("de=", lines.get(0).getLine());
		Assert.assertEquals("de.ludwig=", lines.get(1).getLine());
		Assert.assertEquals("de.ludwig.sub=unter-knoten", lines.get(2).getLine());
	}

	/**
	 * {@link PropertyPreserveMode#STRICT}
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void insertIntoFullStrict() throws FileNotFoundException, IOException
	{
		PropertiesWriter.preservePropertyLayout.change(PropertyPreserveMode.STRICT.name());

		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "top-node").end();
		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", new Language("DE"), "sub-node");
		pf.insert(insertThis);
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(2, lines.size());
	}

	/**
	 * {@link PropertyPreserveMode#NONSTRICT}
	 */
	@Test
	public void insertIntoFullNonStrict()
	{

	}

	/**
	 * {@link PropertyPreserveMode#NONE}
	 */
	@Test
	public void insertIntoFullNone()
	{

	}
}
