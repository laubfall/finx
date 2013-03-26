package de.ludwig.finx.workspace;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.ludwig.finx.ApplicationCodingException;

/**
 * The representation of a project of any type that has I18n-property-files.
 * 
 * @author Daniel
 * 
 */
public class Project implements Serializable
{
	private static final long serialVersionUID = -3455274013485359715L;

	/**
	 * Not optional.
	 */
	private String name;

	private Set<WorkingSet> workingSets;

	/**
	 * Because the name {@link Project#name} has not to be unique we cannot use it as a name for the
	 * savefile. Thats the reason why we save the save-file-name at the project object.
	 */
	private String saveFileName;

	public Project(String name)
	{
		Validate.isTrue(StringUtils.isNotBlank(name));
		this.name = name;
		workingSets = new HashSet<>();
	}

	public final WorkingSet addWorkingSet(final File propertiesDir, final File... sourceFiles)
	{
		final WorkingSet wExi = workingSetByPropertiesDir(propertiesDir);
		if (wExi != null) {
			// we don't return the existing one because this can lead to confusing behaviour because
			// the caller of this method expected the given sourceFiles but we use the sourceFiles
			// that where used for constructing the WorkingSet
			throw new ApplicationCodingException(String.format(
					"a workingset for this property-files %s already exists", propertiesDir.getAbsolutePath()));
		}

		final WorkingSet ws = new WorkingSet(propertiesDir, sourceFiles);
		workingSets.add(ws);
		return ws;
	}

	public final WorkingSet workingSetByPropertiesDir(final File propertiesDir)
	{
		return (WorkingSet) CollectionUtils.find(workingSets, new Predicate() {

			@Override
			public boolean evaluate(Object object)
			{
				final WorkingSet ws = (WorkingSet) object;
				return ws.getPropertiesDir().equals(propertiesDir);
			}
		});
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Package protected to avoid modifications from classes that do not belong to this package.
	 * This is necessary because this field plays a central role in how instances of Project are
	 * persisted.
	 * 
	 * @param saveFileName
	 */
	final void setSaveFileName(String saveFileName)
	{
		this.saveFileName = saveFileName;
	}

	public String getSaveFileName()
	{
		return this.saveFileName;
	}

	/**
	 * @return the workingSets
	 */
	public Set<WorkingSet> getWorkingSets()
	{
		return workingSets;
	}
}
