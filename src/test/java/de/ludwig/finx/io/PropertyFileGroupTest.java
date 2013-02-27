package de.ludwig.finx.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.ludwig.finx.Language;

/**
 * @author Daniel
 * 
 */
public class PropertyFileGroupTest
{
	private File file;

	private int cntLinesInFile;

	public PropertyFileGroupTest() throws URISyntaxException, IOException
	{
		URL resource = getClass().getClassLoader().getResource("PropertyFileGroupTest/test01.properties");
		file = new File(resource.toURI());
		PropertiesWriter.keyGroupSpace.change("1");
		cntLinesInFile = FileUtils.readLines(file).size();
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
	}
}
