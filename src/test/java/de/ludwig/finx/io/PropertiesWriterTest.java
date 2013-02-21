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
import org.junit.Test;

/**
 * @author Daniel
 */
public class PropertiesWriterTest
{
	@Test
	public void writerProperties() throws FileNotFoundException, IOException
	{
		File source = File.createTempFile("writeProperties", null);
		final PropertiesWriter p = new PropertiesWriter(source, Locale.GERMAN);
		RootNode r = new RootNode();
		r.addAll("de.test", "hon schon zu 青");
		r.addAll("de.test.test", "ascii-only");

		p.process(r);
		Assert.assertTrue(source.exists());

		final List<String> propertyLines = FileUtils.readLines(source);
		Assert.assertEquals(2, propertyLines.size());
		Assert.assertEquals("de.test=hon schon zu \u9752", propertyLines.get(0));
		Assert.assertEquals("de.test.test=ascii-only", propertyLines.get(1));
	}

	/**
	 * Simply test if it is possible to make some modifications at a "random"
	 * position of the file
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
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
			Assert.assertEquals("de.test=etwas text - - - t e s t - - - ", line);
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
