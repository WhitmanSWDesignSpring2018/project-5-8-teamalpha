/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;

/**
 * Represents the undoable action of selecting a note. Doesn't work.
 * @author Abbey Felley Colin Aslett Kimberly Taylor Angie Mead
 */
public class SelectCommand implements Undoable {
    private Collection<Playable> playables;
    
    private Collection<Playable> previouslySelected; 
    
    private boolean selected; 
    
    private boolean previousSelected; 

    /**
     * Initializes the command from a set of Playables and a boolean representing
     * their selected state
     * @param set, the set of Playables
     * @param sel, a boolean representing their state
     */
    public SelectCommand(Collection<Playable> set, Collection<Playable> previous, boolean sel, boolean previousSel) {
        playables = set;
        previouslySelected = previous; 
        selected = sel; 
        previousSelected = previousSel; 
    }
    
    public void execute(){
        redo(); 
    }
    /**
     * Unselects the Playables.
     */
    public void undo() {
        
        playables.forEach((Playable) -> {
            Playable.setSelected(!selected);
        }); 
        
        previouslySelected.forEach((Playable) -> {
            Playable.setSelected(!previousSelected);
        }); 

    }
    
    /**
     * Selects the given Playables
     */
    public void redo() {
        previouslySelected.forEach((Playable) -> {
            Playable.setSelected(previousSelected);
        }); 
        playables.forEach((Playable) -> {
            Playable.setSelected(selected);
        }); 
        
    }
}
