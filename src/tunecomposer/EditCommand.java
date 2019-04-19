/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;

/**
 * Represents the command for editing notes by dragging. Doesn't work.
 * @author Abbey Felley Colin Aslett Kimberly Taylor Angie Mead
 */
public class EditCommand {
    private static Collection<Playable> editedPlayables;

    /**
     * Constructs the command from the given set of Playables. Probably what
     * we should do is find a way to tell how far they've moved and do some
     * math.
     * @param toEdit 
     */
    public EditCommand(Collection<Playable> toEdit) {
        editedPlayables = toEdit;
    }

    /**
     * Un-edits the Playables in the set.
     */
    public void undo() {
    }

    /**
     * Re-edits the Playables.
     */
    public void redo() {
    }
}
