package de.ludwig.finx.gui.component.accordion;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TitledPane;

/**
 * A base backing-Bean for the TitledPanes of the Accordion
 * 
 * @author Daniel
 * 
 * @param <TITLEDPANEOBJ>
 *            the type of the backing-bean that is used for the model of the titlePanes Content area
 *            Component.
 */
public class AccordionTitledPaneBackingBean<TITLEDPANEOBJ>
{
	private StringProperty titledPaneTitle = new SimpleStringProperty();

	private TitledPane refComp;

	private TITLEDPANEOBJ titledPaneContentModelObject;

	public AccordionTitledPaneBackingBean(final String panelTitle, final TITLEDPANEOBJ titledPaneContentModelObject)
	{
		titledPaneTitle.set(panelTitle);
		this.titledPaneContentModelObject = titledPaneContentModelObject;
	}

	/**
	 * @return the titledPaneTitle
	 */
	public String getTitledPaneTitle()
	{
		return titledPaneTitle.get();
	}

	/**
	 * @param titledPaneTitle
	 *            the titledPaneTitle to set
	 */
	public void setTitledPaneTitle(String titledPaneTitle)
	{
		this.titledPaneTitle.set(titledPaneTitle);
	}

	public StringProperty titledPaneTitleProperty()
	{
		return titledPaneTitle;
	}

	/**
	 * @return the refComp
	 */
	public TitledPane getRefComp()
	{
		return refComp;
	}

	/**
	 * @param refComp
	 *            the refComp to set
	 */
	public void setRefComp(TitledPane refComp)
	{
		this.refComp = refComp;
	}

	/**
	 * @return the titledPaneContentModelObject
	 */
	public TITLEDPANEOBJ getTitledPaneContentModelObject()
	{
		return titledPaneContentModelObject;
	}
}
