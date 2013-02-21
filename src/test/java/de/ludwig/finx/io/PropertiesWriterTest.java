package de.ludwig.finx.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Daniel
 * 
 */
@Ignore
public class PropertiesWriterTest
{
//	@Test
//	public void writerProperties() throws FileNotFoundException, IOException
//	{
//		Properties p = new PropertiesWriter();
//		p.put("jp.micromata", "hon schon zu 青");
//		p.put("de.micromata", "ein Wert mit nur ascii-zeichen");
//		p.store(new FileOutputStream(new File("/Users/Daniel/Desktop/test.proprtiers.txt")), "");
//
//		System.out.println("hon schon zu \u9752");
//	}

	@Test
	public void bigFile() throws FileNotFoundException, IOException
	{
		Properties p = new Properties() {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.Properties#store(java.io.OutputStream,
			 * java.lang.String)
			 */
			@Override
			public void store(OutputStream arg0, String arg1) throws IOException
			{
				try
				{
					super.store(new BufferedOutputStream(arg0) {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.io.BufferedOutputStream#write(byte[], int,
						 * int)
						 */
						@Override
						public synchronized void write(byte[] arg0, int arg1, int arg2) throws IOException
						{
							baos.write(arg0);
						}

					}, arg1);
					
					String raw = baos.toString("ISO-8859-1");
					LineNumberReader lnr = new LineNumberReader(new StringReader(raw));
					
				} finally
				{
					baos.close();
				}

			}

		};
		p.load(new FileInputStream(
				new File(
						"/Users/Daniel/Documents/workspace-mwo-3/vw-cfgrepository/P1.0.1.I18N_VWGlobal_de_DE_I18NProperties.properties")));

		p.store(new FileOutputStream(new File("/Users/Daniel/Desktop/test.properties.txt")), "");
	}

	@Test
	public void randomAccess() throws IOException
	{
		File file = new File("/Users/Daniel/Desktop/test.proprtiers.txt");
		long sizeOfFile = FileUtils.sizeOf(file);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		String line = null;
		while ((line = raf.readLine()) != null)
		{
			if (line.startsWith("de"))
			{
				final String strToInsert = "---test---\n";
				String filePart = filePart(file, raf.getFilePointer());
				raf.setLength(sizeOfFile + strToInsert.length());
				raf.writeChars(strToInsert);
				raf.writeChars(filePart);
			}
		}

		raf = new RandomAccessFile(file, "r");
		while ((line = raf.readLine()) != null)
		{
			System.out.println(line);
		}
	}

	private String filePart(File f, long offset) throws IOException
	{
		final RandomAccessFile rafR = new RandomAccessFile(f, "r");
		try
		{
			rafR.seek(offset);
			String result = null;
			StringBuilder sb = new StringBuilder();
			while ((result = rafR.readLine()) != null)
			{
				sb.append(result);
			}
			return sb.toString();
		} finally
		{
			rafR.close();
		}
	}
}
