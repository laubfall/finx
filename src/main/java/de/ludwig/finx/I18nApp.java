/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx;

import de.ludwig.finx.settings.AppSettings;
import de.ludwig.finx.settings.SettingsDaoImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Daniel
 */
public class I18nApp extends Application {

    public static void main(String[] argv) {
        
        // GUI starten
        Application.launch(I18nApp.class, argv);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        HBox mainLayout = new HBox(3);
        Scene scene = new Scene(mainLayout, 300, 300);
        stage.setScene(scene);
        
        TreeItem<String> root = new TreeItem<String>("Root Node");
        root.setExpanded(true);
        root.getChildren().addAll(
                new TreeItem<String>("Item 1"),
                new TreeItem<String>("Item 2"),
                new TreeItem<String>("Item 3"));
        TreeView<String> treeView = new TreeView<String>(root);
        
        HBox titlePaneLayout = new HBox(1);
        titlePaneLayout.getChildren().addAll(new TitledPane("T1", new Button("B1")), new TitledPane("T2", new Button("B1")));
        
        mainLayout.getChildren().addAll(treeView, titlePaneLayout);
        
        stage.show();
    }
}
