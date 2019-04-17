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
public class SelectionCommand {
     private static SelectionArea selectionArea;
	
	public SelectionCommand(SelectionArea newSelectionArea) {
		selectionArea = newSelectionArea;
	}
        
	public void undo() {
		selectionArea.getRectangle().setVisible(false);
	}
	
	public void redo() {
		selectionArea.getRectangle().setVisible(true);
	}
    
}
