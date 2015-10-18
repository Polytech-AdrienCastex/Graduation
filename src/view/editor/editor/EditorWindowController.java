package view.editor.editor;

import model.ModelCollection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import view.FxmlRunner;
import view.editor.details.Details;
import view.editor.section.Section;
import view.editor.talk.Talk;

/**
 * FXML Controller class
 *
 * @author Adrien Castex
 */
public class EditorWindowController extends FxmlRunner implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle rb)
    { }
    
    
    
    @Override
    protected String getFxmlPath()
    {
        return "/view/editor/editor/EditorWindow.fxml";
    }
    public static void run()
    {
        launch();
    }
    
    @FXML private VBox sections;
    @FXML private VBox talks;
    @FXML private Details details;
    
    private FileChooser createFileChooser(String title)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Projet", "*.prj"));
        return fc;
    }
    
    private Scene getScene(ActionEvent event)
    {
        return ((Node)event.getSource()).getScene();
    }
    
    @FXML protected void handleSave(ActionEvent event)
    {
        File f = createFileChooser("Sélectionnez où sauvegarder")
                .showSaveDialog(getScene(event).getWindow());
        if(f != null)
        {
            ModelCollection.Builder builder = ModelCollection.create()
                    .setDetails(details.getDetails());
            
            sections.getChildren()
                    .stream()
                    .filter(Section.class::isInstance)
                    .map(n -> (Section)n)
                    .map(Section::getSection)
                    .forEach(builder::addSection);
            
            talks.getChildren()
                    .stream()
                    .filter(Talk.class::isInstance)
                    .map(n -> (Talk)n)
                    .map(Talk::getTalk)
                    .forEach(builder::addTalk);
            
            try
            {
                builder.build()
                        .save(f);
            }
            catch (IOException ex)
            {
                // ERROR
            }
        }
    }
    
    @FXML protected void handleLoad(ActionEvent event)
    {
        File f = createFileChooser("Sélectionnez le projet à charger")
                .showOpenDialog(getScene(event).getWindow());
        if(f != null)
        {
            try
            {
                ModelCollection mc = ModelCollection.load(f);
                
                talks.getChildren().clear();
                sections.getChildren().clear();
            
                mc.getTalks()
                        .stream()
                        .map(t -> { Talk tt = new Talk(); tt.setTalk(t); return tt; })
                        .forEach(talks.getChildren()::add);
                
                
                mc.getSections()
                        .stream()
                        .map(s -> { Section ss = new Section(); ss.setSection(s); return ss; })
                        .forEach(sections.getChildren()::add);
                
                details.setDetails(mc.getDetails());
            }
            catch (ParserConfigurationException | SAXException | IOException ex)
            {
                // ERROR
            }
        }
    }
    
    @FXML protected void handleStart(ActionEvent event)
    {
    }
    
    @FXML protected void handleAddTalk(ActionEvent event)
    {
        Talk t = new Talk();
        talks.getChildren().add(t);
    }
    
    @FXML protected void handleClearTalks(ActionEvent event)
    {
        talks.getChildren().clear();
    }
    
    @FXML protected void handleAddSection(ActionEvent event)
    {
        Section s = new Section();
        sections.getChildren().add(s);
    }
    
    @FXML protected void handleClearSections(ActionEvent event)
    {
        sections.getChildren().clear();
    }
}
