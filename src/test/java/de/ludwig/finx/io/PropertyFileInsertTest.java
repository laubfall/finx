package de.ludwig.finx.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.Language;
import de.ludwig.finx.io.PropertyKeyOrderSetting.PropertyKeyOrder;

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
		propertyFileHandle = I18nFileCreator.start().end();
		Language de = new Language(Locale.GERMANY);
		final PropertyFile pf = new PropertyFile(propertyFileHandle, de);

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", de, "unter-knoten");
		insertThis.update(new Language("EN"), "sub-node");
		List<I18nNode> flatten = insertThis.flatten();
		for (I18nNode ins : flatten) {
			pf.insertOrUpdate(ins);
		}
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(3, lines.size());
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
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.STRICT.name());

		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "top-node").end();
		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", new Language("DE"), "sub-node");
		pf.insertOrUpdate(insertThis);
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(2, lines.size());
	}

	/**
	 * {@link PropertyPreserveMode#NONSTRICT}
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void insertIntoFullNonStrictDesc() throws FileNotFoundException, IOException
	{
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONSTRICT.name());
		PropertiesWriter.keyOrder.change(PropertyKeyOrder.DESC.name());
		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "top-node")
				.addKeyValue("de.ludwig.test", "nur so").end();
		Language lang = new Language(Locale.GERMANY);
		final PropertyFile pf = new PropertyFile(propertyFileHandle, lang);
		Assert.assertNotNull(pf.getStartingBlock());

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", new Language("DE"), "sub-node");
		List<I18nNode> flattenToNonEmpty = insertThis.flattenToNonEmpty(lang);
		for (I18nNode n : flattenToNonEmpty) {
			pf.insertOrUpdate(n);
		}
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(3, lines.size());

		Assert.assertEquals("de.ludwig=top-node", lines.get(0).getLine());
		Assert.assertEquals("de.ludwig.sub=sub-node", lines.get(1).getLine());
		Assert.assertEquals("de.ludwig.test=nur so", lines.get(2).getLine());
	}

	@Test
	public void insertIntoFullNonStrictAsc() throws FileNotFoundException, IOException
	{
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONSTRICT.name());
		PropertiesWriter.keyOrder.change(PropertyKeyOrder.ASC.name());
		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "top-node")
				.addKeyValue("de.ludwig.test", "nur so").end();
		Language lang = new Language(Locale.GERMANY);
		final PropertyFile pf = new PropertyFile(propertyFileHandle, lang);
		Assert.assertNotNull(pf.getStartingBlock());

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", new Language("DE"), "sub-node");
		List<I18nNode> flattenToNonEmpty = insertThis.flattenToNonEmpty(lang);
		for (I18nNode n : flattenToNonEmpty) {
			pf.insertOrUpdate(n);
		}
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(3, lines.size());

		Assert.assertEquals("de.ludwig.sub=sub-node", lines.get(0).getLine());
		Assert.assertEquals("de.ludwig=top-node", lines.get(1).getLine());
		Assert.assertEquals("de.ludwig.test=nur so", lines.get(2).getLine());
	}

	/**
	 * {@link PropertyPreserveMode#NONE}
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void insertIntoFullNoneDesc() throws FileNotFoundException, IOException
	{
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONE.name());
		PropertiesWriter.keyOrder.change(PropertyKeyOrder.DESC.name());
		PropertiesWriter.keyGrouping.change("1");
		PropertiesWriter.keyGroupSpace.change("1");

		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "1").addKeyValue("de.ludwig.test", "2")
				.addKeyValue("com.ludwig", "3").addKeyValue("com.ludwig.test", "4").end();

		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf);
		List<String> filedata = pf.filedata();
		Assert.assertEquals(5, filedata.size());

		I18nNode insertThis = I18nNode.create("de.ludwig.test.sub", new Language("DE"), "sub-node");
		List<I18nNode> flattenToNonEmpty = insertThis.flattenToNonEmpty(new Language(Locale.GERMANY));
		for (I18nNode n : flattenToNonEmpty) {
			pf.insertOrUpdate(n);
		}

		filedata = pf.filedata();
		Assert.assertEquals(6, filedata.size());
		Assert.assertTrue(StringUtils.startsWith(filedata.get(0), "de.ludwig="));
		Assert.assertTrue(StringUtils.startsWith(filedata.get(1), "de.ludwig.test="));
		Assert.assertTrue(StringUtils.startsWith(filedata.get(2), "de.ludwig.test.sub"));
	}

	@Test
	public void insertIntoFullNoneAsc() throws FileNotFoundException, IOException
	{
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONE.name());
		PropertiesWriter.keyOrder.change(PropertyKeyOrder.ASC.name());
		PropertiesWriter.keyGrouping.change("1");
		PropertiesWriter.keyGroupSpace.change("1");

		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "1").addKeyValue("de.ludwig.test", "2")
				.addKeyValue("com.ludwig", "3").addKeyValue("com.ludwig.test", "4").end();

		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf);
		List<String> filedata = pf.filedata();
		Assert.assertEquals(5, filedata.size());

		I18nNode insertThis = I18nNode.create("de.ludwig.test.sub", new Language("DE"), "sub-node");
		List<I18nNode> flattenToNonEmpty = insertThis.flattenToNonEmpty(new Language(Locale.GERMANY));
		for (I18nNode n : flattenToNonEmpty) {
			pf.insertOrUpdate(n);
		}

		filedata = pf.filedata();
		Assert.assertEquals(6, filedata.size());
		Assert.assertTrue(StringUtils.startsWith(filedata.get(2), "de.ludwig="));
		Assert.assertTrue(StringUtils.startsWith(filedata.get(1), "de.ludwig.test="));
		Assert.assertTrue(StringUtils.startsWith(filedata.get(0), "de.ludwig.test.sub"));
	}

	@Test
	public void insertIntoFullNewBlock() throws FileNotFoundException, IOException
	{
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONE.name());
		PropertiesWriter.keyGrouping.change("1");
		PropertiesWriter.keyGroupSpace.change("1");
		PropertiesWriter.keyOrder.change(PropertyKeyOrder.DESC.name());

		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "1").addKeyValue("de.ludwig.test", "2")
				.addKeyValue("com.ludwig", "3").addKeyValue("com.ludwig.test", "4").end();
		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		I18nNode insertThis = I18nNode.create("nl.ludwig.test.sub", new Language("DE"), "sub-node");
		List<I18nNode> flattenToNonEmpty = insertThis.flattenToNonEmpty(new Language(Locale.GERMANY));
		for (I18nNode n : flattenToNonEmpty) {
			pf.insertOrUpdate(n);
		}

		List<String> filedata = pf.filedata();
		Assert.assertEquals(7, filedata.size()); // new block so there has to be an additional empty
													// line
		Assert.assertTrue(StringUtils.startsWith(filedata.get(0), "de.ludwig="));
		Assert.assertTrue(StringUtils.startsWith(filedata.get(1), "de.ludwig.test="));
		Assert.assertTrue(StringUtils.startsWith(filedata.get(6), "nl.ludwig.test.sub"));
	}

	/**
	 * ..Existing = the key that should be inserted already exists in the file. We only want to
	 * update the existing key.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void insertIntoFullExisting() throws FileNotFoundException, IOException
	{
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.STRICT.name());

		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "top-node")
				.addKeyValue("de.ludwig.test", "nur so").end();
		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());

		I18nNode insertThis = I18nNode.create("de.ludwig.test", new Language("DE"), "sub-node");
		List<I18nNode> flattenToNonEmpty = insertThis.flattenToNonEmpty(new Language(Locale.GERMANY));
		for (I18nNode n : flattenToNonEmpty) {
			pf.insertOrUpdate(n);
		}
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(2, lines.size());

		Assert.assertEquals("de.ludwig=top-node", lines.get(0).getLine());
		Assert.assertEquals("de.ludwig.test=sub-node", lines.get(1).getLine());
	}
}
