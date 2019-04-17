/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * Enumerates the instruments playable in the TuneComposer application
 * @author Ian Hawkins, Ian Stewart, Melissa Kohl, Angie Mead
 */
public enum Instrument {
        PIANO,
        HARPSICHORD,
        MARIMBA,
        CHURCH_ORGAN,
        ACCORDION,
        GUITAR,
        VIOLIN,
        FRENCH_HORN;
        
        /**
        * A group of sidebar radio buttons for selecting an instrument.
        */
        @FXML
        private ToggleGroup instrumentToggle;
        /**
         * Override the built-in method from the Enum class
         * @return Lower case string of instrument name with dashes as spaces
         */
        @Override
        public String toString() {
            switch(this) {
                case PIANO:         return "piano";
                case HARPSICHORD:    return "harpsichord";
                case MARIMBA:       return "marimba";
                case CHURCH_ORGAN:  return "church-organ";
                case ACCORDION:     return "accordion";
                case GUITAR:        return "guitar";
                case VIOLIN:        return "violin";
                case FRENCH_HORN:   return "french-horn";
                default: throw new IllegalArgumentException();
            }
        }
            /**
     * Get the instrument currently selected in the sidebar.
     * @return the selected instrument
     */
    private Instrument getInstrument() {
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
}
