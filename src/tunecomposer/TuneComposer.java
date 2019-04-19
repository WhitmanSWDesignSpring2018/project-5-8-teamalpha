/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.io.IOException;
import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.ShortMessage;

/**
 * This JavaFX app lets the user play scales.
 * @author Ian Stewart, Ian Hawkins, Angie Mead, Melissa Kohl + teamalpha
 * @author Abbey Felley, Colin Aslett, Angie Mead, Kimberly Taylor
 */
public class TuneComposer extends Application {

    /**
     * A MidiPlayer for all notes to use.
     */
    public static final MidiPlayer PLAYER = new MidiPlayer(100,60);
    
    /**
     * A CommandManager to manage the stacks of actions.
     */
    public static CommandManager commandManager = new CommandManager();
    
    /**
     * The set of all notes, to be played later.
     */    
    public static Set<Playable> allPlayables;
    
    /**
     * A list of instrument values to associate with MidiPlayer channels.
     */
    private final int[] timbreList = new int[] {0, 6, 12, 19, 21, 24, 40, 60};
    
    /**
     * The background of the application.
     */
    @FXML
    private Group background;

    /**
     * The pane in which notes are constructed.
     */
    @FXML
    public Pane notePane;

    /**
     * An area for click-and-drag note selection.
     */
    private SelectionArea selection;

    /**
     * Rectangle used in click-and-drag note selection.
     */
    @FXML
    private Rectangle selectRect;
            
    /**
     * A group of sidebar radio buttons for selecting an instrument.
     */
    @FXML
    private ToggleGroup instrumentToggle;
    
    public PlayLine playLine;
    
    /**
     * The line wrapped by PlayLine.
     */
    @FXML
    private Line movingLine;
    
    /**
     * The pane in which the play line is constructed and plays.
     */
    @FXML
     AnchorPane playLinePane;
    
    /**
     * Plays notes that have been added.
     * Called when the Play button is clicked.
     */
    
    /**
     * Boolean flags to control flow when user clicks in composition panel.
     */
    private boolean clickInPane = true;
    private boolean changeDuration = false;
    private boolean isDragSelecting = false;

    /**
     * Constructor initializes the set of allPlayables.
     */
    public TuneComposer() {
        allPlayables = new HashSet();
    }

    /**
     * Add the given note to the set of all notes, to be played later.
     * @param note note added to composition
     */
    public static void addNote(Note note) {
        allPlayables.add(note);
    }
    
    /**
     * Get the instrument currently selected in the sidebar.
     * @return the selected instrument
     */
    public Instrument getInstrument() {
        RadioButton selectedButton = (RadioButton)instrumentToggle.getSelectedToggle();
        String instrument = selectedButton.getText();
        switch(instrument) {
            case "Piano":           return Instrument.PIANO;
            case "Harpsichord":     return Instrument.HARPSICHORD;
            case "Marimba":         return Instrument.MARIMBA;
            case "Church Organ":    return Instrument.CHURCH_ORGAN;
            case "Accordion":       return Instrument.ACCORDION;
            case "Guitar":          return Instrument.GUITAR;
            case "Violin":          return Instrument.VIOLIN;
            case "French Horn":     return Instrument.FRENCH_HORN;
            default:
                throw new IllegalArgumentException("Unrecognized Instrument");
        }
    }

