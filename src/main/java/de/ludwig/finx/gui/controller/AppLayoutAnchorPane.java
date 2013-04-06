package de.ludwig.finx.gui.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import de.ludwig.finx.gui.component.I18nViewRow;
import de.ludwig.finx.gui.component.ProjectBackingBean;
import de.ludwig.finx.gui.component.WorkingSetBackingBean;
import de.ludwig.finx.gui.popup.AppSettingsPopup;
import de.ludwig.finx.jfx.event.SelectListItemEvent;
import de.ludwig.finx.workspace.ProjInfo;
import de.ludwig.finx.workspace.Project;
import de.ludwig.finx.workspace.ProjectsInfo;
import de.ludwig.finx.workspace.WorkspacePersistencyDao;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;

/**
 * @author Daniel
 * 
 */
public class AppLayoutAnchorPane extends BaseController
{
	@FXML
	private MenuItem menuItemClose;

	@FXML
	private TableView<I18nViewRow> i18nView;

	/**
	 * the include has the id projectOverviewPane. If you name this field like the id, you will run
	 * into a ClassCastException or something similar because javafx tries to convert the
	 * Root-Element of the included fxml into this field. But the field is a controller. To get the
	 * controller of the included fxml simply attach "Controller" to the end of the fields name!
	 */
	@BindToBeanProperty
	@FXML
	private ProjectListPane projectOverviewPaneController;

	private Model<AppLayoutAnchorPaneBackingBean> model = new Model<AppLayoutAnchorPaneBackingBean>(this,
			new AppLayoutAnchorPaneBackingBean());

	public AppLayoutAnchorPane()
	{
		super(AppLayoutAnchorPane.class.getResource("/de/ludwig/finx/gui/fxml/AppLayoutAnchorPane.fxml"));

		model.bind();

		getContent().addEventHandler(SelectListItemEvent.SELECT, new EventHandler<Event>() {

			@Override
			public void handle(Event event)
			{
				System.out.println("table received item select");
				SelectListItemEvent<WorkingSetBackingBean> ev = (SelectListItemEvent<WorkingSetBackingBean>) event;

			}
		});
	}

	@FXML
	private void mouseClickedCapture(Event e)
	{
		System.out.println("clicked");
	}

	@FXML
	private void openWorkspace(Event e)
	{
		final WorkspacePersistencyDao pers = new WorkspacePersistencyDao();
		final ProjectsInfo loadInfo = pers.loadInfo();
		model.getModelObject().getProjectOverviewPaneController().getProjectsView().clear();

		for (final ProjInfo pi : loadInfo.getInfo()) {
			final Project project = pers.loadProjectBySaveFileName(pi.getProjectSaveFileName());
			final ProjectBackingBean pbb = new ProjectBackingBean(project);
			model.getModelObject().getProjectOverviewPaneController().getProjectsView().add(pbb);
		}
	}

	@FXML
	private void saveWorkspaceAs(Event e)
	{
		final WorkspacePersistencyDao pers = new WorkspacePersistencyDao();

		final ProjectListPaneBackingBean projectList = model.getModelObject().getProjectOverviewPaneController();

		for (final ProjectBackingBean projBackingBean : projectList.getProjectsView()) {
			final Project project = projBackingBean.convert();

			if (projBackingBean.getProjectFilename() != null) {
				final Project persisted = pers.loadProjectBySaveFileName(projBackingBean.getProjectFilename());
				pers.updateProject(project, persisted);
			} else {
				pers.saveProject(project);
			}
		}
	}

	@FXML
	private void editPreferences(ActionEvent e)
	{
		final AppSettingsPopup asp = new AppSettingsPopup();
		asp.show();
	}

	/**
	 * @return the content
	 */
	public AnchorPane getContent()
	{
		return (AnchorPane) super.getContent();
	}

	public Group grouped()
	{
		return new Group(getContent());
	}
}
