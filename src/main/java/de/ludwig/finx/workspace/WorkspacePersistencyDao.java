package de.ludwig.finx.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.ApplicationException;
import de.ludwig.finx.settings.AppSettings;

/**
 * Can: - save Project - load Project
 * 
 * @author Daniel
 * 
 */
public class WorkspacePersistencyDao
{
	public static final String PROJ_FILE_PREFIX = "proj_";

	public static final String PROJ_FILE_POSTFIX = ".prj";

	public static final String PROJ_INFO_FILE_NAME = "proj.info";

	private static final Logger LOG = Logger.getLogger(WorkspacePersistencyDao.class);

	public final void saveProject(final Project project)
	{
		Validate.notNull(project);
		final File setting = AppSettings.projectSaveDir.setting();
		final String fileName = fileName(project);
		final File projectFile = new File(setting, fileName);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(projectFile));) {
			oos.writeObject(project);
			updateProjectsInfo(project); // TODO how to proceed if this fails?
		} catch (IOException e) {
			LOG.error("unable to save project", e);
			throw new ApplicationCodingException("IOException while saving project");
		}
	}

	public final Project loadProject(final File projSaveFile)
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(projSaveFile));) {
			return (Project) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("Exception while loading project Object", e);
			throw new ApplicationCodingException("could not load project Object");
		}
	}

	public final void deleteProject(final Project project)
	{
		final File file = new File(AppSettings.projectSaveDir.setting(), project.getSaveFileName());
		final boolean delete = file.delete();
		if (delete == false) {
			LOG.error("could not delete project-file: " + file.getAbsolutePath());
			throw new ApplicationException("deletion of projects-file failed");
		}
		deleteFromProjectsInfo(project);
	}

	public final ProjectsInfo loadInfo()
	{
		final File piFile = new File(AppSettings.projectSaveDir.setting(), PROJ_INFO_FILE_NAME);
		if (piFile.exists() == false) {
			final ProjectsInfo pi = new ProjectsInfo();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(piFile))) {
				oos.writeObject(pi);
				LOG.info("ProjectsInfo written to: " + piFile.getAbsolutePath());
				return pi;
			} catch (IOException e) {
				LOG.error("Exception while writing ProjectsInfo", e);
			}
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(piFile));) {
			final ProjectsInfo pi = (ProjectsInfo) ois.readObject();
			return pi;
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("Exceptionw while loading ProjectsInfo", e);
		}
		return null;
	}

	private void updateProjectsInfo(final Project project)
	{
		ProjectsInfo pi = loadInfo();
		pi.addOrUpdateProjInfo(project, new File(AppSettings.projectSaveDir.setting(), project.getSaveFileName()));
		saveProjectsInfo(pi);
	}

	private void deleteFromProjectsInfo(final Project project)
	{
		ProjectsInfo pi = loadInfo();
		pi.removeProjInf(project, new File(AppSettings.projectSaveDir.setting(), project.getSaveFileName()));
		saveProjectsInfo(pi);
	}

	private void saveProjectsInfo(final ProjectsInfo pi)
	{
		final File piFile = new File(AppSettings.projectSaveDir.setting(), PROJ_INFO_FILE_NAME);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(piFile));) {
			oos.writeObject(pi);
			LOG.info("saved projectsInfo to " + piFile.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("Exceptionwhile saving ProjectsInfo to file " + piFile.getAbsolutePath());
			throw new ApplicationException("ProjectsInfo could not be saved");
		}
	}

	/**
	 * 
	 * @return
	 */
	private String fileName(final Project project)
	{
		String saveFileName = project.getSaveFileName();
		String tmpFileName = null;
		if (saveFileName == null) {
			tmpFileName = fileName(project.getName());
			File saveFile = containsSaveFile(tmpFileName);
			if (saveFile == null) {
				project.setSaveFileName(tmpFileName);
			} else {
				// create name that is not in use
				int cnt = 1;
				saveFile = null;
				String fileNameExt;
				do {
					fileNameExt = fileNameExt(project.getName(), cnt);
					saveFile = containsSaveFile(fileNameExt);
					cnt++;
				} while (saveFile != null);

				project.setSaveFileName(fileNameExt);
			}
		}

		return project.getSaveFileName();
	}

	private File containsSaveFile(final String fileName)
	{
		final Iterator<File> iter = FileUtils.iterateFiles(AppSettings.projectSaveDir.setting(),
				new String[] { StringUtils.removeStart(PROJ_FILE_POSTFIX, ".") }, false);
		while (iter.hasNext()) {
			final File next = iter.next();
			if (next.getName().toLowerCase().equals(fileName)) {
				return next;
			}
		}

		return null;
	}

	private String fileName(String projectName)
	{
		return (PROJ_FILE_PREFIX + projectName + PROJ_FILE_POSTFIX).toLowerCase();
	}

	private String fileNameExt(String projectName, int cnt)
	{
		// lower case to make sure we do not get into trouble if running on different os's
		return (PROJ_FILE_PREFIX + projectName + String.format("_%03d", cnt) + PROJ_FILE_POSTFIX).toLowerCase();
	}
}
