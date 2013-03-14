package de.ludwig.finx.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.ludwig.finx.Language;
import de.ludwig.finx.SetttingsAwareTest;

/**
 * @author Daniel
 * 
 */
public class PropertyFileBlockSortTest extends SetttingsAwareTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void comparatorUtils()
	{
		Comparator<String> chainedComparator = ComparatorUtils.chainedComparator(new NaturalOrderingComparator(),
				new LengthComparator());
		String[] ar = new String[] { "de.test", "de", "nl.test", "nl", "de.test.blah", "de.sub" };
		Arrays.sort(ar, chainedComparator);
		System.out.println(ar);
	}

	@Test
	public void blockSortKeyLengthAsc() throws FileNotFoundException, IOException
	{
		PropertiesWriter.keyGrouping.change("2");
		PropertiesWriter.keyGroupSpace.change("1");
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONE.name());
		PropertyFile.blockOrder.change(BlockOrder.GROUPING_KEY_LENGTH_ASC.name());

		File end = null;
		try {
			end = I18nFileCreator.start().addKeyValue("nl.ludwig", "5").addKeyValue("de.ludwig.test", "7")
					.addKeyValue("nl.test", "6").addKeyValue("de.ludwig", "1").addKeyValue("de.test", "2")
					.addKeyValue("com.ludwig", "3").addKeyValue("com.test", "4").addKeyValue("nl.ludwig.test", "8")
					.end();

			final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMAN));
			final List<String> filedata = pf.filedata();
			Assert.assertNotNull(filedata);
			Assert.assertEquals(12, filedata.size());

			Assert.assertEquals("de.ludwig=1", filedata.get(0));
			Assert.assertEquals("de.test=2", filedata.get(1));

			Assert.assertEquals("nl.ludwig.test=8", filedata.get(11));

		} finally {
			FileUtils.deleteQuietly(end);
		}
	}

	@Test
	public void blockSortKeyLengthDesc() throws FileNotFoundException, IOException
	{
		PropertiesWriter.keyGrouping.change("2");
		PropertiesWriter.keyGroupSpace.change("1");
		PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.NONE.name());
		PropertyFile.blockOrder.change(BlockOrder.GROUPING_KEY_LENGTH_DESC.name());

		File end = null;
		try {
			end = I18nFileCreator.start().addKeyValue("nl.ludwig", "5").addKeyValue("de.ludwig.test", "7")
					.addKeyValue("nl.test", "6").addKeyValue("de.ludwig", "1").addKeyValue("de.test", "2")
					.addKeyValue("com.ludwig", "3").addKeyValue("com.test", "4").addKeyValue("nl.ludwig.test", "8")
					.end();

			final PropertyFile pf = new PropertyFile(end, new Language(Locale.GERMAN));
			final List<String> filedata = pf.filedata();
			Assert.assertNotNull(filedata);
			Assert.assertEquals(12, filedata.size());

			Assert.assertEquals("de.ludwig.test=7", filedata.get(0));

			Assert.assertEquals("nl.ludwig.test=8", filedata.get(2));

		} finally {
			FileUtils.deleteQuietly(end);
		}
	}
}

class NaturalOrderingComparator implements Comparator<String>
{

	@Override
	public int compare(String o1, String o2)
	{
		return o1.compareTo(o2);
	}

}

class LengthComparator implements Comparator<String>
{

	@Override
	public int compare(String o1, String o2)
	{
		Integer i1 = o1.length();
		Integer i2 = o2.length();
		return i1.compareTo(i2);
	}

}