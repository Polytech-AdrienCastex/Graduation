package view.presentation.slide;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import view.presentation.Drawer;

/**
 *
 * @author Adrien Castex
 */
public class Text extends Drawer<String>
{
    public Text(String obj, JFrame frame)
    {
        super(obj, frame);
    }
    
    protected int opacity;
    private Timer timer;

    protected static final int MARGIN = 50;
    protected static final int TEXT_ROW_SPACE = 50;
    @Override
    public void draw(Graphics g)
    {
        // Draw background image
        g.drawImage(getBackgroundTopImage(), MARGIN, MARGIN, g.getClipBounds().width - MARGIN * 2, (int)((getBackgroundTopImage().getHeight() / (double)getBackgroundTopImage().getWidth()) * g.getClipBounds().height) * 2 - MARGIN * 2, null);
        
        // Draw text
        g.setColor(new Color(0, 0, 0, opacity));
        g.setFont(g.getFont().deriveFont(200f));
        
        int y = MARGIN + 10;
        int gh = g.getFontMetrics().getHeight();
        for(String s : splitToFit(getObject(), g, MARGIN + 10))
        {
            g.drawString(s, getCenter(g, s), y + gh);
            y += gh + TEXT_ROW_SPACE;
        }
    }

    @Override
    public void onEnter()
    {
        super.onEnter();
        opacity = 0;
        
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                opacity += 5;
                refresh();
                
                if(opacity >= 255)
                    timer.cancel();
            }
        }, 0, 50);
    }

    @Override
    public void onLeave()
    {
        super.onLeave();
        timer.cancel();
    }
}
