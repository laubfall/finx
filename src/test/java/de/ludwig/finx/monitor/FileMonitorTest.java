/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.monitor;

import java.io.File;
import java.io.IOException;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.ludwig.finx.monitor.FileListener;
import de.ludwig.finx.monitor.FileMonitor;

/**
 * 
 * @author Daniel
 */
public class FileMonitorTest
{

	@Test
	public void testSimple() throws IOException, InterruptedException
	{
		FileMonitor fm = new FileMonitor(100);

		class TestListener implements FileListener
		{

			private boolean changeDetected = false;

			public void fileChanged(File file)
			{
				changeDetected = true;
			}
		}

		TestListener t = new TestListener();
		fm.addListener(t);
		fm.start();

		final File f = File.createTempFile("fileMonitorTest", null);
		fm.addFile(f);
		
		FileUtils.write(f, "a Change");

		Thread.sleep(500);
		
		Assert.assertTrue(f.exists());
		Assert.assertTrue(t.changeDetected);
	}
}
