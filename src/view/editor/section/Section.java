package view.editor.section;

import model.model.Student;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import view.FxmlElement;

/**
 *
 * @author Adrien Castex
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
        
        setSection(model.model.Section.createDefault());
    }
    
    private model.model.Section section;
    
    @FXML private TextField title;
    @FXML private TextField path;
    @FXML private Label nbStudents;
    @FXML private TitledPane bigTP;
    
    public model.model.Section getSection()
    {
        return section;
    }
    public void setSection(model.model.Section section)
    {
        this.section = section;
        
        setStudents(section.students);
        title.setText(section.name);
        bigTP.setText(section.name);
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
        bigTP.setText(section.name);
    }
}
