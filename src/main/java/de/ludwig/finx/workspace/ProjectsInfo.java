package de.ludwig.finx.workspace;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Holds track about all saved Projects. It delivers a top-level view, so you don't have to load all
 * projects to get some basic informations.
 * 
 * @author Daniel
 * 
 */
public class ProjectsInfo implements Serializable
{
	private static final long serialVersionUID = -5071440238482833582L;

	private List<ProjInfo> info = new ArrayList<>();

	void addOrUpdateProjInfo(final Project project, final File saveFile)
	{
		ProjInfo find = (ProjInfo) CollectionUtils.find(info, new ProjInfoPredicate(saveFile));

		if (find == null) {
			find = new ProjInfo();
			find.setProjectSaveFileName(saveFile.getName());
			info.add(find);
		}

		find.setProjectName(project.getName());
	}

	void removeProjInf(final Project project, final File saveFile)
	{
		ProjInfo find = (ProjInfo) CollectionUtils.find(info, new ProjInfoPredicate(saveFile));
		info.remove(find);
	}

	/**
	 * @return the info
	 */
	public List<ProjInfo> getInfo()
	{
		return info;
	}

	static class ProjInfoPredicate implements Predicate
	{
		private File saveFile;

		/**
		 * @param saveFile
		 */
		public ProjInfoPredicate(File saveFile)
		{
			super();
			this.saveFile = saveFile;
		}

		@Override
		public boolean evaluate(Object object)
		{
			final ProjInfo pi = (ProjInfo) object;
			return pi.getProjectSaveFileName().equals(saveFile.getName());
		}

	}
}