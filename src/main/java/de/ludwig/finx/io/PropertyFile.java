package de.ludwig.finx.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.io.Block.BlockType;
import de.ludwig.finx.io.PropertyKeyOrderSetting.PropertyKeyOrder;

/**
 * @author Daniel
 * 
 */
public class PropertyFile implements Iterable<Block>
{
	private Block startingBlock;

	private Locale language;

	public PropertyFile(final File i18nRes, final Locale language) throws FileNotFoundException, IOException
	{
		this(IOUtils.readLines(new FileInputStream(i18nRes)));
		this.language = language;
	}

	private PropertyFile(final List<String> lines)
	{
		List<BlockDimension> commentBlocks = commentBlocks(lines);
		List<BlockDimension> keyValueBlocks = keyValueBlocks(lines);
		List<BlockDimension> blankBlocks = blankBlocks(lines);

		// merge the three block-lists (natural ordering)
		final List<BlockDimension> allBlocks = new ArrayList<BlockDimension>();
		allBlocks.addAll(commentBlocks);
		allBlocks.addAll(keyValueBlocks);
		allBlocks.addAll(blankBlocks);

		Collections.sort(allBlocks);

		List<Block> blocks = new ArrayList<Block>();

		// initialize the relevant Block-Types
		for (int i = 0; i < allBlocks.size(); i++)
		{
			final BlockDimension dimension = allBlocks.get(i);
			BlockType type = null;
			if (commentBlocks.contains(dimension))
			{
				type = BlockType.COMMENT;
			}

			if (keyValueBlocks.contains(dimension))
			{
				type = BlockType.KEYVALUE;
			}

			if (blankBlocks.contains(dimension))
			{
				type = BlockType.BLANK;
			}

			if (type == null)
			{
				throw new ApplicationCodingException("unable to determine Block-Type with dimensions of: " + dimension);
			}

			blocks.add(new Block(dimension, lines, type));
		}

		// now concat every block with its neighbours
		for (int i = 0; i < blocks.size(); i++)
		{
			Block block = blocks.get(i);
			if (i == 0)
			{
				startingBlock = block;
			}

			if (i + 1 < blocks.size() && i > 0)
			{
				block.concat(blocks.get(i - 1), blocks.get(i + 1));
			} else if (i + 1 == blocks.size() && i > 0)
			{
				block.concat(blocks.get(i - 1), null);
			} else if (i + 1 < blocks.size() && i == 0)
			{
				block.concat(null, blocks.get(i + 1));
			}
		}

		preFormat();
	}

	public int size()
	{
		int cnt = 0;
		final Iterator<Block> it = iterator();
		while (it.hasNext())
		{
			it.next();
			cnt++;
		}

		return cnt;
	}

	/**
	 * Inserts if necessary the given I18nNode with respect to all relevant
	 * Prettyprint-Settings e.g.: PropertiesWriter#preservePropertyLayout
	 * 
	 * @param insertThis
	 */
	public void insert(final I18nNode insertThis)
	{

	}

	/**
	 * Removes the given keyValue-Pair from the file
	 * 
	 * @param removeThis
	 */
	public void remove(final I18nNode removeThis)
	{

	}

	/**
	 * group all keyValues as specified by {@link PropertiesWriter#keyGrouping}
	 * and {@link PropertiesWriter#keyGroupSpace}
	 * 
	 */
	public void grouping()
	{
		// for(final Block b)
	}

