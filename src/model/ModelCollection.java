/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.model.Section;
import model.model.Talk;
import model.model.Side;
import model.serializable.XMLSerializable;
import model.image.ResourceImage;
import model.image.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Adrien
 */
public class ModelCollection
{
    private ModelCollection()
    { }
    
    private final Collection<Section> sections = new ArrayList<>();
    private final Collection<Talk> talks = new LinkedList<>();
    
    public Collection<Section> getSections()
    {
        return sections;
    }
    public Collection<Talk> getTalks()
    {
        return talks;
    }
    
    public static Builder create()
    {
        return new Builder();
    }
    public static class Builder
    {
        private ModelCollection model = new ModelCollection();
        
        public Builder addSection(Section section)
        {
            model.sections.add(section);
            return this;
        }
        public Builder addTalk(Talk talk)
        {
            model.talks.add(talk);
            return this;
        }
        
        public ModelCollection build()
        {
            return model;
        }
    }
    
    
    protected static Stream<Node> toStream(NodeList list)
    {
        return toCollection(list).stream();
    }
    protected static Collection<Node> toCollection(NodeList list)
    {
        Collection<Node> nodes = new LinkedList<>();

        for(int i = 0; i < list.getLength(); i++)
            nodes.add(list.item(i));

        return nodes;
    }
    
    protected static Image toImage(Node node)
    {
        String content = node.getTextContent();
        if(content == null || content.isEmpty())
            return null;
        
        BigInteger uid = new BigInteger(content.trim());
        return Image.getImageByID(uid);
    }
    
    public static ModelCollection load(File source) throws ParserConfigurationException, SAXException, IOException
    {
        ModelCollection model = new ModelCollection();
        
        // Load images
        File dataFile = new File(source.getParent(), source.getName() + ".data");
        Image.resetImageSet();
        if(dataFile.exists())
        {
            byte[] data = Files.readAllBytes(dataFile.toPath());
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            
            while(bais.available() != 0)
            {
                // UID_SIZE | UID | DATA_SIZE | DATA...
                byte[] uid_size_d = new byte[4];
                bais.read(uid_size_d, 0, 4);
                int uid_size = toInt(uid_size_d);
                
                byte[] uid_d = new byte[uid_size];
                bais.read(uid_d, 0, uid_size);
                
                byte[] data_size_d = new byte[4];
                bais.read(data_size_d, 0, 4);
                int data_size = toInt(data_size_d);
                
                byte[] data_d = new byte[data_size];
                bais.read(data_d, 0, data_size);
                
                // Reference the resource
                System.out.println(uid_size);
                System.out.println(uid_d.length);
                System.out.println(":: " + new String(uid_d));
                ResourceImage ri = new ResourceImage(data_d, new BigInteger(new String(uid_d)));
            }
        }
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(source);
        
        // Load sections
        toStream(doc.getElementsByTagName("section"))
                .map(Node::getChildNodes)
                .map(ModelCollection::toCollection)
                .map(ns -> new Section(
                        ns.stream()
                                .filter(n -> "name".equals(n.getNodeName().toLowerCase()))
                                .map(Node::getTextContent)
                                .findFirst()
                                .orElse(""),
                        ns.stream()
                                .filter(n -> "side".equals(n.getNodeName().toLowerCase()))
                                .map(Node::getTextContent)
                                .map(Side::getFromValue)
                                .findFirst()
                                .orElse(Side.LEFT)))
                .forEach(model.sections::add);
        
        // Load talks
        toStream(doc.getElementsByTagName("talk"))
                .map(Node::getChildNodes)
                .map(ModelCollection::toCollection)
                .map(ns -> new Talk(
                        ns.stream()
                                .filter(n -> "title".equals(n.getNodeName().toLowerCase()))
                                .map(Node::getTextContent)
                                .findFirst()
                                .orElse(""),
                        ns.stream()
                                .filter(n -> "text".equals(n.getNodeName().toLowerCase()))
                                .map(Node::getTextContent)
                                .findFirst()
                                .orElse(""),
                        ns.stream()
                                .filter(n -> "image".equals(n.getNodeName().toLowerCase()))
                                .map(ModelCollection::toImage)
                                .filter(i -> i != null)
                                .findFirst()
                                .orElse(null)))
                .forEach(model.talks::add);
        
        return model;
    }
    
    
    protected static byte[] toBytes(int value)
    {
        byte[] data = new byte[4];
        for(int i = 0; i < data.length; i++)
            data[i] = (byte)((value >> (i * 8)) & 0xFF);
        return data;
    }
    protected static int toInt(byte[] data)
    {
        int value = 0;
        int mask = 0xFF;
        for(int i = 0; i < data.length; i++)
        {
            value += (((int)data[i]) << (i * 8)) & mask;
            mask <<= 8;
        }
        return value;
    }
    public void save(File destination) throws IOException
    {
        String xml = "";
        
        xml += "<model>";
        
        xml += "<sections>";
        xml += sections.stream()
                .map(XMLSerializable::toXML)
                .reduce("", (s1,s2) -> s1 + s2);
        xml += "</sections>";
        
        xml += "<talks>";
        xml += talks.stream()
                .map(XMLSerializable::toXML)
                .reduce("", (s1,s2) -> s1 + s2);
        xml += "</talks>";
        
        xml += "</model>";
        
        Files.write(destination.toPath(), xml.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        
        File data = new File(destination.getParentFile(), destination.getName() + ".data");
        data.delete();
        
        Stream.concat(sections.stream().flatMap(s -> s.students.stream()).map(s -> s.picture), talks.stream().map(t -> t.picture))
                .filter(p -> p != null)
                .forEach(p ->
                {
                    // UID_SIZE | UID | DATA_SIZE | DATA...
                    String uid = p.getID().toString(16);
                    int uid_size = uid.length();
                    byte[] ds = p.toBinary();
                    int data_size = ds.length;
                    
                    try
                    {
                        Files.write(data.toPath(), toBytes(uid_size), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        Files.write(data.toPath(), uid.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        Files.write(data.toPath(), toBytes(data_size), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        Files.write(data.toPath(), ds, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    }
                    catch (IOException ex)
                    { }
                });
    }
}
