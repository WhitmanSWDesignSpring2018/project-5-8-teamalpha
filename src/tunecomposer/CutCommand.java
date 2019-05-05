/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;

/**
 * Cut is an undoable command that can be undone/redoable
 * @author taylorkm
 */
public class CutCommand implements Undoable {
    
    public Collection<Playable> toCutPlayables; 
    
    public CutCommand(Collection<Playable> cutPlayables){
        toCutPlayables = cutPlayables; 
    }
    
    public void execute(){
        redo(); 
    }
    
    public void undo(){
        for (Playable p : toCutPlayables) {
            p.getRectangle().setVisible(true);
        }
        TuneComposer.allPlayables.addAll(toCutPlayables);
        
    }
    
    public void redo(){
        for (Playable p : toCutPlayables) {
            p.getRectangle().setVisible(false);
        }
        TuneComposer.allPlayables.removeAll(toCutPlayables);
        
    }
    
    
}
