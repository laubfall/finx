package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.ApplicationCodingException;

/**
 * @author Daniel
 * 
 */
public class BlockTest
{
	@Test
	public void insert()
	{
		final Block b1 = new EmptyBlock(1);
		final Block b2 = new EmptyBlock(1);
		final Block b3 = new EmptyBlock(1);
		final Block b4 = new EmptyBlock(1);

		b1.insert(null, b2);
		b2.insert(b1, b3);
		b4.insert(b3, null);

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

		b1.insert(null, b2);
		b2.insert(b1, b3);
		b4.insert(b3, null);

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

		b1.insert(null, b2);
		b2.insert(b1, b3);
		b4.insert(b3, null);

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

	@Test
	public void detachUp()
	{
		final Block[] b = createBlockChain();

		Block detachUp = b[3].detachUp(b[1]);
		Assert.assertNotNull(detachUp);
		Assert.assertTrue(detachUp.getPersuing() == b[4]);
		Assert.assertTrue(b[0] == detachUp);
		Assert.assertNull(b[3].getPersuing());
		Assert.assertNull(b[1].getPreceding());
	}

	@Test
	public void detachUpNoPreceding()
	{
		final Block[] b = createBlockChain();
		Block detachUp = b[1].detachUp(b[0]);
		Assert.assertNotNull(detachUp);
		Assert.assertTrue(detachUp == b[2]);
		Assert.assertTrue(detachUp.head() == detachUp);
		Assert.assertTrue(detachUp.tail() == b[4]);
	}

	@Test
	public void detachDown()
	{
		final Block[] b = createBlockChain();

		Block detachDown = b[1].detachDown(b[3]);
		Assert.assertNotNull(detachDown);
		Assert.assertTrue(detachDown.getPersuing() == b[4]);
		Assert.assertTrue(b[0] == detachDown);
		Assert.assertNull(b[3].getPersuing());
		Assert.assertNull(b[1].getPreceding());
	}

	@Test
	public void detachDownNoPreceding()
	{
		final Block[] b = createBlockChain();
		Block detachDown = b[3].detachDown(b[4]);
		Assert.assertNotNull(detachDown);
		Assert.assertTrue(detachDown == b[2]);
		Assert.assertTrue(detachDown.head() == b[0]);
		Assert.assertTrue(detachDown.tail() == b[2]);
	}

	@Test
	public void detachFailure()
	{
		final Block[] b = createBlockChain();
		try {
			b[1].detachUp(b[3]);
			Assert.fail();
		} catch (ApplicationCodingException e) {
			// noop
		}

		try {
			b[3].detachDown(b[1]);
			Assert.fail();
		} catch (ApplicationCodingException e) {
			// noop
		}

		try {
			b[2].detachDown(b[2]);
			Assert.fail();
		} catch (ApplicationCodingException e) {
			// noop
		}
	}

	@Test
	public void head()
	{
		final Block[] b = createBlockChain();
		Block head = b[4].head();
		Assert.assertNotNull(head);
		Assert.assertTrue(head == b[0]);

		head = b[2].head();
		Assert.assertNotNull(head);
		Assert.assertTrue(head == b[0]);

		head = b[0].head();
		Assert.assertNotNull(head);
		Assert.assertTrue(head == b[0]);
	}

	@Test
	public void tail()
	{
		final Block[] b = createBlockChain();
		Block tail = b[0].tail();
		Assert.assertNotNull(tail);
		Assert.assertTrue(tail == b[4]);

		tail = b[2].tail();
		Assert.assertNotNull(tail);
		Assert.assertTrue(tail == b[4]);

		tail = b[4].tail();
		Assert.assertNotNull(tail);
		Assert.assertTrue(tail == b[4]);
	}

	/**
	 * @return
	 */
	private Block[] createBlockChain()
	{
		final Block b[] = new Block[] { new Block(BlockType.KEYVALUE, ""), new EmptyBlock(1),
				new Block(BlockType.KEYVALUE, ""), new EmptyBlock(1), new Block(BlockType.KEYVALUE, "") };

		b[1].insert(b[0], b[2]);
		b[3].insert(b[2], b[4]);
		return b;
	}
}
