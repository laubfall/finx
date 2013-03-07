package de.ludwig.finx.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.Language;

/**
 * @author Daniel
 * 
 */
public class PropertyFileTest extends BasePropertyFileTest
{
	@Test
	public void readKeyValue() throws IOException
	{
		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "test")
				.addKeyValue("de.luwdig.sub", "test").end();

		PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());
	}

	@Test
	public void readComment() throws FileNotFoundException, IOException
	{
		propertyFileHandle = I18nFileCreator.start().addSimpleComment("a comment")
				.addSimpleComment(" a Comment with space").addSimpleComment("!!!").end();

		PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());

		Assert.assertNotNull(startingBlock.getLines());
		Assert.assertFalse(startingBlock.getLines().isEmpty());
		Assert.assertEquals(3, startingBlock.getLines().size());
	}

	@Test
	public void readBlank() throws FileNotFoundException, IOException
	{
		propertyFileHandle = I18nFileCreator.start().addEmptyLine(1, 4).addEmptyLine(1).end();

		PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());
	}

	@Test
	public void readReallyBlankLine() throws FileNotFoundException, IOException
	{
		propertyFileHandle = I18nFileCreator.start().addEmptyLine(1).end();

		PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());
	}

	@Test
	public void readMerged() throws FileNotFoundException, IOException
	{
		propertyFileHandle = I18nFileCreator.start().addSimpleComment("test comment")
				.addKeyValue("de.ludwig", "some value").addEmptyLine(1, 2).end();

		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		final Iterator<Block> iterator = pf.iterator();
		Assert.assertTrue(iterator.hasNext());
		Block startingBlock = iterator.next();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(3, pf.size());

		Assert.assertNull(startingBlock.getPreceding());
		Assert.assertNotNull(startingBlock.getPursuing());
		Assert.assertEquals(BlockType.COMMENT, startingBlock.getType());

		Assert.assertTrue(iterator.hasNext());
		startingBlock = iterator.next();
		Assert.assertNotNull(startingBlock.getPreceding());
		Assert.assertNotNull(startingBlock.getPursuing());
		Assert.assertEquals(BlockType.KEYVALUE, startingBlock.getType());

		Assert.assertTrue(iterator.hasNext());
		startingBlock = iterator.next();
		Assert.assertNull(startingBlock.getPursuing());
		Assert.assertNotNull(startingBlock.getPreceding());
		Assert.assertEquals(BlockType.BLANK, startingBlock.getType());
	}

	@Test
	public void explode() throws FileNotFoundException, IOException
	{
		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "test")
				.addKeyValue("de.ludwig.2", "blah").addKeyValue("de.ludwig.3", "blub").end();
		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());
		Assert.assertNull(pf.getStartingBlock().getPursuing());

		final Block startingBlock = pf.getStartingBlock();
		Assert.assertTrue(startingBlock.getLines().size() == 3);

		startingBlock.explode();
		Assert.assertTrue(startingBlock.getLines().size() == 1);

		Block persuing = startingBlock.getPursuing();
		Assert.assertNotNull(persuing);
		Assert.assertTrue(persuing.getLines().size() == 1);
		Line line = persuing.getLines().get(0);
		Assert.assertNotNull(line.getLine());
		Assert.assertTrue(line.getLine().startsWith("de.ludwig.2"));
		Assert.assertNotNull(persuing.getPursuing());
		Assert.assertNotNull(persuing.getPreceding());

		persuing = persuing.getPursuing();
		Assert.assertNotNull(persuing);
		Assert.assertTrue(persuing.getLines().size() == 1);
		line = persuing.getLines().get(0);
		Assert.assertNotNull(line.getLine());
		Assert.assertTrue(line.getLine().startsWith("de.ludwig.3"));
		Assert.assertNull(persuing.getPursuing());
		Assert.assertNotNull(persuing.getPreceding());
	}

	@Test
	public void merge() throws FileNotFoundException, IOException
	{
		propertyFileHandle = I18nFileCreator.start().addKeyValue("de.ludwig", "test")
				.addKeyValue("de.ludwig.2", "blah").addKeyValue("de.ludwig.3", "blub").end();
		final PropertyFile pf = new PropertyFile(propertyFileHandle, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());

		pf.getStartingBlock().explode();
		Block startingBlock = pf.getStartingBlock();
		startingBlock.merge(startingBlock.getPursuing());
		Assert.assertTrue(startingBlock.getLines().size() == 2);
		Assert.assertNotNull(startingBlock.getPursuing());
		startingBlock.merge(startingBlock.getPursuing());
		Assert.assertTrue(startingBlock.getLines().size() == 3);
		Assert.assertNull(startingBlock.getPursuing());
	}

	@Test
	public void isCommentBlock() throws FileNotFoundException, IOException
	{
		PropertiesWriter.attachCommentsWithEmptyLineCount.change("0");
		Block k1 = new Block(BlockType.KEYVALUE, "de=1");
		Block c1 = new Block(BlockType.COMMENT, "# hello", "# hello2", "# hello3");
		c1.insertBefore(k1);
		Block commentAttached = PropertyFile.isCommentAttached(k1);
		Assert.assertNotNull(commentAttached);
		Assert.assertTrue(commentAttached == c1);

		commentAttached = null;
		commentAttached = PropertyFile.isCommentAttached(c1);
		Assert.assertTrue(commentAttached != null);
		Assert.assertTrue(commentAttached == k1);

		c1.detach();
		Block e1 = new Block(BlockType.BLANK, "");
		e1.insert(c1, k1);
		commentAttached = null;
		commentAttached = PropertyFile.isCommentAttached(k1);
		Assert.assertNull(commentAttached);
		commentAttached = PropertyFile.isCommentAttached(c1);
		Assert.assertNull(commentAttached);
		commentAttached = PropertyFile.isCommentAttached(e1);
		Assert.assertNull(commentAttached);

		PropertiesWriter.attachCommentsWithEmptyLineCount.change("1");
		commentAttached = null;
		commentAttached = PropertyFile.isCommentAttached(k1);
		Assert.assertNotNull(commentAttached);
		commentAttached = PropertyFile.isCommentAttached(c1);
		Assert.assertNotNull(commentAttached);
		commentAttached = PropertyFile.isCommentAttached(e1);
		Assert.assertNull(commentAttached);
	}
}
