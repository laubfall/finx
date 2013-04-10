package de.ludwig.finx.gui.component.accordion;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import org.apache.log4j.Logger;

import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombined;

/**
 * Accordion that supports binding of a backing-bean via modelfx.
 * 
 * Instead of creating the titled-panes of this accordion by hand the accordion itself create the
 * titled-panes depending on the given backing-beans.
 * 
 * @author Daniel
 * 
 * @param <T>
 *            type of the backing-bean of a titled-pane
 * @param <R>
 *            type of the backing-bean of the component that is going to be rendered in the content
 *            area of the titled-pane
 */
public class ModelBindedAccordion<T extends AccordionTitledPaneBackingBean<R>, R> extends Accordion implements
		SupportCombined
{

	@BindToBeanProperty
	private SimpleListProperty<T> items = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<T>()));

	private Model<AccordionBackingBean<T>> model = new Model<>(this, new AccordionBackingBean<T>());

	private static final Logger LOG = Logger.getLogger(ModelBindedAccordion.class);

	public ModelBindedAccordion(final TitledPaneFactory<R> renderer)
	{
		items.addListener(new ListChangeListener<T>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends T> c)
			{
				LOG.debug("change on accordion items");
				while (c.next()) {
					if (c.wasRemoved()) {
						final List<? extends T> removed = c.getRemoved();
						for (final T rem : removed) {
							final TitledPane refComp = rem.getRefComp();
							getPanes().remove(refComp);
							LOG.debug("removed item from accordion " + rem.getClass().getName());
						}
					}

					if (c.wasAdded()) {
						final List<? extends T> added = c.getAddedSubList();
						for (final T add : added) {
							getPanes().add(renderer.content(add.getTitledPaneContentModelObject()));
							LOG.debug("added item to accordion " + add.getClass().getName());
						}
					}
				}
			}
		});

		model.bind();
	}

	/**
	 * @return the testBackingList
	 */
	public List<T> getItems()
	{
		return items.get();
	}

	/**
	 * @param items
	 *            the testBackingList to set
	 */
	public void setItems(List<T> items)
	{
		this.items.addAll(items);
	}

	public SimpleListProperty<T> itemsProperty()
	{
		return items;
	}

	/**
	 * 
	 * @author Daniel
	 * 
	 */
	public static abstract class TitledPaneFactory<B>
	{
		/**
		 * 
		 * @param modelObject
		 * @return return the node that should be rendered in the titlePane.
		 */
		public abstract TitledPane content(B modelObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ludwig.jfxmodel.SupportCombined#getModel()
	 */
	@Override
	public Model<?> getModel()
	{
		return model;
	}
}
