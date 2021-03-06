package tunecomposer;

import java.util.Collection;

/**
 * Represents the delete selected notes action.
 * @author Abbey Felley Colin Aslett Kimberly Taylor Angie Mead
 */
public class DeleteCommand implements Undoable {
    private Collection<Playable> removedPlayables;

    /**
     * Initializes the DeleteCommand.
     * @param toRemove, the collection of Playables to be deleted
     */
    public DeleteCommand(Collection<Playable> toRemove) {
        removedPlayables = toRemove;
    }
    
    public void execute(){
        redo(); 
    }

    /**
     * Puts back the deleted playables by adding them back to allPlayables.
     */
    public void undo() {
        for (Playable p : removedPlayables) {
            p.getRectangles().setVisible(true);
        }
        TuneComposer.allPlayables.addAll(removedPlayables);
    }

    /**
     * Removes the playables from the composition. Removes the playables
     * from allPlayables and hides the note rectangles from view.
     */
    public void redo() {
        for (Playable p : removedPlayables) {
            p.getRectangles().setVisible(false);
        }
        TuneComposer.allPlayables.removeAll(removedPlayables);
    }
}
