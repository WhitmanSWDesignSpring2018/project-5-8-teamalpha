package tunecomposer;

public interface Undoable {
	void execute();
	void undo();
	void redo();
}
