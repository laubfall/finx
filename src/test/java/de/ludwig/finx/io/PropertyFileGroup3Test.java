package de.ludwig.finx.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.ludwig.finx.Language;

/**
 * Test that uses PropertyFileGroupTest/test03.properties as Input
 * 
 * @author Daniel
 * 
 */
public class PropertyFileGroup3Test
{
	private File file;

	private int cntLinesInFile;

	public PropertyFileGroup3Test() throws URISyntaxException, IOException
	{
		URL resource = getClass().getClassLoader().getResource("PropertyFileGroupTest/test03.properties");
		file = new File(resource.toURI());
		PropertiesWriter.keyGroupSpace.change("1");
		List<String> lines = FileUtils.readLines(file);
		int nonEmptyCnt = 0;
		for (String l : lines) {
			if (StringUtils.isBlank(l))
				continue;

			nonEmptyCnt++;
		}

		cntLinesInFile = nonEmptyCnt;
	}

	@Test
	public void grouping1() throws FileNotFoundException, IOException
	{
		final PropertyFile pf = new PropertyFile(file, new Language("de"));
		PropertiesWriter.keyGrouping.change("1");
		pf.grouping();

		List<String> filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		// one empty line, because we have two groups (a... and b...)
		Assert.assertEquals(cntLinesInFile + 1, filedata.size());

		PropertiesWriter.keyGroupSpace.change("2");
		pf.grouping();

		filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		// two empty lines, because we have two groups (a... and b...) but two empty-lines are
		// wanted as space
		Assert.assertEquals(cntLinesInFile + 2, filedata.size());
		assertContent(filedata, file);
	}

	@Test
	public void grouping2() throws FileNotFoundException, IOException
	{
		final PropertyFile pf = new PropertyFile(file, new Language("de"));
		PropertiesWriter.keyGrouping.change("2");
		pf.grouping();

		final List<String> filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		// five empty lines, because we have six groups
		Assert.assertEquals(cntLinesInFile + 5, filedata.size());
		assertContent(filedata, file);
	}

	@Test
	public void grouping3() throws FileNotFoundException, IOException
	{
		final PropertyFile pf = new PropertyFile(file, new Language("de"));
		PropertiesWriter.keyGrouping.change("3");
		pf.grouping();

		final List<String> filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		// seven empty lines, because we have eight groups
		Assert.assertEquals(cntLinesInFile + 7, filedata.size());
		assertContent(filedata, file);
	}

	@Test
	public void grouping4() throws FileNotFoundException, IOException
	{
		final PropertyFile pf = new PropertyFile(file, new Language("de"));
		PropertiesWriter.keyGrouping.change("4");
		pf.grouping();

		final List<String> filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		// eight empty lines, because we have nine groups
		Assert.assertEquals(cntLinesInFile + 8, filedata.size());
		assertContent(filedata, file);
	}

	@Test
	public void keyGroupingLongerThenLongestKey() throws FileNotFoundException, IOException
	{
		final PropertyFile pf = new PropertyFile(file, new Language("de"));
		PropertiesWriter.keyGrouping.change("10");
		pf.grouping();

		final List<String> filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		// eight empty lines, because we have nine groups
		Assert.assertEquals(cntLinesInFile + 8, filedata.size());
		assertContent(filedata, file);
	}

	public static void assertContent(final List<String> fileData, final File testData) throws FileNotFoundException,
			IOException
	{
		try (FileInputStream fis = new FileInputStream(testData)) {
			final List<String> testDataLines = IOUtils.readLines(fis);
			for (final String tl : testDataLines) {
				if (StringUtils.isBlank(tl))
					continue;
				String find = (String) CollectionUtils.find(fileData, new Predicate() {
					@Override
					public boolean evaluate(Object object)
					{
						return object.toString().equals(tl);
					}
				});

				Assert.assertNotNull("did not found " + tl, find);
			}
		}
	}
}