	/**
	 * ordering means to order all blocks of type {@link BlockType#KEYVALUE}
	 */
	public void sort()
	{
		List<Block> sortedByKeyValues = blocksOfType(BlockType.KEYVALUE);
		Collections.sort(sortedByKeyValues, new Comparator<Block>() {
			@Override
			public int compare(Block o1, Block o2)
			{
				List<Line> lines1 = o1.getLines();
				List<Line> lines2 = o2.getLines();

				if (lines1.isEmpty() || lines1.size() != 1 || lines2.isEmpty() || lines2.size() != 1)
				{
					throw new ApplicationCodingException("unable to sort block, blocks have to be exploded first");
				}
				PropertyKeyOrder keyOrder = PropertiesWriter.keyOrder.setting().getKeyOrder();
				int res = lines1.get(0).getLine().compareTo(lines2.get(0).getLine());
				switch (keyOrder)
				{
				case ASC:
					return res;
				case DESC:
					return res * -1;
				default:
					return 0;
				}
			}
		});

		/**
		 * position behalten: - aktueller Block ist der sortierte Block (/) -
		 * der aktuelle Block ist der Block der in der Sortierung direkt nach
		 * dem sortierten Block folgt (/)
		 * 
		 * eins vorrücken: - aktueller Block ist ein Kommentar ohne Verbindung
		 * zu einem Key-Value-Block - aktueller Block ist ein Kommmentar mit
		 * Verbindung zu einem Key-Value-Block - aktueller Block ist ein
		 * Empty-Space-Block
		 */
		final ListIterator<Block> listIterator = sortedByKeyValues.listIterator();
		while (listIterator.hasNext())
		{
			Block keyValue = listIterator.next();
			Block checkAgainst = startingBlock;
			while (checkAgainst != null)
			{
				if (checkAgainst == keyValue)
				{
					break;
				}

				if (listIterator.hasNext())
				{
					Block next = listIterator.next();
					if (next == checkAgainst)
					{
						keyValue.detach();
						checkAgainst.concat(keyValue, checkAgainst.getPersuing());
					}
					listIterator.previous();
					break;
				}

				if (checkAgainst.getType().equals(BlockType.BLANK))
				{
					if (checkAgainst.getPersuing() == null)
					{
						keyValue.detach();
						checkAgainst.concat(checkAgainst.getPreceding(), keyValue);
						break;
					}

					checkAgainst = checkAgainst.getPersuing();
					continue;
				}

				if (isCommentAttached(checkAgainst) != null)
				{

				}

				checkAgainst = checkAgainst.getPersuing();
			}
		}
	}

	/**
	 * 
	 * @param block
	 *            check this block if it is attached as a comment to a
	 *            key-value-block or if it has an attached comment if it is a
	 *            key-value-Block
	 * @return the block where a comment is attachted to otherwise null
	 */
	private Block isCommentAttached(final Block block)
	{
		final Integer emptyLineCnt = PropertiesWriter.attachCommentsWithEmptyLineCount.setting();

		if (block.getType().equals(BlockType.COMMENT))
		{
			Block tmp = block.getPersuing();
			while (tmp != null)
			{
				switch (tmp.getType())
				{
				case BLANK:
				case COMMENT:
				case KEYVALUE:

				default:
					break;
				}
			}
		}

		if (block.getType().equals(BlockType.KEYVALUE))
		{
			int cntAgainstEmptyLineCnt = 0;
			Block tmp = block.getPreceding();
			while (tmp != null)
			{
				switch (tmp.getType())
				{
				default:
					tmp = tmp.getPreceding();
				case BLANK:
					if (cntAgainstEmptyLineCnt > emptyLineCnt)
						return null;
					cntAgainstEmptyLineCnt++;
				case COMMENT:
					return block;
				case KEYVALUE:
					return null;
				}
			}
		}

		return null;
	}

	/**
	 * After all modifcations are done to this PropertyFile Instance its time to
	 * write the result to a properties file. Use this method after all
	 * modifications are applied and to receive the data that shall be written
	 * to the properties file.
	 * 
	 * @return see description
	 */
	public List<String> filedata()
	{
		final List<String> result = new ArrayList<String>();

		for (final Block b : this)
		{
			for (Line l : b.getLines())
			{
				result.add(l.getLine());
			}
		}
		return result;
	}

	/**
	 * If Finx shall take care about the formatting of the properties file this
	 * method do any necessary formatting before further changes are made.
	 */
	private void preFormat()
	{
		if (PropertiesWriter.preservePropertyLayout.setting().equals(PropertyPreserveMode.NONE) == false)
		{
			return;
		}

		sort();
		grouping();
	}

	private List<BlockDimension> keyValueBlocks(final List<String> rawPropertyLines)
	{
		final Pattern pat = Pattern.compile(".*=.*");
		return blocks(pat, rawPropertyLines);
	}

