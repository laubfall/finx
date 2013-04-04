package de.ludwig.finx.workspace;

import java.io.Serializable;

/**
 * 
 * @author Daniel
 * 
 */
public class ProjInfo implements Serializable
{
	private static final long serialVersionUID = -4295560827410959192L;

	private String projectName;

	private String projectSaveFileName;

	/**
	 * @return the projectInfo
	 */
	public String getProjectName()
	{
		return projectName;
	}

	/**
	 * @param projectInfo
	 *            the projectInfo to set
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	/**
	 * @return the projectSaveFileName
	 */
	public String getProjectSaveFileName()
	{
		return projectSaveFileName;
	}

	/**
	 * @param projectSaveFileName
	 *            the projectSaveFileName to set
	 */
	public void setProjectSaveFileName(String projectSaveFileName)
	{
		this.projectSaveFileName = projectSaveFileName;
	}
}