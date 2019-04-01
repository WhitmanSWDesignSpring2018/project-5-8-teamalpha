/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author taylorkm
 */
public class Gesture implements Playable {
    
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
    
    public Gesture(Set<Playable> newContents) {
        contents = newContents;
        System.out.println("contents: " + contents.size());
        
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
        System.out.println(x_coord);
        System.out.println(y_coord);
        System.out.println(rectWidth);
        System.out.println(rectHeight);
        
        gestureRect.getStyleClass().addAll("selected", "gesture");
        gestureRect.setMouseTransparent(false);
        
        isSelected = true;
    }
    
    public double getWidth() {
        return rectWidth;
    }
    
    public void updateLastNote() {
        if (x_coord + rectWidth > lastNote) {
            lastNote = x_coord + rectWidth;
        }
    }
    
    public Rectangle getRectangle() {
        return gestureRect;
    }
    
    
    public void schedule() {
        contents.forEach((playable) -> {
            playable.schedule();
            playable.updateLastNote();
        });
    }
    
    public boolean getSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
        
        if (selected) {
        	gestureRect.getStyleClass().clear();
        	gestureRect.getStyleClass().addAll("selected", "gesture");
        } else {
        	gestureRect.getStyleClass().clear();
        	gestureRect.getStyleClass().addAll("unselected", "gesture");
        }
        // add or remove selected style class 
        
        contents.forEach((playable) -> {
            playable.setSelected(selected);
        });
    }
    
    /**
     * When the user presses the mouse to start dragging, calculate the offset
     * between the upper-left corner of the Rectangle and where the mouse is
     * in the Rectangle
     * @param event mouse click
     */
    public void setMovingCoords(MouseEvent event) {
        xOffset = event.getX() - x_coord;
        yOffset = event.getY() - y_coord;
    }
    
    /**
     * While the user is dragging the mouse, move the Rectangle with it
     * @param event mouse drag
     */
    public void moveRect(MouseEvent event) {
        gestureRect.setX(event.getX() - xOffset);
        gestureRect.setY(event.getY() - yOffset);
    }
    
    /**
     * When the user stops dragging the mouse, set Note fields to the
     * Rectangle's current location
     * @param event mouse click
     */
    public void stopMoving(MouseEvent event) {
        double x = event.getX() - xOffset;
        double y = event.getY() - yOffset;
        
        x_coord = x;
        y_coord = y - (y % rectHeight);
        
        gestureRect.setX(x_coord);
        gestureRect.setY(y_coord);
    }
    
    public boolean inLastFive(MouseEvent event) { return false; }
    public void setMovingDuration(MouseEvent event) {}
    public void moveDuration(MouseEvent event) {}
    public void stopDuration(MouseEvent event) {}
}
