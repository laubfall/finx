package de.ludwig.finx.jfx.converter;

import java.io.File;

import javafx.util.StringConverter;

public class FileStringConverter extends StringConverter<File> {

	@Override
	public File fromString(String string) {
		return new File(string);
	}

	@Override
	public String toString(File object) {
		return object.getAbsolutePath();
	}



}
