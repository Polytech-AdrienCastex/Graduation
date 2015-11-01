package view.presentation.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.ModelCollection;
import view.presentation.Drawer;
import view.presentation.slide.SectionManager;
import view.presentation.slide.Talk;
import view.presentation.slide.Text;

/**
 *
 * @author Adrien Castex
 */
public class PresentationWindow extends JFrame implements KeyListener
{
    public PresentationWindow(ModelCollection mc)
    {
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        
        pane = new JPanel()
        {
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                
                g.setFont(new Font("Calibri", Font.BOLD, 200));
                g.setColor(Color.BLACK);
                if(currentDrawer != null)
                    currentDrawer.draw(g);
            }
        };
        pane.setBackground(Color.WHITE);
        this.setContentPane(pane);
        this.addKeyListener(this);
        
        this.nodes = new ArrayList<>();
        init(mc, this.nodes);
        
        this.nodeIndex = -1;
        nextPage();
    }
    
    private final JPanel pane;
    
    // 1ere diapo
    // Discours
    // Diapo desc : Disc -> Sections
    // Sections
    // Bravo
    // Personnel administratif
    private void init(ModelCollection mc, ArrayList<Drawer> nodes)
    {
        nodes.add(new Text(mc.getDetails().computedPresentationText(), this));
        
        mc.getTalks()
                .stream()
                .map(t -> new Talk(t, this))
                .forEach(nodes::add);
        
        nodes.add(new Text(mc.getDetails().computedSectionIntroText(), this));
        
        SectionManager sm = new SectionManager(mc.getSections(), this, 2);
        nodes.add(sm);
        
        nodes.add(new Text(mc.getDetails().computedCongratulationText(), this));
    }
    
    private final ArrayList<Drawer> nodes;
    private int nodeIndex;
    
    private Drawer currentDrawer = null;
    private void setCurrentDrawer(Drawer d)
    {
        if(currentDrawer == d)
            return;
        
        if(currentDrawer != null)
            currentDrawer.onLeave();
        currentDrawer = d;
        if(d != null)
            d.onEnter();
    }
    
    public void refresh()
    {
        pane.repaint();
    }
    
    protected void nextPage()
    {
        if(nodeIndex + 1 >= nodes.size())
            return;
        
        setCurrentDrawer(this.nodes.get(++nodeIndex));
        this.refresh();
    }
    protected void previousPage()
    {
        if(nodeIndex - 1 < 0)
            return;
        
        setCurrentDrawer(this.nodes.get(--nodeIndex));
        this.refresh();
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        /*
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);*/
    }

    @Override
    public void keyTyped(KeyEvent ke)
    { }

    @Override
    public void keyPressed(KeyEvent ke)
    { }

    protected void subSectionMove(int index)
    {
        if(currentDrawer != null)
        {
            currentDrawer.onNext(index);
            if(currentDrawer.isEnded())
                nextPage();
            else
                this.refresh();
        }
    }
    @Override
    public void keyReleased(KeyEvent ke)
    {
        switch(ke.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE:
                this.dispose();
                break;
                
            case KeyEvent.VK_A: // Left section
                subSectionMove(0);
                break;
                
            case KeyEvent.VK_Z: // Right section
                subSectionMove(1);
                break;
                
            case KeyEvent.VK_E:
                subSectionMove(2);
                break;
                
            case KeyEvent.VK_R:
                subSectionMove(3);
                break;
                
            case KeyEvent.VK_T:
                subSectionMove(4);
                break;
                
            case KeyEvent.VK_Y:
                subSectionMove(5);
                break;
                
            case KeyEvent.VK_UP: // Go backward
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_BACK_SPACE:
                previousPage();
                break;
                
            case KeyEvent.VK_DOWN: // Go foreward
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_TAB:
                nextPage();
                break;
        }
    }
}
