/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Adrien
 */
public class LocalImage extends Image
{
    public LocalImage(String filePath)
    {
        super();
        this.filePath = filePath;
    }
    public LocalImage(File file)
    {
        this(file.getPath());
    }
    
    private final String filePath;
    
    public File toFile()
    {
        return new File(filePath);
    }

    @Override
    public BufferedImage getImage() throws IOException
    {
        return ImageIO.read(toFile());
    }

    @Override
    public byte[] toBinary()
    {
        try
        {
            return Files.readAllBytes(toFile().toPath());
        }
        catch (IOException ex)
        {
            return new byte[0];
        }
    }
}
