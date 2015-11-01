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
    
    private Stage primaryStage = null;
    protected Stage getStage()
    {
        return primaryStage;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFxmlPath()));
        fxmlLoader.setController(this);
        
        try
        {
           fxmlLoader.load();
        }
        catch (IOException exception)
        {
           throw new RuntimeException(exception);
        }
        
        Scene scene = new Scene(fxmlLoader.getRoot(), 600, 500);
        
        this.primaryStage = primaryStage;
        
        primaryStage.setTitle("Remise des diplomes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
