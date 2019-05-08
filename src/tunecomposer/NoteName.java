
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 *
 * @author meadap
 */
public class NoteName {
    private static final int RECTHEIGHT = 10;
    private static final int NOTEMOD = 12;
//    public static final int C = 0;
//    public static final int Db = 1;
//    public static final int D = 2;
//    public static final int Eb = 3;
//    public static final int E = 4;
//    public static final int F = 5;
//    public static final int Gb = 6;
//    public static final int G = 7;
//    public static final int Ab = 8;
//    public static final int A = 9;
//    public static final int Bb = 10;
//    public static final int B = 11;
    
    private final Text noteName;
    private int pitchClass;
    
    public NoteName(double x, double y){
        pitchClass = (int)((129-(y/10)) % NOTEMOD);
        String name = new String();
        switch(pitchClass){
            case 0: name = "C";
                    break;
            case 1: name = "C#";
                    break;
            case 2: name = "D";
                    break;
            case 3: name = "D#";
                    break;
            case 4: name = "E";
                    break;
            case 5: name = "F";
                    break;
            case 6: name = "F#";
                    break;
            case 7: name = "G";
                    break;
            case 8: name = "G#";
                    break;
            case 9: name = "A";
                    break;
            case 10: name = "A#";
                    break;
            case 11: name = "B";
                    break;
            default: name = "0";
                    break;
        }
        noteName = new Text(x, y, name);
        noteName.getStyleClass().add("note-text");
    }
    
    public Text getName(){
        return noteName;
    }
}


