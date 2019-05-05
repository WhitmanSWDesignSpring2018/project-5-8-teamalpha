/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author taylorkm
 */
public class FileSaver {
    
    Scanner scanner = new Scanner(System.in); 
    
    String filename; 
    /**
     * Creates and writes to a new file.
     * @param textfile, the String to be written to the file
     * @param filename, the name of the new file
     */
    public void newFile(String textfile, Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        try{
            File selectedFile = fileChooser.showSaveDialog(stage);
            filename = selectedFile.getAbsolutePath(); 
        //try{
          
            FileWriter fw = new FileWriter(selectedFile); 
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(textfile); 
            bw.close();
        }
        catch (IOException e){
            
        }
    }
    
    public void saveFile(String textfile){
        File file = new File(filename); 
        try{
            file.createNewFile(); 
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile()); 
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(textfile); 
            bw.close();
        }
        catch (IOException e){
            
        }
        
    }
    
    public String getFilename(){
        return filename; 
    }
    
}
