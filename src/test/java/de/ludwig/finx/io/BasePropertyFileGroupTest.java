package de.ludwig.finx.io;

import java.util.List;

import org.powermock.reflect.Whitebox;

import de.ludwig.finx.SetttingsAwareTest;

/**
 * Basic functionallity for Grouping-Tests
 * 
 * @author Daniel
 * 
 */
abstract class BasePropertyFileGroupTest extends SetttingsAwareTest
{
	/**
	 * Shortcut for call of the private Method {@link PropertyFile#applicationFormat}
	 * 
	 * @param pf
	 *            The instance where we want to call the method on.
	 */
	public final void callApplicationFormat(final PropertyFile pf)
	{
		try {
			PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONE.name());
			Whitebox.invokeMethod(pf, "applicationFormat");
		} catch (Exception e) {
			throw new RuntimeException("failed to call grouping", e);
		}
	}

	public final List<Block> callBlocksOfType(final PropertyFile pf, final BlockType type)
	{
		try {
			List<Block> blocks = Whitebox.invokeMethod(pf, "blocksOfType", type);
			return blocks;
		} catch (Exception e) {
			throw new RuntimeException("failed to call blocksOfType", e);
		}
	}
}
