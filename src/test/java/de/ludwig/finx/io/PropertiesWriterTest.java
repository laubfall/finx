package de.ludwig.finx.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import de.ludwig.finx.Language;
import de.ludwig.finx.SetttingsAwareTest;

/**
 * @author Daniel
 */
public class PropertiesWriterTest extends SetttingsAwareTest
{
	@Test
	public void writerProperties() throws FileNotFoundException, IOException, URISyntaxException
	{
		File target = null;

		try {
			URL resource = getClass().getClassLoader().getResource(
					"PropertiesWriterTest/writerPropertiesTest.properties");
			final PropertyFile pf = new PropertyFile(new File(resource.toURI()), new Language(Locale.GERMAN));
			PropertyFile.preservePropertyLayout.change(PropertyPreserveMode.STRICT.name());
			// PropertiesWriter.keyGrouping.change("0");
			// PropertiesWriter.keyGroupSpace.change("0");
			// PropertiesWriter.attachCommentsWithEmptyLineCount.change("0");
			target = File.createTempFile("newlyWritten", null);
			final PropertiesWriter pw = new PropertiesWriter(target);
			pw.write(pf);
			final List<String> content = FileUtils.readLines(target);
			Assert.assertNotNull(content);

			// # some comment
			// de.ludwig.finx=application
			// de.ludwig=name
			//
			// # comment after empty line

			Assert.assertEquals("# some comment", content.get(0));
			Assert.assertEquals("de.ludwig.finx=application", content.get(1));
			Assert.assertEquals("de.ludwig=name", content.get(2));
			Assert.assertEquals("", content.get(3));
			Assert.assertEquals("# comment after empty line", content.get(4));
			Assert.assertEquals(5, content.size());
		} finally {
			boolean deleteQuietly = FileUtils.deleteQuietly(target);
			System.out.println(deleteQuietly);
		}
	}

	/**
	 * Simply test if it is possible to make some modifications at a "random" position of the file
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	@Ignore
	public void randomAccess() throws IOException, URISyntaxException
	{
		URL resource = getClass().getClassLoader().getResource("PropertiesWriterTest/test.properties.txt");
		Assert.assertNotNull(resource);
		File file = new File(resource.toURI());
		long sizeOfFile = FileUtils.sizeOf(file);
		String line = null;
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			while ((line = raf.readLine()) != null) {
				final String strToInsert = "---test---\n";
				String filePart = filePart(file, raf.getFilePointer());
				raf.setLength(sizeOfFile + strToInsert.length());
				raf.writeChars(strToInsert);
				raf.writeChars(filePart);
			}
		}

		try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			line = raf.readLine();
			Assert.assertNotNull(line);
			Assert.assertEquals("de.test=etwas text", line);
		}
	}

	private String filePart(File f, long offset) throws IOException
	{
		try (RandomAccessFile rafR = new RandomAccessFile(f, "r")) {
			rafR.seek(offset);
			String result = null;
			StringBuilder sb = new StringBuilder();
			while ((result = rafR.readLine()) != null) {
				sb.append(result);
			}
			return sb.toString();
		}
	}
}
