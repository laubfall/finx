package de.ludwig.finx.workspace;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.ApplicationException;

/**
 * Singleton.
 * 
 * Manages which Project is active and knows about existing {@link WorkingSetMonitoring} Objects
 * that are related to {@link WorkingSet}s of the current Project.
 * 
 * @author Daniel
 * 
 */
public class WorkspaceDao
{
	private static WorkspaceDao instance;

	private Project activeProject;

	private Map<WorkingSet, WorkingSetMonitoring> wsMonitorings = new HashMap<>();

	private WorkspaceDao()
	{

	}

	public Project activeProject()
	{
		return activeProject;
	}

	public void activateProject(final Project newActive)
	{
		Validate.notNull(newActive);
		activeProject = newActive;

		// terminate all monitorings of WorkingSets that do not belong to new active Project
		final Collection<WorkingSetMonitoring> wsms = wsMonitorings.values();
		for (WorkingSetMonitoring wsm : wsms) {
			wsm.getMonitoring().stopMonitoring();
		}

		wsMonitorings = new HashMap<>();

		// create WorkingSetMonitorings for all WorkingSets of the project
		final Set<WorkingSet> workingSets = newActive.getWorkingSets();
		for (WorkingSet ws : workingSets) {
			create(ws);
		}
	}

	public WorkingSetMonitoring create(final WorkingSet ws)
	{
		if (activeProject == null) {
			throw new ApplicationException("there is no active project");
		}

		if (activeProject.getWorkingSets().contains(ws) == false) {
			throw new ApplicationCodingException("choosen WorkingSet is not part of the active project");
		}

		if (wsMonitorings.containsKey(ws)) {
			return wsMonitorings.get(ws);
		}

		WorkingSetMonitoring wsm = new WorkingSetMonitoring(ws);
		wsMonitorings.put(ws, wsm);
		return wsm;
	}

	public static synchronized WorkspaceDao instance()
	{
		if (instance != null) {
			return instance;
		}

		instance = new WorkspaceDao();
		return instance;
	}
}
