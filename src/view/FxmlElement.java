/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Adrien
 */
public abstract class FxmlElement extends AnchorPane implements Initializable
{
    public FxmlElement(String fxmlPath)
    {
        load(fxmlPath);
    }
    public FxmlElement()
    { }
    
    protected void load(String fxmlPath)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try
        {
           fxmlLoader.load();
        }
        catch (IOException exception)
        {
           throw new RuntimeException(exception);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    { }
}