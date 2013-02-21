/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author Daniel
 */
public class SimpleTagScanner implements I18nScanner {

    private String startTag;
    
    private String endTag;

    public SimpleTagScanner(String startTag, String endTag) {
        this.startTag = startTag;
        this.endTag = endTag;
    }
    
    public Set<I18nKey> keys(File file) throws ScannerException {
          Validate.notNull(file);

        if (file.exists() == false) {
            throw new ScannerException("File to scan does not exist " + file.getAbsolutePath());
        }

        if (file.isDirectory()) {
            throw new ScannerException("File to scan is a directory " + file.getAbsolutePath());
        }
        
        final Set<I18nKey> keys = new HashSet<I18nKey>();
        LineNumberReader lnr = null;
        try {
            FileReader fr = new FileReader(file);
            lnr = new LineNumberReader(fr);
            String line = null;
            while((line = lnr.readLine()) != null){
                final String[] substringsBetween = StringUtils.substringsBetween(line, startTag, endTag);
                for(String key : substringsBetween){
                    I18nKey iKey = new I18nKey(key, file);
                    keys.add(iKey);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleTagScanner.class.getName()).log(Level.SEVERE, null, ex);
            throw new ScannerException("Source file does not exist", ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleTagScanner.class.getName()).log(Level.SEVERE, null, ex);
            throw new ScannerException("Unable to read Source file", ex);
        } finally {
        	IOUtils.closeQuietly(lnr);
        }
        
        return keys;
    }
    
}
