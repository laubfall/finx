package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.io.PropertyKeyOrderSetting.PropertyKeyOrder;

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

	private Block pursuing;

	/**
	 * 
	 * @param dimension
	 *            actually this is only used by {@link PropertyFile} for processing the
	 *            Properties-File
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

	public Block(BlockType type, String... lines)
	{
		// TODO does not work if lines is not set (varg!)
		this(new BlockDimension(0, lines.length - 1), Arrays.asList(lines), type);
	}

	public Block(final List<Line> lines, BlockType type)
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
		Block endBlock = null;
		if (pursuing != null)
			endBlock = splitAfter();

		Block previous = this;
		for (int i = 1; i < tmp.size(); i++) {
			final List<Line> newLine = new ArrayList<Line>();
			newLine.add(tmp.get(i));
			final Block newBlock = new Block(newLine, type);

			if (i == 1 && tmp.size() > 2) { // start
				newBlock.insert(startingBlock, null);
			} else if (i == tmp.size() - 1) { // end
				newBlock.insert(previous, endBlock);
			} else {
				newBlock.insert(previous, null);
			}

			previous = newBlock;
		}
	}

	/**
	 * Merges two blocks resulting in one single block. Only blocks of same type can be merged.
	 * 
	 * @param mergeThis
	 *            this block is going to be merged into this block. To do this, the block is
	 *            detached, and his lines are put into the collection of lines of this block
	 */
	public final void merge(final Block mergeThis)
	{
		if (type.equals(mergeThis.getType()) == false)
			throw new ApplicationCodingException("Blocks of different types cannot be merged!");

		mergeThis.detach();

		this.getLines().addAll(mergeThis.getLines());
	}

	/**
	 * Inserts this block to the given blocks. Insertion can only be successful if the two blocks
	 * where we want to insert this block between are connected to each other.
	 * 
	 * @param preceding
	 *            The block before this block. If null this block will not have a preceding block!
	 * @param persuing
	 *            The block after this block. If null this block will not have a persuing block!
	 */
	public final void insert(Block preceding, Block persuing)
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
		if (this.preceding != null && this.pursuing != null) {
			this.preceding.pursuing = this.pursuing;
			this.pursuing.preceding = this.preceding;
		} else if (this.preceding != null) {
			this.preceding.pursuing = null;
		} else if (this.pursuing != null) {
			this.pursuing.preceding = null;
		}

		// now attach this block to the given blocks
		this.preceding = preceding;
		if (preceding != null) {
			preceding.pursuing = this;
		}

		this.pursuing = persuing;
		if (persuing != null) {
			persuing.preceding = this;
		}
	}

	public final void concatTailToHead(final Block head)
	{
		Block tail = this.tail();
		Block realHead = head.head();
		tail.pursuing = realHead;
		realHead.preceding = tail;
	}

	// 0 0 0 0 1 2 0 0 - 3 -
	public final void insertAfter(Block afterThis)
	{
		if (preceding == afterThis) {
			return;
		}
		if (afterThis.pursuing == null && afterThis.preceding == null) {
			afterThis.pursuing = this;
			if (this.preceding != null) {
				this.preceding.pursuing = afterThis;
			}
			afterThis.preceding = this.preceding;
			this.preceding = afterThis;
			return;
		}
		insert(afterThis, afterThis.pursuing);
	}

	/**
	 * WARNING: this method does not work as expected if you put in a block that is attached to
	 * other blocks! the chain of blocks gets broken!
	 * 
	 * @param beforeThis
	 */
	public final void insertBefore(Block beforeThis)
	{
		// don't insert it if it already is in the right position
		if (pursuing == beforeThis) {
			return;
		}
		if (beforeThis.preceding == null && beforeThis.pursuing == null) {
			beforeThis.preceding = this;
			beforeThis.pursuing = this.pursuing;
			if (this.pursuing != null) {
				this.pursuing.preceding = beforeThis;
			}
			this.pursuing = beforeThis;
			return;
		}

		// TODO this is the dangerous call if beforeThis is only attached to one side either
		// preceding or pursuing. The side that is not connected will destroy the block-chain of
		// this-block
		insert(beforeThis.preceding, beforeThis);
	}

	public final Block splitAfter()
	{
		if (pursuing == null)
			return null;
		Block after = pursuing;
		pursuing = null;
		after.preceding = null;
		return after;
	}

	/**
	 * 
	 * Splits the chain of block before this block. That means that the connection between this
	 * block and its preceding block will be decoupled.
	 * 
	 * @return the block before this block, the preceding one. Null if there is no preceding block.
	 */
	public final Block splitBefore()
	{
		if (preceding == null)
			return null;
		Block before = preceding;
		preceding = null;
		before.pursuing = null;
		return before;
	}

	public final Block head()
	{
		Block head = preceding;
		Block result = null;
		while (head != null) {
			result = head;
			head = head.preceding;
		}
		return result == null ? this : result;
	}

	public final Block tail()
	{
		Block tail = pursuing;
		Block result = null;
		while (tail != null) {
			result = tail;
			tail = tail.pursuing;
		}
		return result == null ? this : result;
	}

	/**
	 * remove this block from the chain of block where it belongs to and concats (if necessary) the
	 * preceding and persuing blocks of this one.
	 * 
	 * @return the block we detached this block from.
	 */
	public final Block detach()
	{
		Block returnThis = null;
		if (preceding != null && pursuing != null) {
			preceding.insert(null, pursuing);
			returnThis = preceding;
		} else if (preceding != null) {
			preceding.pursuing = null;
			returnThis = preceding;
		} else if (pursuing != null) {
			pursuing.preceding = null;
			returnThis = pursuing;
		}

		preceding = null;
		pursuing = null;
		return returnThis;
	}

	public final Block detachUp(final Block upperBound)
	{
		Validate.notNull(upperBound);
		Block prec = preceding;
		if (prec == null) {
			throw new ApplicationCodingException("no preceding block, cannot detach to upperbound");
		}
		boolean detached = false;
		Block resultingChain = null;
		while (prec != null) {
			if (prec == upperBound) {
				final Block headBlock = prec.splitBefore();
				final Block tailBlock = this.splitAfter();
				resultingChain = stitch(headBlock, tailBlock);
				detached = true;
				break;
			}
			prec = prec.preceding;
		}

		if (detached == false) {
			throw new ApplicationCodingException("upperbound not found in block hierarchy");
		}

		return resultingChain;
	}

	public final Block detachDown(final Block lowerBound)
	{
		Validate.notNull(lowerBound);
		Block pers = pursuing;
		if (pers == null) {
			throw new ApplicationCodingException("no persuing block, cannot detach to lowerbound");
		}
		boolean detached = false;
		Block resultingChain = null;
		while (pers != null) {
			if (pers == lowerBound) {
				final Block tailBlock = pers.splitAfter();
				final Block headBlock = this.splitBefore();
				// headBlock.insert(headBlock.preceding, tailBlock);
				resultingChain = stitch(headBlock, tailBlock);
				detached = true;
				break;
			}
			pers = pers.pursuing;
		}

		if (detached == false) {
			throw new ApplicationCodingException("lowerbound not found in block hierarchy");
		}

		return resultingChain;
	}

	public final void sortLines()
	{
		final PropertyKeyOrder keyOrder = PropertiesWriter.keyOrder.setting().getKeyOrder();
		if (keyOrder.equals(PropertyKeyOrder.ASC)) {
			Collections.sort(lines);
		} else if (keyOrder.equals(PropertyKeyOrder.DESC)) {
			Collections.sort(lines);
			Collections.reverse(lines);
		}
	}

	/**
	 * Null-safe Method that concats two Blocks together.
	 * 
	 * This method is private because it does not check if it is possible / save to insert the
	 * preceding block of param headBlock between headBlock and tailBlock.
	 * 
	 * @param headBlock
	 *            Optional.
	 * @param tailBlock
	 *            Optional.
	 * @return the first block in the resulting chain. It is only the tailBlock param if the
	 *         headBlock param is null.
	 */
	private Block stitch(final Block headBlock, final Block tailBlock)
	{
		if (headBlock == null && tailBlock != null) {
			return tailBlock;
		} else if (headBlock != null && tailBlock == null) {
			return headBlock;
		} else if (headBlock == null && tailBlock == null) {
			throw new ApplicationCodingException("both blocks are null, unable to stitch null-values together");
		}

		headBlock.insert(headBlock.preceding, tailBlock);

		return headBlock;
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
	public Block getPursuing()
	{
		return pursuing;
	}
}