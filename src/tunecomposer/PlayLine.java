/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * Creates and manages the moving red line that moves as the composition plays
 * @author Nathaniel Larson
 * @author Ian Stewart, Melissa Kohl
 */
public class PlayLine {
    
    /**
     * Fields for the PlayLine class
     */
    private final Line movingLine;
    private Timeline timeline;
    
    /**
     * Check if TuneComposer is currently playing
     */
    private boolean playing;
    
    /**
     * Initializes a new PlayLine object in the newPane
     * @param line a layout pane, now implemented with AnchorPane
     */
    public PlayLine(Line line) {
        
        // initialize Timeline
        timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        
        // initialize the red line
        movingLine = line;
        movingLine.setVisible(false);
    }
    
    /**
     * Make a red line track across the composition at constant speed
     * @param endXCoordinate the x coordinate of the final note of 
     *                       the composition
     */
    public void play(double startXCoordinate,double endXCoordinate) {
        timeline.getKeyFrames().clear();
        movingLine.setEndX(startXCoordinate); // place playLine back at the beginning 
        movingLine.setStartX(startXCoordinate);
        movingLine.setVisible(true);
        
        timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        KeyValue keyValueXEnd = new KeyValue(movingLine.endXProperty(), 
                                             endXCoordinate);
        KeyValue keyValueXStart = new KeyValue(movingLine.startXProperty(), 
                                               endXCoordinate);
        
        // duration calculated for constant speed of 100 pixels per second
        Duration duration = Duration.millis((endXCoordinate-startXCoordinate)*10); 
        
        // when finsihed, playLine will disappear
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent t) -> 
        {
            movingLine.setVisible(false);
            playing = false;
        };
 
        KeyFrame keyFrameEnd = new KeyFrame(duration, onFinished, keyValueXEnd);
        KeyFrame keyFrameStart = new KeyFrame(duration, onFinished, 
                                              keyValueXStart);
        timeline.getKeyFrames().addAll(keyFrameEnd, keyFrameStart);
        timeline.play();
        playing = true;
    }
    
    /**
     * Gets the current state of the PlayLine
     * @return Boolean indicating PlayLine status
     */
    public boolean isPlaying() {
        return playing;
    }
    
    /**
     * Stop the red line and make it disappear
     */
    public void stop() {
        timeline.stop();
        movingLine.setVisible(false);
        playing = false;
    } 
    
}

