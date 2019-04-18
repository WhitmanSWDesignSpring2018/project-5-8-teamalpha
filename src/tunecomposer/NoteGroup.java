/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import static tunecomposer.TuneComposer.allPlayables;

/**
 *
 * @author madoka
 */
public class NoteGroup {
    
    ClickController clickController;
    public NoteGroup(ClickController clickcontroller){
        clickController = clickcontroller;
    }
    
     /**
     * Makes a new gesture from the selected playables. Adds the rectangle to the NotePane and sets the event handlers.
     */
    public void makeGroup() {
        HashSet<Playable> selectedPlayables = new HashSet<Playable>();
    	allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                selectedPlayables.add(n);
            }
        });
    	
        Gesture group = new Gesture(selectedPlayables);
        allPlayables.add(group);
        selectedPlayables.add(group); 
        TuneComposer.notePane.getChildren().add(group.getRectangle());
        group.getRectangle().setOnMousePressed((MouseEvent pressedEvent) -> {
            clickController.handleNoteClick(pressedEvent, group);
            clickController.handleNotePress(pressedEvent, group);
        });
        
        group.getRectangle().setOnMouseDragged((MouseEvent dragEvent) -> {
            clickController.handleNoteDrag(dragEvent);
        });
        
        group.getRectangle().setOnMouseReleased((MouseEvent releaseEvent) -> {
            clickController.handleNoteStopDragging(releaseEvent);
        });
    }
    
    /**
     * Gets rid of all the top-level selected groups. Any nested or unselected gestures remain.
     */
    public void unGroup() {
    	ArrayList<Playable> selectedGestures = new ArrayList<Playable>();
    	ArrayList<Playable> topLevelGestures = new ArrayList<Playable>();
    	for (Playable p : allPlayables) {
    		if (p.isGesture() && p.getSelected()) {
    			selectedGestures.add(p);
    		}
    	}
    	boolean isIn;
    	for (Playable g : selectedGestures) {
    		isIn = false;
    		for (Playable h : selectedGestures) {
    			if (h.getContents().contains(g)) {
    				isIn = true;
    			}
    		}
    		if (!isIn) {
    			topLevelGestures.add(g);
    		}
    	}
    	for (Playable g : topLevelGestures) {
    		TuneComposer.notePane.getChildren().remove(g.getRectangle());
    		allPlayables.remove(g);
    	}
    }
    
    @FXML
    private void handleGroup() {
        makeGroup();
    }
    
    @FXML
    private void handleUnGroup() {
        unGroup();
    }
}
