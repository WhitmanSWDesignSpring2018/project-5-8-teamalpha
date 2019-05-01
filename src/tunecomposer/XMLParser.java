/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author taylorkm
 */
public class XMLParser {
    
    public void initialize(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
    
        try{
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            Document document = builder.parse("testfile.txt");  //get file name here
            NodeList noteList = document.getElementsByTagName("note");
            NodeList gestureList = document.getElementsByTagName("gesture");
            for (int i = 0; i<noteList.getLength(); i++){
                Node n = noteList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE){
                    Element note = (Element) n; 
                    String x = note.getAttribute("x"); 
                    String y = note.getAttribute("y"); 
                    String instrument = note.getAttribute("instrument"); 
                    String width = note.getAttribute("width"); 
                    String isSelected = note.getAttribute("isSelected"); 
                }
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
        } catch (ParserConfigurationException e){
        
        } catch (SAXException e){
            
        }catch (IOException e){
            
        }
        
        
    }
    
    
    
}
