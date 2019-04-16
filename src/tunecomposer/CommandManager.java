package tunecomposer;
import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
	private static Deque<Undoable> undoStack;
	private static Deque<Undoable> redoStack;
	
	private CommandManager() {
		super();
		undoStack = new ArrayDeque<>();
		redoStack = new ArrayDeque<>();
	}
	
	public void add(final Undoable undoable) {
		if(undoable!=null) {
			undoStack.addFirst(undoable);
			redoStack.clear(); 
		}
	}
	
	public void undo() {
		if(!undoStack.isEmpty()) {
			final Undoable undoable = undoStack.removeFirst();
			undoable.undo();
			redoStack.addFirst(undoable);
		}
	}
	
	public void redo() {
		if(!redoStack.isEmpty()) {
			final Undoable undoable = redoStack.removeFirst();
			undoable.redo();
			undoStack.addFirst(undoable);
		}
	}
	
	
}