	private List<BlockDimension> blankBlocks(final List<String> rawPropertyLines)
	{
		final Pattern pat = Pattern.compile("[ ]+|$");
		return blocks(pat, rawPropertyLines);
	}

	/**
	 * Searches for Comments in raw-Property-File Data.
	 * 
	 * @param rawPropertyLines
	 * @return a list of pairs of integers. Every pair describes a start and an
	 *         end point where comments occures. Size of this list is always 0
	 *         if % 2;
	 */
	private List<BlockDimension> commentBlocks(final List<String> rawPropertyLines)
	{
		final Pattern pat = Pattern.compile("\\#\\s*.*");
		return blocks(pat, rawPropertyLines);
	}

	private List<BlockDimension> blocks(final Pattern pat, List<String> rawPropertyLines)
	{
		int lineCount = 0;
		int lastMatchedLine = -1;
		int firstMatchedLine = -1;

		final List<BlockDimension> blocks = new ArrayList<BlockDimension>();

		boolean matched = false;
		boolean fetchedLastMatch = false;

		for (final String line : rawPropertyLines)
		{
			final Matcher match = pat.matcher(line);
			if (match.matches())
			{
				matched = true;
				if (lineCount == rawPropertyLines.size() - 1)
				{
					fetchedLastMatch = true;
				}

				if (firstMatchedLine == -1)
				{
					firstMatchedLine = lineCount;
					lastMatchedLine = lineCount;
				} else
				{
					lastMatchedLine = lineCount;
				}
			} else if (matched)
			{
				fetchedLastMatch = true;
			}

			if (fetchedLastMatch && matched)
			{
				blocks.add(new BlockDimension(firstMatchedLine, lastMatchedLine));
				lastMatchedLine = -1;
				firstMatchedLine = -1;
				matched = false;
				fetchedLastMatch = false;
			}

			lineCount++;
		}

		return blocks;
	}

	private List<Block> blocksOfType(final BlockType type)
	{
		final List<Block> blocks = new ArrayList<Block>();
		final Iterator<Block> blockIterator = iterator();
		while (blockIterator.hasNext())
		{
			Block next = blockIterator.next();
			if (next.getType().equals(type) == false)
				continue;

			blocks.add(next);
		}
		return blocks;
	}

	/**
	 * @return the startingBlock
	 */
	public Block getStartingBlock()
	{
		return startingBlock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Block> iterator()
	{
		return new BlockIterator(startingBlock);
	}
}

class Block
{
	private List<Line> lines = new ArrayList<Line>();

	private final BlockType type;

	private Block preceding;

	private Block persuing;

