/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.io.File;
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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.ShortMessage;
import org.xml.sax.SAXException;

//website where we got information about different types of dialog boxes 
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
    
    /**
    * The pane in which the note names are constructed.
    */
    @FXML
    AnchorPane noteNamesPane; 
    
    /**
     * MouseEvent used to calculate move distances.
     */
    public MouseEvent dragStart; 
    
    /**
     * Collection of playables used for undoing selection events.
     */
    public Collection<Playable> saveSelected = new ArrayList<>(); 
    
    /**
     * The object used for copy, cut, and paste.
     */
    public ClipboardWrapper clipboardWrapper = new ClipboardWrapper();
    
    /**
     * The object that facilitates saving files.
     */
    public FileSaver filesaver = new FileSaver();  
    
    /**
     * Parses saved xml files into compositions.
     */
    public XMLParser xmlparser = new XMLParser(); 
    
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
    @FXML
    private MenuItem saveAction; 
    @FXML
    private MenuItem newAction; 
    @FXML
    private MenuItem pasteAction; 
    @FXML
    private MenuItem copyAction; 
    @FXML
    private MenuItem cutAction; 
    
    /**
     * Boolean flags to control flow when user clicks in composition panel.
     */
    private boolean clickInPane = true;
    private boolean changeDuration = false;
    private boolean isDragSelecting = false;
  
    private Stage stage;
    public boolean isSaved = true;
    public String savedFilename; 

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
        Collection<Playable> selectedPlayable = new ArrayList<Playable>();
        
        if (! control && ! selected) {
            selectAll(false);
            selectedPlayable.add(playable); 
            SelectCommand selectcommand = new SelectCommand(selectedPlayable, currentlySelected, true,false);
            commandManager.add(selectcommand);
            selectcommand.execute(); 
        } else if (control && ! selected) {
            selectedPlayable.add(playable); 
            SelectCommand selectcommand = new SelectCommand(selectedPlayable, currentlySelected, true, true);
            commandManager.add(selectcommand); 
            selectcommand.execute(); 
        } else if (control && selected) {
            selectedPlayable.add(playable); 
            SelectCommand selectcommand = new SelectCommand(selectedPlayable, currentlySelected, false, true); 
            commandManager.add(selectcommand);
            selectcommand.execute(); 
        }
        saveSelected = getSelectedPlayables(); 
        isSaved = false;
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
    }
    
    /**
     * Construct a note from a click. Called via FXML.
     * @param event a mouse click
     */
    @FXML
    private void handleClick(MouseEvent event) {
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        
        Collection<Playable> topLevelPlayables = getTopLevelPlayables(); 
        
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
            Note note = new Note(event.getX(), event.getY(), instrument, 100, 127);
            AddNoteCommand noteaction = new AddNoteCommand(note,selectedPlayables);
            commandManager.add(noteaction);
            undoAction.setDisable(false);
            redoAction.setDisable(true); 
            
            noteaction.execute();
            notePane.getChildren().add(note.getRectangles());
            
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
            note.getRectangle().setOnContextMenuRequested((ContextMenuEvent rightClickEvent)->{
                handleRightClick(rightClickEvent, note); 
            });
        }
        
        clickInPane = true;
        deleteAction.setDisable(false);
        selectAllAction.setDisable(false);
        
        groupAction.setDisable(false); 
        
        if(topLevelPlayables.size() <= 1){
            groupAction.setDisable(true);
        }
        saveSelected = getSelectedPlayables();
        isSaved = false;
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
        
        copyAction.setDisable(false); 
        cutAction.setDisable(false); 
    }
    
    /**
     * Creates the context menu on right click. Called on context menu requested 
     * for a given note or gesture.
     * @param rightClick, the ContextMenuEvent
     * @param playable, the owner of the rectangle which triggered the ContextMenuEvent
     */
    public void handleRightClick(ContextMenuEvent rightClick, Playable playable){
        Collection<Playable> selectedPlayables= getSelectedPlayables(); 
        Collection<Playable> topLevelPlayables = getTopLevelPlayables(); 
        
        boolean gesturePresent = false; 
        for (Playable p: selectedPlayables){
            if (p.isGesture()){
                gesturePresent = true; 
            }
        }
        
        ContextMenu contextMenu = new ContextMenu();

        MenuItem changeVolumeMenuItem = new MenuItem("Change volume");
        changeVolumeMenuItem.setOnAction((ActionEvent volumeChangeClick) ->{
            handleVolumeChange(selectedPlayables); 
        }); 
        
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem changeInstrumentMenuItem = new MenuItem("Change instrument"); 
        changeInstrumentMenuItem.setOnAction((ActionEvent instrumentChangeClick) ->{
            handleInstrumentChange(selectedPlayables, playable); 
        }); 
        
        SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();
        MenuItem deleteRightClickMenuItem = new MenuItem("Delete"); 
        deleteRightClickMenuItem.setOnAction((ActionEvent deleteRightClick) ->{
            handleDelete(deleteRightClick); 
        }); 
        
        MenuItem playSelectedMenuItem = new MenuItem("Play selected"); 
        playSelectedMenuItem.setOnAction((ActionEvent playSelectedClick) ->{
            playSelected(); 
        }); 
        
        SeparatorMenuItem separatorMenuItem3 = new SeparatorMenuItem();
        
        MenuItem groupSelectedMenuItem = new MenuItem("Group"); 
        groupSelectedMenuItem.setOnAction((ActionEvent groupSelectedClick) ->{
            makeGroup(); 
        });
        
        MenuItem ungroupSelectedMenuItem = new MenuItem("Ungroup"); 
        ungroupSelectedMenuItem.setOnAction((ActionEvent ungroupSelectedClick) ->{
            unGroup(); 
        });
        
        ungroupSelectedMenuItem.setDisable(!gesturePresent); 
      
        if (selectedPlayables.size() <= 1 ) {
            groupSelectedMenuItem.setDisable(true); 
        }

        if(topLevelPlayables.size() <= 1){
            groupSelectedMenuItem.setDisable(true);
        }
        
        
        contextMenu.getItems().addAll(playSelectedMenuItem,separatorMenuItem,changeVolumeMenuItem,changeInstrumentMenuItem, 
                                      separatorMenuItem2,groupSelectedMenuItem, ungroupSelectedMenuItem,separatorMenuItem3, deleteRightClickMenuItem);
        
        contextMenu.show(playable.getRectangle(),rightClick.getScreenX(),rightClick.getScreenY()); 
    }
    
    /**
     * Handles changing the volume of the selected playables. Called by context 
     * menu action.
     * @param playables, the set of selected playables
     */
    public void handleVolumeChange(Collection<Playable> playables){
        String dialogVolume = "0-127"; 
        //if isolated note selected the volume of that note will appear in dialog box
        if (playables.size() == 1){ 
            dialogVolume = Integer.toString(playables.iterator().next().getVolume()); 
        }
        
        TextInputDialog dialog = new TextInputDialog(dialogVolume);
        dialog.setTitle("Change Volume");
        dialog.setHeaderText("");
        dialog.setContentText("Please enter a number between 0-127:");

        Optional<String> result = dialog.showAndWait();
     
        if (result.isPresent()){
            int volume = Integer.parseInt(result.get());
            for (Playable p : playables){
                p.setVolume(volume); 
                if (! p.isGesture()){
                    p.setOpacity((double)volume/127); 
                }
            }  
        }
    }
    
    /**
     * Handles changing the instrument of the given playables. Creates a dialog that
     * allows you to choose the new instrument from a drop-down menu
     * @param playables, the list of selected playables
     * @param playable, the playable that triggered the context menu event
     */
    public void handleInstrumentChange(Collection<Playable> playables, Playable playable){
        List<String> choices = new ArrayList<>();
        choices.add("piano"); 
        choices.add("harpsichord");
        choices.add("marimba");
        choices.add("church-organ"); 
        choices.add("accordion");
        choices.add("guitar");
        choices.add("violin"); 
        choices.add("french-horn");
            
        Instrument instrument = playable.getInstrument(); 
        ChoiceDialog<String> dialog = new ChoiceDialog<>(instrument.toString(), choices);
        dialog.setTitle("Change Instrument");
        //dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose an instrument:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()){
            String newInstrument = result.get();
            for (Playable p : playables){
                p.setInstrument(newInstrument); 
            }
        }
    }
    
    /**
     * Allows the user to play only the selectedPlayables.
     */
    public void playSelected(){
        Collection<Playable> selectedNotes = new ArrayList<Playable>(); 
        for (Playable p : allPlayables) {
            if (!p.isGesture() && p.getSelected()) {
                selectedNotes.add(p);
            }
        }
        
        PLAYER.stop();
        PLAYER.clear();
        for(int i=0; i<8; i++){
            PLAYER.addMidiEvent(ShortMessage.PROGRAM_CHANGE + i, timbreList[i], 0, 0, 0);
        }
        
        double minX = 2000; 
        double maxX = 0;
        for (Playable p: selectedNotes){
            if (p.getRectangle().getX() < minX){
                minX = p.getRectangle().getX(); 
            }
            if (p.getRectangle().getX() > maxX) {
                maxX = p.getRectangle().getX() + p.getWidth();
            }
        }
        
        // set the start times relative to the beginnning of the selected playables
        // for midiPlayer scheduling
        for (Playable p : selectedNotes) {
            int diff = p.getStartTime() - (int)minX;
            p.setStartTime(diff);
        }
        
        selectedNotes.forEach((playable) -> {
            playable.schedule();
            playable.updateLastNote();
        });

        PLAYER.play();
        playLine.play(minX,maxX);
        
        stopAction.setDisable(false); 
        
        // reset the start times
        for (Playable p : selectedNotes) {
            int diff = p.getStartTime();
            p.setStartTime(diff + (int)minX);
        }
    }
    
    /**
     * Method to get only the currently selected Playables. 
     * @return, the created collection containing all selected playables. 
     */
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
        Collection<Playable> movedPlayables = new ArrayList<Playable>(); 
        Collection<Playable> stretchedPlayables = new ArrayList<Playable>(); 
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
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
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
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
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
        playLine.play(0,Note.lastNote);
        
        stopAction.setDisable(false); 
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to save before exiting?");
      
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){
            if (savedFilename.equals("new")){
                saveAs(); 
                System.exit(0); 
            }
            else{ //file has not been saved before
                save(); 
                System.exit(0); 
            }
        } else if (result.get() == buttonTypeNo) {
            System.exit(0); 
        } else {
            // ... user chose CANCEL or closed the dialog
        } 
    }
    
    /**
     * When the user selects the "Delete Selected" menu item, delete the selected
     * Playables.
     * @param event the menu selection event
     */
    @FXML
    void handleDelete(ActionEvent event) {
        Collection<Playable> toDelete = getSelectedPlayables(); 
        DeleteCommand deletecommand = new DeleteCommand(toDelete);
        deletecommand.execute();
        TuneComposer.commandManager.add(deletecommand);
        undoAction.setDisable(false); 
        redoAction.setDisable(true); 
        
        deleteAction.setDisable(true);
        
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
        
        groupAction.setDisable(true); 
        ungroupAction.setDisable(true); 
        
        cutAction.setDisable(true); 
        copyAction.setDisable(true); 
    }
    
    /**
     * Select all notes. Called from FXML
     * @param event unused
     */
    @FXML
    void handleSelectAll(ActionEvent event) {
        Collection<Playable> currentlySelected = getSelectedPlayables(); 
        selectAll(true);
        
        Collection<Playable> toSelect = new ArrayList<Playable>();
        allPlayables.forEach((playable)-> {
           toSelect.add(playable); 
        });
        
        SelectCommand select = new SelectCommand(toSelect, currentlySelected, true, false);
        
        commandManager.add(select);
        select.execute();
        
        undoAction.setDisable(false);
        redoAction.setDisable(true); 
        
        deleteAction.setDisable(false); 
        
        selectAllAction.setDisable(true); 
        
        saveSelected = getSelectedPlayables();
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
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
        Collection<Playable> selectedPlayables = getSelectedPlayables();
    	
   
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
        group.getRectangle().setOnContextMenuRequested((ContextMenuEvent rightClickEvent)->{
                handleRightClick(rightClickEvent,group); 
        });
        
        groupAction.setDisable(true);
        ungroupAction.setDisable(false);
        isSaved = false;
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
    }
    
    /**
     * Gets rid of all the top-level selected groups. Any nested or unselected
     * gestures remain.
     */
    public void unGroup() {
    	ArrayList<Playable> selectedTopLevelGestures = getTopLevelGestures();
    	for (Playable g : selectedTopLevelGestures) {
    		notePane.getChildren().remove(g.getRectangle());
    		allPlayables.remove(g);
    	}
        Collection<Playable> topLevelGestures = new ArrayList<>();
        Boolean isIn;
        for (Playable g : allPlayables) {
            isIn = false;
            for (Playable h : allPlayables) {
                if (h.getContents().contains(g)) {
                    isIn = true;
                }
            }
            if (!isIn) {
                topLevelGestures.add(g);
            }
    	}
        
        ungroupAction.setDisable(false); 
        if(topLevelGestures.size() == 0){
            ungroupAction.setDisable(true);
        }
        groupAction.setDisable(false);
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
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
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
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
        isSaved = false; 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
    }
    
    /**
     * Handles the copying action.
     * @param event, the copy MenuItem event
     */
    @FXML
    private void handleCopy(ActionEvent event){
        copy(); 
    }
    
    /**
     * Uses the clipboardWrapper to add the selectedPlayables to the clipboard.
     */
    private void copy(){
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        clipboardWrapper.pushToClipboard(selectedPlayables); 
        
        pasteAction.setDisable(false); 
    }
    
    /**
     * Handles the cutting action.
     * @param event, the cut MenuItem event
     */
    @FXML
    private void handleCut(ActionEvent event){
        cut(); 
    }
    
    /**
     * Uses the clipboardWrapper to add the selected playables to the clipboard
     * and removes them from the current composition.
     */
    private void cut(){
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        clipboardWrapper.pushToClipboard(selectedPlayables);
        
        CutCommand cutCommand = new CutCommand(selectedPlayables); 
        commandManager.add(cutCommand); 
        cutCommand.execute();
        
        isSaved = false; 
        pasteAction.setDisable(false); 
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
    }
    
    /**
     * Handles the paste action.
     * @param event, the paste MenuItem event
     */
    @FXML
    private void handlePaste(ActionEvent event) throws SAXException, IOException{
        paste(); 
    }
    
    /**
     * ???? add javadoc
     */
    private void paste() throws SAXException, IOException{
        Collection<Playable> selectedPlayables = getSelectedPlayables(); 
        for (Playable p : selectedPlayables) {
            p.setSelected(false); 
        }
        Collection<Playable> pastedPlayables = clipboardWrapper.popFromClipboard();  
        
        for (Playable p : pastedPlayables) {
            if(p.isGesture()){
                allPlayables.addAll(p.getContents());
            }
            notePane.getChildren().addAll(p.getNodeList()); 
            addHandlers(p);
        }   
        PasteCommand pasteCommand = new PasteCommand(pastedPlayables); 
        commandManager.add(pasteCommand); 
        pasteCommand.execute();

        isSaved = false; 
        
        saveAction.setDisable(false); 
        saveAsAction.setDisable(false);
    }
  
    
    @FXML
    private void handleQuantize(ActionEvent event) throws SAXException, IOException{
        quantize(); 
    }
    
    private void quantize(){
        allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                double x = n.getRectangle().getX();
                int lowX = (int)(x - (x%25));
                int highX = (int)(lowX + 25);
                double roundX = (double)((x-lowX > highX-x)? highX : lowX);
                n.move(roundX-x, 0);
            }
        }); 
    }
    
    /**
     * Opens a window displaying information about the program.
     * @param event, the about MenuItem event
     */
    @FXML
    private void handleAboutAction(ActionEvent event){
        Alert aboutBox = new Alert(Alert.AlertType.INFORMATION);
        aboutBox.setTitle("About TuneComposer");
        aboutBox.setHeaderText(null);
        aboutBox.setContentText("Welcome to TuneComposer!\n\nClick the canvas to add a note.\nHave fun composing :)\n\nCreated by Angie, Abbey, Colin & Kimberly");
        aboutBox.showAndWait();
    }
 
    /**
     * Opens a new blank composition. If the current composition is not saved, 
     * opens a dialogue to ask if you want to save it and calls save or saveAs as
     * appropriate.
     * @param event, the new MenuItem event 
     */
    @FXML
    private void handleNewAction(ActionEvent event){
        if (!isSaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New");
            alert.setHeaderText("Do you want to save your current composition before opening a new file?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes){
                if (savedFilename.equals("new")){
                    saveAs(); 
                }
                else { //the file has been saved before
                   save(); 
                }
            } else if (result.get() == buttonTypeNo) {
                
            } else {
                return; 
            }
        }
        allPlayables.clear();
        notePane.getChildren().clear();
        playAction.setDisable(true); 
        
        undoAction.setDisable(true); 
        redoAction.setDisable(true); 
        cutAction.setDisable(true); 
        copyAction.setDisable(true); 
        
        groupAction.setDisable(true); 
        deleteAction.setDisable(true); 
        selectAllAction.setDisable(true); 
    }
    
    
    /**
     * Add handlers for pressing, dragging, and releasing the mouse for the given node.
     * @param node 
     */
    private void addHandlers(Playable node){
        if(node.isGesture()){
            for(Playable x : node.getContents()){
                addHandlers(x);
            }
        }
        node.getRectangle().setOnMousePressed((MouseEvent pressedEvent) -> {
            handleNoteClick(pressedEvent,node);
            handleNotePress(pressedEvent,node);
        });

        node.getRectangle().setOnMouseDragged((MouseEvent dragEvent) -> {
            handleNoteDrag(dragEvent);
        });

        node.getRectangle().setOnMouseReleased((MouseEvent releaseEvent) -> {
            handleNoteStopDragging(releaseEvent);
        });
        
        node.getRectangle().setOnContextMenuRequested((ContextMenuEvent rightClickEvent)->{
                handleRightClick(rightClickEvent, node); 
        });
    }
    
    /**
     * Opens a saved composition file. If the current composition is not saved,
     * opens a dialogue asking if you want to save before opening.
     * @param event, the open MenuItem event
     * @throws SAXException
     * @throws IOException 
     */
    @FXML
    private void handleOpenAction(ActionEvent event) throws SAXException, IOException{
        if (!isSaved){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Open");
            alert.setHeaderText("Do you want to save your current composition before opening a saved file?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes){
                if (savedFilename.equals("new")){
                    saveAs(); 
                }
                else{ //file has not been saved before
                   save(); 
                }
            } else if (result.get() == buttonTypeNo) {

            } else {
                return; 
            }
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Text Files", "*.txt"));
        try {
            File selectedFile = fileChooser.showOpenDialog(stage);
            allPlayables.clear();
            notePane.getChildren().clear();
            playAction.setDisable(false); 
            Collection<Playable> loadedPlayables = xmlparser.loadFile(selectedFile); 
            allPlayables.addAll(loadedPlayables);
            for (Playable p : loadedPlayables){
                if(p.isGesture()){
                    allPlayables.addAll(p.getContents());
                }
                notePane.getChildren().addAll(p.getNodeList()); 
                addHandlers(p);
            }
        } catch (Exception e) {
            //nothing to do
        } 
    }
    /**
     * Handles the save MenuItem action.
     * @param event, the save MenuItem event
     */
    @FXML
    private void handleSaveAction(ActionEvent event){
        save(); 
    }
    
    /**
     * Saves the current composition. If it has not previously been saved, calls
     * saveAs to name the file. Turns the composition into a string and passes 
     * it to the filesaver.
     */
    private void save(){
        if (savedFilename.equals("new")){
            saveAs(); 
        }
        else{
            Collection<Playable> topLevelPlayables = getTopLevelPlayables(); 
            String noteString = ""; 

            for (Playable p : topLevelPlayables) {
                noteString = noteString + p.toString() + "\n";
            }
            noteString = "<composition> \n" + noteString + "</composition>"; 
            filesaver.saveFile(noteString);
        }
        isSaved = true;
        saveAction.setDisable(true); 
        saveAsAction.setDisable(true);
    }
    
    /**
     * Handles the save as MenuItem action.
     * @param event, the saveAs MenuItem event
     */
    @FXML
    private void handleSaveAsAction(ActionEvent event){
        saveAs();  
    }
    
    /**
     * Saves the current composition. Opens a dialogue to allow the user to name
     * the file whatever they want, it and the composition to the filesaver
     */
    private void saveAs(){
        Collection<Playable> topLevelPlayables = getTopLevelPlayables(); 
        String noteString = ""; 

        for (Playable p : topLevelPlayables) {
            noteString = noteString + p.toString() + "\n";
        }
        noteString = "<composition> \n" + noteString + "</composition>"; 
        
        try {
        	filesaver.newFile(noteString,stage);
        	savedFilename = filesaver.getFilename(); 
        	isSaved = true; 
        } catch (Exception e) {
        	//do nothing
        }

        saveAction.setDisable(true); 
        saveAsAction.setDisable(true);  
    }
    
    /**
     * Initializes FXML. Called automatically.
     * (1) adds 127 gray lines to background
     * (2) initializes the playLine(set to invisible)
     */
    public void initialize() {
         playLinePane.setMouseTransparent(true);
         noteNamesPane.setMouseTransparent(true); 
         playLine = new PlayLine(movingLine);
         
        // Add gray lines to background
        for (int i = 1; i < 128; i++) {
            Line row = new Line(0, 10 * i, 10000, 10 * i);
            row.getStyleClass().add("row-divider");
            background.getChildren().add(row);
            for (int j = 0; j <= 10000; j = j+1000 ){
                NoteName noteName = new NoteName(j,i*10); 
                noteNamesPane.getChildren().add(noteName.getName()); 
            }
        }
        
        for (int i = 1; i < 10000; i++) {
            Line column = new Line(i*50, 0, i*50, 1280);
            column.getStyleClass().add("row-divider");
            background.getChildren().add(column);
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
        
        saveAction.setDisable(true); 
        saveAsAction.setDisable(true); 
        
        pasteAction.setDisable(true); 
        copyAction.setDisable(true); 
        cutAction.setDisable(true); 
        
        savedFilename = "new"; 
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
        stage = primaryStage; 
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
