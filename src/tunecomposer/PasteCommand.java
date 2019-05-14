/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;

/**
 *
 * @author taylorkm
 */
public class PasteCommand implements Undoable {
    
    Collection<Playable> toPastePlayables; 
    
    public PasteCommand(Collection<Playable> pastedPlayables){
        toPastePlayables = pastedPlayables; 
    }
    
    public void execute(){
        redo(); 
    }
    
    
    public void undo(){
        for (Playable p : toPastePlayables) {
            p.getRectangles().setVisible(false);
        }
        TuneComposer.allPlayables.removeAll(toPastePlayables);
    }
    
    public void redo(){
        for (Playable p : toPastePlayables) {
            p.getRectangles().setVisible(true);
        }
        TuneComposer.allPlayables.addAll(toPastePlayables);
    }
    
    
}
