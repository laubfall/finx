package de.ludwig.finx.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.Language;
import de.ludwig.finx.io.PropertyKeyOrderSetting.PropertyKeyOrder;

/**
 * A programmatic representation of an I18n-Properties-File
 * 
 * @author Daniel
 * 
 */
public class PropertyFile implements Iterable<Block>
{
	private static final Logger LOG = Logger.getLogger(PropertyFile.class);

	private Block startingBlock;

	/**
	 * required to get the correct values of I18n-Nodes
	 */
	private Language language;

	/**
	 * 
	 * @param i18nRes
	 *            The I18n-Properties-File this object is based on. If it is null you start with an
	 *            empty {@link PropertyFile} Object, otherwise the File is processed and its content
	 *            is pushed to this object.
	 * @param language
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PropertyFile(final File i18nRes, final Language language) throws FileNotFoundException, IOException
	{
		if (i18nRes != null && i18nRes.exists()) {
			try (FileInputStream fis = new FileInputStream(i18nRes)) {
				process(IOUtils.readLines(fis));
			}
		}
		this.language = language;
	}

	public int size()
	{
		int cnt = 0;
		final Iterator<Block> it = iterator();
		while (it.hasNext()) {
			it.next();
			cnt++;
		}

		return cnt;
	}

	/**
	 * After all modifcations are done to this PropertyFile Instance its time to write the result to
	 * a properties file. Use this method after all modifications are applied and to receive the
	 * data that shall be written to the properties file.
	 * 
	 * @return see description
	 */
	public List<String> filedata()
	{
		final List<String> result = new ArrayList<String>();

		for (final Block b : this) {
			for (Line l : b.getLines()) {
				result.add(l.getLine());
			}
		}
		return result;
	}

	/**
	 * Inserts if necessary (if there is actually no key in this structure equals to the key of the
	 * {@link I18nNode}) the given I18nNode with respect to all relevant Prettyprint-Settings e.g.:
	 * PropertiesWriter#preservePropertyLayout
	 * 
	 * Regarding the value of the key-value-Pair it has to be said, that it is dependent of the
	 * language of this object which value will be retrieved of the {@link I18nNode}.
	 * 
	 * @param insertThis
	 */
	public final void insert(final I18nNode insertThis)
	{
		final PropertyKeyOrder keyOrder = PropertiesWriter.keyOrder.setting().getKeyOrder();

		if (startingBlock == null) {
			insertIntoEmpty(insertThis, keyOrder);
			return;
		}

		final PropertyPreserveMode preserveMode = PropertiesWriter.preservePropertyLayout.setting();
		switch (preserveMode)
		{
		case NONE:
			insertPreserveModeNone();
			break;
		case NONSTRICT:
			insertPreserveModeNonstrict();
			break;
		case STRICT:
			insertPreserveModeStrict(insertThis);
			break;
		}
	}

	/**
	 * Removes the given keyValue-Pair from the file
	 * 
	 * @param removeThis
	 */
	public final void remove(final I18nNode removeThis)
	{

	}

