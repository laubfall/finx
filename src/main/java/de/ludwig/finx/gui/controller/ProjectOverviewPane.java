package de.ludwig.finx.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * @author Daniel
 * 
 */
public class ProjectOverviewPane implements Initializable
{

	@FXML
	private VBox noProjectVBox;

	@FXML
	private ScrollPane projects;

	@FXML
	private VBox projectsBox;

	public ProjectOverviewPane()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		noProjects();
		projects.setContent(projectsBox);
	}

	@FXML
	private void addProject()
	{
		projects();
		ProjectSummaryComponent psc = new ProjectSummaryComponent();
		projectsBox.getChildren().add(psc);
	}

	private void noProjects()
	{
		noProjectVBox.setVisible(true);
		projects.setVisible(false);
	}

	private void projects()
	{
		noProjectVBox.setVisible(false);
		projects.setVisible(true);
	}
}
