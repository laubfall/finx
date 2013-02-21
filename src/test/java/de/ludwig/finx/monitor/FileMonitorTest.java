package de.ludwig.finx.monitor;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

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

		TestListener listener = new TestListener();
		fm.addListener(listener);
		fm.start();

		File fileMock = PowerMockito.mock(File.class);
		Assert.assertNotNull(fileMock);
		PowerMockito.when(fileMock.lastModified()).thenReturn(1L, 2L);
		PowerMockito.when(fileMock.exists()).thenReturn(true);
		PowerMockito.when(fileMock.getName()).thenReturn("/file/by/powermock.mock");

		fm.addFile(fileMock);

		Thread.sleep(100);

		// Checks if the listener was triggered
		Assert.assertTrue(listener.changeDetected);
	}

	@Test
	public void testStartStop()
	{
		try {
			final FileMonitor fm = new FileMonitor(10);
			fm.start();
			fm.stop();
			fm.start();
			fm.stop();
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
