package view;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Adrien Castex
 */
public abstract class FxmlRunner extends Application
{
    protected abstract String getFxmlPath();
    
    @Override
    public void start(Stage primaryStage)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFxmlPath()));
        
        try
        {
           fxmlLoader.load();
        }
        catch (IOException exception)
        {
           throw new RuntimeException(exception);
        }
        
        Scene scene = new Scene(fxmlLoader.getRoot(), 600, 500);
        
        primaryStage.setTitle("Remise des diplomes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
