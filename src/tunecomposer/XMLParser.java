/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author taylorkm
 */
public class XMLParser {
    
    public void initialize(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
    
        try{
            DocumentBuilder builder = factory.newDocumentBuilder(); 
        } catch (ParserConfigurationException e){
        
        }
    }
    
    
    
}
