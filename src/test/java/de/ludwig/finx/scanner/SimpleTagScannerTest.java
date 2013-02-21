/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.scanner;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.ludwig.finx.scanner.I18nKey;
import de.ludwig.finx.scanner.SimpleTagScanner;

/**
 *
 * @author Daniel
 */
public class SimpleTagScannerTest {
    
    final String startTag = "I18nContext.getMessage(\"";
    
    final String endTag = "\");";
    @Test
    public void testSimple() throws IOException{
        SimpleTagScanner sts = new SimpleTagScanner(startTag, endTag);
        File source = File.createTempFile("simpleTagScannerTestFile", null);
        FileUtils.writeStringToFile(source, "import blah;"
                + "I18nContext.getMessage(\"de.ludwig\");");
        
        Set<I18nKey> result = sts.keys(source);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());
    }
}
