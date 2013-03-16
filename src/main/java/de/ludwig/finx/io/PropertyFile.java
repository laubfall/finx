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
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.Language;
import de.ludwig.finx.io.PropertyKeyOrderSetting.PropertyKeyOrder;
import de.ludwig.finx.settings.Setting;
import de.ludwig.finx.settings.SettingsChangedListener;
import de.ludwig.finx.settings.SettingsDaoImpl;
import de.ludwig.finx.settings.UpdatableSetting;

/**
 * A programmatic representation of an I18n-Properties-File
 * 
 * @author Daniel
 * 
 */
public class PropertyFile implements Iterable<Block>, SettingsChangedListener
{
	private static final Logger LOG = Logger.getLogger(PropertyFile.class);

	private Block startingBlock;

	/**
	 * required to get the correct values of I18n-Nodes
	 */
	private Language language;

	/**
	 * For a full description of the meanings of the different kind of modes see
	 * {@link PropertyPreserveMode}
	 */
	public static UpdatableSetting<PropertyPreserveMode> preservePropertyLayout;

	public static UpdatableSetting<BlockOrder> blockOrder;

	static {
		SettingsDaoImpl.instance().init(PropertyFile.class);
	}

	/**
	 * Flag that indicates that this PropertyFile Instance / its content was formatted as specified
	 * by the application, if the flag is true. It is used by various formatting methods that
	 * assumes that this instance is already formatted as the application specified, because these
	 * methods build up there strategy on this assumption.
	 */
	private boolean applicationStyleFormat = false;

