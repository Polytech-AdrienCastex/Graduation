package model.model;

import model.serializable.XMLSerializable;
import model.image.LocalImage;
import model.image.Image;
import java.io.File;

/**
 *
 * @author Adrien Castex
 */
public class Student implements XMLSerializable
{
    public Student(String name, String firstName, Image picture)
    {
        this.name = name;
        this.firstName = firstName;
        this.picture = picture;
    }
    
    public String name;
    public String firstName;
    public Image picture;
    
    public static boolean isValid(File file)
    {
        String[] names = file.getName().split(".");
        return names.length >= 3
                 && names[0].matches("^[0-9]*$")
                 && names[1].matches("^[a-zA-Z\\-\\_]*$")
                 && names[2].matches("^[a-zA-Z\\-\\_]*$");
    }
    
    public static Student createFromFile(File file)
    {
        String[] names = file.getName().split(".");
        return new Student(names[1], names[2], new LocalImage(file));
    }
    public static Student createDefault()
    {
        return new Student("", "", null);
    }

    @Override
    public String toXML()
    {
        String xml = "";
        
        xml += "<student>";
        
        xml += "<name>" + name + "</name>";
        xml += "<firstName>" + firstName + "</firstName>";
        
        if(picture == null)
            xml += "<image/>";
        else
            xml += "<image>" + picture.getID() + "</image>";
        
        xml += "</student>";
        
        return xml;
    }
}
