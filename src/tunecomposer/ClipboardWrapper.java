/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.xml.sax.SAXException;

/**
 *
 * @author taylorkm
 */
public class ClipboardWrapper {
    
    private final Clipboard clipboard;
    
    private XMLParser xmlparser; 
    
    public ClipboardWrapper(){
        xmlparser = new XMLParser();
        clipboard = Clipboard.getSystemClipboard();
    }
    
    public void pushToClipboard(Collection<Playable> selectedPlayables){
        String playableString = new String(); 
        for (Playable p : selectedPlayables){
            playableString = playableString + p.toString() +  "\n" ;  
        } 
        playableString = "<composition> \n" + playableString + "</composition>"; 
        ClipboardContent content = new ClipboardContent(); 
        content.putString(playableString);  
        clipboard.setContent(content); 
    }
    
    public Collection<Playable> popFromClipboard() throws SAXException, IOException{
        String clipboardString = clipboard.getString(); 
        System.out.println(clipboardString);
        Collection<Playable> poppedFromClipboard = xmlparser.loadFile(clipboardString);
        System.out.println(poppedFromClipboard.size()); 
        return poppedFromClipboard;
    }
}
