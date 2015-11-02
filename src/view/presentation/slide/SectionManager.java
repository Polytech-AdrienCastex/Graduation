package view.presentation.slide;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;
import javax.swing.JFrame;
import model.model.Section;
import model.model.Student;
import view.presentation.Drawer;

/**
 *
 * @author Adrien Castex
 */
public class SectionManager extends Drawer<Collection<model.model.Section>>
{
    public SectionManager(Collection<Section> obj, JFrame frame, int nb)
    {
        super(obj, frame);
        
        sections = new ArrayList<>(obj.stream().map(InnerSection::new).collect(Collectors.toList()));
        indexes = new int[nb];
        
        for(currentIndex = 0; currentIndex < nb; currentIndex++)
            indexes[currentIndex] = currentIndex;
    }
    
    protected static class InnerSection
    {
        public InnerSection(Section section)
        {
            this.section = section;
            this.students = section.students.iterator();
            this.currentStudent = null;
        }
        
        private Section section;
        private Iterator<Student> students;
        private Student currentStudent;
        
        public Section getSection()
        {
            return section;
        }
        public void nextStudent()
        {
            currentStudent = students.next();
        }
        public Student getStudent()
        {
            if(currentStudent == null && hasMoreStudent())
                nextStudent();
            return currentStudent;
        }
        public boolean hasMoreStudent()
        {
            return students.hasNext();
        }
    }
    
    private final ArrayList<InnerSection> sections;
    private final int[] indexes;
    private int currentIndex;
    
    protected InnerSection getInnerSection(int listIndex)
    {
        if(listIndex < 0 || listIndex >= indexes.length || indexes[listIndex] >= sections.size())
            return null;
        
        InnerSection is = sections.get(indexes[listIndex]);
        
        return is;
    }
    
    /**************************************************************************
     * DRAWING
     **************************************************************************/
    private static final int MARGIN = 20;
    private static final double IMAGE_RATIO = 242f / 302f;
    private static final int TEXT_ROW_SPACE = 5;
    
    protected void globalDrawBefore(Graphics g)
    {
        // Draw background image
        g.drawImage(getBackgroundTopImage(), MARGIN, MARGIN, g.getClipBounds().width - MARGIN * 2, (int)((((getBackgroundTopImage().getHeight() / (double)getBackgroundTopImage().getWidth()) * g.getClipBounds().height) * 2 - MARGIN * 2) / 2), null);
    }
    
    protected void draw(Graphics g, int index, InnerSection is)
    {
        int locationX = (g.getClipBounds().width - MARGIN * 2) / indexes.length * index + MARGIN;
        int locationXLimit = (g.getClipBounds().width - MARGIN * 2) / indexes.length * (index + 1) + MARGIN;
        int centerX = (locationXLimit + locationX) / 2;
        
        int IMAGE_H = (int)(500 / 1080f * g.getClipBounds().height);
        int GAP_IMAGE_TEXT = (int)(20 / 1080f * g.getClipBounds().height);
        int GAP_TITLE_TOP = (int)(20 / 1080f * g.getClipBounds().height);
        
        int y = MARGIN + 10 + (int)(300 / 1080f * g.getClipBounds().height);
        
        g.setFont(g.getFont().deriveFont(75f / 1920 * g.getClipBounds().width));
        g.setColor(new Color(255, 255, 255));
        
        int yi = MARGIN + GAP_TITLE_TOP;
        int gh = g.getFontMetrics().getHeight();
        for(String s : splitToFit(is.getSection().name, g, 0, locationXLimit - locationX))
        {
            g.drawString(s, (int)(centerX - g.getFontMetrics().getStringBounds(s, g).getWidth() / 2), yi + gh);
            yi += gh + TEXT_ROW_SPACE;
        }
        
        
        try
        {
            g.setFont(g.getFont().deriveFont(75f / 1920 * g.getClipBounds().width));
            g.setColor(new Color(50, 50, 200));

            int w = (int)(IMAGE_RATIO * IMAGE_H);
            g.drawImage(is.getStudent().picture.getImage(), centerX - w / 2, y, w, IMAGE_H, null);
            
            String fullName = is.getStudent().firstName + " " + is.getStudent().name.toUpperCase();
            g.drawString(fullName, (int)(centerX - g.getFontMetrics().getStringBounds(fullName, g).getWidth() / 2), y + IMAGE_H + GAP_IMAGE_TEXT + g.getFontMetrics().getHeight());
        }
        catch (IOException ex)
        { }
    }
    
    protected void globalDrawAfter(Graphics g)
    {
        
    }
    /**************************************************************************
     * END OF DRAWING
     **************************************************************************/
    
    @Override
    public void draw(Graphics g)
    {
        globalDrawBefore(g);
        
        IntStream.range(0, indexes.length)
                .mapToObj(i -> new Pair<Integer, InnerSection>(i, getInnerSection(i)))
                .filter(p -> p.getValue() != null)
                .forEach(p -> draw(g, p.getKey(), p.getValue()));
        
        globalDrawAfter(g);
    }

    @Override
    public boolean isEnded()
    {
        return IntStream.range(0, indexes.length)
                .mapToObj(this::getInnerSection)
                .allMatch(s -> s == null);
    }
    
    @Override
    public void onNext(int listIndex)
    {
        InnerSection is = getInnerSection(listIndex);
        if(is != null)
        {
            while(!is.hasMoreStudent())
            {
                indexes[listIndex] = currentIndex++;
                if(indexes[listIndex] >= sections.size())
                    return;
                is = sections.get(indexes[listIndex]);
            }
            
            is.nextStudent();
        }
    }
}
