package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Daniel
 * 
 */
public class PropertyFileUtils
{
	/**
	 * Searches for Comments in raw-Property-File Data.
	 * 
	 * @param rawPropertyLines
	 * @return a list of pairs of integers. Every pair describes a start and an
	 *         end point where comments occures. Size of this list is always 0
	 *         if % 2;
	 */
	public static List<Integer> commentBlocks(final List<String> rawPropertyLines)
	{
		int lineCount = 0;
		int lastCommentLine = -1;
		int firstCommentLine = -1;

		final List<Integer> blocks = new ArrayList<Integer>();

		boolean comment = false;
		boolean fetchedLastComment = false;
		
		for (final String line : rawPropertyLines)
		{
			if(StringUtils.isBlank(line)) {
				lineCount++;
				continue;
			}
			
			final String trim = line.trim();
			if (trim.startsWith("#"))
			{
				comment = true;
				if(lineCount == rawPropertyLines.size() - 1) {
					fetchedLastComment = true;
				}
				
				if (firstCommentLine == -1)
				{
					firstCommentLine = lineCount;
					lastCommentLine = lineCount;
				} else
				{
					lastCommentLine = lineCount;
				}
			} else if(comment) {
				fetchedLastComment = true;
			}
			
			if (fetchedLastComment && comment)
			{
				blocks.add(firstCommentLine);
				blocks.add(lastCommentLine);
				lastCommentLine = -1;
				firstCommentLine = -1;
				comment = false;
				fetchedLastComment = false;
			}

			lineCount++;
		}

		return blocks;
	}
}