	public Block(BlockDimension dimension, List<String> rawLines, BlockType type)
	{
		this.type = type;

		if (rawLines == null || rawLines.isEmpty())
			throw new ApplicationCodingException("block with no lines is not allowed");

		for (int i = dimension.getFirst(); i <= dimension.getLast(); i++)
		{
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
	 * Every line of this block is moved to an own Block instance. This instance
	 * becomes the head of the resulting list of Block Elements. No sorting is
	 * applied!
	 */
	public void explode()
	{
		List<Line> tmp = lines;

		lines = new ArrayList<Line>();
		lines.add(tmp.get(0));

		final Block startingBlock = this;
		final Block endBlock = persuing;
		Block previous = this;
		for (int i = 1; i < tmp.size(); i++)
		{
			final List<Line> newLine = new ArrayList<Line>();
			newLine.add(tmp.get(i));
			final Block newBlock = new Block(newLine, type);

			if (i == 1)
			{ // start
				newBlock.concat(startingBlock, null);
			} else if (i == tmp.size() - 1)
			{ // end
				newBlock.concat(previous, endBlock);
			} else
			{
				newBlock.concat(previous, null);
			}

			previous = newBlock;
		}
	}

	/**
	 * Merges two blocks resulting in one single block. Only blocks of same type
	 * can be merged.
	 * 
	 * @param mergeThis
	 *            this block is going to be merged into this block. His persuing
	 *            block will be attached to this block as persuing block.
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

	public void concat(Block preceding, Block persuing)
	{
		// 0 0 0 1 0 0 0 2 0 0 3 : imagine you want to concat block 1 with 2 and
		// 3. In this case the two blocks between 2 and 3 are not connected
		// anymore to other blocks. so thats why we check if the two blocks as
		// params a conntected to each other, to prevent inconsistent state.
		if (preceding != null && persuing != null && persuing.getPreceding() != null
				&& persuing.getPreceding() != preceding)
			throw new ApplicationCodingException(
					"trying to concat block with two blocks that are not connected to each other");

		// remove old connections or refresh them
		if (this.preceding != null && this.persuing != null)
		{
			this.preceding.persuing = this.persuing;
			this.persuing.preceding = this.preceding;
		} else if (this.preceding != null)
		{
			this.preceding.persuing = null;
		} else if (this.persuing != null)
		{
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

	public void detach()
	{
		if (preceding != null && persuing != null)
		{
			preceding.concat(null, persuing);
		} else if (preceding != null)
		{
			preceding.persuing = null;
		} else if (persuing != null)
		{
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

	static enum BlockType
	{
		COMMENT, KEYVALUE, BLANK, ;
	}
}

class Line
{
	private final int pos;

	private final String line;

	/**
	 * @param pos
	 * @param line
	 */
	public Line(int pos, String line)
	{
		super();
		this.pos = pos;
		this.line = line;
	}

	/**
	 * @return the pos
	 */
	public int getPos()
	{
		return pos;
	}

	/**
	 * @return the line
	 */
	public String getLine()
	{
		return line;
	}

}

/**
 * Describes the positioning of a Block in a Property-File. That means: gives
 * you information about the start- and end-line.
 * 
 * @author Daniel
 * 
 */
class BlockDimension implements Comparable<BlockDimension>
{
	private final Integer first;

	private final Integer last;

	/**
	 * @param first
	 * @param last
	 */
	public BlockDimension(Integer first, Integer last)
	{
		super();

		if (last < first)
		{
			throw new ApplicationCodingException("last is not allowed to be lower then first");
		}

		this.first = first;
		this.last = last;
	}

	/**
	 * @return the first
	 */
	public Integer getFirst()
	{
		return first;
	}

	/**
	 * @return the last
	 */
	public Integer getLast()
	{
		return last;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockDimension other = (BlockDimension) obj;
		if (first == null)
		{
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (last == null)
		{
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("BlockDimension [first=").append(first).append(", last=").append(last).append("]");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BlockDimension arg0)
	{
		if (arg0.first > last)
			return -1;

		if (arg0.last < first)
			return 1;
		return 0;
	}
}

class BlockIterator implements ListIterator<Block>
{
	private Block actualBlock;

	public BlockIterator(final Block actualBlock)
	{
		this.actualBlock = actualBlock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		boolean result = actualBlock != null
				&& (actualBlock.getPersuing() != null || (actualBlock.getPreceding() == null && actualBlock
						.getPersuing() == null));
		if (actualBlock == null || (result == false && actualBlock == null))
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#next()
	 */
	@Override
	public Block next()
	{
		Block next = actualBlock;
		actualBlock = actualBlock.getPersuing();
		return next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#hasPrevious()
	 */
	@Override
	public boolean hasPrevious()
	{
		if (actualBlock != null && actualBlock.getPreceding() != null)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#previous()
	 */
	@Override
	public Block previous()
	{
		Block previous = actualBlock.getPreceding();
		actualBlock = previous;
		return previous;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#nextIndex()
	 */
	@Override
	public int nextIndex()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#previousIndex()
	 */
	@Override
	public int previousIndex()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#remove()
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("remove on Block Iterator is not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#set(java.lang.Object)
	 */
	@Override
	public void set(Block e)
	{
		throw new UnsupportedOperationException("set on Block Iterator is not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ListIterator#add(java.lang.Object)
	 */
	@Override
	public void add(Block e)
	{
		throw new UnsupportedOperationException("add on Block Iterator is not supported");
	}
}