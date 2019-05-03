/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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
            NodeList noteList = document.getElementsByTagName("note");
            NodeList gestureList = document.getElementsByTagName("gesture");
            
            for (int i = 0; i<noteList.getLength(); i++) {
                Node n = noteList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE){
                Element note = (Element) n; 
                Playable newNote = parseNote(note);
                playablesToLoad.add(newNote); 
                }
            }
            for (int j = 0; j<gestureList.getLength(); j++){
                Node g = gestureList.item(j);
                Element gesture = (Element) g; 
                Playable newGesture = parseGesture(gesture); 
                playablesToLoad.add(newGesture); 
            }
        } catch (ParserConfigurationException e){
        System.out.println("exception");
        }
        return playablesToLoad; 
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
        Collection<Playable> contents = nodesToPlayables(gestureChildrenList); 
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
        
        Note newNote = new Note(x,y,instrument,width);
        newNote.setSelected(isSelected);
        return newNote; 
    }
    
    /**
     * Turns a NodeList into a Collection of playables. Recursively calls 
     * parseGesture until there are no more nested gestures.
     * @param playableNodes, the NodeList to parse
     * @return the Collection of playables that was created
     */
    public Collection<Playable> nodesToPlayables(NodeList playableNodes){
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
    
    
    

