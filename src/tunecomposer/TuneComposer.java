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
    public static CommandManager commandManager = new CommandManager();
    
    public static ButtonController buttoncontroller = new ButtonController();
    
    public static ClickController clickcontroller;
    
    public static NoteGroup notegroup;

    /**
     * The set of all notes, to be played later.
     */    
    public static Set<Playable> allPlayables;
    
    /**
     * The background of the application.
     */
    @FXML
    private Group background;

    /**
     * The pane in which notes are constructed.
     */
    @FXML
    public static Pane notePane;

    /**
     * An area for click-and-drag note selection.
     */
    private SelectionArea selection;

    /**
     * Rectangle used in click-and-drag note selection
     */
    @FXML
    private Rectangle selectRect;
            
    /**
        * A group of sidebar radio buttons for selecting an instrument.
        */
    @FXML
    private static ToggleGroup instrumentToggle;

    /**
     * Constructor initializes Note sets
     */
    public TuneComposer() {
        allPlayables = new HashSet();
        clickcontroller = new ClickController(buttoncontroller);
        notegroup = new NoteGroup(clickcontroller);
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
    public static Instrument getInstrument() {
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
     * Initializes FXML. Called automatically.
     * (1) adds 127 gray lines to background
     * (2) initializes the playLine(set to invisible)
     */
    public void initialize() {
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
