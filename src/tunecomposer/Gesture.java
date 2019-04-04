/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author teamalpha
 */
public class Gesture implements Playable {
    
    /**
     * Note fields for creating a gesture rectangle 
     */
    
    private final Rectangle gestureRect;
    private double x_coord;
    private double y_coord;
    private double rectWidth;
    private double rectHeight;
    
    public static double lastNote = 0;
    
    /**
     * Offsets for dragging Rectangle
     */
    private double xOffset;
    private double yOffset;
    private double widthOffset;
    
    private boolean isSelected;
    
    private Set<Playable> contents;
    
      /**
     * Creates new selected Gesture which is sized based on the coordinates on its contents
     * @param newContents contents that are selected to be made into a gesture 
     */
    public Gesture(Set<Playable> newContents) {
        contents = new HashSet<Playable>(newContents); 
        System.out.println("created: " + contents.size());
        
        ArrayList<Double> xList = new ArrayList<Double>(contents.size());
        ArrayList<Double> yList = new ArrayList<Double>(contents.size());
        ArrayList<Double> ends = new ArrayList<Double>(contents.size());
        
        contents.forEach((playable) -> {
            xList.add(playable.getRectangle().getX());
            yList.add(playable.getRectangle().getY());
            ends.add(playable.getRectangle().getX() + playable.getWidth()); 
        });
        
        x_coord = Collections.min(xList);
        y_coord = Collections.min(yList);
        rectHeight = Collections.max(yList) - y_coord + 10;
        rectWidth = Collections.max(ends) - x_coord;
        
        gestureRect = new Rectangle(x_coord, y_coord, rectWidth, rectHeight);
        
        gestureRect.getStyleClass().addAll("selected", "gesture");
        gestureRect.setMouseTransparent(false);
        
        isSelected = true;
    }
    
    
    public Collection getContents() {
    	return contents;
    }
    
    public double getWidth() {
        return rectWidth;
    }
    
    /**
     * Update the last note so we know when to stop the player and red line
     */
    public void updateLastNote() {
        if (x_coord + rectWidth > lastNote) {
            lastNote = x_coord + rectWidth;
        }
    }
    
     /**
     * Get this Gestures's Rectangle object
     * @return this Gesture's Rectangle
     */
    public Rectangle getRectangle() {
        return gestureRect;
    }
    
    /**
     * Adds this Note to the MidiPlayer
     */
    public void schedule() {
        contents.forEach((playable) -> {
            playable.schedule();
            playable.updateLastNote();
        });
    }
    
     /**
     * Checks if this gesture is selected
     * @return true if gesture is selected, false otherwise
     */
    public boolean getSelected() {
        return isSelected;
    }
    
    
    public void setSelected(boolean selected) {
    	//System.out.println(gestureRect.getY());
        isSelected = selected;
        
//        contents.forEach((playable) -> {
//        	playable.getRectangle().getStyleClass().addAll("selected");
//            //playable.setSelected(isSelected);
//            //System.out.println("leaves: " + selected);
//        });
        
        if (isSelected) {
        	gestureRect.getStyleClass().clear();
        	gestureRect.getStyleClass().addAll("selected", "gesture");
//        	contents.forEach((playable) -> {
//        		playable.setSelected(true);
//        	});
        } else {
        	gestureRect.getStyleClass().clear();
        	gestureRect.getStyleClass().addAll("unselected-grouped", "gesture");
        }
        
        for (Playable playable: contents){
            playable.setSelected(isSelected);
        }
        
//      System.out.println("contents " + contents.size());
//      System.out.println("y " + y_coord);
        
        //System.out.println(gestureRect.getY());
        
    }
    
    /**
     * When the user presses the mouse to start dragging, calculate the offset
     * between the upper-left corner of the Gesture and where the mouse is
     * in the Gesture
     * @param event mouse click
     */
    public void setMovingCoords(MouseEvent event) {
        xOffset = event.getX() - x_coord;
        yOffset = event.getY() - y_coord;
    }
    
    /**
     * While the user is dragging the mouse, move the Gesture with it
     * @param event mouse drag
     */
    public void moveRect(MouseEvent event) {
        gestureRect.setX(event.getX() - xOffset);
        gestureRect.setY(event.getY() - yOffset);
    }
    
    /**
     * When the user stops dragging the mouse, set Note fields to the
     * Gesture's current location
     * @param event mouse click
     */
    public void stopMoving(MouseEvent event) {
        double x = event.getX() - xOffset;
        double y = event.getY() - yOffset;
        
        x_coord = x;
        y_coord = y - (y % 10);
        
        gestureRect.setX(x_coord);
        gestureRect.setY(y_coord);
        
    }
    
    public boolean isGesture(){return true; }
    
    public boolean inLastFive(MouseEvent event) { return false; }
    
    /**
     * we should delete these if we don't 
     */
    public void setMovingDuration(MouseEvent event) {}
    public void moveDuration(MouseEvent event) {}
    public void stopDuration(MouseEvent event) {}
}
