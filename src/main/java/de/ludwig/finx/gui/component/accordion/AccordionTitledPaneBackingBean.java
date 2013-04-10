package de.ludwig.finx.gui.component.accordion;

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
	private TitledPane refComp;

	private TITLEDPANEOBJ titledPaneContentModelObject;

	public AccordionTitledPaneBackingBean(final TITLEDPANEOBJ titledPaneContentModelObject)
	{
		this.titledPaneContentModelObject = titledPaneContentModelObject;
	}

	/**
	 * @return the refComp
	 */
	TitledPane getRefComp()
	{
		return refComp;
	}

	/**
	 * @param refComp
	 *            the refComp to set
	 */
	void setRefComp(TitledPane refComp)
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
