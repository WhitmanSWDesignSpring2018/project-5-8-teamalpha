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
   
        public Instrument toInstrument(String instrumentString) {
            switch(instrumentString) {
                case "piano":         return PIANO;
                case"harpsichord":    return HARPSICHORD;
                case "marimba":       return MARIMBA;
                case "church-organ":  return CHURCH_ORGAN;
                case "accordion":     return ACCORDION;
                case "guitar":        return GUITAR;
                case "violin":        return VIOLIN;
                case "french-horn":   return FRENCH_HORN;
                default: throw new IllegalArgumentException();
            }
        }
}
