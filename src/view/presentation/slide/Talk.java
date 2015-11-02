package view.presentation.slide;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import view.presentation.Drawer;
import static view.presentation.slide.Text.MARGIN;

/**
 *
 * @author Adrien Castex
 */
public class Talk extends Drawer<model.model.Talk>
{
    public Talk(model.model.Talk obj, JFrame frame)
    {
        super(obj, frame);
    }
    
    private Timer timer;
    
    private BufferedImage image = null;
    protected BufferedImage getImage()
    {
        if(image == null && getObject().picture != null)
        {
            try
            {
                image = getObject().picture.getImage();
            }
            catch (IOException ex)
            { }
        }
        
        return image;
    }
    
    private static final int TEXT_ROW_SPACE = 1;
    private static final int IMAGE_MARGIN = 100;
    
    private int offset;

    @Override
    public void draw(Graphics g)
    {
        if(offset <= -750)
            offset = g.getClipBounds().width / 2;
        
        // Draw background image
        g.drawImage(getBackgroundTopImage(), MARGIN, MARGIN, g.getClipBounds().width - MARGIN * 2, (int)((getBackgroundTopImage().getHeight() / (double)getBackgroundTopImage().getWidth()) * g.getClipBounds().height) * 2 - MARGIN * 2, null);
        
        // Draw title
        g.setFont(g.getFont().deriveFont(200f / 1920 * g.getClipBounds().width));
        g.setColor(new Color(255, 255, 255, 255));
        g.drawString(getObject().title, getCenter(g, getObject().title), 250);
        
        // Draw left text
        g.setFont(g.getFont().deriveFont(75f / 1920 * g.getClipBounds().width));
        g.setColor(new Color(0, 0, 50, 255));
        int y = MARGIN + 10 + 400;
        int gh = g.getFontMetrics().getHeight();
        int ho = (int)g.getClipBounds().width / 2;
        for(String s : splitToFit(getObject().text, g, MARGIN + 10, ho))
        {
            g.drawString(s, getCenter(g, s) - ho / 2 - offset, y + gh);
            y += gh + TEXT_ROW_SPACE;
        }
        
        // Draw image
        if(getImage() != null)
        {
            int hom = ho - 2 * IMAGE_MARGIN;
            int imgh = (int)((getImage().getHeight() / (double)getImage().getWidth() * hom));
            g.drawImage(getImage(), g.getClipBounds().width - MARGIN - hom, g.getClipBounds().height - MARGIN - imgh + offset, hom, imgh, null);
        }
    }
    
    @Override
    public void onEnter()
    {
        super.onEnter();
        offset = -750;
        
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if(offset <= -750)
                    return;
                
                offset -= 10;
                refresh();
                
                if(offset <= 0)
                    timer.cancel();
            }
        }, 0, 10);
    }

    @Override
    public void onLeave()
    {
        super.onLeave();
        timer.cancel();
    }
}
