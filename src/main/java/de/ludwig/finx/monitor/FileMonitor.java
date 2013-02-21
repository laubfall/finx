package de.ludwig.finx.monitor;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * Class for monitoring changes in disk files. Usage:
 * 
 * 1. Implement the FileListener interface. 2. Create a FileMonitor instance. 3.
 * Add the file(s)/directory(ies) to listen for.
 * 
 * fileChanged() will be called when a monitored file is created, deleted or its
 * modified time changes.
 * 
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public class FileMonitor
{
	private static final Logger LOG = Logger.getLogger(FileMonitor.class);

	private Timer timer_;
	private HashMap<File, Long> files_; // File -> Long
	private Collection<WeakReference<FileListener>> listeners_; // of
																// WeakReference(FileListener)
	private long pollingInterval;

	/**
	 * Indicates that the monitor has started but not if there is any timer
	 * running (actuallly if the monitor has been started a timer should also
	 * running)
	 * 
	 * This information can be necessary if other services needs to know if the
	 * monitor is running to continue their work.
	 */
	private boolean started;

	/**
	 * Create a file monitor instance with specified polling interval.
	 * 
	 * @param pollingInterval
	 *            Polling interval in milli seconds.
	 */
	public FileMonitor(long pollingInterval)
	{
		files_ = new HashMap<File, Long>();
		listeners_ = new ArrayList<WeakReference<FileListener>>();

		timer_ = createFileMonitorTimer();
		this.pollingInterval = pollingInterval;
	}

	/**
	 * Remove listener from this file monitor.
	 * 
	 * @param fileListener
	 *            Listener to remove.
	 */
	public void removeListener(FileListener fileListener)
	{
		for (Iterator<WeakReference<FileListener>> i = listeners_.iterator(); i.hasNext();) {
			WeakReference<FileListener> reference = (WeakReference<FileListener>) i.next();
			FileListener listener = (FileListener) reference.get();
			if (listener == fileListener) {
				i.remove();
				break;
			}
		}
	}

	void start()
	{
		LOG.info("starting file monitoring");
		started = true;
		timer_.schedule(new FileMonitorNotifier(), 0, pollingInterval);
	}

	/**
	 * Stop the file monitor polling.
	 */
	void stop()
	{
		LOG.info("stopping file monitoring");
		timer_.cancel();
		timer_ = createFileMonitorTimer();
		started = false;
	}

	/**
	 * Add file to listen for. File may be any java.io.File (including a
	 * directory) and may well be a non-existing file in the case where the
	 * creating of the file is to be trepped.
	 * <p>
	 * More than one file can be listened for. When the specified file is
	 * created, modified or deleted, listeners are notified.
	 * 
	 * @param file
	 *            File to listen for.
	 */
	void addFile(File file)
	{
		if (files_.containsKey(file) == false) {
			long modifiedTime = file.exists() ? file.lastModified() : -1;
			files_.put(file, new Long(modifiedTime));
			LOG.info(String.format("added file %s for monitoring", file.getName()));
		}
	}

	/**
	 * Remove specified file for listening.
	 * 
	 * @param file
	 *            File to remove.
	 */
	void removeFile(File file)
	{
		files_.remove(file);
	}

	/**
	 * Add listener to this file monitor.
	 * 
	 * @param fileListener
	 *            Listener to add.
	 */
	void addListener(FileListener fileListener)
	{
		// Don't add if its already there
		for (Iterator<WeakReference<FileListener>> i = listeners_.iterator(); i.hasNext();) {
			WeakReference<FileListener> reference = (WeakReference<FileListener>) i.next();
			FileListener listener = (FileListener) reference.get();
			if (listener == fileListener)
				return;
		}

		// Use WeakReference to avoid memory leak if this becomes the
		// sole reference to the object.
		listeners_.add(new WeakReference<FileListener>(fileListener));
		LOG.debug("added fileListener " + fileListener.getClass().getName());
	}

	private Timer createFileMonitorTimer()
	{
		return new Timer(true);
	}

	/**
	 * This is the timer thread which is executed every n milliseconds according
	 * to the setting of the file monitor. It investigates the file in question
	 * and notify listeners if changed.
	 */
	private class FileMonitorNotifier extends TimerTask
	{
		public void run()
		{
			// Loop over the registered files and see which have changed.
			// Use a copy of the list in case listener wants to alter the
			// list within its fileChanged method.
			Collection<File> files = new ArrayList<File>(files_.keySet());

			for (Iterator<File> i = files.iterator(); i.hasNext();) {
				File file = i.next();
				long lastModifiedTime = ((Long) files_.get(file)).longValue();
				long newModifiedTime = file.exists() ? file.lastModified() : -1;

				LOG.debug(String.format("FileMonitor is processing File %s lastModified %d newModifiedTime %d",
						file.getName(), lastModifiedTime, newModifiedTime));

				// Check if file has changed
				if (newModifiedTime != lastModifiedTime) {
					LOG.info(String.format("file %s has changed", file.getName()));
					// Register new modified time
					files_.put(file, new Long(newModifiedTime));

					// Notify listeners
					for (Iterator<WeakReference<FileListener>> j = listeners_.iterator(); j.hasNext();) {
						WeakReference<FileListener> reference = (WeakReference<FileListener>) j.next();
						FileListener listener = (FileListener) reference.get();

						// Remove from list if the back-end object has been GC'd
						if (listener == null) {
							LOG.debug("listener was GC'd ");
							j.remove();
						} else {
							LOG.debug("calling fileListener " + listener.getClass().getName());
							listener.fileChanged(file);
						}
					}
				}
			}
		}
	}

	/**
	 * @return the started
	 */
	public boolean isStarted()
	{
		return started;
	}
}
