package de.ludwig.finx.command.impl;

import de.ludwig.finx.command.Command;
import de.ludwig.finx.command.CommandException;
import de.ludwig.finx.workspace.Project;

/**
 * A command that enables the user to create, update or delete projects.
 * 
 * @author Daniel
 * 
 */
public class ProjectCommand implements Command<Project>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.command.Command#execute(java.lang.String)
	 */
	@Override
	public Project execute(String payload) throws CommandException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.command.Command#name()
	 */
	@Override
	public String name()
	{
		return "project";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.finx.command.Command#help()
	 */
	@Override
	public String help()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
