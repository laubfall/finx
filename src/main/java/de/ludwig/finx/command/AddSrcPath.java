/**
 * 
 */
package de.ludwig.finx.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.StrTokenizer;

import de.ludwig.finx.monitor.Monitoring;

/**
 * 
 * 
 * @author Daniel
 *
 */
public class AddSrcPath implements Command {
	public static final String OPT_SRCPATH = "srcPaths";

	/**
	 * 
	 * @param payload List off absolute paths of directories (see {@link #help()}) seperated by a semicolon
	 */
	public Object execute(String payload) {
		final List<File> srcDirs = srcDirs(payload);
		Monitoring.instance().addSrcDirectoriesToMonitor(srcDirs);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#name()
	 */
	public String name() {
		return "addsourcepath";
	}

	/* (non-Javadoc)
	 * @see de.ludwig.i18n.commandline.Command#help()
	 */
	public String help() {
		return "use this command to add paths of a directory to the list of directories which contains text-files shall be monitored by FINX." +
				"If you want to add more then one path sepereate eacht path by a semicolon";
	}

    private final List<File> srcDirs(final String srcPaths){
    	@SuppressWarnings("unchecked")
		final List<String> tokenList = StrTokenizer.getCSVInstance(srcPaths).setDelimiterChar(';').getTokenList();
    	final List<File> result = new ArrayList<File>();
    	for(String path : tokenList) {
    		result.add(new File(path));
    	}
        return result;
    }
}
