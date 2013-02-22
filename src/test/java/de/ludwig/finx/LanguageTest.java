package de.ludwig.finx;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Daniel
 * 
 */
public class LanguageTest
{
	@Test
	public void isSame()
	{
		Language l1 = new Language("DE");
		Language l2 = new Language("DE");
		Assert.assertTrue(l1.equals(l2));

		l2 = new Language("de");
		Assert.assertTrue(l1.equals(l2));

		l2 = new Language(Locale.GERMAN);
		Assert.assertTrue(l1.equals(l2));
	}
}
