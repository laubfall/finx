package de.ludwig.finx.gui.wizard.project;

import java.io.File;
import java.util.ArrayList;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.ludwig.finx.gui.controller.WorkingSetBackingBean;

/**
 * @author Daniel
 * 
 */
public class WorkingSetSettingsBackingBean
{
	private SimpleObjectProperty<File> propertiesDirPath = new SimpleObjectProperty<>();

	private SimpleStringProperty prefix = new SimpleStringProperty();

	private SimpleStringProperty postifx = new SimpleStringProperty();

	/**
	 * All configured WorkingSets as BackingBeans
	 */
	private SimpleListProperty<WorkingSetBackingBean> workingSetsComponent = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<WorkingSetBackingBean>()));

	/**
	 * @return the prefix
	 */
	public String getPrefix()
	{
		return prefix.get();
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix)
	{
		this.prefix.set(prefix);
	}

	public SimpleStringProperty prefixProperty()
	{
		return prefix;
	}

	/**
	 * @return the postifx
	 */
	public String getPostifx()
	{
		return postifx.get();
	}

	/**
	 * @param postifx
	 *            the postifx to set
	 */
	public void setPostifx(String postifx)
	{
		this.postifx.set(postifx);
	}

	public SimpleStringProperty postfixProperty()
	{
		return postifx;
	}

	/**
	 * @return the propertiesDirPath
	 */
	public File getPropertiesDirPath()
	{
		return propertiesDirPath.get();
	}

	/**
	 * @param propertiesDirPath
	 *            the propertiesDirPath to set
	 */
	public void setPropertiesDirPath(File propertiesDirPath)
	{
		this.propertiesDirPath.set(propertiesDirPath);
	}

	public SimpleObjectProperty<File> propertiesDirPathProperty()
	{
		return propertiesDirPath;
	}

	/**
	 * @return the workingSetsComponent
	 */
	public ObservableList<WorkingSetBackingBean> getWorkingSetsComponent()
	{
		return workingSetsComponent;
	}

	/**
	 * @param workingSetsComponent
	 *            the workingSetsComponent to set
	 */
	public void setWorkingSetsComponent(ObservableList<WorkingSetBackingBean> workingSetsComponent)
	{
		this.workingSetsComponent.set(workingSetsComponent);
	}

	public SimpleListProperty<WorkingSetBackingBean> workingSetsComponentProperty()
	{
		return workingSetsComponent;
	}
}
