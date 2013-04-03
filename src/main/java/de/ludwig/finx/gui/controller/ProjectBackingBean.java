package de.ludwig.finx.gui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.workspace.Project;
import de.ludwig.finx.workspace.WorkingSet;

/**
 * @author Daniel
 * 
 */
public class ProjectBackingBean
{
	private SimpleStringProperty projectTitledPane = new SimpleStringProperty();

	private SimpleListProperty<WorkingSetBackingBean> workingSetsContainer = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<WorkingSetBackingBean>()));

	private final String projectFilename;

	/**
	 * For new Projects that are not persisted yet
	 */
	public ProjectBackingBean()
	{
		projectFilename = null;
	}

	/**
	 * Constructor to create a backing-bean from an existing project (existing means persisted).
	 * 
	 * @param project
	 * @throws if
	 *             the project is not persistent, that means if {@link Project#getSaveFileName()} is
	 *             null
	 */
	public ProjectBackingBean(final Project project) throws ApplicationCodingException
	{
		if (project.getSaveFileName() == null) {
			throw new ApplicationCodingException("constructor call with a non-persistent project");
		}

		projectFilename = project.getSaveFileName();
		projectTitledPane.set(project.getName());
		final Set<WorkingSet> workingSets = project.getWorkingSets();
		for (final WorkingSet ws : workingSets) {
			workingSetsContainer.add(new WorkingSetBackingBean(ws));
		}
	}

	/**
	 * @return the name
	 */
	public String getProjectTitledPane()
	{
		return projectTitledPane.get();
	}

	/**
	 * @param projectName
	 *            the name to set
	 */
	public void setProjectTitledPane(String projectTitledPane)
	{
		this.projectTitledPane.set(projectTitledPane);
	}

	public SimpleStringProperty projectTitledPaneProperty()
	{
		return projectTitledPane;
	}

	/**
	 * @return the workingSetsContainer
	 */
	public List<WorkingSetBackingBean> getWorkingSetsContainer()
	{
		return workingSetsContainer.get();
	}

	/**
	 * @param workingSetsContainer
	 *            the workingSetsContainer to set
	 */
	public void setWorkingSetsContainer(List<WorkingSetBackingBean> workingSetsContainer)
	{
		this.workingSetsContainer.addAll(workingSetsContainer);
	}

	public SimpleListProperty<WorkingSetBackingBean> workingSetsContainerProperty()
	{
		return workingSetsContainer;
	}

	/**
	 * @return the projectFilename
	 */
	public String getProjectFilename()
	{
		return projectFilename;
	}
}
