package model.model;

import java.util.Calendar;
import model.image.Image;
import model.serializable.XMLSerializable;

/**
 *
 * @author Adrien Castex
 */
public class Details implements XMLSerializable
{
    public static final String YEAR_SYMBOL = "%Y";
    
    public Details(
            int year,
            String presentationText,
            String sectionIntroText,
            String congratulationText,
            Image polytechImage)
    {
        this.year = year;
        this.presentationText = presentationText;
        this.sectionIntroText = sectionIntroText;
        this.congratulationText = congratulationText;
        this.polytechImage = polytechImage;
    }
    
    public int year;
    public String presentationText;
    public String sectionIntroText;
    public String congratulationText;
    public Image polytechImage;
    
    public String computedPresentationText()
    {
        return presentationText.replace(YEAR_SYMBOL, Integer.toString(year));
    }
    public String computedSectionIntroText()
    {
        return sectionIntroText.replace(YEAR_SYMBOL, Integer.toString(year));
    }
    public String computedCongratulationText()
    {
        return congratulationText.replace(YEAR_SYMBOL, Integer.toString(year));
    }
    
    public static Details createDefault(int year)
    {
        return new Details(year, "Bienvenue à la Remise des Diplomes de " + YEAR_SYMBOL, "Début de la Remise des Diplômes de " + YEAR_SYMBOL, "BRAVO", null);
    }
    public static Details createDefault()
    {
        return createDefault(Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public String toXML()
    {
        String xml = "";
        
        xml += "<details>";
        
        xml += "<year>" + year + "</year>";
        xml += "<presentationText>" + presentationText + "</presentationText>";
        xml += "<sectionIntroText>" + sectionIntroText + "</sectionIntroText>";
        xml += "<congratulationText>" + congratulationText + "</congratulationText>";
        
        if(polytechImage == null)
            xml += "<polytechImage/>";
        else
            xml += "<polytechImage>" + polytechImage.getID() + "</polytechImage>";
        
        xml += "</details>";
        
        return xml;
    }
}
