package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniel
 * 
 */
public class BlockTest
{
	@Test
	public void concat()
	{
		final Block b1 = new EmptyBlock(1);
		final Block b2 = new EmptyBlock(1);
		final Block b3 = new EmptyBlock(1);
		final Block b4 = new EmptyBlock(1);

		b1.concat(null, b2);
		b2.concat(b1, b3);
		b4.concat(b3, null);

		Assert.assertNotNull(b1.getPersuing());
		Assert.assertNull(b1.getPreceding());

		Assert.assertNotNull(b2.getPersuing());
		Assert.assertNotNull(b2.getPreceding());

		Assert.assertNotNull(b3.getPersuing());
		Assert.assertNotNull(b3.getPreceding());

		Assert.assertNotNull(b4.getPreceding());
		Assert.assertNull(b4.getPersuing());

		Assert.assertTrue(b1.getPersuing() == b2);
		Assert.assertTrue(b2.getPreceding() == b1);
		Assert.assertTrue(b2.getPersuing() == b3);
		Assert.assertTrue(b3.getPreceding() == b2);
		Assert.assertTrue(b3.getPersuing() == b4);
		Assert.assertTrue(b4.getPreceding() == b3);
	}

	@Test
	public void insertAfter()
	{
		final Block b1 = new EmptyBlock(1);
		final Block b2 = new EmptyBlock(1);
		final Block b3 = new EmptyBlock(1);
		final Block b4 = new EmptyBlock(1);

		b1.concat(null, b2);
		b2.concat(b1, b3);
		b4.concat(b3, null);

		b1.insertAfter(b2);
		Assert.assertNull(b2.getPreceding());
		Assert.assertNotNull(b2.getPersuing());
		Assert.assertTrue(b2.getPersuing() == b1);
		Assert.assertTrue(b1.getPreceding() == b2);
		Assert.assertTrue(b1.getPersuing() == b3);
		Assert.assertNotNull(b3.getPreceding());
		Assert.assertTrue(b3.getPreceding() == b1);

		// actual order: b2 b1 b3 b4
		b4.insertAfter(b1);
		Assert.assertNotNull(b1.getPersuing());
		Assert.assertNotNull(b4.getPreceding());
		Assert.assertNotNull(b4.getPersuing());
		Assert.assertTrue(b4.getPreceding() == b1);
		Assert.assertTrue(b4.getPersuing() == b3);
	}

	@Test
	public void insertBefore()
	{
		final Block b1 = new EmptyBlock(1);
		final Block b2 = new EmptyBlock(1);
		final Block b3 = new EmptyBlock(1);
		final Block b4 = new EmptyBlock(1);

		b1.concat(null, b2);
		b2.concat(b1, b3);
		b4.concat(b3, null);

		b3.insertBefore(b2);
		Assert.assertNotNull(b3.getPersuing());
		Assert.assertNotNull(b3.getPreceding());
		Assert.assertTrue(b3.getPreceding() == b1);
		Assert.assertTrue(b3.getPersuing() == b2);
		Assert.assertTrue(b1.getPersuing() == b3);
		Assert.assertTrue(b2.getPreceding() == b3);

		// actual order b1 b3 b2 b4
		b4.insertBefore(b1);
		Assert.assertNull(b4.getPreceding());
		Assert.assertNotNull(b4.getPersuing());
		Assert.assertTrue(b4.getPersuing() == b1);
		Assert.assertTrue(b1.getPreceding() == b4);
	}

	@Test
	public void explode()
	{
		List<String> lines = new ArrayList<>();
		lines.add("de.ludwig=1");
		Block b1 = new Block(new BlockDimension(0, lines.size() - 1), lines, BlockType.KEYVALUE);
		Block b2 = new EmptyBlock(2);
		b1.insertBefore(b2);

		Assert.assertTrue(b1.getPersuing() == b2);
		Assert.assertTrue(b2.getPreceding() == b1);

		b1.explode();
		List<Line> lines2 = b1.getLines();
		Assert.assertNotNull(lines2);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(1, lines2.size());

		Block b1a = b1.getPersuing();
		Assert.assertEquals(BlockType.BLANK, b1a.getType());
		Assert.assertTrue(b1a == b2);
		Assert.assertNull(b1a.getPersuing());
		Assert.assertTrue(b1a.getPreceding() == b1);
	}

	@Test
	public void explode2()
	{
		List<String> lines = new ArrayList<>();
		lines.add("de.ludwig=2");
		lines.add("de.ludwig.test=2");
		Block b1 = new Block(new BlockDimension(0, lines.size() - 1), lines, BlockType.KEYVALUE);
		Block b2 = new EmptyBlock(2);
		b1.insertBefore(b2);

		Assert.assertTrue(b1.getPersuing() == b2);
		Assert.assertTrue(b2.getPreceding() == b1);

		b1.explode();
		List<Line> lines2 = b1.getLines();
		Assert.assertNotNull(lines2);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(1, lines2.size());

		Block b1a = b1.getPersuing();
		Assert.assertEquals(BlockType.KEYVALUE, b1a.getType());
		lines2 = b1a.getLines();
		Assert.assertNotNull(lines2);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(1, lines2.size());
		Assert.assertNotNull(b1a.getPersuing());
		Assert.assertEquals(BlockType.BLANK, b1a.getPersuing().getType());
	}

	@Test
	public void explode3()
	{
		List<String> lines = new ArrayList<>();
		lines.add("de.ludwig=3");
		lines.add("de.ludwig.test=3");
		lines.add("de.ludwig.blah=3");
		Block b1 = new Block(new BlockDimension(0, lines.size() - 1), lines, BlockType.KEYVALUE);
		Block b2 = new EmptyBlock(2);
		b1.insertBefore(b2);

		Assert.assertTrue(b1.getPersuing() == b2);
		Assert.assertTrue(b2.getPreceding() == b1);

		b1.explode();
		List<Line> lines2 = b1.getLines();
		Assert.assertNotNull(lines2);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(1, lines2.size());

		Block b1a = b1.getPersuing();
		Assert.assertEquals(BlockType.KEYVALUE, b1a.getType());
		lines2 = b1a.getLines();
		Assert.assertNotNull(lines2);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(1, lines2.size());

		Block b1b = b1a.getPersuing();
		Assert.assertNotNull(b1b);
		Assert.assertEquals(BlockType.KEYVALUE, b1b.getType());
		lines2 = b1b.getLines();
		Assert.assertNotNull(lines2);
		Assert.assertFalse(lines.isEmpty());
		Assert.assertEquals(1, lines2.size());
		Assert.assertNotNull(b1b.getPersuing());
		Assert.assertEquals(BlockType.BLANK, b1b.getPersuing().getType());
	}
}