	/**
	 * group all keyValues as specified by {@link PropertiesWriter#keyGrouping} and
	 * {@link PropertiesWriter#keyGroupSpace}
	 */
	public final void grouping()
	{
		List<Block> keyValueBlocks = blocksOfType(BlockType.KEYVALUE);
		if (keyValueBlocks.isEmpty())
			return;

		for (Block kv : keyValueBlocks) {
			kv.explode();
		}

		keyValueBlocks = blocksOfType(BlockType.KEYVALUE);

		// key: key length (that means the number of key-parts)
		final Map<Integer, List<GroupPart>> keyLengthGrouped = new HashMap<>();
		for (Block exp : keyValueBlocks) {
			final GroupPart gp = new GroupPart(exp);
			Integer keyLength = gp.keyLength();
			if (keyLengthGrouped.containsKey(keyLength) == false) {
				keyLengthGrouped.put(keyLength, new ArrayList<GroupPart>());
			}

			keyLengthGrouped.get(keyLength).add(gp);
		}

		// final Integer keyGrouping = PropertiesWriter.keyGrouping.setting();
		final Set<Integer> keyLengths = keyLengthGrouped.keySet();

		Map<String, List<GroupPart>> keyGroups = new HashMap<>();
		keyGroups.putAll(groupLower(keyLengths, keyLengthGrouped));

		final Set<String> groupingKeys = keyGroups.keySet();
		LOG.debug("grouping keys: " + groupingKeys);

		startingBlock = null;
		final List<Block> mergedKeyValueBlocks = new ArrayList<>();
		// next we merge all key-value-Blocks to one block that represents the group defined by the
		// grouping key
		for (String gk : groupingKeys) {
			final List<GroupPart> groupParts = keyGroups.get(gk);
			LOG.debug(String.format("GroupParts for groupingKey %s : %d ", gk, groupParts.size()));

			Block mergeBlock = null;
			for (int i = 0; i < groupParts.size(); i++) {
				GroupPart next = groupParts.get(i);
				next.owningBlock.detach();
				if (mergeBlock == null) {
					mergeBlock = next.owningBlock;
				}

				if (i < groupParts.size() - 1) {
					GroupPart next2 = groupParts.get(i + 1);
					next2.owningBlock.detach();
					mergeBlock.insertBefore(next2.owningBlock);
					mergeBlock.merge(next2.owningBlock);
				} else { // the last line of the current grouped block
					mergedKeyValueBlocks.add(mergeBlock);
					mergeBlock = null;
				}
			}
		}

		// in a last step all resulting blocks are connected to each other
		startingBlock = null;
		Block lastBlockInGroup = null;
		final Iterator<Block> mergedBlocksIterator = mergedKeyValueBlocks.iterator();
		while (mergedBlocksIterator.hasNext()) {
			Block b = mergedBlocksIterator.next();
			if (startingBlock == null) {
				startingBlock = b;
			}

			if (lastBlockInGroup != null) {
				b.insertAfter(lastBlockInGroup);
			}

			if (mergedBlocksIterator.hasNext()) {
				Block empty = new EmptyBlock(PropertiesWriter.keyGroupSpace.setting());
				empty.insertAfter(b);
				lastBlockInGroup = empty;
			}
		}
	}

	/**
	 * 
	 * @param block
	 *            check this block if it is attached as a comment to a key-value-block or if it has
	 *            an attached comment if it is a key-value-Block
	 * @return the block where a comment is attachted to otherwise null
	 */
	public static final Block isCommentAttached(final Block block)
	{
		final Integer emptyLineCnt = PropertiesWriter.attachCommentsWithEmptyLineCount.setting();

		if (block.getType().equals(BlockType.COMMENT)) {
			int cntAgainstEmptyLineCnt = 0;
			Block tmp = block.getPersuing();
			while (tmp != null && cntAgainstEmptyLineCnt <= emptyLineCnt) {
				switch (tmp.getType())
				{
				case BLANK:
					if (cntAgainstEmptyLineCnt > emptyLineCnt)
						return null;
					cntAgainstEmptyLineCnt++;
					break;
				case COMMENT:
					return null;
				case KEYVALUE:
					return tmp;
				}
				tmp = tmp.getPersuing();
			}
		}

		if (block.getType().equals(BlockType.KEYVALUE)) {
			int cntAgainstEmptyLineCnt = 0;
			Block tmp = block.getPreceding();
			while (tmp != null && cntAgainstEmptyLineCnt <= emptyLineCnt) {
				switch (tmp.getType())
				{
				case BLANK:
					if (cntAgainstEmptyLineCnt > emptyLineCnt)
						return null;
					cntAgainstEmptyLineCnt++;
					break;
				case COMMENT:
					return tmp;
				case KEYVALUE:
					return null;
				}
				tmp = tmp.getPreceding();
			}
		}

		return null;
	}

