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
		WorkingSetMonitoring create = workspaceDao.create(ws);
		Assert.assertNotNull(create);
		WorkingSetMonitoring create2 = workspaceDao.create(ws);
		Assert.assertNotNull(create2);
		Assert.assertTrue(create == create2);
	}
}
