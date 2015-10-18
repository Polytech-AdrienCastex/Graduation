/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.model;

import model.serializable.XMLSerializable;
import model.image.Image;

/**
 *
 * @author Adrien
 */
public class Talk implements XMLSerializable
{
    public Talk(String title, String text, Image picture)
    {
        this.title = title;
        this.text = text;
        this.picture = picture;
    }
    
    public String title;
    public String text;
    public Image picture;
    
    public static Talk createDefault()
    {
        return new Talk("Discours", "", null);
    }

    @Override
    public String toXML()
    {
        String xml = "";
        
        xml += "<talk>";
        
        xml += "<title>" + title + "</title>";
        xml += "<text>" + text + "</text>";
        
        if(picture == null)
            xml += "<image/>";
        else
            xml += "<image>" + picture.getID() + "</image>";
        
        xml += "</talk>";
        
        return xml;
    }
}
