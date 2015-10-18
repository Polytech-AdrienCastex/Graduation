/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.model;

import model.serializable.XMLSerializable;
import java.util.Collection;
import java.util.LinkedList;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Adrien
 */
public class Section implements XMLSerializable
{
    public Section(String name, Side side)
    {
        this.name = name;
        this.side = side;
        this.students = new LinkedList<>();
    }
    
    public Collection<Student> students;
    public String name;
    public Side side;
    
    public static Section createDefault(Side side)
    {
        return new Section("Informatique", side);
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
        xml += "<side>" + side.getValue() + "</side>";
        
        xml += "</section>";
        
        return xml;
    }
}
