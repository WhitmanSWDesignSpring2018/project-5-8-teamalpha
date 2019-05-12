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
import java.util.List;
import java.util.Set;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Creates a gesture grouping playables together.
 * @author Abbey Felley, Colin Aslett, Angie Mead, Kimberly Taylor
 */
public class Gesture implements Playable {
    /**
     * Constants for the rectangle 
     */
    private final Rectangle gestureRect;
    private double x_coord;
    private double y_coord;
    private double rectWidth;
    private double rectHeight;
    private final int GRIDHEIGHT = 10;
    
    public static double lastNote = 0;
    
    /**
     * Offsets for dragging Rectangle
     */
    private double xOffset;
    private double yOffset;
    private double widthOffset;
    
    private boolean isSelected;
    private int startTime;
    
    /**
     * a set of everything contained in the gesture
     */
    private Set<Playable> contents;
    
    /**
     * Creates a new Gesture with the given contents. Calculates the height and
     * width of the rectangle from the contents
     * @param newContents, set of Playables to be included in the new gesture
     */
    public Gesture(Collection<Playable> newContents) {
        contents = new HashSet<Playable>(newContents); 

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
        startTime = (int)x_coord;
    }
    
    /**
     * Gets the width of the gesture's rectangle.
     */
    public double getWidth() {
        return rectWidth;
    }
    
    
    
    /**
     * Updates the location of the last note so the animation knows when to stop.
     */
    public void updateLastNote() {
        if (x_coord + rectWidth > lastNote) {
            lastNote = x_coord + rectWidth;
        }
    }
    
    /**
     * Gets the gesture's rectangle.
     */
    public Rectangle getRectangle() {
        return gestureRect;
    }
    
    /**
     * Schedules the gesture in the midiplayer, then updates the last note. 
     * Does this by calling schedule for every playable in contents
     */
    public void schedule() {
        contents.forEach((playable) -> {
            playable.schedule();
            playable.updateLastNote();
        });
    }
    
    public int getStartTime() {
        return startTime;
    }
    
    public void setStartTime(int newStartTime) {
//        for (Playable p : contents) {
//            int diff = p.getStartTime() - startTime;
//            p.setStartTime(newStartTime + diff);
//            //System.out.println(p.getStartTime());
//        }
        startTime = newStartTime;
    }
    
    /**
     * Gets the selected state of the gesture.
     */
    public boolean getSelected() {
        return isSelected;
    }
    
    /**
     * Sets the selected state of the gesture, including setting every playable 
     * in contents to be selected.
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
        
        if (isSelected) {
        	gestureRect.getStyleClass().clear();
        	gestureRect.getStyleClass().addAll("selected", "gesture");
        } else {
        	gestureRect.getStyleClass().clear();
        	gestureRect.getStyleClass().addAll("unselected-grouped", "gesture");
        }
        
        for (Playable playable : contents) {
        	playable.setSelected(isSelected);
        }
    }
    
    public int getVolume(){
        return 127; 
    }
    
    
    public void setVolume(int newVolume){
        for (Playable playable : contents) {
        	playable.setVolume(newVolume);
        }
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
        y_coord = y - (y % GRIDHEIGHT);
        
        gestureRect.setX(x_coord);
        gestureRect.setY(y_coord);   
    }
    
    public void move(double XDistance, double YDistance){
        double x = x_coord + XDistance; 
        double y = y_coord +YDistance; 
      
        
        x_coord = x;
        y_coord = y - (y % GRIDHEIGHT);
        //y_coord = (long) ((y+5)/10)*10; 
        
        gestureRect.setX(x_coord); 
        gestureRect.setY(y_coord); 
        
    }
    
    public void unmove(double finalXLocation, double finalYLocation){
        
    }
    
    public void stretch(double xDistance){
        
    }
    
    /**
     * Gets the collection of the contents.
     */
    public Collection<Playable> getContents() {return contents;}
    
    /**
     * Returns true. This allows us to call isGesture() on any Playable to 
     * determine its identity.
     * @return true
     */
    public boolean isGesture(){return true; }
    
    /**
     * Returns false. Eventually may be implemented to allow for editing the 
     * lengths of gestures.
     * @param event, the mouseClickEvent
     * @return false because it isn't implemented yet
     */
    public boolean inLastFive(MouseEvent event) { return false; }
    
    /**
     * Does nothing. Eventually may be implemented to allow for editing the 
     * lengths of gestures.
     * @param event, the mouseClickEvent
     */
    public void setMovingDuration(MouseEvent event) {}
    
    /**
     * Does nothing. Eventually may be implemented to allow for editing the 
     * lengths of gestures.
     * @param event, the mouseClickEvent
     */
    public void moveDuration(MouseEvent event) {}
    
    /**
     * Does nothing. Eventually may be implemented to allow for editing the 
     * lengths of gestures.
     * @param event, the mouseClickEvent
     */
    public void stopDuration(MouseEvent event) {}
    
    public String toString(){
        String playableString = new String(); 
        for (Playable p: contents){
            playableString = playableString + p.toString() + "\n"; 
        }
        return ("<gesture isSelected=\"" + Boolean.toString(isSelected) + "\"> \n"+ playableString + "</gesture> \n"); 
    }
    
    public Instrument getInstrument(){
        return Instrument.PIANO; 
    }
    
    public void setInstrument(String newInstrument){
        for (Playable p:contents){
            p.setInstrument(newInstrument);
        }
    }
    
    @Override
    public List<Rectangle> getNodeList(){
        List<Rectangle> gestureList = new ArrayList<>();
        contents.forEach((element)->{
            gestureList.addAll(element.getNodeList());
        });
        gestureList.add(gestureRect);
        return gestureList;
    }
    
}