	private Map<String, List<GroupPart>> groupLower(final Collection<Integer> lengths,
			Map<Integer, List<GroupPart>> keyLengthGrouped)
	{
		final Map<String, List<GroupPart>> result = new HashMap<>();
		final Integer keyGrouping = PropertiesWriter.keyGrouping.setting();
		for (Integer len : lengths) {
			final List<GroupPart> parts = keyLengthGrouped.get(len);
			for (final GroupPart l : parts) {

				String groupingKeyPart;
				if (len <= keyGrouping) {
					groupingKeyPart = l.groupingKeyPart();
				} else {
					groupingKeyPart = l.partOfKey(keyGrouping);
				}
				if (groupingKeyPart == null) {
					result.put(l.key(), new ArrayList<GroupPart>() {
						private static final long serialVersionUID = 1L;

						{
							add(l);
						}
					});
					continue;
				}
				if (result.containsKey(groupingKeyPart) == false) {
					result.put(groupingKeyPart, new ArrayList<GroupPart>());
				}

				result.get(groupingKeyPart).add(l);
			}
		}
		return result;
	}

	private void insertPreserveModeNone()
	{

	}

	private void insertPreserveModeNonstrict()
	{

	}

	private void insertPreserveModeStrict(I18nNode nodeToInsert)
	{
		final Iterator<Block> blockIterator = iterator();
		while (blockIterator.hasNext()) {
			final Block last = blockIterator.next();
			if (blockIterator.hasNext()) {
				continue;
			}

			if (last.getType().equals(BlockType.KEYVALUE)) {
				last.getLines().add(new Line(0, nodeToInsert.keyValue(language)));
			} else {
				final List<String> rawLines = new ArrayList<>();
				rawLines.add(nodeToInsert.keyValue(language));
				Block newKeyValueBlock = new Block(new BlockDimension(0, 0), rawLines, BlockType.KEYVALUE);
				last.concat(null, newKeyValueBlock);
			}

			break;
		}
	}

	/**
	 * @param insertThis
	 * @param keyOrder
	 */
	private void insertIntoEmpty(final I18nNode insertThis, final PropertyKeyOrder keyOrder)
	{
		if (startingBlock != null) {
			throw new ApplicationCodingException("cannot insert into empty because startingBlock already exists");
		}

		final List<String> rawLines = new ArrayList<>();
		final List<I18nNode> flattened = insertThis.flatten();
		PropertyKeyOrderSetting.sort(flattened, keyOrder);
		for (I18nNode n : flattened) {
			rawLines.add(n.keyValue(language));
		}
		startingBlock = new Block(new BlockDimension(0, flattened.size() - 1), rawLines, BlockType.KEYVALUE);
	}

	private void process(final List<String> lines)
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
		for (int i = 0; i < allBlocks.size(); i++) {
			final BlockDimension dimension = allBlocks.get(i);
			BlockType type = null;
			if (commentBlocks.contains(dimension)) {
				type = BlockType.COMMENT;
			}

			if (keyValueBlocks.contains(dimension)) {
				type = BlockType.KEYVALUE;
			}

			if (blankBlocks.contains(dimension)) {
				type = BlockType.BLANK;
			}

			if (type == null) {
				throw new ApplicationCodingException("unable to determine Block-Type with dimensions of: " + dimension);
			}

			blocks.add(new Block(dimension, lines, type));
		}

		// now concat every block with its neighbours
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (i == 0) {
				startingBlock = block;
			}

