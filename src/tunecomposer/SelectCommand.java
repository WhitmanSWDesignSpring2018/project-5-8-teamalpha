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
    boolean selected;
    
    /**
     * Initializes the command from a set of Playables and a boolean representing
     * their selected state
     * @param set, the set of Playables
     * @param sel, a boolean representing their state
     */
    public SelectCommand(Collection<Playable> set, boolean sel) {
        playables = set;
        selected = sel;
    }
    
    public void execute(){
        redo(); 
    }
    /**
     * Unselectes the Playables.
     */
    public void undo() {
        for (Playable p : playables) {
            p.setSelected(!selected);
        }
    }
    
    /**
     * Selects the given Playables
     */
    public void redo() {
        for (Playable p : playables) {
            p.setSelected(selected);
        }
    }
}
