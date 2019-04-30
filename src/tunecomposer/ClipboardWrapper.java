/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;
import java.util.HashSet;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 *
 * @author taylorkm
 */
public class ClipboardWrapper {
    
    private final Clipboard clipboard;
    
    public ClipboardWrapper(){
        clipboard = Clipboard.getSystemClipboard();
    }
    
    public void addToClipboard(Collection<Playable> selectedPlayables){
        String playableString = new String(); 
        for (Playable p : selectedPlayables){
            playableString = playableString + p.toString() +  "\n" ;  
        } 
        ClipboardContent content = new ClipboardContent(); 
        content.putString(playableString);  
        clipboard.setContent(content); 
        
    }
    
    public Collection<Playable> popFromClipboard(){
        
    }
    
}
