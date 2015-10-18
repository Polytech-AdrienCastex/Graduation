package view.editor.talk;

import model.image.LocalImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import view.FxmlElement;

/**
 *
 * @author Adrien Castex
 */
public class Talk extends FxmlElement
{
    public Talk()
    {
        super("/view/editor/talk/Talk.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        super.initialize(url, rb);
        
        setTalk(model.model.Talk.createDefault());
    }
    
    private model.model.Talk talk;
    
    @FXML private TextField title;
    @FXML private TextField path;
    @FXML private TextArea text;
    @FXML private TitledPane bigTP;
    
    public void setTalk(model.model.Talk talk)
    {
        this.talk = talk;
        title.setText(talk.title);
        bigTP.setText(talk.title);
        text.setText(talk.text);
        path.setText(talk.picture == null ? "" : "@" + talk.picture.getID().toString());
    }
    public model.model.Talk getTalk()
    {
        return talk;
    }
    
    @FXML protected void handleBrowse(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sélectionnez une image");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.jpeg", "*.png"));
        File f = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if(f != null)
        {
            path.setText(f.getPath());
            talk.picture = new LocalImage(f);
        }
    }
    
    @FXML protected void handleTitleChanged(KeyEvent event)
    {
        talk.title = title.getText();
        bigTP.setText(talk.title);
    }
    
    @FXML protected void handleTextChanged(KeyEvent event)
    {
        talk.text = text.getText();
    }
}
