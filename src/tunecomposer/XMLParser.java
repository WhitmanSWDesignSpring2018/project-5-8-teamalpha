/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 *
 * @author taylorkm
 */
public class XMLParser {
    
    Instrument tempInstrument;
    
    /**
     * Constructs the parser.
     */
    public XMLParser(){
        tempInstrument = Instrument.PIANO;
    }
    
    /**
     * Loads a file with xml text and parses it into a collection of Playables.
     * @param filename, the name of the file to parse
     * @return a collection of playables parsed from the file
     * @throws org.xml.sax.SAXException
     * @throws IOException 
     */
    public Collection<Playable> loadFile(File filename) throws org.xml.sax.SAXException, IOException{
        Collection<Playable> playablesToLoad = new ArrayList<Playable>(); 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        try{
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(filename);  //get file name here
            document.getDocumentElement().normalize();
            
            NodeList mainList = document.getDocumentElement().getChildNodes();
            playablesToLoad = nodesToComposition(mainList);
            return playablesToLoad;
        } catch (ParserConfigurationException e){

        }
        return null;
    }
    
    public Collection<Playable> loadFile(String XMLString) throws org.xml.sax.SAXException, IOException{
        Collection<Playable> playablesToLoad = new ArrayList<Playable>(); 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        try{
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse(new InputSource(new StringReader(XMLString))); 
            document.getDocumentElement().normalize();
            
            NodeList mainList = document.getDocumentElement().getChildNodes();
            playablesToLoad = nodesToComposition(mainList);
            return playablesToLoad;
           
        } catch (ParserConfigurationException e){
       
        }
        return null; 
    }

    /**
     * Turns the given element into a gesture. Calls nodesToPlayables recursively
     * until there are no more nested gestures.
     * @param gesture, the element of the node
     * @return the gesture that was parsed
     */
    public Playable parseGesture(Element gesture){
        Boolean isSelected = Boolean.parseBoolean(gesture.getAttribute("isSelected")); 
        NodeList gestureChildrenList = gesture.getChildNodes();
        Collection<Playable> contents = nodesToComposition(gestureChildrenList); 
        Gesture newGesture = new Gesture(contents);
        newGesture.setSelected(isSelected); 
        return newGesture; 
    }
    
    /**
     * Turns the given element into a note. Takes the parsed xml element and creates
     * and returns a note.
     * @param note, the parsed xml element
     * @return the note that was created
     */
    public Playable parseNote(Element note){
        Double x = Double.parseDouble(note.getAttribute("x")); 
        Double y = Double.parseDouble(note.getAttribute("y")); 
        Instrument instrument = tempInstrument.toInstrument(note.getAttribute("instrument")); 
        Double width = Double.parseDouble(note.getAttribute("width")); 
        Boolean isSelected = Boolean.parseBoolean(note.getAttribute("isSelected")); 
        int volume = Integer.parseInt(note.getAttribute("volume"));
        
        Note newNote = new Note(x, y, instrument, width, volume);
        newNote.setSelected(isSelected);
        newNote.setOpacity((double)volume/127);
        return newNote; 
    }
    
    /**
     * Turns a NodeList into a Collection of playables. Recursively calls 
     * parseGesture until there are no more nested gestures.
     * @param playableNodes, the NodeList to parse
     * @return the Collection of playables that was created
     */
    public Collection<Playable> nodesToComposition(NodeList playableNodes){
        Collection<Playable> allPlayables = new ArrayList<Playable>(); 
        for (int j = 0; j<playableNodes.getLength(); j++){
            Node p = playableNodes.item(j);
            if (p.getNodeType() == Node.ELEMENT_NODE){
                Element playable = (Element) p; 
                if (playable.getTagName() == "note" ){
                    Playable note = parseNote(playable); 
                    allPlayables.add(note); 
                }
                else{
                    Playable gesture = parseGesture(playable); 
                    allPlayables.add(gesture); 
                }
            }
        }
        return allPlayables;
    }
}
    
    
    

