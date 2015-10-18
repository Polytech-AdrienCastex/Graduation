/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.editor.section;

import model.model.Side;
import model.model.Student;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import view.FxmlElement;

/**
 *
 * @author Adrien
 */
public class Section extends FxmlElement
{
    public Section()
    {
        load("/view/editor/section/Section.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        super.initialize(url, rb);
    }
    
    private model.model.Section section;
    @FXML private Side side;
    
    @FXML private TextField title;
    @FXML private TextField path;
    @FXML private Label nbStudents;
    
    public model.model.Section getSection()
    {
        return section;
    }
    public void setSection(model.model.Section section)
    {
        this.section = section;
        
        setStudents(section.students);
        title.setText(section.name);
    }
    
    public void setStudents(Collection<Student> students)
    {
        String plur = students.size() > 1 ? "s" : "";
        
        section.setStudents(students);
        nbStudents.setText(students.size() + " étudiant" + plur + " trouvé" + plur + "!");

        if(students.isEmpty())
            nbStudents.setTextFill(Paint.valueOf("red"));
        else
            nbStudents.setTextFill(Paint.valueOf("green"));
    }
    
    public void setSide(Side side)
    {
        this.side = side;
        
        section = model.model.Section.createDefault(side);
        title.setText(section.name);
    }
    public Side getSide()
    {
        return side;
    }
    
    private Collection<Student> extractFolder(File dir)
    {
        return Stream.of(dir.listFiles())
                .filter(File::exists)
                .filter(File::isFile)
                .filter(Student::isValid)
                .map(Student::createFromFile)
                .collect(Collectors.toList());
    }
    
    @FXML protected void handleBrowse(ActionEvent event)
    {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choisissez un dossier contenant les images.");
        
        File dir = dc.showDialog(((Node)event.getSource()).getScene().getWindow());
        if(dir != null && dir.exists())
        {
            path.setText(dir.getPath());
            setStudents(extractFolder(dir));
        }
    }
    
    @FXML protected void handleTitleChanged(KeyEvent event)
    {
        section.name = title.getText();
    }
}
