/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 * Represents the action of creating a gesture.
 * @author Abbey Felley Colin Aslett Kimberly Taylor Angie Mead
 */
public class GroupCommand implements Undoable{
    private Gesture gesture;

    /**
     * Initializes the Gesture owned by the GroupCommand.
     * @param newGesture 
     */
    public GroupCommand(Gesture newGesture) {
            gesture = newGesture;
    }

    /**
     * Removes the Gesture from the composition. Removes it from allPlayables 
     * and hides the rectangle from view.
     */
    public void undo() {
            TuneComposer.allPlayables.remove(gesture);
            gesture.getRectangle().setVisible(false);
    }

    /**
     * Adds the Gesture to the composition. Adds it to allPlayables and sets the
     * rectangle to invisible.
     */
    public void redo() {
            TuneComposer.allPlayables.add(gesture);
            gesture.getRectangle().setVisible(true);
    }
}
