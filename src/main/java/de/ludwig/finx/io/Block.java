package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.List;

import de.ludwig.finx.ApplicationCodingException;

/**
 * A Block object represents one or more lines inside a property file that are of one type (s.
 * BlockType) and if all of these lines following after each other. That means between these lines
 * are no empty lines or blocks of a different type
 * 
 * @author Daniel
 * 
 */
class Block
{
	private List<Line> lines = new ArrayList<Line>();

	private final BlockType type;

	private Block preceding;

	private Block persuing;

	/**
	 * 
	 * @param dimension
	 *            TODO that does not seems very usefull. Why not save the dimension at this object?
	 *            Other question, is it really necessary?
	 * @param rawLines
	 * @param type
	 */
	public Block(BlockDimension dimension, List<String> rawLines, BlockType type)
	{
		this.type = type;

		if (rawLines == null || rawLines.isEmpty())
			throw new ApplicationCodingException("block with no lines is not allowed");

		for (int i = dimension.getFirst(); i <= dimension.getLast(); i++) {
			final String raw = rawLines.get(i);
			lines.add(new Line(i, raw));
		}
	}

	private Block(final List<Line> lines, BlockType type)
	{
		this.type = type;
		this.lines = lines;
	}

	/**
	 * Every line of this block is moved to an own Block instance. This instance becomes the head of
	 * the resulting list of Block Elements. No sorting is applied!
	 */
	public final void explode()
	{
		if (lines.isEmpty() || lines.size() == 1) {
			return;
		}

		List<Line> tmp = lines;

		lines = new ArrayList<Line>();
		lines.add(tmp.get(0));

		final Block startingBlock = this;
		if (persuing != null)
			persuing.splitBefore();
		final Block endBlock = persuing;
		Block previous = this;
		for (int i = 1; i < tmp.size(); i++) {
			final List<Line> newLine = new ArrayList<Line>();
			newLine.add(tmp.get(i));
			final Block newBlock = new Block(newLine, type);

			if (i == 1 && tmp.size() > 2) { // start
				newBlock.concat(startingBlock, null);
			} else if (i == tmp.size() - 1) { // end
				newBlock.concat(previous, endBlock);
			} else {
				newBlock.concat(previous, null);
			}

			previous = newBlock;
		}
	}

	/**
	 * Merges two blocks resulting in one single block. Only blocks of same type can be merged.
	 * 
	 * @param mergeThis
	 *            this block is going to be merged into this block. His persuing block will be
	 *            attached to this block as persuing block.
	 */
	public void merge(final Block mergeThis)
	{
		if (type.equals(mergeThis.getType()) == false)
			throw new ApplicationCodingException("Blocks of different types cannot be merged!");

		Block preceding = mergeThis.getPreceding();
		if (preceding != null)
			preceding.concat(preceding.preceding, mergeThis.persuing);

		this.getLines().addAll(mergeThis.getLines());
	}

	/**
	 * Concats this block to the given blocks
	 * 
	 * @param preceding
	 *            The block before this block. If null this block will not have a preceding block!
	 * @param persuing
	 *            The block after this block. If null this block will not have a persuing block!
	 */
	public final void concat(Block preceding, Block persuing)
	{
		// 0 0 0 1 0 0 0 2 0 0 3 : imagine you want to concat block 1 with 2 and
		// 3. In this case the two blocks between 2 and 3 are not connected
		// anymore to other blocks. so thats why we check if the two blocks as
		// params are conntected to each other, to prevent inconsistent state.
		if (preceding != null && persuing != null && persuing.getPreceding() != null
				&& persuing.getPreceding() != preceding)
			throw new ApplicationCodingException(
					"trying to concat block with two blocks that are not connected to each other");

		// remove old connections or refresh them
		if (this.preceding != null && this.persuing != null) {
			this.preceding.persuing = this.persuing;
			this.persuing.preceding = this.preceding;
		} else if (this.preceding != null) {
			this.preceding.persuing = null;
		} else if (this.persuing != null) {
			this.persuing.preceding = null;
		}

		// now attach this block to the given blocks
		this.preceding = preceding;
		if (preceding != null)
			preceding.persuing = this;

		this.persuing = persuing;
		if (persuing != null)
			persuing.preceding = this;
	}

	public final void insertAfter(Block afterThis)
	{
		concat(afterThis, afterThis.persuing);
	}

	public final void insertBefore(Block beforeThis)
	{
		concat(beforeThis.preceding, beforeThis);
	}

	public final Block splitAfter()
	{
		Block after = persuing;
		persuing = null;
		return after;
	}

	public final Block splitBefore()
	{
		Block before = preceding;
		preceding = null;
		return before;
	}

	/**
	 * remove this block from the chain of block where it belongs to and concats (if necessary) the
	 * preceding and persuing blocks of this one.
	 */
	public void detach()
	{
		if (preceding != null && persuing != null) {
			preceding.concat(null, persuing);
		} else if (preceding != null) {
			preceding.persuing = null;
		} else if (persuing != null) {
			persuing.preceding = null;
		}

		preceding = null;
		persuing = null;
	}

	/**
	 * @return the lines
	 */
	public List<Line> getLines()
	{
		return lines;
	}

	/**
	 * @return the type
	 */
	public BlockType getType()
	{
		return type;
	}

	/**
	 * @return the preceding
	 */
	public Block getPreceding()
	{
		return preceding;
	}

	/**
	 * @return the persuing
	 */
	public Block getPersuing()
	{
		return persuing;
	}
}