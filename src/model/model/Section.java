package model.model;

import model.serializable.XMLSerializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Adrien Castex
 */
public class Section implements XMLSerializable
{
    public Section(String name)
    {
        this.name = name;
        this.students = new LinkedList<>();
    }
    
    public Collection<Student> students;
    public String name;
    
    public static Section createDefault()
    {
        return new Section("Informatique");
    }
    
    public void addStudent(Student student)
    {
        students.add(student);
    }
    public void addStudents(Collection<Student> students)
    {
        this.students.addAll(students);
    }
    public void setStudents(Collection<Student> students)
    {
        this.students.clear();
        this.students.addAll(students);
    }

    @Override
    public String toXML()
    {
        String xml = "";
        
        xml += "<section>";
        
        xml += students.stream()
                .map(XMLSerializable::toXML)
                .reduce("", (s1,s2) -> s1 + s2);
        
        xml += "<name>" + name + "</name>";
        
        xml += "</section>";
        
        return xml;
    }
}
