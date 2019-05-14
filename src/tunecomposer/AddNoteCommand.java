package tunecomposer;

import java.util.Collection;

/**
 * Represents the action of adding a note 
 * @author Colin Aslett Abbey Felley Kimberly Taylor Angie Mead
 */
public class AddNoteCommand implements Undoable {
    
    private Note note;
    
    private Collection<Playable> selectedPlayables; 
    

    /**
     * Creates the command based on the given note.
     * @param newNote, the note that the action will be able to do and undo
     */
    public AddNoteCommand(Note newNote, Collection<Playable> selectedNotes) {
        note = newNote;
        selectedPlayables = selectedNotes; 
    }
    
    public void execute(){
        redo();
    }

    /**
     * Undoes the addition of the note. Removes the note from allPlayables
     * and hides the rectangle so that it neither appears nor plays the note
     */
    public void undo() {
        TuneComposer.allPlayables.remove(note);
        note.getRectangles().setVisible(false);
        selectedPlayables.forEach((Playable) -> {
            Playable.setSelected(true);
        }); 
    }

    /**
     * Adds a note to the composition. Adds the stored note to allPlayables 
     * and shows the rectangle
     */
    public void redo() {
        TuneComposer.allPlayables.add(note);
        note.getRectangles().setVisible(true);
        selectedPlayables.forEach((Playable) -> {
            Playable.setSelected(false);
        }); 
    }
}
