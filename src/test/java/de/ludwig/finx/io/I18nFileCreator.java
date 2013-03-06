package de.ludwig.finx.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author Daniel
 * 
 */
public class I18nFileCreator
{
	private static I18nFileCreator creator;

	private List<String> lines = new ArrayList<String>();

	private I18nFileCreator()
	{

	}

	public static I18nFileCreator start()
	{
		creator = new I18nFileCreator();
		return creator;
	}

	public I18nFileCreator addSimpleComment(String commentText)
	{
		addMultilineComment(commentText);
		return creator;
	}

	public I18nFileCreator addMultilineComment(String... commentLines)
	{
		for (String comment : commentLines) {
			lines.add("# " + comment);
		}

		return creator;
	}

	public I18nFileCreator addKeyValue(String key, String value)
	{
		lines.add(key + "=" + value);
		return creator;
	}

	public I18nFileCreator addEmptyLine(int lines, int blanks)
	{
		String str = "";
		for (int i = 0; i < blanks; i++) {
			str += " ";
		}

		for (int i = 0; i < lines; i++) {
			this.lines.add(str);
		}
		return creator;
	}

	public I18nFileCreator addEmptyLine(int lines)
	{
		addEmptyLine(lines, 0);
		return creator;
	}

	public File end()
	{
		File result = null;
		try {
			result = File.createTempFile("i18nFileCreator", "properties");
			FileUtils.writeLines(result, lines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		lines.clear();
		return result;
	}
}
