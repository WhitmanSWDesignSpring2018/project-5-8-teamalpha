package tunecomposer;

import javafx.scene.input.MouseEvent;

public class AddNoteCommand implements Undoable {
	private static Note note;
	
	public AddNoteCommand(Note newNote) {
		note = newNote;
	}
	
	public void execute() {
		
	}
	
	public void undo() {
		TuneComposer.allPlayables.remove(note);
		note.getRectangle().setVisible(false);
	}
	
	public void redo() {
		TuneComposer.allPlayables.add(note);
		note.getRectangle().setVisible(true);
	}
}
