package de.ludwig.finx.gui.wizard;

import javafx.event.Event;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

/**
 * @author Daniel
 * 
 */
public class ContentTest
{
	@Test
	public void buttonVisibilityOnePage()
	{
		final Content c = new Content(null);
		c.addPage(new TestWizardPage());
		c.start();
		Assert.assertFalse(c.getPrevious().isVisible());
		Assert.assertFalse(c.getNext().isVisible());
		Assert.assertTrue(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());
	}

	@Test
	public void buttonVisibilityTwoPages() throws Exception
	{
		final Content c = new Content(null);
		c.addPage(new TestWizardPage());
		c.addPage(new TestWizardPage());
		c.start();

		Assert.assertFalse(c.getPrevious().isVisible());
		Assert.assertTrue(c.getNext().isVisible());
		Assert.assertFalse(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());

		Whitebox.invokeMethod(c, "next", PowerMockito.mock(Event.class));

		Assert.assertTrue(c.getPrevious().isVisible());
		Assert.assertFalse(c.getNext().isVisible());
		Assert.assertTrue(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());

		Whitebox.invokeMethod(c, "previous", PowerMockito.mock(Event.class));

		Assert.assertFalse(c.getPrevious().isVisible());
		Assert.assertTrue(c.getNext().isVisible());
		Assert.assertFalse(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());
	}

	@Test
	public void buttonVisibilityThreePages() throws Exception
	{
		final Content c = new Content(null);
		c.addPage(new TestWizardPage());
		c.addPage(new TestWizardPage());
		c.addPage(new TestWizardPage());
		c.start();

		Assert.assertFalse(c.getPrevious().isVisible());
		Assert.assertTrue(c.getNext().isVisible());
		Assert.assertFalse(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());

		Whitebox.invokeMethod(c, "next", PowerMockito.mock(Event.class));

		Assert.assertTrue(c.getPrevious().isVisible());
		Assert.assertTrue(c.getNext().isVisible());
		Assert.assertFalse(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());

		Whitebox.invokeMethod(c, "next", PowerMockito.mock(Event.class));

		Assert.assertTrue(c.getPrevious().isVisible());
		Assert.assertFalse(c.getNext().isVisible());
		Assert.assertTrue(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());

		Whitebox.invokeMethod(c, "previous", PowerMockito.mock(Event.class));

		Assert.assertTrue(c.getPrevious().isVisible());
		Assert.assertTrue(c.getNext().isVisible());
		Assert.assertFalse(c.getFinish().isVisible());
		Assert.assertTrue(c.getCancel().isVisible());
	}
}

class TestWizardPage extends WizardStep
{
	private boolean next;

	private boolean previous;

	private boolean finish;

	public TestWizardPage()
	{

	}

	/**
	 * @param next
	 * @param previous
	 * @param finish
	 */
	public TestWizardPage(boolean next, boolean previous, boolean finish)
	{
		super();
		this.next = next;
		this.previous = previous;
		this.finish = finish;
	}

	@Override
	public boolean next()
	{
		return next;
	}

	@Override
	public boolean finish()
	{
		return finish;
	}

	@Override
	public boolean previous()
	{
		return previous;
	}

	@Override
	public void onNext()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrevious()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void validate() throws StepValidationException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String wizardStepDescription()
	{
		return "";
	}

}