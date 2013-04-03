package de.ludwig.finx.jfx.converter;

import java.io.File;

import javafx.util.StringConverter;

/**
 * Binding Converter for Bidirectional-Bindings.
 * 
 * @author Daniel
 * 
 */
public class FileStringConverter extends StringConverter<File>
{

	@Override
	public File fromString(String string)
	{
		return new File(string);
	}

	@Override
	public String toString(File object)
	{
		if (object == null) {
			return "";
		}
		return object.getAbsolutePath();
	}

}
