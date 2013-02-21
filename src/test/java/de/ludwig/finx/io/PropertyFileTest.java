package de.ludwig.finx.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.Language;
import de.ludwig.finx.io.Block.BlockType;

/**
 * @author Daniel
 * 
 */
public class PropertyFileTest
{
	File end = null;

	@Test
	public void readKeyValue() throws IOException
	{
		end = I18nFileCreator.start().addKeyValue("de.ludwig", "test").addKeyValue("de.luwdig.sub", "test").end();

		PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());
	}

	@Test
	public void readComment() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().addSimpleComment("a comment").addSimpleComment(" a Comment with space")
				.addSimpleComment("!!!").end();

		PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
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
		end = I18nFileCreator.start().addEmptyLine(1, 4).addEmptyLine(1).end();

		PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());
	}

	@Test
	public void readReallyBlankLine() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().addEmptyLine(1).end();

		PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(1, pf.size());
	}

	@Test
	public void readMerged() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().addSimpleComment("test comment").addKeyValue("de.ludwig", "some value")
				.addEmptyLine(1, 2).end();

		final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		final Iterator<Block> iterator = pf.iterator();
		Assert.assertTrue(iterator.hasNext());
		Block startingBlock = iterator.next();
		Assert.assertNotNull(startingBlock);
		Assert.assertEquals(3, pf.size());

		Assert.assertNull(startingBlock.getPreceding());
		Assert.assertNotNull(startingBlock.getPersuing());
		Assert.assertEquals(BlockType.COMMENT, startingBlock.getType());

		Assert.assertTrue(iterator.hasNext());
		startingBlock = iterator.next();
		Assert.assertNotNull(startingBlock.getPreceding());
		Assert.assertNotNull(startingBlock.getPersuing());
		Assert.assertEquals(BlockType.KEYVALUE, startingBlock.getType());

		Assert.assertTrue(iterator.hasNext());
		startingBlock = iterator.next();
		Assert.assertNull(startingBlock.getPersuing());
		Assert.assertNotNull(startingBlock.getPreceding());
		Assert.assertEquals(BlockType.BLANK, startingBlock.getType());
	}

	@Test
	public void explode() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().addKeyValue("de.ludwig", "test").addKeyValue("de.ludwig.2", "blah")
				.addKeyValue("de.ludwig.3", "blub").end();
		final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());
		Assert.assertNull(pf.getStartingBlock().getPersuing());

		final Block startingBlock = pf.getStartingBlock();
		Assert.assertTrue(startingBlock.getLines().size() == 3);

		startingBlock.explode();
		Assert.assertTrue(startingBlock.getLines().size() == 1);

		Block persuing = startingBlock.getPersuing();
		Assert.assertNotNull(persuing);
		Assert.assertTrue(persuing.getLines().size() == 1);
		Line line = persuing.getLines().get(0);
		Assert.assertNotNull(line.getLine());
		Assert.assertTrue(line.getLine().startsWith("de.ludwig.2"));
		Assert.assertNotNull(persuing.getPersuing());
		Assert.assertNotNull(persuing.getPreceding());

		persuing = persuing.getPersuing();
		Assert.assertNotNull(persuing);
		Assert.assertTrue(persuing.getLines().size() == 1);
		line = persuing.getLines().get(0);
		Assert.assertNotNull(line.getLine());
		Assert.assertTrue(line.getLine().startsWith("de.ludwig.3"));
		Assert.assertNull(persuing.getPersuing());
		Assert.assertNotNull(persuing.getPreceding());
	}

	@Test
	public void merge() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().addKeyValue("de.ludwig", "test").addKeyValue("de.ludwig.2", "blah")
				.addKeyValue("de.ludwig.3", "blub").end();
		final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());

		pf.getStartingBlock().explode();
		Block startingBlock = pf.getStartingBlock();
		startingBlock.merge(startingBlock.getPersuing());
		Assert.assertTrue(startingBlock.getLines().size() == 2);
		Assert.assertNotNull(startingBlock.getPersuing());
		startingBlock.merge(startingBlock.getPersuing());
		Assert.assertTrue(startingBlock.getLines().size() == 3);
		Assert.assertNull(startingBlock.getPersuing());
	}

	@Test
	public void insertIntoEmpty() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().end();
		final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", "DE", "unter-knoten");
		insertThis.update("EN", "sub-node");
		pf.insert(insertThis);
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(1, lines.size());

		final Line line = lines.get(0);
		// we expect the value in german language because the PropertyFile was constructed with a
		// german locale
		Assert.assertEquals("de.ludwig.sub=unter-knoten", line.getLine());
	}

	@Test
	public void insertIntoFull() throws FileNotFoundException, IOException
	{
		end = I18nFileCreator.start().addKeyValue("de.ludwig", "top-node").end();
		final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMANY));
		Assert.assertNotNull(pf.getStartingBlock());

		I18nNode insertThis = I18nNode.create("de.ludwig.sub", "DE", "sub-node");
		pf.insert(insertThis);
		Block startingBlock = pf.getStartingBlock();
		Assert.assertNotNull(startingBlock);
		List<Line> lines = startingBlock.getLines();
		Assert.assertNotNull(lines);
		Assert.assertEquals(2, lines.size());
	}

	@After
	public void deleteFile()
	{
		if (end == null)
			return;

		boolean delete = end.delete();
		if (delete == false)
			throw new RuntimeException("unable to delete tmp-testfile");
	}
}
