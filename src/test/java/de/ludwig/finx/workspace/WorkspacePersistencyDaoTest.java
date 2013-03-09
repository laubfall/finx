package de.ludwig.finx.workspace;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ludwig.finx.settings.AppSettings;

/**
 * @author Daniel
 * 
 */
public class WorkspacePersistencyDaoTest
{
	@Before
	public void setup() throws IOException
	{
		Path projectDir = Files.createTempDirectory("workspaceDaoTest");
		AppSettings.projectSaveDir.change(projectDir.toFile().getAbsolutePath());
	}

	@After
	public void tearDown() throws IOException
	{
		FileUtils.deleteDirectory(AppSettings.projectSaveDir.setting());
	}

	@Test
	public void saveSimple()
	{
		final WorkspacePersistencyDao dao = new WorkspacePersistencyDao();
		dao.saveProject(createProject("test"));
		ProjectsInfo info = dao.loadInfo();
		Assert.assertNotNull(info);
		final List<ProjInfo> projInfos = info.getInfo();
		Assert.assertNotNull(projInfos);
		Assert.assertEquals(1, projInfos.size());

		final Project prj = dao.loadProject(new File(AppSettings.projectSaveDir.setting(), projInfos.get(0)
				.getProjectSaveFileName()));
		Assert.assertNotNull(prj);
		WorkingSet workingSetByPropertiesDir = prj.workingSetByPropertiesDir(new File("/"));
		Assert.assertNotNull(workingSetByPropertiesDir);

		dao.deleteProject(prj);
		info = dao.loadInfo();
		Assert.assertNotNull(info);
		Assert.assertTrue(info.getInfo().isEmpty());
	}

	@Test
	public void saveMultiple()
	{
		final WorkspacePersistencyDao dao = new WorkspacePersistencyDao();
		dao.saveProject(createProject("test"));
		dao.saveProject(createProject("test"));

		ProjectsInfo info = dao.loadInfo();
		Assert.assertNotNull(info);
		List<ProjInfo> projInfos = info.getInfo();
		Assert.assertFalse(projInfos.isEmpty());
		Assert.assertEquals(2, projInfos.size());
		ProjInfo pi1 = projInfos.get(0);
		ProjInfo pi2 = projInfos.get(1);
		Assert.assertFalse(pi1.getProjectSaveFileName().equals(pi2.getProjectSaveFileName()));
		Assert.assertEquals("proj_test.prj", pi1.getProjectSaveFileName());
		Assert.assertEquals("proj_test_001.prj", pi2.getProjectSaveFileName());
	}

	@Test
	public void saveAndRename()
	{
		final WorkspacePersistencyDao dao = new WorkspacePersistencyDao();
		final Project proj = createProject("test");
		dao.saveProject(proj);
		proj.setName("test-neu");

		dao.saveProject(proj);
		ProjectsInfo loadInfo = dao.loadInfo();
		List<ProjInfo> projInfos = loadInfo.getInfo();
		Assert.assertNotNull(projInfos);
		Assert.assertFalse(projInfos.isEmpty());
		Assert.assertEquals(1, projInfos.size());

		ProjInfo pi1 = projInfos.get(0);
		Assert.assertNotNull(pi1);
		Assert.assertEquals("test-neu", pi1.getProjectName());
	}

	private Project createProject(final String name)
	{
		final Project p = new Project(name);
		p.addWorkingSet(new File("/"), new File("sources/s1"), new File("sources/s2"));
		return p;
	}
}
