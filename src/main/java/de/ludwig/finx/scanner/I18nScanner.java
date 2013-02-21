/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.scanner;

import java.io.File;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public interface I18nScanner {
    /**
     * 
     * @param file Nicht optional. Die Datei die nach Änderungen bzgl. I18n-Keys durchsucht werden soll
     * @return Alle gefundenen I18n-Keys
     * @throws wie auch immer gearteter Fehler beim Scannen der Datei
     */
    public Set<I18nKey> keys(final File file) throws ScannerException;
}
