package controller;

import java.awt.GraphicsEnvironment;
import view.editor.window.EditorWindow;

/**
 *
 * @author Adrien Castex
 */
public class Graduation
{
    /**
     * Entry Point
     * 
     * @param args Program arguments
     */
    public static void main(String[] args)
    {
        EditorWindow.run();
    }
    
    protected static void listAllFonts()
    {
        for(String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
            System.out.println(s);
    }
}
