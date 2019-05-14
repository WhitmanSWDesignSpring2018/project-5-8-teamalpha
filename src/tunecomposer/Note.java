/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;


/**
 * Note class creates a Rectangle representing the note to be played
 * @author Ian Hawkins, Madi Crowley, Ian Stewart, Melissa Kohl
 * @author Abbey Felley, Colin Aslett, Angie Mead, Kimberly Taylor
 */
public class Note implements Playable {
    /**
     * Constants for playing note in MidiPlayer
     */
    private static final int MAX_PITCH = 128;
    //private static final int DEFAULT_DURATION = 100;
    private static final int TRACK = 0;
    
    /**
     * Constants for Rectangle in composition panel
     */
    private static final int RECTHEIGHT = 10;
    private static final int MARGIN = 5;
    
    /**
     * End time for MidiPlayer
     */
    public static double lastNote = 0;
    
    /**
     * Note fields for creating rectangle and playing note
     */
    private final Group rectGroup;
    private final Rectangle noteRect;
    private final Rectangle borderRect;
    private double x_coord;
    private double y_coord;
    private double rectWidth;
    private int startTime;
    private int pitch;
    private Instrument instrument;
    private int volume = 127;
    
    /**
     * Offsets for dragging Rectangle
     */
    private double xOffset;
    private double yOffset;
    private double widthOffset;
    
    /**
     * Is this note currently selected
     */
    private boolean isSelected;

    /**
     * Creates new selected Rectangle at given coordinates with a default width
     * of 100 pixels and creates a note of the given instrument at the 
     * calculated start time and pitch.
     * @param x x-coordinate of new rectangle and starting time for note
     * @param y y-coordinate of new rectangle and pitch of note
     * @param inst instrument that the note should be played
     */
    public Note(double x, double y, Instrument inst, double width, int vol) {
        startTime = (int) x;
        pitch = MAX_PITCH - (int) y / RECTHEIGHT;
        
        x_coord = x;
        y_coord = y - ( y % RECTHEIGHT);
        
        instrument = inst;
        rectWidth = width;
        
        noteRect = new Rectangle(x_coord, y_coord, rectWidth, RECTHEIGHT);
        noteRect.getStyleClass().add(instrument.toString());
        noteRect.setMouseTransparent(false);
        
        borderRect = new Rectangle(x_coord, y_coord, rectWidth, RECTHEIGHT);
        borderRect.getStyleClass().addAll("selected", "border-rect");
        borderRect.setMouseTransparent(true);
        
        rectGroup = new Group(noteRect, borderRect);
        
        isSelected = true;
        volume = vol;
    }
    
    /**
     * Gets the width of the note rectangle.
     * @return rectWidth, the width of the note rectangle.
     */
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
     * Get this Note's Rectangle object
     * @return this Note's Rectangle
     */
    public Rectangle getRectangle() {
        return noteRect;
    }
    
    public Group getRectangles() {
        return rectGroup;
    }
    
    /**
     * Adds this Note to the MidiPlayer
     */
    public void schedule() {
        TuneComposer.PLAYER.addNote(pitch, volume, startTime, (int)rectWidth, 
                                    instrument.ordinal(), TRACK);
        noteRect.setVisible(true);
    }
    
    public int getStartTime() {
        return startTime;
    }
    
    public void setStartTime(int newStartTime) {
        startTime = newStartTime;
    }
    
    /**
     * Checks if this note is selected
     * @return true if note is selected, false otherwise
     */
    public boolean getSelected() {
        return isSelected;
    }
    
    /**
     * Set the note to be selected or unselected and updates the style class
     * of the Rectangle
     * @param selected boolean to set selected to
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            borderRect.getStyleClass().clear();
            borderRect.getStyleClass().addAll("selected", "border-rect");
        } else {
            borderRect.getStyleClass().clear();
            borderRect.getStyleClass().addAll("unselected", "border-rect");
        }
    }
    
    /**
     * Gets the note's volume.
     * @return volume
     */
    public int getVolume(){
        return volume; 
    }
    
    /**
     * Sets the note's volume.
     * @param newVolume, the intended volume, 0-127
     */
    public void setVolume(int newVolume){
        volume = newVolume; 
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
        noteRect.setX(event.getX() - xOffset);
        noteRect.setY(event.getY() - yOffset);
        
        borderRect.setX(event.getX() - xOffset);
        borderRect.setY(event.getY() - yOffset);
    }
    
    /**
     * When the user stops dragging the mouse, set Note fields to the
     * Rectangle's current location
     * @param event mouse click
     */
    public void stopMoving(MouseEvent event) {
        double x = event.getX() - xOffset;
        double y = event.getY() - yOffset;
     
    }
    
