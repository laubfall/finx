package de.ludwig.finx.gui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

/**
 * @author Daniel
 * 
 */
public class WorkingSetBackingBean
{
	private SimpleListProperty<File> sourceDirs = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<File>()));

	private SimpleObjectProperty<File> propDir = new SimpleObjectProperty<>();

	private SimpleStringProperty postfix = new SimpleStringProperty();

	private SimpleStringProperty prefix = new SimpleStringProperty();

	public WorkingSetBackingBean()
	{

	}

	public WorkingSetBackingBean(final File propDir, final String prefix, final String postfix,
			final Collection<File> sourceDirs)
	{
		this.propDir.set(propDir);
		this.postfix.set(postfix);
		this.prefix.set(prefix);
		this.sourceDirs.setAll(sourceDirs);
	}

	/**
	 * @return the sourceDirs
	 */
	public List<File> getSourceDirs()
	{
		return sourceDirs.get();
	}

	/**
	 * @param sourceDirs
	 *            the sourceDirs to set
	 */
	public void setSourceDirs(List<File> sourceDirs)
	{
		this.sourceDirs.setAll(sourceDirs);
	}

	public SimpleListProperty<File> sourceDirsProperty()
	{
		return sourceDirs;
	}

	/**
	 * @return the propDir
	 */
	public File getPropDir()
	{
		return propDir.get();
	}

	/**
	 * @param propDir
	 *            the propDir to set
	 */
	public void setPropDir(File propDir)
	{
		this.propDir.set(propDir);
	}

	public SimpleObjectProperty<File> propDirProperty()
	{
		return this.propDir;
	}

	/**
	 * @return the postfix
	 */
	public String getPostfix()
	{
		return postfix.get();
	}

	/**
	 * @param postfix
	 *            the postfix to set
	 */
	public void setPostfix(String postfix)
	{
		this.postfix.set(postfix);
	}

	public SimpleStringProperty postfixProperty()
	{
		return postfix;
	}

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
}
