/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javax.sound.midi.ShortMessage;
import static tunecomposer.TuneComposer.allPlayables;

/**
 *
 * @author aslettcj
 */
public class ButtonController {
    public static final MidiPlayer PLAYER = new MidiPlayer(100,60);
    
    /**
     * A list of instrument values to associate with MidiPlayer channels
     */
    
    private final int[] timbreList = new int[] {0, 6, 12, 19, 21, 24, 40, 60};
    
    /**
     * The line wrapped by PlayLine.
     */
    @FXML
    private Line movingLine;
    /**
     * The pane in which the play line is constructed and plays.
     */
    @FXML
    private AnchorPane playLinePane;
    // Let mouse events go through to notePane
     /**
     * Plays notes that have been added.
     * Called when the Play button is clicked.
     */
    public PlayLine playLine;
    public ButtonController(){
        playLinePane.setMouseTransparent(true);
        playLine = new PlayLine(movingLine);
    }
    public void startPlaying() {
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
    public void handleStartPlaying(ActionEvent ignored) {
        startPlaying();
    }

    /**
     * Stops playing composition.
     * Called when the Stop button is clicked. Does not remove notes from the
     * screen or from allPlayables.
     */
    public void stopPlaying() {
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
    public void selectAll(boolean selected) {
        allPlayables.forEach((playable) -> {
            playable.setSelected(selected);
        });
    }
}