    public void move(double XDistance, double YDistance){
        double x = x_coord + XDistance; 
        double y = y_coord +YDistance; 
        startTime = (int) x;
        pitch = MAX_PITCH - (int) y / RECTHEIGHT;
        
        x_coord = x;
        y_coord = y - (y % RECTHEIGHT);
        //y_coord = (long) ((y+5)/10)*10; 
        
        noteRect.setX(x_coord); 
        noteRect.setY(y_coord); 
        
        borderRect.setX(x_coord); 
        borderRect.setY(y_coord); 
      
    }
    
    public void unmove(double XDistance, double YDistance){
        double x = x_coord + XDistance; 
        double y = y_coord +YDistance; 
        startTime = (int) x;
        pitch = MAX_PITCH - (int) y / RECTHEIGHT;
        
        x_coord = x;
        y_coord = y + (y % RECTHEIGHT);
        //y_coord = (long) ((y+5)/10)*10; 
        
        noteRect.setX(x_coord); 
        noteRect.setY(y_coord); 
        
        borderRect.setX(x_coord); 
        borderRect.setY(y_coord); 
    }
    
    public void stretch(double xDistance){
        rectWidth = rectWidth + xDistance + widthOffset;
        if (rectWidth < MARGIN) rectWidth = MARGIN;
        
        noteRect.setWidth(rectWidth);
        borderRect.setWidth(rectWidth);
    }
    

    
    /**
     * Check whether the user has clicked within the last 5 pixels
     * of the Rectangle
     * @param event mouse click
     * @return true if mouse is within the last 5 pixels of the Rectangle
     */
    public boolean inLastFive(MouseEvent event) {
        return (event.getX() > x_coord + rectWidth - MARGIN);
    }
    
    /**
     * When the user clicks near the right end of the Rectangle, calculate 
     * the offset between the right edge of the Rectangle and where the mouse 
     * is in the Rectangle 
     * @param event mouse click
     */
    public void setMovingDuration(MouseEvent event) {
        widthOffset = x_coord + rectWidth - event.getX();
    }
    
    /**
     * While the user is dragging the mouse, change the width of the Rectangle
     * @param event mouse drag
     */
    public void moveDuration(MouseEvent event) {
        double tempWidth = event.getX() - x_coord + widthOffset;
        if (tempWidth < 5) tempWidth = 5;
        noteRect.setWidth(tempWidth);
        borderRect.setWidth(tempWidth);
    }
    
    /**
     * When the user stops dragging the mouse, set Rectangle width
     * to final width
     * @param event 
     */
    public void stopDuration(MouseEvent event) {
        rectWidth = event.getX() - x_coord + widthOffset;
        if (rectWidth < MARGIN) rectWidth = MARGIN;
        
        noteRect.setWidth(rectWidth);
        borderRect.setWidth(rectWidth);
    }
    
    /**
     * Returns false. This allows us to call isGesture() on any Playable to 
     * determine its identity.
     * @return false
     */
    public boolean isGesture() {return false;}
    
    /**
     * Returns an empty HashSet. This method is mostly written so that we can 
     * retrieve the contents of a Gesture but call it on any Playable.
     * @return an empty HashSet
     */
    public Collection<Playable> getContents() {return new HashSet();}
    
    /**
     * Converts the note to an xml-format string.
     * @return, the note as a string
     */
    public String toString(){
        return "<note x=\"" + Double.toString(x_coord)+ "\" y=\"" + Double.toString(y_coord) 
                + "\" instrument=\"" + instrument +"\" width=\""+ 
                Double.toString(rectWidth)+ "\" isSelected=\""+ Boolean.toString(isSelected)+ "\" volume=\""
                + Integer.toString(volume)+ "\"/> \n";
    }
    
    /**
     * Gets the note's instrument.
     * @return the instrument
     */
    public Instrument getInstrument(){
        return instrument; 
    }
    
    /**
     * Sets the instrument and changes the style class of the note rectangle.
     * @param newInstrument the intended instrument
     */
    public void setInstrument(String newInstrument){
        noteRect.getStyleClass().remove(instrument.toString());
        instrument = instrument.toInstrument(newInstrument); 
        noteRect.getStyleClass().add(instrument.toString());
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<Rectangle> getNodeList(){
        List<Rectangle> rectList = new ArrayList<Rectangle>();
        rectList.add(this.getRectangle());
        rectList.add(borderRect);
        return rectList;
    }
    
    /**
     * Changes the opacity of the background rectangle.
     * @param opacity the desired opacity, 0-1
     */
    @Override
    public void setOpacity(double opacity) {
        noteRect.setOpacity(opacity);
    }
}
