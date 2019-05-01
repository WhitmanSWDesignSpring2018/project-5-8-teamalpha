/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

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
    
    public Collection<Playable> loadFile(){
        Collection<Playable> = new ArrayList()<Playable>; 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        try{
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse("testfile.txt");  //get file name here
            NodeList noteList = document.getElementsByTagName("note");
            NodeList gestureList = document.getElementsByTagName("gesture");
            for (int i = 0; i<noteList.getLength(); i++){
                Node n = noteList.item(i);
                Element note = (Element) n; 
                Playable newNote = parseNote(note);
                   
            }
            for (int j = 0; j<gestureList.getLength(); j++){
                Node g = gestureList.item(j);
                if (g.getNodeType() == Node.ELEMENT_NODE){
                    Element gesture = (Element) g; 
                    String isSelected = gesture.getAttribute("isSelected"); 
                    NodeList gestureChildrenList = g.getChildNodes();
                    for (int k=0; k<gestureChildrenList.getLength(); k++) {
                    }
                }
            }
        } catch (Exception e){
        
        }
    }
    
    public Playable parseGesture(Element gesture){
        Boolean isSelected = Boolean.parseBoolean(gesture.getAttribute("isSelected")); 
        NodeList gestureChildrenList = gesture.getChildNodes();
        Collection<Playable> contents = nodesToPlayables(gestureChildrenList); 
        Gesture newGesture = new Gesture(contents);
        newGesture.setSelected(isSelected); 
        return newGesture; 
    }
    
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
    
    public Collection<Playable> nodesToPlayables(NodeList playableNodes){
        Collection<Playable> allPlayables = new ArrayList<Playable>(); 
        for (int j = 0; j<playableNodes.getLength(); j++){
            Node p = playableNodes.item(j);
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
        return allPlayables;
    }
}
    
    
    