			if (i + 1 < blocks.size() && i > 0) {
				block.concat(blocks.get(i - 1), blocks.get(i + 1));
			} else if (i + 1 == blocks.size() && i > 0) {
				block.concat(blocks.get(i - 1), null);
			} else if (i + 1 < blocks.size() && i == 0) {
				block.concat(null, blocks.get(i + 1));
			}
		}

		preFormat();
	}

	/**
	 * ordering means to order all blocks of type {@link BlockType#KEYVALUE}
	 */
	private void sort()
	{
		List<Block> sortedByKeyValues = blocksOfType(BlockType.KEYVALUE);
		Collections.sort(sortedByKeyValues, new Comparator<Block>() {
			@Override
			public int compare(Block o1, Block o2)
			{
				List<Line> lines1 = o1.getLines();
				List<Line> lines2 = o2.getLines();

				if (lines1.isEmpty() || lines1.size() != 1 || lines2.isEmpty() || lines2.size() != 1) {
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
		 * position behalten: - aktueller Block ist der sortierte Block (/) - der aktuelle Block ist
		 * der Block der in der Sortierung direkt nach dem sortierten Block folgt (/)
		 * 
		 * eins vorrücken: - aktueller Block ist ein Kommentar ohne Verbindung zu einem
		 * Key-Value-Block - aktueller Block ist ein Kommmentar mit Verbindung zu einem
		 * Key-Value-Block - aktueller Block ist ein Empty-Space-Block
		 */
		final ListIterator<Block> listIterator = sortedByKeyValues.listIterator();
		while (listIterator.hasNext()) {
			Block keyValue = listIterator.next();
			Block checkAgainst = startingBlock;
			while (checkAgainst != null) {
				if (checkAgainst == keyValue) {
					break;
				}

				if (listIterator.hasNext()) {
					Block next = listIterator.next();
					if (next == checkAgainst) {
						keyValue.detach();
						checkAgainst.concat(keyValue, checkAgainst.getPersuing());
					}
					listIterator.previous();
					break;
				}

				if (checkAgainst.getType().equals(BlockType.BLANK)) {
					if (checkAgainst.getPersuing() == null) {
						keyValue.detach();
						checkAgainst.concat(checkAgainst.getPreceding(), keyValue);
						break;
					}

					checkAgainst = checkAgainst.getPersuing();
					continue;
				}

				if (isCommentAttached(checkAgainst) != null) {

				}

				checkAgainst = checkAgainst.getPersuing();
			}
		}
	}

	/**
	 * If Finx shall take care about the formatting of the properties file this method do any
	 * necessary formatting before further changes are made.
	 */
	private void preFormat()
	{
		if (PropertiesWriter.preservePropertyLayout.setting().equals(PropertyPreserveMode.NONE) == false) {
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
	 * @return a list of pairs of integers. Every pair describes a start and an end point where
	 *         comments occures. Size of this list is always 0 if % 2;
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

		for (final String line : rawPropertyLines) {
			final Matcher match = pat.matcher(line);
			if (match.matches()) {
				matched = true;
				if (lineCount == rawPropertyLines.size() - 1) {
					fetchedLastMatch = true;
				}

				if (firstMatchedLine == -1) {
					firstMatchedLine = lineCount;
					lastMatchedLine = lineCount;
				} else {
					lastMatchedLine = lineCount;
				}
			} else if (matched) {
				fetchedLastMatch = true;
			}

			if (fetchedLastMatch && matched) {
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
		while (blockIterator.hasNext()) {
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

	private class GroupPart
	{
		final Line line;

		final Block owningBlock;

		GroupPart(final Block exploded) throws ApplicationCodingException
		{
			List<Line> lines = exploded.getLines();
			if (lines.size() != 1) {
				throw new ApplicationCodingException("it is not allowed for a group part to have more than one line");
			}

			line = lines.get(0);
			owningBlock = exploded;
		}

		String key()
		{
			return StringUtils.substringBefore(line.getLine(), "=");
		}

		/**
		 * 
		 * @return the part of the key that can be used to group other keys. For example: you have
		 *         this key de.ludwig.test, then the groupingKeyPart would be de.ludwig, because
		 *         there can be other keys with the same prefix de.ludwig . Maybe de.ludwig.test2
		 */
		String groupingKeyPart()
		{
			return partOfKey(keyLength() - 1);
		}

		String partOfKey(final int partCnt)
		{
			final String[] keyParts = keyParts();
			if (keyParts.length == 1) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < partCnt; i++) {
				sb.append(keyParts[i]);
				if (i < partCnt - 1) {
					sb.append(".");
				}
			}
			return sb.toString();
		}

		String[] keyParts()
		{
			return I18nNode.i18nKeySplit(key());
		}

		Integer keyLength()
		{
			return keyParts().length;
		}
	}
}