package view.presentation;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import view.presentation.window.PresentationWindow;

/**
 *
 * @author Adrien Castex
 */
public abstract class Drawer<T>
{
    public Drawer(T obj, JFrame frame)
    {
        this.obj = obj;
        this.frame = frame;
    }
    
    private final JFrame frame;
    
    private final T obj;
    protected T getObject()
    {
        return obj;
    }
    
    public abstract void draw(Graphics g);
    
    public void onNext(int listIndex)
    { }
    
    public boolean isEnded()
    {
        return true;
    }
    
    public void onEnter()
    { }
    public void onLeave()
    { }
    
    protected static Collection<String> splitToFit(String text, Graphics g, int margin)
    {
        return splitToFit(text, g, margin, g.getClipBounds().width);
    }
    protected static Collection<String> splitToFit(String text, Graphics g, int margin, int w)
    {
        text = text.trim();
        if(text.contains("\n"))
        {
            return Stream.of(text.split("\n"))
                    .map(s -> splitToFit(s, g, margin, w))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        
        FontMetrics fm = g.getFontMetrics();
        Collection<String> strs = new LinkedList<>();
        
        do
        {
            String currentText = text;
            while(fm.getStringBounds(currentText, g).getWidth() >= w - margin * 2)
            {
                currentText = currentText.substring(0, currentText.lastIndexOf(" "));
            }
            if(currentText.length() == 0)
            {
                int index = text.indexOf(" ");
                if(index == -1)
                {
                    strs.add(text);
                    return strs;
                }
                else
                {
                    currentText = text.substring(index);
                }
            }
            
            strs.add(currentText);
            text = text.substring(currentText.length());
        } while(!text.isEmpty());
        
        return strs;
    }
    
    protected void refresh()
    {
        ((PresentationWindow)frame).refresh();
    }
    
    protected int getCenter(Graphics g, String text)
    {
        return (int)(g.getClipBounds().width - g.getFontMetrics().getStringBounds(text, g).getWidth())/2;
    }
    protected int getCenter(Graphics g)
    {
        return (int)g.getClipBounds().width/2;
    }
    
    
    private static BufferedImage backgroundTopImage = null;
    protected static BufferedImage getBackgroundTopImage()
    {
        if(backgroundTopImage == null)
            try
            {
                backgroundTopImage = ImageIO.read(new File("E:\\other\\Graduation\\bg_big.jpg"));
            }
            catch (IOException ex)
            { }
        
        return backgroundTopImage;
    }
}
