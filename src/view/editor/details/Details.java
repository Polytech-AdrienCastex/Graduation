package view.editor.details;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.image.LocalImage;
import view.FxmlElement;

/**
 *
 * @author Adrien Castex
 */
public class Details extends FxmlElement
{
    public Details()
    {
        load("/view/editor/details/Details.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        super.initialize(url, rb);
        
        setDetails(model.model.Details.createDefault());
        
        explanation.getChildren()
                .stream()
                .filter(Label.class::isInstance)
                .map(n -> (Label)n)
                .forEach(l -> l.setText(l.getText().replace("%YEAR_SYMBOL", model.model.Details.YEAR_SYMBOL)));
    }
    
    private model.model.Details details;
    
    @FXML private VBox explanation;
    
    @FXML private TextField year;
    @FXML private TextField sectionIntroText;
    @FXML private TextField presentationText;
    @FXML private TextField polytechImage;
    @FXML private TextField congratulationText;
    
    public model.model.Details getDetails()
    {
        return details;
    }
    public void setDetails(model.model.Details details)
    {
        this.details = details;
        
        year.setText(Integer.toString(details.year));
        sectionIntroText.setText(details.sectionIntroText);
        presentationText.setText(details.presentationText);
        congratulationText.setText(details.congratulationText);
        polytechImage.setText(details.polytechImage == null ? "" : "Image présente en mémoire");
    }
    
    @FXML protected void handleBrowse(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sélectionnez une image");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.jpeg", "*.png"));
        File f = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if(f != null)
        {
            polytechImage.setText(f.getPath());
            details.polytechImage = new LocalImage(f);
        }
    }
    
    @FXML protected void handleYearChanged(KeyEvent event)
    {
        String value = year.getText();
        if(value.matches("^[0-9]*$"))
            details.year = Integer.parseInt(value);
        else
        {
            int selectionStart = year.getSelection().getStart();
            year.setText(Integer.toString(details.year));
            year.positionCaret(selectionStart);
        }
    }
    
    @FXML protected void handleSectionIntroTextChanged(KeyEvent event)
    {
        details.sectionIntroText = sectionIntroText.getText();
    }
    @FXML protected void handlePresentationTextChanged(KeyEvent event)
    {
        details.presentationText = presentationText.getText();
    }
    @FXML protected void handleCongratulationTextChanged(KeyEvent event)
    {
        details.congratulationText = congratulationText.getText();
    }
}
