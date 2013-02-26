package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel
 * 
 */
public class EmptyBlock extends Block
{

	public EmptyBlock(int emptyLineCnt)
	{
		super(new BlockDimension(0, emptyLineCnt - 1), rawLines(emptyLineCnt), BlockType.BLANK);
	}

	private static List<String> rawLines(int emptyLineCnt)
	{
		final List<String> result = new ArrayList<>();
		for (int i = 0; i < emptyLineCnt; i++) {
			result.add("\n");
		}
		return result;
	}
}
