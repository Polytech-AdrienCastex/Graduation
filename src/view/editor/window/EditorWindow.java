package view.editor.window;

import model.ModelCollection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
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
import view.presentation.window.PresentationWindow;

/**
 * FXML Controller class
 *
 * @author Adrien Castex
 */
public class EditorWindow extends FxmlRunner implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle rb)
    { }
    
    
    
    @Override
    protected String getFxmlPath()
    {
        return "/view/editor/window/EditorWindow.fxml";
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
    
    protected ModelCollection getCurrentModelCollection()
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

        return builder.build();
    }
    
    @FXML protected void handleSave(ActionEvent event)
    {
        File f = createFileChooser("Sélectionnez où sauvegarder")
                .showSaveDialog(getScene(event).getWindow());
        if(f != null)
        {
            try
            {
                getCurrentModelCollection().save(f);
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
                        .map(t -> { Talk tt = new Talk(this::handleTalkUp, this::handleTalkDown); tt.setTalk(t); return tt; })
                        .forEach(talks.getChildren()::add);
                
                
                mc.getSections()
                        .stream()
                        .map(s -> { Section ss = new Section(this::handleSecionUp, this::handleSecionDown); ss.setSection(s); return ss; })
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
        new PresentationWindow(getCurrentModelCollection()).setVisible(true);
    }
    
    @FXML protected void handleAddTalk(ActionEvent event)
    {
        Talk t = new Talk(this::handleTalkUp, this::handleTalkDown);
        talks.getChildren().add(t);
    }
    
    @FXML protected void handleClearTalks(ActionEvent event)
    {
        talks.getChildren().clear();
    }
    
    @FXML protected void handleAddSection(ActionEvent event)
    {
        Section s = new Section(this::handleSecionUp, this::handleSecionDown);
        sections.getChildren().add(s);
    }
    
    @FXML protected void handleClearSections(ActionEvent event)
    {
        sections.getChildren().clear();
    }
    
    protected void up(Object o, ObservableList children)
    {
        int index = children.indexOf(o);
        
        if(index == 0)
            return;
        
        children.remove(o);
        children.add(index - 1, o);
    }
    protected void down(Object o, ObservableList children)
    {
        int index = children.indexOf(o);
        
        if(index == children.size() - 1)
            return;
        
        children.remove(o);
        children.add(index + 1, o);
    }
    
    @FXML protected void handleTalkUp(ActionEvent event)
    {
        up(event.getSource(), talks.getChildren());
    }
    @FXML protected void handleTalkDown(ActionEvent event)
    {
        down(event.getSource(), talks.getChildren());
    }
    
    @FXML protected void handleSecionUp(ActionEvent event)
    {
        up(event.getSource(), sections.getChildren());
    }
    @FXML protected void handleSecionDown(ActionEvent event)
    {
        down(event.getSource(), sections.getChildren());
    }
}
