package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.io.PropertyFileUtils;

/**
 * @author Daniel
 *
 */
public class PropertyFileUtilsTest
{
	@Test
	public void testCommentBlocks() {
		final List<String> lines = new ArrayList<String>();
		lines.add("test=kfdjf");
		lines.add("# some comment");

		final List<Integer> commentBlocks = PropertyFileUtils.commentBlocks(lines);
		Assert.assertNotNull(commentBlocks);
		Assert.assertEquals(2, commentBlocks.size());
		Assert.assertEquals(1, commentBlocks.get(0).intValue());
		Assert.assertEquals(1, commentBlocks.get(1).intValue());
	}
	
	@Test
	public void testCommentBlocks2() {
		final List<String> lines = new ArrayList<String>();
		lines.add("##");
		lines.add("# multiline comment");
		lines.add("# nonsens");
		lines.add("##");
		lines.add("hello=test");
		
		final List<Integer> commentBlocks = PropertyFileUtils.commentBlocks(lines);
		Assert.assertNotNull(commentBlocks);
		Assert.assertEquals(2, commentBlocks.size());
		Assert.assertEquals(0, commentBlocks.get(0).intValue());
		Assert.assertEquals(3, commentBlocks.get(1).intValue());
	}
	
	@Test
	public void testCommentBlocks3() {
		final List<String> lines = new ArrayList<String>();
		lines.add("key=blah");
		lines.add("#");
		lines.add("#");
		lines.add("key.de=kdfj");
		lines.add("#");
		
		final List<Integer> commentBlocks = PropertyFileUtils.commentBlocks(lines);
		Assert.assertNotNull(commentBlocks);
		Assert.assertEquals(4, commentBlocks.size());
		Assert.assertEquals(1, commentBlocks.get(0).intValue());
		Assert.assertEquals(2, commentBlocks.get(1).intValue());
		Assert.assertEquals(4, commentBlocks.get(2).intValue());
		Assert.assertEquals(4, commentBlocks.get(3).intValue());
	}
	
	@Test
	public void testCommentBlock4() {
		final List<String> lines = new ArrayList<String>();
		lines.add("##");
		lines.add("key=blah");
		lines.add("#");
		lines.add("#");
		lines.add("key.de=kdfj");
		lines.add("#");
		
		final List<Integer> commentBlocks = PropertyFileUtils.commentBlocks(lines);
		Assert.assertNotNull(commentBlocks);
		Assert.assertEquals(6, commentBlocks.size());
		Assert.assertEquals(0, commentBlocks.get(0).intValue());
		Assert.assertEquals(0, commentBlocks.get(1).intValue());
		Assert.assertEquals(2, commentBlocks.get(2).intValue());
		Assert.assertEquals(3, commentBlocks.get(3).intValue());
		Assert.assertEquals(5, commentBlocks.get(4).intValue());
		Assert.assertEquals(5, commentBlocks.get(5).intValue());
	}
	
	@Test
	public void testCommentBlock5() {
		final List<String> lines = new ArrayList<String>();
		lines.add("##");
		lines.add("key=blah");
		lines.add("  ");
		lines.add("#");
		lines.add("#");
		lines.add("key.de=kdfj");
		lines.add("#");
		
		final List<Integer> commentBlocks = PropertyFileUtils.commentBlocks(lines);
		Assert.assertNotNull(commentBlocks);
		Assert.assertEquals(6, commentBlocks.size());
		Assert.assertEquals(0, commentBlocks.get(0).intValue());
		Assert.assertEquals(0, commentBlocks.get(1).intValue());
		Assert.assertEquals(3, commentBlocks.get(2).intValue());
		Assert.assertEquals(4, commentBlocks.get(3).intValue());
		Assert.assertEquals(6, commentBlocks.get(4).intValue());
		Assert.assertEquals(6, commentBlocks.get(5).intValue());
	}
}
