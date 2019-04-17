/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 *
 * @author taylorkm
 */
public class GroupCommand implements Undoable{
    private static Gesture gesture;
	
	public GroupCommand(Gesture newGesture) {
		gesture = newGesture;
	}
        
	public void undo() {
		TuneComposer.allPlayables.remove(gesture);
		gesture.getRectangle().setVisible(false);
	}
	
	public void redo() {
		TuneComposer.allPlayables.add(gesture);
		gesture.getRectangle().setVisible(true);
	}
    
    
}
