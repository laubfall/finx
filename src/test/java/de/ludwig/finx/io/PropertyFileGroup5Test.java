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
 * @author Daniel
 * 
 */
public class PropertyFileGroup5Test extends BasePropertyFileGroupTest
{
	private File file;

	private int cntLinesInFile;

	public PropertyFileGroup5Test() throws URISyntaxException, IOException
	{
		URL resource = getClass().getClassLoader().getResource("PropertyFileGroupTest/test05.properties");
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
		callGrouping(pf);

		List<String> filedata = pf.filedata();
		Assert.assertNotNull(filedata);
		Assert.assertEquals(cntLinesInFile, filedata.size());

		PropertiesWriter.keyGroupSpace.change("2");
		callGrouping(pf);

		filedata = pf.filedata();
		Assert.assertNotNull(filedata);

		Assert.assertEquals(cntLinesInFile, filedata.size());
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