    /**
     * When user presses on a Playable, set the Playables to be selected or 
     * unselected accordingly.
     * @param event mouse click
     * @param playable note Rectangle that was clicked
     */
    private void handleNoteClick(MouseEvent event, Playable playable) {
        clickInPane = false;
        boolean control = event.isControlDown();
        boolean selected = playable.getSelected();
        if (! control && ! selected) {
            selectAll(false);
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
    @FXML
    private void handleClick(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
        }
        else if (isDragSelecting){
            isDragSelecting = false;
            selection.endRectangle();
        }
        else if (clickInPane) {
        	//System.out.println("else 2");
            if (!event.isControlDown()) {
                selectAll(false);
            }
            
            Instrument instrument = getInstrument();
            Note note = new Note(event.getX(), event.getY(), instrument);
            AddNoteCommand noteaction = new AddNoteCommand(note);
            
            commandManager.add(noteaction);
            //System.out.println(commandManager.undoSize());
            
            noteaction.redo();
            notePane.getChildren().add(note.getRectangle());
            
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
    private void handleNotePress(MouseEvent event, Playable playable) {
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
    private void handleNoteDrag(MouseEvent event) {
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
    private void handleNoteStopDragging(MouseEvent event) {
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
    @FXML
    private void startDrag(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
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
    @FXML
    private void continueDrag(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
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
            selectAll(false);
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
    
    /**
     * Plays the Playables in the composition. Stops and clears the MidiPlayer
     * and schedules all the Playables in allPlayables.
     */
    private void startPlaying() {
        PLAYER.stop();
        PLAYER.clear();
        for(int i=0; i<8; i++){
            PLAYER.addMidiEvent(ShortMessage.PROGRAM_CHANGE + i, timbreList[i], 0, 0, 0);
        }
        allPlayables.forEach((playable) -> {
            playable.schedule();
            playable.updateLastNote();
        });

        PLAYER.play();
        playLine.play(Note.lastNote);
    }

    /**
     * When user selects "Start" menu item, start playing composition
     * @param ignored not used
     */
    @FXML
    private void handleStartPlaying(ActionEvent ignored) {
        startPlaying();
    }

    /**
     * Stops playing composition.
     * Called when the Stop button is clicked. Does not remove notes from the
     * screen or from allPlayables.
     */
    private void stopPlaying() {
        PLAYER.stop();
        playLine.stop();
    }

    /**
     * When the user selects "Stop" menu item, stop playing composition
     * @param ignored not used
     */
    @FXML
    protected void handleStopPlaying(ActionEvent ignored) {
        stopPlaying();
    }

    /**
     * When the user selects the "Exit" menu item, exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }
    
    /**
     * When the user selects the "Delete Selected" menu item, delete the selected
     * Playables.
     * @param event the menu selection event
     */
    @FXML
    void handleDelete(ActionEvent event) {
        Collection toDelete = new ArrayList();
        allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                toDelete.add(playable);
            }
        });
        DeleteCommand deletecommand = new DeleteCommand(toDelete);
        deletecommand.redo();
        TuneComposer.commandManager.add(deletecommand);
    }
    
    /**
     * Select all notes. Called from FXML
     * @param event unused
     */
    @FXML
    void handleSelectAll(ActionEvent event) {
        selectAll(true);
    }
    
    /**
     * Sets selection values for all of the notes
     * @param selected true to select all
     */
    private void selectAll(boolean selected) {
        allPlayables.forEach((playable) -> {
            playable.setSelected(selected);
        });
    }
    
    /**
     * Makes a new gesture from the selected Playables. Adds the rectangle to 
     * the NotePane and sets the event handlers.
     */
    public void makeGroup() {
        HashSet<Playable> selectedPlayables = new HashSet<Playable>();
    	allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                selectedPlayables.add(n);
            }
        });
    	
        Gesture group = new Gesture(selectedPlayables);
        selectedPlayables.add(group); 
        notePane.getChildren().add(group.getRectangle());
        GroupCommand groupcommand = new GroupCommand(group);
        commandManager.add(groupcommand);
        groupcommand.redo();
        group.getRectangle().setOnMousePressed((MouseEvent pressedEvent) -> {
            handleNoteClick(pressedEvent, group);
            handleNotePress(pressedEvent, group);
        });
        
        group.getRectangle().setOnMouseDragged((MouseEvent dragEvent) -> {
            handleNoteDrag(dragEvent);
        });
        
        group.getRectangle().setOnMouseReleased((MouseEvent releaseEvent) -> {
            handleNoteStopDragging(releaseEvent);
        });
    }
    
    /**
     * Gets rid of all the top-level selected groups. Any nested or unselected
     * gestures remain.
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
    		notePane.getChildren().remove(g.getRectangle());
    		allPlayables.remove(g);
    	}
    }
    
    /**
     * Calls makeGroup to handle the group menu event.
     * @param event unused
     */
    @FXML
    private void handleGroup(ActionEvent event) {
        makeGroup();
    }
    
    /**
     * Calls unGroup to handle the ungroup menu event.
     * @param event unused
     */
    @FXML
    private void handleUnGroup(ActionEvent event) {
        unGroup();
    }
    
    /**
     * Handles the redo menu event by calling the method in CommandManager.
     * @param event unused
     */
    @FXML
    private void handleRedo(ActionEvent event){
        commandManager.redo();
    }
    
    /**
     * Handles the undo menu event by calling the method in CommandManager.
     */
    @FXML
    private void handleUndo(ActionEvent event){
        commandManager.undo();
    }
    
    /**
     * Initializes FXML. Called automatically.
     * (1) adds 127 gray lines to background
     * (2) initializes the playLine(set to invisible)
     */
    public void initialize() {
         playLinePane.setMouseTransparent(true);
         playLine = new PlayLine(movingLine);
         
        // Add gray lines to background
        for (int i = 1; i < 128; i++) {
            Line row = new Line(0, 10 * i, 2000, 10 * i);
            row.getStyleClass().add("row-divider");
            background.getChildren().add(row);
        }

        selection = new SelectionArea(selectRect);
    }
    
    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TuneComposer.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Scale Player");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });
        
        primaryStage.show();
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
}
