package tunecomposer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;


/**
 * Manages the undo and redo actions. Keeps two stacks, one for undoable actions
 * and one for redoable actions.
 * @author Abbey Felley Colin Aslett Kimberly Taylor Angie Mead
 */
public class CommandManager {
    /**
     * the deques of actions
     */
    private static Deque<Undoable> undoStack;
    private static Deque<Undoable> redoStack;

    /**
     * Initializes the deques.
     */
    public CommandManager() {
        super();
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    /**
     * Adds the given undoable action to the commandmanager by pushing it to the
     * undoStack and clearing the redoStack.
     * @param undoable 
     */
    public void add(final Undoable undoable) {
        if(undoable!=null) {
                undoStack.push(undoable);
                redoStack.clear(); 
        }
    }

    /**
     * Undoes the action at the front of the queue. Pops the item from the queue,
     * undoes it, and pushes it to the redoStack.
     */
    public void undo() {
        if(!undoStack.isEmpty()) {
                Undoable undoable = undoStack.pop();
                undoable.undo();
                redoStack.push(undoable);
        }
        //System.out.println(undoStack.size());
    }

    /**
     * Redoes the action at the front of the redoStack. Pops the action, redoes
     * it, and pushes it to the undoStack.
     */
    public void redo() {
        if(!redoStack.isEmpty()) {
                Undoable undoable = redoStack.pop();
                undoable.redo();
                undoStack.push(undoable);
        }
    }

// I wrote this to help debug
//    public int undoSize() {
//        return undoStack.size();
//    }
    
}
