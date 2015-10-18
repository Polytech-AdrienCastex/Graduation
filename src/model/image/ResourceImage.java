/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import javax.imageio.ImageIO;

/**
 *
 * @author Adrien
 */
public class ResourceImage extends Image
{
    public ResourceImage(byte[] data)
    {
        super();
        this.data = data;
    }
    public ResourceImage(byte[] data, BigInteger uid)
    {
        super(uid);
        this.data = data;
    }
    
    private final byte[] data;
    
    public InputStream toInputStream()
    {
        return new ByteArrayInputStream(data);
    }

    @Override
    public BufferedImage getImage() throws IOException
    {
        return ImageIO.read(toInputStream());
    }

    @Override
    public byte[] toBinary()
    {
        return data;
    }
}
