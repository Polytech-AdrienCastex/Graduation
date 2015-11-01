package model.image;

import model.serializable.BinarySerializable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Adrien Castex
 */
public abstract class Image implements BinarySerializable
{
    public Image()
    {
        this.UID = generateUID();
        images.add(this);
    }
    public Image(BigInteger uid)
    {
        this.UID = uid;
        
        synchronized(sUID)
        {
            if(sUID.subtract(uid).signum() < 0)
                sUID = uid;
        }
        
        images.add(this);
    }
    
    static
    {
        images = new ConcurrentLinkedQueue<>();
        sUID = BigInteger.ZERO;
    }
    
    private static Collection<Image> images;
    public static Image getImageByID(BigInteger id)
    {
        return images.stream()
                .filter(i -> i.getID().equals(id))
                .findFirst()
                .orElse(null);
    }
    public static void resetImageSet()
    {
        images.clear();
        sUID = BigInteger.ZERO;
    }
    
    protected final BigInteger UID;
    public BigInteger getID()
    {
        return UID;
    }
    
    private static BigInteger sUID;
    private static BigInteger generateUID()
    {
        synchronized(sUID)
        {
            sUID = sUID.add(BigInteger.ONE);
            return sUID;
        }
    }
    
    public abstract BufferedImage getImage() throws IOException;
}
