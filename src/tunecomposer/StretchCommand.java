/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author taylorkm
 */
public class StretchCommand implements Undoable {
    
    private Collection<Playable> stretchedPlayables;
    
    double xDistance; 

    /**
     * Constructs the command from the given set of Playables. Probably what
     * we should do is find a way to tell how far they've moved and do some
     * math.
     * @param toEdit 
     */
    public StretchCommand(Collection<Playable> toStretch, MouseEvent dragStartingPoint, MouseEvent dragEndingPoint) {
        stretchedPlayables = toStretch;
        xDistance = dragEndingPoint.getX()- dragStartingPoint.getX(); 
    }
    
    public void execute(){
        stretchedPlayables.forEach((Playable) -> {
            TuneComposer.allPlayables.add(Playable);
        }); 
        for (Playable p : stretchedPlayables) {
            p.stretch(xDistance); 
        }
    }

    /**
     * Un-edits the Playables in the set.
     */
    public void undo() {
        for (Playable p : stretchedPlayables) {
            p.stretch(-xDistance); 
        }
    }

    /**
     * Re-edits the Playables.
     */
    public void redo() {
        for (Playable p : stretchedPlayables) {
            p.stretch(xDistance); 
        }
    }
    
}
