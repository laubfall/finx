package de.ludwig.finx.gui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import org.apache.log4j.Logger;

import de.ludwig.finx.gui.component.I18nViewRow;
import de.ludwig.finx.gui.component.ProjectBackingBean;
import de.ludwig.finx.gui.component.WorkingSetBackingBean;
import de.ludwig.finx.gui.component.accordion.AccordionTitledPaneBackingBean;
import de.ludwig.finx.gui.popup.AppSettingsPopup;
import de.ludwig.finx.io.PropertiesReader;
import de.ludwig.finx.io.RootNode;
import de.ludwig.finx.jfx.event.SelectListItemEvent;
import de.ludwig.finx.jfx.event.SelectWorkingSetEvent;
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

	private static final Logger LOG = Logger.getLogger(AppLayoutAnchorPane.class);

	public AppLayoutAnchorPane()
	{
		super(AppLayoutAnchorPane.class.getResource("/de/ludwig/finx/gui/fxml/AppLayoutAnchorPane.fxml"));

		model.bind();

		// Event-Handler for updating the TableView if user selects another WorkingSet
		getContent().addEventHandler(SelectListItemEvent.SELECT, new EventHandler<Event>() {
			@Override
			public void handle(Event event)
			{
				final SelectWorkingSetEvent ev = (SelectWorkingSetEvent) event;
				final WorkingSetBackingBean ws = ev.getSelectedItem();
				final PropertiesReader pr = new PropertiesReader(ws.getPropDir(), ws.getPostfix(), ws.getPrefix());
				final RootNode nodeView = pr.createNodeView();

				final ObservableList<I18nViewRow> viewData = I18nViewHelper.createViewData(nodeView);
				if (viewData.isEmpty() == false) {
					I18nViewHelper.addColumns(i18nView, nodeView);
				}

				i18nView.getItems().clear();
				i18nView.itemsProperty().set(viewData);
			}
		});
	}

	@FXML
	private void openWorkspace(Event e)
	{
		final WorkspacePersistencyDao pers = new WorkspacePersistencyDao();
		final ProjectsInfo loadInfo = pers.loadInfo();
		model.getModelObject().getProjectOverviewPaneController().getProjectsView2().getItems().clear();

		final List<AccordionTitledPaneBackingBean<ProjectBackingBean>> projects = new ArrayList<>();
		for (final ProjInfo pi : loadInfo.getInfo()) {
			final Project project = pers.loadProjectBySaveFileName(pi.getProjectSaveFileName());
			final ProjectBackingBean pbb = new ProjectBackingBean(project);
			projects.add(new AccordionTitledPaneBackingBean<ProjectBackingBean>(pbb));
		}

		model.getModelObject().getProjectOverviewPaneController().getProjectsView2().setItems(projects);
		LOG.debug(String.format("loaded %d projects", projects.size()));
	}

	@FXML
	private void saveWorkspaceAs(Event e)
	{
		final WorkspacePersistencyDao pers = new WorkspacePersistencyDao();

		final ProjectListPaneBackingBean projectList = model.getModelObject().getProjectOverviewPaneController();

		List<AccordionTitledPaneBackingBean<ProjectBackingBean>> items = projectList.getProjectsView2().getItems();
		LOG.debug(String.format("saving %d projects due to gui event", items.size()));
		for (final AccordionTitledPaneBackingBean<ProjectBackingBean> projBackingBean : items) {
			final ProjectBackingBean pbb = projBackingBean.getTitledPaneContentModelObject();
			final Project project = pbb.convert();

			if (pbb.getProjectFilename() != null) {
				final Project persisted = pers.loadProjectBySaveFileName(pbb.getProjectFilename());
				pers.updateProject(project, persisted);
			} else {
				pers.saveProject(project);
			}
			LOG.debug(String.format("saved project %s due to gui event", pbb.getText()));
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
