/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.Collection;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Provides a common interface for note and gesture.
 * @author Abbey Felley, Colin Aslett, Angie Mead, Kimberly Taylor
 */
public interface Playable {
    
    double getWidth();
    
    void updateLastNote();
    
    Rectangle getRectangle();
    
    void schedule();
    
    boolean getSelected();
    
    void setSelected(boolean selected);
    
    void setMovingCoords(MouseEvent event);
    
    void moveRect(MouseEvent event);
    
    void stopMoving(MouseEvent event);
    
    boolean inLastFive(MouseEvent event);
    
    void setMovingDuration(MouseEvent event);
    
    void moveDuration(MouseEvent event);
    
    void stopDuration(MouseEvent event);
    
    boolean isGesture(); 
    
    Collection getContents();

}
