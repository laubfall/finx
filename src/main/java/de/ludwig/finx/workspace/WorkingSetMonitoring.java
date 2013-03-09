package de.ludwig.finx.workspace;

import de.ludwig.finx.io.PropertyFileHandling;
import de.ludwig.finx.monitor.Monitoring;

/**
 * Könnte die Verknüpfung von WorkingSet und Monitoring sein.
 * 
 * @author Daniel
 * 
 */
public class WorkingSetMonitoring
{
	private WorkingSet ws;

	private Monitoring monitor;

	private PropertyFileHandling pfh;

	public WorkingSetMonitoring(final WorkingSet ws)
	{
		this.ws = ws;
		monitor = new Monitoring();
		pfh = new PropertyFileHandling();
		pfh.setupPropertiesReader(ws.getPropertiesDir(), ws.getI18nPropertiesFilePostfix(),
				ws.getI18nPropertiesFilePrefix());

		monitor.addSrcDirectoriesToMonitor(ws.getSourceDirsAsList());
	}

	public PropertyFileHandling getPfh()
	{
		return pfh;
	}

	public Monitoring getMonitoring()
	{
		return monitor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ws == null) ? 0 : ws.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkingSetMonitoring other = (WorkingSetMonitoring) obj;
		if (ws == null) {
			if (other.ws != null)
				return false;
		} else if (!ws.equals(other.ws))
			return false;
		return true;
	}
}