	/**
	 * 
	 * @param i18nRes
	 *            Optional. The I18n-Properties-File this object is based on. If it is null you
	 *            start with an empty {@link PropertyFile} Object, otherwise the File is processed
	 *            and its content is pushed to this object.
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
			b.sortLines();
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
	 * If there is already a key-value-Pair with the key to be inserted, then the value is going to
	 * be updated.
	 * 
	 * Regarding the value of the key-value-Pair it has to be said, that it is dependent of the
	 * language of this object which value will be retrieved of the {@link I18nNode}.
	 * 
	 * @param insertThis
	 */
	public final void insertOrUpdate(final I18nNode insertThis)
	{
		final PropertyKeyOrder keyOrder = PropertiesWriter.keyOrder.setting();

		if (startingBlock == null) {
			insertIntoEmpty(insertThis, keyOrder);
			return;
		}

		if (update(insertThis)) {
			return;
		}

		final PropertyPreserveMode preserveMode = PropertyFile.preservePropertyLayout.setting();
		switch (preserveMode)
		{
		case NONE:
			insertPreserveModeNone(insertThis);
			break;
		case NONSTRICT:
			insertPreserveModeNonStrict(insertThis);
			applicationStyleFormat = false;
			break;
		case STRICT:
			insertPreserveModeStrict(insertThis);
			applicationStyleFormat = false;
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
	 * This method checks if a specific Block has a comment attached or, if the block is of Type
	 * {@link BlockType#COMMENT}, if it is attached to a key-value-Block. Attached means that a
	 * Block with comments comes first before a Block with key-Value-Pairs. By default no empty
	 * lines (Blocks) are allowed between these two blocks, otherwise the comment is not attached.
	 * This behaviour is dependent of the Setting
	 * {@link PropertiesWriter#attachCommentsWithEmptyLineCount}
	 * 
	 * @param block
	 *            check this block if it is attached as a comment to a key-value-block or if it has
	 *            an attached comment if it is a key-value-Block.
	 * @return if the block param is of type {@link BlockType#KEYVALUE} and it has a comment
	 *         attached, this method returns the comment-block. If the block param is of type
	 *         {@link BlockType#COMMENT} and this block is attached to a key-value-Block the
	 *         key-value-Block is returned. In all other cases the return value is null.
	 */
	public static final Block isCommentAttached(final Block block)
	{
		final Integer emptyLineCnt = PropertiesWriter.attachCommentsWithEmptyLineCount.setting();

		if (block.getType().equals(BlockType.COMMENT)) {
			int cntAgainstEmptyLineCnt = 0;
			Block tmp = block.getPursuing();
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
				tmp = tmp.getPursuing();
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

	/**
	 * If Finx shall take care about the formatting of the properties file this method do any
	 * necessary formatting before further changes are made.
	 * 
	 * This method does nothing if {@link PropertyPreserveMode} is not
	 * {@link PropertyPreserveMode#NONE}
	 */
	private void applicationFormat()
	{
		if (PropertyFile.preservePropertyLayout.setting().equals(PropertyPreserveMode.NONE) == false) {
			return;
		}

		LOG.debug("PropertyFile is going to be formatted as specified by application");

		List<BlockGroupInfo> grouping = grouping();
		blockSort(grouping);

		final List<Block> sortedBlocks = new ArrayList<>();
		for (BlockGroupInfo bgi : grouping) {
			sortedBlocks.add(bgi.groupBlock);
		}

		buildBlockHierarchy(sortedBlocks);

		applicationStyleFormat = true;
	}

	/**
	 * group all keyValues as specified by {@link PropertiesWriter#keyGrouping} and
	 * {@link PropertiesWriter#keyGroupSpace}
	 * 
	 */
	private List<BlockGroupInfo> grouping()
	{
		List<Block> keyValueBlocks = blocksOfType(BlockType.KEYVALUE);
		if (keyValueBlocks.isEmpty())
			return new ArrayList<>();

		LOG.debug(String.format("processing %d key-value-blocks", keyValueBlocks.size()));

		for (Block kv : keyValueBlocks) {
			kv.explode();
		}

		keyValueBlocks = blocksOfType(BlockType.KEYVALUE);

		LOG.debug(String.format("key-value-Blocks were splittet into %d key-value-Block with one line",
				keyValueBlocks.size()));

		// key: key length (that means the number of key-parts)
		final Map<Integer, List<KeyValueGroupPart>> keyLengthGrouped = new HashMap<>();
		for (Block exp : keyValueBlocks) {
			final KeyValueGroupPart gp = new KeyValueGroupPart(exp);
			Integer keyLength = gp.keyLength();
			if (keyLengthGrouped.containsKey(keyLength) == false) {
				keyLengthGrouped.put(keyLength, new ArrayList<KeyValueGroupPart>());
			}

			keyLengthGrouped.get(keyLength).add(gp);
		}

		final Set<Integer> keyLengths = keyLengthGrouped.keySet();

		Map<String, List<KeyValueGroupPart>> keyGroups = new HashMap<>();
		keyGroups.putAll(groupLower(keyLengths, keyLengthGrouped));

		final Set<String> groupingKeys = keyGroups.keySet();
		LOG.debug("grouping keys: " + groupingKeys);

		List<BlockGroupInfo> groupingInfo = new ArrayList<>();
		// final List<Block> mergedKeyValueBlocks = new ArrayList<>();
		// next we merge all key-value-Blocks to one block that represents the group defined by the
		// grouping key
		for (String gk : groupingKeys) {
			final List<KeyValueGroupPart> groupParts = keyGroups.get(gk);
			LOG.debug(String.format("GroupParts for groupingKey %s : %d ", gk, groupParts.size()));

			Block mergeBlock = null;
			for (int i = 0; i < groupParts.size(); i++) {
				KeyValueGroupPart actual = groupParts.get(i);
				final Block b = new Block(BlockType.KEYVALUE, actual.line.getLine());
				final Block commentAttached = isCommentAttached(actual.owningBlock);
				if (commentAttached != null) {
					final Block comment = new Block(commentAttached.getLines(), BlockType.COMMENT);
					b.insertAfter(comment);
				}

				if (mergeBlock == null) {
					mergeBlock = b;
				} else {
					mergeBlock.concatTailToHead(b);
					if (commentAttached == null) {
						mergeBlock.merge(b);
					}
				}
			}

			groupingInfo.add(new BlockGroupInfo(gk, mergeBlock));
			// mergedKeyValueBlocks.add(mergeBlock);
		}

		return groupingInfo;
	}

	/**
	 * 
	 * @param grouping
	 */
	private void blockSort(List<BlockGroupInfo> groupingInfo)
	{
		Comparator<BlockGroupInfo> comparator = comparatorBySetting();
		if (comparator == null)
			return;
		Collections.sort(groupingInfo, comparator);
	}

	private void buildBlockHierarchy(List<Block> mergedKeyValueBlocks)
	{
		// this needs to be done early, because during this method we modify the starting-block
		// what effects every iteration over all blocks in this PropertyFile-Object
		final List<Block> commentBlocksNotAttached = commentBlocksNotAttached();

		// comments that are not attached to any key-Value-Block are added to this PropertyFile now.
		final Iterator<Block> cbnaIter = commentBlocksNotAttached.iterator();
		startingBlock = null;
		while (cbnaIter.hasNext()) {
			Block current = cbnaIter.next();
			if (startingBlock == null) {
				startingBlock = current;
			}
			current.detach();
			if (cbnaIter.hasNext()) {
				Block next = cbnaIter.next();
				next.detach();
				current.merge(next);
			}
		}

		// in a last step all resulting blocks are connected to each other
		Block lastBlockInGroup = null;
		final Iterator<Block> mergedBlocksIterator = mergedKeyValueBlocks.iterator();
		LOG.debug(String.format("%d key-value-block were created", mergedKeyValueBlocks.size()));
		while (mergedBlocksIterator.hasNext()) {
			Block b = mergedBlocksIterator.next().head();

			if (lastBlockInGroup != null) {
				lastBlockInGroup.concatTailToHead(b);
			}

			if (mergedBlocksIterator.hasNext()) {
				Block empty = new EmptyBlock(PropertiesWriter.keyGroupSpace.setting());
				empty.insertAfter(b);
				lastBlockInGroup = empty;
			} else {
				lastBlockInGroup = b;
			}
		}

		if (startingBlock == null) {
			startingBlock = lastBlockInGroup.head();
		} else {
			startingBlock.insertBefore(lastBlockInGroup.head());
		}
	}

	@SuppressWarnings("unchecked")
	private Comparator<BlockGroupInfo> comparatorBySetting()
	{
		final Comparator<BlockGroupInfo> keyNatural = new Comparator<BlockGroupInfo>() {

			@Override
			public int compare(BlockGroupInfo o1, BlockGroupInfo o2)
			{
				return o1.groupingKey.compareTo(o2.groupingKey);
			}
		};

		final Comparator<BlockGroupInfo> keyLength = new Comparator<BlockGroupInfo>() {

			@Override
			public int compare(BlockGroupInfo o1, BlockGroupInfo o2)
			{
				Integer i1 = o1.groupingKey.length();
				Integer i2 = o2.groupingKey.length();
				return i1.compareTo(i2);
			}
		};

		final BlockOrder order = blockOrder.setting();
		switch (order)
		{
		case GROUPING_KEY_LENGTH_ASC:
			return keyLength;
		case GROUPING_KEY_LENGTH_DESC:
			return ComparatorUtils.reversedComparator(keyLength);
		case GROUPING_KEY_AND_LENGTH_ASC:
			return ComparatorUtils.chainedComparator(keyLength, keyNatural);
		case GROUPING_KEY_AND_LENGTH_DESC:
			return ComparatorUtils.reversedComparator(ComparatorUtils.chainedComparator(keyLength, keyNatural));
		case NONE:
			return null;
		}

		return null;
	}

	private Map<String, List<KeyValueGroupPart>> groupLower(final Collection<Integer> lengths,
			Map<Integer, List<KeyValueGroupPart>> keyLengthGrouped)
	{
		final Map<String, List<KeyValueGroupPart>> result = new HashMap<>();
		final Integer keyGrouping = PropertiesWriter.keyGrouping.setting();
		for (Integer len : lengths) {
			final List<KeyValueGroupPart> parts = keyLengthGrouped.get(len);
			for (final KeyValueGroupPart l : parts) {

				String groupingKeyPart;
				if (len <= keyGrouping) {
					groupingKeyPart = l.groupingKeyPart();
				} else {
					groupingKeyPart = l.partOfKey(keyGrouping);
				}
				if (groupingKeyPart == null) {
					result.put(l.key(), new ArrayList<KeyValueGroupPart>() {
						private static final long serialVersionUID = 1L;

						{
							add(l);
						}
					});
					continue;
				}
				if (result.containsKey(groupingKeyPart) == false) {
					result.put(groupingKeyPart, new ArrayList<KeyValueGroupPart>());
				}

				result.get(groupingKeyPart).add(l);
			}
		}
		return result;
	}

	private void insertPreserveModeNone(I18nNode nodeToInsert)
	{
		LOG.debug(String.format("insert node with key %s in mode NONE", nodeToInsert.key()));
		// take care about the fact that we can only proceed in some meaningful manner if the
		// pf was formated in the "fink way" before.
		if (applicationStyleFormat == false) {
			startingBlock.concatTailToHead(new Block(BlockType.KEYVALUE, nodeToInsert.keyValue(language)));
			applicationFormat();
			return;
		}

		// after this point every operation expects that this PropertyFile-Instance is formatted in
		// "Finx-Style"
		final List<Block> keyValueBlocks = blocksOfType(BlockType.KEYVALUE);
		final KeyValueLineInfo kvliNew = new KeyValueLineInfo();
		kvliNew.line = new Line(0, nodeToInsert.keyValue(language));
		Block matchedBlock = null;
		final Integer keyGrouping = PropertiesWriter.keyGrouping.setting();
		blocks: for (final Block b : keyValueBlocks) {
			lines: for (Line l : b.getLines()) {
				final KeyValueLineInfo kvliExisting = new KeyValueLineInfo();
				kvliExisting.line = l;

				if (kvliNew.partOfKey(keyGrouping).equals(kvliExisting.groupingKeyPart())) {
					matchedBlock = b;
					break blocks;
				} else {
					continue lines;
				}
			}
		}

		if (matchedBlock != null) {
			matchedBlock.getLines().add(kvliNew.line);
		} else {
			final Block empty = new EmptyBlock(PropertiesWriter.keyGroupSpace.setting());
			empty.insertBefore(new Block(BlockType.KEYVALUE, kvliNew.line.getLine()));
			startingBlock.concatTailToHead(empty);
		}
	}

	/**
	 * In NonStrict Mode new keys are added at that point where there natural ordering matches most.
	 * The ordering is dependent of Setting {@link PropertiesWriter#keyOrder}
	 * 
	 * @param nodeToInsert
	 */
	private void insertPreserveModeNonStrict(I18nNode nodeToInsert)
	{
		LOG.debug(String.format("inserte node with key %s in mode NONSTRICT", nodeToInsert.key()));

		PropertyKeyOrder keyOrder = PropertiesWriter.keyOrder.setting();
		final String keyValue = nodeToInsert.keyValue(language);
		final List<Block> keyValueBlocks = blocksOfType(BlockType.KEYVALUE);
		Integer smallestCompare = null;
		Block smallestCompareBlock = null;
		Line smallesCompareLine = null;
		for (Block b : keyValueBlocks) {
			List<Line> lines = b.getLines();
			for (Line l : lines) {
				final int compareTo = l.getLine().compareTo(keyValue);
				if (smallestCompare == null) {
					smallestCompare = compareTo;
					// necessary because if DESC we are only interessted in values ge 0
					if (keyOrder.equals(PropertyKeyOrder.DESC)) {
						smallestCompare = Math.abs(smallestCompare);
					}
					smallestCompareBlock = b;
					smallesCompareLine = l;
				} else if ((keyOrder.equals(PropertyKeyOrder.ASC) && compareTo <= 0 && compareTo > smallestCompare)
						|| (keyOrder.equals(PropertyKeyOrder.DESC) && compareTo >= 0 && compareTo < smallestCompare)) {
					smallestCompare = compareTo;
					smallestCompareBlock = b;
					smallesCompareLine = l;
				}
			}
		}

		List<Line> lines = smallestCompareBlock.getLines();
		int indexOf = lines.indexOf(smallesCompareLine);
		lines.add(indexOf, new Line(indexOf, keyValue));
	}

	/**
	 * In Strict Mode new keys are added at the bottom of the file
	 * 
	 * @param nodeToInsert
	 */
	private void insertPreserveModeStrict(I18nNode nodeToInsert)
	{
		LOG.debug(String.format("inserte node with key %s in mode STRICT", nodeToInsert.key()));

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
				last.insert(null, newKeyValueBlock);
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

		startingBlock = new Block(BlockType.KEYVALUE, insertThis.keyValue(language));
	}

	/**
	 * Looks
	 * 
	 * @param update
	 */
	private boolean update(final I18nNode update)
	{
		final List<Block> keyValues = blocksOfType(BlockType.KEYVALUE);
		Line lineToUpdate = null;
		Block referencedBlock = null;
		outter: for (Block b : keyValues) {
			final List<Line> lines = b.getLines();
			for (Line l : lines) {
				KeyValueGroupPart gp = new KeyValueGroupPart(new Block(BlockType.KEYVALUE, l.getLine()));
				if (gp.key().equals(update.key())) {
					lineToUpdate = l;
					referencedBlock = b;
					break outter;
				}
			}
		}

		if (lineToUpdate != null) {
			int index = referencedBlock.getLines().indexOf(lineToUpdate);
			referencedBlock.getLines().add(index, new Line(0, update.keyValue(language)));
			referencedBlock.getLines().remove(lineToUpdate);
			return true;
		}

		return false;
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
				block.insert(blocks.get(i - 1), blocks.get(i + 1));
			} else if (i + 1 == blocks.size() && i > 0) {
				block.insert(blocks.get(i - 1), null);
			} else if (i + 1 < blocks.size() && i == 0) {
				block.insert(null, blocks.get(i + 1));
			}
		}

		applicationFormat();
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

	@SuppressWarnings("unchecked")
	private List<Block> commentBlocksNotAttached()
	{
		final List<Block> comments = blocksOfType(BlockType.COMMENT);
		return (List<Block>) CollectionUtils.select(comments, new Predicate() {

			@Override
			public boolean evaluate(Object object)
			{
				final Block b = (Block) object;
				return isCommentAttached(b) == null;
			}
		});
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

	private class KeyValueLineInfo
	{
		Line line;

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

	private class KeyValueGroupPart extends KeyValueLineInfo
	{
		final Block owningBlock;

		KeyValueGroupPart(final Block exploded) throws ApplicationCodingException
		{
			List<Line> lines = exploded.getLines();
			if (lines.size() != 1) {
				throw new ApplicationCodingException("it is not allowed for a group part to have more than one line");
			}

			line = lines.get(0);
			owningBlock = exploded;
		}
	}

	/**
	 * Class to keep track of information that results out of the grouping process.
	 * 
	 * @author Daniel
	 * 
	 */
	private class BlockGroupInfo
	{
		private String groupingKey;

		private Block groupBlock;

		public BlockGroupInfo(String groupingKey, Block groupBlock)
		{
			if (groupBlock.getType().equals(BlockType.KEYVALUE) == false) {
				throw new ApplicationCodingException(
						"BlockGroupInfo Instance only possible for blocks of type KEYVALUE, was: "
								+ groupBlock.getType());
			}
			this.groupingKey = groupingKey;
			this.groupBlock = groupBlock;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.settings.SettingsChangedListener#settingChanged(java.lang.String,
	 * de.ludwig.finx.settings.UpdatableSetting)
	 */
	@Override
	public void settingChanged(String settingName, Setting<?> oldSetting)
	{
		// TODO Auto-generated method stub

	}
}