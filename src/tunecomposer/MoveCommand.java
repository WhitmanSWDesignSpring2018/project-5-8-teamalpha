/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;
import javafx.scene.input.MouseEvent;
import static tunecomposer.TuneComposer.allPlayables;

/**
 *
 * @author taylorkm
 */
public class MoveCommand implements Undoable {
    
    private Collection<Playable> movedPlayables;
  
    private double xDistance;
    
    private double yDistance;
   

    /**
     * Constructs the command from the given set of Playables. Probably what
     * we should do is find a way to tell how far they've moved and do some
     * math.
     * @param toEdit 
     */
    public MoveCommand(Collection<Playable> toMove, MouseEvent dragStartingPoint, MouseEvent dragEndingPoint) {
        movedPlayables = toMove;
        xDistance = dragEndingPoint.getX()-dragStartingPoint.getX();
        yDistance = dragEndingPoint.getY()-dragStartingPoint.getY(); 
    }

    /**
     * Un-edits the Playables in the set.
     */
    
    public void execute(){
        movedPlayables.forEach((Playable) -> {
            TuneComposer.allPlayables.add(Playable);
        }); 
        for (Playable p : movedPlayables) {
            p.move(xDistance,yDistance); 
        }
        
    }
    
    public void undo() {
        for (Playable p : movedPlayables) {
            p.unmove(-xDistance,-yDistance); 
        }
    }

    /**
     * Re-edits the Playables.
     */
    public void redo() {
        for (Playable p : movedPlayables) {
            p.move(xDistance,yDistance); 
        }
    }
    
}
