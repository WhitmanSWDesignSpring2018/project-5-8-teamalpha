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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.ShortMessage;
import org.xml.sax.SAXException;


//https://code.makery.ch/blog/javafx-dialogs-official/

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
    
    
    public MouseEvent dragStart; 
    
    public Collection<Playable> saveSelected = new ArrayList<Playable>(); 
    
    public ClipboardWrapper clipboardWrapper = new ClipboardWrapper();
    
    FileSaver filesaver = new FileSaver();  
    
    XMLParser xmlparser = new XMLParser(); 
    
    
    /**
     * Plays notes that have been added.
     * Called when the Play button is clicked.
     */
    //buttons
    @FXML
    private MenuItem deleteAction;
    @FXML
    private MenuItem selectAllAction;
    @FXML
    private MenuItem groupAction;
    @FXML
    private MenuItem ungroupAction;
    @FXML
    private MenuItem redoAction; 
    @FXML
    private MenuItem undoAction;
    @FXML 
    private MenuItem playAction; 
    @FXML
    private MenuItem stopAction; 
    @FXML
    private MenuItem openAction; 
    @FXML
    private MenuItem saveAsAction; 
    
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
        
        Collection<Playable> currentlySelected = getSelectedPlayables(); 
        Collection<Playable> selectedPlayable = new ArrayList();
        
        if (! control && ! selected) {
            selectAll(false);
            selectedPlayable.add(playable); 
            
            SelectCommand selectcommand = new SelectCommand(selectedPlayable, currentlySelected, true,false);
            commandManager.add(selectcommand);
            selectcommand.execute(); 
    

            //playable.setSelected(true);
        } else if (control && ! selected) {
            //playable.setSelected(true);
            selectedPlayable.add(playable); 
            
            SelectCommand selectcommand = new SelectCommand(selectedPlayable, currentlySelected, true, true);
            commandManager.add(selectcommand); 
            selectcommand.execute(); 
       
  
        } else if (control && selected) {
            //playable.setSelected(false);
            selectedPlayable.add(playable); 
            
            SelectCommand selectcommand = new SelectCommand(selectedPlayable, currentlySelected, false, true); 
            commandManager.add(selectcommand);
            selectcommand.execute(); 
        }
        
        saveSelected = getSelectedPlayables(); 
        
    }
    
    /**
     * Construct a note from a click. Called via FXML.
     * @param event a mouse click
     */
    @FXML
    private void handleClick(MouseEvent event) {
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        
        if (playLine.isPlaying()) {
            stopPlaying();
        }
        else if (isDragSelecting){
            isDragSelecting = false;
            selection.endRectangle();
            
            SelectCommand selectcommand = new SelectCommand(selectedPlayables, saveSelected,true,false); 
            commandManager.add(selectcommand);
            selectcommand.execute(); 
            
        }
        else if (clickInPane) {
            playAction.setDisable(false); 
            if (!event.isControlDown()) {
                selectAll(false);
            }
            
            Instrument instrument = getInstrument();
            
            Note note = new Note(event.getX(), event.getY(), instrument, 100);
            AddNoteCommand noteaction = new AddNoteCommand(note,selectedPlayables);
            
            
            commandManager.add(noteaction);
            undoAction.setDisable(false);
            redoAction.setDisable(true); 
            
            noteaction.execute();
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
        deleteAction.setDisable(false);
        selectAllAction.setDisable(false);
        
        if(selectedPlayables.size() > 1){
            groupAction.setDisable(false);
        }
        
        saveSelected = getSelectedPlayables();
        System.out.println(saveSelected.size());
    }
    
    public Collection<Playable> getSelectedPlayables(){
        HashSet<Playable> selectedPlayables = new HashSet<Playable>();
    	allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                selectedPlayables.add(n);
            }
        });
        return selectedPlayables; 
    }
  
    /**
     * When user presses on a note, set offsets in each Note in case the user
     * drags the mouse.
     * @param event mouse click
     * @param note note Rectangle that was clicked
     */
    private void handleNotePress(MouseEvent event, Playable playable) {
        dragStart = event; 
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
     * When the user stops dragging the mouse, stops moving the selected notes
     * @param event mouse click
     */
    private void handleNoteStopDragging(MouseEvent event) {
        clickInPane = false;
        Collection<Playable> movedPlayables = new ArrayList(); 
        Collection<Playable> stretchedPlayables = new ArrayList(); 
        allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    //n.stopDuration(event);
                    stretchedPlayables.add(n); 
                }
                else if ((event.getX()-dragStart.getX() != 0) && (event.getY()-dragStart.getY() != 0)){
                    movedPlayables.add(n); 
                }
            }
        });
        
        if (!movedPlayables.isEmpty()) {
            MoveCommand newcommand = new MoveCommand(movedPlayables, dragStart, event); 
            commandManager.add(newcommand); 
            newcommand.execute(); 
        }
        
        if (!stretchedPlayables.isEmpty()) {
            StretchCommand stretchcommand = new StretchCommand(stretchedPlayables, dragStart, event); 
            commandManager.add(stretchcommand); 
            stretchcommand.execute(); 
        }
        
        changeDuration = false;
        
        undoAction.setDisable(false); 
        redoAction.setDisable(true); 
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition
     * is playing, and starts dragging selection rectangle if mouse click is
     * not on a note Rectangle.
     * @param event mouse click
     */
    @FXML
    private void startDrag(MouseEvent event) {
        dragStart = event; 
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
                //toSelect.add(note);
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
        
        stopAction.setDisable(false); 
        playAction.setDisable(true); 
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
        
        stopAction.setDisable(true); 
        playAction.setDisable(false); 
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
        deletecommand.execute();
        TuneComposer.commandManager.add(deletecommand);
        undoAction.setDisable(false); 
        redoAction.setDisable(true); 
        if(allPlayables.isEmpty()){
            deleteAction.setDisable(true);
            selectAllAction.setDisable(true);
        }
    }
    
    /**
     * Select all notes. Called from FXML
     * @param event unused
     */
    @FXML
    void handleSelectAll(ActionEvent event) {
        Collection<Playable> currentlySelected = getSelectedPlayables(); 
        selectAll(true);
        
        Collection toSelect = new ArrayList();
        allPlayables.forEach((playable)-> {
           toSelect.add(playable); 
        });
        
        SelectCommand select = new SelectCommand(toSelect, currentlySelected, true, false);
        
        commandManager.add(select);
        select.execute();
        
        undoAction.setDisable(false);
        redoAction.setDisable(true); 
        
        saveSelected = getSelectedPlayables();
       
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
        redoAction.setDisable(true); 
        undoAction.setDisable(false); 
        groupcommand.execute();
        
        group.getRectangle().setOnMousePressed((MouseEvent pressedEvent) -> {
            handleNoteClick(pressedEvent,group);
            handleNotePress(pressedEvent,group);
        });
        
        group.getRectangle().setOnMouseDragged((MouseEvent dragEvent) -> {
            handleNoteDrag(dragEvent);
        });
        
        group.getRectangle().setOnMouseReleased((MouseEvent releaseEvent) -> {
            handleNoteStopDragging(releaseEvent);
        });
        
        groupAction.setDisable(true);
        ungroupAction.setDisable(false);
    }
    
    /**
     * Gets rid of all the top-level selected groups. Any nested or unselected
     * gestures remain.
     */
    public void unGroup() {
    	ArrayList<Playable> topLevelGestures = getTopLevelGestures();
    	for (Playable g : topLevelGestures) {
    		notePane.getChildren().remove(g.getRectangle());
    		allPlayables.remove(g);
    	}
        topLevelGestures = getTopLevelGestures(); 
        if(topLevelGestures.size() == 0){
            ungroupAction.setDisable(true);
        }
        groupAction.setDisable(false);
    }
    
    
     /**
     * Returns gestures that are not contained in any other gestures
     */
    public ArrayList<Playable> getTopLevelGestures(){
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
        return topLevelGestures; 
    }
    
    /**
     * Returns playables that are not contained in any playables
     */
    public ArrayList<Playable> getTopLevelPlayables(){
    	ArrayList<Playable> topLevelPlayables = new ArrayList<Playable>();
    	boolean isIn;
    	for (Playable g : allPlayables) {
    		isIn = false;
    		for (Playable h : allPlayables) {
    			if (h.getContents().contains(g)) {
    				isIn = true;
    			}
    		}
    		if (!isIn) {
    			topLevelPlayables.add(g);
    		}
    	}
        return topLevelPlayables; 
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
        if (commandManager.getRedoStackSize()< 1){
            redoAction.setDisable(true); 
        } 
        undoAction.setDisable(false); 
        playAction.setDisable(false); 
        selectAllAction.setDisable(false); 
        deleteAction.setDisable(false); 
        saveSelected = getSelectedPlayables();
    }
    
    /**
     * Handles the undo menu event by calling the method in CommandManager.
     */
    @FXML
    private void handleUndo(ActionEvent event){
        commandManager.undo();
        if (commandManager.getUndoStackSize()< 1){
            undoAction.setDisable(true); 
            deleteAction.setDisable(true); 
            playAction.setDisable(true); 
            selectAllAction.setDisable(true); 
            
        }
        redoAction.setDisable(false); 
        saveSelected = getSelectedPlayables();
        System.out.println(saveSelected.size()); 
    }
    
    @FXML
    private void handleCopy(ActionEvent event){
        copy(); 
    }
    
    private void copy(){
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        clipboardWrapper.addToClipboard(selectedPlayables); 
    }
    
    @FXML
    private void handleCut(ActionEvent event){
        cut(); 
    }
    
    private void cut(){
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        clipboardWrapper.addToClipboard(selectedPlayables);
        for (Playable p : selectedPlayables) {
            p.getRectangle().setVisible(false);
        }
        allPlayables.removeAll(selectedPlayables); //modify this for undoable 
    }
    
    @FXML
    private void handlePaste(ActionEvent event){
        paste(); 
    }
    
    private void paste(){
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
    }
    
    
    @FXML
    private void handleAboutAction(ActionEvent event){
        Alert aboutBox = new Alert(Alert.AlertType.INFORMATION);
        aboutBox.setTitle("About TuneComposer");
        aboutBox.setHeaderText(null);
        aboutBox.setContentText("Welcome to TuneComposer!\nCreated by Angie,Abbey,Colin & Kimberly\nHave fun composing");
        aboutBox.showAndWait();
    }
    
    
    
    
    
    @FXML
    private void handleNewAction(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New");
        alert.setHeaderText("Do you want to save the composition before making a new one?");
        alert.setContentText("Choose your option:");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // ... user chose "One"
        } else if (result.get() == buttonTypeTwo) {
            // ... user chose "Two"
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
    
    @FXML
    private void handleOpenAction(ActionEvent event) throws SAXException, IOException{
        playAction.setDisable(false); 
        Collection<Playable> loadedPlayables = xmlparser.loadFile(); 
        System.out.println(loadedPlayables.size()); 
        for (Playable p : loadedPlayables){
            notePane.getChildren().add(p.getRectangle()); 
            allPlayables.add(p); 
        }
    }
    
    @FXML
    private void handleSaveAction(ActionEvent event){
        //only appear when it is the first time saving so we need to add an if 
        TextInputDialog dialog = new TextInputDialog("filename.txt");
        dialog.setTitle("Save As");
        dialog.setHeaderText("Save your composition!");
        dialog.setContentText("Filename:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait(); 
        
        Collection<Playable> topLevelPlayables = getTopLevelPlayables(); 
        String noteString = ""; 
        
        for (Playable p : topLevelPlayables) {
            noteString = noteString + p.toString() + "\n";
        }
        noteString = "<composition> \n" + noteString + "</composition>"; 
        filesaver.newFile(noteString);
    }
    
    @FXML
    private void handleSaveAsAction(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog("filename.txt");
        dialog.setTitle("Save As");
        dialog.setHeaderText("Save your composition!");
        dialog.setContentText("Filename:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait(); 
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
        deleteAction.setDisable(true);
        selectAllAction.setDisable(true);
        groupAction.setDisable(true);
        ungroupAction.setDisable(true);
        undoAction.setDisable(true); 
        redoAction.setDisable(true); 
        playAction.setDisable(true); 
        stopAction.setDisable(true); 
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
