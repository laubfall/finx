package de.ludwig.finx.workspace;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Daniel
 * 
 */
public class WorkspaceDaoTest
{
	@Test
	public void createWorkingSetMonitoring()
	{
		final Project p = new Project("test");
		final WorkspaceDao workspaceDao = WorkspaceDao.instance();
		workspaceDao.activateProject(p);
		WorkingSet ws = p.addWorkingSet(new File("/"));
		WorkingSetMonitoring create = workspaceDao.createMonitoring(ws);
		Assert.assertNotNull(create);
		WorkingSetMonitoring create2 = workspaceDao.createMonitoring(ws);
		Assert.assertNotNull(create2);
		Assert.assertTrue(create == create2);
	}
}
