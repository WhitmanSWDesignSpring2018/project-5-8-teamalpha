/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import static tunecomposer.TuneComposer.allPlayables;
import static tunecomposer.TuneComposer.commandManager;

/**
 *
 * @author aslettcj
 */
public class ClickController {
     /**
     * Boolean flags to control flow when user clicks in composition panel
     */
    private boolean clickInPane = true;
    private boolean changeDuration = false;
    private boolean isDragSelecting = false;
    private SelectionArea selection;
    @FXML
    private Rectangle selectRect;
    
    ButtonController buttonController;
    
    @FXML
    public static AnchorPane playLinePane;
    
    public ClickController(ButtonController buttoncontroller){
        buttonController = buttoncontroller;
    }
    /**
     * When user presses on a note, set the notes to be selected or 
     * unselected accordingly.
     * @param event mouse click
     * @param playable note Rectangle that was clicked
     */
    public void handleNoteClick(MouseEvent event, Playable playable) {
        clickInPane = false;
        boolean control = event.isControlDown();
        boolean selected = playable.getSelected();
        if (! control && ! selected) {
            buttonController.selectAll(false);
            playable.setSelected(true);
        } else if (control && ! selected) {
            playable.setSelected(true);
        } else if (control && selected) {
            playable.setSelected(false);
        }
    }
    /**
     * Construct a note from a click. Called via FXML.
     * @param event a mouse click
     */
    public void handleClick(MouseEvent event) {
        if (buttonController.playLine.isPlaying()) {
            buttonController.stopPlaying();
        }
        else if (isDragSelecting){
            isDragSelecting = false;
            selection.endRectangle();
        }
        else if (clickInPane) {
        	//System.out.println("else 2");
            if (!event.isControlDown()) {
                buttonController.selectAll(false);
            }
            
            Instrument instrument = TuneComposer.getInstrument();
            Note note = new Note(event.getX(), event.getY(), instrument);
            
            allPlayables.add(note);
            TuneComposer.notePane.getChildren().add(note.getRectangle());
            
            note.getRectangle().setOnMousePressed((MouseEvent pressedEvent) -> {
                handleNoteClick(pressedEvent, note);
                handleNotePress(pressedEvent, note);
            });
            
            note.getRectangle().setOnMouseDragged((MouseEvent dragEvent) -> {
                handleNoteDrag(dragEvent);
            });
            
            note.getRectangle().setOnMouseReleased((MouseEvent releaseEvent) -> {
                handleNoteStopDragging(releaseEvent);
            });
        }
        clickInPane = true;
    }

    
    /**
     * When user presses on a note, set offsets in each Note in case the user
     * drags the mouse.
     * @param event mouse click
     * @param note note Rectangle that was clicked
     */
    public void handleNotePress(MouseEvent event, Playable playable) {
        changeDuration = playable.inLastFive(event);
        allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.setMovingDuration(event);
                } else {
                    n.setMovingCoords(event);
                }
            }
        });
    }
    
    /**
     * When the user drags the mouse on a playable Rectangle, move all selected
     * playables
     * @param event mouse drag
     */
    public void handleNoteDrag(MouseEvent event) {
        allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.moveDuration(event);
                } else {
                    n.moveRect(event);
                }
            }
        });
    }
    
    /**
     * When the user stops dragging the mouse, stop moving the selected notes
     * @param event mouse click
     */
    public void handleNoteStopDragging(MouseEvent event) {
        clickInPane = false;
        allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.stopDuration(event);
                } else {
                    n.stopMoving(event);
                }
            }
        });
        changeDuration = false;
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition
     * is playing, and starts dragging selection rectangle if mouse click is
     * not on a note Rectangle.
     * @param event mouse click
     */
    public void startDrag(MouseEvent event) {
        if (buttonController.playLine.isPlaying()) {
            buttonController.stopPlaying();
        } else if (clickInPane) {
            handleSelectionStartDrag(event);
        }
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition
     * is playing, and continues to drag selection rectangle if initial mouse 
     * click was not on a note Rectangle.
     * @param event mouse click
     */
    public void continueDrag(MouseEvent event) {
        if (buttonController.playLine.isPlaying()) {
            buttonController.stopPlaying();
        } else if (clickInPane) {
            handleSelectionContinueDrag(event);
        }
    }
    
    /**
     * Move lower-right corner of selection rectangle with the dragging mouse
     * @param event mouse drag
     */
    private void handleSelectionStartDrag(MouseEvent event) {
        isDragSelecting = true;
        
        selection.startRectangle(event.getX(), event.getY());

        if(!event.isControlDown()){
            buttonController.selectAll(false);
        }
    }
    
    /**
     * Continue to update notes throughout drag.
     * @param event Current value of MouseEvent
     */
    private void handleSelectionContinueDrag(MouseEvent event) {
        selection.update(event.getX(), event.getY());

        allPlayables.forEach((note) -> {
            Rectangle rect = note.getRectangle();
            double horizontal = selectRect.getX() + selectRect.getWidth();
            double vertical = selectRect.getY() + selectRect.getHeight();

            // Thanks to Paul for suggesting the `intersects` method.
            if(selection.getRectangle().intersects(note.getRectangle().getLayoutBounds())) {
                note.setSelected(true);
            } else {
                if(note.getSelected()) {
                    note.setSelected(false);
                }
            }
        });
    }
}
